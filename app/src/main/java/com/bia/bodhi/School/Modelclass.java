package com.bia.bodhi.School;

import android.widget.ImageView;

public class Modelclass {
    private String content_of_notice="";
    private String datetime_of_notice="";
    private String img_of_notice="";
    private boolean b;
    //student part
    private boolean msgRead;
    private ImageView new_message_icon;

    public void setContent_of_notice(String content_of_notice) { this.content_of_notice = content_of_notice;
    }

    public String getContent_of_notice() {
        return content_of_notice;
    }


    public void setDatetime_of_notice(String datetime_of_notice) { this.datetime_of_notice = datetime_of_notice;
    }

    public String getDatetime_of_notice() {
        return datetime_of_notice;
    }

    public void setImg_of_notice(String img_of_notice) { this.img_of_notice = img_of_notice;
    }

    public String getImg_of_notice() {
        return img_of_notice;
    }

    public void setBoolImage(boolean b){this.b= b;}

    public boolean getBoolImage(){ return b;}
     //Student part
    public void setNew_message_icon(ImageView new_message_icon){this.new_message_icon= new_message_icon;}

    public ImageView getNew_message_icon(){ return new_message_icon;}

    public void setBoolMSgRead(boolean msgRead){this.msgRead= msgRead;}

    public boolean getBoolMsgRead(){ return msgRead;}


}
