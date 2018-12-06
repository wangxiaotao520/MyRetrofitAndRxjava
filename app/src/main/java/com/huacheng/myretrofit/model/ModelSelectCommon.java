package com.huacheng.myretrofit.model;


/**
 * 一个测试的demo bean
 * Description: 选择楼号 单元号....
 * created by wangxiaotao
 */
public class ModelSelectCommon  {
    //住宅类型
    String id;
    String type_name;
    //抄表类型
    String name;
    //楼号
    String buildsing_id;
    //单元号
    String unit;
    //房间号 //商铺号
    String code;
    //抄表费项价格
    String price;
    //层
    String floor;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildsing_id() {
        return buildsing_id;
    }

    public void setBuildsing_id(String buildsing_id) {
        this.buildsing_id = buildsing_id;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
