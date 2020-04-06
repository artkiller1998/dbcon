/*
Здесь можно задать общие свойства, присущие всем объектам БД (Уникальный Id...)
*/
package ru.web_marks.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

// указан каталог для сохранения объекта
@Document(collection = "negdb1")
public class MongoModels {
    // генерация уникального ID ( первичный ключ )
    @Id
    protected String id;
//    protected String firstName;
//    protected String lastName;
    //    final Random random = new Random();
    // создается для каждого объекта, вне зависимости от типа
//    public MongoModels(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
////        this.id = random.nextInt();
//    }
//
//    public String getInstanceFirstName() {
//        return firstName;
//    }
//
//    public void setInstanceFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getInstanceLastName() {
//        return firstName;
//    }
//
//    public void setInstanceLastName(String lastName) {
//        this.lastName = lastName;
//    }

    // форматированный вывод информации о объекте
    @Override
    public String toString() {
        return String.format(
//                "Instance[id=%s, firstName='%s', lastName='%s']",
                "[id=%s]",
                id
//                , firstName, lastName
        );
    }

}
