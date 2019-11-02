package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.CommentBO;
import net.sunxu.demo.sb.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    Page<CommentBO> listArticleComments(Long articleId, Pageable option);

    CommentBO getComment(Long commentId);

    void createComment(CommentBO comment);

    void likeComment(Long commentId);

    void dislikeComment(Long commentId);

    void deleteComment(Long commentId);
}
