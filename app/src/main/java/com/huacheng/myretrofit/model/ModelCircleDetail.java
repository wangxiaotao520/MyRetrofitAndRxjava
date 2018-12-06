package com.huacheng.myretrofit.model;

import java.util.List;

/**
 * Description:
 * created by wangxiaotao
 * 2018/12/5 0005 下午 4:45
 */
public class ModelCircleDetail {

    private String id;
    private String uid;
    private String admin_id;
    private String c_name;
    private String content;
    private String click;
    private String reply_num;
    private String addtime;
    private String avatars;
    private String nickname;

    private String img;
    private String img_size;


    private List<ModelCircleDetail> reply_list;
    private String social_id;
    private String r_nickname;
    private String r_avatars;

    private String social_num;
    private String is_observe;

    private int type = 0;//0是评论1是删除

    public String getIs_observe() {
        return is_observe;
    }

    public void setIs_observe(String is_observe) {
        this.is_observe = is_observe;
    }

    public String getSocial_num() {
        return social_num;
    }

    public void setSocial_num(String social_num) {
        this.social_num = social_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getReply_num() {
        return reply_num;
    }

    public void setReply_num(String reply_num) {
        this.reply_num = reply_num;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg_size() {
        return img_size;
    }

    public void setImg_size(String img_size) {
        this.img_size = img_size;
    }


    public List<ModelCircleDetail> getReply_list() {
        return reply_list;
    }

    public void setReply_list(List<ModelCircleDetail> reply_list) {
        this.reply_list = reply_list;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getR_nickname() {
        return r_nickname;
    }

    public void setR_nickname(String r_nickname) {
        this.r_nickname = r_nickname;
    }

    public String getR_avatars() {
        return r_avatars;
    }

    public void setR_avatars(String r_avatars) {
        this.r_avatars = r_avatars;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
