package ru.web_marks.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mark  extends  MongoModels{
    private String mrk;
    private Date date;
    //private scales scale;
    private String scale;
    String color = "default";
    String descr;

    public Mark(String mrk, String descr , String scale) {
        //super(firstName, lastName, groupNum);
        date = new Date();
        this.mrk = mrk;
        this.descr = descr;
        this.scale = scale;
        //scale = predictScale();
        //setInstanceMark(this);
    }

    public scales predictScale() {
        scales prediction = scales.Other;
        if (mrk.matches("[+-]{1}")) prediction = scales.Symbolic;
        if (mrk.matches("\\d+%")) prediction = scales.Percentage;
        if (mrk.matches("(\\b[0-9]{1}\\b)|(\\b10{1}\\b)")) prediction = scales.Grade10;
        if (mrk.matches("\\b[0-5]{1}\\b")) prediction = scales.Grade5; //(?U)
        if (mrk.matches("[отл|хор|уд|неуд]{1}")) prediction = scales.Name;
        return prediction;
    }

    ;

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return String.format(
                "{ \"class\":\"Mark\", \"mark\":\"%s\"," +
                " \"date\":\"%s\", \"scale\":\"%s\", \"descr\":\"%s\", \"color\":\"%s\" }",
                mrk, dateFormat.format(date), scale, descr, color);
    }

    private enum scales {
        Symbolic, // ["+","-"]
        Percentage, // [0-100]%
        Grade5, //  [0-5]
        Grade10, // [0-10]
        Name, // ["отл","хор","уд","неуд"];
        Other // any other type
    }

}
