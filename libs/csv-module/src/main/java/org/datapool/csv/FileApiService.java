package org.datapool.csv;

import org.datapool.dto.commons.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.Message;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileApiService implements FileService{
    protected String storage;

    public String getStorage() {
        return storage;
    }

    public FileApiService setStorage(String storage) {
        this.storage = storage;
        return this;
    }

    public FileApiService(String storage){
        this.storage = storage;
    }

    @Override
    public InternalApiRequest<FileInfo> uploadFile(MultipartFile file, String project) {
        InternalApiRequest<FileInfo> result = new InternalApiRequest<>();
        FileInfo fileInfo = new FileInfo(project, file.getOriginalFilename());
        Path storeFile = buildResultPath(file.getOriginalFilename(), project, fileInfo.getCreateDate());
        try (InputStream inputStream = file.getInputStream()){
            if (storeFile!=null){
                Files.copy(inputStream, storeFile);
                fileInfo.setSize(storeFile.toFile().length());
                InternalApiRequest<FileInfo> checkResult = checkCsv(fileInfo);
                if (checkResult.isSuccess()){
                    result.setSuccess(true);
                    result.setResult(checkResult.getResult());
                    result.setMessage("OK");
                } else {
                    result.setResult(fileInfo);
                    result.setSuccess(false);
                    result.setMessage("Invalid format");
                    buildResultPath(fileInfo.getFileName(), fileInfo.getProject(), fileInfo.getCreateDate()).toFile().delete();
                }

            }
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage("IO exception");
            buildResultPath(fileInfo.getFileName(), fileInfo.getProject(), fileInfo.getCreateDate()).toFile().delete();
        }
        return result;
    }

    @Override
    public InternalApiRequest getProjectFiles(String projectId) {
        InternalApiRequest result = new InternalApiRequest();
        File projectFolder = new File(new File(storage), projectId);
        List<FileInfo> csvFiles = new ArrayList<>();
        if (projectFolder.exists()){
            File[] files = projectFolder.listFiles();
            for (File file : files){
                long createTimestamp = Long.parseLong(file.getName());
                if (!file.isFile()){
                    for (File csv : file.listFiles()){
                        InternalApiRequest<FileInfo> request = checkCsv(
                                new FileInfo(
                                        projectId,
                                        csv.getName(),
                                        createTimestamp
                                )
                        );
                        if (request.isSuccess()){
                            csvFiles.add(request.getResult());
                        }
                    }
                }
            }
            result.setResult(csvFiles);
            result.setSuccess(true);
        }
        return result;
    }

    @Override
    public InternalApiRequest deleteFile(FileInfo fileInfo) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            File file = buildResultPath(fileInfo.getFileName(), fileInfo.getProject(), fileInfo.getCreateDate()).toFile();
            if (file.exists()){
                file.delete();
                result.setSuccess(true);
                result.setMessage("OK");
                result.setResult(new Message("OK"));
            } else {
                result.setSuccess(false);
                result.setMessage("Not Found");
                result.setResult(new Message("Not found"));
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error.");
            result.setResult(new Message("Internal error."));
        }

        return result;
    }

    @Override
    public InternalApiRequest getAll() {
        InternalApiRequest result = new InternalApiRequest();
        File storage = new File(getStorage());
        Map<String, List<FileInfo>> files = new HashMap<>();
        for (File projectFolder : storage.listFiles()){
            if (!projectFolder.isFile()){
                List<FileInfo> csvFiles = (List<FileInfo>) getProjectFiles(projectFolder.getName()).getResult();
                files.put(projectFolder.getName(), csvFiles);
            }
        }
        result.setResult(files);
        result.setSuccess(true);
        result.setMessage("OK");
        return result;
    }

    public CsvParcer initParcer(FileInfo fileInfo) throws Exception {
        CsvParcer csvParcer = new CsvParcer(
                buildResultPath(
                        fileInfo.getFileName(),
                        fileInfo.getProject(),
                        fileInfo.getCreateDate()
                )
        );
        return csvParcer;
    }

    public InternalApiRequest checkCsv(FileInfo fileInfo){
        InternalApiRequest result = new InternalApiRequest();
        try {
            CsvParcer csvParcer = new CsvParcer(
                    buildResultPath(
                            fileInfo.getFileName(),
                            fileInfo.getProject(),
                            fileInfo.getCreateDate()
                    )
            );
            fileInfo.setColumns(csvParcer.getColumns());
            fileInfo.setRows(csvParcer.getRownum());
            fileInfo.setCreateDate(fileInfo.getCreateDate());
            fileInfo.setSize(csvParcer.getFilePath().toFile().length());

            result.setMessage("OK");
            result.setSuccess(true);
            result.setResult(fileInfo);
        } catch (Exception e){
            e.printStackTrace();
            result.setMessage("Validation failed");
            result.setSuccess(false);
            result.setResult(fileInfo);
        }
        return result;
    }


    public Path buildResultPath(String fileName, String project, long createDate){
        File resultPath = new File(storage);
        if (!resultPath.exists()){
            resultPath.mkdir();
        }
        resultPath = new File(resultPath, project);
        if (!resultPath.exists()){
            resultPath.mkdir();
        }
        resultPath = new File(resultPath, String.valueOf(createDate));
        if (!resultPath.exists()){
            resultPath.mkdir();
        }
        resultPath = new File(resultPath, fileName);
        return resultPath.toPath();
    }
}
