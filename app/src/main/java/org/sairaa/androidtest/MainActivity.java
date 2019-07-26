package org.sairaa.androidtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ACTION_UPDATE_NOTIFICATION = "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
    private Calculator calculator;

    private TextView testText;
    private Button testB;

    private RecyclerView testRv;
    private TestAdapter adapter;

    private Button notify;
    private Button update;
    private Button cancel;

    private static final String PRIMARY_CHANNEL_ID = "primary_channel_id";
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0;

    private NotificationReceiver notificationReceiver;
    private NotificationReceiverSub nReceiver;

    private JobScheduler jobScheduler;
    private static final int JOB_ID = 0;
    private Button scheduleJobButton;
    private Button cancelJobButton;

    private Button settingPrefButton;
    private String sharedPrefFile =
            "org.sairaa.android.hellosharedprefs";
    private SharedPreferences mPreferences;

    private List<String> stringList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(notificationReceiver);
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(notificationReceiver);
        this.unregisterReceiver(nReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testText = findViewById(R.id.text_view1);
        testB = findViewById(R.id.test);
        testB.setOnClickListener(this);

        notify = findViewById(R.id.notify);
        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);

        notify.setOnClickListener(this);
        createNotificationChannel();

        update.setOnClickListener(this);
        cancel.setOnClickListener(this);

        settingPrefButton = findViewById(R.id.setting);
        settingPrefButton.setOnClickListener(this);

        PreferenceManager.setDefaultValues(MainActivity.this,R.xml.preference,false);

        sharedPrefreanceValue();

        scheduleJobButton = findViewById(R.id.schedule_job);
        scheduleJobButton.setOnClickListener(this);

        cancelJobButton = findViewById(R.id.cancel_job);
        cancelJobButton.setOnClickListener(this);

        notificationReceiver = new NotificationReceiver();


        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);


        this.registerReceiver(notificationReceiver,filter);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(notificationReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));


        nReceiver = new NotificationReceiverSub();
        this.registerReceiver(nReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);

        testRv = findViewById(R.id.test_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        testRv.setLayoutManager(layoutManager);
        adapter = new TestAdapter(this);
        testRv.setAdapter(adapter);
        stringList.clear();
        for (int i = 0;i<100; i++){
            stringList.add(String.valueOf(i));
        }
        adapter.updateList(stringList);


        calculator = new Calculator();

        int c = caluclate(5,6);

    }

    private void sharedPrefreanceValue() {
//        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
////                PreferenceManager.getDefaultSharedPreferences(this);
//        String listPrefs = sharedPreferences.getString("listpref", "Default list prefs");
//        Toast.makeText(this, ""+listPrefs, Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(MainActivity.this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingActivity.KEY_PREF_EXAMPLE_SWITCH, false);

        //uncomment this to get toast of boolean

//        Toast.makeText(this, switchPref.toString(),
//                Toast.LENGTH_SHORT).show();

        String listPrefs = sharedPref.getString(SettingActivity.KEY_LIST_PREF, "Default list prefs");
        Toast.makeText(this, ""+listPrefs, Toast.LENGTH_SHORT).show();

    }

    private int caluclate(int a, int b) {
        return calculator.add(a,b);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the scores.
//        outState.putInt(STATE_SCORE_1, Integer.parseInt(number1.getText().toString().trim()));
//        outState.putInt(STATE_SCORE_2, Integer.parseInt(number2.getText().toString().trim()));
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.test:
                testText.setText("Hello");
                break;

            case R.id.notify:
                sendNotification();
                break;

            case R.id.update:
//                updateNotification();
                sendBroadcastToUpdateNotification();
                break;

            case R.id.cancel:
                cancelNotification();
                break;
            case R.id.schedule_job:
                scheduleJob();
                break;

            case R.id.cancel_job:
                cancelJob();
                break;
            case R.id.setting:
                Intent intent = new Intent(this,SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void cancelJob() {
        if (jobScheduler!=null){
            jobScheduler.cancelAll();
            jobScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleJob() {

        ComponentName serviceName = new ComponentName(getPackageName(),NotificationJobService.class.getName());
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID,serviceName);
        jobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setOverrideDeadline(10*1000);

        JobInfo jobInfo = jobBuilder.build();
        jobScheduler.schedule(jobInfo);
        Toast.makeText(this, "Job Scheduled, job will run when " +
                "the constraints are met.", Toast.LENGTH_SHORT).show();


    }

    private void sendBroadcastToUpdateNotification() {
        Intent updateNotificationIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateNotificationIntent);
    }

    private void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void updateNotification() {
        Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.om);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
//        notifyBuilder.addAction(R.drawable.ic_launcher_foreground,"Update Notification",)
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
        .bigPicture(image)
        .setBigContentTitle("Notification Updated")
        .setSummaryText("hello notification updated and continue to update further as well. Good Luck guy")
        );

//        notifyBuilder.setStyle(new NotificationCompat.BigTextStyle()
//        .bigText("hello notification updated and continue to update further as well. Good Luck guy"));

        notificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }

    private void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent =
                PendingIntent.getBroadcast(this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_ONE_SHOT);

        notifyBuilder.addAction(R.drawable.ic_launcher_foreground,"Update Notification broad cast",updatePendingIntent);
        notificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());


    }

    private void createNotificationChannel(){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //create Notification Channel
            NotificationChannel notificationChannel =
                    new NotificationChannel(PRIMARY_CHANNEL_ID,"Masscot Notification",NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                .setContentTitle("Notificatio")
                .setContentText("notified")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        return notificationBuilder;

    }
    public class NotificationReceiverSub extends BroadcastReceiver {

        public NotificationReceiverSub() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the notification
            String action = intent.getAction();
            switch (action){
                case ACTION_UPDATE_NOTIFICATION:
                    Toast.makeText(context, "update Notification", Toast.LENGTH_SHORT).show();
                    updateNotification();
                    break;
            }

        }
    }
}
