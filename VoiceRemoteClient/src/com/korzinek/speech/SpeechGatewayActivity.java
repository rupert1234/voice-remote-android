package com.korzinek.speech;

import android.app.Activity;
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

	TextView console;
	AudioRecord audioInput;
	AudioReader audioReader;
	Thread audioReaderThread;
	Handler consoleHandler;
	String server;
	int port;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)findViewById(R.id.record_button);
        button.setOnTouchListener(this);
        
        Button exit = (Button)findViewById(R.id.Exit);
        exit.setOnClickListener(this);        
		
		console=(TextView)findViewById(R.id.console);
		consoleHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				String text=msg.getData().getString("msg");
				console.append(text);
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
		menu.add("Setings");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		Intent intent=new Intent();
		intent.setClass(SpeechGatewayActivity.this, Settings.class);
		startActivity(intent);
			
		return true;
	}

	public void onClick(View v) {			
		finish();				
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