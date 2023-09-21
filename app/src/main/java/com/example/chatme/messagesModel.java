package com.example.chatme;

public class messagesModel {
    String uId,messages;
    Long timestamp;

    public messagesModel(String uId, String messages, Long timestamp) {
        this.uId = uId;
        this.messages = messages;
        this.timestamp = timestamp;
    }
public messagesModel()//empty con=tructor
{

}
    public messagesModel(String uId, String messages) {
        this.uId = uId;
        this.messages = messages;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
