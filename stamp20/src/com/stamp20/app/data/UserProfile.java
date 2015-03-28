package com.stamp20.app.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UserProfile")
public class UserProfile extends ParseObject{

    public UserProfile() {
    }
    
    public String getUserProfileParseId() {
        return getObjectId();
    }
}
