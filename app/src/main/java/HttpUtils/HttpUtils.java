package HttpUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;

public class HttpUtils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public void post(String url, String json, Handler handler) throws IOException {//post请求，返回String类型
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")//添加头部
//                .addHeader("Authorization",sn + " " + sign)
                .url(url)
                .post(body)
                .build();
        Log.d("InHttpUtils", "Post:" + json);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = handler.obtainMessage();
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }
}