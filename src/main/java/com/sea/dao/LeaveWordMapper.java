package com.sea.dao;

import com.sea.bean.LeaveWord;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LeaveWordMapper extends MyMapper<LeaveWord> {
    List<LeaveWord> enquiryGoodsLeaveWord(Integer goodsId);
}
