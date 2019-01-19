/**
 * Copyright 2018 bejson.com
 */
package com.example.lenovo.httpapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auto-generated: 2018-12-07 13:17:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class Event implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("actor")
    private actor actors;

    @SerializedName("repo")
    private repo repos;

    @SerializedName("payload")
    private payload payloads;



    public class actor {


        @SerializedName("login")
        private String login;

        @SerializedName("avatar_url")
        private String url;

        actor(String url, String login ){
            this.url = url;
            this.login = login;

        }

        public String getType() {
            return type;
        }
        public String getUrl() {
            return url;
        }

        public String getLogin() {
            return login;
        }
    }

    public class repo {
        @SerializedName("name")
        private String name;

        repo(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public class Commits {
        @SerializedName("message")
        private String message;

        public String getMessage() {
            return message;
        }
    }

    public class payload {
        @SerializedName("action")
        private String action;

        @SerializedName("issue")
        private Issue issue;

        @SerializedName("commits")
        private ArrayList<Commits> commits;

        public Issue getIssue() {
            return issue;
        }

        public Commits getCommits() {
            return commits.get(0);
        }
    }


    Event(String type, String created_at,actor actors, repo repos, payload payloads){
       this.type = type;
       this.created_at = created_at;
       this.actors = actors;
       this.repos = repos;
       this.payloads = payloads;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public actor getActors() {
        return actors;
    }

    public payload getPayloads() {
        return payloads;
    }

    public repo getRepos() {
        return repos;
    }


}
