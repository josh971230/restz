package edu.example.sample.controller;
import edu.example.restz.RestzApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.Arrays;

@SpringBootTest(classes= RestzApplication.class,
               webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTests {
    @Autowired(required = false)
    private TestRestTemplate testRestTemplate;  //REST 방식 테스트 클래스

    @Test
    public void testHellos(){
        String[] result = testRestTemplate.getForObject(
                                "/api/v1/sample/hellos",
                                String[].class);
        System.out.println(Arrays.toString(result));
    }
}
