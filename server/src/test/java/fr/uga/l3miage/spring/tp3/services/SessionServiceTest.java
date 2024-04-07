package fr.uga.l3miage.spring.tp3.services;


import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.HashSet;
import java.util.Set;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionComponent sessionComponent;

    @MockBean
    private ExamComponent examComponent;

    @SpyBean
    private SessionMapper sessionMapper;


    @Test
    void TestCreateSession() {

        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .id(1L)
                .code("A10")
                .build();
        Set<SessionProgrammationStepCreationRequest> steps = new HashSet<>();
        steps.add(sessionProgrammationStepCreationRequest);

        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .id(2L)
                .steps(steps)
                .label("Nour")
                .build();


        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();


    }


}
