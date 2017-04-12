package cn.sugar.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CartBean implements Serializable {

    /**
     * id : 35
     * userName : a952702
     * goodsId : 7677
     * goods : null
     * count : 2
     * isChecked : false
     * checked : false
     */

    private int id;
    private String userName;
    private int goodsId;
    private GoodsDetailsBean goods;
    private int count;
    private boolean isChecked;
    private boolean checked;

    public CartBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public GoodsDetailsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsDetailsBean goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", goodsId=" + goodsId +
                ", count=" + count +
                ", isChecked=" + isChecked +
                ", checked=" + checked +
                '}';
    }
}
