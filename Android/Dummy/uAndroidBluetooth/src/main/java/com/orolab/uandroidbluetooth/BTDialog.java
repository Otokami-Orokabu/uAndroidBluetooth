package com.orolab.uandroidbluetooth;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.unity3d.player.UnityPlayer;

public class BTDialog {
    public static void showErrorDialog(String message){
        new AlertDialog.Builder(UnityPlayer.currentActivity)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UnityPlayer.currentActivity.finish();
                    }
                })
                .show();
    }
    public static void showWarmomgDialog(String message){
        new AlertDialog.Builder(UnityPlayer.currentActivity)
                .setTitle("info")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
