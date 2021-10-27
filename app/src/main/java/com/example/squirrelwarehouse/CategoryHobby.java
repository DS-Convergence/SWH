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
        map.put("stationery",0);
        map.put("pencil case",0);
        map.put("printer",0);
        map.put("book scanner",0);


        // 게임 > 1
        map.put("keyboard",1);
        map.put("console game",1);
        map.put("portable video game",1);
        map.put("gaming chair",1);
        map.put("gaming mouse",1);
        map.put("monitor",1);
        map.put("charger",1);


        // 독서 > 2
        map.put("reading table",2);
        map.put("magnifier",2);
        map.put("book",2);
        map.put("bookmark",2);
        map.put("thimble",2);
        map.put("stand",2);


        // 문화/예술 > 3
        map.put("frame",3);
        map.put("opera glass",3);
        map.put("fabric poster",3);
        map.put("photo printer",3);
        map.put("printer",3);
        map.put("tripod",3);
        map.put("camera lens",3);
        map.put("camera bag",3);
        map.put("filming light",3);


        // 미술/공예 > 4
        map.put("brush",4);
        map.put("paints",4);
        map.put("pallet",4);
        map.put("easel",4);
        map.put("apron",4);
        map.put("cutting mat",4);
        map.put("tablet",4);
        map.put("gesso",4);
        map.put("pencil",4);
        map.put("utility knife",4);
        map.put("tape",4);
        map.put("carving knife",4);
        map.put("resin lamp",4);
        map.put("sewing machine",4);


        // 요리 > 5
        map.put("kettle",5);
        map.put("coffee kettle",5);
        map.put("hand mixer",5);
        map.put("weighting scale",5);
        map.put("dripper",5);
        map.put("spatula",5);
        map.put("cookie cutter",5);
        map.put("mixing bowl",5);
        map.put("blender",5);
        map.put("cutting board",5);
        map.put("kitchen knife",5);
        map.put("kitchen scissors",5);
        map.put("knife sharpener stick",5);
        map.put("whisk",5);
        map.put("silicone mold",5);
        map.put("knife sharpener",5);
        map.put("analogue weighting scale",5);


        // 원예 > 6
        map.put("shovel",6);
        map.put("ratan basket",6);
        map.put("homie",6);
        map.put("watering can",6);
        map.put("sickle",6);


        // 운동/스포츠 > 7
        map.put("bicycle",7);
        map.put("helmet",7);
        map.put("stick",7);
        map.put("skateboard",7);
        map.put("protector",7);
        map.put("fishinggrob",7);
        map.put("golfclub",7);
        map.put("snorkeling",7);
        map.put("shoes",7);
        map.put("quickboard",7);
        map.put("ski",7);
        map.put("inlineskate",7);
        map.put("skates",7);
        map.put("ball",7);


        // 음악 > 8
        map.put("turntable",8);
        map.put("cd album",8);
        map.put("speaker",8);
        map.put("headset",8);
        map.put("earphone",8);
        map.put("vinyl",8);
        map.put("violin",8);
        map.put("ukulele",8);


        // 펫 > 9
        map.put("water fountain",9);
        map.put("pet wheel",9);
        map.put("bird cage",9);
        map.put("automatic feeder",9);



        // 기타 > 10



    }

    public int getCategory() {  // 카테고리 번호 반환 0~8

        return map.get(product)+1;

    }
}