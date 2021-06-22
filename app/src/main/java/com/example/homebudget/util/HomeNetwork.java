package com.example.homebudget.util;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.homebudget.listeners.DataEntryInterface;
import com.example.homebudget.listeners.LoginNetworkInterface;
import com.example.homebudget.listeners.MainNetworkInterface;
import com.example.homebudget.models.Category;
import com.example.homebudget.models.Items;
import com.example.homebudget.models.Shop;
import com.example.homebudget.models.SubCategory;
import com.example.homebudget.models.User;

import org.json.JSONObject;

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
        AndroidNetworking.get("http://192.168.1.2:3010/users")
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

    public void getShops(MainNetworkInterface mainNetworkInterface){
        AndroidNetworking.get("http://192.168.1.2:3010/shops")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Shop.class, new ParsedRequestListener<ArrayList<Shop>>() {

                    @Override
                    public void onResponse(ArrayList<Shop> response) {
                        mainNetworkInterface.onShopReceived(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainNetworkInterface.onError(anError.getMessage());
                    }
                });
    }

    public void getCategory(MainNetworkInterface mainNetworkInterface){
        AndroidNetworking.get("http://192.168.1.2:3010/categories")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Category.class, new ParsedRequestListener<ArrayList<Category>>() {

                    @Override
                    public void onResponse(ArrayList<Category> response) {
                        mainNetworkInterface.onCategoryReceived(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainNetworkInterface.onError(anError.getMessage());
                    }
                });
    }

    public void getSubCategory(MainNetworkInterface mainNetworkInterface, String id){
        AndroidNetworking.get("http://192.168.1.2:3010/subCategories/"+id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(SubCategory.class, new ParsedRequestListener<ArrayList<SubCategory>>() {

                    @Override
                    public void onResponse(ArrayList<SubCategory> response) {
                        mainNetworkInterface.onSubCategoryReceived(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainNetworkInterface.onError(anError.getMessage());
                    }
                });
    }

    public void getItems(MainNetworkInterface mainNetworkInterface, String id){
        AndroidNetworking.get("http://192.168.1.2:3010/items/"+id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Items.class, new ParsedRequestListener<ArrayList<Items>>() {

                    @Override
                    public void onResponse(ArrayList<Items> response) {
                        mainNetworkInterface.onItemsReceived(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        mainNetworkInterface.onError(anError.getMessage());
                    }
                });
    }

    /*Post section*/
    public void post(DataEntryInterface dataEntryInterface, JSONObject request,String urlExtension){
        AndroidNetworking.post("http://192.168.1.2:3010/"+urlExtension)
                .addJSONObjectBody(request)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dataEntryInterface.onSuccess();
                    }

                    @Override
                    public void onError(ANError anError) {
                        dataEntryInterface.onFailure(anError.getErrorBody());
                    }
                });
    }


}
