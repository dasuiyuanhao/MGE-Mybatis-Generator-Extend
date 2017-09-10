package com.coolead.mapper;

import com.coolead.domain.Pet;
import com.coolead.domain.PetCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PetMapper {
    int countByExample(PetCriteria example);

    int deleteByExample(PetCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Pet record);

    int insertSelective(Pet record);

    List<Pet> selectByExample(PetCriteria example);

    Pet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Pet record, @Param("example") PetCriteria example);

    int updateByExample(@Param("record") Pet record, @Param("example") PetCriteria example);

    int updateByPrimaryKeySelective(Pet record);

    int updateByPrimaryKey(Pet record);

    List<Pet> selectPage(PetCriteria example);
}