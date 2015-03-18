package com.sis.biciunidos;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.PrintStream;

public class mapActivity
  extends ActionBarActivity
{
  //private final LatLng S_LOCATION = new LatLng(4.602664D, -74.066264000000004D);
  private GoogleMap map;
  private String numero;
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_map);
    if (this.map == null)
    {
    	MapFragment mf = (MapFragment)getFragmentManager().findFragmentById(R.id.maps);
    	map = mf.getMap();
    	if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext())!=ConnectionResult.SUCCESS)
    	{
    		GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()), this, ConnectionResult.SUCCESS);
    	}
    	else
    	{
    		map.setMyLocationEnabled(true);
        	UiSettings localUiSettings2 = this.map.getUiSettings();
        	localUiSettings2.setMyLocationButtonEnabled(true);
        	localUiSettings2.setZoomControlsEnabled(true);
    	}
    }
    else
    {
    	if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext())!=ConnectionResult.SUCCESS)
    	{
    		GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()), this, ConnectionResult.SUCCESS);
    	}
    	else
    	{
    		map.setMyLocationEnabled(true);
        	UiSettings localUiSettings2 = this.map.getUiSettings();
        	localUiSettings2.setMyLocationButtonEnabled(true);
        	localUiSettings2.setZoomControlsEnabled(true);
    	}
    }
      pointLocation();
      ((ImageButton)findViewById(R.id.contacts)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          String[] arrayOfString = { "Compartir mapa Sms", "Llamar contacto" };
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(mapActivity.this);
          localBuilder.setTitle("Opciones").setItems(arrayOfString, new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
            {
              if (paramAnonymous2Int == 0)
              {
                Intent localIntent = new Intent(mapActivity.this, SMSActivity.class);
                mapActivity.this.startActivity(localIntent);
                mapActivity.this.finish();
                return;
              }
              else
              {
            	  AlertDialog.Builder localBuilder = new AlertDialog.Builder(mapActivity.this);
                  localBuilder.setTitle("Llamada");
                  localBuilder.setMessage("Ingrese el numero del contacto");
                  final EditText localEditText = new EditText(mapActivity.this);
                  localEditText.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                  localBuilder.setView(localEditText);
                  localBuilder.setPositiveButton("LLamar", new DialogInterface.OnClickListener()
                  {
                    public void onClick(DialogInterface paramAnonymous3DialogInterface, int paramAnonymous3Int)
                    {
                      mapActivity.this.numero = localEditText.getText().toString();
                      Intent localIntent = new Intent("android.intent.action.CALL");
                      localIntent.setData(Uri.parse("tel:" + mapActivity.this.numero));
                      mapActivity.this.startActivity(localIntent);
                      mapActivity.this.finish();
                    }
                  });
                  localBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                  {
                    public void onClick(DialogInterface paramAnonymous3DialogInterface, int paramAnonymous3Int)
                    {
                      paramAnonymous3DialogInterface.cancel();
                    }
                  });
                  localBuilder.show();
              }
              
            }
          });
          localBuilder.create();
          localBuilder.show();
        }
      });
      return;
    }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.activity_main_actions, paramMenu);
    return true;
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    if (i == R.id.homeM)
    {
      startActivity(new Intent(this, MainActivity.class));
      System.out.println("Casa");
      finish();
    }
    else if(i == R.id.bikeM) {
      return true;
    }
    else if(i == R.id.handM)
    {
      startActivity(new Intent(this, AlquilerActivity.class));
      System.out.println("Hand");
      finish();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public void pointLocation()
  {
    if (this.map != null) {
      this.map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
      {
        public void onMapLongClick(LatLng paramAnonymousLatLng)
        {
            double d1 = paramAnonymousLatLng.latitude;
            double d2 = paramAnonymousLatLng.longitude;
            mapActivity.this.map.addMarker(new MarkerOptions().position(new LatLng(d1, d2)));
            Toast.makeText(mapActivity.this.getApplicationContext(), "Latitude : " + d1 + "Longitude :" + " " + d2, Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
