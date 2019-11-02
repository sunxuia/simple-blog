package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.entity.FastDFSFile;

public interface FileService {

    void upload(FastDFSFile file, byte[] data);

    void changeFileInfo(FastDFSFile newFileInfo);

    byte[] downFile(Long id);

    void deleteFile(Long id);
}
