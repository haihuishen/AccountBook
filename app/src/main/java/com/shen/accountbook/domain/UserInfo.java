package com.shen.accountbook.domain;

/**
 * Created by shen on 10/14 0014.
 */
public class UserInfo {

    private String userName;
    private String passWord;
    private int sex;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


}
