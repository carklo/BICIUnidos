package com.sis.biciunidos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemporizadorActivity
  extends ActionBarActivity
{
  private AlertDialog alert11;
  private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
  
  @SuppressLint({"SimpleDateFormat"})
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_temporizador);
    final TextView temp = (TextView)findViewById(R.id.tempo);
    TextView localTextView2 = (TextView)findViewById(R.id.tempM);
    final SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    String str = null;
    Date ahora = null;
    SimpleDateFormat localSimpleDateFormat = null;
    Date fecha = null;
    if (localSharedPreferences.getBoolean("ALquilerVigente", true))
    {
      System.out.println("hay alquiler vigente");
      str = localSharedPreferences.getString("FechaAlquiler", "");
      Log.v("FECHA", str);
      ahora = new Date();
      localSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
      fecha = null;
    }
    Date aht = null;
    try
    {
      fecha = localSimpleDateFormat.parse(str);
      System.out.println("AHORA " + this.mFormatter.format(ahora));
      aht = localSimpleDateFormat.parse(this.mFormatter.format(ahora));
    }
    catch (ParseException localParseException)
    {
        localParseException.printStackTrace();
        System.out.println("ERRORRRR");
    }
    if (fecha.getTime() > aht.getTime())
    {
      localTextView2.setText("Restante para la entrega!");
      long l = fecha.getTime() - aht.getTime();
      System.out.println(l / 1000L);
      new CountDownTimer(l, 1000L)
      {
        public void onFinish() {}
        
        public void onTick(long paramAnonymousLong)
        {
          long l1 = 1000L * 60L;
          long l2 = l1 * 60L;
          long l3 = l2 * 24L;
          long l4 = paramAnonymousLong / l3;
          long l5 = paramAnonymousLong % l3;
          long l6 = l5 / l2;
          long l7 = l5 % l2;
          long l8 = l7 / l1;
          long l9 = l7 % l1 / 1000L;
          temp.setText(l4 + ":" + l6 + ":" + l8 + ":" + l9);
        }
      }.start();
      ((Button)findViewById(R.id.btnCancelar)).setOnClickListener(new View.OnClickListener()
      {
        @SuppressLint({"CommitPrefEdits"})
        public void onClick(View paramAnonymousView)
        {
          Intent localIntent1 = new Intent(TemporizadorActivity.this.getApplicationContext(), RecibidorAlarmaActivity.class);
          PendingIntent localPendingIntent = PendingIntent.getActivity(TemporizadorActivity.this.getApplicationContext(), 12345, localIntent1, PendingIntent.FLAG_CANCEL_CURRENT);
          AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
          am.cancel(localPendingIntent);
          SharedPreferences.Editor localEditor = localSharedPreferences.edit();
          localEditor.putBoolean("AlquilerVigente", false);
          localEditor.putString("FechaAlquiler", null);
          localEditor.commit();
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(TemporizadorActivity.this);
          localBuilder.setMessage("Este alquiler se ha terminado exitosamente");
          localBuilder.setCancelable(false);
          localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
            {
              paramAnonymous2DialogInterface.dismiss();
            }
          });
          TemporizadorActivity.this.alert11 = localBuilder.create();
          TemporizadorActivity.this.alert11.show();
          Intent localIntent2 = new Intent(TemporizadorActivity.this, MainActivity.class);
          TemporizadorActivity.this.startActivity(localIntent2);
          TemporizadorActivity.this.finish();
        }
      });
      return;
    }
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.activity_main_actions, paramMenu);
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    if (this.alert11 != null)
    {
      this.alert11.dismiss();
      this.alert11 = null;
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    if (i == R.id.homeM)
    {
      startActivity(new Intent(this, MainActivity.class));
      System.out.println("Casa");
      finish();
      return true;
    }
    if (i == R.id.bikeM)
    {
      System.out.println("Bike");
      startActivity(new Intent(this, mapActivity.class));
      finish();
      return true;
    }
    if (i == R.id.handM)
    {
      startActivity(new Intent(this, AlquilerActivity.class));
      System.out.println("Hand");
      finish();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
}