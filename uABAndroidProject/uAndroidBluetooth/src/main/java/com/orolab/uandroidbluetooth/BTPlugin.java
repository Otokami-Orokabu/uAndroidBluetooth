package com.orolab.uandroidbluetooth;

public class BTPlugin {
    private static BTMain btMain = new BTMain();

    /**
     * 初期化
     */
    public static void init(){
        btMain.init();

        btMain.DevicesDialog();
    }

    /**
     *
     */
    public static boolean BTEnable = btMain.isEnable();

    /**
     * デバイス名指定接続
     * @param deviceName
     */
    public static void Connect(String deviceName){
        btMain.doConnect(deviceName);
    }

    /**
     * 接続終了
     */
    public static void Close(){
        btMain.doClose();
    }

    /**
     * 受信スレッド
     * @return
     */
    public static byte[] Receive(){
        return btMain.doReceive();
    }
}
