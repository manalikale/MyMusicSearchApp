package com.example.mymusicsearchapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends Activity {

	//declare global variables here
	ProgressDialog dialogue;
	Context ctx;
	Spinner myspinner;
	EditText myedittext;
	String name;
	String type;
	String url;
	StringBuffer resultString;
	String returnString;
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	Activity currentActivity=this;
	static String TYPE="TYPE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("Spinnerrrrrrrrrrr","inside create");
		setContentView(R.layout.activity_main);
		ctx=this;
		
		Button btn = (Button)findViewById(R.id.button1);
		myspinner=(Spinner)findViewById(R.id.spinner1);
		myedittext=(EditText)findViewById(R.id.editText1);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Log.i("Spinnerrrrrrrrrrr","inside");
				type = myspinner.getSelectedItem().toString();
				name = myedittext.getText().toString();
				name=name.trim();
				Log.i("Spinnerrrrrrrrrrr in onClick",type);
				Log.i("Spinnerrrrrrrrrrr in onClick",name);			
				new ASyncTaskClass().execute();
			}
		});
	
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	class ASyncTaskClass extends AsyncTask<String, String, String>
	{ 
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogue=new ProgressDialog(ctx);
			dialogue.setMessage("Loading");
			dialogue.setIndeterminate(false);
			dialogue.setCancelable(true);
			dialogue.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.i("Spinnerrrrrrrrrrr in bg",type);
			Log.i("Spinnerrrrrrrrrrr in bg",name);
			
			//perform validation
			if(name.equalsIgnoreCase(""))
			{
				Log.i("Spinnerrrrrrrrrrr error empty string",name);
				return null;
			}
			
			url="http://cs-server.usc.edu:35431/examples/servlet/HelloWorldExample?name="+name+"&sel="+type;
			Log.i("Spinnerrrrrrrrrrr in bg",url);
			
			//create a request depending on browser being used
			try 
			{
				Log.i("Spinnerrrrrrrrrrr","inside try");
				URL myurl=new URL(url);
				Log.i("Spinnerrrrrrrrrrr","created url");
				URLConnection urlConnection = myurl.openConnection();
				//urlConnection.setAllowUserInteraction(false);
				//urlConnection.setReadTimeout(1000);
				Log.i("Spinnerrrrrrrrrrr","connection created");
				resultString = new StringBuffer();
				Log.i("Spinnerrrrrrrrrrr","string made");
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
				Log.i("Spinnerrrrrrrrrrr","br declared");
				String jasonInput;
				Log.i("Spinnerrrrrrrrrrr","jason string made. starting while here");
				while((jasonInput=br.readLine()) != null)
				{
					resultString.append(jasonInput);
					Log.i("Spinnerrrrrrrrrrr","inside while");
				}
				Log.i("Spinnerrrrrrrrrrr","outside while");
				br.close();
				Log.i("Spinnerrrrrrrrrrr","afer closing br");
				returnString=new String(resultString);
				Log.i("Spinnerrrrrrrrrrr","return string ready");
				Log.i("Spinnerrrrrrrrrrr in jason string",returnString);
				
				
			} 
			catch (MalformedURLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("Spinnerrrrrrrrrrr","before return");
			return returnString;
		}
		
		
		

		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialogue.dismiss();
			Log.i("Newwwwwwwwwwwwwwwwwwwww ","I am in onPostExecute. Creating intent");
			Intent intent = new Intent(currentActivity, DisplayDataActivity.class);
			intent.putExtra(EXTRA_MESSAGE, returnString);
			intent.putExtra(TYPE, type);
		    startActivity(intent);
			Log.i("Newwwwwwwwwwwwwwwwwwwww ", "Created intent");
		}

		
	}

}
