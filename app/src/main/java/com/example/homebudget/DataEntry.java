package com.example.homebudget;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homebudget.listeners.DataEntryInterface;
import com.example.homebudget.listeners.MainNetworkInterface;
import com.example.homebudget.models.Category;
import com.example.homebudget.models.Items;
import com.example.homebudget.models.Measure;
import com.example.homebudget.models.Shop;
import com.example.homebudget.models.SubCategory;
import com.example.homebudget.util.HomeNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.homebudget.util.Constants.CATEGORY_URL_EXTENSION;
import static com.example.homebudget.util.Constants.ITEMS_URL_EXTENSION;
import static com.example.homebudget.util.Constants.SHOP_URL_EXTENSION;
import static com.example.homebudget.util.Constants.SUB_CATEGORY_URL_EXTENSION;
import static com.example.homebudget.util.Constants.USER_URL_EXTENSION;

public class DataEntry extends AppCompatActivity implements MainNetworkInterface, DataEntryInterface {

    private Category selectedCategory;
    Spinner categoryList;
    Spinner subCategoryList;
    private SubCategory selectedSubCategory;
    private EditText addUsersTxtBox;
    private EditText addShopTxtBox;
    private EditText addCategoryTxtBox;
    private EditText addSubCatTxtBox;
    private EditText addItemsTxtBox;
    private TextView errorResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        addUsersTxtBox = findViewById(R.id.addUsersTxtBox);
        addShopTxtBox = findViewById(R.id.addShopTxtBox);
        addCategoryTxtBox = findViewById(R.id.addCategoryTxtBox);
        addSubCatTxtBox = findViewById(R.id.addSubCatTxtBox);
        addItemsTxtBox = findViewById(R.id.addItemsTxtBox);
        categoryList = findViewById(R.id.selectCategoryList);
        subCategoryList = findViewById(R.id.selectSubCategoryList);
        errorResponse = findViewById(R.id.errorResponse);
        Button submitDataBtn = findViewById(R.id.dataSubmitBtn);
        submitDataBtn.setOnClickListener(v -> submitClicked());
        HomeNetwork.getInstance().getSubCategory(this, "0");
        HomeNetwork.getInstance().getCategory(this);
    }

    private void submitClicked() {
        JSONObject request = new JSONObject();
        String urlExtension = "";
        if (addUsersTxtBox.getText().toString().trim().length() > 0) {
            urlExtension = USER_URL_EXTENSION;
            try {
                request.put("userName", addUsersTxtBox.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (addShopTxtBox.getText().toString().trim().length() > 0) {
            urlExtension = SHOP_URL_EXTENSION;
            try {
                request.put("shopName", addShopTxtBox.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (addCategoryTxtBox.getText().toString().trim().length() > 0) {
            urlExtension = CATEGORY_URL_EXTENSION;
            try {
                request.put("categoryName", addCategoryTxtBox.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (addSubCatTxtBox.getText().toString().trim().length() > 0) {
            urlExtension = SUB_CATEGORY_URL_EXTENSION;
            try {
                request.put("subCategoryName", addSubCatTxtBox.getText().toString().trim());
                request.put("categoryId", selectedCategory.getCategoryId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (addItemsTxtBox.getText().toString().trim().length() > 0) {
            urlExtension = ITEMS_URL_EXTENSION;
            try {
                request.put("itemName", addItemsTxtBox.getText().toString().trim());
                request.put("subCategoryId", selectedSubCategory.getSubCategoryId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (urlExtension.length() > 0) {
            post(request, urlExtension);
        }

    }

    private void post(JSONObject request, String urlExtension) {
        HomeNetwork.getInstance().post(this, request, urlExtension);
    }


    @Override
    public void onShopReceived(ArrayList<Shop> shops) {

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
        categoryList.setAdapter(adapter);
        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
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
        subCategoryList.setAdapter(adapter);
        subCategoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCategory = subCategories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemsReceived(ArrayList<Items> items) {

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onMeasureReceived(ArrayList<Measure> response) {

    }

    @Override
    public void onSuccess() {
        addUsersTxtBox.setText("");
        addCategoryTxtBox.setText("");
        addItemsTxtBox.setText("");
        addShopTxtBox.setText("");
        addSubCatTxtBox.setText("");
    }

    @Override
    public void onFailure(String error) {
        errorResponse.setText(error);
    }
}