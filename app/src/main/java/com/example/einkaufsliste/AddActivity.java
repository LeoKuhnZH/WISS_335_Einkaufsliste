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
// Edit Produkt und finde es bei der Id
        EditText editProdukt = findViewById(R.id.editProdukt);
        EditText editBeschreibung = findViewById(R.id.editBeschreibung);
        EditText editMenge = findViewById(R.id.editMenge);
        EditText editPreis = findViewById(R.id.editPreis);
// Button speichern findet man unter der id =
        Button btnSpeichern = findViewById(R.id.btnSpeichern);
// Funktion mit listener was passiert wenn button Speicher  gedrückt wird
        btnSpeichern.setOnClickListener(v -> {

            String produkt = editProdukt.getText().toString().trim();
            String beschreibung = editBeschreibung.getText().toString().trim();
            String menge = editMenge.getText().toString().trim();
            String preis = editPreis.getText().toString().trim();

            // Pflichtfeld funktion Produkt
            if (produkt.isEmpty()) {
                editProdukt.setError("Pflichtfeld");
                return;
            }
            //Pflichtfeld Preis
            if (preis.isEmpty()){
                editPreis.setError("Plichtfeld");
                return;

            }

            // Daten werden zurück gegeben
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