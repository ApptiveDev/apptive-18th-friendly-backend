package apptive.team1.friendly.domain.curating.service;

import apptive.team1.friendly.domain.curating.repository.ContentRepository;
import apptive.team1.friendly.domain.curating.repository.HeartRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HeartServiceTest {

    @Autowired HeartService heartService;
    @Autowired HeartRepository heartRepository;
    @Autowired ContentRepository contentRepository;
}