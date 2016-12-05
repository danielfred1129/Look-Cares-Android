package thelookcompany.lookcares.datamodel;

/**
 * Created by buddy on 12/6/2016.
 */


import java.io.Serializable;

public class UserObject implements Serializable {
    private String token;
    private String userKey;
    private String userName;
    private String userPass;
    private String clientKey;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getClientKey() {
        return clientKey;
    }
    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }
}