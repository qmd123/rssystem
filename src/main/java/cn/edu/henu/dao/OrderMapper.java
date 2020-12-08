package cn.edu.henu.dao;

import cn.edu.henu.bean.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Qing_Y
 * @date 2020-11-27 17:01
 */
public interface OrderMapper extends CrudDao<Order> {
    /**
     * 查询对应id消费者的所有订单
     *
     * @param cid
     * @return
     */
    List<Order> selectByCid(String cid);

    /**
     * 查询对应id商家的所有订单
     *
     * @param bid
     * @return
     */
    List<Order> selectByBid(Integer bid);

    /**
     * 根据订单id更改订单状态
     *
     * @param orderId
     * @param state
     * @return
     */
    int updateStatusByOid(@Param("id") Integer orderId, @Param("status") Integer state);

    /**
     * 根据订单上的商品id批量更改订单状态
     *
     * @param pid
     * @param state
     * @return
     */
    int updateAllStatusByPid(@Param("pid") Integer pid, @Param("status") Integer state);

}
