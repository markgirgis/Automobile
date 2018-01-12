package com.example.markg.automobile;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


public class AutoDetails extends AppCompatActivity
{
    protected FrameLayout fl;
    protected AutoFragment autoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fl = new FrameLayout(this);
        fl.setId(R.id.messageDetailFrame);
        setContentView(fl);

        autoFragment= new AutoFragment();
        autoFragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.messageDetailFrame, autoFragment);
        ft.commit();
    }
}
