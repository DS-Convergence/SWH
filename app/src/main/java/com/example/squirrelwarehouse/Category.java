package com.example.squirrelwarehouse;

import java.util.HashMap;

public class Category {

    String product;
    HashMap<String,Integer> map = new HashMap<>();

    Category(String product) {
        this.product = product;

        setDataInit(); // 데이터 초기화. 1000개의 데이터 매핑하는 함수
    }

    private void setDataInit() {
        // 디지털 가전 > 0
        map.put("cellular telephone",0);




        // 생활/인테리어 > 1
        map.put("analog clock",1);




        // 스포츠/레저 > 2
        map.put("balance beam",2);




        // 패션/뷰티/미용 > 3
        map.put("puffer",3);




        // 문구/완구 > 4
        map.put("Old English sheepdog",4);




        // 도서 > 5
        map.put("comic book",5);


        // 음악 > 6
        map.put("accordion",6);




        // 기타 > 7
        map.put("apiary",7);




    }

    public int getCategory() {  // 카테고리 번호 반환 0~7

        return map.get(product)+1;

    }

}