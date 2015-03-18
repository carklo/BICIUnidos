package com.sis.biciunidos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlquilerDatabaseHandler
  extends SQLiteOpenHelper
{
  public AlquilerDatabaseHandler(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
  }
  
  public void agregarAlquiler(Alquiler paramAlquiler)
  {
    SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("fecha", Integer.valueOf(paramAlquiler.getFechaEntrega()));
    localContentValues.put("LLantaDeEst", paramAlquiler.getLlantaDelanteraEstado());
    localContentValues.put("LLantaTraEst", paramAlquiler.getLlantaTraseraEstado());
    localContentValues.put("FrenoDeEst", paramAlquiler.getFrenoDelanteroEstado());
    localContentValues.put("FrenoTraEst", paramAlquiler.getFrenoTraseroEstado());
    localContentValues.put("EstEst", paramAlquiler.getEstructuraEstado());
    localContentValues.put("LlantaDeObs", paramAlquiler.getLlantaDelanteraObs());
    localContentValues.put("LlantaTraObs", paramAlquiler.getLlantaTraseraObs());
    localContentValues.put("FrenoDeObs", paramAlquiler.getFrenoDelanteroObs());
    localContentValues.put("FrenoTraObs", paramAlquiler.getFrenoTraseroObs());
    localContentValues.put("EstObs", paramAlquiler.getEstructuraObs());
    localSQLiteDatabase.insert("alquileres", null, localContentValues);
    localSQLiteDatabase.close();
  }
  
  public Alquiler darAlquiler(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = getReadableDatabase();
    String[] arrayOfString1 = { "id", "fecha", "LLantaDeEst", "LLantaTraEst", "FrenoDeEst", "FrenoTraEst", "EstEst", "LlantaDeObs", "LlantaTraObs", "FrenoDeObs", "FrenoTraObs", "EstObs" };
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = String.valueOf(paramInt);
    Cursor localCursor = localSQLiteDatabase.query(false, "alquileres", arrayOfString1, "fecha=?", arrayOfString2, null, null, null, null);
    if (localCursor != null) {
      localCursor.moveToFirst();
    }
    return new Alquiler(Integer.parseInt(localCursor.getString(1)), localCursor.getString(2), localCursor.getString(3), localCursor.getString(4), localCursor.getString(5), localCursor.getString(6), localCursor.getString(7), localCursor.getString(8), localCursor.getString(9), localCursor.getString(10), localCursor.getString(11));
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE alquileres(id INTEGER PRIMARY KEY,fecha INTEGER,LLantaDeEst TEXT,LLantaTraEst TEXTFrenoDeEst TEXT,FrenoTraEst TEXT,EstEst TEXT,LlantaDeObs TEXT,LlantaTraObs TEXT,FrenoDeObs TEXT,FrenoTraObs TEXT,EstObs TEXT,)");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS alquileres");
    onCreate(paramSQLiteDatabase);
  }
}
