package com.mecamotors.api.exceptions;

import com.mecamotors.api.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Atrapa todas las RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> manejarErroresLogica(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), // Devolveremos 400 (Bad Request)
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Atrapa cualquier otro error inesperado (NullPointer, SQL connection failed, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErroresGenerales(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "Ocurrió un error interno en el servidor. Contacte a soporte.",
                LocalDateTime.now()
        );
        // Loguear el error para análisis (aquí simplemente lo imprimimos en consola)
        ex.printStackTrace();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}