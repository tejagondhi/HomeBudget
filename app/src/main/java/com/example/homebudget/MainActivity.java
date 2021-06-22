package com.example.homebudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.homebudget.listeners.MainNetworkInterface;
import com.example.homebudget.models.Category;
import com.example.homebudget.models.Items;
import com.example.homebudget.models.Shop;
import com.example.homebudget.models.SubCategory;
import com.example.homebudget.util.Constants;
import com.example.homebudget.util.HomeNetwork;
import com.example.homebudget.util.PreferencesUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainNetworkInterface {
 private Spinner shopDropDown;
 private Spinner categoryDropDown;
 private Spinner subCategoryDropDown;
 private Spinner itemsDropDown;
 private ListView itemsList;
 private Shop selectedShop;
 private Category selectedCategory;
 private SubCategory selectedSubCategory;
 private Items selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        shopDropDown = findViewById(R.id.listOfShops);
        categoryDropDown = findViewById(R.id.categoryList);
        subCategoryDropDown = findViewById(R.id.subCategoryList);
        itemsDropDown = findViewById(R.id.itemsList);
        itemsList = findViewById(R.id.itemsListView);
        FloatingActionButton fab = findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        String pref = new PreferencesUtil(MainActivity.this).getPreference(Constants.USER_NAME);
        if (pref!=null&&pref.equalsIgnoreCase("teja")||pref.equalsIgnoreCase("jagapathi")){
            fab.show();
        }else{
            fab.hide();
        }

        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,DataEntry.class)));
        HomeNetwork.getInstance().getShops(this);
        HomeNetwork.getInstance().getCategory(this);


    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        new PreferencesUtil(MainActivity.this).logout();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onShopReceived(ArrayList<Shop> shops) {
        String[] shopArray = new String[shops.size()];
        int count=0;
        for (Shop shop: shops) {
            shopArray[count++]= shop.getShopName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shopArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopDropDown.setAdapter(adapter);
        shopDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedShop=shops.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCategoryReceived(ArrayList<Category> categories) {
        String[] categoryArray = new String[categories.size()];
        int count=0;
        for (Category category: categories) {
            categoryArray[count++]= category.getCategoryName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropDown.setAdapter(adapter);
        categoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory=categories.get(position);
                selectedItems=null;
                selectedSubCategory=null;
                subCategoryDropDown.setAdapter(null);
                itemsDropDown.setAdapter(null);
                HomeNetwork.getInstance().getSubCategory(MainActivity.this,selectedCategory.getCategoryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSubCategoryReceived(ArrayList<SubCategory> subCategories) {
        String[] subCategoryArray = new String[subCategories.size()];
        int count=0;
        for (SubCategory subCategory: subCategories) {
            subCategoryArray[count++]= subCategory.getSubCategoryName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoryDropDown.setAdapter(adapter);
        subCategoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCategory=subCategories.get(position);
                selectedItems=null;
                itemsDropDown.setAdapter(null);
                HomeNetwork.getInstance().getItems(MainActivity.this,selectedSubCategory.getSubCategoryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemsReceived(ArrayList<Items> items) {
        String[] itemsArray = new String[items.size()];
        int count=0;
        for (Items items1: items) {
            itemsArray[count++]= items1.getItemName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsDropDown.setAdapter(adapter);
        itemsDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItems=items.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}