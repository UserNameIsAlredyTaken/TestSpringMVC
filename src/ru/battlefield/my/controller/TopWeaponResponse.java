package ru.battlefield.my.controller;

/**
 * Created by danil on 24.01.2018.
 */
public class TopWeaponResponse {
    private String topWeaponName;
    private int topWeaponKills;

    public String getTopWeaponName() {
        return topWeaponName;
    }

    public void setTopWeaponName(String topWeaponName) {
        this.topWeaponName = topWeaponName;
    }

    public int getTopWeaponKills() {
        return topWeaponKills;
    }

    public void setTopWeaponKills(int topWeaponKills) {
        this.topWeaponKills = topWeaponKills;
    }
}
