package com.example.einkaufsliste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> produktListe = new ArrayList<>();
    private static final String PREFS_NAME = "EinkaufslistePrefs";
    private static final String KEY_LISTE = "produktListe";
    private TextView textViewListe;

    private ActivityResultLauncher<Intent> addProductLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.addbtn);
        textViewListe = findViewById(R.id.textViewListe);
        loadListe();
        updateListe();

        // RESULT HANDLING
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();
// Get Data
                        String produkt = data.getStringExtra("produkt");
                        String beschreibung = data.getStringExtra("beschreibung");
                        String menge = data.getStringExtra("menge");
                        String preis = data.getStringExtra("preis");

                        String eintrag = "- " + produkt + " | " +
                                beschreibung + " | " +
                                menge + " | CHF " + preis;
// Produkt Eintagen
                        produktListe.add(eintrag);
// Und dann liste aktualiesieren
                        saveListe();
                        updateListe();

                    }
                }
        );

// Button Velinkung zu AddActivity
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            addProductLauncher.launch(intent);
        });
    }
    //funktion Save List
    private void saveListe() {
// öffnet speicher bereich
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
// edit wird geöffnet
        SharedPreferences.Editor editor = prefs.edit();
// langer string wird gebaut
        StringBuilder sb = new StringBuilder();
// Item wird in die Produkt Liste gesetzt ;;; = attributet z.B. Milch ,Zucker, Mehl
        for (String item : produktListe) {
            sb.append(item).append(";;;");
        }
//speichert den string unter Key Liste
        editor.putString(KEY_LISTE, sb.toString());
        editor.apply();
    }
    // Funktion Liste Laden
    private void loadListe() {
//öffnet Speicher bereich der app
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
// Greift auf die gespeicherte Key Liste zu
        String gespeicherteListe =
                prefs.getString(KEY_LISTE, "");
// falls liste leer
        if (!gespeicherteListe.isEmpty()) {
//teile ;;; in drei einzelne items also MilchZuckerMehl = Milch, Zucker und Mehl
            String[] items = gespeicherteListe.split(";;;");

            produktListe.clear();

            for (String item : items) {

                if (!item.isEmpty()) {
                    produktListe.add(item);
                }
            }
        }
    }

    private void updateListe() {
        StringBuilder sb = new StringBuilder();

        for (String item : produktListe) {
            sb.append(item).append("\n\n");
        }

        textViewListe.setText(sb.toString());
    }
}