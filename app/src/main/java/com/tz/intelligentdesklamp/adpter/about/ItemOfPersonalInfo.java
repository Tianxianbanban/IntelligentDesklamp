package com.tz.intelligentdesklamp.adpter.about;

/**
 * 个人信息item对应的内容
 */

public class ItemOfPersonalInfo {
    private String item;
    private String content;
    private int imageId;
    public ItemOfPersonalInfo(String item,String content,int imageId){
        this.item=item;
        this.content=content;
        this.imageId=imageId;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public int getImageId() {
        return imageId;
    }
}
