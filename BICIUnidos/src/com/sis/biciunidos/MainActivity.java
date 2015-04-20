package com.sis.biciunidos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.Map;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

public class MainActivity extends ActionBarActivity
{
	private ImageButton btnPerfil;
	private TextView txtNombre;
	private Button btnAmigos;
	private TextView txtKilometros;
	private TextView txtCaravanas;
	private TextView txtRitmo;
	private TextView txtCalorias;
	private TextView txtPedalazos;
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath = "";
	private Usuario user;

	protected void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_main);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		btnPerfil = (ImageButton) findViewById(R.id.imageButton1);
		txtNombre =  (TextView) findViewById(R.id.textView1);
		btnAmigos = (Button) findViewById(R.id.button1);
		txtKilometros = (TextView) findViewById(R.id.textView4);
		txtCaravanas = (TextView)findViewById(R.id.textView7);
		txtRitmo = (TextView)findViewById(R.id.textView10);
		txtCalorias = (TextView)findViewById(R.id.textView13);
		txtPedalazos = (TextView)findViewById(R.id.textView16);
		ActualizarInfoPanel();
		btnPerfil.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				 Intent intent = new Intent();
                 intent.setType("image/*");
                 intent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
			}
		});
	}
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.activity_main_actions, paramMenu);
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    if (i == R.id.homeM)
    {
      System.out.println("Casa");
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
      System.out.println("Alquiler");
      startActivity(new Intent(this, AlquilerActivity.class));
      finish();
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }

  private void ActualizarInfoPanel()
  {
	  Gson gson = new Gson();
	  String jsonObjUser = PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext()).getString("USUARIO", "");
	  user = gson.fromJson(jsonObjUser, Usuario.class);
	  Firebase.setAndroidContext(getApplicationContext());
	  Firebase ref = new Firebase(Constantes.FIRE_REF+"/users/"+user.getKeyU()+"/"+user.getNombreUser()+"/");
	  ref.addValueEventListener(new ValueEventListener() 
	  {	
		@Override
		public void onDataChange(DataSnapshot arg0) 
		{
			@SuppressWarnings("unchecked")
			Map<String, Usuario> datosUsuario = (Map<String, Usuario>) arg0.getValue();
			Usuario usu = datosUsuario.get(user.getNombreUser());
			user = null;
			user = usu;
			String PATH = PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext()).getString("ImagenPerfil", "");
			System.out.println("PATH: "+ PATH);
			File file = new File(PATH);
			if(file.exists())
			{
				Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				btnPerfil.setImageBitmap(myBitmap);
			}
			txtNombre.setText(user.getNombre());
			txtKilometros.setText(user.getNumKilom()+"");
			btnAmigos.setText(user.getAmigos()+"");
			txtCaravanas.setText(user.getCaravanas());
			txtRitmo.setText(user.getRitmo()+"");
			txtPedalazos.setText(user.getPedalazos()+"");
			txtCalorias.setText(user.getCaloriasTotales()+"");
		}
		
		@Override
		public void onCancelled(FirebaseError arg0) 
		{
			AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
			builder1.setTitle("Error!");
			builder1.setMessage("Ocurrio un error al cargar su perfil en linea: "+arg0.getMessage()+", se cargaran los ultimos datos guardados localmente");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					dialog.cancel();
				}
			});
			AlertDialog alert11 = builder1.create();
			alert11.show();
			if(alert11.isShowing())
			{
				try {
					Thread.sleep(4000);
					alert11.dismiss();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String PATH = PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext()).getString("ImagenPerfil", "");
			System.out.println("PATH: "+ PATH);
			File file = new File(PATH);
			if(file.exists())
			{
				Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				btnPerfil.setImageBitmap(myBitmap);
			}
			txtNombre.setText(user.getNombre());
			txtKilometros.setText(user.getNumKilom()+"");
			btnAmigos.setText(user.getAmigos()+"");
			txtCaravanas.setText(user.getCaravanas());
			txtRitmo.setText(user.getRitmo()+"");
			txtPedalazos.setText(user.getPedalazos()+"");
			txtCalorias.setText(user.getCaloriasTotales()+"");
		}
	});
  }
  
  public void onActivityResult(int requestCode, int resultCode, Intent data) 
  {
      if (resultCode == RESULT_OK) 
      {
          if (requestCode == SELECT_PICTURE) 
          {
              Uri selectedImageUri = data.getData();
              selectedImagePath = getPath(selectedImageUri);
              System.out.println("Image Path : " + selectedImagePath);
              btnPerfil.setImageURI(selectedImageUri);
          }
      }
  }
	 public String getPath(Uri uri)
	 {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        @SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, projection, null, null, null);
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
}