package ru.web_marks.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mark {
    private String mark;
    private Date date;
    private scales scale;

    public Mark(String mark) {
        //super(firstName, lastName, groupNum);
        date = new Date();
        this.mark = mark;
        scale = predictScale();
        //setInstanceMark(this);
    }

    public scales predictScale() {
        scales prediction = scales.Other;
        if (mark.matches("[+-]{1}")) prediction = scales.Symbolic;
        if (mark.matches("\\d+%")) prediction = scales.Percentage;
        if (mark.matches("(\\b[0-9]{1}\\b)|(\\b10{1}\\b)")) prediction = scales.Grade10;
        if (mark.matches("\\b[0-5]{1}\\b")) prediction = scales.Grade5; //(?U)
        if (mark.matches("[отл|хор|уд|неуд]{1}")) prediction = scales.Name;
        return prediction;
    }

    ;

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return String.format(
                "[ class=Mark, mark='%s', date='%s', scale ='%s' ]",
                mark, dateFormat.format(date), scale);
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
