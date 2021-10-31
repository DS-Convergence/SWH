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
        map.put("speaker",0);
        map.put("headset",0);
        map.put("earphone",0);
        map.put("keyboard",0);
        map.put("console game",0);
        map.put("portable video game",0);
        map.put("gaming mouse",0);
        map.put("monitor",0);
        map.put("charger",0);
        map.put("book scanner",0);
        map.put("printer",0);
        map.put("photo printer",0);
        map.put("camera",0);
        map.put("camera lens",0);
        map.put("tablet",0);
        map.put("blender",0);
        map.put("hand mixer",0);
        map.put("filming light",0);


        // 생활/인테리어 > 1
        map.put("gaming chair",1);
        map.put("reading table",1);
        map.put("stand",1);
        map.put("frame",1);
        map.put("fabric poster",1);
        map.put("tripod",1);
        map.put("mixing bowl",1);
        map.put("spatula",1);
        map.put("ratan basket",1);
        map.put("dripper",1);
        map.put("whisk",1);
        map.put("cookie cutter",1);
        map.put("cutting board",1);
        map.put("kitchen knife",1);
        map.put("kitchen scissors",1);
        map.put("knife sharpener",1);
        map.put("knife sharpener stick",1);
        map.put("analogue weighting scale",1);
        map.put("silicone mold",1);
        map.put("coffee kettle",1);
        map.put("kettle",1);
        map.put("water fountain",1);
        map.put("pet wheel",1);
        map.put("bird cage",1);
        map.put("automatic feeder",1);
        map.put("thimble",1);
        map.put("camera bag",1);
        map.put("watering can",1);
        map.put("shovel",1);
        map.put("homie",1);
        map.put("sickle",1);


        // 스포츠/레저 > 2
        map.put("bicycle",2);
        map.put("helmet",2);
        map.put("stick",2);
        map.put("skateboard",2);
        map.put("protector",2);
        map.put("fishinggrob",2);
        map.put("golfclub",2);
        map.put("snorkeling",2);
        map.put("shoes",2);
        map.put("quickboard",2);
        map.put("ski",2);
        map.put("inlineskate",2);
        map.put("skates",2);
        map.put("ball",2);


        // 패션/뷰티/미용 > 3
        map.put("apron",3);   // 고려필요
        map.put("resin lamp",3);
        map.put("sewing machine",3);


        // 문구/완구 > 4
        map.put("bookmark",4);
        map.put("magnifier",4);
        map.put("pencil case",4);
        map.put("stationery",4);
        map.put("opera glass",4);
        map.put("brush",4);
        map.put("paints",4);
        map.put("pallet",4);
        map.put("pencil",4);
        map.put("easel",4);
        map.put("cutting mat",4);
        map.put("gesso",4);
        map.put("utility knife",4);
        map.put("tape",4);
        map.put("carving knife",4);


        // 도서 > 5
        map.put("book",5);


        // 음악 > 6
        map.put("turntable",6);
        map.put("cd album",6);
        map.put("vinyl",6);
        map.put("violin",6);
        map.put("ukulele",6);


        // 기타 > 7
        

    }

    public int getCategory() {  // 카테고리 번호 반환 0~7

        return map.get(product)+1;

    }

}