package ru.battlefield.my.controller;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.battlefield.my.domain.PlayerProfile;
import ru.battlefield.my.repository.PlayerProfilesRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
        System.out.println("getAllUsers");
        return (List<PlayerProfile>) playerProfilesRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkPerson")
    public PersonCheckResponse checkPerson(@RequestBody PersonCheckRequest person)throws Exception{
        PlayerProfile player = playerProfilesRepository.findByNickName(person.getLoggin());
        if(player==null){
            return PersonCheckResponse.LogginIsIncorrect;
        }
        if(!Password.check(person.getPass(),player.getHashPass())){
            return PersonCheckResponse.PasswordIsIncorrect;
        }

        HttpURLConnection  connection = null;
        String jsonResponse = null;
        try{
            connection = (HttpURLConnection) new URL("http://api.bf3stats.com/pc/player/").openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            String bodyParametrs = "opt=clear,nextranks,global,rank,ranking,scores,equipment,equipmentName,equipmentInfo,specializations,weapons,weaponsName,weaponsInfo,weaponsUnlocks\n&player="+person.getLoggin();
            byte[] postData = bodyParametrs.getBytes(StandardCharsets.UTF_8);
            connection.getOutputStream().write(postData);
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                jsonResponse = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);

                System.out.println(jsonResponse);
            }else{
                System.out.println("Request is failed");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(jsonResponse);
        // получаем название города, для которого смотрим погоду
        System.out.println(weatherJsonObject.get("plat"));
//        JSONObject weather = (JSONObject) weatherJsonObject.get("stats");
//        JSONArray weatherArray = (JSONArray) weatherJsonObject.get("weapons");
//        // достаем из массива первый элемент
//        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        return PersonCheckResponse.AllIsCorrect;
    }


    /**Just test method*/
    @RequestMapping(method = RequestMethod.GET, value = "/getString")
    public String getString()throws Exception{
        System.out.println("adding person");
        playerProfilesRepository.save(new PlayerProfile(0,"pasha", Password.getSaltedHash("pass"),1,2,3,4,5,6,7,8,9,null,null,null));
        return "String";
    }
}
