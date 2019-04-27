package tl25.tileappv2;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Textpage extends AppCompatActivity {
    String texttile;
    EditText Textinput;
    Button submitButton;
    static Textpage INSTANCE;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    final DatabaseReference text = myRef.child("Text");
    final DatabaseReference mode= myRef.child("Mode");

    TileMainPage main = new TileMainPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        INSTANCE = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textpage);

        Textinput = (EditText) findViewById(R.id.edt_comment);

        submitButton = (Button) findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                  texttile = Textinput.getText().toString();
                  text.setValue(texttile);
                  mode.setValue(0);
                  getText();
                  Textpage.super.onBackPressed();
            }
        });

            text.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });


    }

    public void getText(){
        Intent intent = new Intent ("messenger").putExtra("StatusText",this.texttile);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
