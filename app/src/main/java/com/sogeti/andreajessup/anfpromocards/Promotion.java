package com.sogeti.andreajessup.anfpromocards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andreajessup on 10/5/15.
 */
public class Promotion  implements Serializable {
    private PromoButton button;
    private String description;
    private String footer;
    private String image;
    private String title;

    public Promotion() {}

    public static Promotion promotionFromJson(JSONObject jsonObject) {
        Promotion promotion = new Promotion();
        try {
            if (jsonObject.has("button")) {
                String buttonData = jsonObject.getString("button");
                Object json = new JSONTokener(buttonData).nextValue();
                if (json instanceof JSONObject)
                    promotion.setButton(PromoButton.buttonFromJson((JSONObject)json));
                else if (json instanceof JSONArray)
                    promotion.setButton(PromoButton.buttonFromJson(((JSONArray) json).getJSONObject(0)));
            }
            if (jsonObject.has("description")) {
                promotion.setDescription(jsonObject.getString("description"));
            }
            if (jsonObject.has("footer")) {
                promotion.setFooter(jsonObject.getString("footer"));
            }
            if (jsonObject.has("image")) {
                promotion.setImage(jsonObject.getString("image"));
            }
            if (jsonObject.has("title")) {
                promotion.setTitle(jsonObject.getString("title"));
            }
        } catch (JSONException je) {
            Log.e("ERROR", "Threw JSONException", je);
            return null;
        }
        return promotion;
    }

    public static ArrayList<Promotion> promotionsFromJson(JSONArray jsonArray) {
        ArrayList<Promotion> promotions = new ArrayList<Promotion>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject promotionJson = null;
            try {
                promotionJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                Log.e("ERROR", "Threw Exception", e);
                continue;
            }

            Promotion promotion = Promotion.promotionFromJson(promotionJson);
            if (promotion != null) {
                promotions.add(promotion);
            }
        }
        return promotions;
    }

    public PromoButton getButton() {
        return this.button;
    }

    public void setButton(PromoButton button) {
        this.button = button;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFooter() {
        return  footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getImage() {
        return  this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}