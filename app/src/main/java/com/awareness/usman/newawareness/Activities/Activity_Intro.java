package com.awareness.usman.newawareness.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.awareness.usman.newawareness.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by usman on 4/12/2017.
 */
public class Activity_Intro extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("New Awarenss", "New Awarenss is an intelligent tool based on Awareness API.It performs a certain function when the assigned conditions are fulfilled.", R.drawable.intro_1,getResources().getColor(R.color.blue)));
        addSlide(AppIntroFragment.newInstance("Condition-Responce Pair","The given pair will open Gallery when your heaphones are plugged in,weather is clear and the user is running.", R.drawable.intro_2,getResources().getColor(R.color.blue)));
        addSlide(AppIntroFragment.newInstance("Custom Pairing", "Create your own Pairs by making any combination of the given situations.The Supported fences are:Heaphone,Weather,Location,Activity and Time.It's cool,isn't it?", R.drawable.intro_3,getResources().getColor(R.color.blue)));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent=new Intent(Activity_Intro.this,Activity_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        // Add new Flag to start new Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent=new Intent(Activity_Intro.this,Activity_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        // Add new Flag to start new Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
