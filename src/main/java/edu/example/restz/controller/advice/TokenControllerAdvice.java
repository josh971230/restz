package edu.example.restz.controller.advice;

import edu.example.restz.exception.EntityNotFoundException;
import edu.example.restz.exception.MemberTaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TokenControllerAdvice {
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleException(AuthorizationDeniedException e){
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(errMap);
    }

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<?> handleException(MemberTaskException e){
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("error", e.getMessage());

        return ResponseEntity.status(e.getCode())
                             .body(errMap);
    }
}
