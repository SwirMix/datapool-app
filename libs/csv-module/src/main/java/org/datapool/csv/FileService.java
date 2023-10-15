package org.datapool.csv;

import org.datapool.dto.commons.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public InternalApiRequest<FileInfo> uploadFile(MultipartFile file, String project);
    public InternalApiRequest getProjectFiles(String projectId);

    public InternalApiRequest deleteFile(FileInfo fileInfo);
    public InternalApiRequest getAll();

}
