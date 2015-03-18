package com.sis.biciunidos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import java.io.PrintStream;

public class SplashActivity
  extends Activity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_splash);
    if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("loggedIn", false))
    {
      System.out.println("No existe la preferencia");
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          Intent localIntent = new Intent(SplashActivity.this, LoginActivity.class);
          SplashActivity.this.startActivity(localIntent);
          SplashActivity.this.finish();
        }
      }, 2000);
      return;
    }
    System.out.println("Existe la preferencia");
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        Intent localIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(localIntent);
        SplashActivity.this.finish();
      }
    }, 1000);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.activity_main_actions, paramMenu);
    return true;
  }
}