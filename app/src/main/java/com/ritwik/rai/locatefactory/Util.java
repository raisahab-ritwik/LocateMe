package com.ritwik.rai.locatefactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    private static String USERCLASS = "USERCLASS";

    private static final String PREF_NAME = "TrioPrefs";

    /**
     * To check Internet Connection
     */
    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conManager.getActiveNetworkInfo();
        if ((i == null) || (!i.isConnected()) || (!i.isAvailable())) {

            return false;
        }
        return true;
    }

    /* Show log
     * @param type of log 0 for info,1 error,2 for verbosa
     * @param logtitle
     * @param logcontent
     * @return
     */
    public static void printLog(int type, String logtitle, String logcontent) {

        switch (type) {
            case 0:
                Log.e(logtitle, logcontent + "");
                break;
            case 1:
                Log.e(logtitle, logcontent + "");
                break;
            case 2:
                Log.e(logtitle, logcontent + "");
                break;

            default:
                Log.e(logtitle, logcontent + "");
                break;
        }
    }

    /**
     * show global alert message
     *
     * @param context   application context
     * @param title     alert title
     * @param btn_title alert click button name
     * @param msg       alert message
     */
    public static void alertMessage(Context context, String title, String btn_title,
                                    String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(btn_title,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showMessageWithOk(final Context mContext, final String message) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);

                alert.setMessage(message);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });
    }

    public static void showCallBackMessageWithOkCallback(final Context mContext, final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onSubmit();
                    }
                });

                alert.show();
            }
        });
    }

    public static void showCallBackMessageWithOkCancel(final Context mContext, final String message, final AlertDialogCallBack callBack) {
        ((Activity) mContext).runOnUiThread(new Runnable() {

            public void run() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(R.string.app_name);
                alert.setCancelable(false);
                alert.setMessage(message);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        callBack.onSubmit();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //callBack.onSubmit();
                    }
                });

                alert.show();
            }
        });
    }

    /**
     * Saving UserClass details
     **/
    public static void saveUserClass(final Context mContext, UserClass userClass) {
        SharedPreferences userPreference = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = userPreference.edit();
        try {
            prefsEditor.putString(USERCLASS, ObjectSerializer.serialize(userClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsEditor.commit();
    }

    /**
     * Fetching UserClass details
     **/
    public static UserClass fetchUserClass(final Context mContext) {
        SharedPreferences userPreference = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        UserClass userClass = null;
        String serializeOrg = userPreference.getString(USERCLASS, null);
        try {
            if (serializeOrg != null) {
                userClass = (UserClass) ObjectSerializer.deserialize(serializeOrg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userClass;
    }

    public static String convertToMD5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {

            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it

        return dir.delete();
    }

    public static AlertDialog showSettingsAlert(final Context applicationContext,
                                                AlertDialog systemAlertDialog) {
        Log.e("showSettingsAlert()", "true");

        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext);
        builder.setTitle("GPS Disabled");
        builder.setIcon(R.drawable.warning);
        builder.setCancelable(false);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // mContext.startActivity(new
                // Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // <-- Newly added line
                applicationContext.startActivity(viewIntent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        systemAlertDialog = builder.create();
        systemAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        systemAlertDialog.show();
        return systemAlertDialog;
    }

    public static void showMessageWithOkFocus(final Context mContext,
                                              final String message, final ScrollView mScrollView, final View mView) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(R.string.app_name);

        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
//				view.requestFocus();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.scrollTo(0, mView.getBottom());
                    }
                });
            }
        });
        alert.show();
    }

    /**
     * Get base64 string from bitmap
     */
    public static String getBase64StringFromBitmap(Bitmap mBitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        return (Base64.encodeToString(ba, Base64.DEFAULT));
    }

    /**
     * Get bitmap from base64 string
     */
    public static Bitmap getBitmapBase64FromString(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return (decodedByte);
    }
}
