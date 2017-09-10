package com.coolead.util;


import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/*
	 * 去掉指定字符串的开头和结尾的指定字符
	 * 
	 * @param stream 要处理的字符串
	 * 
	 * @param trimstr 要去掉的字符串
	 * 
	 * @return 处理后的字符串
	 */
	public static String sideTrim(String stream, String trimstr) {
		// null或者空字符串的时候不处理
		if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
			return stream;
		}

		// 结束位置
		int epos = 0;

		// 正规表达式
		String regpattern = "[" + trimstr + "]*+";
		Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);

		// 去掉结尾的指定字符
		StringBuffer buffer = new StringBuffer(stream).reverse();
		Matcher matcher = pattern.matcher(buffer);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
		}

		// 去掉开头的指定字符
		matcher = pattern.matcher(stream);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = stream.substring(epos);
		}

		// 返回处理后的字符串
		return stream;
	}

	/*
	 * 去掉指定字符串的开头和结尾的指定字符
	 * 
	 * @param stream 要处理的字符串
	 * 
	 * @param trimstr 要去掉的字符串
	 * 
	 * @return 处理后的字符串
	 */
	public static String sideTrimStrat(String stream, String trimstr) {
		// null或者空字符串的时候不处理
		if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
			return stream;
		}

		// 结束位置
		int epos = 0;

		// 正规表达式
		String regpattern = "[" + trimstr + "]*+";
		Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);

		// 去掉开头的指定字符
		StringBuffer buffer = new StringBuffer(stream);
		Matcher matcher = pattern.matcher(buffer);
		if (matcher.lookingAt()) {
			epos = matcher.end();
			stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
		}

		// 返回处理后的字符串
		return stream;
	}

	public static boolean IsEmptyOrNull(String str) {
		if (str == null)
			return true;
		if (str.length() <= 0)
			return true;
		if (str.isEmpty())
			return true;
		return false;
	}

	/**
	 * 加密
	 *
	 * @return
	 */
	public static String Base64UTF8Encode(String str) {
		try {
			byte[] bytes = str.getBytes("utf-8");
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 解密
	 *
	 * @return
	 */
	public static String Base64TextUTF8Decode(String str) {
		try {
			byte[] bytes = Base64.getDecoder().decode(str);
			return new String(bytes, "utf-8");
		} catch (Exception ex) {
			return "";
		}
	}

	 /** 
	 * @description: 不为空，不为0
	 * @param str
	 * @return
	 * @author: ltf
	 * @date: 2016年4月21日 下午2:03:11 
	 */	
	public static boolean isNotBlankAndZero(String str) {
		if (StringUtils.isNotBlank(str) && !"0".equals(str.trim()))
			return true;
		return false;
	}
	
	/**
	 * 校验是否是数字
	 * 正整数 : true
	 * 0 : true
	 * null : false
	 * 空 : false
	 * 负数 : false
	 * 小数 : false
	 */
	public static boolean isNumber(String string){
		if(null == string || string.equals("")){
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(string).matches();
	}
	
	/**
	 * @author Jaron Li
	 * @description 将含有，分隔的字符串转换成list。
	 * @注 转译符分隔不能正确解析
	 * 如    a,b,c  分隔为   a b c放入list中
	 * 
	 */
	public static List<String> parseStringToList(String str,String symbol){
		//参数为null
		if(str==null){
			return null;
		}
		
		List <String> parseResult = Arrays.asList(str.split(symbol));
		return parseResult;
	}
	
	/**
	 * 时间字符串 是否左值小于右值
	 * @author Jaron Li
	 * @param leftTime
	 * @param rightTime
	 * @return
	 */
	public static boolean isValidTimeScope(String leftTime,String rightTime){
		boolean result = true;
		Date leftDate = null;
		Date rightDate = null;
		SimpleDateFormat format = new SimpleDateFormat(DateFormartUtil.YYYY_MM_DD_HH_mm_ss);
		//参数完整性检验
		if(leftTime==null||rightTime==null){
			return false;
		}
		
		//检查传入参数的格式
		try{
			format.setLenient(false);
			leftDate = format.parse(leftTime);
			rightDate = format.parse(rightTime);
		}catch(ParseException e){
			result = false;
		}
		
		//检查是否左值比右值小
		if(result){
			if(!leftDate.before(rightDate)){
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * @author Jaron Li
	 * 先确定参数是时间格式
	 * @param date
	 * @return
	 */
	public static Date parseStringToDate(String date){
		SimpleDateFormat format = new SimpleDateFormat(DateFormartUtil.YYYY_MM_DD_HH_mm_ss);
		Date resultDate = null;
		boolean isValidDate = true;
		//参数完整性检验
		if(date==null){
			return null;
		}
		
		//检查传入参数的格式
		try{
			format.setLenient(false);
			resultDate = format.parse(date);
		}catch(ParseException e){
			isValidDate = false;
		}
		
		//传入的字符串不是正确的时间格式
		if(!isValidDate){
			resultDate = null;
		}
		
		return resultDate;
	}

	/**
	 * 把字符串转换为Long型的list
	 * @param str
	 * @param symbol
	 * @return
	 */
	public static List<Long> parseStringToListInLong(String str,String symbol) throws Exception {
		//参数为null
		if(str==null||str.equals("")){
			return null;
		}
				
		List <String> strList = Arrays.asList(str.split(symbol));
		List<Long> parseResult = new ArrayList<Long>();
		for(String s:strList){
			if(!isNumber(s)){
				throw new Exception("非数字转换失败");
			}
			parseResult.add(Long.parseLong(s));
		}
		return parseResult;
	}
	/**
	 * 把字符串转换为Integer型的list
	 * @param str
	 * @param symbol
	 */
	public static List<Integer> parseStringToListInInteger(String str,String symbol) throws Exception {
		//参数为null
		if(str==null||str.equals("")){
			return null;
		}
				
		List <String> strList = Arrays.asList(str.split(symbol));
		List<Integer> parseResult = new ArrayList<Integer>();
		for(String s:strList){
			if(!isNumber(s)){
				throw new Exception("非数字转换失败");
			}
			parseResult.add(Integer.parseInt(s));
		}
		return parseResult;
	}
	
	/**
	 * 
	 * @author HanWey
	 * @Description: 验证手机号是否合法。
	 * @param mobile
	 * @return
	 * boolean
	 * @throws
	 */
	public static boolean isValidMobile(String mobile){
		if(mobile==null){
			return false;
		}
		Pattern p=null;
		Matcher m=null;
		p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$");
		m=p.matcher(mobile);
		return m.matches();
	}
	
	/**
	 * 
	 * 功能描述	首字母大写
	 * @param target
	 * @return
	 * 创建作者	caizhiqin
	 * 创建时间	2016年9月2日 上午11:57:28
	 */
	public static String firstCharToUpperCase(String target) {
		char[] cs = target.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}
	
	/**
	 * 将list转string
	 * @param ids
	 * @return
	 */
	public static String parsListToString(List<Long> ids){
		if (ids.size() == 0){
			return null;
		}

		String idString = "";
		for (int i = 0; i < ids.size(); i++) {
			if (i == 0){
				idString += ids.get(i).toString();
			}else{
				idString += ",";
				idString += ids.get(i).toString();
			}
		}

		return idString;
	}

	/**
	 * 将list转string
	 * @return
	 */
	public static String parsStringListToString(List<String> source){
		if (source == null || source.size() == 0){
			return null;
		}

		StringBuffer resultString = new StringBuffer();
		for (int i = 0; i < source.size(); i++) {
			if (i == 0){
				resultString.append(source.get(i));
			}else{
				resultString.append(",").append(source.get(i));
			}
		}

		return resultString.toString();
	}
	/**
	 * 将字符串中的"<"和">"进行转义
	 * @param source
	 * @return
	 */
	public static String stringEscape(String source) {
		if (source == null||source.equals("")) {
			return source;
		}
		String result = "";
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
				case '<':
					buffer.append("&lt;");
					break;
				case '>':
					buffer.append("&gt;");
					break;
				default:
					buffer.append(c);
			}
		}
		result = buffer.toString();
		return result;
	}

	/**
	 * 判断字符串中是否存在某个字符串
	 * @param source
	 * @param sub
	 * @return
	 */
	public static boolean isExistSubStr(String source, String sub) {
		if (IsEmptyOrNull(source) || IsEmptyOrNull(sub)) {
			return false;
		}
		if(source.indexOf(sub) > -1){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取截断后的字符串
	 * @param data
	 * @param subLength
     * @return
     */
	public static String substringComfortable(String data,int subLength){
		if(data==null)
			return StringUtils.EMPTY;
		if(data.length()<=subLength){
			return data;
		}
		return data.substring(0,subLength);
	}

}