package com.example.mediasample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //メディアプレーヤフィールド
    private MediaPlayer _palyer;
    //再生・一時停止ボタンフィールド
    private Button _btPlay;
    //戻るボタンフィールド
    private Button _btBack;
    //進むボタンフィールド
    private Button _btForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //フィールドの各ボタンを取得
        _btPlay=findViewById(R.id.btPlay);
        _btBack=findViewById(R.id.btBack);
        _btForward=findViewById(R.id.btForward);
        //フィールドのメディアプレーヤーオブジェクトを生成
        _palyer=new MediaPlayer();
        //音声ファイルのURI文字列を作成。
        String mediaFileUriStr="android.resource://"+getPackageName()+"/"+R.raw.elephant1;
        //音声ファイルのURI文字列をもとにURIオブジェトを生成。
        Uri mediaFileUri=Uri.parse(mediaFileUriStr);
        try{
            //メディアプレイヤーに音声ファイルを指定
            _palyer.setDataSource(MainActivity.this,mediaFileUri);
            //非同期でのメディア再生準備が完了した際のリスナを設定
            _palyer.setOnPreparedListener(new PlayerPreparedListener());
            //メディア再生が終了した際のリスナを設定。
            _palyer.setOnCompletionListener(new PlayerCompletionListener());
            //非同期でメディア再生を準備
            _palyer.prepareAsync();
        }catch (IOException e){
            e.printStackTrace();
        }
     //スイッチを取得。
        Switch loopSwitch =findViewById(R.id.swLoop);
        //スイッチにリスナを設定
        loopSwitch.setOnCheckedChangeListener(new LoopSwichChangedListener());
    }
    //プレイヤーの再生準備が整った時のリスナクラス
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            //各ボタンをタップ可能に
            _btPlay.setEnabled(true);
            _btBack.setEnabled(true);
            _btForward.setEnabled(true);
        }
    }

    //再生が終了した時のリスナクラス
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void  onCompletion(MediaPlayer mp){
          //ループが設定されていなければ
            if(!_palyer.isLooping()){
                //再生ボタンのラベルを「再生」に設定。
                _btPlay.setText(R.string.bt_play_play);
            }
        }
    }
    public  void  onPlayButtonClick(View view){
        //プレイヤーが再生中だったら
        if(_palyer.isPlaying()){
            //プレイヤーを一時停止
            _palyer.pause();
            //再生ボタンのラベルを「再生」に設定。
            _btPlay.setText(R.string.bt_play_play);
        }
        //プレイヤーが再生中でなかったら
        else{
            //プレイヤーを再生
            _palyer.start();
            //再生ボタンのラベルを「一時停止」に設定。
            _btPlay.setText(R.string.bt_play_pause);

        }


    }
    @Override
    protected  void onDestroy(){
        //親クラスのメソッド呼び出し
        super.onDestroy();
        //プレーヤーが再生中なら
        if(_palyer.isPlaying()){
            //プレイヤーを停止
            _palyer.stop();
        }
        //プレーヤーを解放
        _palyer.release();
        //プレーヤー用フィールドをnullに。
        _palyer=null;

    } public void onBackButtonClick(View view){
        //再生位置を先頭に変更
        _palyer.seekTo(0);
    }
    public void onForwardButtonClick(View view){
        //現在再生中のメディアファイルの長さを取得。
        int duration=_palyer.getDuration();
        //再生位置を終端に変更
        _palyer.seekTo(duration);
        //再生中でないなら
        if(!_palyer.isPlaying()){
            //再生を開始
            _palyer.start();
        }
    }

    private class LoopSwichChangedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            //ループするかどうかを設定。
            _palyer.setLooping(isChecked);
        }
    }

}
