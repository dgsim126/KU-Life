package com.donggeun.kulife.controller;

import com.donggeun.kulife.dto.JoinDTO;
import com.donggeun.kulife.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    /**
     * 회원가입
     * POST api/v1/join
     * @param joinDTO
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity<String> joinPost(@RequestBody JoinDTO joinDTO) {
        joinService.joinProcess(joinDTO);
        return ResponseEntity.status(202).body("Join process complete.");
    }
}
