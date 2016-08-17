package com.test.android.mappy;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

//todo add search(home screen)
// todo add filter option based on district, ATMS, BANKS, HOSPITALS, PHARMACIES, SERVICE STATIONS,
public class Main_Activity extends ListActivity implements AdapterView.OnItemSelectedListener {
    private final ArrayList<String> CheckList = new ArrayList<>();
    private final ArrayList<String> SelectList = new ArrayList<>();
    private final String mColumns[] = OfflineData.M_COLUMNS;
    private SQLiteDatabase Place;
    private Spinner DistrictList;
    private Button SearchButton;
    protected static String CategorySelect = null;
    protected static String DistrictSelect = null;
    protected static String SelectedArea = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_homescreen);
        DistrictList = (Spinner) findViewById(R.id.District_Selector);
        DistrictList.setOnItemSelectedListener(this);
        OfflineData Places = new OfflineData(this);
        // read the database
        Place = Places.getReadableDatabase();
        PopulateSelectList();
        SearchButton = (Button) findViewById(R.id.search_button);
        SearchButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent start = new Intent(Main_Activity.this, Maps_Activity.class);
                        Main_Activity.this.startActivity(start);
                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        findViewById(R.id.empty).setVisibility(View.GONE);
        // An item was selected. You can retrieve the selected item using
        SelectedArea = parent.getSelectedItem().toString();
        if (!SelectedArea.equals("Select Search Area")) {
            if (!SelectedArea.equals("Around My Location"))
                DistrictSelect = mColumns[5] + " = '" + SelectedArea + "'";
            CategorySelect = null;
            CheckList.clear();
            PopulateCheckList();
            SelectList.remove("Select Search Area");
            SearchButton.setVisibility(View.VISIBLE);
        } else {
            CheckList.clear();
            PopulateCheckList();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onCategorySelect(View view) {
        String Category = mColumns[4] + " = '" + ((CheckBox) view).getText().toString() + "')";
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            if (CategorySelect == null)
                CategorySelect = "(" + Category;
            else
                CategorySelect = CategorySelect.replace(")", " OR " + Category);
        } else {
            if (CategorySelect.contains("OR " + Category))
                CategorySelect = CategorySelect.replace(" OR " + Category, ")");
            else if (CategorySelect.contains("OR")) {
                Category = Category.replace(")", "");
                CategorySelect = CategorySelect.replace(Category + " OR ", "");
            } else
                CategorySelect = null;
        }
    }

    private void PopulateSelectList() {
        //save the info in a cursor object
        SelectList.add("Select Search Area");
        Cursor Query = Place.query(OfflineData.LOCATION_TABLE_NAME, mColumns, null, null, mColumns[5], null, null);
        if (Query != null && Query.moveToFirst()) {
            do {
                String District = Query.getString(Query.getColumnIndex(mColumns[5]));
                SelectList.add(District);
            } while (Query.moveToNext());
            SelectList.add("Near My Location");
            Query.close();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SelectList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistrictList.setAdapter(dataAdapter);
    }

    private void PopulateCheckList() {
        if (!SelectedArea.equals("Select Search Area")) {
            Cursor Query;//save the info in a cursor object
            if (SelectedArea.equals("Near My Location"))
                Query = Place.query(OfflineData.LOCATION_TABLE_NAME, mColumns, null, null, mColumns[4], null, null);
            else
                Query = Place.query(OfflineData.LOCATION_TABLE_NAME, mColumns, DistrictSelect, null, mColumns[4], null, null);
            if (Query != null && Query.moveToFirst()) {
                do {
                    String Category = Query.getString(Query.getColumnIndex(mColumns[4]));
                    CheckList.add(Category);
                } while (Query.moveToNext());
                Query.close();
            }
        } else {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        }
        CategoryAdapter dataAdapter;
        dataAdapter = new CategoryAdapter(this, R.layout.categorylist, R.id.Category_Item, CheckList);
        setListAdapter(dataAdapter);
    }

    private class CategoryAdapter extends ArrayAdapter {

        private ArrayList items;
        private int[] mappins = {R.drawable.mappin_atm, R.drawable.mappin_bank, R.drawable.mappin_hospital, R.drawable.mappin_pharmacy, R.drawable.mappin_service_station};

        public CategoryAdapter(Context context, int categorylist, int textViewResourceId, ArrayList items) {
            super(context, categorylist, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.categorylist, null);
            }

            String it = items.get(position).toString();
            if (it != null) {
                ImageView iv = (ImageView) v.findViewById(R.id.mappin);
                TextView tv = (TextView) v.findViewById(R.id.Category_Item);
                if (iv != null) {
                    int CategoryImage = 0;
                    if (it.contains("ATM")) CategoryImage = 0;
                    else if (it.contains("Bank")) CategoryImage = 1;
                    else if (it.contains("Hospital")) CategoryImage = 2;
                    else if (it.contains("Pharmacy")) CategoryImage = 3;
                    else if (it.contains("Service")) CategoryImage = 4;
                    iv.setImageResource(mappins[CategoryImage]);
                    tv.setText(items.get(position).toString());
                }
            }

            return v;
        }
    }

}
