package com.sea.dao;

import com.sea.bean.Test;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestMapper  extends MyMapper<Test> {
}
