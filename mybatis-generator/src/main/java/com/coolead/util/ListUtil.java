package com.coolead.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ListUtil {
	private static final Logger logger = LoggerFactory.getLogger(ListUtil.class);

	public static <T> List<List<T>> createList(List<T> targe, int size) {
		List<List<T>> listArr = new ArrayList<>();
		//获取被拆分的数组个数  
		int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
		for (int i = 0; i < arrSize; i++) {
			List<T> sub = new ArrayList<>();
			//把指定索引数据放入到list中  
			for (int j = i * size; j <= size * (i + 1) - 1; j++) {
				if (j <= targe.size() - 1) {
					sub.add(targe.get(j));
				}
			}
			listArr.add(sub);
		}
		return listArr;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> deepCopyList(List<T> src) {
		List<T> dest = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			dest = (List<T>) in.readObject();
		} catch (IOException e) {
			logger.error("IOException",e);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException",e);
		}
		return dest;
	}
}
