package com.calculator.vault.gallery.locker.hide.data.googleDrive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;

import ir.mahdi.mzip.zip.ZipArchive;

public class UploadService extends Service {

    private static final String TAG = "UploadService";
    private DriveServiceHelper mDriveServiceHelper;
    private String mOpenFileId;
    private String fileId;
    private NotificationManager mNotificationManager;
    private NotificationManagerCompat notificationManager;
    private Notification.Builder notification;
    private NotificationCompat.Builder builder;
    public static boolean isUploadRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }



    /**
     * Opens the Storage Access Framework file picker using @link REQUEST_CODE_OPEN_DOCUMENT
     */
    private void upload() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening file picker.");

            mDriveServiceHelper.fileinfolder()
                    .addOnSuccessListener(fileId -> {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            notification.setOngoing(false);
                            notification.setContentText("Backup Uploaded.");
                            notification.setProgress(0, 0, false);
                            mNotificationManager.notify(3, notification.build());

                        } else {

                            builder.setOngoing(false);
                            builder.setContentText("Backup Uploaded.");
                            builder.setProgress(0, 0, false);
                            notificationManager.notify(3, builder.build());

                        }

                        this.stopSelf();

                    })
                    .addOnFailureListener(exception -> {
                        Log.e(TAG, "Couldn't create file.", exception);
                        //Toast.makeText(this, "Backup failed.", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            notification.setOngoing(false);
                            notification.setContentText("Backup Failed.");
                            notification.setProgress(0, 0, false);
                            mNotificationManager.notify(3, notification.build());

                        } else {

                            builder.setOngoing(false);
                            builder.setContentText("Backup Failed.");
                            builder.setProgress(0, 0, false);
                            notificationManager.notify(3, builder.build());

                        }

                        this.stopSelf();

                    });
        }
    }

    /**
     * Queries the Drive REST API for files visible to this app and lists them in the content view.
     */
//    private void query() {
//        if (mDriveServiceHelper != null) {
//            Log.d(TAG, "Querying for files.");
//
//            mDriveServiceHelper.queryFiles().addOnSuccessListener(fileList -> {
//                        StringBuilder builder = new StringBuilder();
//                        for (File file : fileList.getFiles()) {
//                            builder.append(file.getName()).append("\n");
////                            mDriveServiceHelper.downloadfile(file.getId());
//                            fileId = file.getId();
//                            if (fileId != null && !fileId.trim().isEmpty()){
//                                mDriveServiceHelper.deleteFileFromDrive(fileId);
//                            }
//                        }
//                    })
//                    .addOnFailureListener(exception -> Log.e(TAG, "Unable to query files.", exception));
//            Log.d(TAG, "Querying for files.  " + fileId);
//        }
//    }

    private class UpoadTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... strings) {


            Log.d(TAG, "doInBackground: upload task");

            try {

                if (mDriveServiceHelper != null) {

                    Log.d(TAG, "doInBackground: upload task noy null");


                    Log.d(TAG, "Querying for files.");

                    mDriveServiceHelper.queryFiles().addOnSuccessListener(fileList -> {
                        StringBuilder builder = new StringBuilder();
                        for (File file : fileList.getFiles()) {
                            builder.append(file.getName()).append("\n");
                            fileId = file.getId();
                            if (fileId != null && !fileId.trim().isEmpty()) {

                                mDriveServiceHelper.deleteFileFromDrive(fileId);
                            }

                        }

                        Log.d(TAG, "doInBackground: upload task success");


                    })
                            .addOnFailureListener(exception -> {
                                Log.d(TAG, "doInBackground: upload task failed");

                                Log.e(TAG, "Unable to query files.", exception);
                            });
                    Log.d(TAG, "Querying for files.  " + fileId);
                }

                return true;
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: upload task failed null"+e.toString());

                Log.i(TAG, "doInBackground: " + e.toString());
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid) {


                try {
                    Log.d(TAG, "onPostExecute: try");
                    upload();
                } catch (Exception e) {
                    Log.d(TAG, "onPostExecute: catch" + e.toString());
                    Toast.makeText(UploadService.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(UploadService.this, "Errrrrr", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ZipingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            Log.d(TAG, "doInBackground: ZipingTask");

            if (new java.io.File(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + "CalculatorBackup.zip").exists()) {
                java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + "CalculatorBackup.zip");
                if (file.exists()) {

                    Log.d(TAG, "doInBackground: ZipingTask file exist");


                    try {
                        file.delete();
                        file.getCanonicalFile().delete();
                        if (file.exists()) {
                            getApplicationContext().deleteFile(file.getName());
                        }
                    } catch (IOException e) {

                        Log.d(TAG, "doInBackground: ZipingTask: file exist " + e.toString());


                        e.printStackTrace();
                    }
                }
            }

            String outputPath = Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + "CalculatorBackup.zip";
            ZipArchive.zip(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + ".androidData", outputPath, "");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: ZipingTask");

            new UpoadTask().execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isUploadRunning = false;
    }
}
