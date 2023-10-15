package org.datapool.dto.commons.dto.commons;

public class InternalApiRequest<V>{
    private V result;
    private boolean success;
    private String message = "";

    public String getMessage() {
        return message;
    }

    public InternalApiRequest<V> setMessage(String message) {
        this.message = message;
        return this;
    }

    public V getResult() {
        return result;
    }

    public InternalApiRequest<V> setResult(V result) {
        this.result = result;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public InternalApiRequest<V> setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
