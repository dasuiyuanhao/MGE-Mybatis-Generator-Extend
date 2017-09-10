package com.coolead.util;


import com.coolead.common.ResultPagedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanConverter<T, R> {
	private static final Logger logger = LoggerFactory.getLogger(BeanConverter.class);

	public static <T, R> R convert(T source, Class<R> clazz) {
		R target = null;
		try {
			target = clazz.newInstance();
			BeanUtils.copyProperties(source, target);
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("bean转换失败", e);
		}
		return target;
	}

	public static <T, R> List<R> convertList(List<T> source, Class<R> clazz) {
		return source.stream().map(i -> convert(i, clazz)).collect(Collectors.toList());
	}

	public static <T, R> ResultPagedData<R> convertPagedData(ResultPagedData<T> resultPagedData, Class<R> clazz) {
		ResultPagedData<R> resp = new ResultPagedData<>();
		resp.setDatas(resultPagedData.getDatas().stream().map(i -> convert(i, clazz)).collect(Collectors.toList()));
		resp.setPageIndex(resultPagedData.getPageIndex());
		resp.setPageSize(resultPagedData.getPageSize());
		resp.setTotalCount(resultPagedData.getTotalCount());
		resp.setTotalPage(resultPagedData.getTotalPage());
		return resp;
	}

	//不做数据转换，手动转换
	public static <T, R> ResultPagedData<R> convertPagedDataWithoutData(ResultPagedData<T> resultPagedData, Class<R> clazz) {
		ResultPagedData<R> resp = new ResultPagedData<>();
		resp.setDatas(new ArrayList<>());
		resp.setPageIndex(resultPagedData.getPageIndex());
		resp.setPageSize(resultPagedData.getPageSize());
		resp.setTotalCount(resultPagedData.getTotalCount());
		resp.setTotalPage(resultPagedData.getTotalPage());
		return resp;
	}
}
