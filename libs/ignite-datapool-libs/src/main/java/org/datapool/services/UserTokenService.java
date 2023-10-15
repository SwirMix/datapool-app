package org.datapool.services;

import org.apache.ignite.Ignition;
import org.datapool.TokenService;
import org.datapool.dto.DataPoolToken;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.Message;

public class UserTokenService extends AbstractCacheProvider implements TokenService {

    public UserTokenService(String config){
        setIgniteCfg(config);
    }

    @Override
    public InternalApiRequest<DataPoolToken> getToken(String token) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            DataPoolToken data = getTokensCache().get(token);
            result.setResult(data);
            result.setMessage("OK");
            result.setSuccess(true);
        } catch (Exception e){
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setResult(new Message("Internal error"));
        }
        return result;
    }

    @Override
    public InternalApiRequest<DataPoolToken> updateOrCreate(DataPoolToken token) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            getTokensCache().put(token.getToken(), token);
            result.setResult(token);
            result.setMessage("OK");
            result.setSuccess(true);
        } catch (Exception e){
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setResult(new Message("Internal error"));
        }
        return result;
    }

    @Override
    public InternalApiRequest deleteToken(String token) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            getTokensCache().remove(token);
            result.setResult(token);
            result.setMessage("OK");
            result.setSuccess(true);
        } catch (Exception e){
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setResult(new Message("Internal error"));
        }
        return result;
    }
}
