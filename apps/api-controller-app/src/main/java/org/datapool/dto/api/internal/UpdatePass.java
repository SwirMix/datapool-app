package org.datapool.dto.api.internal;

public class UpdatePass {
    private String old;
    private String newPass;

    public String getOld() {
        return old;
    }

    public UpdatePass setOld(String old) {
        this.old = old;
        return this;
    }

    public String getNewPass() {
        return newPass;
    }

    public UpdatePass setNewPass(String newPass) {
        this.newPass = newPass;
        return this;
    }
}
