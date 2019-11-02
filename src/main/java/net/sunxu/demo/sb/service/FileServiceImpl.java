package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.entity.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${fastdfs.server-url}")
    private String fileServerUrl;
    @Value("${fastdfs.config-file}")
    private String configFile;

    private TrackerClient trackerClient;
    private TrackerServer trackerServer;
    private StorageServer storageServer;
    // 键值是文件名, value[0] 是group name, value[1] 是remote file name
    private Map<String, String[]> files = new HashMap<>();

    @PostConstruct
    private void initial() throws IOException, MyException {
        String filePath = new ClassPathResource(configFile).getFile().getAbsolutePath();
        ClientGlobal.init(filePath);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageServer = trackerClient.getStoreStorage(trackerServer);
    }

    @PreDestroy
    private void closeConnection() throws IOException {
        trackerServer.close();
        storageServer.close();
    }


    @Override
    public void upload(FastDFSFile file, byte[] data) {
        logger.info("uplaod file [{}], length [{}]", file.getName(), data.length);
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("author", file.getAuthorId().toString());

        String[] uploadResults = applyFastDFSAction(client ->
                client.upload_file(data, file.getExt(), metaList));
        if (uploadResults != null) {
            logger.info("upload file success, group name [{}], remote file name [{}]",
                    uploadResults[0], uploadResults[1]);
            files.put(file.getName(), uploadResults);
        }
    }

    @Override
    public void changeFileInfo(FastDFSFile newFileInfo) {

    }

    @Override
    public byte[] downFile(Long id) {
        return new byte[0];
    }

    @Override
    public void deleteFile(Long id) {

    }


    @FunctionalInterface
    private interface FastDFSAction<T> {
        T apply(StorageClient client) throws IOException, MyException;
    }

    private <T> T applyFastDFSAction(FastDFSAction<T> action) {
        StorageClient client = new StorageClient(trackerServer, storageServer);
        long startTime = System.currentTimeMillis();
        try {
            return action.apply(client);
        } catch (IOException e) {
            throw new RuntimeException("IO error while execute FastDFS action.", e);
        } catch (MyException e) {
            throw new RuntimeException("Error while execute FastDFS action.", e);
        } finally {
            logger.debug("FastDFS execution time [{}ms]", System.currentTimeMillis() - startTime);
            if (client.getErrorCode() != 0) {
                logger.error("FastDFS error [{}] occurred.", client.getErrorCode());
            }
        }
    }

    public FileInfo getFileInfo(String fileName) {
        String[] uploadResult = getFileUploadResult(fileName);
        return applyFastDFSAction(client ->
                client.get_file_info(uploadResult[0], uploadResult[1]));
    }

    private String[] getFileUploadResult(String fileName) {
        String[] uploadResult = files.get(fileName);
        if (uploadResult == null) {
            throw new RuntimeException("file [" + fileName + "] not found.");
        }
        return uploadResult;
    }

    public byte[] downFile(String fileName) {
        String[] uploadResult = getFileUploadResult(fileName);
        byte[] fileByte = applyFastDFSAction(client ->
                client.download_file(uploadResult[0], uploadResult[1]));
        return fileByte;
    }

    public String getFileUrl(String fileName) {
        String[] uploadResult = getFileUploadResult(fileName);
        return fileServerUrl + uploadResult[0] + "/" + uploadResult[1];
    }

    public void deleteFile(String fileName) {
        String[] uploadResult = getFileUploadResult(fileName);
        int res = applyFastDFSAction(client ->
                client.delete_file(uploadResult[0], uploadResult[1]));
        files.remove(fileName);
        logger.info("delete file successfully " + res);
    }
}
