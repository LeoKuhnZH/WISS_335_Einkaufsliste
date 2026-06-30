package com.example.einkaufsliste;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ArrayList<String> produktListe = new ArrayList<>();
    private TextView textViewListe;

    private ActivityResultLauncher<Intent> addProductLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        Button button = findViewById(R.id.addbtn);
        textViewListe = findViewById(R.id.textViewListe);

        //  Launcher zuerst registrieren
        setupLauncher();

        //  Daten laden
        ladeProdukte();

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            addProductLauncher.launch(intent);
        });
    }

    //  RESULT HANDLING
    private void setupLauncher() {

        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();

                        String produkt = data.getStringExtra("produkt");
                        String beschreibung = data.getStringExtra("beschreibung");
                        String menge = data.getStringExtra("menge");
                        String preis = data.getStringExtra("preis");

                        dbHelper.insertProdukt(produkt, beschreibung, menge, preis);

                        ladeProdukte();
                    }
                }
        );
    }

    private void ladeProdukte() {

        produktListe.clear();

        Cursor cursor = dbHelper.getProdukte();

        while (cursor.moveToNext()) {

            String produkt = cursor.getString(1);
            String beschreibung = cursor.getString(2);
            String menge = cursor.getString(3);
            String preis = cursor.getString(4);

            String eintrag =
                    "- " + produkt +
                            " | " + beschreibung +
                            " | " + menge +
                            " | CHF " + preis;

            produktListe.add(eintrag);
        }

        cursor.close();

        updateListe();
    }

    private void updateListe() {

        StringBuilder sb = new StringBuilder();

        for (String item : produktListe) {
            sb.append(item).append("\n\n");
        }

        textViewListe.setText(sb.toString());
    }
}