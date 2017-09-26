package com.letstalk.rathero.vspc_redtide.test1;



public class FragmentHelper {
    public static TalksList talksList;

    public static TalksList GetTalksList(){
        if(talksList == null) talksList = new TalksList();
        return talksList;
    }

}
