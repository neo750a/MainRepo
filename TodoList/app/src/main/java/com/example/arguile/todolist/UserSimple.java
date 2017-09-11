package com.example.arguile.todolist;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Arguile on 23/08/2017.
 */

public class UserSimple {
    @SerializedName("UserSimple")
    private UserSimple user[];
    private String name;
    private int age;

    public UserSimple()
    {
    }

    public UserSimple[] getUser() {
        return user;
    }

    public void setUser(UserSimple[] user) {
        this.user = user;
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

}
