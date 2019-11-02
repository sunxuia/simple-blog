package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.CommentBO;
import net.sunxu.demo.sb.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Page<CommentBO> listArticleComments(Long articleId, Pageable option) {
        return null;
    }

    @Override
    public CommentBO getComment(Long commentId) {
        return null;
    }

    @Override
    public void createComment(CommentBO comment) {

    }

    @Override
    public void likeComment(Long commentId) {

    }

    @Override
    public void dislikeComment(Long commentId) {

    }

    @Override
    public void deleteComment(Long commentId) {

    }
}
