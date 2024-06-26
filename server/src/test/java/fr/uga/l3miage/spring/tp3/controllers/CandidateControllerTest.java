package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.handlers.CandidatNotFoundHandler;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")


public class CandidateControllerTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Autowired
    private ExamRepository examRepository;

    @SpyBean
    private CandidateComponent candidateComponent;

    @AfterEach
    public void clear(){
        candidateRepository.deleteAll();
    }


    @Test
    void NotFoundCandidateId() {

        // Given
        final HttpHeaders headers = new HttpHeaders();

        final Map<String,Object> urlParams = new HashMap<>();
        urlParams.put("candidateId","le candidat n'existe pas");


        // when
        ResponseEntity<CandidatNotFoundHandler> response = testRestTemplate.exchange("/{candidateId}/average",HttpMethod.GET,new HttpEntity<>(null,headers),CandidatNotFoundHandler.class,urlParams);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


    @Test
    void FoundCandidateIdReturnsAverage() {


        // Given
        final HttpHeaders headers = new HttpHeaders();
        final Map<String, Object> urlParams = new HashMap<>();


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

        urlParams.put("candidateId",candidateEntity.getId());


        ResponseEntity<Double> response = testRestTemplate.exchange(
                "/{candidateId}/average",
                HttpMethod.GET,
                new HttpEntity<>(null,headers),
                Double.class,
                urlParams
        );


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


    }



}
