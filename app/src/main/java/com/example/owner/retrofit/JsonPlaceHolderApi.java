package com.example.owner.retrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    // 設置一個GET連線，路徑為posts
    @GET("posts")                       //https://jsonplaceholder.typicode.com/posts 因為網址最後一段為posts
    Call<List<Post>> getPosts(
            @Query("userId") int userId,                //亦可 Integer[] userId  , to call: new Integer[x,x,x]
            @Query("_sort") String sort,                //https://jsonplaceholder.typicode.com/comments?userId=1&_sort=id&_order=desc
            @Query("_order") String order
    );        // 取得的回傳資料用Post物件接收，連線名稱取為getPosts

    @GET("posts")                       //https://jsonplaceholder.typicode.com/posts 因為網址最後一段為posts
    Call<List<Post>> getPosts(@QueryMap Map<String,String> parameters);

    @GET("posts/{id}/comments")         // 用{}表示路徑參數，@Path會將參數帶入至該位置
    Call<List<Comment>> getComment(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComment(@Url String url);

    @POST("posts")                      // 用@Body表示要傳送Body資料
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded                     // to call  Call<Post> call = jsonPlaceHolderApi.createPost(23,"標題","文字");
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );
    @FormUrlEncoded                     // to call  Map<String,String> fields = new HashMap<>(); fileds.put("key","value")
    @POST("posts")                       //Call<Post> call = jsonPlaceHolderApi.createPost(fileds);
    Call<List<Post>>createPost(@FieldMap Map<String,String> fields);

    @Headers({"Static-Header1: 123","Static-Header2: 456"})
    //PUT請求將替換整個對象。另一方面，PATCH請求僅發送更改的屬性，而忽略所有保持不變的內容
    @PUT("posts/{id}")
    Call<Post> putPost (@Header ("Dynamic-Header") String header,
                        @Path("id") int id,
                        @Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost(@HeaderMap Map<String,String> headers,
                         @Path("id") int id,
                         @Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}