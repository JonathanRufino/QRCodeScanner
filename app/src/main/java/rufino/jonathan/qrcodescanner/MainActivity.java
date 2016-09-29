package rufino.jonathan.qrcodescanner;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Camera camera;
    private CameraPreview cameraPreview;

    private Button readQRCode;
    private FrameLayout cameraLayout;
    private TextView qrcodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraLayout = (FrameLayout) findViewById(R.id.camera_layout);
        qrcodeInfo = (TextView) findViewById(R.id.qrcode_info);
        readQRCode = (Button) findViewById(R.id.read_qrcode);

        readQRCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        camera = getCameraInstance();
        cameraPreview = new CameraPreview(getApplicationContext(), camera);
        cameraLayout.addView(cameraPreview);
    }

    public static Camera getCameraInstance(){
        Camera camera = null;

        try {
            camera = Camera.open();
        }
        catch (Exception e){
            Log.d(TAG, "Camera is not available, in use or does not exist");
        }

        return camera;
    }
}
