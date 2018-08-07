package com.tengbo.drivertransport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.tengbo.commonlibrary.widget.TakePhotoDialogFragment;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void trans(View v) {
//        CCResult ccResult = CC.obtainBuilder("y").
//                setActionName("startActivity")
//                .addParam("requestCode", 3)
//                .build().call();
//
//
//        Log.d("yk", ccResult.getCode() + "");
    }

    IComponentCallback printResultCallback = new IComponentCallback() {
        @Override
        public void onResult(CC cc, CCResult result) {
            showResult(cc, result);
        }
    };
    private void showResult(CC cc, CCResult result) {
        Log.e("RESULT", cc.toString());
        Log.e("RESULT", result.toString());
//        String text = "result:\n" + JsonFormat.format(result.toString());
//        text += "\n\n---------------------\n\n";
//        text += "cc:\n" + JsonFormat.format(cc.toString());
//        Log.e("RESULT", text);
    }



    public void getData(View v) {
        TakePhotoDialogFragment takePhotoDialogFragment = TakePhotoDialogFragment.newInstance(3);
        takePhotoDialogFragment.setOnResultListener(new TakePhotoDialogFragment.TakePhotoCallBack() {
        });

        takePhotoDialogFragment.show(getSupportFragmentManager(), "yk");
    }
}
