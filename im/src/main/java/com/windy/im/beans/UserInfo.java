package com.windy.im.beans;

import java.io.Serializable;

/**
 * author: wang
 * time: 2015/11/20
 * description:
 *      用户信息
 */
public class UserInfo implements Serializable{
    private String user;
    private String password;

    public UserInfo(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public UserInfo() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
