package com.heshanexample.etolling;

public class PostWiFi {
    private String first_name;
    private StringBuffer age;
    private String second_name;
    private int id;

    /*public Post(int id){
        this.id = id;
    }*/

    public PostWiFi(String first_name, StringBuffer age, String second_name) {
        this.first_name = first_name;
        this.age = age;
        this.second_name = second_name;
    }

    public PostWiFi(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public StringBuffer getAge() {
        return age;
    }

    public String getSecond_name() {
        return second_name;
    }
}
