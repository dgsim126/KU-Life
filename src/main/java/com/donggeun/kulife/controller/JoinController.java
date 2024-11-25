package com.donggeun.kulife.controller;

import com.donggeun.kulife.dto.JoinDTO;
import com.donggeun.kulife.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String joinPost(JoinDTO joinDTO) {

        joinService.joinProcess(joinDTO);

        return "redirect:/login";
    }
}
