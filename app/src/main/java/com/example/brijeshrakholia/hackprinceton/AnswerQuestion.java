package com.example.brijeshrakholia.hackprinceton;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.provider.Settings.Secure;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

public class AnswerQuestion extends ActionBarActivity {

    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateQuestions(View view) throws JSONException {
        TelephonyManager TM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // IMEI No.
        String imeiNo = TM.getDeviceId();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        Log.v("Longitude", Double.toString(longitude));
        JSONObject j = new JSONObject();
        j.put("longitude", longitude);
        j.put("latitude", latitude);

        Log.v("JSON:", j.toString());
        if (latitude != 0 && longitude != 0) {
            AsyncTest get = new AsyncTest(j);
            get.execute();


        }


    }

    private  class TestMain {
        OkHttpClient client = new OkHttpClient();

        // post request code here

        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");


        String doPostRequest(String url) throws IOException {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();
        }
    }
    private class AsyncTest extends AsyncTask<Void, Integer, String> {
        private JSONObject j;
        String result;
        public AsyncTest(JSONObject json){
            j = json;

        }
        @Override
        protected String doInBackground(Void... params) {
            TestMain post = new TestMain();
            String postrequest = null;
            try {
                postrequest = post.doPostRequest("http://hispy.herokuapp.com/getprompts");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.v("PostRequest: ", postrequest);
            result = postrequest;
            return postrequest;
        }
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

             try {
                 JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
                 JSONArray data = object.getJSONArray("data");

                String question = data.getJSONObject(0).getString("question");
                Button btn = (Button) findViewById(R.id.ques);
                btn.setText(question);
                btn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photo));
                        Uri imageUri = Uri.fromFile(photo);
                        startActivityForResult(intent, 1);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.v("Post: ", result);
        }


    }





}


