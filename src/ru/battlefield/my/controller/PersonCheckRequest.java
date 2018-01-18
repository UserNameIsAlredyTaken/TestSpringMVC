package ru.battlefield.my.controller;

/**
 * Created by danil on 18.01.2018.
 */
public class PersonCheckRequest {
    private String loggin;
    private String pass;

    public String getLoggin() {
        return loggin;
    }

    public void setLoggin(String loggin) {
        this.loggin = loggin;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
