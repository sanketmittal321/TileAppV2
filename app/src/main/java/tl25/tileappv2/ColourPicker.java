package tl25.tileappv2;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColourPicker extends AppCompatActivity {
    CoordinatorLayout mLayout;
    int mDefaultColor;
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("Colour");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpicker);

        mLayout = (CoordinatorLayout) findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(ColourPicker.this, R.color.colorPrimary);

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                mLayout.setBackgroundColor(mDefaultColor);

                String colortoesp = Integer.toHexString(color);
                myRef.setValue(colortoesp.substring(2));

                getColour();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run(){
                        finish();
                    }
                }, 500);
            }
        });

        colorPicker.show();
    }

    public void getColour() {
        Log.d("tag","SSS" + mDefaultColor);
        String SColour = Integer.toString(mDefaultColor);

        Intent intent2 = new Intent ("messenger2").putExtra("StatusColor",SColour);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
    }
}

