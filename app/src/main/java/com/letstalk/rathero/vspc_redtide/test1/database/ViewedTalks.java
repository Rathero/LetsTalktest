package com.letstalk.rathero.vspc_redtide.test1.database;

import com.orm.SugarRecord;


public class ViewedTalks extends SugarRecord<SubscribedTalk> {
    public String TalkId;
    public Long LastReview;

    public ViewedTalks(){
        super();
    }

    public ViewedTalks(String id){
        this.TalkId = id;
        this.SetReviewed();
    }
    public void SetReviewed(){
        Long tsLong = System.currentTimeMillis();
        this.LastReview = Long.valueOf("-" + tsLong.toString());
    }
}
