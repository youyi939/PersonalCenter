package com.example.personalcenter.pojo;

public class Personal {
    public Personal() {
    }

    public Personal(String userName, String nickName, String phonenumber, int sex, String avatar) {
        this.userName = userName;
        this.nickName = nickName;
        this.phonenumber = phonenumber;
        this.sex = sex;
        this.avatar = avatar;
    }

    private String userName;
    private String nickName;
    private String phonenumber;
    private int sex;
    private String avatar;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public String toString() {
        return "Personal{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", sex=" + sex +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
