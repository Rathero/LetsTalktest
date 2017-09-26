package com.letstalk.rathero.vspc_redtide.test1.database;

import com.orm.SugarRecord;


public class SubscribedTalk  extends SugarRecord<SubscribedTalk> {
    public String TalkId;

    public SubscribedTalk(){
        super();
    }

    public SubscribedTalk(String id){
        this.TalkId = id;
    }
}
