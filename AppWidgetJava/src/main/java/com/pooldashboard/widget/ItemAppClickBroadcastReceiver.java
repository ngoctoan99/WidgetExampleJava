package com.pooldashboard.widget;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.pooldashboard.widget.Constant.EXTRA_PACKAGE_NAME;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

public class ItemAppClickBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
        if (checkAppInstall(packageName, context)) {

            assert packageName != null;
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(launchIntent);
            //   PendingIntent.getActivity(context,0,launchIntent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {

            Uri linkUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
            Intent intent1 = new Intent(Intent.ACTION_VIEW, linkUri);
            intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

            //   PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private boolean checkAppInstall(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(uri, PackageManager.PackageInfoFlags.of(0L));
            } else {
                pm.getPackageInfo(uri, 0);
            }
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}