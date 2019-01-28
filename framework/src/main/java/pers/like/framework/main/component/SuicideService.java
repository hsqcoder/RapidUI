package pers.like.framework.main.component;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * @author like
 */
public class SuicideService extends Service {
    private Handler handler;

    public SuicideService() {
        handler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        long stopDelayed = intent.getLongExtra("delayed", 2000);
        String packageName = intent.getStringExtra("packageName");
        handler.postDelayed(() -> {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            startActivity(launchIntent);
            SuicideService.this.stopSelf();
        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}