package com.example.android.app.portfolio.myappportfolio;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    final String prefix = "This button will launch my ";
    final String postfix = " app!";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_main, container, false);

        final Button button1 = (Button) view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextNow(button1.getText().toString());
            }
        });

        final Button button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextNow(button2.getText().toString());
            }
        });

        final Button button3 = (Button) view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextNow(button3.getText().toString());
            }
        });

        final Button button4 = (Button) view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextNow(button4.getText().toString());
            }
        });

        final Button button5 = (Button) view.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextNow(button5.getText().toString());
            }
        });

        final Button button6 = (Button) view.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextNow(button6.getText().toString());
            }
        });

        return view;
    }

    public void showTextNow(String caption) {
        Toast.makeText(getActivity(),prefix + caption + postfix, Toast.LENGTH_SHORT).show();
    }
}
