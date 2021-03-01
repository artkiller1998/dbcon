package ru.web_marks.model;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseFillController {

    class Config {
        public String _id;
        public List<MarkConfig> mark;
    }

    class MarkConfig {
        public String scale;
        public String descr;
    }

    static class Mark {
        public String mark;
        public String color;

        public Mark(String mrk, String clr) {
            this.mark = mrk;
            this.color = clr;
        }
    }

    static class MarkNote {
        public Config config;
        List<Mark> marks;

        public MarkNote(Config config) {
            this.config = config;
            this.marks = new ArrayList<>();
            for (int i = 0; i < config.mark.size(); i++) {
                Mark m = new Mark("", "");
                this.marks.add(m);
            }
        }
    }

    static class markExit {
        public String scale;
        public String mrk;
        public String descr;
        public String color;
        public Date date;
        public String mrk_id;

        markExit(String scale, String mark, String descr, String color) {
            this.scale = scale;
            this.mrk = mark;
            this.descr = descr;
            this.color = color;
            this.date = new Date(0);
            this.mrk_id = new ObjectId().toHexString();
        }
    }

    static class Task {
        List<markExit> marks;
        public String lesson;

        public Task(MarkNote marknote) {
            this.marks = new ArrayList<>();
            this.lesson = marknote.config._id;
            for (int i = 0; i < marknote.marks.size(); i++) {
                markExit me = new markExit(marknote.config.mark.get(i).scale, marknote.marks.get(i).mark, marknote.config.mark.get(i).descr, marknote.marks.get(i).color);
                this.marks.add(me);
            }
        }
    }

    static class Note {
        public List<String> ancestors;
        public String parent;
        List<Task> tasks;

        public Note(List<MarkNote> marknote, String name, String group, String mixed, String filename) {
            this.tasks = new ArrayList<>();

            for (int j = 0; j < marknote.size(); j++) {
                this.tasks.add(new Task(marknote.get(j)));
                this.ancestors = new ArrayList<>();
                this.ancestors.add(name.toUpperCase());
                this.ancestors.add(group.toUpperCase());
                this.ancestors.add(filename);
                this.ancestors.add(filename.substring(0, filename.length() - 2));
            }

            if (mixed.equals("MIXED"))
                this.ancestors.add("MIXED");
            this.parent = name;
        }
    }



    public DatabaseFillController(String config_content, String group_content, String group_name) throws IOException {
        new LoadData(config_content,  group_content, group_name);
    }
}