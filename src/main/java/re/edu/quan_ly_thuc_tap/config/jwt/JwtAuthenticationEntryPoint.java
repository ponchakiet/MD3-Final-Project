package re.edu.quan_ly_thuc_tap.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JsonMapper jsonMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String exceptionDetail = (String) request.getAttribute("exception");
        ApiResponse<?> apiResponse;

        if ("EXPIRED_TOKEN".equals(exceptionDetail)) {
            apiResponse = new ApiResponse<>(false, "Lỗi xác thực Token: Token đã hết hạn", null, null, LocalDateTime.now());
        } else if ("MALFORMED_TOKEN".equals(exceptionDetail) || "UNSUPPORTED_TOKEN".equals(exceptionDetail)) {
            apiResponse = new ApiResponse<>(false, "Lỗi xác thực Token: Định dạng không hợp lệ hoặc chữ ký sai", null, null, LocalDateTime.now());
        } else if ("ILLEGAL_ARGUMENT_TOKEN".equals(exceptionDetail)) {
            apiResponse = new ApiResponse<>(false, "Lỗi xác thực Token: Chuỗi Token trống", null, null, LocalDateTime.now());
        } else if ("MISSING_TOKEN".equals(exceptionDetail)) {
            apiResponse = new ApiResponse<>(false, "Lỗi xác thực Token: Token bị thiếu hoặc không được cung cấp", null, null, LocalDateTime.now());
        } else {
            apiResponse = new ApiResponse<>(false, "Lỗi xác thực Token: " + authException.getMessage(), null, null, LocalDateTime.now());
        }

        jsonMapper.writeValue(response.getOutputStream(),
                apiResponse);
    }
}