package edu.example.restz.controller.advice;

import edu.example.restz.exception.CartTaskException;
import edu.example.restz.exception.EntityNotFoundException;
import edu.example.restz.exception.ProductTaskException;
import edu.example.restz.exception.ReviewTaskException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class APIControllerAdvice {
    @ExceptionHandler(CartTaskException.class)
    public ResponseEntity<Map<String, String>> handleException(CartTaskException e){
        log.info("--- CartTaskException");
        log.info("--- e.getMessage() : " + e.getMessage());

        Map<String, String> errMap = Map.of("error", e.getMessage());
        return ResponseEntity.status(e.getCode()).body(errMap);
    }

    @ExceptionHandler(ReviewTaskException.class)
    public ResponseEntity<Map<String, String>> handleException(ReviewTaskException e){
        log.info("--- ReviewTaskException");
        log.info("--- e.getMessage() : " + e.getMessage());

        Map<String, String> errMap = Map.of("error", e.getMessage());
        return ResponseEntity.status(e.getCode()).body(errMap);
    }

    @ExceptionHandler(ProductTaskException.class)
    public ResponseEntity<Map<String, String>> handleException(ProductTaskException e){
        log.info("--- ProductTaskException");
        log.info("--- e.getClass().getName() : " + e.getClass().getName());
        log.info("--- e.getMessage() : " + e.getMessage());

        Map<String, String> errMap = Map.of("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(errMap);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgsException(MethodArgumentNotValidException e){
        Map<String, Object> errMap = new HashMap<>();
        e.getBindingResult()
         .getFieldErrors().forEach( err -> errMap.put(err.getField(),
                                                      err.getDefaultMessage()));
        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleArgsException(EntityNotFoundException e){
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("message", e.getMessage());

        return new ResponseEntity<>(errMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleArgsException(MethodArgumentTypeMismatchException e){
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("error", "Type Mismatched.");
        errMap.put(e.getName(), e.getValue() + " is NOT VALID VALUE");

        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }
}











