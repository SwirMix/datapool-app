package jpa;

import org.datapool.ApplicationStarter;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.UserProjectPublicData;
import org.datapool.repository.ProjectPermissionRepository;
import org.datapool.services.ProjectServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ApplicationStarter.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.yaml")
public class ProjectServiceTest {

    @Autowired
    private ProjectServiceImpl projectService;
    private String userId = "d8e35c03-0c4e-435c-860b-361aaa6b81dc";
    private String secondUser = "b78432fa-de9d-48de-8fbb-200a559820aa";

    @Test
    public void getUserProjectTest(){
        System.out.println("fin");
    }
}
