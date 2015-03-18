package com.sis.biciunidos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import java.io.IOException;
import java.io.PrintStream;

public class RecibidorAlarmaActivity
  extends Activity
{
  private MediaPlayer mMediaPlayer;
  
  private Uri getAlarmUri()
  {
    Uri localUri = RingtoneManager.getDefaultUri(4);
    if (localUri == null)
    {
      localUri = RingtoneManager.getDefaultUri(2);
      if (localUri == null) {
        localUri = RingtoneManager.getDefaultUri(1);
      }
    }
    return localUri;
  }
  
  private void playSound(Context paramContext, Uri paramUri)
  {
    this.mMediaPlayer = new MediaPlayer();
    try
    {
      this.mMediaPlayer.setDataSource(paramContext, paramUri);
      if (((AudioManager)paramContext.getSystemService("audio")).getStreamVolume(4) != 0)
      {
        this.mMediaPlayer.setAudioStreamType(4);
        this.mMediaPlayer.prepare();
        this.mMediaPlayer.start();
      }
      return;
    }
    catch (IOException localIOException)
    {
      System.out.println("OOPS");
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(R.layout.activity_recibidor_alarma);
    playSound(this, getAlarmUri());
    ((Button)findViewById(R.id.btnAlarm1)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RecibidorAlarmaActivity.this.mMediaPlayer.stop();
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(RecibidorAlarmaActivity.this.getApplicationContext()).edit();
        localEditor.putBoolean("AlquilerVigente", false);
        localEditor.putString("FechaAlquiler", null);
        localEditor.commit();
        Intent localIntent = new Intent(RecibidorAlarmaActivity.this, MainActivity.class);
        RecibidorAlarmaActivity.this.startActivity(localIntent);
        RecibidorAlarmaActivity.this.finish();
      }
    });
  }
}