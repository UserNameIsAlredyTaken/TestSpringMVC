package ru.battlefield.my.controller;


import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.battlefield.my.domain.*;
import ru.battlefield.my.repository.*;

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

    @RequestMapping(method = RequestMethod.GET, value = "/getAllWeapons/{login}")
    public ArrayList<AllWeaponsResponse> getAllWeapons(@PathVariable String login){
        ArrayList<AllWeaponsResponse> responses = new ArrayList<AllWeaponsResponse>();
        for (Weapon weapon : weaponRepository.findAll()) {
            AllWeaponsResponse response = new AllWeaponsResponse();

            PlayerProfile player = playerProfilesRepository.findByNickName(login);

            response.setKills(weaponKillsRepository.findByWeaponAndPlayerProfile(weapon,player).getCountOfKills());
            response.setAmmunition(weapon.getAmmunition());
            response.setCategory(weapon.getCategory());
            response.setName(weapon.getName());
            response.setRange(weapon.getRange());
            response.setRpm(weapon.getRpm());
            response.setType(weapon.getType());

            responses.add(response);
        }
        return responses;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUpgrades/{weaponsName}")
    public Set<Upgrade> getUpgrades(@PathVariable String weaponsName){
        Weapon weapon = weaponRepository.findByName(weaponsName);
        return weapon.getUpgrades();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllSpecializations")
    public ArrayList<Specialization> getAllSpecializations(){
        return (ArrayList<Specialization>) specializationsRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllGadgets")
    public ArrayList<Gadget> getAllGadgets(){
        return (ArrayList<Gadget>) gadgetsRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getWeaponInfo/{name}")
    public Weapon getWeaponInfo(@PathVariable String name){
        return weaponRepository.findByName(name);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTopWeapon/{login}")
    public TopWeaponResponse getTopWeapon(@PathVariable String login){
        TopWeaponResponse topWeapon = new TopWeaponResponse();

        PlayerProfile playerProfile = playerProfilesRepository.findByNickName(login);
        WeaponKills weaponKills = weaponKillsRepository.findTopByPlayerProfileOrderByCountOfKillsDesc(playerProfile);

        topWeapon.setTopWeaponName(weaponKills.getWeapon().getName());
        topWeapon.setTopWeaponKills(weaponKills.getCountOfKills());

        return topWeapon;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getTop")
    public ArrayList<String> getTop(){
        ArrayList<String> topPlayersNames = new ArrayList<String>();
        for (PlayerProfile playerProfile:playerProfilesRepository.findTop10ByOrderByKillsDesc()) {
            topPlayersNames.add(playerProfile.getNickName());
        }
        return topPlayersNames;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUsers")
    public ArrayList<PlayerProfile> getAllUsers(){
        return (ArrayList<PlayerProfile>)playerProfilesRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUser/{login}")
    public PlayerProfile getUser(@PathVariable String login){
        return playerProfilesRepository.findByNickName(login);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUsersNames")
    public ArrayList<String> getAllUsersNames(){
        ArrayList<String> nameList = new ArrayList<String>();
        for (PlayerProfile user:playerProfilesRepository.findAll()) {
            nameList.add(user.getNickName());
        }
        return nameList;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registerPerson")
    public PersonCheckResponse registerPerson(@RequestBody PersonCheckRequest person)throws Exception{
        PlayerProfile newPlayer = playerProfilesRepository.findByNickName(person.getLogin());
        if(newPlayer!=null){
            return PersonCheckResponse.LoginHasAlreadyBeenRegistered;
        }
        newPlayer = new PlayerProfile();

        String jsonResponse = LoadHelper.getJsonData(person.getLogin());
        JSONObject jsonObject = new JSONObject(jsonResponse);

        try{
            if(jsonObject.getString("status").equals("notfound")){
                return PersonCheckResponse.ThereIsNoSuchPlayer;
            }
        }catch (JSONException statusIsNotString){}

        newPlayer.setNickName       (jsonObject.getString("name"));
        newPlayer.setHashPass(Password.getSaltedHash(person.getPassword()));
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
        try{
            newPlayer.setScoreForNextLvl(jsonObject.getJSONArray("nextranks").getJSONObject(0).getInt("score"));
        }catch(JSONException noNextRank){
            newPlayer.setScoreForNextLvl(0);
        }
        newPlayer.setPrivacy(false);
        playerProfilesRepository.save(newPlayer);

        String weaponsString = jsonResponse.substring(jsonResponse.lastIndexOf("\"weapons\":{")+11,jsonResponse.lastIndexOf("\"mgQBB95\""));
        String[] weapons = weaponsString.split("\"}]},");
        for(int i = 0; i < weapons.length; i++){
            String weaponName = weapons[i].substring(1,weapons[i].indexOf("\":{"));
            Weapon weapon = weaponRepository.findByName(jsonObject.getJSONObject("weapons").getJSONObject(weaponName).getString("name"));

            WeaponKills weaponKills = new WeaponKills();
            weaponKills.setWeapon       (weapon);
            weaponKills.setPlayerProfile(newPlayer);
            weaponKills.setCountOfKills(jsonObject.getJSONObject("weapons").getJSONObject(weaponName).getInt("kills"));

            weaponKillsRepository.save(weaponKills);
        }

        return PersonCheckResponse.PersonWasSuccessfullyRegistered;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/checkPerson")
    public PersonCheckResponse checkPerson(@RequestBody PersonCheckRequest person)throws Exception{
        PlayerProfile player = playerProfilesRepository.findByNickName(person.getLogin());
        if(player==null){
            return PersonCheckResponse.LoginIsIncorrect;
        }
        if(!Password.check(person.getPassword(),player.getHashPass())){
            return PersonCheckResponse.PasswordIsIncorrect;
        }

        PlayerProfile playerProfile = playerProfilesRepository.findByNickName(person.getLogin());

        String jsonResponse = LoadHelper.getJsonData(person.getLogin());
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
        try{
            playerProfile.setScoreForNextLvl(jsonObject.getJSONArray("nextranks").getJSONObject(0).getInt("score"));
        }catch(JSONException noNextRank){
            playerProfile.setScoreForNextLvl(0);
        }
        playerProfilesRepository.save(playerProfile);

        String weaponsString = jsonResponse.substring(jsonResponse.lastIndexOf("\"weapons\":{")+11,jsonResponse.lastIndexOf("\"mgQBB95\""));
        String[] weapons = weaponsString.split("\"}]},");
        for(int i = 0; i < weapons.length; i++){
            String weaponName = weapons[i].substring(1,weapons[i].indexOf("\":{"));
            Weapon weapon = weaponRepository.findByName(jsonObject.getJSONObject("weapons").getJSONObject(weaponName).getString("name"));
            WeaponKills weaponKills = weaponKillsRepository.findByWeaponAndPlayerProfile(weapon,playerProfile);
            weaponKills.setCountOfKills (jsonObject.getJSONObject("weapons").getJSONObject(weaponName).getInt("kills"));
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
        newPlayer.setPrivacy(false);
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
