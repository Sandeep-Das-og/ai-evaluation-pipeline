package com.sd.ai.eval.service;

import com.sd.ai.eval.domain.Evaluation;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class EvaluationCacheService {
    public static final String LATEST_EVAL_CACHE = "latestEvaluation";

    private final CacheManager cacheManager;
    private final Counter hitCounter;
    private final Counter missCounter;

    public EvaluationCacheService(CacheManager cacheManager, MeterRegistry meterRegistry) {
        this.cacheManager = cacheManager;
        this.hitCounter = Counter.builder("cache.latest_evaluation.hit").register(meterRegistry);
        this.missCounter = Counter.builder("cache.latest_evaluation.miss").register(meterRegistry);
    }

    public Evaluation getLatest(String conversationId) {
        Cache cache = cacheManager.getCache(LATEST_EVAL_CACHE);
        if (cache == null) {
            missCounter.increment();
            return null;
        }
        Evaluation evaluation;
        try {
            evaluation = cache.get(conversationId, Evaluation.class);
        } catch (IllegalStateException ex) {
            // Evict legacy/incorrectly-typed cache entries.
            cache.evict(conversationId);
            missCounter.increment();
            return null;
        }
        if (evaluation == null) {
            missCounter.increment();
        } else {
            hitCounter.increment();
        }
        return evaluation;
    }

    public void putLatest(String conversationId, Evaluation evaluation) {
        Cache cache = cacheManager.getCache(LATEST_EVAL_CACHE);
        if (cache != null) {
            cache.put(conversationId, evaluation);
        }
    }

    public void evict(String conversationId) {
        Cache cache = cacheManager.getCache(LATEST_EVAL_CACHE);
        if (cache != null) {
            cache.evict(conversationId);
        }
    }
}
