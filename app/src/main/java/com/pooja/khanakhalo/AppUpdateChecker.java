package com.pooja.khanakhalo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;

class AppUpdateChecker {
    private Activity activity;
    AppUpdateChecker(Activity activity){
        this.activity = activity;
    }
    //Device App Version
    private String getCurrentVersion(){
        PackageManager packageManager = activity.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(activity.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException exception){
            exception.printStackTrace();
        }
        assert packageInfo != null;
        return packageInfo.versionName;
    }

    private class GetLatestVersion extends AsyncTask<String,String,String>{
        private String latestVersion;
        private ProgressDialog progressDialog;
        private boolean mannualCheck;
        GetLatestVersion(boolean mannualCheck){
            this.mannualCheck = mannualCheck;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(mannualCheck){
                if(progressDialog!=null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
            String currentVersion = getCurrentVersion();
            if(!currentVersion.equals(latestVersion)&&latestVersion!=null){
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("An Update Available");
                builder.setMessage("Its better to update now, otherwise you miss the letest features.");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+activity.getPackageName())));
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel button action
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }else {
                if (mannualCheck) {
                    Toast.makeText(activity, "No Update Available", Toast.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mannualCheck) {
                progressDialog=new ProgressDialog(activity);
                progressDialog.setMessage("Checking For Update.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return latestVersion;
            } catch (Exception e) {
                return latestVersion;
            }
        }
    }
    void checkForUpdate()
    {
        new GetLatestVersion(false).execute();
    }
}