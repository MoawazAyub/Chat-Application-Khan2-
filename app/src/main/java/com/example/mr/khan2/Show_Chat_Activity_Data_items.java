package com.example.mr.khan2;

/**
 * Created by MR on 11/29/2017.
 */

public class Show_Chat_Activity_Data_items {

    private String Status;
    private String Image_Url;
    private String Name;
    private String Phone;
    private String Thumb;

    public Show_Chat_Activity_Data_items()
    {
    }

    public Show_Chat_Activity_Data_items(String status, String image_Url, String name, String thumb) {
        Status = status;
        Image_Url = image_Url;
        Name = name;
        Thumb = thumb;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String name) {
        Phone = name;
    }

    public String getThumb() {
        return Thumb;
    }

    public void setThumb(String name) {
        Thumb = name;
    }


}
