package fr.uga.l3miage.spring.tp3.components;


import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {

    @Autowired
    private SessionComponent sessionComponent;



    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;

    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    @MockBean
    private ExamRepository examRepository;



    @Test
    void creationSessionNotFound() {

        // Given

        EcosSessionProgrammationEntity ecosSessionProgrammationEntity = new EcosSessionProgrammationEntity();

        ecosSessionProgrammationEntity.setEcosSessionProgrammationStepEntities(new HashSet<>());

        EcosSessionEntity ecosSessionEntity = new EcosSessionEntity();

        ecosSessionEntity.setEcosSessionProgrammationEntity(ecosSessionProgrammationEntity);

        doThrow(
                new RuntimeException("Erreur")

        ).when(ecosSessionRepository).save(any(EcosSessionEntity.class));

        // when-then

        assertThatThrownBy(
                ()->sessionComponent.createSession(ecosSessionEntity)
        ).isInstanceOf(RuntimeException.class).hasMessageContaining("Erreur");
    }

    @Test
    void creationSessionFound()  {

        // Given

        EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity1 = EcosSessionProgrammationStepEntity
                .builder()
                .code("A10")
                .build();

        EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity2 = EcosSessionProgrammationStepEntity
                .builder()
                .code("A12")
                .build();

        ecosSessionProgrammationStepRepository.save(ecosSessionProgrammationStepEntity1);
        ecosSessionProgrammationStepRepository.save(ecosSessionProgrammationStepEntity2);

        Set<EcosSessionProgrammationStepEntity> ecosSessionProgrammationStepEntities = new HashSet<>();
        ecosSessionProgrammationStepEntities.add(ecosSessionProgrammationStepEntity1);
        ecosSessionProgrammationStepEntities.add(ecosSessionProgrammationStepEntity2);

        EcosSessionProgrammationEntity ecosSessionProgrammationEntity = EcosSessionProgrammationEntity
                .builder()
                .ecosSessionProgrammationStepEntities(ecosSessionProgrammationStepEntities)
                .build();

        ExamEntity examEntity = ExamEntity
                .builder()
                .build();

        examRepository.save(examEntity);


        ecosSessionProgrammationRepository.save(ecosSessionProgrammationEntity);

        Set<ExamEntity> examEntities = new HashSet<>();
        examEntities.add(examEntity);

        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .examEntities(examEntities)
                .ecosSessionProgrammationEntity(ecosSessionProgrammationEntity)
                .build();

        ecosSessionRepository.save(ecosSessionEntity);




        when(ecosSessionRepository.save(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);
        when(ecosSessionProgrammationRepository.save(any(EcosSessionProgrammationEntity.class))).thenReturn(ecosSessionProgrammationEntity);
        when(ecosSessionProgrammationStepRepository.save(any(EcosSessionProgrammationStepEntity.class))).thenReturn(ecosSessionProgrammationStepEntity1);
        when(ecosSessionProgrammationStepRepository.save(any(EcosSessionProgrammationStepEntity.class))).thenReturn(ecosSessionProgrammationStepEntity2);



        EcosSessionEntity result = sessionComponent.createSession(ecosSessionEntity);

        // Vérifications
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(ecosSessionEntity);


    }


}