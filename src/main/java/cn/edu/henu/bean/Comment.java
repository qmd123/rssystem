package cn.edu.henu.bean;

import java.io.Serializable;

/**
 * @author Qing_Y
 * @date 2020-12-16 13:33
 */
public class Comment implements Serializable {
    /**
     * 评论id
     */
    private Integer id;
    /**
     * 评论用户id
     */
    private Integer cid;
    /**
     * 商家id
     */
    private Integer bid;
    /**
     * 评论餐品id
     */
    private Integer pid;
    /**
     * 评分
     */
    private Integer grade;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 消费者
     */
    private Consumer consumer;
    /**
     * 商品
     */
    private Product product;
    /**
     * 商家
     */
    private Business business;

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", cid=" + cid +
                ", bid=" + bid +
                ", pid=" + pid +
                ", grade=" + grade +
                ", comment='" + comment + '\'' +
                ", consumer=" + consumer +
                ", product=" + product +
                ", business=" + business +
                '}';
    }
}
