package com.sea.dao;

import com.sea.bean.Goods;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsMapper extends MyMapper<Goods> {
    List<Goods> searchLikeByTitleOrDescribe(@Param(value = "key") String key);
}
