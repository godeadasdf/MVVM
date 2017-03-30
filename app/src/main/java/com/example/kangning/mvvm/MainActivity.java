package com.example.kangning.mvvm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mydi.DIUtil;
import com.example.mydi.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.mid)
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollLinear scrollLinear = new ScrollLinear(this);
        scrollLinear.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.mid, null, false);
        scrollLinear.addViewToMiddle(view);
        DIUtil.bind(this,view);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DIUtil","OOOOOOOOOOOOOOOOOOO!");
            }
        });
        setContentView(scrollLinear);
    }
}
