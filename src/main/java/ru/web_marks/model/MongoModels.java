/*
Здесь можно задать общие свойства, присущие всем объектам БД (Уникальный Id...)
*/
package ru.web_marks.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

// указан каталог для сохранения объекта
@Document(collection = "ttt")
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
