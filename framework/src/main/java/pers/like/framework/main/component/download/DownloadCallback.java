package pers.like.framework.main.component.download;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Response;

/**
 * @author like
 */
public abstract class DownloadCallback {

    private String destFileDir;
    private String destFileName;

    public DownloadCallback(@NonNull String destFileDir, @NonNull String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    public abstract void success(File file);

    public abstract void failed();

    public abstract void progress(float percent, long breakpoint, long total);

    public void processResponse(Response response) {
        saveFile(response);
    }

    @SuppressLint("CheckResult")
    public void saveFile(Response response) {
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                boolean b = dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                Observable.timer(0, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> progress(finalSum * 1.0f / total, finalSum, total));
            }
            fos.flush();
            Observable.timer(0, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> success(file));
        } catch (IOException e) {
            e.printStackTrace();
            Observable.timer(0, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> failed());
        } finally {
            try {
                response.body().close();
                if (is != null) {
                    is.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignored) {
            }

        }
    }


}
