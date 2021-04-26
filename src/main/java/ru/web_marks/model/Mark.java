package ru.web_marks.model;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mark {
    String scale;
    String mrk;
    String descr;
    String color;
    Date date;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    String mrk_id;


    private void updateMark(String value){
        mrk = value;
    }

    public String getMrk() {
        return mrk;
    }

    public String getMrk_id() {
        return mrk_id;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return String.format(
                "{ \"mrk_id\":\"%s\", \"mark\":\"%s\"," +
                " \"date\":\"%s\", \"scale\":\"%s\", \"descr\":\"%s\", \"color\":\"%s\" }",
                mrk_id, mrk, dateFormat.format(date), scale, descr, color);
    }

}
