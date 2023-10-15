package org.datapool;

import org.datapool.dto.DataPoolToken;
import org.datapool.dto.commons.InternalApiRequest;

import java.util.Optional;

public interface TokenService {
    public InternalApiRequest<DataPoolToken> getToken(String token);
    public InternalApiRequest<DataPoolToken> updateOrCreate(DataPoolToken token);
    public InternalApiRequest<DataPoolToken> deleteToken(String token);
}
