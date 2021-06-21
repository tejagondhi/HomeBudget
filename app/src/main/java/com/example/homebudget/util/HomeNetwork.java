package com.example.homebudget.util;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.homebudget.listeners.LoginNetworkInterface;
import com.example.homebudget.models.User;

import java.util.ArrayList;

public class HomeNetwork {

    private static HomeNetwork network;
    private HomeNetwork() {

    }
    public static HomeNetwork getInstance(){
        if(network!=null){
            return network;
        }
        network = new HomeNetwork();
        return network;
    }

    public void getUsers(LoginNetworkInterface loginNetworkInterface){
        AndroidNetworking.get("http://192.168.1.15:3010/users")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(User.class, new ParsedRequestListener<ArrayList<User>>() {

                    @Override
                    public void onResponse(ArrayList<User> response) {
                        loginNetworkInterface.onReceived(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        loginNetworkInterface.onError(anError.getMessage());
                    }
                });
    }
}
