package com.htphong.mylife;

import android.content.Context;
import android.util.Log;

import com.htphong.mylife.API.Client;
import com.htphong.mylife.API.PostService;
import com.htphong.mylife.Models.Post;
import com.htphong.mylife.POJO.PostPOJO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Constant {
    //    public static final String URL = "https://hp-blog.herokuapp.com";
    public static final String URL = "http://192.168.1.180/blog/public";
    public static final String DOMAIN = "http://192.168.1.180/blog/public/";
    public static final String HOME = URL + "/api";

    public static String convertDateTime(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String convertTime(String pattern, String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat(pattern);
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static ArrayList<Post> getPost(String user_id, Context context) {
        ArrayList<Post> list = new ArrayList<>();
        Retrofit retrofit = new Client().getRetrofit(context);;

        PostService postService = retrofit.create(PostService.class);

        Call<PostPOJO> call = postService.getPost(user_id);
        call.enqueue(new Callback<PostPOJO>() {
            @Override
            public void onResponse(Call<PostPOJO> call, Response<PostPOJO> response) {
                if(response.isSuccessful()) {
                    List<Post> post = response.body().getPosts();
                    for(int i = 0; i < post.size(); i++) {
                        list.add(post.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<PostPOJO> call, Throwable t) {
                Log.d("HomeFragment: ", t.getMessage());
            }
        });

        return list;
    }

    public static String timeDifferent(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "trước";

        try {
            dataDate = changeTimeZone(dataDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();
            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if(second < 10) {
                return "Vừa xong";
            }
            else if (second < 60) {
                convTime = second + " giây " + suffix;
            } else if (minute < 60) {
                convTime = minute + " phút "+suffix;
            } else if (hour < 24) {
                convTime = hour + " giờ "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " năm " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " tháng " + suffix;
                } else {
                    convTime = (day / 7) + " tuần " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" ngày "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    private static String changeTimeZone(String time) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        oldFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        newFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            String reformattedStr = newFormat.format(oldFormat.parse(time));
            return reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

}
