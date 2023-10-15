package org.datapool.services;

import org.datapool.dto.api.internal.CreateDataSourceRq;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.DataSource;
import org.datapool.dto.db.Role;
import org.datapool.dto.metadata.Message;
import org.datapool.repository.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Service
public class DataSourceService {
    @Autowired
    private DataSourceRepository dataSourceRepository;
    @Autowired
    private PermissionValidator permissionValidator;


    public InternalApiRequest createDataSource(String userId, CreateDataSourceRq request) {
        InternalApiRequest result = new InternalApiRequest();
        DataSource dataSource;
        switch (request.getProperties().getType()) {
            case POSTGRESQL:
                if (checkJdbcDataSource(request.getProperties().getProperties())) {
                    dataSource = createJdbcDataSource(request);
                    result.setSuccess(true);
                    result.setResult(dataSource);
                    result.setMessage("CREATED");
                } else {
                    result.setSuccess(false);
                    result.setResult(new Message("Invalid POSTGRESQL properties."));
                    result.setMessage("Null props params. Add valid url, username and password.");
                }
                break;
            case CSV:
                result.setSuccess(false);
                result.setResult(new Message("Wait this feature."));
                break;
            default:
                result.setSuccess(false);
                result.setResult(new Message("Invalid datasource type. Try POSTGRESQL or CSV"));
                result.setMessage("Invalid datasource type. Try POSTGRESQL or CSV");
                break;
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public InternalApiRequest deleteDataSource(String userId, String dataSourceId) {
        InternalApiRequest result = new InternalApiRequest();
        Optional<DataSource> queryResult = dataSourceRepository.findById(dataSourceId);
        if (queryResult.isPresent()){
            DataSource dataSource = queryResult.get();
            if (permissionValidator.checkProjectPermission(userId, dataSource.getProjectId(), Role.ADMIN)){
                dataSourceRepository.delete(dataSource);
                result = getDataSources(dataSource.getProjectId(), userId);
            } else {
                result.setMessage("Invalid permission for userId: " + userId);
                result.setSuccess(false);
                result.setResult(new Message("required ADMIN permissions."));
            }
        } else {
            result.setMessage("Invalid dataSourceId");
            result.setSuccess(false);
            result.setResult(new Message("invalid Id"));
        }
        return result;
    }

    public InternalApiRequest getDataSources(String projectId, String userId) {
        InternalApiRequest result = new InternalApiRequest();
        if (permissionValidator.checkProjectPermission(userId, projectId)){
            result.setResult(dataSourceRepository.findByProjectId(projectId));
            result.setSuccess(true);
            result.setMessage("OK");
        } else {
            result.setMessage("Invalid permission for userId: " + userId);
            result.setSuccess(false);
        }
        return result;
    }

    public InternalApiRequest getDataSourceById(String id, String userId) {
        InternalApiRequest result = new InternalApiRequest();
        Optional<DataSource> resultSet = dataSourceRepository.findById(id);
        if (resultSet.isPresent()){
            DataSource dataSource = resultSet.get();
            if (permissionValidator.checkProjectPermission(userId, dataSource.getProjectId())){
                result.setResult(dataSource);
                result.setSuccess(true);
                result.setMessage("OK");
            } else {
                result.setResult(new Message("permission denied"));
                result.setMessage("Invalid permission for userId: " + userId);
                result.setSuccess(false);
            }
        } else {
            result.setResult(new Message("Datasource not found."));
            result.setMessage("Datasource not found.");
            result.setCode(404);
            result.setSuccess(false);
        }
        return result;
    }

    public InternalApiRequest updateDataSource(DataSource request, String userId) {
        InternalApiRequest result = new InternalApiRequest();
        if (permissionValidator.checkProjectPermission(userId, request.getProjectId(), Role.ADMIN)){
            Optional<DataSource> queryResult = dataSourceRepository.findById(request.getId());
            if (queryResult.isPresent()){
                dataSourceRepository.save(request);
                result.setResult(request);
                result.setSuccess(true);
                result.setMessage("OK");
            } else {
                result.setMessage("Invalid dataSourceData");
                result.setSuccess(false);
            }
        } else {
            result.setMessage("Invalid permission for userId: " + userId);
            result.setSuccess(false);
        }
        return result;
    }

    private boolean checkJdbcDataSource(Properties properties){
        if (properties!=null) {
            if (properties.containsKey("url") && properties.containsKey("username") && properties.containsKey("password")){
                return true;
            } else return false;
        }
        return false;
    }

    private DataSource createJdbcDataSource(CreateDataSourceRq request){
        String id = UUID.randomUUID().toString();
        DataSource dataSource = new DataSource();
        dataSource.setProjectId(request.getProjectId());
        dataSource.setName(request.getDataSourceName());
        dataSource.setId(id);
        dataSource.setProperties(request.getProperties());
        dataSourceRepository.save(
                dataSource
        );
        return dataSource;
    }


    protected Role getRole(int role){
        switch (role){
            case 0:
                return Role.ADMIN;
            case 1:
                return Role.READER;
            case 2:
                return Role.TEAMMATE;
        }
        return null;
    }

}
