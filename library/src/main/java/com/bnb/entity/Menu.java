package com.bnb.entity;

/**
 * 创建人：Administrator
 * 创建时间： 2016/7/18 0018 14:15
 * 功能概述:
 * 修改人：
 * 修改时间：
 */
public class Menu {

    private CharSequence name; //菜单图标的名字

    private int redId; //菜单图标的图片资源

    public Menu() {
    }

    public Menu(int redId, String name) {
        this.redId = redId;
        this.name = name;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public int getRedId() {
        return redId;
    }

    public void setRedId(int redId) {
        this.redId = redId;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name=" + name +
                ", redId=" + redId +
                '}';
    }
}
