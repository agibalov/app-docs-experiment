package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DummyTest {
    @Test
    public void itShouldWorkSomehow() {
        RestTemplate restTemplate = new RestTemplate();
        AboutApiDto aboutApiDto = restTemplate.getForObject("http://localhost:8080/api/about", AboutApiDto.class);
        assertEquals("1.0", aboutApiDto.version);
        assertEquals("Some very useful description", aboutApiDto.description);
    }
}
