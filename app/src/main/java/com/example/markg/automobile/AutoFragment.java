package com.example.markg.automobile;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by markg on 2017-12-10.
 */

public class AutoFragment extends Fragment {
    protected TextView viewDetails;
    protected TextView viewID;
    protected Button deleteButton;
   // protected Button editButton;

    protected AutoActivity autoActivity;

    public AutoFragment()
    {
        super();
    }

    @SuppressLint("ValidFragment")
    public AutoFragment(AutoActivity autoActivity)
    {
        super();
        this.autoActivity = autoActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.activity_auto_fragment, container, false);
        final Bundle args = getArguments();

        viewDetails = (TextView) view.findViewById(R.id.details);

        deleteButton = (Button) view.findViewById(R.id.delete);
        //editButton = (Button) view.findViewById(R.id.edit);

        String messageText = "Info:\n" + " " + args.getString("message");
        //String idText = "ID: " + " " + Long.toString(args.getLong("id"));

        viewDetails.setText(messageText);
        //viewID.setText(idText);

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (autoActivity == null)
                {
                    Intent msgDetailsIntent = new Intent(getActivity(), AutoFragment.class);
                    msgDetailsIntent.putExtra("msgID", args.getLong("id"));

                    getActivity().setResult(autoActivity.REQUEST_MSG_DELETE, msgDetailsIntent);
                    getActivity().finish();
                }
                else
                {
                    autoActivity.deleteItem(args.getLong("id"));

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(AutoFragment.this);
                    ft.commit();
                }
            }
        });



        return view;
    }
}