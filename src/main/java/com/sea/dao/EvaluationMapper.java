package com.sea.dao;

import com.sea.bean.Evaluation;
import com.sea.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EvaluationMapper extends MyMapper<Evaluation> {
}
