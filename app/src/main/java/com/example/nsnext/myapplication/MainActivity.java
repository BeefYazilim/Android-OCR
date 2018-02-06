package com.example.nsnext.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends Activity {

    private Bitmap bitmap;
    private ImageView imageView;
    private Button buttonCamera;
    private Button buttonProcess;
    private TextView txt_result;

    final int CAMERA_CAPTURE = 1;
    final int GALLERY_CAPTURE = 2;
    private Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.buttonCamera = (Button) this.findViewById(R.id.btnCamera);
        this.buttonProcess = (Button) this.findViewById(R.id.btnProcess);
        this.txt_result = (TextView) this.findViewById(R.id.txt_result);

        buttonCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CAPTURE);
            }
        });

        buttonProcess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational())
                    Log.e("ERROR","Detector dependencies are not yet available");
                else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0;i<items.size();++i)
                    {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }
                    txt_result.setText(stringBuilder);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==this.RESULT_OK){

            if (requestCode==CAMERA_CAPTURE){
                picUri = data.getData();
                Bundle bundle=data.getExtras();
                bitmap=bundle.getParcelable("data");
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}