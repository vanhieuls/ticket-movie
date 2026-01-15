package com.example.english.Exception;

import com.example.english.Dto.Response.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException (AppException appException){
        ApiResponse apiResponse = ApiResponse.builder()
                .code(appException.getErrorCode().getCode())
                .message(appException.getErrorCode().getMessage())
                .build();
        return ResponseEntity.status(appException.getErrorCode().getHttpStatusCode()).body(apiResponse);
    }
}
