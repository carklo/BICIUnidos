package com.sis.biciunidos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

public class MultiTouch extends Activity
{
    public MultiTouch()
    {
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
       final int actionPeformed = ev.getAction();
       switch(actionPeformed){
          case MotionEvent.ACTION_DOWN:
          {
             
          break;
         }
          case MotionEvent.ACTION_MOVE:{
              Context contxt = getApplicationContext();
              Toast.makeText(contxt, "Fue un multitouch", Toast.LENGTH_SHORT).show();
          break;
         }
       }
       return true;

    }

}
