package com.letstalk.rathero.vspc_redtide.test1;

import com.orm.SugarRecord;

import java.sql.Timestamp;
import java.util.Date;

public class Talk {
    public String Title;
    public String Description;
    public String Category;
    public String Id;
    public String Language;
    public String TimeStamp;
    public Talk(){

    }

    public Talk(String title, String description, String category, String language, String id){
        this.Title = title;
        this.Description = description;
        this.Category = category;
        this.Id = id;
        this.Language = language;
        Long tsLong = System.currentTimeMillis()/1000;
        this.TimeStamp = tsLong.toString();
    }
}
