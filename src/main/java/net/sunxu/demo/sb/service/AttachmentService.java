package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.entity.Attachment;

import java.util.List;

public interface AttachmentService {
    List<Attachment> listArticleAttachments(Long articleId);

    Attachment getAttachment(Long attachmentId);

    void addAttachment(Attachment attachment);

    void updateAttachment(Attachment attachment);

    void deleteAttachment(Attachment attachment);

}