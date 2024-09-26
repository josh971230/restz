package edu.example.sample.controller;
import edu.example.sample.service.SampleService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
@RequiredArgsConstructor
//4. 문서화 표시 X
@Hidden
@Log4j2
public class SampleController {
    private final SampleService sampleService;      //DI 생성자 인젝션

    @PreAuthorize("hasRole('USER')")
//  @PreAuthorize("hasRole('ROLE_USER')")  //자동으로 ROLE_ 표시됨
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        log.info("--- list() ");
        return ResponseEntity.ok(
                new String[]{ "AAA", "BBB", "CCC" });
    }

    @GetMapping("/hello")
    public String hello(){
        sampleService.sampleMethod();
        return "Hello World!~";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hellos")
    public String[] hellos(){
        return new String[]{ "Hello", "Hi" };
    }
}








