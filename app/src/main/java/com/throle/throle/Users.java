package com.throle.throle;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by TEST on 11/9/2017.
 */

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> counsellor = new ArrayList<>();
    PrefManager prefManager;
    PrefMan prefMan;
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        prefManager = new PrefManager(Users.this);
        prefMan = new PrefMan(Users.this);
        setTitle(prefMan.getUserName());
        prefManager.setFirstTimeLaunch(false);
        Log.v("Users", "This is chat with: " + prefManager.getSessionStart());
        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);


       fetchCouncellors();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Users.this);
                alertDialog.setTitle("New Session");
                alertDialog.setMessage("You are about to start a session with a counsellor");
                alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserDetails.chatWith = al.get(position);
                        prefManager.setSessionStart(UserDetails.chatWith);
                        Log.v("Users", "This is prefManager.getSessionStart: " + prefManager.getSessionStart());
                        Log.v("Users", "This is chat with: " + UserDetails.chatWith);
                        startActivity(new Intent(Users.this, Chat.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                });// Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();


            }
        });
    }



    public void fetchCouncellors(){
        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String url = "https://throle-81230.firebaseio.com/counsellorsonline.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(prefMan.getUserName())) {
                    al.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("Users", "This is Userdetails.chatwith: "+ UserDetails.chatWith);
        Log.v("Users", "This is prefManager.getSessionStart: "+ prefManager.getSessionStart());




        if(totalUsers < 1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else if (!prefManager.getSessionStart().equals("")
                && al.contains(prefManager.getSessionStart())){
           counsellor.add(al.get(al.indexOf(prefManager.getSessionStart())));
           al = counsellor;
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);

        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
        }

        usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));

        pd.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                al.clear();
                counsellor.clear();
                fetchCouncellors();
                return true;


            case R.id.log_out:
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Users.this);
                alertDialog2.setTitle("Log Out");
                alertDialog2.setMessage("Are you sure you want to Log Out? Messages may not be retained");
                alertDialog2.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prefManager.setFirstTimeLaunch(true);
                        prefManager.setSessionStart("");
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Toast.makeText(Users.this, "Logged Out Sussesfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });// Setting Negative "NO" Button
                alertDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog2.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
