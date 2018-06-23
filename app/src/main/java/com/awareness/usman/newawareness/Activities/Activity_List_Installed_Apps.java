package com.awareness.usman.newawareness.Activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.awareness.usman.newawareness.Adapters.AppAdapter;
import com.awareness.usman.newawareness.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sada on 8/27/2016.
 */
public class Activity_List_Installed_Apps extends ListActivity {

    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist=null;
    private AppAdapter listAdapter=null;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=(ListView) findViewById(R.id.listview);
        packageManager=getPackageManager();

        new LoadApplications().execute();
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app= applist.get(position);

        try{
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Activity_List_Installed_Apps.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("action",app.loadLabel(packageManager).toString());
            editor.putString("appname",app.packageName);

            editor.commit();
            Intent intent=new Intent(Activity_List_Installed_Apps.this,Activity_Fence_Selection.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );

            // Add new Flag to start new Activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //this.finish();
        }catch (NoSuchMethodError e){
            Toast.makeText(this, "Launching "+app.loadLabel(packageManager)+ e+"...", Toast.LENGTH_LONG).show();

        }

        catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list){
        ArrayList<ApplicationInfo> applist=new ArrayList<>();

        for(ApplicationInfo info : list){
            try{
                if(packageManager.getLaunchIntentForPackage(info.packageName) != null){
                    applist.add(info);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  applist;
    }
    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress=null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(packageManager.GET_META_DATA));
            listAdapter=new AppAdapter(Activity_List_Installed_Apps.this,R.layout.listitem, applist);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listAdapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(Activity_List_Installed_Apps.this, null, "Loading app info ...");
            super.onPreExecute();
        }
    }


}
