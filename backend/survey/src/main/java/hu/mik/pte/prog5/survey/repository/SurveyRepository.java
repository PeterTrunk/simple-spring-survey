package hu.mik.pte.prog5.survey.repository;

import hu.mik.pte.prog5.survey.entity.Survey;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Optional<Survey> findByShareCode(String shareCode);

    boolean existsByShareCode(String shareCode);

    List<Survey> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Survey s WHERE s.shareCode = :code")
    Optional<Survey> findByShareCodeForUpdate(@Param("code") String code);
}
