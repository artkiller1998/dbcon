package ru.web_marks.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mark  extends  MongoModels{
    String scale;
    String mrk;
    String descr;
    String color;
    Date date;

    //private scales scale;




//
//    public scales predictScale() {
//        scales prediction = scales.Other;
//        if (mrk.matches("[+-]{1}")) prediction = scales.Symbolic;
//        if (mrk.matches("\\d+%")) prediction = scales.Percentage;
//        if (mrk.matches("(\\b[0-9]{1}\\b)|(\\b10{1}\\b)")) prediction = scales.Grade10;
//        if (mrk.matches("\\b[0-5]{1}\\b")) prediction = scales.Grade5; //(?U)
//        if (mrk.matches("[отл|хор|уд|неуд]{1}")) prediction = scales.Name;
//        return prediction;
//    }

    private void updateMark(String value){
        mrk = value;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return String.format(
                "{ \"id\":\"%s\", \"mark\":\"%s\"," +
                " \"date\":\"%s\", \"scale\":\"%s\", \"descr\":\"%s\", \"color\":\"%s\" }",
                id, mrk, dateFormat.format(date), scale, descr, color);
    }

//    private enum scales {
//        Symbolic, // ["+","-"]
//        Percentage, // [0-100]%
//        Grade5, //  [0-5]
//        Grade10, // [0-10]
//        Name, // ["отл","хор","уд","неуд"];
//        Other // any other type
//    }

}
