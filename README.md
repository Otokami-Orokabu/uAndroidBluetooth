# uAndroidBluetooth

UnityでAndroidのBluetooth(シリアル通信)するやつ

## Android メモ

- InputStream? Bluetooth.getInputStream はconnect()すると、InputStreamにデータが溜まるらしい...
  - receiveを処理しない場合に、30秒ほどで落ちる原因だった
