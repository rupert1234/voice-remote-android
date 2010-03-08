package com.korzinek.speech;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;


public class SpeechGatewayActivity extends Activity implements OnTouchListener, OnClickListener{
	

	@Override
	protected void onDestroy() {
		audioInput.release();
		audioInput=null;
		super.onDestroy();
	}

	AudioRecord audioInput;
	AudioReader audioReader;
	Thread audioReaderThread;
	Handler consoleHandler;
	String server;
	int port;
	String dialogMessage;
	
	Dialog errorDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.record_button);
        button.setOnTouchListener(this);
        
        
		consoleHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				dialogMessage=msg.getData().getString("msg");
				showDialog(0);
			}
			
		};
		
		int audioBuf = AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT) * 2;
		
		audioInput = new AudioRecord(MediaRecorder.AudioSource.MIC,
                16000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                audioBuf);
		
		audioReader=null;		 
		 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,1,0,"Setings");
		menu.add(0,0,0,"Exit");
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		
		if(id==0)
		{
	        errorDialog=new Dialog(this);
	        errorDialog.setContentView(R.layout.error_dialog);
	        errorDialog.setTitle("Error");
	        Button button = (Button)errorDialog.findViewById(R.id.Button01);
	        button.setOnClickListener(this);
			return errorDialog;
		}
		
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if(id==0)
		{
			TextView text=(TextView)dialog.findViewById(R.id.TextView01);
			if(dialog.isShowing())
				text.append("\n"+dialogMessage);
			else
				text.setText(dialogMessage);
			
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==0)
		{
			finish();
		}
		else
		{
			Intent intent=new Intent();
			intent.setClass(SpeechGatewayActivity.this, SettingsActivity.class);
			startActivity(intent);
		}
			
		return true;
	}

	public void onClick(View v) {			
		errorDialog.cancel();			
	}


	public boolean onTouch(View v, MotionEvent ev) {

		if(ev.getAction()==MotionEvent.ACTION_DOWN)
		{
			SharedPreferences settings = getSharedPreferences("speech_remote.prefs", 0);
			server=settings.getString("server", "192.168.0.13");
			port=settings.getInt("port", 5555);			
			
			if(audioReader==null) audioReader=new AudioReader(consoleHandler,audioInput,server,port);
			audioReaderThread=new Thread(audioReader);
			audioReaderThread.start();
			return true;
		}
		
		if(ev.getAction()==MotionEvent.ACTION_UP)
		{
			audioReader.stop();
			audioReader=null;
			return true;
		}
		
		return false;
	}
}