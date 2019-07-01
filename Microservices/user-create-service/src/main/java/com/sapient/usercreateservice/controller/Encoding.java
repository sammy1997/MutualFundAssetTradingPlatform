package com.sapient.usercreateservice.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoding {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String x(){
        return  encoder.encode("qwerty123");
    }
    public static void main(String args[]){
        System.out.println(new Encoding().x());
    }
}
