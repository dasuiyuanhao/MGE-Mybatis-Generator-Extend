package com.coolead.service.impl;

import com.coolead.domain.Pet;
import com.coolead.domain.PetCriteria;
import com.coolead.mapper.PetMapper;
import com.coolead.service.PetService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetMapper petMapper;

    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    public int countByExample(PetCriteria example) {
        int count = this.petMapper.countByExample(example);
        logger.debug("count: {}", count);
        return count;
    }

    public Pet selectByPrimaryKey(Integer id) {
        return this.petMapper.selectByPrimaryKey(id);
    }

    public List<Pet> selectByExample(PetCriteria example) {
        return this.petMapper.selectByExample(example);
    }

    public int deleteByPrimaryKey(Integer id) {
        return this.petMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Pet record) {
        return this.petMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Pet record) {
        return this.petMapper.updateByPrimaryKey(record);
    }

    public int deleteByExample(PetCriteria example) {
        return this.petMapper.deleteByExample(example);
    }

    public int updateByExampleSelective(Pet record, PetCriteria example) {
        return this.petMapper.updateByExampleSelective(record, example);
    }

    public int updateByExample(Pet record, PetCriteria example) {
        return this.petMapper.updateByExample(record, example);
    }

    public int insert(Pet record) {
        return this.petMapper.insert(record);
    }

    public int insertSelective(Pet record) {
        return this.petMapper.insertSelective(record);
    }
}