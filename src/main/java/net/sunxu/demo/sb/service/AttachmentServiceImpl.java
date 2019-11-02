package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.entity.Attachment;
import net.sunxu.demo.sb.repository.AttachmentRepository;
import net.sunxu.demo.sb.service.event.ArticleDeletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;


    @Override
    public List<Attachment> listArticleAttachments(Long articleId) {
        return attachmentRepository.findAllByArticleId(articleId);
    }

    @Override
    public Attachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId).orElse(null);
    }

    @Override
    public void addAttachment(Attachment attachment) {
        attachmentRepository.save(attachment);
    }

    @Override
    public void updateAttachment(Attachment attachment) {
        attachmentRepository.save(attachment);
    }

    @Override
    public void deleteAttachment(Attachment attachment) {
        attachmentRepository.delete(attachment);
    }
}
