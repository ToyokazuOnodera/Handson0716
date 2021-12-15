package com.example.handson0716;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 *Androidアプリ  ハンズオン②
 */
public class MainActivity extends AppCompatActivity {

    //位置情報取得クライアント宣言
    private FusedLocationProviderClient fusedLocationClient;
    //取得した位置情報を保持するLocationオブジェクト
    private Location callbackLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 表示ボタンであるButtonオブジェクトを取得
        Button btClick = findViewById(R.id.btClick);
        // リスナクラスのインスタンスを生成
        HelloListener listener = new HelloListener();
        // 表示ボタンにリスナを設定
        btClick.setOnClickListener(listener);

        //※余裕のある方はクリアボタンの配置と、処理実装も試してみてください!
        // クリアボタンであるButtonオブジェクトを取得
        //Button btClear = findViewById(R.id.btClear);
        // クリアボタンにリスナを設定
        //btClear.setOnClickListener(listener);

        // 位置情報取得クライアントのインスタンスを生成
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // 位置情報取得開始
        startUpdateLocation();
    }

    /**
     * ボタンをクリックしたときのリスナクラス
     */
    private class HelloListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 名前入力欄であるEditTextオブジェクトを取得
            EditText input = findViewById(R.id.etName);
            // メッセージを表示するTextViewオブジェクトを取得
            TextView output = findViewById(R.id.tvOutput);

            // タップされた画面部品のidのR値を取得
            int id = view.getId();
            // idのR値に応じて処理を分岐
            switch(id) {
                // 表示ボタンの場合
                case R.id.btClick:
                    // 入力された名前文字列を取得
                    String inputStr = input.getText().toString();
                    // メッセージを表示
                    output.setText(inputStr + "さん、こんにちは!あなたの現在地は緯度:"
                            + callbackLocation.getLatitude() + " 経度:"
                            + callbackLocation.getLongitude() + "です！");
                    break;
                //※余裕のある方はクリアボタンの配置と、処理実装も試してみてください!
                // クリアボタンの場合
                //case R.id.btClear:
                // 名前入力欄を空文字に設定
                //    input.setText("");
                // メッセージ表示欄を空文字に設定
                //    output.setText("");
                //    break;
            }
        }
    }
    /**
     * 位置情報取得開始メソッド
     */
    private void startUpdateLocation() {
        // 位置情報取得権限の確認
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 権限がない場合、許可ダイアログを表示し一旦処理を抜ける
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 2000);
            return;
        }

        // 位置情報パラメータ設定
        //パラメータリクエスト生成
        LocationRequest locationRequest = LocationRequest.create();
        //位置情報更新間隔の希望値（ミリ秒）
        locationRequest.setInterval(10000);
        //位置情報更新間隔の最速値（ミリ秒）
        locationRequest.setFastestInterval(5000);
        //即位のプライオリティ指定
        locationRequest.setPriority(
                //高精度、GPS優先のため通信状況がよい場合に効果的、処理負荷・バッテリー負荷ともに高い
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        //GPSに加えてWIFIや電話網を活用する、バッテリー負荷は低いが精度も少し落ちる
        //LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //バッテリー負荷は低いが精度は非常に荒い
        //LocationRequest.PRIORITY_LOW_POWER);
        //測位しない、他のアプリで取得した位置情報があれば使用する
        //LocationRequest.PRIORITY_NO_POWER);
        //位置情報の追跡開始
        fusedLocationClient.requestLocationUpdates(locationRequest,  new MyLocationCallback(), null);
    }
    /**
     * 位置情報受取コールバッククラス（パラメータで設定した更新間隔により位置情報が取得されるたびに呼ばれるクラス）
     */
    private class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //何かの理由で位置情報が取得できなかった場合、今回は何もせず処理を抜ける
            if (locationResult == null) {
                return;
            }
            //位置情報を退避
            callbackLocation = locationResult.getLastLocation();
        }
    }
    /**
     * 位置情報取の使用許可確認（当クラスは機能使用の許可選択をした後に毎回呼ばれるクラス）
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 機能使用許可OKの場合、位置情報取得開始
            startUpdateLocation();
        }
    }
}
