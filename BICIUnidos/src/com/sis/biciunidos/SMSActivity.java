package com.sis.biciunidos;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSActivity
  extends ActionBarActivity
{
  Button btnSendSMS;
  EditText txtMessage;
  EditText txtPhoneNo;
  
  private void sendSMS(String phoneNumber, String message)
  {        
      String SENT = "SMS_SENT";
      String DELIVERED = "SMS_DELIVERED";

      PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
          new Intent(SENT), 0);

      PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
          new Intent(DELIVERED), 0);

      //---when the SMS has been sent---
      registerReceiver(new BroadcastReceiver(){
          @Override
          public void onReceive(Context arg0, Intent arg1) {
              switch (getResultCode())
              {
                  case Activity.RESULT_OK:
                      Toast.makeText(getBaseContext(), "SMS sent", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                      Toast.makeText(getBaseContext(), "Generic failure", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_NO_SERVICE:
                      Toast.makeText(getBaseContext(), "No service", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_NULL_PDU:
                      Toast.makeText(getBaseContext(), "Null PDU", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case SmsManager.RESULT_ERROR_RADIO_OFF:
                      Toast.makeText(getBaseContext(), "Radio off", 
                              Toast.LENGTH_SHORT).show();
                      break;
              }
          }
      }, new IntentFilter(SENT));

      //---when the SMS has been delivered---
      registerReceiver(new BroadcastReceiver(){
          @Override
          public void onReceive(Context arg0, Intent arg1) {
              switch (getResultCode())
              {
                  case Activity.RESULT_OK:
                      Toast.makeText(getBaseContext(), "SMS delivered", 
                              Toast.LENGTH_SHORT).show();
                      break;
                  case Activity.RESULT_CANCELED:
                      Toast.makeText(getBaseContext(), "SMS not delivered", 
                              Toast.LENGTH_SHORT).show();
                      break;                        
              }
          }
      }, new IntentFilter(DELIVERED));        

      SmsManager sms = SmsManager.getDefault();
      sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_sms);
    this.btnSendSMS = ((Button)findViewById(R.id.btnSendSMS));
    this.txtPhoneNo = ((EditText)findViewById(R.id.txtPhoneNo));
    this.txtMessage = ((EditText)findViewById(R.id.txtMessage));
    this.btnSendSMS.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        String str1 = SMSActivity.this.txtPhoneNo.getText().toString();
        String str2 = SMSActivity.this.txtMessage.getText().toString();
        if ((str1.length() > 0) && (str2.length() > 0))
        {
          SMSActivity.this.sendSMS(str1, str2);
          return;
        }
        else
        {
        	Toast.makeText(SMSActivity.this.getBaseContext(), "Por favor ingrese tanto el numero de contacto y el mensaje", Toast.LENGTH_SHORT).show();
        }
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
