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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")


public class CandidateControllerTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

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



}
