package ru.battlefield.my.controller;

/**
 * Created by danil on 18.01.2018.
 */
public enum PersonCheckResponse {
    LoginIsIncorrect,
    PasswordIsIncorrect,
    AllIsCorrect,

    LoginHasAlreadyBeenRegistered,
    ThereIsNoSuchPlayer,
    PersonWasSuccessfullyRegistered
}
