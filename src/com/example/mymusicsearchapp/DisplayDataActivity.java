package com.example.mymusicsearchapp;

import java.util.Iterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.widget.TextView;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;



public class DisplayDataActivity extends Activity {
	
	//declare global variables
	String name;
	String results;
	String currentDynamicKey;
	String nameArray[]=new String[5];
	int length;
	int i;
	TableRow tr;
	TextView tv;
	TableLayout table;
	ScrollView sv;
	HorizontalScrollView hsv;
	ListView listview;
	JSONArray jsonArray;
	Bitmap myBitMap[]=new Bitmap[5];
	String type;
	Activity myActivity;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_data);		
		listview = (ListView) findViewById(R.id.ListView1);
		myActivity=this;
		  
		
		// Get the message from the intent
	    Intent intent = getIntent();
	    String resultString = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
	    type=intent.getStringExtra(MainActivity.TYPE);
	    

	    
	    try 
	    {
	    	JSONObject json=(JSONObject) new JSONTokener(resultString).nextValue();
	    	JSONObject jsonR = json.getJSONObject("results");
	    	jsonArray=jsonR.getJSONArray("result");
	    	name=json.toString();
	    	length=jsonArray.length();
	    	name="Length of Array is "+length;
	    	
	    	String urlArray[]=new String[length];
	    	
	    	
	    	for(i=0; i<length; i++)
	    	{
	    		JSONObject myJsonObject=jsonArray.getJSONObject(i);
	    		if(type.equalsIgnoreCase("artists"))
	    		{
	    			String a_cover=URLDecoder.decode(myJsonObject.getString("cover"), "UTF-8");
		    		if(a_cover.equalsIgnoreCase("speaker.png"))
		    		{
		    			a_cover="http://cs-server.usc.edu:35430/speaker.png";
		    		}
		    		urlArray[i]=a_cover;
	    		}
	    		if(type.equalsIgnoreCase("songs"))
	    		{
	    			String a_sample=URLDecoder.decode(myJsonObject.getString("sample"), "UTF-8");
	    			//if(a_sample.equalsIgnoreCase("headphones.png") || a_sample.equalsIgnoreCase("N/A"))
		    		//{
	    				a_sample="http://cs-server.usc.edu:35430/headphones.png";
		    		//}
		    		urlArray[i]=a_sample;
		    		Log.i("Sampleeeeeeeeeee",a_sample);
	    		}
	    		if(type.equalsIgnoreCase("albums"))
	    		{
	    			String a_cover=URLDecoder.decode(myJsonObject.getString("cover"), "UTF-8");
	    			if(a_cover.equalsIgnoreCase("disk.png"))
		    		{
		    			a_cover="http://cs-server.usc.edu:35430/disk.png";
		    		}
		    		urlArray[i]=a_cover;
	    		}
	    		
	    		/*Log.i("JAAAsssssoooooonnnnnnn",URLDecoder.decode(a_cover, "UTF-8"));
	    		String a_name=myJsonObject.getString("name");
	    		Log.i("JAAAsssssoooooonnnnnnn",URLDecoder.decode(a_name, "UTF-8"));
	    		String a_genre=myJsonObject.getString("genre");
	    		Log.i("JAAAsssssoooooonnnnnnn",URLDecoder.decode(a_genre, "UTF-8"));
	    		String a_year=myJsonObject.getString("year");
	    		Log.i("JAAAsssssoooooonnnnnnn",URLDecoder.decode(a_year, "UTF-8"));
	    		String a_details=myJsonObject.getString("details");
	    		Log.i("JAAAsssssoooooonnnnnnn", URLDecoder.decode(a_details, "UTF-8"));*/
	    		
	    	}
	    	
		    new MyAsyncTask().execute(urlArray);	    			    	
	    	//tryign to call onclickListener
		    listview.setClickable(true);
		    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		        @SuppressWarnings("deprecation")
				@Override
		        public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) 
		        {
		            Object o = listview.getItemAtPosition(position);
		            String str=(String)o;//As you are using Default String Adapter
		            //Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
		            
		            AlertDialog alertDialog = new AlertDialog.Builder(myActivity).create();
		            alertDialog.setTitle("Post to Facebook");
		            //alertDialog.setMessage("Are you sure?");
		            alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
		            {
		            	public void onClick(DialogInterface dialog, int which) 
		            	{
		            		// here you can add functions
		            		
		            		Session.openActiveSession(myActivity, true, new Session.StatusCallback() 
		            		{
		            			// callback when session changes state
		            			@Override
		            			public void call(Session session, SessionState state, Exception exception) 
		            			{
		            				if (session.isOpened()) 
		            				{
		            					
		            					//create new JSOn Object
		            					JSONObject myJsonObject;
										try 
										{
											myJsonObject = jsonArray.getJSONObject(position);
											
											String line1 = null, line2 = null;
											String a_name = null, a_cover = null, a_genre, a_year, a_details = null, a_artist, a_sample, a_performer, a_composer;
											
											if(type.equalsIgnoreCase("albums"))
											{
												a_cover=myJsonObject.getString("cover");
										    	a_cover=URLDecoder.decode(a_cover, "UTF-8");
										    	a_name=myJsonObject.getString("name");
										    	a_name=URLDecoder.decode(a_name, "UTF-8");
										    	a_artist=myJsonObject.getString("artist");
										    	a_artist=URLDecoder.decode(a_artist, "UTF-8");
										    	a_genre=myJsonObject.getString("genre");
										    	a_genre=URLDecoder.decode(a_genre, "UTF-8");
										    	a_year=myJsonObject.getString("year");
										    	a_year=URLDecoder.decode(a_year, "UTF-8");
										    	a_details=myJsonObject.getString("details");
										    	a_details=URLDecoder.decode(a_details, "UTF-8");
										    	

												line1="I like "+a_name+" released in year "+a_year;
												line2="Artist "+a_artist+" Genre "+a_genre;
											}
											if(type.equalsIgnoreCase("artists"))
											{
												a_cover=myJsonObject.getString("cover");
									    		a_cover=URLDecoder.decode(a_cover, "UTF-8");
									    		a_name=myJsonObject.getString("name");
									    		a_name=URLDecoder.decode(a_name, "UTF-8");
									    		a_genre=myJsonObject.getString("genre");
									    		a_genre=URLDecoder.decode(a_genre, "UTF-8");
									    		a_year=myJsonObject.getString("year");
									    		a_year=URLDecoder.decode(a_year, "UTF-8");
									    		a_details=myJsonObject.getString("details");
									    		a_details=URLDecoder.decode(a_details, "UTF-8");
									    		
									    		line1="I like "+a_name+" who is active since year "+a_year;
												line2="Genre of music is "+a_genre;
											}
											if(type.equalsIgnoreCase("songs"))
											{
												a_cover=myJsonObject.getString("sample");
												a_cover=URLDecoder.decode(a_cover, "UTF-8");
									    		a_name=myJsonObject.getString("title");
									    		a_name=URLDecoder.decode(a_name, "UTF-8");
									    		a_performer=myJsonObject.getString("performer");
									    		a_performer=URLDecoder.decode(a_performer, "UTF-8");
									    		a_composer=myJsonObject.getString("composer");
									    		a_composer=URLDecoder.decode(a_composer, "UTF-8");
									    		a_details=myJsonObject.getString("details");
									    		a_details=URLDecoder.decode(a_details, "UTF-8");
									    		
									    		
									    		line1="I like "+a_name+" composed by "+a_composer;
												line2="Performed by "+a_performer;
											}
			            		    		
			            		    		
			            		    		
			            		    		Bundle params = new Bundle();
			            				    params.putString("name", a_name);
			            				    params.putString("caption", line1);
			            				    params.putString("description", line2);
			            				    params.putString("link", a_details);
			            				    params.putString("picture", a_cover);
			            				    
			            				    WebDialog feedDialog = (
				            				        new WebDialog.FeedDialogBuilder(myActivity,
				            				            Session.getActiveSession(),
				            				            params))
				            				        .setOnCompleteListener(new OnCompleteListener() {

				            				            //@Override
				            				            public void onComplete(Bundle values,
				            				                FacebookException error) {
				            				                if (error == null) {
				            				                    // When the story is posted, echo the success
				            				                    // and the post Id.
				            				                    final String postId = values.getString("post_id");
				            				                    if (postId != null) {
				            				                        Toast.makeText(myActivity,
				            				                            "Posted story, id: "+postId,
				            				                            Toast.LENGTH_SHORT).show();
				            				                    } else {
				            				                        // User clicked the Cancel button
				            				                        Toast.makeText(myActivity.getApplicationContext(), 
				            				                            "Publish cancelled", 
				            				                            Toast.LENGTH_SHORT).show();
				            				                    }
				            				                } else if (error instanceof FacebookOperationCanceledException) {
				            				                    // User clicked the "x" button
				            				                    Toast.makeText(myActivity.getApplicationContext(), 
				            				                        "Publish cancelled", 
				            				                        Toast.LENGTH_SHORT).show();
				            				                } else {
				            				                    // Generic, ex: network error
				            				                    Toast.makeText(myActivity.getApplicationContext(), 
				            				                        "Error posting story", 
				            				                        Toast.LENGTH_SHORT).show();
				            				                }
				            				            }

				            				        })
				            				        .build();
				            				    feedDialog.show();
										}
										
										catch (JSONException e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 
										
										catch (UnsupportedEncodingException e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		            					
		            					

		            				    
		            					
		            				}
		            			}	
		            	    });
		            	}
		            }
		        );
		            //alertDialog.setIcon(R.drawable.icon);
		            alertDialog.show();
		            
		       }
		       
		    });
	    	
			
		} 
	    catch (JSONException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	
	class MyAsyncTask extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) 
		{
			InputStream is;
			try {
				for(int j=0; j<params.length; j++)
				{
					is = (InputStream) new URL(params[j]).getContent();
					Log.i("bbbbbbbbbbbbb", "input stream create");
					myBitMap[j]=BitmapFactory.decodeStream(is);
					Log.i("bbbbbbbbbbbbb", "bitmap entry created");
				}
				
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
			catch(Exception e)
			{
				e.printStackTrace();
			}
    		return null;
		}
		
		@Override
		protected void onPostExecute(Void result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(type.equalsIgnoreCase("albums"))
			{
				AlbumsAdapter albumsAdapter=new AlbumsAdapter();
		    	listview.setAdapter(albumsAdapter);
		    	albumsAdapter.notifyDataSetChanged();
			}
			else
			{
				ArtistAdapter artistAdapter=new ArtistAdapter();
		    	listview.setAdapter(artistAdapter);
		    	artistAdapter.notifyDataSetChanged();
			}
			
		}
		
	}
	
	class ArtistAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertView==null)
			{
				convertView=getLayoutInflater().inflate(R.layout.list_design, null);
			}
			
			
			try
			{
				if(type.equalsIgnoreCase("artists"))
				{
					
					ImageView cover=(ImageView) convertView.findViewById(R.id.imageView1);
					TextView name=(TextView) convertView.findViewById(R.id.textView1);
					TextView genre=(TextView) convertView.findViewById(R.id.TextView01);
					TextView year=(TextView) convertView.findViewById(R.id.TextView02);
					
					JSONObject myJsonObject=jsonArray.getJSONObject(position);
		    		String a_cover=myJsonObject.getString("cover");
		    		a_cover=URLDecoder.decode(a_cover, "UTF-8");
		    		String a_name=myJsonObject.getString("name");
		    		a_name=URLDecoder.decode(a_name, "UTF-8");
		    		String a_genre=myJsonObject.getString("genre");
		    		a_genre=URLDecoder.decode(a_genre, "UTF-8");
		    		String a_year=myJsonObject.getString("year");
		    		a_year=URLDecoder.decode(a_year, "UTF-8");
		    		String a_details=myJsonObject.getString("details");
		    		a_details=URLDecoder.decode(a_details, "UTF-8");
		    		
		    		//setting variables
		    		
		    		cover.setImageBitmap(myBitMap[position]);
					genre.setText(a_genre);
					year.setText(a_year);
					name.setText(a_name);
				}
				
				if(type.equalsIgnoreCase("songs"))
				{
					
					ImageView sample=(ImageView) convertView.findViewById(R.id.imageView1);
					TextView name=(TextView) convertView.findViewById(R.id.textView1);
					TextView genre=(TextView) convertView.findViewById(R.id.TextView01);
					TextView year=(TextView) convertView.findViewById(R.id.TextView02);
					
					JSONObject myJsonObject=jsonArray.getJSONObject(position);
		    		String a_sample=myJsonObject.getString("sample");
		    		a_sample=URLDecoder.decode(a_sample, "UTF-8");
		    		String a_name=myJsonObject.getString("title");
		    		a_name=URLDecoder.decode(a_name, "UTF-8");
		    		String a_genre=myJsonObject.getString("performer");
		    		a_genre=URLDecoder.decode(a_genre, "UTF-8");
		    		String a_year=myJsonObject.getString("composer");
		    		a_year=URLDecoder.decode(a_year, "UTF-8");
		    		/*String a_details=myJsonObject.getString("details");
		    		a_details=URLDecoder.decode(a_details, "UTF-8");*/
		    		
		    		//setting variables
		    		
		    		sample.setImageBitmap(myBitMap[position]);
					genre.setText(a_genre);
					year.setText(a_year);
					name.setText(a_name);
				}
			
    		
    		
			}
			catch(JSONException e1)
			{
				e1.printStackTrace();
			}
			catch(UnsupportedEncodingException e2)
			{
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return convertView;
		}
		
	}
	
	
	class AlbumsAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) 
		{
			if(convertView==null)
			{
				convertView=getLayoutInflater().inflate(R.layout.albums_design, null);
			}
			
			
			try
			{
				ImageView cover=(ImageView) convertView.findViewById(R.id.ImageView1);
				TextView name=(TextView) convertView.findViewById(R.id.textView1);
				TextView artist=(TextView) convertView.findViewById(R.id.TextView01);
				TextView genre=(TextView) convertView.findViewById(R.id.TextView02);
				TextView year=(TextView) convertView.findViewById(R.id.textView2);
					
				JSONObject myJsonObject=jsonArray.getJSONObject(position);
		    	String a_cover=myJsonObject.getString("cover");
		    	a_cover=URLDecoder.decode(a_cover, "UTF-8");
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa",a_cover);
		    	String a_name=myJsonObject.getString("name");
		    	a_name=URLDecoder.decode(a_name, "UTF-8");
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa",a_name);
		    	String a_artist=myJsonObject.getString("artist");
		    	a_artist=URLDecoder.decode(a_artist, "UTF-8");
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa",a_artist);
		    	String a_genre=myJsonObject.getString("genre");
		    	a_genre=URLDecoder.decode(a_genre, "UTF-8");
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa",a_genre);
		    	String a_year=myJsonObject.getString("year");
		    	a_year=URLDecoder.decode(a_year, "UTF-8");
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa",a_year);
		    	String a_details=myJsonObject.getString("details");
		    	a_details=URLDecoder.decode(a_details, "UTF-8");
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa",a_details);
		    		
		    	//setting variables
		    		
		    	cover.setImageBitmap(myBitMap[position]);
		    	Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaa","set cover bitmap image");
				genre.setText(a_genre);
				Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaa","set genre");
				artist.setText(a_artist);
				Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaa","set artist");
				year.setText(a_year);
				Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaa","set year");
				name.setText(a_name);	
				Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaa","set name");
    		
			}
			catch(JSONException e1)
			{
				e1.printStackTrace();
			}
			catch(UnsupportedEncodingException e2)
			{
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}
		
	}

	private void createView(TableRow tr, TextView t, String viewdata) 
	{
		// TODO Auto-generated method stub
		t.setText(viewdata);
		//adjust the properties of the textView
        t.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        t.setTextColor(Color.DKGRAY);
        t.setBackgroundColor(Color.LTGRAY);
        t.setPadding(20, 0, 0, 0); 
        tr.setPadding(0, 1, 0, 1);
        tr.setBackgroundColor(Color.BLACK);
        tr.addView(t); // add TextView to row.
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	
	
	
}
