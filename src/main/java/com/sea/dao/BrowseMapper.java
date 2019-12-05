package com.sea.dao;

import com.sea.bean.Browse;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BrowseMapper extends MyMapper<Browse> {

}
