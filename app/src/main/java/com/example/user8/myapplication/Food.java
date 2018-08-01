package com.example.user8.myapplication;

import java.util.HashMap;
import java.util.Map;

public class Food
{
    public String uid,u_food,u_date,u_time,status,u_postdate,key,uid_request;
    User user;

    public Food(String uid, String u_food, String u_date, String u_time, String u_postdate, String status, String key, String uid_request)
    {
        this.uid = uid;
        this.u_food = u_food;
        this.u_date = u_date;
        this.u_time = u_time;
        this.u_postdate = u_postdate;
        this.status = status;
        this.key = key;
        this.uid_request = uid_request;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("u_food",u_food);
        result.put("u_time",u_time);
        result.put("u_date",u_date);
        result.put("u_postdate",u_postdate);
        result.put("status",status);
        result.put("key",key);
        result.put("uid_request",uid_request);

        return  result;
    }

    public String getUid_request() {
        return uid_request;
    }

    public void setUid_request(String uid_request) {
        this.uid_request = uid_request;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getU_food() {
        return u_food;
    }

    public void setU_food(String u_food) {
        this.u_food = u_food;
    }

    public String getU_date() {
        return u_date;
    }

    public void setU_date(String u_date) {
        this.u_date = u_date;
    }

    public String getU_time() {
        return u_time;
    }

    public void setU_time(String u_time) {
        this.u_time = u_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getU_postdate() {
        return u_postdate;
    }

    public void setU_postdate(String u_postdate) {
        this.u_postdate = u_postdate;
    }

    Food(){}

    public void setUser(User user) {
        this.user = user;
    }
}
