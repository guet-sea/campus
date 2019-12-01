package com.sea.dao;

import com.sea.bean.Address;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressMapper extends MyMapper<Address> {
    Address queryDefaultAddress(String userName);
}
