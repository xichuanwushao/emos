package com.xichuan.emos.mapper;

import com.xichuan.emos.domain.TB_User;
import com.xichuan.emos.domain.TB_UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TB_UserMapper {
    long countByExample(TB_UserExample example);

    int deleteByExample(TB_UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TB_User record);

    int insertSelective(TB_User record);

    List<TB_User> selectByExampleWithBLOBs(TB_UserExample example);

    List<TB_User> selectByExample(TB_UserExample example);

    TB_User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TB_User record, @Param("example") TB_UserExample example);

    int updateByExampleWithBLOBs(@Param("record") TB_User record, @Param("example") TB_UserExample example);

    int updateByExample(@Param("record") TB_User record, @Param("example") TB_UserExample example);

    int updateByPrimaryKeySelective(TB_User record);

    int updateByPrimaryKeyWithBLOBs(TB_User record);

    int updateByPrimaryKey(TB_User record);
}