package hu.mik.pte.prog5.survey.entity;

import hu.mik.pte.prog5.survey.auth.AppUser;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "survey_response")
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "respondent_id", nullable = false)
    private AppUser respondent;

    @Column(name = "survey_version", nullable = false)
    private int surveyVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "structure_snapshot", nullable = false, columnDefinition = "jsonb")
    private String structureSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    protected String answers;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SurveyResponse() {}

    public SurveyResponse(Survey survey, AppUser respondent, String answers){
        this.survey = survey;
        this.respondent = respondent;
        this.surveyVersion = survey.getVersion();
        this.structureSnapshot = survey.getStructure();
        this.answers = answers;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        submittedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getStructureSnapshot() {
        return structureSnapshot;
    }

    public void setStructureSnapshot(String structureSnapshot) {
        this.structureSnapshot = structureSnapshot;
    }

    public int getSurveyVersion() {
        return surveyVersion;
    }

    public void setSurveyVersion(int surveyVersion) {
        this.surveyVersion = surveyVersion;
    }

    public AppUser getRespondent() {
        return respondent;
    }

    public void setRespondent(AppUser respondent) {
        this.respondent = respondent;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
