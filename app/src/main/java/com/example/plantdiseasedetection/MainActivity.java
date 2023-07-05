package com.example.plantdiseasedetection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;




public class MainActivity extends AppCompatActivity implements GetResposnse.getResponseInterface {
    private static final String TAG = "MainActivity";
    private ImageView imageView;
//    private Button selectButton;
    private Button predictButton;
//    private TextView class_ ;
//    private TextView confidence_;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        imageView = (ImageView) findViewById(R.id.plant_image);
//        selectButton = (Button) findViewById(R.id.Select_button);
        predictButton = (Button) findViewById(R.id.Predict_button);
//        class_ = (TextView) findViewById(R.id.predicted_class);
//        confidence_ = (TextView) findViewById(R.id.Confidence_value);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(MainActivity.this)
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        predictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetResposnse getResposnse = new GetResposnse("https://us-central1-minor-project-crop-disease-mod.cloudfunctions.net/predict", path,MainActivity.this);
                getResposnse.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(TAG, "onActivityResult: URI IS " + uri.toString());
            Context context = MainActivity.this;

            path = RealPathUtil.getRealPath(context, uri);
            Log.d(TAG, "onActivityResult: is isMediaDOcument " + RealPathUtil.isMediaDocument(uri));
            Log.d(TAG, "onActivityResult: file path is  " + path);
//            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
            imageView.setImageURI(uri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Ondownload(IMAGE img, downloadStatus status) {

        if(status == downloadStatus.OK){
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
            intent.putExtra("class",img.getClass_name());
            intent.putExtra("confidence",img.getConfidence());
            Log.d(TAG, "Ondownload: "+img.getClass_name()+" "+ img.getConfidence());
            startActivity(intent);
        } else{
                Toast.makeText(this, img.getError(),Toast.LENGTH_SHORT).show();
            }
//            if(status == downloadStatus.OK){
//                class_.setText(img.getClass_name());
//                confidence_.setText(img.getConfidence());
    }

}