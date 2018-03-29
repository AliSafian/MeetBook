package com.facefive.meetbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facefive.meetbook.TimetableSession.SlotSingleRow;
import com.facefive.meetbook.TimetableSession.TimetableDay;
import com.facefive.meetbook.TimetableSession.TimetableSession;
import com.facefive.meetbook.UserHandling.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchedualPlanSlotSetting extends AppCompatActivity {

    private ListView list;
    private Button btn;
    private Button btn_pre;
    private TextView tv_day;



    private int tempDayCout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedual_plan_slot_setting);




//        Toast.makeText(getApplicationContext(), TimetableSession.noOfSlots+"",Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), TimetableSession.Days.size()+"",Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(getApplicationContext(), TimetableSession.Days.get(1).getSlotList().get(0).getEndTime().toString()+"",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), TimetableSession.Days.get(1).getSlotList().size()+"",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), TimetableSession.startTime.toString(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), TimetableSession.endTime.toString(),Toast.LENGTH_SHORT).show();



        //..............................start working



        list = (ListView) findViewById(R.id.slot_lv_scheduleplanslotsetting_xml);
        btn = (Button) findViewById(R.id.next_btn_scheduleplanslotsetting_xml);
        btn_pre = (Button) findViewById(R.id.pre_btn_scheduleplanslotsetting_xml);

        btn_pre.setVisibility(View.INVISIBLE);

        tv_day = (TextView) findViewById(R.id.day_tv_scheduleplanslotsetting_xml);


        tempDayCout = 0;

        tv_day.setText(TimetableSession.Days.get(tempDayCout).getDay());
        SlotListAdapter customAdapter = new SlotListAdapter(getApplicationContext(), TimetableSession.Days.get(tempDayCout).getSlotList());
        list.setAdapter(customAdapter);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempDayCout <TimetableSession.Days.size()-1) {



                    tempDayCout++;
                    tv_day.setText(TimetableSession.Days.get(tempDayCout).getDay());
                    SlotListAdapter customAdapter = new SlotListAdapter(getApplicationContext(), TimetableSession.Days.get(tempDayCout).getSlotList());
                    list.setAdapter(customAdapter);
                    if(tempDayCout==1)
                    {
                        btn_pre.setVisibility(View.VISIBLE);
                    }
                    if(tempDayCout == TimetableSession.Days.size()-1)
                    {
                        btn.setText("Save");

                    }


                }
                else
                {
                    UserSessionManager userSessionManager=new UserSessionManager(getApplicationContext());

                    StoreTimeTable(TimetableSession.Days.size(),TimetableSession.startTime,TimetableSession.endTime,TimetableSession.noOfSlots,userSessionManager.getUserID());



                }

            }
        });










        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempDayCout >0) {
                    tempDayCout--;
                    tv_day.setText(TimetableSession.Days.get(tempDayCout).getDay());
                    SlotListAdapter customAdapter = new SlotListAdapter(getApplicationContext(), TimetableSession.Days.get(tempDayCout).getSlotList());
                    list.setAdapter(customAdapter);
                    btn.setText("Next");

                    if(tempDayCout==0)
                        btn_pre.setVisibility(View.INVISIBLE);
                }




            }
        });

    }

    public  void SendData(final TimetableDay timetableDay){

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.URL_TIMATABLESLOTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SchedualPlanSlotSetting.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> params = new HashMap<String, String>();
                JSONArray object=new JSONArray();
                try {
                    object.put(timetableDay.getDay());
                    for (SlotSingleRow singleRow:timetableDay.getSlotList())
                    {
                        JSONObject srow=new JSONObject();
                        srow.put("starttime",singleRow.getStartTime());
                        srow.put("endtime",singleRow.getEndTime());
                        srow.put("header",singleRow.getHeader());
                        srow.put("slottype",singleRow.getSlotType());
                        srow.put("tid",TimetableSession.timetableID);
                        object.put(srow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    params.put("timetableday",object.toString());
                return  params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public  void StoreTimeTable(final int Days, final Time startTime, final Time endTime, final int noOfSlots, final int UserID){

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.URL_TIMATABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                   if(! jsonObject.getBoolean("error"))
                   {
                     TimetableSession.timetableID= jsonObject.getInt("ttid");
                       for(TimetableDay object: TimetableSession.Days){
                           SendData(object);
                       }
                   }
                   else
                   {

                       Toast.makeText(SchedualPlanSlotSetting.this,jsonObject.getString("error_msg"),Toast.LENGTH_SHORT).show();
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SchedualPlanSlotSetting.this,error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> params = new HashMap<String, String>();
                JSONArray object=new JSONArray();
                try {
                        JSONObject srow=new JSONObject();
                        srow.put("noofdays",Days);
                        srow.put("starttime",startTime);
                        srow.put("endtime",endTime);
                        srow.put("userid",UserID);
                        srow.put("nooofslots",noOfSlots);
                        object.put(srow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.put("timetable",object.toString());
                return  params;
            }
        };
        requestQueue.add(stringRequest);

    }

}