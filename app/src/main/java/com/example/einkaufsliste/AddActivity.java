package com.example.einkaufsliste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        EditText editProdukt = findViewById(R.id.editProdukt);
        EditText editBeschreibung = findViewById(R.id.editBeschreibung);
        EditText editMenge = findViewById(R.id.editMenge);
        EditText editPreis = findViewById(R.id.editPreis);

        Button btnSpeichern = findViewById(R.id.btnSpeichern);

        btnSpeichern.setOnClickListener(v -> {

            String produkt = editProdukt.getText().toString().trim();
            String beschreibung = editBeschreibung.getText().toString().trim();
            String menge = editMenge.getText().toString().trim();
            String preis = editPreis.getText().toString().trim();

            // VALIDIERUNG
            if (produkt.isEmpty()) {
                editProdukt.setError("Pflichtfeld");
                return;
            }

            // Daten zurückgeben
            Intent resultIntent = new Intent();
            resultIntent.putExtra("produkt", produkt);
            resultIntent.putExtra("beschreibung", beschreibung);
            resultIntent.putExtra("menge", menge);
            resultIntent.putExtra("preis", preis);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}