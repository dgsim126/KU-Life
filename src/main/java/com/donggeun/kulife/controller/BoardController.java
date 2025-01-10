package com.donggeun.kulife.controller;

import com.donggeun.kulife.dto.BoardRequestDTO;
import com.donggeun.kulife.dto.BoardResponseDTO;
import com.donggeun.kulife.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 모든 게시글 불러오기
    @GetMapping("/posts")
    public ResponseEntity<List<BoardResponseDTO>> getAllPosts() {
        List<BoardResponseDTO> posts = boardService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 게시글 상세보기
    @GetMapping("/post/{id}")
    public ResponseEntity<BoardResponseDTO> getPostById(@PathVariable int id) {
        try {
            BoardResponseDTO post = boardService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 새로 생성하기
    @PostMapping("/post/create")
    public ResponseEntity<BoardResponseDTO> createPost(@RequestBody BoardRequestDTO requestDto) {
        BoardResponseDTO newPost = boardService.createPost(requestDto);
        return ResponseEntity.ok(newPost);
    }

    // 게시글 삭제하기
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        try {
            boardService.deletePost(id);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
