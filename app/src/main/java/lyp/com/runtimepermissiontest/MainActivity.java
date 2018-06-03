package lyp.com.runtimepermissiontest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button makeCall = findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 判断用户是否授权，借助 ContextCompat.checkSelfPermission() 方法
                // ContextCompat.checkSelfPermission() 方法接收两参数：context 和权限名
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                        CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    // 3. 若没授权，则调用 ActivityCompat.requestPermissions()方法来向用户授权
                    //三个参数：Activity实例；String数组(权限名)；请求码(唯一即可，这里传入1)
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                            Manifest.permission.CALL_PHONE},1);
                    // 4. 调用完 requestPermissions()方法后，会弹出一个权限申请对话框，供用户选择，
                    // 最后回调 onRequestPermissionsResult()方法
                }else {
                    // 2. 若已授权，则直接执行拨打电话的逻辑
                    call();
                }
            }
        });
    }

    private void call(){
        try {
            // Intent.ACTION_CALL 是系统内置的打电话动作（而Intent.ACTION_DIAL指打开拨号界面，不需要声明权限）
            Intent intent= new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    call();
                }else {
                    Toast.makeText(this,"你拒绝了权限请求",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }
}
