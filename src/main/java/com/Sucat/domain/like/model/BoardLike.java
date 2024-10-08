package com.Sucat.domain.like.model;

import com.Sucat.global.common.dao.BaseEntity;
import com.Sucat.domain.board.model.Board;
import com.Sucat.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    /*연관관계 메서드*/
    @Builder
    public BoardLike(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}
