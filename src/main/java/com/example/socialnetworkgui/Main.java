package com.example.socialnetworkgui;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.exceptions.EntityAlreadyFound;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;
import com.example.socialnetworkgui.domain.validators.*;
import com.example.socialnetworkgui.repo.db.FriendshipDBRepository;
import com.example.socialnetworkgui.repo.db.MessageDbRepo;
import com.example.socialnetworkgui.repo.db.RequestDbRepo;
import com.example.socialnetworkgui.repo.db.UserDbRepo;
import com.example.socialnetworkgui.service.Service;
import com.example.socialnetworkgui.service.ServiceMessage;
import com.example.socialnetworkgui.service.ServiceRequest;
import com.example.socialnetworkgui.utils.events.ChangeEventType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

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

        LocalDateTime l= LocalDateTime.now();
        Request r1= new Request(1L, 2L, l, RequestStatus.SENT);
        Request r2= new Request(1L, 2L, l, RequestStatus.ACCEPTED);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r1.equals(r2));
        System.out.println(r1.hashCode()+" "+r2.hashCode());

        Validator<Request> valR= new RequestValidator();
        RequestDbRepo repoR= new RequestDbRepo("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", valR);
        ServiceRequest serviceRequest= new ServiceRequest(uRepo, fRepo, repoR);
        System.out.println(uRepo.findAll());

//        ServiceRequest sR= new ServiceRequest(uRepo, fRepo, repoR);
//        sR.deleteRequest(1L,2L);
//        sR.getAllRequests().forEach(System.out::println);

        System.out.println("Aici--------------------------");
        Iterable<Request> all= serviceRequest.allRequests();
        System.out.println(all);
        System.out.println(uRepo.findAll());
//
//        all.forEach(System.out::println);
//
//        Predicate<Request> p= r-> r.getId().getSecond().equals(9L);
//        List<Request> allR= StreamSupport.stream(all.spliterator(), false)
//                .filter(p).collect(Collectors.toList());
//
//        System.out.println(allR);
//        //convert Request to UserRequestDTO
//        List<UserRequestDTO> allUR= allR.stream().map(x-> new UserRequestDTO(x.getId().getFirst(), serviceRequest.getWithId(x.getId().getFirst()).getFirstName(),
//                serviceRequest.getWithId(x.getId().getFirst()).getLastName(), x.getSentAt().format(DATE_TIME_FORMATTER), x.getStatus().toString())).collect(Collectors.toList());
//        System.out.println(allUR);
        System.out.println("Mesajeee");

        MessageValidator valM= new MessageValidator();
        MessageDbRepo repoM= new MessageDbRepo("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", valM);
        ServiceMessage serviceMessage= new ServiceMessage(repoM);

        try {
            serviceMessage.addMessage(null, 2L, "");
        }catch (EntityAlreadyFound| ValidationException|IllegalArgumentException | NullPointerException e){
            System.out.println(e.getMessage());
        }

        try{
            serviceMessage.deleteMessage(3L);
            System.out.println("Succesfully deleted message!");
        }catch (EntityNotFound| IllegalArgumentException| ValidationException e){
            System.out.println(e.getMessage());
        }
        serviceMessage.getAllMessages().forEach(System.out::println);

        System.out.println("Gasit:");
        System.out.println(serviceMessage.getWithId(2L));
    }
}
