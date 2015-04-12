package com.sis.biciunidos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("InflateParams") public class mapActivity
extends ActionBarActivity
{
	//private final LatLng S_LOCATION = new LatLng(4.602664D, -74.066264000000004D);
	private GoogleMap map;
	private String numero;
	public ArrayList<Marcador> markers = new ArrayList<Marcador>();
	private TextView textTip;
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
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
				if (location != null)
				{
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
				}
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
				.zoom(17)                   // Sets the zoom
				.bearing(90)                // Sets the orientation of the camera to east
				.tilt(40)                   // Sets the tilt of the camera to 30 degrees
				.build();                   // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
				if (location != null)
				{
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
				}
				CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
				.zoom(17)                   // Sets the zoom
				.bearing(90)                // Sets the orientation of the camera to east
				.tilt(40)                   // Sets the tilt of the camera to 30 degrees
				.build();                   // Creates a CameraPosition from the builder
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				UiSettings localUiSettings2 = this.map.getUiSettings();
				localUiSettings2.setMyLocationButtonEnabled(true);
				localUiSettings2.setZoomControlsEnabled(true);
			}
		}
		pointLocation();
		showInfoMarker();
		eliminarMarcador();
		((ImageButton) findViewById(R.id.btnClean)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				limpiarMapa();
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
}
