package ru.battlefield.my.controller;

import org.apache.commons.io.IOUtils;


import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.battlefield.my.domain.*;
import ru.battlefield.my.repository.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


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

    @Autowired
    private UpgradesRepository upgradesRepository;

    @Autowired
    private SpecializationsRepository specializationsRepository;

    @Autowired
    private GadgetsRepository gadgetsRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUsers")
    public List<PlayerProfile> getAllUsers(){
        System.out.println("getAllUsers");
        return (List<PlayerProfile>) playerProfilesRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registerPerson")
    public PersonCheckResponse registerPerson(@RequestBody PersonCheckRequest person)throws Exception{
        PlayerProfile newPlayer = playerProfilesRepository.findByNickName(person.getLoggin());
        if(newPlayer!=null){
            return PersonCheckResponse.LogginHasAlreadyBeenRegistered;
        }
        newPlayer = new PlayerProfile();

        String jsonResponse = LoadHelper.getJsonData(person.getLoggin());
        JSONObject jsonObject = new JSONObject(jsonResponse);

        newPlayer.setNickName       (jsonObject.getString("name"));
        newPlayer.setHashPass(Password.getSaltedHash(person.getPass()));
        jsonObject = jsonObject.getJSONObject("stats");
        newPlayer.setKills          (jsonObject.getJSONObject("global") .getInt("kills"));
        newPlayer.setDeaths         (jsonObject.getJSONObject("global") .getInt("deaths"));
        newPlayer.setWins           (jsonObject.getJSONObject("global") .getInt("wins"));
        newPlayer.setLosses         (jsonObject.getJSONObject("global") .getInt("losses"));
        newPlayer.setLvl            (jsonObject.getJSONObject("rank")   .getInt("nr"));
        newPlayer.setAssaultPoints  (jsonObject.getJSONObject("scores") .getInt("assault"));
        newPlayer.setEngineerPoints (jsonObject.getJSONObject("scores") .getInt("engineer"));
        newPlayer.setReconPoints    (jsonObject.getJSONObject("scores") .getInt("recon"));
        newPlayer.setSupportPoints  (jsonObject.getJSONObject("scores") .getInt("support"));
        newPlayer.setTotalScore     (jsonObject.getJSONObject("scores") .getInt("score"));
        newPlayer.setScoreForThisLvl(jsonObject.getJSONObject("rank")   .getInt("score"));
        newPlayer.setScoreForNextLvl(jsonObject.getJSONArray("nextranks").getJSONObject(0).getInt("score"));
        playerProfilesRepository.save(newPlayer);

        return PersonCheckResponse.PersonWasSuccsessfullyRegistered;
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

        PlayerProfile playerProfile = playerProfilesRepository.findByNickName(person.getLoggin());

        String jsonResponse = LoadHelper.getJsonData(person.getLoggin());
        JSONObject jsonObject = new JSONObject(jsonResponse);

        playerProfile.setNickName       (jsonObject.getString("name"));
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
        playerProfilesRepository.save(playerProfile);

        String weaponsString = jsonResponse.substring(jsonResponse.lastIndexOf("\"weapons\":{")+11,jsonResponse.lastIndexOf("\"mgQBB95\""));
        String[] weapons = weaponsString.split("\"}]},");
        for(int i = 0; i < weapons.length; i++){
            String weaponName = weapons[i].substring(1,weapons[i].indexOf("\":{"));
            Weapon weapon = weaponRepository.findByName(jsonObject.getJSONObject("weapons").getJSONObject(weaponName).getString("name"));
            WeaponKills weaponKills = weaponKillsRepository.findByWeaponAndPlayerProfile(weapon,playerProfile);
            weaponKills.setCountOfKills(jsonObject.getJSONObject("weapons").getJSONObject(weaponName).getInt("kills"));
            weaponKillsRepository.save(weaponKills);
        }

        return PersonCheckResponse.AllIsCorrect;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/fullDB")
    public String getString()throws Exception{

        String jsonResponse = LoadHelper.getJsonData("Rango38");

        LoadHelper.loadSpecializations(jsonResponse, specializationsRepository);

        LoadHelper.loadGadgets(jsonResponse,gadgetsRepository);

        LoadHelper.loadWeaponsAndUpgrades(jsonResponse, weaponRepository, upgradesRepository);

        PlayerProfile newPlayer = new PlayerProfile();
        newPlayer.setNickName("Rango38");
        newPlayer.setHashPass(Password.getSaltedHash("pass"));
        playerProfilesRepository.save(newPlayer);

        for (Weapon weapon:weaponRepository.findAll()) {
            WeaponKills weaponKills = new WeaponKills();
            weaponKills.setCountOfKills(0);
            weaponKills.setPlayerProfile(newPlayer);
            weaponKills.setWeapon(weapon);
            weaponKillsRepository.save(weaponKills);
        }

        return "is full";
    }
}
