package com.devjk.devtalk.controller;

public class RealTimeDBController {

    private static RealTimeDBController instance;

    public RealTimeDBController(){
        //constructor
    }

    public static RealTimeDBController getInstance() {
        if(instance == null){
            instance = new RealTimeDBController();
        }
        return instance;
    }
}
