package com.letstalk.rathero.vspc_redtide.test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent("com.letstalk.rathero.vspc_redtide.test1.NotificationService");
        context.startService(serviceIntent);
    }
}
