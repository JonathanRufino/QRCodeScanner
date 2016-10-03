package rufino.jonathan.qrcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int EMPTY = 0;
    private static final int CAMERA_WIDTH = 300;
    private static final int CAMERA_HEIGTH = 300;

    private BarcodeDetector detector;
    private CameraSource cameraSource;
    private Context context;

    private SurfaceView cameraView;
    private TextView qrcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        qrcodeData = (TextView) findViewById(R.id.qrcode_data);

        detector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(context, detector).setRequestedPreviewSize(CAMERA_WIDTH, CAMERA_HEIGTH).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size() != EMPTY) {
                    qrcodeData.post(new Runnable() {
                        @Override
                        public void run() {
                            qrcodeData.setText(barcodes.valueAt(0).rawValue);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cameraSource.release();
        detector.release();
    }
}
