package com.example.matriculas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MatriculaAlreadyExistsException extends RuntimeException {
    
    public MatriculaAlreadyExistsException(String message) {
        super(message);
    }
}