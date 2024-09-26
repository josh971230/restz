package edu.example.restz.controller.advice;

import edu.example.restz.exception.MemberTaskException;
import edu.example.restz.exception.UploadNotSupportedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class FileControllerAdvice {
    @ExceptionHandler(UploadNotSupportedException.class)
    public ResponseEntity<?> handleException(UploadNotSupportedException e){
        return ResponseEntity.badRequest()
                             .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleException(MaxUploadSizeExceededException e){
        return ResponseEntity.badRequest()
                             .body(Map.of("error", "파일 크기 제한 초과"));
    }
}
