package hu.mik.pte.prog5.survey.repository;

import hu.mik.pte.prog5.survey.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

    Optional<SurveyResponse> findBySurveyIdAndRespondentId(Long surveyId, Long respondentId);

    long countBySurveyId(Long surveyId);

    List<SurveyResponse> findBySurveyIdOrderBySubmittedAtDesc(Long surveyId);

    List<SurveyResponse> findByRespondentIdOrderBySubmittedAtDesc(Long respondentId);
}
