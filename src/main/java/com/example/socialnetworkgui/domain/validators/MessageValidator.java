package com.example.socialnetworkgui.domain.validators;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        String errors="";
        if(entity.getId()==null) errors+="Id cannot be null!\n";
        if(entity.getId1()==null) errors+="ID of sender cannot be null!\n";
        if(entity.getId2()==null) errors+="ID of receiver cannot be null!\n";
        if(entity.getSentAt()==null) errors+="Date cannot be null!\n";

        if(entity.getId1()!=null&&entity.getId2()!=null) {
            if (entity.getId1().equals(entity.getId2())) errors += "User cannot send message to itself!";
        }
        if(entity.getText().equals("")) errors+="Text cannot be empty!";
        if(errors.length()>0){
            throw new ValidationException(errors);
        }
    }
}
