package com.sd.ai.eval.model;

import java.util.List;

public class EvaluationResponse {
    private String evaluationId;
    private String conversationId;
    private Scores scores;
    private ToolEvaluation toolEvaluation;
    private Routing routing;
    private List<IssueDto> issuesDetected;
    private List<SuggestionDto> improvementSuggestions;

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public ToolEvaluation getToolEvaluation() {
        return toolEvaluation;
    }

    public void setToolEvaluation(ToolEvaluation toolEvaluation) {
        this.toolEvaluation = toolEvaluation;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }

    public List<IssueDto> getIssuesDetected() {
        return issuesDetected;
    }

    public void setIssuesDetected(List<IssueDto> issuesDetected) {
        this.issuesDetected = issuesDetected;
    }

    public List<SuggestionDto> getImprovementSuggestions() {
        return improvementSuggestions;
    }

    public void setImprovementSuggestions(List<SuggestionDto> improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }

    public static class Scores {
        private Double overall;
        private Double responseQuality;
        private Double toolAccuracy;
        private Double coherence;

        public Double getOverall() {
            return overall;
        }

        public void setOverall(Double overall) {
            this.overall = overall;
        }

        public Double getResponseQuality() {
            return responseQuality;
        }

        public void setResponseQuality(Double responseQuality) {
            this.responseQuality = responseQuality;
        }

        public Double getToolAccuracy() {
            return toolAccuracy;
        }

        public void setToolAccuracy(Double toolAccuracy) {
            this.toolAccuracy = toolAccuracy;
        }

        public Double getCoherence() {
            return coherence;
        }

        public void setCoherence(Double coherence) {
            this.coherence = coherence;
        }
    }

    public static class ToolEvaluation {
        private Double selectionAccuracy;
        private Double parameterAccuracy;
        private Boolean executionSuccess;

        public Double getSelectionAccuracy() {
            return selectionAccuracy;
        }

        public void setSelectionAccuracy(Double selectionAccuracy) {
            this.selectionAccuracy = selectionAccuracy;
        }

        public Double getParameterAccuracy() {
            return parameterAccuracy;
        }

        public void setParameterAccuracy(Double parameterAccuracy) {
            this.parameterAccuracy = parameterAccuracy;
        }

        public Boolean getExecutionSuccess() {
            return executionSuccess;
        }

        public void setExecutionSuccess(Boolean executionSuccess) {
            this.executionSuccess = executionSuccess;
        }
    }

    public static class Routing {
        private String decision;
        private Double agreement;
        private Double averageConfidence;

        public String getDecision() {
            return decision;
        }

        public void setDecision(String decision) {
            this.decision = decision;
        }

        public Double getAgreement() {
            return agreement;
        }

        public void setAgreement(Double agreement) {
            this.agreement = agreement;
        }

        public Double getAverageConfidence() {
            return averageConfidence;
        }

        public void setAverageConfidence(Double averageConfidence) {
            this.averageConfidence = averageConfidence;
        }
    }

    public static class IssueDto {
        private String type;
        private String severity;
        private String description;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class SuggestionDto {
        private String type;
        private String suggestion;
        private String rationale;
        private Double confidence;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }

        public String getRationale() {
            return rationale;
        }

        public void setRationale(String rationale) {
            this.rationale = rationale;
        }

        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
    }
}
