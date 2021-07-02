package com.example.homebudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homebudget.adapters.HomeBudgetListView;
import com.example.homebudget.listeners.DataEntryInterface;
import com.example.homebudget.listeners.MainNetworkInterface;
import com.example.homebudget.models.Category;
import com.example.homebudget.models.Items;
import com.example.homebudget.models.ItemsList;
import com.example.homebudget.models.Measure;
import com.example.homebudget.models.Shop;
import com.example.homebudget.models.SubCategory;
import com.example.homebudget.util.Constants;
import com.example.homebudget.util.HomeNetwork;
import com.example.homebudget.util.PreferencesUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.homebudget.util.Constants.LOGIN_USER;
import static com.example.homebudget.util.Constants.SPENDINGS_URL_EXTENSION;

public class MainActivity extends AppCompatActivity implements MainNetworkInterface, DataEntryInterface {
    private Spinner shopDropDown;
    private Spinner categoryDropDown;
    private Spinner subCategoryDropDown;
    private Spinner itemsDropDown;
    private Shop selectedShop;
    private Category selectedCategory;
    private SubCategory selectedSubCategory;
    private Items selectedItems;
    private RecyclerView listItems;
    private final ArrayList<ItemsList> data = new ArrayList<>();
    private HomeBudgetListView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        shopDropDown = findViewById(R.id.listOfShops);
        categoryDropDown = findViewById(R.id.categoryList);
        subCategoryDropDown = findViewById(R.id.subCategoryList);
        itemsDropDown = findViewById(R.id.itemsList);
        FloatingActionButton fab = findViewById(R.id.fab);
        listItems = findViewById(R.id.listRecycleView);
        Button addItem = findViewById(R.id.addItemButton);
        Button uploadItem = findViewById(R.id.uploadButton);

        uploadItem.setOnClickListener(v -> {
            JSONObject request = new JSONObject();
            AtomicReference<ArrayList<ItemsList>> data = new AtomicReference<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            adapter.getData(data);

            JSONArray requestData1 = new JSONArray();
            for (ItemsList items : data.get()) {
                try {
                    JSONArray requestData = new JSONArray();
                    if (items.getAmount() <= 0 || items.getQuantity() <= 0 || (items.getMeasurementId() == null || items.getMeasurementId().length() <= 0)) {
                        return;
                    }
                    requestData.put("1234");
                    requestData.put(items.getShop().getShopId());
                    requestData.put(items.getCategory().getCategoryId());
                    requestData.put(items.getSubCategory().getSubCategoryId());
                    requestData.put(items.getItem().getItemId());
                    requestData.put(items.getUserId());
                    requestData.put(items.getQuantity());
                    requestData.put(items.getAmount());
                    requestData.put(items.getMeasurementId());
                    requestData.put(format.format(new Date()));
                    requestData1.put(requestData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                request.put("data", requestData1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            post(request, SPENDINGS_URL_EXTENSION);


        });


        addItem.setOnClickListener(v -> {
            ItemsList itemsList = new ItemsList();
            itemsList.setItem(selectedItems);
            itemsList.setCategory(selectedCategory);
            itemsList.setSubCategory(selectedSubCategory);
            itemsList.setShop(selectedShop);
            itemsList.setUserId(new PreferencesUtil(MainActivity.this).getPreference(LOGIN_USER));
            data.add(itemsList);
            adapter.addItem(data.size() - 1, itemsList);
            listItems.invalidate();
        });

        setSupportActionBar(toolbar);
        String pref = new PreferencesUtil(MainActivity.this).getPreference(Constants.USER_NAME);
        if (pref != null && pref.equalsIgnoreCase("teja") || Objects.requireNonNull(pref).equalsIgnoreCase("jagapathi")) {
            fab.show();
        } else {
            fab.hide();
        }

        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DataEntry.class)));
        HomeNetwork.getInstance().getShops(this);
        HomeNetwork.getInstance().getCategory(this);
        HomeNetwork.getInstance().getMeasurement(this);

        listItems.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new HomeBudgetListView(this, new ArrayList<>());
        listItems.setAdapter(adapter);

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
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onShopReceived(ArrayList<Shop> shops) {
        String[] shopArray = new String[shops.size()];
        int count = 0;
        for (Shop shop : shops) {
            shopArray[count++] = shop.getShopName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shopArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopDropDown.setAdapter(adapter);
        shopDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedShop = shops.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCategoryReceived(ArrayList<Category> categories) {
        String[] categoryArray = new String[categories.size()];
        int count = 0;
        for (Category category : categories) {
            categoryArray[count++] = category.getCategoryName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropDown.setAdapter(adapter);
        categoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
                selectedItems = null;
                selectedSubCategory = null;
                subCategoryDropDown.setAdapter(null);
                itemsDropDown.setAdapter(null);
                HomeNetwork.getInstance().getSubCategory(MainActivity.this, selectedCategory.getCategoryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSubCategoryReceived(ArrayList<SubCategory> subCategories) {
        String[] subCategoryArray = new String[subCategories.size()];
        int count = 0;
        for (SubCategory subCategory : subCategories) {
            subCategoryArray[count++] = subCategory.getSubCategoryName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoryDropDown.setAdapter(adapter);
        subCategoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCategory = subCategories.get(position);
                selectedItems = null;
                itemsDropDown.setAdapter(null);
                HomeNetwork.getInstance().getItems(MainActivity.this, selectedSubCategory.getSubCategoryId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemsReceived(ArrayList<Items> items) {
        String[] itemsArray = new String[items.size()];
        int count = 0;
        for (Items items1 : items) {
            itemsArray[count++] = items1.getItemName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsDropDown.setAdapter(adapter);
        itemsDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItems = items.get(position);
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

    @Override
    public void onMeasureReceived(ArrayList<Measure> response) {
        adapter.addMeasure(response);
    }

    private void post(JSONObject request, String urlExtension) {
        HomeNetwork.getInstance().post(this, request, urlExtension);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        adapter.deleteAll();
        data.clear();
        listItems.invalidate();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}