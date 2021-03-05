/*
Здесь можно задать общие свойства, присущие всем объектам БД (Уникальный Id...)
*/
package ru.web_marks.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


// указан каталог для сохранения объекта

@Document(collection = "default")

public class MongoModels {
    // генерация уникального ID ( первичный ключ )
    @Id
    protected String id;

    // форматированный вывод информации о объекте
    @Override
    public String toString() {
        return String.format(
                "[id=%s]",
                id
        );
    }

}
