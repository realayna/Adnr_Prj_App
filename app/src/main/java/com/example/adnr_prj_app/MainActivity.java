package com.example.adnr_prj_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat; // Add this import statement

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextArg1, editTextArg2, editTextName;
    private TextView textViewSum, textViewName, textViewNone, textViewZero;
    private ArrayList<String> dataList;

    private int stopCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextArg1 = findViewById(R.id.editTextArg1);
        editTextArg2 = findViewById(R.id.editTextArg2);
        editTextName = findViewById(R.id.editTextName);
        textViewSum = findViewById(R.id.textViewSum);
        textViewName = findViewById(R.id.textViewName);
        textViewNone = findViewById(R.id.textViewNone);
        textViewZero = findViewById(R.id.textViewZero);

        // Initialize dataList and load saved data
        dataList = loadData();

        // Button to display values and add to the list
        Button displayButton = findViewById(R.id.buttonDisplay);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayValues();
            }
        });

        // Button to show the list in a new activity
        Button showListButton = findViewById(R.id.buttonAdd);
        showListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList();
            }
        });

        // Button to clear the list
        Button clearListButton = findViewById(R.id.buttonClear);
        clearListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }
        });
    }

    private void displayValues() {
        String arg1 = editTextArg1.getText().toString();
        String arg2 = editTextArg2.getText().toString();
        String name = editTextName.getText().toString();

        if (arg1.isEmpty() && arg2.isEmpty() && name.isEmpty()) {
            // If all fields are empty, show the list without adding an entry
            showList();
        } else if (!arg1.isEmpty() && !arg2.isEmpty() && !name.isEmpty()) {
            // If fields are not empty, calculate the sum and display values
            int sum = Integer.parseInt(arg1) + Integer.parseInt(arg2);
            textViewSum.setText("Sum: " + sum);
            textViewName.setText("Name: " + name);
            textViewNone.setVisibility(View.GONE);
            textViewZero.setVisibility(View.GONE);

            // Add an entry to the list
            addToList();
        } else {
            // Handle partially filled fields
            Toast.makeText(this, "Please fill in all fields or leave them all empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void showList() {
        // Start new activity to display the list
        Intent intent = new Intent(this, DisplayListActivity.class);

        // Pass the dataList to the DisplayListActivity
        intent.putStringArrayListExtra("dataList", dataList);

        startActivity(intent);
    }


    private void addToList() {
        String arg1 = editTextArg1.getText().toString();
        String arg2 = editTextArg2.getText().toString();
        String name = editTextName.getText().toString();

        if (!arg1.isEmpty() && !arg2.isEmpty() && !name.isEmpty()) {
            String entry = "Name: " + name + ", Sum: " + (Integer.parseInt(arg1) + Integer.parseInt(arg2));
            dataList.add(entry);
            saveData();

            // Start new activity to display the list
            Intent intent = new Intent(this, DisplayListActivity.class);

            // Pass the dataList to the DisplayListActivity
            intent.putStringArrayListExtra("dataList", dataList);

            startActivity(intent);
        } else {
            // Handle empty fields
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }



    private void clearList() {
        dataList.clear();
        saveData();
        Toast.makeText(this, "List cleared", Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> dataSet = new HashSet<>(dataList);
        editor.putStringSet("listData", dataSet);
        editor.apply();
    }

    private ArrayList<String> loadData() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> dataSet = preferences.getStringSet("listData", new HashSet<>());
        return new ArrayList<>(dataSet);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopCounter++;


        displayNotification("App Stopped", "Number of Stops: " + stopCounter);
    }

    private void displayNotification(String title, String message) {

        createNotificationChannel();


        int smallIconResourceId = R.drawable.ic_stat_name;


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "stop_channel")
                .setSmallIcon(smallIconResourceId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(stopCounter, builder.build());
    }



    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Stop Channel";
            String description = "Channel for displaying stop notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("stop_channel", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void clearList(View view) {
        dataList.clear();
        saveData();
        Toast.makeText(this, "List cleared", Toast.LENGTH_SHORT).show();
    }


    // Load data from SharedPreferences


    // Add this method to show Toast for list item click
    private void showToastForListItem(String listItem) {
        Toast.makeText(this, listItem, Toast.LENGTH_SHORT).show();
    }

}