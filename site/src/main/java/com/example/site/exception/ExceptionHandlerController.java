package com.example.site.exception;

import com.example.site.dto.ResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ResultDto<?>> notFound(NotFoundException e) {
        ResultDto<?> resultDto = new ResultDto<>();
        resultDto.getErrors().add(e.getMessage());
        return ResponseEntity.status(404).body(resultDto);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ResultDto<?>> illegal(IllegalArgumentException e) {
        ResultDto<?> resultDto = new ResultDto<>();
        resultDto.getErrors().add(e.getMessage());
        return ResponseEntity.badRequest().body(resultDto);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResultDto<?>> exeption(Exception e) {
        ResultDto<?> resultDto = new ResultDto<>();
        resultDto.getErrors().add(e.getMessage());
        return ResponseEntity.internalServerError().body(resultDto);
    }
}
