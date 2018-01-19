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
        JSONObject JsonObject = (JSONObject) JSONValue.parseWithException(jsonResponse);
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setNickName(JsonObject.get("name").toString());
        JsonObject = (JSONObject)JsonObject.get("stats");
        playerProfile.setKills(Integer.parseInt(((JSONObject)JsonObject.get("global")).get("kills").toString()));
        playerProfile.setDeaths(Integer.parseInt(((JSONObject)JsonObject.get("global")).get("deaths").toString()));
        playerProfile.setWins(Integer.parseInt(((JSONObject)JsonObject.get("global")).get("wins").toString()));
        playerProfile.setLosses(Integer.parseInt(((JSONObject)JsonObject.get("global")).get("losses").toString()));
        playerProfile.setLvl(Integer.parseInt(((JSONObject)JsonObject.get("rank")).get("nr").toString()));
        playerProfile.setAssaultPoints(Integer.parseInt(((JSONObject)JsonObject.get("scores")).get("assault").toString()));
        playerProfile.setEngineerPoints(Integer.parseInt(((JSONObject)JsonObject.get("scores")).get("engineer").toString()));
        playerProfile.setReconPoints(Integer.parseInt(((JSONObject)JsonObject.get("scores")).get("recon").toString()));
        playerProfile.setSupportPoints(Integer.parseInt(((JSONObject)JsonObject.get("scores")).get("support").toString()));
        playerProfile.setScoreForThisLvl(Integer.parseInt(((JSONObject)JsonObject.get("rank")).get("score").toString()));
        playerProfile.setScoreForThisLvl(Integer.parseInt(((JSONObject)((JSONArray)JsonObject.get("nextRanks")).get(0)).get("score").toString()));
        playerProfile.setScoreForThisLvl(Integer.parseInt(((JSONObject)JsonObject.get("scores")).get("score").toString()));

        System.out.println(playerProfile.getNickName());
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
        playerProfilesRepository.save(new PlayerProfile(0,"pasha", Password.getSaltedHash("pass"),1,2,3,4,5,6,7,8,9,8,8,8,null,null,null));
        return "String";
    }
}
