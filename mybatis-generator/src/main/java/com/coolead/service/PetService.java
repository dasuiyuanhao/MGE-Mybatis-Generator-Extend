package com.coolead.service;

import com.coolead.domain.Pet;
import com.coolead.domain.PetCriteria;
import java.util.List;

public interface PetService {
    int countByExample(PetCriteria example);

    Pet selectByPrimaryKey(Integer id);

    List<Pet> selectByExample(PetCriteria example);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pet record);

    int updateByPrimaryKey(Pet record);

    int deleteByExample(PetCriteria example);

    int updateByExampleSelective(Pet record, PetCriteria example);

    int updateByExample(Pet record, PetCriteria example);

    int insert(Pet record);

    int insertSelective(Pet record);
}