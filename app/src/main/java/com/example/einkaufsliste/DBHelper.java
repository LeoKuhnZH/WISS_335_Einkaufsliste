package com.example.einkaufsliste;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {

    //DB Name
    private static final String DB_NAME = "einkaufsliste.db";

    //Version
    private static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null,DB_VERSION);
    }
    @Override
    public  void onCreate(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE produkte(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "produkt TEXT," +
                        "beschreibung TEXT," +
                        "menge TEXT," +
                        "preis TEXT)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS produkte");
        onCreate(db);
    }
    //Produkte Speichern in Database
    public void insertProdukt(String produkt, String beschreibung, String menge, String preis){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Attribute die in der Data Base Gespeichert werden

        values.put("produkt", produkt);
        values.put("beschreibung", beschreibung);
        values.put("menge", menge );
        values.put("preis", preis);

        db.insert("produkte", null, values);

    }
    // Produkte aus der Data Base Laden
    public Cursor getProdukte(){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM produkte", null);
    }

}
