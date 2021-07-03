package com.htphong.mylife.Utils;

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
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Constant {
    //    public static final String URL = "https://hp-blog.herokuapp.com";
    public static final String URL = "http://192.168.152.52/blog/public";
    // home
     public static final String DOMAIN = "http://192.168.152.52/blog/public/";
//    public static final String DOMAIN = "http://192.168.1.8/blog/public/";

//    public static final String HOME = URL + "/api";

    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "AAAALr0WyN4:APA91bGR8iYRFETKkChKMeyxVVOPNJ5OpWutL882wqzd9diUzZ2oIAtW7WyE5PycDuqII7-dp7XetekjVUwouTdqy-cohcaJxn_YPoiLCJwxO2xnkjaEN1ba2gXEk_ySd_H6RYy_FD_4");
        headers.put("Content-type", "application/json");
        return headers;
    }

}
