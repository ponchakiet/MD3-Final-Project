package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import re.edu.quan_ly_thuc_tap.config.jwt.JwtProvider;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsCustom;
import re.edu.quan_ly_thuc_tap.dto.request.AuthLoginRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AuthRefreshRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AuthResponse;
import re.edu.quan_ly_thuc_tap.dto.response.UserResponse;
import re.edu.quan_ly_thuc_tap.entity.User;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.UserMapper;
import re.edu.quan_ly_thuc_tap.repository.IUserRepository;
import re.edu.quan_ly_thuc_tap.service.IAuthService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse login(AuthLoginRequestDTO request) {

        // 1. Kiểm tra username và password bằng AuthenticationManager
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        //2. Nếu dúng lấy thông tin username
        UserDetailsCustom userDetailsCustom =  (UserDetailsCustom) authentication.getPrincipal();

        // 3. Tạo token
        String jwt = jwtProvider.generateToken(userDetailsCustom);
        String refreshToken = jwtProvider.generateRefreshToken(userDetailsCustom);

        // 4. Tạo response và lưu
        User user = userDetailsCustom.getUser();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .userName(user.getUserName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public UserResponse getMyInfo(User currentUser) {
        return userMapper.toUserResponse(currentUser);
    }

    @Override
    public AuthResponse refreshToken(AuthRefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        // 1. Lấy username từ Refresh Token
        String username = jwtProvider.extractUserName(requestRefreshToken);

        // 2. Tìm user trong DB
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Người dùng không tồn tại trong hệ thống"));

        // 3. Kiểm tra với DB
        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(requestRefreshToken)) {
            throw new BadRequestException("Lỗi: Refresh Token không hợp lệ hoặc đã bị thu hồi!");
        }

        // 4. Tạo token mới và refresh token mới
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
        );
        UserDetailsCustom userDetailsCustom = new UserDetailsCustom(user, authorities);

        String newAccessToken = jwtProvider.generateToken(userDetailsCustom);
        String newRefreshToken = jwtProvider.generateRefreshToken(userDetailsCustom); // mỗi refresh xài 1 lần.

        // 5. Lưu vào DB
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userName(user.getUserName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void logout(User currentUser) {
        currentUser.setRefreshToken(null);
        userRepository.save(currentUser);
    }


}
