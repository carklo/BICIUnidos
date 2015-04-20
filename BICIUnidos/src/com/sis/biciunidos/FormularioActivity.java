package com.sis.biciunidos;

import java.util.HashMap;
import java.util.Map;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FormularioActivity extends ActionBarActivity
{
	 private static final int SELECT_PICTURE = 1;
	 private String selectedImagePath = "";
	 private ImageView imv;
	 private EditText user;
	 private EditText fullName;
	 private EditText phoneNumber;
	 private Usuario newUser;
	 private Firebase userRef;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formulario);
		user = (EditText)findViewById(R.id.txtUser);
		fullName = (EditText) findViewById(R.id.txtFullName);
		phoneNumber = (EditText) findViewById(R.id.txtPhone);
		imv = (ImageView) findViewById(R.id.previewImage);
		Button btnCargarImagen = (Button) findViewById(R.id.uploadPhoto);
		Button btnSaveProfile = (Button) findViewById(R.id.btnSaveProfile);
		
		btnCargarImagen.setOnClickListener(new OnClickListener() 
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
		
		btnSaveProfile.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(user.getText().toString().equals("") || user.getText() == null || fullName.getText().toString().equals("") || fullName.getText().toString() == null 
						|| phoneNumber.getText().equals("") || phoneNumber.getText() == null || phoneNumber.getText().toString().matches("[0-9]+") == false 
						|| phoneNumber.getText().toString().length()!=10 || user.getText().toString().contains(".")||user.getText().toString().contains("#")
						||user.getText().toString().contains("[") || user.getText().toString().contains("]")||user.getText().toString().contains("(")||user.getText().toString().contains(")") || user.getText().toString().contains("/"))
				{
					AlertDialog.Builder builder1 = new AlertDialog.Builder(FormularioActivity.this);
					builder1.setTitle("Error");
					builder1.setMessage("Debe llenar todos los campos correctamente");
					builder1.setCancelable(true);
					builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int id) 
						{
							dialog.dismiss();
						}
					});
					AlertDialog alert11 = builder1.create();
					alert11.show();
				}
				else
				{
					//Es la primera vez por lo que no hay datos en el firebase, se guardaran todos los datos que entren
					Firebase ref = new Firebase(Constantes.FIRE_REF);
					Firebase.setAndroidContext(FormularioActivity.this);
					Firebase preuserRef = ref.child("users");
					String email = PreferenceManager.getDefaultSharedPreferences(FormularioActivity.this.getApplicationContext()).getString("User", "");
					Log.e("EMAIL", email);
					newUser = new Usuario(0, fullName.getText().toString(), user.getText().toString(),"", 0L, Double.parseDouble(phoneNumber.getText().toString()), 0.0, 0.0, email,0.0,0,0);
					Map<String, Usuario> usuario = new HashMap<String, Usuario>();
					usuario.put(user.getText().toString(), newUser);
					userRef = preuserRef.push();
					userRef.setValue(usuario, new Firebase.CompletionListener() 
					{
						
						@Override
						public void onComplete(FirebaseError arg0, Firebase arg1) 
						{
							if (arg0 != null) 
							{
					            System.out.println("Data could not be saved. " +arg0.getMessage());
					            AlertDialog.Builder builder1 = new AlertDialog.Builder(FormularioActivity.this);
								builder1.setTitle("Perfil: Error!");
								builder1.setMessage("Ocurrio un error al crear su perfil: "+arg0.getMessage());
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
					        } 
							else 
							{
								Gson gson = new Gson();
								newUser.setKeyU(userRef.getKey());
								String newUserJson = gson.toJson(newUser);
								SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(FormularioActivity.this.getApplicationContext()).edit();
								localEditor.putString("ImagenPerfil", selectedImagePath);
								localEditor.putString("USUARIO", newUserJson);
								localEditor.commit();
								AlertDialog.Builder builder1 = new AlertDialog.Builder(FormularioActivity.this);
								builder1.setTitle("Perfil: Exito!");
								builder1.setMessage("Su Perfil fue guardado y sincronizado con exito.");
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
									Intent localIntent = new Intent(FormularioActivity.this, MainActivity.class);
							        FormularioActivity.this.startActivity(localIntent);
							        FormularioActivity.this.finish();
								}
								else
								{
									Intent localIntent = new Intent(FormularioActivity.this, MainActivity.class);
							        FormularioActivity.this.startActivity(localIntent);
							        FormularioActivity.this.finish();
								}
					        }
						}
					});
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.formulario, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                imv.setImageURI(selectedImageUri);
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
