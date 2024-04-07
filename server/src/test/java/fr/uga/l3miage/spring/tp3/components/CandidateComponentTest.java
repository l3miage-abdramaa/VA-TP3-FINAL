package fr.uga.l3miage.spring.tp3.components;


import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {

    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private ExamRepository examRepository;



    @Test
    void getCandidatIdNotFound() {

        // Given
        when(candidateRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        // then - when

        assertThrows(CandidateNotFoundException.class,
                ()->candidateComponent.getCandidatById(anyLong())
                );


    }



    @Test
    void getCandidateIdFound() {

        // Given

        // création et sauvegarde d'un Candidat avec des notes
        CandidateEvaluationGridEntity candidateEvaluationGrid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(10)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGrid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(6)
                .build();

        ExamEntity examEntity = ExamEntity
                .builder()
                .weight(1)
                .build();

        examRepository.save(examEntity);

        candidateEvaluationGrid1.setExamEntity(examEntity);
        candidateEvaluationGrid2.setExamEntity(examEntity);

        candidateEvaluationGridRepository.save(candidateEvaluationGrid1);
        candidateEvaluationGridRepository.save(candidateEvaluationGrid2);



        Set<CandidateEvaluationGridEntity> candidateEvaluationGridEntities = new HashSet<>();
        candidateEvaluationGridEntities.add(candidateEvaluationGrid1);
        candidateEvaluationGridEntities.add(candidateEvaluationGrid2);

        examEntity.setCandidateEvaluationGridEntities(candidateEvaluationGridEntities);



        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .id(1L)
                .email("abdou@uga.fr")
                .birthDate(LocalDate.of(2001,5,3))
                .candidateEvaluationGridEntities(candidateEvaluationGridEntities)
                .build();

        candidateRepository.save(candidateEntity);

        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));

        // when -then

        assertDoesNotThrow(
                ()->candidateComponent.getCandidatById(candidateEntity.getId())
        );

    }


}
