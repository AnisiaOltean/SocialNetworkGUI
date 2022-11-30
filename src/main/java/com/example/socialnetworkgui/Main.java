package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.FriendshipValidator;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repo.db.FriendshipDBRepository;
import com.example.socialnetworkgui.repo.db.UserDbRepo;
import com.example.socialnetworkgui.service.Service;

public class Main {
    public static void main(String[] args) {
        System.out.println("ok");
        Validator<User> validator= new UserValidator();
        //UserFileRepo uRepo= new UserFileRepo("src/users.csv", validator);
        //UserDBRepository uRepo= new UserDBRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", validator);
        UserDbRepo uRepo= new UserDbRepo(validator,"jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");
        Validator<Friendship> valF= new FriendshipValidator();
        //FriendshipFileRepo fRepo= new FriendshipFileRepo("src/friendships.csv", valF);
        FriendshipDBRepository fRepo= new FriendshipDBRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", valF);
        Service service= new Service(uRepo, fRepo);

        System.out.println("Users: ");
        service.printUsers();

        System.out.println("Friendships: ");
        service.printFriendships();
    }
}
