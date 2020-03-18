/*
    Конкретная реализация записей типа Преподаватель
*/
package ru.web_marks.model;

public class Lector extends MongoModels {

    public Lector(String firstName, String lastName, int lectorAge) {
        super(firstName, lastName);
        //this.lectorAge = lectorAge;
    }
    //int lectorAge;


//    public int getInstanceAge() {
//        return lectorAge;
//    }
//
//    public void setInstanceAge(int lectorAge) {
//        this.lectorAge = lectorAge;
//    }

//    public boolean makeMark(String fname, String lname, String groupid, String mark) {
//        Mark mark = new Mark(fname, lname, groupid, mark);
//    }

    @Override
    public String toString() {
        return String.format(
                "Lector[id=%s]",
                id);
    }
}