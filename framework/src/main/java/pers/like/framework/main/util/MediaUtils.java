package pers.like.framework.main.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 媒体类型工具包
 *
 * @author Like
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@SuppressLint("SimpleDateFormat")
public class MediaUtils {
    private static Map<String, String> FORMAT_TO_CONTENT_TYPE = new HashMap<>();
    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String PHOTO = "photo";
    public static final String IMAGE = "image";
    public static final String FILE = "file";
    public static final String CONTENT = "content";
    public static final String PRIMARY = "primary";

    static {
        // 音频
        FORMAT_TO_CONTENT_TYPE.put("mp3", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("mid", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("midi", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("asf", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("wm", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("wma", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("wmd", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("amr", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("wav", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("3gpp", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("mod", AUDIO);
        FORMAT_TO_CONTENT_TYPE.put("mpc", AUDIO);

        // 视频
        FORMAT_TO_CONTENT_TYPE.put("fla", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("flv", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("wmv", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("avi", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("rm", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("rmvb", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("3gp", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("mp4", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("mov", VIDEO);

        // flash
        FORMAT_TO_CONTENT_TYPE.put("swf", VIDEO);
        FORMAT_TO_CONTENT_TYPE.put("null", VIDEO);

        // 图片
        FORMAT_TO_CONTENT_TYPE.put("jpg", PHOTO);
        FORMAT_TO_CONTENT_TYPE.put("jpeg", PHOTO);
        FORMAT_TO_CONTENT_TYPE.put("png", PHOTO);
        FORMAT_TO_CONTENT_TYPE.put("bmp", PHOTO);
        FORMAT_TO_CONTENT_TYPE.put("gif", PHOTO);
    }

    public static final int REQUEST_PHOTO = 0x00630;
    public static final int REQUEST_ALBUM = 0x00631;
    public static final int REQUEST_CROP = 0x00632;
    public static final int REQUEST_COMPRESS = 0x00633;


    /**
     * 存放app部分文件的sd卡目录
     */
    private static final String SD_STORAGE_DIR_NAME = "MediaUtils";
    private static final String SAVE_PHONE_NAME_TEMP = "IMG";
    /**
     * 裁剪后的图片存放名字
     */
    private static final String SAVE_PHONE_NAME_CROP = "IMG_CROP";
    /**
     * 拍照产生的照片
     */
    private static File photoFile;
    /**
     * 裁剪后的图片文件
     */
    private static File cropPhotoFile;


    /**
     * 拍照获取
     */
    public static void getPhotoFromCamera(@NonNull Object target) {
        Context context;
        if (target instanceof Fragment) {
            context = ((Fragment) target).requireContext();
        } else if (target instanceof Activity) {
            context = (Context) target;
        } else {
            throw new IllegalArgumentException("target must be an Activity or a Fragment");
        }

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toasty.error(context, "未找到SD卡").show();
            return;
        }
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File saveDir = new File(savePath, SD_STORAGE_DIR_NAME);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                Toasty.error(context, "文件创建失败!").show();
                return;
            }
        }
        String timeStamp = new SimpleDateFormat("MMddHHMMss")
                .format(new Date());
        photoFile = new File(saveDir.getAbsolutePath(), SAVE_PHONE_NAME_TEMP + timeStamp + ".jpg");
        if (photoFile.exists()) {
            if (!photoFile.delete()) {
                Toasty.warning(context, "缓存文件删除失败!").show();
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }
        if (target instanceof Fragment) {
            ((Fragment) target).startActivityForResult(intent, REQUEST_PHOTO);
        } else {
            ((Activity) target).startActivityForResult(intent, REQUEST_PHOTO);
        }
    }

    /**
     * 从手机相册获取
     */
    public static void getPhotoFromAlbum(@NonNull Object target) {
        Context context;
        if (target instanceof Fragment) {
            context = ((Fragment) target).requireContext();
        } else if (target instanceof Activity) {
            context = (Context) target;
        } else {
            throw new IllegalArgumentException("target must be an Activity or a Fragment");
        }
        try {
            int sdkInt = Build.VERSION.SDK_INT;
            Intent intent;
            if (sdkInt >= Build.VERSION_CODES.KITKAT) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent = Intent.createChooser(pickIntent, "选择图片");
            } else {
                Intent contentIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentIntent.setType("image/*");
                intent = Intent.createChooser(contentIntent, "选择图片");
            }
            if (target instanceof Fragment) {
                ((Fragment) target).startActivityForResult(intent, REQUEST_ALBUM);
            } else {
                ((Activity) target).startActivityForResult(intent, REQUEST_ALBUM);
            }
        } catch (Exception e) {
            Toasty.error(context, "未找到系统相册").show();
        }
    }

    public static void onActivityResult(final @NonNull Object target, final Handler handler,
                                        final int requestCode, int resultCode, final Intent data, boolean needCrop) {
        onActivityResult(target, handler, requestCode, resultCode, data, needCrop, new Crop());
    }

    public static void onActivityResult(final @NonNull Object target,
                                        final Handler handler, final int requestCode, int resultCode,
                                        final Intent data, boolean needCrop, Crop params) {

        Context context;
        if (target instanceof Fragment) {
            context = ((Fragment) target).requireContext();
        } else if (target instanceof Activity) {
            context = (Context) target;
        } else {
            throw new IllegalArgumentException("target must be an Activity or a Fragment");
        }

        if (resultCode != Activity.RESULT_OK) {
            Toasty.error(context, "图片无法获取").show();
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            if (needCrop) {
                cropPhoto(REQUEST_PHOTO, Uri.fromFile(photoFile), target, params);
            } else {
                Message msg = handler.obtainMessage(0, photoFile.getAbsolutePath());
                handler.sendMessage(msg);
            }
        }

        if (requestCode == REQUEST_ALBUM) {
            if (data == null || data.getData() == null) {
                Toasty.error(context, "无法获取该图片").show();
                return;
            }
            if (needCrop) {
                Uri uri;
                if (isGooglePhotosUri(data.getData())) {
                    uri = Uri.parse(data.getData().getLastPathSegment());
                } else if (isGooglePlayPhotosUri(data.getData())) {
                    uri = Uri.parse(getImageUrlWithAuthority(context, data.getData()));
                } else {
                    uri = data.getData();
                }
                cropPhoto(REQUEST_ALBUM, uri, target, params);
            } else {
                Message msg = handler.obtainMessage(0, getPath(context, data.getData()));
                handler.sendMessage(msg);
            }
        }

        if (requestCode == REQUEST_CROP) {
            Message msg = handler.obtainMessage(0, cropPhotoFile.getAbsolutePath());
            handler.sendMessage(msg);
        }
    }

    private static boolean isPhotoFormat(String uri) {
        boolean flag = true;
        if (uri.contains(FILE)) {
            String attFormat = uri.substring(uri.lastIndexOf('.') + 1);
            flag = PHOTO.equals(getContentType(attFormat));
        }
        return flag;
    }


    private static void cropPhoto(int type, Uri uri, @NonNull Object target, Crop params) {

        Context context;
        if (target instanceof Fragment) {
            context = ((Fragment) target).requireContext();
        } else if (target instanceof Activity) {
            context = (Context) target;
        } else {
            throw new IllegalArgumentException("target must be an Activity or a Fragment");
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        String providerPath = context.getPackageName() + ".fileProvider";

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            File saveDir = new File(savePath, SD_STORAGE_DIR_NAME);
            if (!saveDir.exists()) {
                if (!saveDir.mkdirs()) {
                    Toasty.error(context, "文件创建失败").show();
                    return;
                }
            }
            String timeStamp = new SimpleDateFormat("MMddHHMMss")
                    .format(new Date());

            cropPhotoFile = new File(saveDir.getAbsolutePath(), SAVE_PHONE_NAME_CROP
                    + timeStamp + ".jpg");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri inUri;
            if (type != REQUEST_ALBUM) {
                inUri = FileProvider.getUriForFile(context, providerPath, photoFile);
            } else {
                inUri = uri;
            }
            intent.setDataAndType(inUri, "image/*");

            Uri outUri = FileProvider.getUriForFile(context, providerPath, cropPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);

            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, inUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.grantUriPermission(packageName, outUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropPhotoFile));
        }

        intent.putExtra("outputFormat", params.outputFormat());
        intent.putExtra("crop", params.crop() ? "true" : "false");
        intent.putExtra("aspectX", params.aspectX());
        intent.putExtra("aspectY", params.aspectY());
        intent.putExtra("outputX", params.outputX());
        intent.putExtra("outputY", params.outputY());
        intent.putExtra("scale", params.scale());
        intent.putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded());
        intent.putExtra("return-data", false);
        if (target instanceof Fragment) {
            ((Fragment) target).startActivityForResult(intent, REQUEST_CROP);
        } else {
            ((Activity) target).startActivityForResult(intent, REQUEST_CROP);
        }
    }


    private static void getCropPhotoFile() {


    }

    /**
     * 根据根据扩展名获取类型
     */
    private static String getContentType(String attFormat) {
        String contentType = FORMAT_TO_CONTENT_TYPE.get("null");

        if (attFormat != null) {
            contentType = FORMAT_TO_CONTENT_TYPE.get(attFormat
                    .toLowerCase());
        }
        return contentType;
    }

    @SuppressLint("ObsoleteSdkInt")
    private static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if (PRIMARY.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if (IMAGE.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (VIDEO.equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (AUDIO.equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            } else if (isGooglePlayPhotosUri(uri)) {
                return getImageUrlWithAuthority(context, uri);
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if (FILE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Google相册图片获取路径
     **/
    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将图片流读取出来保存到手机本地相册中
     **/
    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {column};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     **/
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     **/
    public static boolean isGooglePlayPhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}