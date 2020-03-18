//package ru.web_marks.web.controllers;
////
////import org.springframework.data.crossstore.ChangeSetPersister;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.ArrayList;
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
////
////
//@RestController
//@RequestMapping("message")
//public class MessageController {
////    private int counter = 4;
////
////    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
////        add(new HashMap<String, String>() {{
////            put("id", "1");
////            put("text", "First message");
////        }});
////        add(new HashMap<String, String>() {{
////            put("id", "2");
////            put("text", "Second message");
////        }});
////        add(new HashMap<String, String>() {{
////            put("id", "3");
////            put("text", "Third message");
////        }});
////    }};
////
//    @GetMapping
//    public List<Map<String, String>> list() {
///       return messages;
////    }
////
//////    @GetMapping("{id}")
//////    public Map<String, String> getOne(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
//////        return getMessage(id);
//////    }
//////
//////    private Map<String, String> getMessage(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
//////        return messages.stream()
//////                .filter(message -> message.get("id").equals(id))
//////                .findFirst()
//////                .orElseThrow(ChangeSetPersister.NotFoundException::new);
//////    }
////
//////    @PostMapping
//////    public Map<String, String> create(@RequestBody Map<String, String> message) {
//////        message.put("id", String.valueOf(counter++));
//////
//////        messages.add(message);
//////
//////        return message;
//////    }
////
//////    @PutMapping("{id}")
//////    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) throws ChangeSetPersister.NotFoundException {
//////        Map<String, String> messageFromDb = getMessage(id);
//////
//////        messageFromDb.putAll(message);
//////        messageFromDb.put("id", id);
//////
//////        return messageFromDb;
//////    }
////
//////    @DeleteMapping("{id}")
//////    public void delete(@PathVariable String id) throws ChangeSetPersister.NotFoundException {
//////        Map<String, String> message = getMessage(id);
//////
//////        messages.remove(message);
//////    }
////}
