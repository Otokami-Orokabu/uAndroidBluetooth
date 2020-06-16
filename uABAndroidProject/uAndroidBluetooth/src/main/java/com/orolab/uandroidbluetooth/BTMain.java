package com.orolab.uandroidbluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.unity3d.player.UnityPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BTMain {
    private static final String TAG ="BTMain";
    /**
     * Bluetooth 接続時のUUID(SPP Profile)
     */
    private static final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;

    private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static BluetoothDevice bluetoothDevice = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static InputStream btIn;
    private static OutputStream btOut;

    /**
     * 初期化
     */
    public void init(){
        if(bluetoothAdapter == null){
            BTDialog.showErrorDialog("This device is not implement Bluetooth.");
            return;
        }
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            UnityPlayer.currentActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     *
     * @return　ペアリング済みのデバイス一覧を返す
     */
    public Set<BluetoothDevice> getPairedDevices(){
        return bluetoothAdapter.getBondedDevices();
    }

    /**
     * 非同期接でデバイスに接続を開始する
     * @param device
     */
    public void doConnect(BluetoothDevice device) {
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(APP_UUID);
            new ConnectTask().execute();
        } catch (IOException e) {
            Log.e(TAG, e.toString(), e);
            BTDialog.showWarmomgDialog(e.toString());
        }

    }

    /**
     * デバイス名で接続を開始する
     * @param string
     */
    @NonNull
    public void doConnect(String string){
        for(BluetoothDevice device : getPairedDevices()){
            if(device.getName().equals(string)){
                // 接続処理に流す
                doConnect(device);
            }
        }
    }

    /**
     * 非同期で接続を閉じる
     */
    public void doClose(){
        new CloseTask().execute();
    }

    /**
     * 非同期でメッセージ送信
     * @param data
     */
    public void doSend(byte[] data){
        new SendTask().execute(data);
    }

    /**
     * メッセージ受信
     * @return
     */
    public byte[] doReceive(){
        return new byte[0];
    }

    /**
     * デバイス選択ダイアログ
     */
    public void DevicesDialog(){
        // デバイス取得
        Set<BluetoothDevice> pairedDevices = getPairedDevices();
        final BluetoothDevice[] devices = pairedDevices.toArray(new BluetoothDevice[0]);
        String[] items = new String[devices.length];
        for(int i=0;i<devices.length;i++) {
            items[i] = devices[i].getName();
        }

        // ダイアログ構築
        new AlertDialog.Builder(UnityPlayer.currentActivity)
                .setTitle("Select device")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();;
                        // 選択されたデバイスを通知する、そのまま接続開始
                        doConnect(devices[which]);
                    }
                })
                .show();
    }

    //-------------------------------------------------------
    // 接続処理

    /**
     * 非同期接続
     */
    private class ConnectTask extends AsyncTask<Void, Void,Object>{
        @Override
        protected void onPreExecute() {
            //activity.showWaitDialog("Connect Bluetooth Device.");
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                bluetoothSocket.connect();
                btIn = bluetoothSocket.getInputStream();
                btOut = bluetoothSocket.getOutputStream();
            } catch (Throwable t) {
                doClose();
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e(TAG,result.toString(),(Throwable)result);
                BTDialog.showErrorDialog(result.toString());
            } else {
                //activity.hideWaitDialog();
            }
        }
    }

    //-------------------------------------------------------
    // 切断処理

    /**
     * 非同期接続終了
     */
    private class CloseTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            try {
                try{btOut.close();}catch(Throwable t){/*ignore*/}
                try{btIn.close();}catch(Throwable t){/*ignore*/}
                bluetoothSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e(TAG,result.toString(),(Throwable)result);
                BTDialog.showErrorDialog(result.toString());
            }
        }
    }

    //-------------------------------------------------------
    // 非同期送信
    private class SendTask extends AsyncTask<byte[], Void, Object> {
        @Override
        protected Object doInBackground(byte[]... params) {
            try {
                btOut.write(params[0]);
                btOut.flush();
                return "";
            } catch (Throwable t) {
                doClose();
                return t;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Exception) {
                Log.e(TAG,result.toString(),(Throwable)result);
                BTDialog.showErrorDialog(result.toString());
            }
        }
    }
}
