package com.example.android.app.portfolio.myappportfolio;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    public MainActivityFragment() {
    }

    private final FragmentActivity mContext = getActivity();

    private final String space = " ";
    private String prefix = null;
    private String postfix = null;

    private Button mPopularMoviesButton, mStockHawkButton, mBuildItBiggerButton, mMakeYourAppMaterialButtonn, mGoUbiquitousButton, mCapstoneButton;

    private static Toast toast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (View) inflater.inflate(R.layout.fragment_main, container, false);

        prefix = getString(R.string.toast_prefix) + space;
        postfix =  space + getString(R.string.toast_postfix);


        mPopularMoviesButton = (Button) view.findViewById(R.id.button1);
        mStockHawkButton = (Button) view.findViewById(R.id.button2);
        mBuildItBiggerButton = (Button) view.findViewById(R.id.button3);
        mMakeYourAppMaterialButtonn = (Button) view.findViewById(R.id.button4);
        mGoUbiquitousButton = (Button) view.findViewById(R.id.button5);
        mCapstoneButton = (Button) view.findViewById(R.id.button6);

        mPopularMoviesButton.setOnClickListener(this);
        mStockHawkButton.setOnClickListener(this);
        mBuildItBiggerButton.setOnClickListener(this);
        mMakeYourAppMaterialButtonn.setOnClickListener(this);
        mGoUbiquitousButton.setOnClickListener(this);
        mCapstoneButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        switch(button.getId()) {
            case R.id.button1:
                showToast(getActivity(),button.getText().toString());
                break;
            case R.id.button2:
                showToast(getActivity(),button.getText().toString());
                break;
            case R.id.button3:
                showToast(getActivity(),button.getText().toString());
                break;
            case R.id.button4:
                showToast(getActivity(),button.getText().toString());
                break;
            case R.id.button5:
                showToast(getActivity(),button.getText().toString());
                break;
            case R.id.button6:
                showToast(getActivity(),button.getText().toString());
                break;
        }
    }


    /* Taken from  https://gist.github.com/raafaelima/338be1b4b6a21cba1ea2c2a7d7bd4051 */

    private void showToast(Context ctx, String caption) {
        //if the toast is showing, we cancel its exibition.
        if (toast != null) {
            toast.cancel();
        }
        //Then we create another toast and show tit to the user
        toast = Toast.makeText(ctx, prefix + caption + postfix, Toast.LENGTH_SHORT);
        toast.show();
    }
}
