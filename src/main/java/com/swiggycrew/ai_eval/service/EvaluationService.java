package com.swiggycrew.ai_eval.service;

import com.swiggycrew.ai_eval.domain.*;
import com.swiggycrew.ai_eval.repository.ConversationRepository;
import com.swiggycrew.ai_eval.repository.EvaluationRepository;
import com.swiggycrew.ai_eval.service.eval.Evaluator;
import com.swiggycrew.ai_eval.service.eval.EvaluatorResult;
import com.swiggycrew.ai_eval.service.suggest.SuggestionService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class EvaluationService {
    private static final double AGREEMENT_THRESHOLD = 0.7;
    private static final double CONFIDENCE_THRESHOLD = 0.6;
    private final List<Evaluator> evaluators;
    private final EvaluationRepository evaluationRepository;
    private final ConversationRepository conversationRepository;
    private final SuggestionService suggestionService;
    private final FeedbackAgreementService agreementService;
    private final EvaluationCacheService cacheService;

    public EvaluationService(List<Evaluator> evaluators,
                             EvaluationRepository evaluationRepository,
                             ConversationRepository conversationRepository,
                             SuggestionService suggestionService,
                             FeedbackAgreementService agreementService,
                             EvaluationCacheService cacheService) {
        this.evaluators = evaluators;
        this.evaluationRepository = evaluationRepository;
        this.conversationRepository = conversationRepository;
        this.suggestionService = suggestionService;
        this.agreementService = agreementService;
        this.cacheService = cacheService;
    }

    @Transactional
    public Evaluation evaluateByConversationId(String conversationId, boolean force) {
        if (!force) {
            Evaluation cached = cacheService.getLatest(conversationId);
            if (cached != null) {
                return cached;
            }
        }

        Conversation conversation = conversationRepository.findWithTurnsByConversationId(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
        Hibernate.initialize(conversation.getTurns());
        conversation.getTurns().forEach(turn -> Hibernate.initialize(turn.getToolCalls()));
        return evaluate(conversation, true);
    }

    @Transactional
    public Evaluation evaluate(Conversation conversation, boolean force) {
        if (!force) {
            Evaluation cached = cacheService.getLatest(conversation.getConversationId());
            if (cached != null) {
                return cached;
            }
        }

        Hibernate.initialize(conversation.getTurns());
        conversation.getTurns().forEach(turn -> Hibernate.initialize(turn.getToolCalls()));

        Evaluation evaluation = new Evaluation();
        evaluation.setEvaluationId("eval_" + UUID.randomUUID());
        evaluation.setConversation(conversation);

        Set<IssueDetected> issues = new LinkedHashSet<>();

        for (Evaluator evaluator : evaluators) {
            EvaluatorResult result = evaluator.evaluate(conversation);
            mergeScores(evaluation, result);
            issues.addAll(result.getIssues());
        }

        evaluation.setOverallScore(calculateOverall(evaluation));
        Double agreement = agreementService.computeAgreement(conversation.getFeedback());
        evaluation.setAnnotatorAgreement(agreement);
        addAnnotationIssues(conversation, issues, agreement);

        for (IssueDetected issue : issues) {
            issue.setEvaluation(evaluation);
        }
        evaluation.setIssues(issues);

        List<Suggestion> suggestions = suggestionService.generateSuggestions(conversation, evaluation);
        Set<Suggestion> suggestionSet = new LinkedHashSet<>(suggestions);
        for (Suggestion suggestion : suggestionSet) {
            suggestion.setEvaluation(evaluation);
        }
        evaluation.setSuggestions(suggestionSet);

        Evaluation saved = evaluationRepository.save(evaluation);
        cacheService.putLatest(conversation.getConversationId(), saved);
        return saved;
    }

    public Evaluation evaluate(Conversation conversation) {
        return evaluate(conversation, false);
    }

    private void mergeScores(Evaluation evaluation, EvaluatorResult result) {
        if (result.getResponseQualityScore() != null) {
            evaluation.setResponseQualityScore(result.getResponseQualityScore());
        }
        if (result.getToolAccuracyScore() != null) {
            evaluation.setToolAccuracyScore(result.getToolAccuracyScore());
        }
        if (result.getCoherenceScore() != null) {
            evaluation.setCoherenceScore(result.getCoherenceScore());
        }
        if (result.getSelectionAccuracy() != null) {
            evaluation.setSelectionAccuracy(result.getSelectionAccuracy());
        }
        if (result.getParameterAccuracy() != null) {
            evaluation.setParameterAccuracy(result.getParameterAccuracy());
        }
        if (result.getExecutionSuccess() != null) {
            evaluation.setExecutionSuccess(result.getExecutionSuccess());
        }
    }

    private double calculateOverall(Evaluation evaluation) {
        double total = 0;
        int count = 0;
        if (evaluation.getResponseQualityScore() != null) {
            total += evaluation.getResponseQualityScore();
            count++;
        }
        if (evaluation.getToolAccuracyScore() != null) {
            total += evaluation.getToolAccuracyScore();
            count++;
        }
        if (evaluation.getCoherenceScore() != null) {
            total += evaluation.getCoherenceScore();
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return Math.round((total / count) * 100.0) / 100.0;
    }

    private void addAnnotationIssues(Conversation conversation, Set<IssueDetected> issues, Double agreement) {
        Double avgConfidence = agreementService.computeAverageConfidence(conversation.getFeedback());
        if (agreement != null && agreement < AGREEMENT_THRESHOLD) {
            IssueDetected issue = new IssueDetected();
            issue.setType("annotator_disagreement");
            issue.setSeverity(IssueSeverity.WARNING);
            issue.setDescription("Annotator agreement " + agreement + " below threshold " + AGREEMENT_THRESHOLD +
                    ". Route to human review.");
            issues.add(issue);
        }
        if (avgConfidence != null && avgConfidence < CONFIDENCE_THRESHOLD) {
            IssueDetected issue = new IssueDetected();
            issue.setType("low_annotation_confidence");
            issue.setSeverity(IssueSeverity.WARNING);
            issue.setDescription("Average annotation confidence " + avgConfidence +
                    " below threshold " + CONFIDENCE_THRESHOLD + ". Prefer human review.");
            issues.add(issue);
        }
    }
}
