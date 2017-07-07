package com.money.deep.tstock.model;

/**
 * Created by Administrator on 2016/9/11.
 */
public class UserEncrypted {
    private String Anonymous;
    private String AnonymUserId;
    private String Production;
    private String CleanLicense;

    public String getCleanLicense() {
        return CleanLicense;
    }

    public void setCleanLicense(String cleanLicense) {
        CleanLicense = cleanLicense;
    }

    public String getAnonymous() {
        return Anonymous;
    }

    public void setAnonymous(String anonymous) {
        Anonymous = anonymous;
    }

    public String getAnonymUserId() {
        return AnonymUserId;
    }

    public void setAnonymUserId(String anonymUserId) {
        AnonymUserId = anonymUserId;
    }

    public String getProduction() {
        return Production;
    }

    public void setProduction(String production) {
        Production = production;
    }
}
