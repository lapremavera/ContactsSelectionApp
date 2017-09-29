package com.lapremavera.contactsselectionapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactIntentActivity extends AppCompatActivity{

    private List<ContactObject> contactsList;
    private int RUNTIME_PERMISSION_CODE = 2;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_intent);
        ListView intentListView = (ListView) findViewById(R.id.listView1);
        contactsList = new ArrayList<>();
        contactsList.add(new ContactObject("Android One", "111-1111-1111", "www.androidATC.com"));
        contactsList.add(new ContactObject("Android Two", "122-1111-1111", "www.androidATC.com"));
        contactsList.add(new ContactObject("Android Three", "133-1111-1111", "www.androidATC.com"));
        contactsList.add(new ContactObject("Android Four", "144-1111-1111", "www.androidATC.com"));

        List<String> listName = new ArrayList<>();
        for (int i = 0; i < contactsList.size(); i++) {
            listName.add(contactsList.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ContactIntentActivity.this, android.R.layout.simple_list_item_1, listName);
        intentListView.setAdapter(adapter);

        Log.d("Contacts", listName.toString());

        intentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactIntentActivity.this, ContactPageActivity.class);
                intent.putExtra("Object", contactsList.get(position));
                startActivityForResult(intent,0);
                
            }
        });
        
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultData = data.getExtras();
        String value = resultData.getString("value");
        switch (resultCode)
        {
            case Contstants.PHONE: makeCall(value);
                break;
            case Contstants.WEBSITE:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + value)));
                break;
        }
    }

    private void makeCall(String value) {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        
        if (result == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +value)));
        } 
        else {
            requestCallPermission();
        }
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            explainPermissionDialog();
        } ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, RUNTIME_PERMISSION_CODE);
    }
    public void onRequestPermissionResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RUNTIME_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can make the phone call", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops, you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void explainPermissionDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Call permission Required");
        alertDialogBuilder.setMessage("This app requires Call permission to make phone calls. Please grant the permission").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
