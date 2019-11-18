package com.sea.dao;

import com.sea.bean.User;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@Component
public interface UserMapper extends MyMapper<User> {


}