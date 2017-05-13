package com.example.usman.newawareness.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.usman.newawareness.Activities.Activity_Fence_Selection;
import com.example.usman.newawareness.Activities.Activity_Fence_Create;
import com.example.usman.newawareness.R;
import com.example.usman.newawareness.Objects.Realm_Awareness_Fence;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.util.List;

import io.realm.Realm;

/**
 * Created by usman on 3/28/2017.
 */

public class Page_Situation_Adapter extends RecyclerView.Adapter <Page_Situation_Adapter.Situation_Holder>{
    private List<Realm_Awareness_Fence> list;
    Realm realm;
    private Context context;
    GoogleApiClient mgoogleApiClient;

    private Page_Situation_CustomItemClickListener listener;
    public Page_Situation_Adapter(GoogleApiClient googleApiClient,Context context, List<Realm_Awareness_Fence> list, Page_Situation_CustomItemClickListener listener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
        this.mgoogleApiClient=googleApiClient;
    }
    @Override
    public Situation_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.page_situation_inflater,parent,false);
        final Situation_Holder situation_holder=new Situation_Holder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, situation_holder.getAdapterPosition());
            }
        });
        return situation_holder;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(Situation_Holder holder, final int position) {
        realm=Realm.getDefaultInstance();
        final Realm_Awareness_Fence fence=realm.where(Realm_Awareness_Fence.class).equalTo("situationname",list.get(position).getSituationname()).findFirst();
        if(fence.getActive())
        {
            holder.aSwitch.setChecked(true);
        }
        else if (fence.getActive())
        {
            holder.aSwitch.setChecked(false);
        }

        if(list.get(position).getNotification()!=null)
        {
            holder.tv_top.setText(" Send Notification");

        }
        else
        {
            holder.tv_top.setText("Open : "+list.get(position).getAppopen());

        }
        holder.tv_time.setText(list.get(position).getTime());
        if("Choose State".equalsIgnoreCase(list.get(position).getHeadphone()))
        {
            holder.rl_headphone.getLayoutParams().height=0;
        }
        else
        {
            holder.tv_headphone.setText(list.get(position).getHeadphone());

        }
        if("Choose State".equalsIgnoreCase(list.get(position).getWeather()))        {
            holder.rl_weather.getLayoutParams().height=0;


        }
        else
        {
            holder.tv_weather.setText(list.get(position).getWeather());

        }
        if(list.get(position).getLocation()==null)        {
            holder.rl_location.getLayoutParams().height=0;

        }
        else
        {
            holder.tv_location.setText(list.get(position).getLocation());

        }
        if("Choose State".equalsIgnoreCase(list.get(position).getActivity()))        {
            holder.rl_activity.getLayoutParams().height=0;

        }
        else
        {
            holder.tv_activity.setText(list.get(position).getActivity());

        }
        if(list.get(position).getDate()==null)

        {
            holder.rl_time.getLayoutParams().height=0;

        }
        else
        {

            holder.tv_time.setText(list.get(position).getDate()+"  "+list.get(position).getTime());

        }
    holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked)
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    fence.setActive(false);
                    Activity_Fence_Selection page_fence_selection=new Activity_Fence_Selection();
                    page_fence_selection.unregisterFence(fence.getSituationname(),context);
                }
            });
        }
        else
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    fence.setActive(true);
                    Activity_Fence_Create fence_create=new Activity_Fence_Create();
                    AwarenessFence fenceupgrade = null;
                    try {
                        fenceupgrade=fence_create.Upgrade_Situation(fence);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Activity_Fence_Selection page_fence_selection=new Activity_Fence_Selection();
                    page_fence_selection.registerFence(fence.getSituationname(),fenceupgrade,context);

                }
            });
        }
        }
    });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Situation_Holder extends RecyclerView.ViewHolder {
        TextView tv_top,tv_headphone,tv_weather,tv_location,tv_activity,tv_time;
        RelativeLayout rl_headphone,rl_weather,rl_location,rl_activity,rl_time;
        SwitchCompat aSwitch;
        public Situation_Holder(View itemView) {
            super(itemView);
            tv_top=(TextView)itemView.findViewById(R.id.page_situation_inflater_tv) ;
            rl_headphone=(RelativeLayout) itemView.findViewById(R.id.rl_headphone) ;
            rl_weather=(RelativeLayout) itemView.findViewById(R.id.rl_weather) ;
            rl_location=(RelativeLayout) itemView.findViewById(R.id.rl_location) ;
            rl_activity=(RelativeLayout) itemView.findViewById(R.id.rl_activity) ;
            rl_time=(RelativeLayout) itemView.findViewById(R.id.rl_time) ;
            tv_headphone=(TextView)itemView.findViewById(R.id.tv_headphone) ;
            tv_weather=(TextView)itemView.findViewById(R.id.tv_weather) ;
            tv_location=(TextView)itemView.findViewById(R.id.tv_location) ;
            tv_activity=(TextView)itemView.findViewById(R.id.tv_activity) ;
            tv_time=(TextView)itemView.findViewById(R.id.tv_time) ;
            aSwitch=(SwitchCompat) itemView.findViewById(R.id.switch_1);
        }
    }
    }
