package com.bia.bodhinew.School;

import android.widget.ImageView;

public class Modelclass {
    //school part
    private String student_name="";
    private String student_icon="";
    private String content_of_notice="";
    private String datetime_of_notice="";
    private String img_of_notice="";
    private boolean b;
    private String id;
    //student part
    private boolean msgRead;
    private ImageView new_message_icon;
    //previously watched
    private String file_name = "";
    private String file_description = "";
    private String subject_name = "";

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

    public void setStudent_name(String student_name) { this.student_name = student_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_icon(String student_icon) { this.student_icon = student_icon;
    }

    public String getStudent_icon() {
        return student_icon;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

     //Student part
    public void setNew_message_icon(ImageView new_message_icon){this.new_message_icon= new_message_icon;}

    public ImageView getNew_message_icon(){ return new_message_icon;}

    public void setBoolMSgRead(boolean msgRead){this.msgRead= msgRead;}

    public boolean getBoolMsgRead(){ return msgRead;}

    //previously watched

    public void setFile_name(String file_name) { this.file_name = file_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_description(String file_description) { this.file_description = file_description;
    }

    public String getFile_description() {
        return file_description;
    }


    public void setSubject_name(String subject_name) { this.subject_name = subject_name;
    }

    public String getSubject_name() {
        return subject_name;
    }


}
