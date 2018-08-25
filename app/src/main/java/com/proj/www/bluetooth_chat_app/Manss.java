package com.proj.www.bluetooth_chat_app;

public class Manss {

    private String messages, time, myIdNumber;

    public Manss(String messages, String time, String myIdNumber) {
        this.messages = messages;
        this.time = time;
        this.myIdNumber = myIdNumber;
    }

    public String getMessages() {
        return messages;
    }


    public String getMyIdNumber() {
        return myIdNumber;
    }

    public void setMyIdNumber(String myIdNumber) {
        this.myIdNumber = myIdNumber;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
