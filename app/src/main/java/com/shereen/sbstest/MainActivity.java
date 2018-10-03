package com.shereen.sbstest;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.shereen.sbstest.gson.TopLevel;

//View Controller class
public class MainActivity extends AppCompatActivity implements HelperFragment.OnFragmentInteractionListener{

    Button requestButton;
    HelperFragment helperFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start the fragment
        helperFragment = new HelperFragment();
        getSupportFragmentManager().beginTransaction()
                .add(helperFragment, "helperFragment")
                .commit();

        requestButton = findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                //search for given input

                //call rest api
                //change button with result
                requestButton.setText("Clicked");
                if(helperFragment != null){
                    helperFragment.onRequestButtonClicked();
                }

            }
        });
    }

    @Override
    public void onResultReceived(TopLevel topLevel){
        System.out.println("Received!");
        System.out.println("This is from Main: "+topLevel.getStatus());

        //get the 6th element (5th index)
        String color = topLevel.getData()[5].getColor();
        String title = topLevel.getData()[5].getTitle();

        System.out.println("Title: "+title+" color: "+color);
        setParams(title,color);

    }

    void setParams(String title, String color){
        requestButton.setText(title);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(Color.parseColor(color));
    }
}
