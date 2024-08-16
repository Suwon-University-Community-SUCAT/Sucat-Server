package com.Sucat.domain.board.controller;

import com.Sucat.domain.board.model.BoardCategory;
import com.Sucat.domain.board.service.BoardService;
import com.Sucat.global.common.code.SuccessCode;
import com.Sucat.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Sucat.domain.board.dto.BoardDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    /* 게시글 작성 */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createBoard(@RequestBody BoardPostRequest boardPostRequestDTO, HttpServletRequest request) {
        boardService.createBoard(boardPostRequestDTO.toEntity(), request);
        return ApiResponse.onSuccess(SuccessCode._CREATED);
    }

    /* (자유, 비밀, 중고장터) 카테고리의 게시글 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getBoardsByCategory(@RequestParam BoardCategory category) {
        BoardListResponseWithHotPost allBoards = boardService.getAllBoards(category);

        return ApiResponse.onSuccess(SuccessCode._OK, allBoards);
    }

    /* 게시글 단일 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getBoard(@PathVariable Long id) {
        BoardDetailResponse board = boardService.getBoard(id);
        return ApiResponse.onSuccess(SuccessCode._OK, board);
    }

    //TODO 게시글 수정 페이지 불러오기 기능 개발

    /* 게시글 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateBoard(@PathVariable Long id,
                                                            @RequestBody BoardUpdateRequest boardUpdateRequest,
                                                            HttpServletRequest request) {
        boardService.updateBoard(id, boardUpdateRequest, request);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        boardService.deleteBoard(id, request);
        return ApiResponse.onSuccess(SuccessCode._OK);
    }
}
