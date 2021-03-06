package com.example.root.kutt_app_i;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceJob extends JobIntentService {
    static final int JOB_ID = 1012;
    public final String CHANNEL_ID="my_notification_channel";
    public final String CHANNEL_ID_B="background_channel";
    private static final int idUnique=1234;
    TextView link;
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ServiceJob.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(Intent intent) {
        Notif(getApplicationContext());

    }
    final Handler mHandler = new Handler();

    // Helper for showing tests
    void Notif(final Context context) {
        mHandler.post(new Runnable() {
            @Override public void run() {
                final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
                    public void onPrimaryClipChanged() {
                        boolean fg = isAppIsInBackground(getApplicationContext());
                        String a = clipboard.getText().toString();
                        //myDb = new DatabaseHelper(TheService.this);
                        String[] text1 = a.split(":");
                        if (!a.equals("") && fg) {
                            if (text1[0].equals("http") || text1[0].equals("https")) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                    CharSequence name = "Personal notifications ";
                                    String descriptions = "Include Personal notifications";
                                    int importance = NotificationManager.IMPORTANCE_HIGH;


                                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                    notificationChannel.setDescription(descriptions);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.createNotificationChannel(notificationChannel);

                                }

                                //final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.coustomnotification);
                                // Set Notification Title
                   /* String strtitle = "This is title";
                    // Set Notification Text
                    String strtext = "This is text";*/

                                // Open NotificationView Class on Notification Click



                                // Send data to NotificationView Class
                                /*intent.putExtra("title", "1");*/
                                //startActivity(intent);
                                //intent.putExtra("action","action1");
                                // Open NotificationView.java Activity

                                Intent open = new Intent(getApplicationContext(), MainActivity.class);
                                PendingIntent opp = PendingIntent.getActivity(getApplicationContext(), 0, open,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                        // Set Icon
                                        .setSmallIcon(R.drawable.notify)
                                        // Set Ticker Message
                                        // Dismiss Notification
                                        // Set PendingIntent into Notification
                                        .setContentIntent(opp)
                                        // Set RemoteViews into Notification
                                        //.setContentTitle("New link in Clipboard !")
                                        // .setContentText("Copy??")
                                        //.addAction(R.drawable.ic_file_notification,"ADD",pIntent)
                                        //.setContentIntent(pIntent)
                                        .setAutoCancel(true)
                                        //.setContent(remoteViews)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                //remoteViews.setOnClickPendingIntent(R.id.not,opp);
                                if(a.length()>=27) {
                                    if (!a.substring(0, 26).equals("http://kutt.fossgect.club/")) {
                                        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        response = "http://kutt.fossgect.club/" + response;
                                                        String cc = clipboard.getText().toString();
                                                        Intent i = new Intent(Intent.ACTION_SEND);
                                                        i.setType("text/plain");
                                                        i.putExtra(Intent.EXTRA_TEXT, response);
                                                        Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
                                                        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                                        PendingIntent share = PendingIntent.getActivity(getApplicationContext(), 0, i,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                                        builder.setContentText(cc);
                                                        builder.setContentTitle(response);
                                                        builder.addAction(R.id.save,"SAVE",pIntent);
                                                        builder.addAction(R.drawable.ic_baseline_share_24px,"SHARE",share);
                                                        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                        // Build Notification with Notification Manager
                                                        notificationmanager.notify(0, builder.build());
                                                        requestQueue.stop();
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        String cc = clipboard.getText().toString();
                                                        Toast.makeText(getApplicationContext(), cc, Toast.LENGTH_LONG).show();
                                                        Intent i = new Intent();
                                                        i.setAction(Intent.ACTION_SEND);
                                                        i.setType("text/plain");
                                                        i.putExtra(Intent.EXTRA_TEXT, cc);
                                                        Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
                                                        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                                        PendingIntent share = PendingIntent.getActivity(getApplicationContext(), 0, i,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                                        builder.setContentText(cc);
                                                        //remoteViews.setOnClickPendingIntent(R.id.image2, pIntent);
                                                        //remoteViews.setOnClickPendingIntent(R.id.image1, share);
                                                        //  remoteViews.setTextViewText(R.id.title, "Something went wrong!!");
                                                        builder.setContentTitle("Save Link ?");
                                                        builder.addAction(R.id.save,"SAVE",pIntent);
                                                        builder.addAction(R.drawable.ic_baseline_share_24px,"SHARE",share);
                                                        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                        // Build Notification with Notification Manager
                                                        notificationmanager.notify(0, builder.build());
                                                        error.printStackTrace();
                                                        requestQueue.stop();
                                                    }

                                                }) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                String cc = clipboard.getText().toString();
                                                params.put("url", cc);
                                                return params;
                                            }
                                        };
                                        requestQueue.add(stringRequest);
                                    } else {
                                        Intent i = new Intent();
                                        i.setAction(Intent.ACTION_SEND);
                                        i.setType("text/plain");
                                        i.putExtra(Intent.EXTRA_TEXT, a);
                                        Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
                                        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                        PendingIntent share = PendingIntent.getActivity(getApplicationContext(), 0, i,
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentText(a);
                                        //remoteViews.setOnClickPendingIntent(R.id.image2, pIntent);
                                        //remoteViews.setOnClickPendingIntent(R.id.image1, share);
                                        //remoteViews.setTextViewText(R.id.title, "KUTT");
                                        builder.setContentTitle("KUTT");
                                        builder.addAction(R.id.save,"SAVE",pIntent);
                                        builder.addAction(R.drawable.ic_baseline_share_24px,"SHARE",share);
                                        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        // Build Notification with Notification Manager
                                        notificationmanager.notify(0, builder.build());
                                    }
                                }else {
                                    final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://kutt.fossgect.club/short/",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    response = "http://kutt.fossgect.club/" + response;
                                                    String cc = clipboard.getText().toString();
                                                    Intent i = new Intent(Intent.ACTION_SEND);
                                                    i.setType("text/plain");
                                                    i.putExtra(Intent.EXTRA_TEXT, response);
                                                    Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
                                                    PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                                    PendingIntent share = PendingIntent.getActivity(getApplicationContext(), 0, i,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentText(cc);
                                                    // remoteViews.setOnClickPendingIntent(R.id.image2, pIntent);
                                                    //remoteViews.setOnClickPendingIntent(R.id.image1, share);
                                                    //remoteViews.setTextViewText(R.id.title, response);
                                                    builder.setContentTitle(response);
                                                    builder.addAction(R.id.save,"SAVE",pIntent);
                                                    builder.addAction(R.drawable.ic_baseline_share_24px,"SHARE",share);
                                                    NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    // Build Notification with Notification Manager
                                                    notificationmanager.notify(0, builder.build());
                                                    requestQueue.stop();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    String cc = clipboard.getText().toString();
                                                    Toast.makeText(getApplicationContext(), cc, Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent();
                                                    i.setAction(Intent.ACTION_SEND);
                                                    i.setType("text/plain");
                                                    i.putExtra(Intent.EXTRA_TEXT, cc);
                                                    Intent intent = new Intent(getApplicationContext(), ActionReceiver.class);
                                                    PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                                    PendingIntent share = PendingIntent.getActivity(getApplicationContext(), 0, i,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentText(cc);
                                                    // remoteViews.setOnClickPendingIntent(R.id.image2, pIntent);
                                                    //remoteViews.setOnClickPendingIntent(R.id.image1, share);
                                                    //remoteViews.setTextViewText(R.id.title, "Something went wrong!!");
                                                    builder.setContentTitle("Save Link ?");
                                                    builder.addAction(R.id.save,"SAVE",pIntent);
                                                    builder.addAction(R.drawable.ic_baseline_share_24px,"SHARE",share);
                                                    NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    // Build Notification with Notification Manager
                                                    notificationmanager.notify(0, builder.build());
                                                    error.printStackTrace();
                                                    requestQueue.stop();
                                                }

                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            String cc = clipboard.getText().toString();
                                            params.put("url", cc);
                                            return params;
                                        }
                                    };
                                    requestQueue.add(stringRequest);
                                }

                                // remoteViews.setString(R.id.link,String ,a);




                                //Toast.makeText(getBaseContext(), "Copy:\n" + a, Toast.LENGTH_LONG).show();

                                //String text = Clipboard_Utils.getDataFromClipboard(TheService.this);
                            /*String[] text1 = a.split(":");
                            if (!a.equals("")) {
                                if (text1[0].equals("http") || text1[0].equals("https")) {
                                    boolean isInserted = myDb.insertData(a);
                                    if (isInserted == true) {
                                        Toast.makeText(TheService.this, "Data Inserted ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(TheService.this, "Not a legal link", Toast.LENGTH_SHORT).show();
                                    }
                                } else
                                    Toast.makeText(TheService.this, "Clipboard is empty.", Toast.LENGTH_SHORT).show();
                            }*/
                            }
                        }
                    }
                });
                //Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
