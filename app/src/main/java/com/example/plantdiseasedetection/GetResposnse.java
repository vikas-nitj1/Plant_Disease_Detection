package com.example.plantdiseasedetection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

enum downloadStatus { OK , Failed };
public class GetResposnse extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetResposnse";
    private final String url ;
    private String path ;
    private getResponseInterface mcallback;
    public downloadStatus mdownloadStatus;

    private final OkHttpClient client = new OkHttpClient();

    private  final String IMGUR_CLIENT_ID = "...";
    private  final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    public interface getResponseInterface{
        void Ondownload(IMAGE img , downloadStatus status);
    }

    public GetResposnse(String url, String path, getResponseInterface callback){
        this.url = url;
        this.path = path;
        this.mcallback = callback;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(mdownloadStatus == downloadStatus.OK){
            try{
                Log.d(TAG, "onPostExecute: s is "+ s);
                JSONObject jsonData = new JSONObject(s);
                String clas_name = jsonData.getString("class");
                String confidence = jsonData.getString("confidence");
                IMAGE image_object = new IMAGE(clas_name , confidence);

                Log.d(TAG, "onPostExecute: values are "+ image_object.toString());
                mcallback.Ondownload(image_object,mdownloadStatus);


            }catch (JSONException e){
                Log.e(TAG, "onPostExecute: error due to json exception"+ e.getMessage() );
            }
        }
        else{
            Log.e(TAG, "onPostExecute: download status error" );
            mcallback.Ondownload( new IMAGE(s), mdownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        try{
            String data = run();
            Log.d(TAG, "doInBackground:  data is "+ data);
            return data;
        }catch(Exception e){
            mdownloadStatus = downloadStatus.Failed;
            Log.e(TAG, "doInBackground: in run function error "+ e.getMessage());
            return e.getMessage();
        }
//        return null;
    }

    public String run() throws Exception {
// Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        File sourceFile = new File(path);
        String result = "No data is stored here";
        RequestBody requestBody = new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart(
//                            Headers.of("Content-Disposition", "form-data; name=\"file\""),
                        "file", sourceFile.getName(),RequestBody.create(MEDIA_TYPE_JPG,sourceFile)
                )
                .build();

        Request request = new Request.Builder()
//                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        try{
            result = response.body().string();
            Log.d(TAG, "run:  Final response output : "+ result);
//            Log.d(TAG, "run:  result " + result);
            mdownloadStatus = downloadStatus.OK;
            return result;

        }catch (NullPointerException e){
            mdownloadStatus = downloadStatus.Failed;
            Log.e(TAG, "run:  null pointer exception due to toString method :: " + e.getMessage() );
        }
        return  result;
    }
}
