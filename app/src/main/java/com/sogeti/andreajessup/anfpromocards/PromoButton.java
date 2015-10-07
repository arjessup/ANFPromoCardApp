package com.sogeti.andreajessup.anfpromocards;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by andreajessup on 10/5/15.
 */
public class PromoButton  implements Serializable {
    private String target;
    private String title;

    public PromoButton() {}

    public String toString() {
        return "Target: " + target + "\nTitle: " + title;
    }

    public static PromoButton buttonFromJson(JSONObject jsonObject) {
        PromoButton button = new PromoButton();
        try {
            if (jsonObject.has("target")) {
                button.setTarget(jsonObject.getString("target"));
            }
            if (jsonObject.has("title")) {
                button.setTitle(jsonObject.getString("title"));
            }
        } catch (JSONException je) {
            Log.e("ERROR", "Threw JSONException", je);
            return null;
        }
        return button;
    }

    public String getTarget() {
        return  this.target;
    }

    public void  setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
