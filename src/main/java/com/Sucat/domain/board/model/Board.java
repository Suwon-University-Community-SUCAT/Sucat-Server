package com.Sucat.domain.board.model;

import com.Sucat.domain.comment.domain.Comment;
import com.Sucat.domain.image.model.Image;
import com.Sucat.domain.like.model.BoardLike;
import com.Sucat.domain.scrap.model.Scrap;
import com.Sucat.domain.user.model.User;
import com.Sucat.global.common.dao.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String userNickname;

    private String title;

    private String content;

    private int likeCount;

    private int commentCount;

    private int scrapCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();

    // Scrap과의 양방향 관계 설정, Board 삭제 -> 자동으로 Scrap 삭제 -> Board를 스크랩한 사용자들의 ScrapList에서 정보 삭제
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scrapList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BoardLike> likeList = new ArrayList<>();

    @Builder
    public Board(String userNickname, String title, String content, BoardCategory category) {
        this.userNickname = userNickname;
        this.title = title;
        this.content = content;
        this.likeCount = 0; //초기 좋아요 수
        this.commentCount = 0;  //초기 댓글 수
        this.scrapCount = 0;    //초기 스크랩 수
        this.category = category;
    }

    /* 연관관계 메서드
    * 양방향 연관 관계의 정리: 주체가 되는 쪽에서 반대쪽 엔티티의 연관 관계도 설정해주는 것이 좋다.
    *  */
    public void addUser(User user) {
        this.user = user;
        this.userNickname = user.getNickname();
    }

    public void addImage(Image image) {
        this.imageList.add(image);

    }

    public void addAllImage(List<Image> images) {
        this.imageList.addAll(images);
    }

    // 필요하다면 Scrap 리스트에 추가하는 메서드
    public void addScrap(Scrap scrap) {
        scrapList.add(scrap);
        this.scrapCount++;
    }

    public void addLike(BoardLike boardLike) {
        likeList.add(boardLike);
        this.likeCount++;
    }


    /* Using Method */
    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateBoard(String title, String content, List<Image> imageList) {
        this.title = title;
        this.content = content;
        this.imageList.clear();
        this.imageList.addAll(imageList);
    }

    public void decrementScrapCount() {
        this.scrapCount--;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        comment.setBoard(this);
        this.commentCount++;
    }

    public void removeComment(Comment comment) {
        this.commentList.remove(comment);
        this.commentCount--;
    }
}
