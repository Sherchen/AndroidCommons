package com.sherchen.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author: Sherchen
 *     blog  : http://blog.csdn.net/ncuboy045wsq
 *     time  : 2017/5/16
 *     desc  : 相机，图库相关工具类
 * </pre>
 */
public final class CamPhotoUtils {

    /**
     * 打开相机
     * @param activity
     * @param requestCode
     */
    public static void openCamera(Activity activity, int requestCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 根据指定的图片路径打开相机
     * @param activity
     * @param requestCode
     * @param path 图片文件路径
     */
    public static void openCamera(Activity activity, int requestCode, final String path){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开图库选择图片
     * @param activity
     * @param requestCode
     */
    public static void openGallery4Pick(Activity activity, int requestCode){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        activity.startActivityForResult(i, requestCode);
    }

    public static void openKitkatGallery4Pick(Activity activity, int requestCode){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取拍照之后的照片文件（JPG格式）
     *
     * @param data     onActivityResult回调返回的数据
     * @param filePath 保存图片的文件路径
     * @return 文件
     */
    public static boolean saveImage2File(Intent data, String filePath) {
        if (data == null) return false;
        Bundle extras = data.getExtras();
        if (extras == null) return false;
        Bitmap photo = extras.getParcelable("data");
        File file = new File(filePath);
        if (ImageUtils.save(photo, file, Bitmap.CompressFormat.JPEG)) return true;
        return false;
    }

    /**
     * 获得选中相册的图片
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     * @return bitmap
     */
    public static Bitmap getChoosedImageBitmap(Activity context, Intent data) {
        if (data == null) return null;
        Bitmap bm = null;
        ContentResolver cr = context.getContentResolver();
        Uri originalUri = data.getData();
        try {
            bm = MediaStore.Images.Media.getBitmap(cr, originalUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 获得选中相册的图片路径
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     * @return
     */
    public static String getChoosedImagePath(Activity context, Intent data) {
        if (data == null) return null;
        String path = "";
        ContentResolver resolver = context.getContentResolver();
        Uri originalUri = data.getData();
        if (null == originalUri) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = resolver.query(originalUri, projection, null, null, null);
        if (null != cursor) {
            try {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!cursor.isClosed()) {
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return StringUtils.isEmpty(path) ? originalUri.getPath() : null;
    }

    /**
     * 获得选中相册的图片路径(4.4以上)
     * @param context
     * @param data onActivityResult返回的Intent
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getChoosedImagePath4Kitkat(final Context context, final Intent data) {
        final Uri uri = data.getData();
        final boolean isKitKat = isKitKat();

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//    /**
//     * @return Whether the URI is a local one.
//     */
//    public static boolean isLocal(String url) {
//        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
//            return true;
//        }
//        return false;
//    }

//    /**
//     * Get a file path from a Uri. This will get the the path for Storage Access
//     * Framework Documents, as well as the _data field for the MediaStore and
//     * other file-based ContentProviders.<br>
//     * <br>
//     * Callers should check whether the path is local before assuming it
//     * represents a local file.
//     *
//     * @param context The context.
//     * @param uri The Uri to query.
//     * @see #isLocal(String)
//     * @see #getFile(Context, Uri)
//     * @author paulburke
//     */
//    public static String getPath(final Context context, final Uri uri) {
//
////		if (DEBUG)
////			Log.d(TAG + " File -",
////					"Authority: " + uri.getAuthority() +
////							", Fragment: " + uri.getFragment() +
////							", Port: " + uri.getPort() +
////							", Query: " + uri.getQuery() +
////							", Scheme: " + uri.getScheme() +
////							", Host: " + uri.getHost() +
////							", Segments: " + uri.getPathSegments().toString()
////			);
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // LocalStorageProvider
////			if (isLocalStorageDocument(uri)) {
////				// The path is the id
////				return DocumentsContract.getDocumentId(uri);
////			}
//            // ExternalStorageProvider
////			else
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[] {
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();
//
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }

//    /**
//     * Convert Uri into File, if possible.
//     *
//     * @return file A local file that the Uri was pointing to, or null if the
//     *         Uri is unsupported or pointed to a remote resource.
//     * @see #getPath(Context, Uri)
//     * @author paulburke
//     */
//    public static File getFile(Context context, Uri uri) {
//        if (uri != null) {
//            String path = getPath(context, uri);
//            if (path != null && isLocal(path)) {
//                return new File(path);
//            }
//        }
//        return null;
//    }

//    private CamPhotoUtils() {
//        throw new UnsupportedOperationException("u can't instantiate me...");
//    }
//
//    /**
//     * 获取打开照程序界面的Intent
//     */
//    public static Intent getOpenCameraIntent() {
//        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    }
//
//    /**
//     * 获取跳转至相册选择界面的Intent
//     */
//    public static Intent getImagePickerIntent() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        return intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//    }
//
//    /**
//     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
//     */
//    public static Intent getImagePickerIntent(int outputX, int outputY, Uri fromFileURI,
//                                              Uri saveFileURI) {
//        return getImagePickerIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI);
//    }
//
//    /**
//     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
//     */
//    public static Intent getImagePickerIntent(int aspectX, int aspectY, int outputX, int outputY, Uri fromFileURI,
//                                              Uri saveFileURI) {
//        return getImagePickerIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI);
//    }
//
    /**
     * 获取[跳转至相册选择界面,并跳转至裁剪界面，可以指定是否缩放裁剪区域]的Intent
     *
     * @param aspectX     裁剪框尺寸比例X
     * @param aspectY     裁剪框尺寸比例Y
     * @param outputX     输出尺寸宽度
     * @param outputY     输出尺寸高度
     * @param canScale    是否可缩放
     * @param fromFileURI 文件来源路径URI
     * @param saveFileURI 输出文件路径URI
     */
    public static Intent getImagePickerIntent(int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
                                              Uri fromFileURI, Uri saveFileURI) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(fromFileURI, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX <= 0 ? 1 : aspectX);
        intent.putExtra("aspectY", aspectY <= 0 ? 1 : aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", canScale);
        // 图片剪裁不足黑边解决
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 去除人脸识别
        return intent.putExtra("noFaceDetection", true);
    }

    /**
     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
     */
    public static Intent getCameraIntent(Uri saveFileURI) {
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return mIntent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
    }

    /**
     * 获取[跳转至裁剪界面,默认可缩放]的Intent
     */
    public static Intent getCropImageIntent(int outputX, int outputY, Uri fromFileURI,
                                            Uri saveFileURI) {
        return getCropImageIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI);
    }

    /**
     * 获取[跳转至裁剪界面,默认可缩放]的Intent
     */
    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, Uri fromFileURI,
                                            Uri saveFileURI) {
        return getCropImageIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI);
    }


    /**
     * 获取[跳转至裁剪界面]的Intent
     */
    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
                                            Uri fromFileURI, Uri saveFileURI) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fromFileURI, "image/*");
        intent.putExtra("crop", "true");
        // X方向上的比例
        intent.putExtra("aspectX", aspectX <= 0 ? 1 : aspectX);
        // Y方向上的比例
        intent.putExtra("aspectY", aspectY <= 0 ? 1 : aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", canScale);
        // 图片剪裁不足黑边解决
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        // 需要将读取的文件路径和裁剪写入的路径区分，否则会造成文件0byte
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
        // true-->返回数据类型可以设置为Bitmap，但是不能传输太大，截大图用URI，小图用Bitmap或者全部使用URI
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        return intent;
    }
}
