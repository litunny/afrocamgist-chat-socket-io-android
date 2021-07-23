
package com.app.hmp.cognitive.afrocamgistchat.activity;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageDetails implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("nextPage")
    @Expose
    private Integer nextPage;
    @SerializedName("data")
    @Expose
    private ArrayList<Message> messages = null;
    private final static long serialVersionUID = 636256232663825264L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
