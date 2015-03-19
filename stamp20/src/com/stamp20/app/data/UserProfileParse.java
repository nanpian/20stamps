package com.stamp20.app.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UserProfileParse")
public class UserProfileParse extends ParseObject{

    public UserProfileParse() {
    }
    
    public String getUserProfileParseId() {
        return getObjectId();
    }
}
