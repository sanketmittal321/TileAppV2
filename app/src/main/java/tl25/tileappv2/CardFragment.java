package tl25.tileappv2;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.warkiz.widget.ColorCollector;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CardFragment extends Fragment {

    ArrayList<MainNavigation> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Options[] = {"Enter Text", "Pick Colour", "Draw"};
    int Images[] = {R.drawable.text, R.drawable.color, R.drawable.draw};
    int Imagesleft[] = {R.drawable.snail, R.drawable.low_brightness,R.drawable.low_brightness};
    int ImagesRight[] = {R.drawable.leopard,R.drawable.full_brightness,R.drawable.full_brightness};
    int Seekbarid[]= {1,2,3};
    Class myClass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    final DatabaseReference mbrightness = myRef.child("Brightness");
    final DatabaseReference mspeed = myRef.child("Speedoftext");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeList();
        getActivity().setTitle("Welcome to the Tile App");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);


        return view;
    }

    public void initializeList() {
        listitems.clear();

        for (int i = 0; i < 3; i++) {

            MainNavigation item = new MainNavigation();
            item.setCardName(Options[i]);
            item.setImageResourceId(Images[i]);
            item.setImageLeftID(Imagesleft[i]);
            item.setImageRightID(ImagesRight[i]);
            item.setSeekbarID(Seekbarid[i]);
            listitems.add(item);

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView imageleft;
        public ImageView imageright;
        public IndicatorSeekBar mSeekbar;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            imageleft = v.findViewById(R.id.imageleft);
            imageright = v.findViewById(R.id.imageright);
            mSeekbar = v.findViewById(R.id.seekbar);

      }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<MainNavigation> list;

        public MyAdapter(ArrayList<MainNavigation> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.option_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.titleTextView.setText(list.get(position).getCardName());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.imageleft.setImageResource(list.get(position).getImageLeftID());
            holder.imageright.setImageResource(list.get(position).getImageRightID());
            holder.mSeekbar.setTag(list.get(position).getSeekbarID());

            int id = list.get(position).getSeekbarID();
            if (id == 1)holder.mSeekbar.setMax(25);
            else if (id == 2) holder.mSeekbar.setMax(255);
            else if (id == 3){}

            holder.mSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {

                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                    int id = list.get(position).getSeekbarID();
                    if(id == 1) mspeed.setValue(seekBar.getProgress());
                    if (id == 2) mbrightness.setValue(seekBar.getProgress());
                }
            });

                holder.coverImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String optionitems = String.valueOf(list.get(position).getCardName());
                        if (optionitems.equals("Enter Text")) myClass = Textpage.class;
                        else if (optionitems.equals("Pick Colour")) myClass = ColourPicker.class ;
                        else if (optionitems.equals("Draw")) myClass = PaintMainpage.class;

                        Intent intent = new Intent(view.getContext(), myClass);
                        intent.putExtra("image_id", String.valueOf(list.get(position).getCardName()));
                        view.getContext().startActivity(intent);
                    }
                });
            }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }
}


