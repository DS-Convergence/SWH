package com.example.squirrelwarehouse;

import java.util.HashMap;

public class CategoryHobby {

    String product;
    HashMap<String,Integer> map = new HashMap<>();

    CategoryHobby(String product) {
        this.product = product;

        setDataInit();  // 데이터 초기화. 1000개의 데이터 매핑하는 함수
    }

    private void setDataInit() {

        // 공부 > 0
        map.put("overskirt",0);




        // 게임 > 1
        map.put("power drill",1);




        // 독서 > 2
        map.put("bakery",2);





        // 문화/예술 > 3
        map.put("balloon",3);





        // 미술/공예 > 4
        map.put("balance beam",4);





        // 요리 > 5
        map.put("plow",5);




        // 원예 > 6
        map.put("abacus",6);




        // 운동/스포츠 > 7
        map.put("slot",7);




        // 음악 > 8
        map.put("Old English sheepdog",8);


        // 펫 > 9
        map.put("Old English sheepdog",9);


        // 기타 > 10
        map.put("Old English sheepdog",10);




    }

    public int getCategory() {  // 카테고리 번호 반환 0~8

        return map.get(product)+1;

    }
}