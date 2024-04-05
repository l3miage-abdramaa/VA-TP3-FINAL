package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
)
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    void testRequestFindAllByTestCenterEntityCode() {
        TestCenterEntity testCenterEntity1 = TestCenterEntity
                .builder()
                .code(TestCenterCode.NCE)
                .university("Université de Nice")
                .city("Nice")
                .build();
        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2000,5,3))
                .hasExtraTime(true)
                .email("abdra@uga.fr")
                .testCenterEntity(testCenterEntity1)
                .build();
        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2001,5,5))
                .hasExtraTime(false)
                .email("kbg@uga.fr")
                .testCenterEntity(testCenterEntity1)
                .build();
        testCenterRepository.save(testCenterEntity1);
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);



        Set<CandidateEntity> findAllCandidatesByCenterEntity = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.NCE);
        assertThat(findAllCandidatesByCenterEntity).hasSize(2);
    }

    @Test
    void testRequestFindAllByCandidateEvaluationGridEntitiesGradeLessThan() {
        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity
                .builder()
                .grade(15.50)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity1 = CandidateEvaluationGridEntity
                .builder()
                .grade(8.00)
                .build();


        Set<CandidateEvaluationGridEntity> candidateEvaluationGridEntities1 = new HashSet<>();
        candidateEvaluationGridEntities1.add(candidateEvaluationGridEntity);

        Set<CandidateEvaluationGridEntity> candidateEvaluationGridEntities2 = new HashSet<>();
        candidateEvaluationGridEntities2.add(candidateEvaluationGridEntity1);

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2000,5,3))
                .hasExtraTime(true)
                .email("abdra@uga.fr")
                .candidateEvaluationGridEntities(candidateEvaluationGridEntities1)
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.of(2001,3,10))
                .hasExtraTime(false)
                .email("kbg@uga.fr")
                .candidateEvaluationGridEntities(candidateEvaluationGridEntities2)
                .build();

        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);

        candidateEvaluationGridEntity.setCandidateEntity(candidateEntity1);
        candidateEvaluationGridEntity1.setCandidateEntity(candidateEntity2);

        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity1);

        Set<CandidateEntity> findAllByCandidateEvaluationGridEntitiesGradeLessThan = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10);
        assertThat(findAllByCandidateEvaluationGridEntitiesGradeLessThan).hasSize(1);
    }

    @Test
    void testRequestFindAllByHasExtraTimeFalseAndBirthDateBefore() {
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("abdr@uga.fr")
                .birthDate(LocalDate.of(2000,3,5))
                .hasExtraTime(true)
                .build();
        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .email("abdou@uga.fr")
                .birthDate(LocalDate.of(1998,2,1))
                .hasExtraTime(true)
                .build();
        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .email("abd@uga.fr")
                .birthDate(LocalDate.of(2002,8,6))
                .hasExtraTime(true)
                .build();
        CandidateEntity candidateEntity3 = CandidateEntity
                .builder()
                .email("ab@uga.fr")
                .birthDate(LocalDate.of(1997,3,5))
                .hasExtraTime(false)
                .build();
        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);

        Set<CandidateEntity> findAllByHasExtraTime = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2000,2,3));
        assertThat(findAllByHasExtraTime).hasSize(1);
        assertThat(findAllByHasExtraTime.stream().findFirst().get().getBirthDate()).isEqualTo(LocalDate.of(1997,3,5));
        assertThat(findAllByHasExtraTime.stream().findFirst().get().isHasExtraTime()).isEqualTo(false);

    }



}
