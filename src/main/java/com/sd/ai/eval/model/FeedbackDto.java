package com.sd.ai.eval.model;

import jakarta.validation.Valid;
import java.util.List;

public class FeedbackDto {
    private Integer userRating;

    @Valid
    private OpsReviewDto opsReview;

    @Valid
    private List<AnnotationDto> annotations;

    private Integer rephrasingCount;

    private Boolean earlyExit;

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public OpsReviewDto getOpsReview() {
        return opsReview;
    }

    public void setOpsReview(OpsReviewDto opsReview) {
        this.opsReview = opsReview;
    }

    public List<AnnotationDto> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationDto> annotations) {
        this.annotations = annotations;
    }

    public Integer getRephrasingCount() {
        return rephrasingCount;
    }

    public void setRephrasingCount(Integer rephrasingCount) {
        this.rephrasingCount = rephrasingCount;
    }

    public Boolean getEarlyExit() {
        return earlyExit;
    }

    public void setEarlyExit(Boolean earlyExit) {
        this.earlyExit = earlyExit;
    }
}
