package com.sis.biciunidos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.sis.slide.SlideDateTimeListener;
import com.sis.slide.SlideDateTimePicker;

@SuppressLint({"SimpleDateFormat"})
public class AlquilerActivity
  extends ActionBarActivity
{
  private AlertDialog alert11;
  private ArrayList<Item> grupos;
  private SlideDateTimeListener listener = new SlideDateTimeListener()
  {
    @SuppressLint("ShowToast")
	public void onDateTimeCancel()
    {
      Toast.makeText(AlquilerActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
    }
    
    @SuppressLint({"CommitPrefEdits", "ShowToast"})
    public void onDateTimeSet(Date paramAnonymousDate)
    {
      Toast.makeText(AlquilerActivity.this, AlquilerActivity.this.mFormatter.format(paramAnonymousDate), Toast.LENGTH_SHORT).show();
      SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(AlquilerActivity.this.getApplicationContext());
      SharedPreferences.Editor localEditor = localSharedPreferences.edit();
      if (!localSharedPreferences.getBoolean("AlquilerVigente", false))
      {
        localEditor.putBoolean("AlquilerVigente", true);
        localEditor.putString("FechaAlquiler", AlquilerActivity.this.mFormatter.format(paramAnonymousDate));
        localEditor.commit();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(AlquilerActivity.this);
        localBuilder.setMessage("Recuerda!, tienes maximo 24 horas para devolver la bicicleta");
        localBuilder.setCancelable(false);
        localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
          {
            paramAnonymous2DialogInterface.dismiss();
          }
        });
        AlquilerActivity.this.alert11 = localBuilder.create();
        AlquilerActivity.this.alert11.show();
        Intent localIntent1 = new Intent(AlquilerActivity.this.getApplicationContext(), RecibidorAlarmaActivity.class);
        PendingIntent localPendingIntent = PendingIntent.getActivity(AlquilerActivity.this.getApplicationContext(), 12345, localIntent1, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, paramAnonymousDate.getTime(), localPendingIntent);
        Intent localIntent2 = new Intent(AlquilerActivity.this, TemporizadorActivity.class);
        AlquilerActivity.this.startActivity(localIntent2);
        AlquilerActivity.this.finish();
      }
    }
  };
  private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
  
  public void cargarDatos()
  {
    this.grupos = new ArrayList<Item>();
    SubItem localSubItem1 = new SubItem("Llanta delantera");
    SubItem localSubItem2 = new SubItem("Llanta trasera");
    SubItem localSubItem3 = new SubItem("Freno delantero");
    SubItem localSubItem4 = new SubItem("Freno trasero");
    SubItem localSubItem5 = new SubItem("Tuberia");
    ArrayList<SubItem> localArrayList1 = new ArrayList<SubItem>();
    localArrayList1.add(localSubItem1);
    localArrayList1.add(localSubItem2);
    Item localItem1 = new Item("Llantas", localArrayList1, R.drawable.llanta);
    ArrayList<SubItem> localArrayList2 = new ArrayList<SubItem>();
    localArrayList2.add(localSubItem3);
    localArrayList2.add(localSubItem4);
    Item localItem2 = new Item("Frenos", localArrayList2, R.drawable.freno);
    ArrayList<SubItem> localArrayList3 = new ArrayList<SubItem>();
    localArrayList3.add(localSubItem5);
    Item localItem3 = new Item("Estructura", localArrayList3, R.drawable.tuberia);
    this.grupos.add(localItem1);
    this.grupos.add(localItem2);
    this.grupos.add(localItem3);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_alquiler);
    cargarDatos();
    ((ExpandableListView)findViewById(R.id.expandableListView1)).setAdapter(new AdaptadorLista(this, grupos));
    //getActionBar().setDisplayHomeAsUpEnabled(true);
    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("AlquilerVigente", false))
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setMessage("Ya existe un alquiler vigente!");
      localBuilder.setCancelable(false);
      localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          paramAnonymousDialogInterface.dismiss();
        }
      });
      this.alert11 = localBuilder.create();
      this.alert11.show();
      System.out.println("debio mostrar el dialogo");
      startActivity(new Intent(this, TemporizadorActivity.class));
      finish();
    }
    ((Button)findViewById(R.id.btn1)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        new SlideDateTimePicker.Builder(AlquilerActivity.this.getSupportFragmentManager()).setListener(listener).setInitialDate(new Date()).setMinDate(new Date()).setIs24HourTime(false).build().show();
      }
    });
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
    else if (i == R.id.bikeM)
    {
      System.out.println("Bike");
      startActivity(new Intent(this, mapActivity.class));
      finish();
      return true;
    }
    else if (i == R.id.handM)
    {
      System.out.println("Hand");
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public void onResume()
  {
    super.onResume();
    if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("AlquilerVigente", false))
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setMessage("Ya existe un alquiler vigente!");
      localBuilder.setCancelable(false);
      localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          paramAnonymousDialogInterface.dismiss();
        }
      });
      this.alert11 = localBuilder.create();
      this.alert11.show();
      System.out.println("debio mostrar el dialogo");
      startActivity(new Intent(this, TemporizadorActivity.class));
      finish();
    }
  }
}