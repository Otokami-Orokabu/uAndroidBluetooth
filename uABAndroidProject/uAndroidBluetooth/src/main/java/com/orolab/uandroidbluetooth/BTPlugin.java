package com.orolab.uandroidbluetooth;

public class BTPlugin {
    private static BTMain btMain = new BTMain();

    public static void init(){
        btMain.init();

        btMain.DevicesDialog();
    }

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
}
