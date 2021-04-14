package com.example.owner.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private TextView text_result;
    private JsonPlaceHolderApi jsonPlaceHolderApi ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_result = findViewById(R.id.text_view_result);

        Gson gson = new GsonBuilder().serializeNulls().create();        //gson在序列化的過程會忽略null值 所以要序列化null值時需使用serializeNulls()方法

        HttpLoggingInterceptor  loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request request = originalRequest.newBuilder()
                                .header("Interceptor_Header","xyz")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        // 設置baseUrl即要連的網站，addConverterFactory用Gson作為資料處理Converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")              //API介面基地址的封裝物件，型別為HttpUrl
                .addConverterFactory(GsonConverterFactory.create(gson))             //對資料做序列化或反序列化處理
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getpost();
//       getcomment();
//        createPost();
//        updataPost();
//        deletPost();
    }

    private void deletPost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                text_result.setText("Code : " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                text_result.setText("111");
            }
        });
    }

    private void updataPost() {
        Post post = new Post(12,null,"new text");

        Map<String,String>headers = new HashMap<>();
        headers.put("Map-Header1","def");
        headers.put("Map-Header2","ghii");

        Call<Post> call = jsonPlaceHolderApi.patchPost(headers,5,post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    text_result.setText("Code : " + response.code());
                }
                Post postresponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n" ;
                content += "ID: " + postresponse.getId() + "\n" ;
                content += "User ID: " + postresponse.getUserId() + "\n";
                content += "Title: " + postresponse.getTitle() + "\n";
                content += "Text: " + postresponse.getText() + "\n\n";
                text_result.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                text_result.setText("404");
            }
        });
    }

    private void createPost(){
        // 建立要POST的物件
        Post post = new Post(23,"New Title","New Text");

        // 將物件作為createPost的參數
        Call<Post> call = jsonPlaceHolderApi.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    text_result.setText("Code : " + response.code());
                }
                Post postresponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n" ;
                content += "ID: " + postresponse.getId() + "\n" ;
                content += "User ID: " + postresponse.getUserId() + "\n";
                content += "Title: " + postresponse.getTitle() + "\n";
                content += "Text: " + postresponse.getText() + "\n\n";
                text_result.append(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void getcomment(){
        //建立連線的Call 此處設置call為jsonPlaceHolderApi中的getComment連線
        Call<List<Comment>> call = jsonPlaceHolderApi.getComment( "comments?postId=1");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if(!response.isSuccessful()){
                    text_result.setText("Code : " + response.code());
                }

                List<Comment> comments = response.body();

                for (Comment comment : comments){
                    String content = "";
                    content += "ID: " + comment.getId() + "\n" ;
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "name: " + comment.getName() + "\n";
                    content += "email: " + comment.getEmail() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";
                    text_result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                //連線失敗
                text_result.setText(t.getMessage());
            }
        });
    }

    private  void  getpost(){
        Map<String,String> parameters = new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

        //建立連線的Call 此處設置call為jsonPlaceHolderApi中的getPost()連線
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // 連線成功
                // 回傳的資料已轉成Post物件，可直接用get方法取得特定欄位
                if(!response.isSuccessful()){
                    text_result.setText("Code : " + response.code());
                }

                List<Post> posts = response.body();

                for (Post post : posts){
                    String content = "";
                    content += "ID: " + post.getId() + "\n" ;
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                    text_result.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                //連線失敗
                text_result.setText(t.getMessage());
            }
        });
    }
}
