package com.example.socialnetworkgui.repo.db;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repo.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class MessageDbRepo implements Repository<Long, Message> {

    private String URL;
    private String userName;
    private String password;
    private Validator<Message> messageValidator;

    public MessageDbRepo(String URL, String userName, String password, Validator<Message> messageValidator) {
        this.URL = URL;
        this.userName = userName;
        this.password = password;
        this.messageValidator = messageValidator;
    }


    @Override
    public Message save(Message entity) {
        if(entity.getId()==null) throw new IllegalArgumentException("Id cannot be null!");

        messageValidator.validate(entity);
        Message found= this.findOne(entity.getId());
        String sql= "INSERT INTO messages(id, id1, id2, sentat, text) VALUES (?,?,?,?,?)";

        if(found==null){
            try(Connection connection= DriverManager.getConnection(URL, userName, password);
                PreparedStatement ps= connection.prepareStatement(sql)){
                ps.setLong(1, entity.getId());
                ps.setLong(2, entity.getId1());
                ps.setLong(3, entity.getId2());
                ps.setString(4, entity.getSentAt().format(DATE_TIME_FORMATTER));
                ps.setString(5, entity.getText());
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return found;
    }

    @Override
    public Message delete(Long aLong) {
        if(aLong==null) throw new IllegalArgumentException("Id cannot be null!");
        Message found= this.findOne(aLong);
        if(found!=null){
            String sql= "DELETE FROM messages WHERE id='"+aLong+"'";
            try(Connection connection= DriverManager.getConnection(URL, userName, password);
                PreparedStatement ps= connection.prepareStatement(sql);){
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return found;
    }

    @Override
    public Message findOne(Long aLong) {
        Predicate<Message> p= m-> m.getId().equals(aLong);
        Optional<Message> found= StreamSupport.stream(findAll().spliterator(), false).filter(p).findFirst();
        return found.orElse(null);
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> allM= new HashSet<>();
        try(Connection connection= DriverManager.getConnection(URL, userName, password);
           PreparedStatement ps= connection.prepareStatement("SELECT * FROM messages");
           ResultSet rs= ps.executeQuery();){
            while(rs.next()){
                Long id= rs.getLong("id");
                Long id1= rs.getLong("id1");
                Long id2= rs.getLong("id2");
                LocalDateTime sentAt= LocalDateTime.parse(rs.getString("sentat"), DATE_TIME_FORMATTER);
                String text= rs.getString("text");
                Message m= new Message(id1, id2, sentAt, text);
                m.setId(id);

                allM.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allM;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
