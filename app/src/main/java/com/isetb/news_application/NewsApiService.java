package com.isetb.news_application;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("top-headlines")
    Call<News> getTopHeadlines(
            @Query("apiKey") String apiKey,
            @Query("country") String country,
            @Query("category") String category,
            @Header("User-Agent") String userAgent
    );


}
