package com.eventcatalog.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para toda la aplicación.
 * 
 * TASK 4: Implementar @ControllerAdvice para capturar excepciones
 * TASK 2: Retornar errores 400, 404 y 409 con mensajes claros
 * 
 * Principios SOLID aplicados:
 * - SRP (Single Responsibility): Solo maneja la conversión de excepciones a respuestas HTTP
 * - OCP (Open/Closed): Puede extenderse con nuevos handlers sin modificar los existentes
 * 
 * @RestControllerAdvice combina @ControllerAdvice + @ResponseBody
 * para manejar excepciones y devolver respuestas JSON automáticamente.
 * 
 * @author Event Catalog Team
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de campos (@Valid en DTOs).
     * 
     * Se lanza cuando las validaciones de Jakarta Validation fallan.
     * Retorna HTTP 400 (Bad Request) con detalles de cada campo inválido.
     * 
     * @param ex Excepción de validación
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        // Extraer errores de cada campo
        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.ValidationError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Error de validación: uno o más campos son inválidos")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja violaciones de constraints a nivel de método.
     * 
     * Se lanza cuando hay validaciones en parámetros de métodos.
     * Retorna HTTP 400 (Bad Request).
     * 
     * @param ex Excepción de constraint
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        
        List<ErrorResponse.ValidationError> validationErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> ErrorResponse.ValidationError.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .rejectedValue(violation.getInvalidValue())
                        .build())
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Error de validación en los parámetros")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de eventos duplicados.
     * 
     * TASK 2: Validar duplicados en nombres de eventos
     * Retorna HTTP 409 (Conflict).
     * 
     * @param ex Excepción de evento duplicado
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(DuplicateEventException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEventException(
            DuplicateEventException ex,
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de lugares duplicados.
     * 
     * Retorna HTTP 409 (Conflict).
     * 
     * @param ex Excepción de lugar duplicado
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(DuplicateVenueException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateVenueException(
            DuplicateVenueException ex,
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones cuando no se encuentra un evento.
     * 
     * Retorna HTTP 404 (Not Found).
     * 
     * @param ex Excepción de evento no encontrado
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(
            EventNotFoundException ex,
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando no se encuentra un lugar.
     * 
     * Retorna HTTP 404 (Not Found).
     * 
     * @param ex Excepción de lugar no encontrado
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(VenueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVenueNotFoundException(
            VenueNotFoundException ex,
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja cualquier otra excepción no capturada específicamente.
     * 
     * Retorna HTTP 500 (Internal Server Error).
     * 
     * @param ex Excepción genérica
     * @param request Petición HTTP
     * @return ResponseEntity con detalles del error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Ha ocurrido un error interno en el servidor")
                .path(request.getRequestURI())
                .build();
        
        // Log del error para debugging (en producción)
        ex.printStackTrace();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}