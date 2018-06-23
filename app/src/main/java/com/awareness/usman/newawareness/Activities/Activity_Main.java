package com.awareness.usman.newawareness.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.awareness.usman.newawareness.Adapters.ItemOffsetDecoration;
import com.awareness.usman.newawareness.Adapters.Page_Situation_Adapter;
import com.awareness.usman.newawareness.Adapters.Page_Situation_CustomItemClickListener;
import com.awareness.usman.newawareness.R;
import com.awareness.usman.newawareness.Objects.Realm_Awareness_Fence;
import com.awareness.usman.newawareness.Objects.Realm_Intro;
import com.awareness.usman.newawareness.Utilities.Utils;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class Activity_Main extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Realm realm;
    Page_Situation_Adapter page_situation_adapter;
    MaterialTapTargetPrompt mFabPrompt;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm.init(this);
        try{
            realm = Realm.getDefaultInstance();

        }catch (Exception e){

            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);

        }
        if (Utils.isGooglePlayServicesAvailable(Activity_Main.this)) {
            mGoogleApiClient = new GoogleApiClient.Builder(Activity_Main.this)
                    .addApi(Awareness.API)
                    .build();
            mGoogleApiClient.connect();
        } else {
            Toast.makeText(Activity_Main.this,"No services",Toast.LENGTH_LONG).show();

        }
        long countintro=realm.where(Realm_Intro.class).count();
        if(countintro==0)
        {
            Intent intent=new Intent(Activity_Main.this,Activity_Intro.class);
            startActivity(intent);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                Realm_Intro realm_intro=realm.createObject(Realm_Intro.class);
                    realm_intro.setId(1);
                }
            });
        }

        Long count=realm.where(Realm_Awareness_Fence.class).count();
        if(count==0)
        {
            showFabPrompt();
        }

        recyclerView=(RecyclerView)findViewById(R.id.activity_main_rv);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        Adapter_Setter();
        recyclerView.setAdapter(page_situation_adapter);
        GridLayoutManager llm = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(llm);
        page_situation_adapter.notifyDataSetChanged();

        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = true;
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(2);
        recyclerView.addItemDecoration(itemDecoration);
        Click_Listeners();
    }

    @Override
    protected void onStop() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Activity_Main.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onResume() {
        page_situation_adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        page_situation_adapter.notifyDataSetChanged();

        super.onRestart();
    }

    @Override
    protected void onPause() {
        page_situation_adapter.notifyDataSetChanged();

        super.onPause();
    }

    private void Click_Listeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_Main.this,Activity_Fence_Selection.class);
                startActivity(intent);
            }
        });
    }

    public void Adapter_Setter(){
        /**
realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        Realm_Situation realm_situation=realm.createObject(Realm_Situation.class);
        realm_situation.setName("HeadPhones");

    }
});*/
        final RealmResults<Realm_Awareness_Fence> result=realm.where(Realm_Awareness_Fence.class).findAll();
        page_situation_adapter=new Page_Situation_Adapter(mGoogleApiClient,Activity_Main.this,result,new Page_Situation_CustomItemClickListener(){
            @Override
            public void onItemClick(View v, int position) {
                //Toast.makeText(getApplicationContext(),"You Clicked :  "+result.get(position).getSituationname(),Toast.LENGTH_SHORT).show();
                super.onItemClick(v, position);
            }
        }

        );


    }
    public void showFabPrompt()
    {
        if (mFabPrompt != null)
        {
            return;
        }
        mFabPrompt = new MaterialTapTargetPrompt.Builder(Activity_Main.this)
                .setTarget(findViewById(R.id.fab))
                .setFocalToTextPadding(R.dimen.dp40)
                .setPrimaryText("Welcome to Awareness API ")
                .setSecondaryText("Tap the + icon to register your first Situation-action pair")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget)
                    {
                        mFabPrompt = null;
                        //Do something such as storing a value so that this prompt is never shown again
                    }

                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .create();
        mFabPrompt.show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
