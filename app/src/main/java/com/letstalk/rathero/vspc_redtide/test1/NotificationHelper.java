package com.letstalk.rathero.vspc_redtide.test1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


public class NotificationHelper {

    public static void ShowNotification(Context context, Message message){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_camera);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_camera) // notification icon
                .setContentTitle(message.Username) // title for notification
                .setContentText(message.Text) // message for notification
                .setLargeIcon(bitmap)
                .setSound(uri)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH); // clear notification after clic
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("Let's Talk",message.TimeStamp.intValue(), mBuilder.build());
    }
}
