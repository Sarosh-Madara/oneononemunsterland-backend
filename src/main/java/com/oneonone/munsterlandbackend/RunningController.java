package com.oneonone.munsterlandbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
public class RunningController {

    @GetMapping("/ping")
    public  String ping() {
        return "Its up and running.....";
    }

    @GetMapping("/")
    public String hello() {
        return "check /ping path for operations";
    }

    // @GetMapping("listData")
    // public List<String> getDataList(){
    //     return TestData()
    // }


    // @AllArgsConstructor
    // class TestData{
    //     String name;
        
    //     public void setName(String name) {
    //         this.name = name;
    //     }
    //     public String getName() {
    //         return name;
    //     }

    // }
}
