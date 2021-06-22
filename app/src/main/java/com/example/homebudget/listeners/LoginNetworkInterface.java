package com.example.homebudget.listeners;

import com.example.homebudget.models.User;

import java.util.ArrayList;

public interface LoginNetworkInterface {
     void onReceived(ArrayList<User> users);
     void onError(String error);
}

