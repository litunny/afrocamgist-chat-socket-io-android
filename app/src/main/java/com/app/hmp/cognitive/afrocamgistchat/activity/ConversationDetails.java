
package com.app.hmp.cognitive.afrocamgistchat.activity;

import java.io.Serializable;
import java.util.ArrayList;
import com.app.hmp.cognitive.afrocamgistchat.model.Conversation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationDetails implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<Conversation> conversationList = null;
    private final static long serialVersionUID = 1751519301770627554L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ArrayList<Conversation> getConversationList() {
        return conversationList;
    }

    public void setConversationList(ArrayList<Conversation> conversationList) {
        this.conversationList = conversationList;
    }
}