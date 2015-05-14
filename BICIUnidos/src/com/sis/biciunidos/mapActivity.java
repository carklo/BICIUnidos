package com.sis.biciunidos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.sis.geofirebase.GeoFire;
import com.sis.geofirebase.GeoFire.CompletionListener;
import com.sis.geofirebase.GeoLocation;
import com.sis.geofirebase.GeoQuery;
import com.sis.geofirebase.GeoQueryEventListener;

@SuppressLint("InflateParams") public class mapActivity
extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, com.google.android.gms.location.LocationListener, GeoQueryEventListener, GoogleMap.OnCameraChangeListener
{
	//private final LatLng S_LOCATION = new LatLng(4.602664D, -74.066264000000004D);
	private GoogleMap map;
	private String numero;
	public ArrayList<Marcador> markers = new ArrayList<Marcador>();
	private TextView textTip;
	public ArrayList<LatLng> marcadores;
	private Location initial;
	private boolean vaARecibirOnLoc0 = false;
	private int contOnC = 0;
	private int contBtn = 0;
	private GoogleApiClient mGoogleApiClient;
	private long initTime;
	private Circle searchCircle;
	private GeoFire geoFire;
	private GeoQuery geoQuery;
	private Usuario user;
	private Marker inicial = null;
	private Map<String,Marker> markersF;
	private Chronometer chr;
	private ArrayList<PlaceMark>cuadrantes;
	
	protected void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_map);
		marcadores = new ArrayList<LatLng>();
		cuadrantes = new ArrayList<PlaceMark>();
		Gson gson = new Gson();
		chr = (Chronometer) findViewById(R.id.chronometer1);
		String jsonObjUser = PreferenceManager.getDefaultSharedPreferences(mapActivity.this.getApplicationContext()).getString("USUARIO", "");
		user = gson.fromJson(jsonObjUser, Usuario.class);
		Firebase.setAndroidContext(getApplicationContext());
		geoFire = new GeoFire(new Firebase(Constantes.FIRE_REF));
		markersF = new HashMap<String, Marker>();
		if (this.map == null)
		{
			MapFragment mf = (MapFragment)getFragmentManager().findFragmentById(R.id.maps);
			map = mf.getMap();
			contOnC = 0;
			contBtn = 0;
			initTime = 0L;
			markers = new ArrayList<Marcador>();
			if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext())!=ConnectionResult.SUCCESS)
			{
				GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()), this, ConnectionResult.SUCCESS);
			}
			else
			{
				map.setMyLocationEnabled(true);
				Log.i("PROCESO", "SE ESTA PIDIENDO LA ULTIMA LOCACION");
				mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(LocationServices.API)
		        .build();
				mGoogleApiClient.connect();	
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
				Log.i("PROCESO", "SE ESTA PIDIENDO LA ULTIMA LOCACION");
				mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(LocationServices.API)
		        .build();
				mGoogleApiClient.connect();
			}
		}
		pointLocation();
		showInfoMarker();
		eliminarMarcador();
		((ImageButton) findViewById(R.id.chronometer)).setOnClickListener(new OnClickListener() 
		{
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) 
			{
				if(contBtn ==0)
				{
					contBtn++;
					//Primera vez que se da a ejecutar al boton
					Drawable orange = getResources().getDrawable(R.drawable.round_button_whi);
					((ImageButton) findViewById(R.id.chronometer)).setBackgroundDrawable(orange);
					initTime = System.currentTimeMillis();
					mGoogleApiClient = new GoogleApiClient.Builder(mapActivity.this)
			        .addConnectionCallbacks(mapActivity.this)
			        .addOnConnectionFailedListener(mapActivity.this)
			        .addApi(LocationServices.API)
			        .build();
					mGoogleApiClient.connect();	
					chr.setFormat("H:MM:SS");
					chr.start();
				}
				else
				{
					chr.stop();
					//Segunda vez que se da a ejecutar al boton.
					LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, (com.google.android.gms.location.LocationListener) mapActivity.this);
					// remove all event listeners to stop updating in the background
					geoQuery.removeAllListeners();
					AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
					builder1.setTitle("Pausa!");
					builder1.setMessage("¿Qué deseas hacer?");
					builder1.setCancelable(true);
					builder1.setPositiveButton("Reestablecer", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int id) 
						{
							chr.start();
							initTime = System.currentTimeMillis();
							mGoogleApiClient = new GoogleApiClient.Builder(mapActivity.this)
					        .addConnectionCallbacks(mapActivity.this)
					        .addOnConnectionFailedListener(mapActivity.this)
					        .addApi(LocationServices.API)
					        .build();
							mGoogleApiClient.connect();
							dialog.dismiss();
						}
					});
					builder1.setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							chr.setBase(0L);
							chr.start();
							for (Marker marker: markersF.values()) 
							{
					            marker.remove();
					        }
					        markersF.clear();
					      //Segunda vez que se da a ejecutar al boton. Cancelar?
							LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, (com.google.android.gms.location.LocationListener) mapActivity.this);
							// remove all event listeners to stop updating in the background
							geoQuery.removeAllListeners();
							chr.start();
							initTime = System.currentTimeMillis();
							mGoogleApiClient = new GoogleApiClient.Builder(mapActivity.this)
					        .addConnectionCallbacks(mapActivity.this)
					        .addOnConnectionFailedListener(mapActivity.this)
					        .addApi(LocationServices.API)
					        .build();
							mGoogleApiClient.connect();
							dialog.dismiss();
						}
					});
					builder1.setNeutralButton("Apagar", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							
						}
					});
					AlertDialog alert11 = builder1.create();
					alert11.show();
					//Segunda vez que se da a ejecutar al boton. Cancelar?
					LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, (com.google.android.gms.location.LocationListener) mapActivity.this);
					// remove all event listeners to stop updating in the background
					Drawable orange = getResources().getDrawable(R.drawable.round_button);
					((ImageButton) findViewById(R.id.chronometer)).setBackgroundDrawable(orange);
					geoQuery.removeAllListeners();
			        for (Marker marker: markersF.values()) {
			            marker.remove();
			        }
			        markersF.clear();
			        chr.stop();
					contBtn = 0;
				}
			}
		});
		
		((ImageButton) findViewById(R.id.btnClean)).setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				limpiarMapa();
			}
		});
		((ImageButton) findViewById(R.id.station)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				try 
				{
					cargarCuadrantes();
					dibujarCuadrantes();
				} catch (ParserConfigurationException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		((ImageButton) findViewById(R.id.btnFind)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				trazarRuta();
			}
		});
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
		if (this.map != null) 
		{
			map.setOnMapLongClickListener(new OnMapLongClickListener() 
			{
				@Override
				public void onMapLongClick(LatLng point) 
				{
					if(markers.size()>=10)
					{
						AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
						builder1.setMessage("El numero maximo de marcadores que se pueden crear es 10");
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
						final double d1 = point.latitude;
						final double d2 = point.longitude;
						AlertDialog.Builder builder = new AlertDialog.Builder(mapActivity.this);
						// Get the layout inflater
						LayoutInflater inflater = mapActivity.this.getLayoutInflater();
						View la = inflater.inflate(R.layout.dialog_radio, null);
						final RadioGroup rg = (RadioGroup) la.findViewById(R.id.radioTipMar);
						// Inflate and set the layout for the dialog
						// Pass null as the parent view because its going in the dialog layout
						builder.setView(la)
						// Add action buttons
						.setPositiveButton("Crear", new DialogInterface.OnClickListener() 
						{
							@Override
							public void onClick(DialogInterface dialog, int id) 
							{
								int sel = rg.getCheckedRadioButtonId();
								Log.e("SELECCION", sel+"");
								if(sel == -1)
								{
									AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
									builder1.setMessage("Debe seleccionar algun opción para crear el marcador");
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
									Log.e("SELECCION", sel+"");
									int contIni = 0;
									int contFin = 0;
									if(sel==R.id.radTipMar0)
									{
										for(int i = 0;i<markers.size();i++)
										{
											Marcador mar = markers.get(i);
											if(mar.getTipo().equals("Inicio"))
											{
												contIni++;
											}
										}
										
										if(contIni==0)
										{
											Marcador m = new Marcador(new LatLng(d1,d2), 0);
											mapActivity.this.map.addMarker(new MarkerOptions()
											.position(new LatLng(d1, d2))
											.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
											agregarMarcador(m);
										}
										else
										{
											AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
											builder1.setMessage("Ya existe un marcador de inicio, intente eliminar el existente");
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
										
									}
									else if(sel==R.id.radTipMar1)
									{
										for(int i = 0;i<markers.size();i++)
										{
											Marcador mar = markers.get(i);
											if(mar.getTipo().equals("Fin"))
											{
												contFin++;
											}
										}
										if(contFin==0)
										{
											Marcador m = new Marcador(new LatLng(d1,d2), 1);
											mapActivity.this.map.addMarker(new MarkerOptions()
											.position(new LatLng(d1, d2))
											.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
											agregarMarcador(m);
										}
										else
										{
											AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
											builder1.setMessage("Ya existe un marcador de fin, intente eliminar el existente");
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
									}
									else
									{
										Marcador m = new Marcador(new LatLng(d1,d2), 2);
										mapActivity.this.map.addMarker(new MarkerOptions()
										.position(new LatLng(d1, d2))
										.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
										agregarMarcador(m);
									}
								}

							}})
							.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) 
								{
									dialog.cancel();
								}
							});     
						AlertDialog alert11 = builder.create();
						alert11.show();
					}
				}
			});
		}
	}

	public void showInfoMarker()
	{
		map.setOnMarkerClickListener(new OnMarkerClickListener() 
		{

			@Override
			public boolean onMarkerClick(Marker marker) 
			{
				map.setInfoWindowAdapter(new InfoWindowAdapter() 
				{

					@Override
					public View getInfoWindow(Marker marker) 
					{
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) 
					{
						LayoutInflater inflater = mapActivity.this.getLayoutInflater();
						View v = inflater.inflate(R.layout.info_window_marker, null);
						textTip = (TextView) v.findViewById(R.id.textTip);
						LatLng latLng = marker.getPosition();
						String tipo = "";
						String url = generateURL(latLng);
						DownloadAddressTask dat = new DownloadAddressTask();
						dat.execute(url);
						Log.d("MARCADORES", markers.size()+"");
						for(int i = 0;i<markers.size();i++)
						{
							Marcador m = markers.get(i);
							if(m==null)
							{
								Log.d("MARCADORES", "marcador nulo en i= " +i);
							}
							LatLng pos = m.getPosicion();
							if(pos.equals(latLng))
							{
								tipo = m.getTipo();
							}
							Log.d("MARCADOR-"+i,pos.toString()+", "+tipo);
						}
						textTip.setText(tipo);
						return v;
					}
				});
				return false;
			}
		});
	}

	private void agregarMarcador(Marcador marker)
	{
		markers.add(marker);
	}

	public void trazarRuta()
	{
		if(markers.size()<=1)
		{
			AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
			builder1.setMessage("Se necesitan al menos dos marcadores (Inicio, Fin) para trazar la ruta");
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
			if(markers.size()==2)
			{
				Marcador m1 = markers.get(0);
				Marcador m2 = markers.get(1);
				if(m1.getTipo().equals("Inicio") && m2.getTipo().equals("Fin"))
				{
					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(m1.getPosicion(), m2.getPosicion(),0,1);
					DownloadTask downloadTask = new DownloadTask();
					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
				}
				else if(m2.getTipo().equals("Inicio") && m1.getTipo().equals("Fin"))
				{
					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(m2.getPosicion(), m1.getPosicion(),1,0);
					DownloadTask downloadTask = new DownloadTask();
					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
				}
				else
				{
					AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
					builder1.setMessage("Se necesitan al menos dos marcadores (Inicio, Fin) para trazar la ruta");
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
			}
			else
			{
				LatLng ini = null;
				LatLng fin = null;
				int posIni = -1;
				int posFin = -1;
				for(int i = 0; i<markers.size();i++)
				{
					Marcador m = markers.get(i);
					String tipo = m.getTipo();
					
					if(tipo.equals("Inicio"))
					{
						ini = m.getPosicion();
						posIni = i;
					}
					else if(tipo.equals("Fin"))
					{
						fin = m.getPosicion();
						posFin = i;
					}
				}
				if(posIni==-1 || posFin == -1)
				{
					AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
					builder1.setMessage("Se necesitan al menos dos marcadores (Inicio, Fin) para trazar la ruta");
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
					// Getting URL to the Google Directions API
					String url = getDirectionsUrl(ini,fin,posIni,posFin);
					DownloadTask downloadTask = new DownloadTask();
					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
				}
			}
		}
	}

	private String getDirectionsUrl(LatLng origin,LatLng dest, int posIni, int posFin){

		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;

		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Waypoints
		String waypoints = "waypoints=optimize:true|";
		for(int i=0;i<markers.size();i++)
		{
			Marcador m = markers.get(i);
			LatLng point = m.getPosicion();
			if(i != markers.size()-1)
			{
				if(i != posIni && i != posFin)
				{
					waypoints += point.latitude + "," + point.longitude + "|";
				}
			}
			else
			{
				waypoints += point.latitude + "," + point.longitude;
			}
		}

		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+waypoints+"&"+sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException
	{
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb  = new StringBuffer();

			String line = "";
			while( ( line = br.readLine())  != null){
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		}catch(Exception e){
			Log.d("Exception while downloading url", e.toString());
		}finally{
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>
	{

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service

			String data = "";

			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try{
				jObject = new JSONObject(jsonData[0]);
				JSONParser parser = new JSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			}catch(Exception e){
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.BLUE);
			}

			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);
		}
	}
	
	private class DownloadAddressTask extends AsyncTask<String, Void, JSONObject>
	{

		@Override
		protected JSONObject doInBackground(String... url) 
		{
			HttpGet httpGet = new HttpGet(url[0]);
		    HttpClient client = new DefaultHttpClient();
		    HttpResponse response;
		    StringBuilder stringBuilder = new StringBuilder();

		    try 
		    {
		        response = client.execute(httpGet);
		        HttpEntity entity = response.getEntity();
		        InputStream stream = entity.getContent();
		        int b;
		        while ((b = stream.read()) != -1) 
		        {
		            stringBuilder.append((char) b);
		        }
		    } 
		    catch (ClientProtocolException e) 
		    {
		    } 
		    catch (IOException e) 
		    {
		    }

		    JSONObject jsonObject = new JSONObject();
		    try {
		        jsonObject = new JSONObject(stringBuilder.toString());
		    } catch (JSONException e) {
		        e.printStackTrace();
		    }

		    return jsonObject;
		}
		
		@Override
		protected void onPostExecute(JSONObject jsonObject)
		{
			super.onPostExecute(jsonObject);
			
			ParserAddressTask pat = new ParserAddressTask();
			pat.execute(jsonObject);
		}
	}
	
	private String generateURL(LatLng latlng)
	{
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+ latlng.latitude+","+latlng.longitude +"&sensor=true";
		return url;
	}
	
	private class ParserAddressTask extends AsyncTask<JSONObject, Void, String>
	{

		@Override
		protected String doInBackground(JSONObject... jSonObject) 
		{
			JSONObject jsonObj = jSonObject[0];
		    Log.i("JSON string =>", jsonObj.toString());

		    String currentLocation = "";
		    String street_address = null;
		    //String postal_code = null; 

		    try {
		        String status = jsonObj.getString("status").toString();
		        Log.i("status", status);

		        if(status.equalsIgnoreCase("OK")){
		            JSONArray results = jsonObj.getJSONArray("results");
		            int i = 0;
		            do{

		                JSONObject r = results.getJSONObject(i);
		                JSONArray typesArray = r.getJSONArray("types");
		                String types = typesArray.getString(0);

		                if(types.equalsIgnoreCase("street_address")){
		                    street_address = r.getString("formatted_address").split(",")[0];
		                    Log.i("street_address", street_address);
		                }
//		                else if(types.equalsIgnoreCase("postal_code")){
//		                    postal_code = r.getString("formatted_address");
//		                    Log.i("postal_code", postal_code);
//		                }

		                if(street_address!=null){
		                    currentLocation = street_address;
		                    Log.d("Current Location =>", currentLocation); //Delete this
		                    i = results.length();
		                }

		                i++;
		            }while(i<results.length());

		            Log.i("JSON Geo Locatoin =>", currentLocation);
		            return currentLocation;
		        }

		    } 
		    catch (JSONException e) 
		    {
		        Log.e("testing","Failed to load JSON");
		        e.printStackTrace();
		    }
		    return null;
		}
		
		@Override
		protected void onPostExecute(String res)
		{
			super.onPostExecute(res); 
			Log.e("POSTEXECUTE", res);
			Toast.makeText(mapActivity.this.getApplicationContext(), res, Toast.LENGTH_LONG).show();
		}
	}
	
	private void eliminarMarcador()
	{
		this.map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() 
		{
			
			@Override
			public void onInfoWindowClick(Marker marker) 
			{
				int posRemove = -1;
				marker.remove();
				for(int i = 0;i<markers.size();i++)
				{
					Marcador m = markers.get(i);
					LatLng l = m.getPosicion();
					if(marker.getPosition().equals(l))
					{
						posRemove = i;
						break;
					}
				}
				markers.remove(posRemove);
			}
		});
	}
	
	private void limpiarMapa()
	{
		map.clear();
		markers = new ArrayList<Marcador>();
	}
	
	
//	private class checkConnectionTask extends AsyncTask<Context, Void, String>
//	{
//		private mapActivity map;
//		
//		public checkConnectionTask(mapActivity m)
//		{
//			map = m;
//		}
//		@Override
//		protected String doInBackground(Context... params) 
//		{
//			String res = null;
//			ConnectivityManager connectivity = (ConnectivityManager) params[0].getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo nf=connectivity.getActiveNetworkInfo();
//			if(nf != null && nf.isConnected()==true )
//			{
//				res = "true";
//			}
//			else
//			{
//				res = "false";
//			}
//		    return res;
//		}
//		
//		protected void onPostExecute(String res)
//		{
//			Log.e("PROCESO","termino el proceso de chequeo de conexion =" + res);
//			map.processTracking(res);
//		}
//	}
	
//	private class ContinuosTracking extends AsyncTask<Context, Void, Void>
//	{
//		private mapActivity mapA;
//		private int modo;
//		public ContinuosTracking(mapActivity ma, int mod)
//		{
//			mapA = ma;
//			modo = mod;
//		}
//
//		@Override
//		protected Void doInBackground(Context... params) 
//		{
//			final Context con = params[0];
//			if(modo ==  Constantes.GSM_TRACKER)
//			{
//				while(mapA.endTracking == false)
//				{
//					
//				}
//			}
//			else
//			{
//				while(mapA.endTracking == false)
//				{
//					try 
//					{
//						mapA.runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() 
//							{
//								Toast.makeText(con, "Va comenzar el minuto y medio", Toast.LENGTH_LONG).show();
//
//							}
//						});
//						Thread.sleep(90000); // 1 minuto y medio
//						mapA.runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() 
//							{
//								Toast.makeText(con, "Termino el minuto y medio", Toast.LENGTH_LONG).show();
//							}
//						});
//						LocationManager locationManager = (LocationManager) params[0].getSystemService(Context.LOCATION_SERVICE);
//						Criteria criteria = new Criteria();
//						final Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//						mapA.marcadores.add(new LatLng(location.getLatitude(), location.getLongitude()));
//						mapA.runOnUiThread(new Runnable() 
//						{
//							@Override
//							public void run() 
//							{
//								map.addMarker(new MarkerOptions()
//								.position(new LatLng(location.getLatitude(), location.getLongitude()))
//								.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
//							}
//						});
//					} 
//					catch (InterruptedException e) 
//					{
//						e.printStackTrace();
//					}
//					
//				}
//			}
//			return null;
//		}
//	}
//	private void starTracking()
//	{
//		checkConnectionTask cct = new checkConnectionTask(this);
//		Log.e("PROCESO", "Va ejecutar el chequeo de conexion");
//		cct.execute(mapActivity.this.getApplicationContext());
//	}
//	
//	private void processTracking(String res)
//	{
//		if(res.equals("false"))
//		{
//			AlertDialog.Builder builder1 = new AlertDialog.Builder(mapActivity.this);
//			builder1.setMessage("Actualmente no hay conexion, se utilizara la opcion de ubicacion por GSM");
//			builder1.setCancelable(true);
//			builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() 
//			{
//				public void onClick(DialogInterface dialog, int id) 
//				{
//					dialog.cancel();
//				}
//			});
//			AlertDialog alert11 = builder1.create();
//			alert11.show();
//			ContinuosTracking ct = new ContinuosTracking(this, Constantes.GSM_TRACKER);
//		}
//		else
//		{
//			Log.e("PROCESO", "va a obtener una primera ubicacion ya que hay conexion");
//			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//			Criteria criteria = new Criteria();
//			Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//			if(location != null)
//			{
//				Log.e("PROCESO", "primera location: "+ location.getLatitude()+";"+location.getLongitude());
//				marcadores.add(new LatLng(location.getLatitude(), location.getLongitude()));
//				map.addMarker(new MarkerOptions()
//				.position(new LatLng(location.getLatitude(), location.getLongitude()))
//				.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
//				Log.e("PROCESO", "debio poner el marcador");
//				ContinuosTracking ct = new ContinuosTracking(this, Constantes.NETWORK_TRACKER);
//			}	
//		}
//	}

	@Override
	public void onConnected(Bundle connectionHint) 
	{
		if(contOnC ==0)
		{
			Log.e("PROCESO","LLEGO A ONCONNECTED");
			Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			Log.i("PROCESO", "SE HIZO EL PEDIDO DE LA ULTIMA LOCACION");
			if (location != null) 
			{
				Log.i("PROCESO", "NO LLEGO NULA LA ULTIMA LOCACION");
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
				.zoom(15)                   // Sets the zoom
				.bearing(90)                // Sets the orientation of the camera to east
				.tilt(40)                   // Sets the tilt of the camera to 30 degrees
				.build();                   // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				searchCircle = map.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(), location.getLongitude())).radius(1000));
				this.searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
		        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
				UiSettings localUiSettings2 = this.map.getUiSettings();
				localUiSettings2.setMyLocationButtonEnabled(true);
				localUiSettings2.setZoomControlsEnabled(true);
			}
			else
			{
				Log.i("PROCESO", "LLEGO NULA LA ULTIMA LOCACION");
				if(mGoogleApiClient.isConnected())
				{
					Log.i("PROCESO", "SE ESTA CONECTADO A LA GOOGLE API CLIENT");
					LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,new LocationRequest()
					.setInterval(10000)
					.setFastestInterval(8000)
					.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), (com.google.android.gms.location.LocationListener) this);
					Log.i("PROCESO", "SE ENVIO REQUEST DE UPDATE");
					vaARecibirOnLoc0 = true;
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			contOnC++;
		}
		else
		{
			//Se van a generar los updates de las posiciones
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,new LocationRequest()
			.setInterval(30000)
			.setFastestInterval(20000)
			.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY), mapActivity.this);
		}
	}

	@Override
	public void onConnectionSuspended(int cause) 
	{
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		if(vaARecibirOnLoc0 == true)
		{
			initial = location;
			LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
			if(initial != null)
			{
				Log.i("PROCESO", "REQUEST EXITOSO");
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(initial.getLatitude(), initial.getLongitude()), 13));
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(initial.getLatitude(), initial.getLongitude()))      // Sets the center of the map to location user
				.zoom(15)                   // Sets the zoom
				.bearing(90)                // Sets the orientation of the camera to east
				.tilt(40)                   // Sets the tilt of the camera to 30 degrees
				.build();                   // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				
			}
			else
			{
				Log.i("PROCESO", "REQUEST FALLIDO");
			}

			UiSettings localUiSettings2 = this.map.getUiSettings();
			localUiSettings2.setMyLocationButtonEnabled(true);
			localUiSettings2.setZoomControlsEnabled(true);
			vaARecibirOnLoc0 = false;
		}
		else
		{
			if(marcadores.size() == 0)
			{
				LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
				map.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
				marcadores.add(latlng);	
				String userName = user.getNombreUser();
				geoQuery = this.geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),location.getLongitude()),1);
				this.geoQuery.addGeoQueryEventListener(this);
				geoFire.setLocation("users/"+user.getKeyU()+"/"+userName+"/lastlatLong", new GeoLocation(latlng.latitude, latlng.longitude), new CompletionListener() {
					
					@Override
					public void onComplete(String key, FirebaseError error) 
					{
						if (error != null) 
						{
				            System.err.println("There was an error saving the location to GeoFire: " + error);
				        } 
						else 
						{
				            System.out.println("Location saved on server successfully!");
				        }
					}
				});
			}
			else if(marcadores.size() == 1)
			{
				LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
				inicial = map.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
				marcadores.add(latlng);
				animateMarkerTo(inicial, latlng.latitude, latlng.longitude);
				String userName = user.getNombreUser();
				geoQuery = this.geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),location.getLongitude()),1);
				this.geoQuery.addGeoQueryEventListener(this);
				geoFire.setLocation("users/"+user.getKeyU()+"/"+userName+"/lastlatLong", new GeoLocation(latlng.latitude, latlng.longitude), new CompletionListener() {

					@Override
					public void onComplete(String key, FirebaseError error) 
					{
						if (error != null) 
						{
							System.err.println("There was an error saving the location to GeoFire: " + error);
						} 
						else 
						{
							System.out.println("Location saved on server successfully!");
						}
					}
				});
			}
			else
			{
				LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
				marcadores.add(latlng);
				animateMarkerTo(inicial, latlng.latitude, latlng.longitude);
				String userName = user.getNombreUser();
				geoQuery = this.geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),location.getLongitude()),1);
				this.geoQuery.addGeoQueryEventListener(this);
				geoFire.setLocation("users/"+user.getKeyU()+"/"+userName+"/lastlatLong", new GeoLocation(latlng.latitude, latlng.longitude), new CompletionListener() {

					@Override
					public void onComplete(String key, FirebaseError error) 
					{
						if (error != null) 
						{
							System.err.println("There was an error saving the location to GeoFire: " + error);
						} 
						else 
						{
							System.out.println("Location saved on server successfully!");
						}
					}
				});
			}
		}
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) 
	{
		Log.e("PROCESO", "NO SE PUDO CONECTAR A GOOGLE API CLIENT");
	}

	@Override
	public void onKeyEntered(String key, GeoLocation location) 
	{
		Marker marker = this.map.addMarker(new MarkerOptions().
		icon(BitmapDescriptorFactory.fromResource(R.drawable.fmarker))
		.position(new LatLng(location.latitude, location.longitude)));
		markersF.put(key, marker);
        System.out.println(key);
	}

	@Override
	public void onKeyExited(String key) 
	{
		Marker marker = this.markersF.get(key);
        if (marker != null) {
            marker.remove();
            this.markersF.remove(key);
        }
	}

	@Override
	public void onKeyMoved(String key, GeoLocation location) 
	{
		// Move the marker
        Marker marker = this.markersF.get(key);
        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
	}

	@Override
	public void onGeoQueryReady() 
	{
		
	}

	@Override
	public void onGeoQueryError(FirebaseError error) 
	{
		new AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
        .setPositiveButton(android.R.string.ok, null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();
	}

	@Override
	public void onCameraChange(CameraPosition position) 
	{
		// Update the search criteria for this geoQuery and the circle on the map
        LatLng center = position.target;
        double radius = zoomLevelToRadius(position.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius/1000);
	}

	 private void animateMarkerTo(final Marker marker, final double lat, final double lng) 
	 {
	        final Handler handler = new Handler();
	        final long start = SystemClock.uptimeMillis();
	        final long DURATION_MS = 2800;
	        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
	        final LatLng startPosition = marker.getPosition();
	        handler.post(new Runnable() {
	            @Override
	            public void run() {
	                float elapsed = SystemClock.uptimeMillis() - start;
	                float t = elapsed/DURATION_MS;
	                float v = interpolator.getInterpolation(t);

	                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
	                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
	                marker.setPosition(new LatLng(currentLat, currentLng));

	                // if animation is not finished yet, repeat
	                if (t < 1) {
	                    handler.postDelayed(this, 16);
	                }
	            }
	        });
	    }
	 
	 private double zoomLevelToRadius(double zoomLevel) 
	 {
		 return 16384000/Math.pow(2, zoomLevel);
	 }
	 
	 private void cargarCuadrantes() throws ParserConfigurationException, SAXException, IOException
	 {
		 SAXParserFactory factory = SAXParserFactory.newInstance();
		 SAXParser saxParser = factory.newSAXParser();
		 
		 DefaultHandler handler = new DefaultHandler()
		 {
			 boolean bname = false;
			 boolean bdescr = false;
			 boolean bcoord = false;
			 PlaceMark pl = null;
			 
			 public void startElement(String uri, String localName,String qName, org.xml.sax.Attributes attributes)
			 {
				 if(qName.equalsIgnoreCase("Placemark"))
				 {
					 pl = new PlaceMark();
				 }
				 else if(qName.equalsIgnoreCase("name"))
				 {
					 bname = true;
				 }
				 else if(qName.equalsIgnoreCase("description"))
				 {
					 bdescr = true;
				 }
				 else if(qName.equalsIgnoreCase("coordinates"))
				 {
					 bcoord = true;
				 }
			 }
			 
			 public void endElement(String uri, String localName, String qName)
			 {
				 if(qName.equalsIgnoreCase("Placemark"))
				 {
					 cuadrantes.add(pl);
					 //System.out.println(pl.getNombre());
					 //System.out.println(pl.getCoordenadas());
					 //System.out.println(pl.getDescripcion());
					 pl = null;
				 }
			 }
			 
			 public void characters(char ch[], int start, int length)
			 {
				 if(bname)
				 {
					 pl.setNombre(new String(ch, start, length));
					 System.out.println(new String(ch, start, length));
					 bname = false;
				 }
				 if(bdescr)
				 {
					 pl.setDescripcion(new String(ch, start, length));
					 System.out.println(new String(ch, start, length));
					 //TODO Hacer split
					 bdescr =  false;
				 }
				 if(bcoord)
				 {
					 pl.setCoordenadas(new String(ch, start, length));
					 System.out.println(new String(ch, start, length));
					 //TODO Hacer split para guardar latlng
					 bcoord = false;
				 }
			 }
		 };
		 AssetManager asset = getAssets();
		 InputStream input = asset.open("doc.kml");
		 int size = input.available();
		 byte[] buffer = new byte[size];
		 input.read(buffer);
		 input.close();
		 File file = new File(getFilesDir(), "doc.kml");
		 FileOutputStream fos = new FileOutputStream(file);
		 fos.write(buffer);
		 fos.close();
		 saxParser.parse(file, handler);
	 }
	 
	 private void dibujarCuadrantes() 
	 {
		 for(int i = 0;i<cuadrantes.size();i++)
		 {
			 PlaceMark pl = cuadrantes.get(i);
			 String loc = pl.getCoordenadas();
			 System.out.println("COOR-----> "+loc);
			 String[] coor = loc.split(",");
			 if(coor.length == 1)
			 {
				 double lon = Double.parseDouble(coor[0]);
				 double lat = 4.757677;
				 LatLng latlo = new LatLng(lat, lon);
				 mapActivity.this.map.addMarker(new MarkerOptions()
				 .position(latlo)
				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.cop)));
			 }
			 else
			 {
				 double lat = Double.parseDouble(coor[1]);
				 double lon = Double.parseDouble(coor[0]);
				 LatLng latlo = new LatLng(lat, lon);
				 mapActivity.this.map.addMarker(new MarkerOptions()
				 .position(latlo)
				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.cop)));
			 }
		 }
	 }
}
