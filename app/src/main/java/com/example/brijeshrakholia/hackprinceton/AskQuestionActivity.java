package com.example.brijeshrakholia.hackprinceton;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class AskQuestionActivity extends ActionBarActivity {
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
    }
    public void sendQToServer(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ask_question, menu);
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
    public void sendQuestion(View view) throws IOException, JSONException {
        // Retrieve latitude and longitude

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
        EditText mEdit = (EditText) findViewById(R.id.question);
        Log.v("Longitude", Double.toString(longitude));


        JSONObject j = new JSONObject();
        j.put("longitude", longitude);
        j.put("latitude", latitude);
        j.put("android_id", imeiNo );
        j.put("question", mEdit.getText());
        Log.v("JSON", j.toString());
        if (longitude != 0 && latitude != 0){
            AsyncTest backendThread = new AsyncTest(j);
            backendThread.execute();
        }

    }

    private  class TestMain {
        OkHttpClient client = new OkHttpClient();

        // post request code here

        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");


        String doPostRequest(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
    private class AsyncTest extends AsyncTask<Void, Integer, String>{
        private JSONObject j;
        public AsyncTest(JSONObject json){
            j = json;

        }
        @Override
        protected String doInBackground(Void... params) {
            TestMain post = new TestMain();
            String postrequest = null;
            try {
                postrequest = post.doPostRequest("http://hispy.herokuapp.com/sendprompt", j.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("PostRequest: ", postrequest);
            return postrequest;
        }
    }

}
