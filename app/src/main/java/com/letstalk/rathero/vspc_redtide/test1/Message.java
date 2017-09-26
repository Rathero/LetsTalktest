package com.letstalk.rathero.vspc_redtide.test1;

import java.util.Date;

public class Message {

    public String Text;
    public String TalkId;
    public String Username;
    public Long TimeStamp;

    public Message(){
        Long tsLong = System.currentTimeMillis();
        this.TimeStamp = Long.valueOf("-" + tsLong.toString());
    }
}
