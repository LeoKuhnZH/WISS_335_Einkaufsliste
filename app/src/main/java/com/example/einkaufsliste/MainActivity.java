package com.example.einkaufsliste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> produktListe = new ArrayList<>();
    private TextView textViewListe;

    private ActivityResultLauncher<Intent> addProductLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.addbtn);
        textViewListe = findViewById(R.id.textViewListe);

        // RESULT HANDLING
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Intent data = result.getData();

                        String produkt = data.getStringExtra("produkt");
                        String beschreibung = data.getStringExtra("beschreibung");
                        String menge = data.getStringExtra("menge");
                        String preis = data.getStringExtra("preis");

                        String eintrag = "🛒 " + produkt + " | " +
                                beschreibung + " | " +
                                menge + " | CHF " + preis;

                        produktListe.add(eintrag);

                        updateListe();
                    }
                }
        );

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            addProductLauncher.launch(intent);
        });
    }

    private void updateListe() {
        StringBuilder sb = new StringBuilder();

        for (String item : produktListe) {
            sb.append(item).append("\n\n");
        }

        textViewListe.setText(sb.toString());
    }
}