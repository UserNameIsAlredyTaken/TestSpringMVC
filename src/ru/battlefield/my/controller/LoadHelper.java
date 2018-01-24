package ru.battlefield.my.controller;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.battlefield.my.domain.*;
import ru.battlefield.my.repository.GadgetsRepository;
import ru.battlefield.my.repository.SpecializationsRepository;
import ru.battlefield.my.repository.UpgradesRepository;
import ru.battlefield.my.repository.WeaponRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by danil on 23.01.2018.
 */
public class LoadHelper {
    static void loadGadgets(String jsonResponse, GadgetsRepository repository){
        JSONObject jsonObject = new JSONObject(jsonResponse);
        jsonObject = jsonObject.getJSONObject("stats").getJSONObject("equipment");
        LinkedHashSet<String> gadgetsNames = new LinkedHashSet<String>();
        gadgetsNames.add("wasRT");
        gadgetsNames.add("waeM67");
        gadgetsNames.add("waeC4");
        gadgetsNames.add("wasDef");
        gadgetsNames.add("waeMort");
        for (String gadgetsName:gadgetsNames) {
            Gadget gadget = new Gadget();
            gadget.setName(jsonObject.getJSONObject(gadgetsName).getString("name"));
            gadget.setClazz(jsonObject.getJSONObject(gadgetsName).getString("kit"));
            try{
                gadget.setNecessaryPoint(jsonObject.getJSONObject(gadgetsName).getJSONObject("unlock").getInt("needed"));
            }catch (JSONException unlockIsNull){
                gadget.setNecessaryPoint(0);
            }
            repository.save(gadget);
        }
    }

    static void loadSpecializations(String jsonResponse, SpecializationsRepository repository){
        JSONObject jsonObject = new JSONObject(jsonResponse);
        jsonObject = jsonObject.getJSONObject("stats").getJSONObject("specializations");
        String specializationsString = jsonResponse.substring(jsonResponse.lastIndexOf("\"specializations\":{")+19,jsonResponse.lastIndexOf("\"grenades2\""));
        String[] specializations = specializationsString.split("\"Rank\"},");
        for(int i = 0; i < specializations.length; i++){
            String specializationsName = specializations[i].substring(1,specializations[i].indexOf("\":{"));

            Specialization specialization = new Specialization();
            specialization.setName(specializationsName);
            specialization.setNecessaryLvl(jsonObject.getJSONObject(specializationsName).getInt("needed"));
            repository.save(specialization);
        }
    }

    static void loadWeaponsAndUpgrades(String jsonResponse, WeaponRepository weaponRepository, UpgradesRepository upgradesRepository){
        JSONObject jsonObject= new JSONObject(jsonResponse);
        jsonObject = jsonObject.getJSONObject("stats").getJSONObject("weapons");

        String weaponsString = jsonResponse.substring(jsonResponse.lastIndexOf("\"weapons\":{")+11,jsonResponse.lastIndexOf("\"mgQBB95\""));
        String[] weapons = weaponsString.split("\"}]},");
        for(int i = 0; i < weapons.length; i++){
            String weaponName = weapons[i].substring(1,weapons[i].indexOf("\":{"));
            Weapon weapon = new Weapon();
            weapon.setName       (jsonObject.getJSONObject(weaponName).getString("name"));
            if(weaponName.equals("wasK")){
                weapon.setRpm       ("-");
                weapon.setRange     ("-");
                weapon.setAmmunition("-");
            }else{
                try{
                    weapon.setRpm(jsonObject.getJSONObject(weaponName).getString("rateOfFire"));
                }catch(JSONException rateOfFireIsNotAString){
                    weapon.setRpm(Integer.toString(jsonObject.getJSONObject(weaponName).getInt("rateOfFire")));
                }
                weapon.setRange      (jsonObject.getJSONObject(weaponName).getString("range"));
                weapon.setAmmunition (jsonObject.getJSONObject(weaponName).getString("ammo"));
            }
            weapon.setCategory   (jsonObject.getJSONObject(weaponName).getString("category"));
            weapon.setType       (jsonObject.getJSONObject(weaponName).getString("kit"));
            weaponRepository.save(weapon);

            /**
             * upgrades
             */
            JSONArray unlocks = jsonObject.getJSONObject(weaponName).getJSONArray("unlocks");
            HashSet<Upgrade> upgradesSet = new HashSet<Upgrade>();
            for(int j = 0; j < unlocks.length(); j++){
                String unlockName = unlocks.getJSONObject(j).getString("name");
                Upgrade upgrade = upgradesRepository.findByName(unlockName);
                if(upgrade==null){
                    upgrade = new Upgrade();
                    upgrade.setName(unlockName);
                    upgrade.setType(unlocks.getJSONObject(j).getString("id"));
                    upgrade.setKillsForUnlock(unlocks.getJSONObject(j).getInt("needed"));
                    upgradesRepository.save(upgrade);
                }
                upgradesSet.add(upgrade);
            }
            weapon.setUpgrades(upgradesSet);
        }
    }

    static String getJsonData(String loggin){
        HttpURLConnection connection = null;
        String jsonResponse = null;
        try{
            connection = (HttpURLConnection) new URL("http://api.bf3stats.com/pc/player/").openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            String bodyParametrs = "opt=clear,nextranks,global,rank,ranking,scores,equipment,equipmentName,equipmentInfo,specializations,weapons,weaponsName,weaponsInfo,weaponsUnlocks\n&player="+loggin;
            byte[] postData = bodyParametrs.getBytes(StandardCharsets.UTF_8);
            connection.getOutputStream().write(postData);
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                jsonResponse = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
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

        return jsonResponse;
    }

    static void changePrivacy(PlayerProfile playerProfile){
        playerProfile.setPrivacy(!playerProfile.isPrivacy());
    }

}
