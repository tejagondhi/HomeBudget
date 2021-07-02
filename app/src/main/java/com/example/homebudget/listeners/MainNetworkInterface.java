package com.example.homebudget.listeners;

import com.example.homebudget.models.Category;
import com.example.homebudget.models.Items;
import com.example.homebudget.models.Measure;
import com.example.homebudget.models.Shop;
import com.example.homebudget.models.SubCategory;

import java.util.ArrayList;

public interface MainNetworkInterface {
    void onShopReceived(ArrayList<Shop> shops);

    void onCategoryReceived(ArrayList<Category> categories);

    void onSubCategoryReceived(ArrayList<SubCategory> subCategories);

    void onItemsReceived(ArrayList<Items> items);

    void onError(String error);

    void onMeasureReceived(ArrayList<Measure> response);
}
