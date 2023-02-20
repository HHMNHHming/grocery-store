package com.gwm.po;

public class Order {
    private Integer id;
    private String orderNo;
    private Double totalPrice;
    private String userInfo;
    private String productInfo;

    public Order(Integer id, String orderNo, Double totalPrice, String userInfo, String productInfo) {
        this.id = id;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
        this.userInfo = userInfo;
        this.productInfo = productInfo;
    }

    public Order(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", totalPrice=" + totalPrice +
                ", userInfo='" + userInfo + '\'' +
                ", productInfo='" + productInfo + '\'' +
                '}';
    }
}
