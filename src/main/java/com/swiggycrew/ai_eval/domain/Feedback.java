package com.swiggycrew.ai_eval.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userRating;

    private String opsQuality;

    @Column(columnDefinition = "TEXT")
    private String opsNotes;

    private Integer rephrasingCount;

    private Boolean earlyExit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    @JsonIgnore
    private Conversation conversation;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Annotation> annotations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public String getOpsQuality() {
        return opsQuality;
    }

    public void setOpsQuality(String opsQuality) {
        this.opsQuality = opsQuality;
    }

    public String getOpsNotes() {
        return opsNotes;
    }

    public void setOpsNotes(String opsNotes) {
        this.opsNotes = opsNotes;
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

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
