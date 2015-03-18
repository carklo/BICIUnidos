package com.sis.biciunidos.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiverActivity
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
	  //---get the SMS message passed in---
      Bundle bundle = paramIntent.getExtras();        
      SmsMessage[] msgs = null;
      String str = "";            
      if (bundle != null)
      {
          //---retrieve the SMS message received---
          Object[] pdus = (Object[]) bundle.get("pdus");
          msgs = new SmsMessage[pdus.length];            
          for (int i=0; i<msgs.length; i++){
              msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
              str += "SMS from " + msgs[i].getOriginatingAddress();                     
              str += " :";
              str += msgs[i].getMessageBody().toString();
              str += "\n";        
          }
          //---display the new SMS message---
          Toast.makeText(paramContext, str, Toast.LENGTH_LONG).show();
          
      }                         
  }
}
