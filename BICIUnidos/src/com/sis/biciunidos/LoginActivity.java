package com.sis.biciunidos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.shaded.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;

/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************ In order for Google+ sign in
 * to work with your app, you must first go to:
 * https://developers.google.com/+/mobile
 * /android/getting-started#step_1_enable_the_google_api and follow the steps in
 * "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends ActionBarActivity implements
		LoaderCallbacks<Cursor> {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	public String mEmail;
	public String mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().show();
		getSupportActionBar().setLogo(R.drawable.ic_launcher);
		
		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		populateAutoComplete();

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
		
		((Button)findViewById(R.id.Sing_up)).setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// Reset errors.
				mEmailView.setError(null);
				mPasswordView.setError(null);

				// Store values at the time of the login attempt.
				String email = mEmailView.getText().toString();
				String password = mPasswordView.getText().toString();

				boolean cancel = false;
				View focusView = null;

				// Check for a valid password, if the user entered one.
				if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
					mPasswordView.setError(getString(R.string.error_invalid_password));
					focusView = mPasswordView;
					cancel = true;
				}

				// Check for a valid email address.
				if (TextUtils.isEmpty(email)) {
					mEmailView.setError(getString(R.string.error_field_required));
					focusView = mEmailView;
					cancel = true;
				} else if (!isEmailValid(email)) {
					mEmailView.setError(getString(R.string.error_invalid_email));
					focusView = mEmailView;
					cancel = true;
				}

				if (cancel) {
					// There was an error; don't attempt login and focus the first
					// form field with an error.
					focusView.requestFocus();
				} else {
					// Show a progress spinner, and kick off a background task to
					// perform the user login attempt.
					showProgress(true);
					RegisterTask rt = new RegisterTask(email, password);
					rt.execute((Void) null);
				}
			}
		});
	}

	private void populateAutoComplete() {
		getLoaderManager().initLoader(0, null, this);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() 
	{
		mAuthTask = null;
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		// TODO: Replace this with your own logic
		return password.length() > 4;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}


	/**
	 * Check if the device supports Google Play Services. It's best practice to
	 * check first rather than handling this as an error case.
	 * 
	 * @return whether the device supports Google Play Services
	 */
	private boolean supportsGooglePlayServices() {
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
				ProfileQuery.PROJECTION,

				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE + " = ?",
				new String[] { ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE },

				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}

		addEmailsToAutoComplete(emails);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	private interface ProfileQuery {
		String[] PROJECTION = { ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY, };

		int ADDRESS = 0;
	}

	private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
		// Create adapter to tell the AutoCompleteTextView what to show in its
		// dropdown list.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				LoginActivity.this,
				android.R.layout.simple_dropdown_item_1line,
				emailAddressCollection);

		mEmailView.setAdapter(adapter);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mEmail;
		private final String mPassword;

		UserLoginTask(String email, String password) {
			mEmail = email;
			mPassword = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) 
		{	
			Firebase.setAndroidContext(LoginActivity.this);
			Firebase ref = new Firebase(Constantes.FIRE_REF);
			ref.authWithPassword(mEmail, mPassword, new Firebase.AuthResultHandler() 
			{
			    @Override
			    public void onAuthenticated(AuthData authData) 
			    {
			        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
			        LoginActivity.this.mAuthTask = null;
				    LoginActivity.this.showProgress(false); 
				    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this.getApplicationContext()).edit();
				    localEditor.putBoolean("loggedIn", true);
				    localEditor.putString("User", mEmail);
				    System.out.println("email-->"+LoginActivity.this.mEmail);
				    System.out.println(mEmail);
				    localEditor.putString("Pass", mPassword);
				    localEditor.commit();
				    Intent localIntent = new Intent(LoginActivity.this, FormularioActivity.class);
				    LoginActivity.this.startActivity(localIntent);
				    LoginActivity.this.finish();
				    return;
			    }
			    @Override
			    public void onAuthenticationError(FirebaseError firebaseError) 
			    {
			        // there was an error
			    	final FirebaseError fe = firebaseError;
			    	runOnUiThread(new Runnable() 
			        {
						public void run() 
						{
							showProgress(false);
							AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
							builder1.setTitle("Inicio de sesion");
							builder1.setMessage("Ocurrio un error en la autenticacion: "+fe.getMessage() +" "+", intentelo mas tarde");
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
					});
			    }
			});
			return true;
		}

		@Override
		protected void onCancelled() 
		{
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	private class RegisterTask extends AsyncTask<Void, Void, Void>
	{

		private String email;
		private String pass;
		public RegisterTask(String mEmail, String mPass)
		{
			email = mEmail;
			pass = mPass;
		}
		
		@Override
		protected Void doInBackground(Void... params) 
		{
			final StringBuffer s = new StringBuffer();
			Firebase.setAndroidContext(LoginActivity.this);
			Firebase ref = new Firebase(Constantes.FIRE_REF);
			ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
			    @Override
			    public void onSuccess(Map<String, Object> result) 
			    {
			        Log.e("Successfully created user account with uid: ",result.get("uid")+"");
			        s.append("true");
			        runOnUiThread(new Runnable() 
			        {
						public void run() 
						{
							showProgress(false);
							AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
							builder1.setTitle("Crear cuenta: Exito!");
							builder1.setMessage("Su cuenta fue creada exitosamente");
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
					});
			    }
			    @Override
			    public void onError(FirebaseError firebaseError) 
			    {
			    	final FirebaseError fe = firebaseError;
			    	runOnUiThread(new Runnable() 
			        {
						public void run() 
						{
							showProgress(false);
							AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
							builder1.setTitle("Crear cuenta: Error!");
							builder1.setMessage("Ocurrio un error al crear su cuenta: "+fe.getMessage());
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
					});
			    }
			});
			return null;
		}
	}
}
