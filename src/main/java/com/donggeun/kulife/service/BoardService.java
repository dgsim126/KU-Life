package com.donggeun.kulife.service;

import com.donggeun.kulife.domain.BoardEntity;
import com.donggeun.kulife.domain.UserEntity;
import com.donggeun.kulife.dto.BoardRequestDTO;
import com.donggeun.kulife.dto.BoardResponseDTO;
import com.donggeun.kulife.repository.BoardRepository;
import com.donggeun.kulife.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    // 모든 게시글 가져오기
    public List<BoardResponseDTO> getAllPosts() {
        List<BoardEntity> boards = boardRepository.findAll();
        List<BoardResponseDTO> responseDTOs = new ArrayList<>();

        for (BoardEntity board : boards) {
            BoardResponseDTO dto = convertToDto(board);
            responseDTOs.add(dto);
        }

        return responseDTOs;
    }

    // 특정 게시글 가져오기
    public BoardResponseDTO getPostById(int id) {
        Optional<BoardEntity> optionalBoard = boardRepository.findById(id);

        // Optional을 명시적으로 처리
        if (!optionalBoard.isPresent()) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id);
        }

        BoardEntity board = optionalBoard.get();
        return convertToDto(board);
    }

    // 새 게시글 생성
    public BoardResponseDTO createPost(BoardRequestDTO requestDto) {
        // 사용자 검색
        Optional<UserEntity> optionalUser = userRepository.findById(requestDto.getUserId());
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found. ID: " + requestDto.getUserId());
        }
        UserEntity user = optionalUser.get();

        // 게시글 엔티티 생성
        BoardEntity board = new BoardEntity();
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setCreatedAt(String.valueOf(System.currentTimeMillis())); // 간단한 예시
        board.setActive(true);
        board.setUser(user);

        // 저장 후 DTO로 변환
        BoardEntity savedBoard = boardRepository.save(board);
        return convertToDto(savedBoard);
    }

    // 게시글 삭제
    public void deletePost(int id) {
        if (!boardRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제하려는 게시글이 존재하지 않습니다. ID: " + id);
        }
        boardRepository.deleteById(id);
    }

    // Entity를 DTO로 변환
    private BoardResponseDTO convertToDto(BoardEntity board) {
        BoardResponseDTO responseDto = new BoardResponseDTO();
        responseDto.setId(board.getId());
        responseDto.setTitle(board.getTitle());
        responseDto.setContent(board.getContent());
        responseDto.setCreatedAt(board.getCreatedAt());
        responseDto.setActive(board.isActive());
        responseDto.setUsername(board.getUser().getUsername());
        return responseDto;
    }
}
