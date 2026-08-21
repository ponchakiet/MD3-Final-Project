package re.edu.quan_ly_thuc_tap.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi: dữ liệu đầu vào không hợp lệ")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }








    // JWT
    // ----- Lỗi Refresh Token hết hạn
    @ExceptionHandler({
            ExpiredJwtException.class,
            MalformedJwtException.class,
            SignatureException.class,
            UnsupportedJwtException.class
    })
    public ResponseEntity<?> handleJwtTokenExceptions(Exception ex) {
        String msg = "Token không hợp lệ";
        if (ex instanceof io.jsonwebtoken.ExpiredJwtException) msg = "Refresh token đã hết hạn";
        else if (ex instanceof io.jsonwebtoken.MalformedJwtException) msg = "Định dạng token bị sai";
        else if (ex instanceof io.jsonwebtoken.security.SignatureException) msg = "Chữ ký token không hợp lệ";

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : " + msg)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi: Thiếu tham số bắt buộc '" + paramName + "' trên URL!")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : Tên đăng nhập hoặc mật khẩu không chính xác")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        String message = "Dữ liệu JSON không hợp lệ.";

        // Kiểm tra nếu lỗi do Role không hợp lệ
        if (ex.getMessage().contains("RoleEnum")) {
            message = "Vai trò không hợp lệ. Chỉ chấp nhận các giá trị: ADMIN, MENTOR, STUDENT.";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.<String>builder()
                        .success(false)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message;

        // Kiểm tra nếu lỗi do convert sang Enum
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] enumConstants = ex.getRequiredType().getEnumConstants();
            String validValues = java.util.Arrays.stream(enumConstants)
                    .map(Object::toString)
                    .collect(java.util.stream.Collectors.joining(", "));
            message = "Giá trị '" + ex.getValue() + "' không hợp lệ cho tham số '"
                    + ex.getName() + "'. Chỉ chấp nhận: " + validValues;
        } else {
            message = "Tham số '" + ex.getName() + "' có giá trị không hợp lệ: " + ex.getValue();
        }

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi: " + message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi: Đường dẫn '" + ex.getResourcePath() + "' không tồn tại!")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi Server: Lỗi xử lý dữ liệu hệ thống")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi : " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Lỗi Server: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
