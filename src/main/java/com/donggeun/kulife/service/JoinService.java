package com.donggeun.kulife.service;

import com.donggeun.kulife.domain.UserEntity;
import com.donggeun.kulife.dto.JoinDTO;
import com.donggeun.kulife.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 회원가입 기본로직
     * @param joinDTO
     */
    public void joinProcess(JoinDTO joinDTO) {

        // username 중복 확인
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if (isUser) {
            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); // 비밀번호 암호화
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }

}
