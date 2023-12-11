package org.example;

import org.example.telegrambot.mybot.Mybot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args){

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.execute(()->{
            try {
                TelegramBotsApi    telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(new Mybot("6513821661:AAFM0NrnilTDJt6_djY9lK_Y-KmZTXBYRRw"));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }
}