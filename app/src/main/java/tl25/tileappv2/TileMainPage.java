package tl25.tileappv2;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.github.zagum.switchicon.SwitchIconView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorType;
import com.warkiz.widget.TickMarkType;


public class TileMainPage extends AppCompatActivity {
    private SwitchIconView switchIcon1;
    private SwitchIconView switchIcon2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    final DatabaseReference text = myRef.child("Text");
    final DatabaseReference mode= myRef.child("Mode");
    final DatabaseReference brightness = myRef.child("Brightness");
    final DatabaseReference color= myRef.child("Colour");
    final DatabaseReference speed = myRef.child("Speedoftext");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tile_main_page);

        Intent intent = new Intent(this, LoadingPage.class);
        startActivity(intent);

        switchIcon2 = (SwitchIconView) findViewById(R.id.switchIconView2);
        switchIcon1 = (SwitchIconView) findViewById(R.id.switchIconView1);
        final SwipeButton MirrorMode = findViewById(R.id.swipe_btn);




            MirrorMode.setOnStateChangeListener(new OnStateChangeListener() {
                @Override
                public void onStateChange(boolean active) {
                    if (active) {
                        mode.setValue(3);
                        switchIcon1.setIconEnabled(false);
                    } else mode.setValue(5);
                }
            });


        switchIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchIcon2.switchState();
                if (switchIcon2.isIconEnabled()){
                    MirrorMode.setHasActivationState(false);
                    mode.setValue(0);
                    brightness.setValue(0);
                    if(MirrorMode.isActive()) MirrorMode.toggleState();
                    if(switchIcon1.isIconEnabled()) switchIcon1.setIconEnabled(false);
                }
                else{
                    MirrorMode.setHasActivationState(true);
                    text.setValue("TL25");
                    mode.setValue(5);
                    brightness.setValue(200);
                    color.setValue("7E17F5");
                    speed.setValue(6);
                }
            }
        });

        switchIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchIcon2.isIconEnabled()) {
                      switchIcon1.switchState();
                    if (switchIcon1.isIconEnabled()) {
                        mode.setValue(4);
                        if (MirrorMode.isActive()) MirrorMode.toggleState();
                    } else {
                        mode.setValue(5);
                    }
                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("messenger"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver2, new IntentFilter("messenger2"));

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new CardFragment();

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();

        }


    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String newText = intent.getStringExtra("StatusText");
            Log.d("tag", newText);
            TextView textView = findViewById(R.id.textView);
            textView.setText(newText);

        }
    };

    private BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newColour = intent.getStringExtra("StatusColor");

            int IColor = Integer.parseInt(newColour);
            Log.d("tag","J" + IColor);

            Toolbar bar = findViewById(R.id.toolbar);
            bar.setBackgroundColor(IColor);
            bar.getBackground().setAlpha(128);

//            SwipeButton button = findViewById(R.id.swipe_btn);
//            button.setBackgroundColor(0xFFFF0000);
        }
    };
  }

