package ru.battlefield.my.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.battlefield.my.domain.PlayerProfile;
import ru.battlefield.my.repository.PlayerProfilesRepository;

import java.util.List;

/**
 * Created by danil on 16.01.2018.
 */
@RestController
public class MainController {

    @Autowired
    private PlayerProfilesRepository playerProfilesRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUsers")
    public List<PlayerProfile> getAllUsers(){
        System.out.println("Xnjnj");
        return (List<PlayerProfile>) playerProfilesRepository.findAll();
    }



    /**Just test method*/
    @RequestMapping(method = RequestMethod.GET, value = "/getString")
    public String getString(){
        System.out.println("adding person");
        playerProfilesRepository.save(new PlayerProfile(0,"pahsa",1,2,3,4,5,null,null,null,null));
        return "String";
    }
}
