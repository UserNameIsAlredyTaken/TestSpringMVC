package ru.battlefield.my.controller;

import org.apache.commons.io.IOUtils;


import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.battlefield.my.domain.PlayerProfile;
import ru.battlefield.my.domain.Weapon;
import ru.battlefield.my.domain.WeaponKills;
import ru.battlefield.my.repository.PlayerProfilesRepository;
import ru.battlefield.my.repository.WeaponKillsRepository;
import ru.battlefield.my.repository.WeaponRepository;

import javax.swing.text.html.HTMLDocument;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by danil on 16.01.2018.
 */
@RestController
public class MainController {

    @Autowired
    private PlayerProfilesRepository playerProfilesRepository;

    @Autowired
    private WeaponKillsRepository weaponKillsRepository;

    @Autowired
    private WeaponRepository weaponRepository;

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

        PlayerProfile playerProfile = playerProfilesRepository.findByNickName(person.getLoggin());
        JSONObject jsonObject = new JSONObject(jsonResponse);
        playerProfile.setNickName(jsonObject.getString("name"));
        jsonObject = jsonObject.getJSONObject("stats");
        playerProfile.setKills          (jsonObject.getJSONObject("global") .getInt("kills"));
        playerProfile.setDeaths         (jsonObject.getJSONObject("global") .getInt("deaths"));
        playerProfile.setWins           (jsonObject.getJSONObject("global") .getInt("wins"));
        playerProfile.setLosses         (jsonObject.getJSONObject("global") .getInt("losses"));
        playerProfile.setLvl            (jsonObject.getJSONObject("rank")   .getInt("nr"));
        playerProfile.setAssaultPoints  (jsonObject.getJSONObject("scores") .getInt("assault"));
        playerProfile.setEngineerPoints (jsonObject.getJSONObject("scores") .getInt("engineer"));
        playerProfile.setReconPoints    (jsonObject.getJSONObject("scores") .getInt("recon"));
        playerProfile.setSupportPoints  (jsonObject.getJSONObject("scores") .getInt("support"));
        playerProfile.setTotalScore     (jsonObject.getJSONObject("scores") .getInt("score"));
        playerProfile.setScoreForThisLvl(jsonObject.getJSONObject("rank")   .getInt("score"));
        playerProfile.setScoreForNextLvl(jsonObject.getJSONArray("nextranks").getJSONObject(0).getInt("score"));



        jsonObject = jsonObject.getJSONObject("weapons");
        jsonObject = jsonObject.getJSONObject("smMP7");

        Weapon weapon = weaponRepository.findByName(jsonObject.getString("name"));
        WeaponKills weaponKills = weaponKillsRepository.findByWeaponAndPlayerProfile(weapon,playerProfile);

        weaponKills.setCountOfKills(jsonObject.getInt("kills"));

        weaponKillsRepository.save(weaponKills);

        playerProfilesRepository.save(playerProfile);

        return PersonCheckResponse.AllIsCorrect;
    }


    /**Just test method*/
    @RequestMapping(method = RequestMethod.GET, value = "/fullDB")
    public String getString()throws Exception{

        PlayerProfile newPlayer = new PlayerProfile();
        newPlayer.setNickName("Rango38");
        newPlayer.setHashPass(Password.getSaltedHash("pass"));
        playerProfilesRepository.save(newPlayer);

        Weapon newWeapon = new Weapon();
        newWeapon.setName  ("MP7");
        newWeapon.setRpm   (950);
        newWeapon.setRange ("SHORT");
        newWeapon.setAmmunition("20 [4.6x30mm]");
        newWeapon.setCategory("Sub machine guns");
        newWeapon.setType("general");
        weaponRepository.save(newWeapon);

        WeaponKills weaponKills = new WeaponKills();
        weaponKills.setWeapon(newWeapon);
        weaponKills.setPlayerProfile(newPlayer);
        weaponKills.setCountOfKills(0);
        weaponKillsRepository.save(weaponKills);
        return "is full";
    }
}
