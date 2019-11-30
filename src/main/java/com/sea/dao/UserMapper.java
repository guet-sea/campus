package com.sea.dao;

import com.sea.bean.User;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends MyMapper<User> {

    List<User> queryUserList();
    int updateUserTel(User user);
    User queryUserById(int id);
}
