package com.mal.wafaaghareeb.finalmovieapp.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Reviews
{
    private String id;
    private String author;
    private String content;
    public Reviews(String id, String author, String content)
    {
        this.id = id;
        this.author = author;
        this.content = content;
    }
    public Reviews(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.author = trailer.getString("author");
        this.content = trailer.getString("content");
    }
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
