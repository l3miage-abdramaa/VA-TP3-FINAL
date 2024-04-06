package fr.uga.l3miage.spring.tp3.services;


import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionComponent sessionComponent;

    @MockBean
    private ExamComponent examComponent;
}
