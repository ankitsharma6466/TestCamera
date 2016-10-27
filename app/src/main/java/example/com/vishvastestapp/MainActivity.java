package example.com.vishvastestapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 99999;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient networkClient = new OkHttpClient();

    ImageView imagePreview;
    TextView responseView;

    Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePreview = (ImageView) findViewById(R.id.image_preview);
        responseView = (TextView) findViewById(R.id.response_text);
    }

    public void onTestClick(View v){
        if(capturedImage != null){
            post();
        }
    }

    public void onTrainClick(View v){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {

            capturedImage = (Bitmap) data.getExtras().get("data");

            //this is the bitmap object returned from camera, we need to convert this object into required format for network call

            imagePreview.setImageBitmap(capturedImage);
        }
    }

    public void post()  {

        //we are using OKHTTP for network calls, checkout http://square.github.io/okhttp/ for any help

        String json = "<----data string here---->";

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url("<-----your url here---->")
                .post(body)
                .build();
        networkClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //here is your success response
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //check response object here

                            responseView.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
}
