package com.sea.dao;

import com.sea.bean.Chat;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChatMapper extends MyMapper<Chat> {
}
