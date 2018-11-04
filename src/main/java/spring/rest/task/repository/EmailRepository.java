package spring.rest.task.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmailRepository {
    @Value("${email.filename}")
    private String filename;
    private List<SimpleMailMessage> emailList;

    public EmailRepository(){
        this.emailList = new ArrayList<>();
    }
    public List<SimpleMailMessage> findAll(){
        if(!Files.exists(Paths.get(String.valueOf(filename)))){
            try {
                Files.createFile(Paths.get(String.valueOf(filename)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(String.valueOf(filename)))) {
            emailList = (List<SimpleMailMessage>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return emailList;
    }
    public void deleteAll(){
        emailList.clear();
        save();
    }
    public void delete(SimpleMailMessage simpleMailMessage){
        emailList.remove(simpleMailMessage);
        save();
    }
    public void save(){
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(String.valueOf(filename)))) {
            ous.writeObject(emailList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void save(List<SimpleMailMessage> simpleMailMessages){
        emailList = simpleMailMessages;
        save();
    }
    public void save(SimpleMailMessage simpleMailMessage){
        emailList.add(simpleMailMessage);
        save();
    }
}
