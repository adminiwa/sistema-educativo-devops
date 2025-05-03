package com.example.asignaturas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AsignaturaAlreadyExistsException extends RuntimeException {
    
    public AsignaturaAlreadyExistsException(String message) {
        super(message);
    }
}