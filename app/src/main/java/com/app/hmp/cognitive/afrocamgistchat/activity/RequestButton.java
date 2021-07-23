
package com.app.hmp.cognitive.afrocamgistchat.activity;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestButton implements Serializable
{

    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("button_link")
    @Expose
    private String buttonLink;
    @SerializedName("button_type")
    @Expose
    private String buttonType;
    private final static long serialVersionUID = -6553886570092798151L;

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonLink() {
        return buttonLink;
    }

    public void setButtonLink(String buttonLink) {
        this.buttonLink = buttonLink;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

}
