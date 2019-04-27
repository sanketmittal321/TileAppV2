package tl25.tileappv2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoadingPage extends AppCompatActivity {


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_loading);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        final DatabaseReference text = myRef.child("Text");
        final DatabaseReference mode= myRef.child("Mode");
        final DatabaseReference brightness = myRef.child("Brightness");
        final DatabaseReference color= myRef.child("Colour");
        final DatabaseReference speed = myRef.child("Speedoftext");

        text.setValue("TL25");
        mode.setValue(5);
        brightness.setValue(200);
        color.setValue("7E17F5");
        speed.setValue(6);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run(){
                finish();
            }
        }, 500);

    }

}
