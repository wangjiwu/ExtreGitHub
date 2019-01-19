/**
 * Copyright 2018 bejson.com
 */
package com.example.lenovo.httpapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Auto-generated: 2018-12-07 13:17:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class ForkResponse implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("fork")
    private String fork;

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getFork() {
        return fork;
    }


}
