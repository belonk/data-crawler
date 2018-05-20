package com.belonk.taobao.mapper;

import com.belonk.taobao.domain.Category;
import org.apache.ibatis.annotations.Param;

public interface CategoryDynamicMapper {
    int deleteByPrimaryKey(@Param("name") String name, @Param("id") Long id);

    int insert(@Param("name") String name, @Param("record") Category record);

    int insertSelective(@Param("name") String name, @Param("record") Category record);

    Category selectByPrimaryKey(@Param("name") String name, @Param("id") Long id);

    int updateByPrimaryKeySelective(@Param("name") String name, @Param("record") Category record);

    int updateByPrimaryKey(@Param("name") String name, @Param("record") Category record);

    void createTable(@Param("name") String name);

    void deleteTable(@Param("name") String name);
}