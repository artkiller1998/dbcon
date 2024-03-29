/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.web_marks.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.web_marks.model.Mark;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "backup")
public class Backup {

    @Id
    private String id;

    ArrayList<String> ancestors = new ArrayList<String>();

    Map<String, Mark> marks = new HashMap<>();

    Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getAncestors() {
        return ancestors;
    }

    public void setAncestors(ArrayList<String> ancestors) {
        this.ancestors = ancestors;
    }

    public Map<String, Mark> getMarks() {
        return marks;
    }

    public void setMarks(Map<String, Mark> marks) {
        this.marks = marks;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}