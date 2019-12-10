package com.sea.dao;

import com.sea.bean.GoodOrder;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper extends MyMapper<GoodOrder> {
    List<GoodOrder> getMyBuyOrderListWithPic(Integer buyerid);
    List<GoodOrder> getMySellOrderListWithPic(Integer sellerid);
}
