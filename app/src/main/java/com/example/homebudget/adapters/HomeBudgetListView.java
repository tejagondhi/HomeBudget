package com.example.homebudget.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homebudget.MainActivity;
import com.example.homebudget.R;
import com.example.homebudget.models.ItemsList;
import com.example.homebudget.models.Measure;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class HomeBudgetListView extends RecyclerView.Adapter<HomeBudgetListView.HomeBudgetView> {


    private final MainActivity context;
    private final ArrayList<ItemsList> itemsList;
    private ArrayList<Measure> measurements;

    public HomeBudgetListView(MainActivity mainActivity, ArrayList<ItemsList> itemsList) {
        this.context = mainActivity;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public HomeBudgetListView.HomeBudgetView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_budget_adapter, parent, false);
        return new HomeBudgetView(view, measurements);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeBudgetListView.HomeBudgetView holder, int position) {
        holder.itemName.setText(itemsList.get(position).getItem().getItemName());
        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 0) {
                    return;
                }
                itemsList.get(holder.getAdapterPosition()).setAmount((Double.parseDouble(s.toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.measurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemsList.get(holder.getAdapterPosition()).setMeasurementId(measurements.get(position).getMeasureId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 0) {
                    return;
                }
                itemsList.get(holder.getAdapterPosition()).setQuantity((Double.parseDouble(s.toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void addItem(int position, ItemsList data) {
        itemsList.add(position, data);
        notifyItemInserted(position);
    }

    public void addMeasure(ArrayList<Measure> measures) {
        this.measurements = measures;
    }


    public void getData(AtomicReference<ArrayList<ItemsList>> data) {
        data.set(itemsList);
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void deleteAll() {
        itemsList.clear();
        notifyDataSetChanged();
    }

    public static class HomeBudgetView extends RecyclerView.ViewHolder {
        TextView itemName;
        EditText quantity;
        Spinner measurement;
        EditText amount;

        public HomeBudgetView(@NonNull View itemView, ArrayList<Measure> measurements) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            quantity = itemView.findViewById(R.id.quantity);
            measurement = itemView.findViewById(R.id.measurement);
            amount = itemView.findViewById(R.id.amount);
            String[] measureArray = new String[measurements.size()];
            int count = 0;
            for (Measure measure : measurements) {
                measureArray[count++] = measure.getMeasureName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, measureArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            measurement.setAdapter(adapter);
        }
    }
}
