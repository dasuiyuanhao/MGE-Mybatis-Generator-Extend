package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.PluginAdapter;

import java.util.ArrayList;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 纯service层
 * generate service class
 * 
 * @author Liyb
 * 
 * @date 2016-03-17 10:34:45
 */
public class MybatisServicePlugin extends PluginAdapter{

	private FullyQualifiedJavaType slf4jLogger;
	private FullyQualifiedJavaType slf4jLoggerFactory;
	private FullyQualifiedJavaType serviceType;
	private FullyQualifiedJavaType daoType;
	private FullyQualifiedJavaType interfaceType;
	private FullyQualifiedJavaType pojoType;
	private FullyQualifiedJavaType pojoCriteriaType;
	private FullyQualifiedJavaType listType;
	private FullyQualifiedJavaType autowired;
	private FullyQualifiedJavaType service;
	private FullyQualifiedJavaType returnType;
	private String servicePack;
	private String serviceImplPack;
	private String project;
	private String pojoUrl;
	/**
	 * 所有的方法
	 */
	private List<Method> methods;
	/**
	 * 是否添加注解
	 */
	private boolean enableAnnotation = true;
	private boolean enableInsert = true;
	private boolean enableInsertSelective = false;
	private boolean enableDeleteByPrimaryKey = false;
	private boolean enableDeleteByExample = false;
	private boolean enableUpdateByExample = false;
	private boolean enableUpdateByExampleSelective = false;
	private boolean enableUpdateByPrimaryKey = false;
	private boolean enableUpdateByPrimaryKeySelective = false;
	private boolean enablePaged = false;//是否分页

	public MybatisServicePlugin() {
		super();
		// default is slf4j
		slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
		slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
		methods = new ArrayList<Method>();
	}

	/**
	 * 读取配置文件
	 */
	@Override
	public boolean validate(List<String> warnings) {
		
		String enableAnnotation = properties.getProperty("enableAnnotation");
		
		String enableInsert = properties.getProperty("enableInsert");

		String enableUpdateByExampleSelective = properties.getProperty("enableUpdateByExampleSelective");

		String enableInsertSelective = properties.getProperty("enableInsertSelective");

		String enableUpdateByPrimaryKey = properties.getProperty("enableUpdateByPrimaryKey");

		String enableDeleteByPrimaryKey = properties.getProperty("enableDeleteByPrimaryKey");

		String enableDeleteByExample = properties.getProperty("enableDeleteByExample");

		String enableUpdateByPrimaryKeySelective = properties.getProperty("enableUpdateByPrimaryKeySelective");

		String enableUpdateByExample = properties.getProperty("enableUpdateByExample");

		String enablePaged=properties.getProperty("enablePaged");
		
		 
		if (StringUtility.stringHasValue(enableAnnotation)){
			this.enableAnnotation = StringUtility.isTrue(enableAnnotation);
		}
		
		if (StringUtility.stringHasValue(enableInsert)){
			this.enableInsert = StringUtility.isTrue(enableInsert);
		}
		
		if (StringUtility.stringHasValue(enableUpdateByExampleSelective)){
			this.enableUpdateByExampleSelective = StringUtility.isTrue(enableUpdateByExampleSelective);
		}
		
		if (StringUtility.stringHasValue(enableInsertSelective)){
			this.enableInsertSelective = StringUtility.isTrue(enableInsertSelective);
		}
		
		if (StringUtility.stringHasValue(enableUpdateByPrimaryKey)){
			this.enableUpdateByPrimaryKey = StringUtility.isTrue(enableUpdateByPrimaryKey);
		}
		
		if (StringUtility.stringHasValue(enableDeleteByPrimaryKey)){
			this.enableDeleteByPrimaryKey = StringUtility.isTrue(enableDeleteByPrimaryKey);
		}
		
		if (StringUtility.stringHasValue(enableDeleteByExample)){
			this.enableDeleteByExample = StringUtility.isTrue(enableDeleteByExample);
		}
		
		if (StringUtility.stringHasValue(enableUpdateByPrimaryKeySelective)){
			this.enableUpdateByPrimaryKeySelective = StringUtility.isTrue(enableUpdateByPrimaryKeySelective);
		}
		
		if (StringUtility.stringHasValue(enableUpdateByExample)){
			this.enableUpdateByExample = StringUtility.isTrue(enableUpdateByExample);
		}
		if (StringUtility.stringHasValue(enablePaged)){
			this.enablePaged = StringUtility.isTrue(enablePaged);
		}
		
		this.servicePack = properties.getProperty("targetPackage");
		this.serviceImplPack = properties.getProperty("implementationPackage");
		this.project = properties.getProperty("targetProject"); 
		this.pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

		if (this.enableAnnotation) {
			autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
			service = new FullyQualifiedJavaType("org.springframework.stereotype.Service");
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
		// 取Service名称【com.coolead.service.PetService】
		String table = introspectedTable.getBaseRecordType();
		String tableName = table.replaceAll(this.pojoUrl + ".", "");
		interfaceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");

		// 【com.coolead.mapper.PetMapper】
		daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

		// 【com.coolead.service.impl.PetServiceImpl】logger.info(toLowerCase(daoType.getShortName()));
		serviceType = new FullyQualifiedJavaType(serviceImplPack + "." + tableName + "ServiceImpl");
		
		// 【com.coolead.domain.Pet】
		pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);

		// 【com.coolead.domain.Criteria】//此外报错[已修2016-03-22，增加:"tableName +"]
		pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "."  + tableName + "Example");
		listType = new FullyQualifiedJavaType("java.util.List");
		Interface interface1 = new Interface(interfaceType);
		TopLevelClass topLevelClass = new TopLevelClass(serviceType);
		
		// 导入必须的类
		addImport(interface1, topLevelClass);

		// 接口
		addService(interface1,introspectedTable, tableName, files);
		
		// 实现类
		addServiceImpl(topLevelClass,introspectedTable, tableName, files);
		addLogger(topLevelClass);

		return files;
	}

	/**
	 * add interface
	 * 
	 * @param tableName
	 * @param files
	 */
	protected void addService(Interface interface1,IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {

		interface1.setVisibility(JavaVisibility.PUBLIC);

		// add method
		Method method = countByExample(introspectedTable, tableName);
		method.removeAllBodyLines();
		interface1.addMethod(method);

		method = selectByPrimaryKey(introspectedTable, tableName);
		method.removeAllBodyLines();
		interface1.addMethod(method);

		method = selectByExample(introspectedTable, tableName);
		method.removeAllBodyLines();
		interface1.addMethod(method);

		if (enableDeleteByPrimaryKey) {
			method = getOtherInteger("deleteByPrimaryKey", introspectedTable, tableName, 2);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableUpdateByPrimaryKeySelective) {
			method = getOtherInteger("updateByPrimaryKeySelective", introspectedTable, tableName, 1);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableUpdateByPrimaryKey) {
			method = getOtherInteger("updateByPrimaryKey", introspectedTable, tableName, 1);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableDeleteByExample) {
			method = getOtherInteger("deleteByExample", introspectedTable, tableName, 3);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableUpdateByExampleSelective) {
			method = getOtherInteger("updateByExampleSelective", introspectedTable, tableName, 4);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableUpdateByExample) {
			method = getOtherInteger("updateByExample", introspectedTable, tableName, 4);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableInsert) {
			method = getOtherInsertboolean("insert", introspectedTable, tableName);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enableInsertSelective) {
			method = getOtherInsertboolean("insertSelective", introspectedTable, tableName);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
		if (enablePaged) {//分页
			method = selectByPaged(introspectedTable, tableName);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}



		//此外报错[已修2016-03-22，增加:"context.getJavaFormatter()"]
		GeneratedJavaFile file = new GeneratedJavaFile(interface1, project,context.getJavaFormatter());
        
		files.add(file);
	}

	/**
	 * add implements class
	 * 
	 * @param introspectedTable
	 * @param tableName
	 * @param files
	 */
	protected void addServiceImpl(TopLevelClass topLevelClass,IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		// set implements interface
		topLevelClass.addSuperInterface(interfaceType);

		if (enableAnnotation) {
			topLevelClass.addAnnotation("@Service");
			topLevelClass.addImportedType(service);
		}
		// add import dao
		addField(topLevelClass, tableName);
		// add method
		topLevelClass.addMethod(countByExample(introspectedTable, tableName));
		topLevelClass.addMethod(selectByPrimaryKey(introspectedTable, tableName));
		topLevelClass.addMethod(selectByExample(introspectedTable, tableName));

		/**
		 * type:  pojo 1 ;key 2 ;example 3 ;pojo+example 4
		 */
		if (enableDeleteByPrimaryKey) {
			topLevelClass.addMethod(getOtherInteger("deleteByPrimaryKey", introspectedTable, tableName, 2));
		}
		
		if (enableUpdateByPrimaryKeySelective) {
			topLevelClass.addMethod(getOtherInteger("updateByPrimaryKeySelective", introspectedTable, tableName, 1));
		}
		
		if (enableUpdateByPrimaryKey) {
			topLevelClass.addMethod(getOtherInteger("updateByPrimaryKey", introspectedTable, tableName, 1));
		}
		
		if (enableDeleteByExample) {
			topLevelClass.addMethod(getOtherInteger("deleteByExample", introspectedTable, tableName, 3));
		}
		
		if (enableUpdateByExampleSelective) {
			topLevelClass.addMethod(getOtherInteger("updateByExampleSelective", introspectedTable, tableName, 4));
		}
		
		if (enableUpdateByExample) {
			topLevelClass.addMethod(getOtherInteger("updateByExample", introspectedTable, tableName, 4));
		}
		
		if (enableInsert) {
			topLevelClass.addMethod(getOtherInsertboolean("insert", introspectedTable, tableName));
		}
		
		if (enableInsertSelective) {
			topLevelClass.addMethod(getOtherInsertboolean("insertSelective", introspectedTable, tableName));
		}
		if (enablePaged) {//分页
			topLevelClass.addMethod(selectByPaged(introspectedTable, tableName));
		}

		//此外报错[已修2016-03-22，增加:",context.getJavaFormatter()"]
		GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project, context.getJavaFormatter());
		files.add(file);
	}

	/**
	 * 添加字段
	 * 
	 * @param topLevelClass
	 */
	protected void addField(TopLevelClass topLevelClass, String tableName) {
		// add dao
		Field field = new Field();
		field.setName(toLowerCase(daoType.getShortName())); // set var name
		topLevelClass.addImportedType(daoType);
		field.setType(daoType); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		if (enableAnnotation) {
			field.addAnnotation("@Autowired");
		}
		topLevelClass.addField(field);
	}

	/**
	 * 添加方法
	 * 
	 */
	protected Method selectByPrimaryKey(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectByPrimaryKey");
		method.setReturnType(pojoType);
		if (introspectedTable.getRules().generatePrimaryKeyClass()) {
			FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
			method.addParameter(new Parameter(type, "key"));
		} else {
			for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
				FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
				method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
			}
		}
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		// method.addBodyLine("try {");
		sb.append("return this.");
		sb.append(getDaoShort());
		sb.append("selectByPrimaryKey");
		sb.append("(");
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(");");
		method.addBodyLine(sb.toString());
		// method.addBodyLine("} catch (Exception e) {");
		// method.addBodyLine("logger.error(\"Exception: \", e);");
		// method.addBodyLine("return null;");
		// method.addBodyLine("}");
		return method;
	}

	/**
	 * add method
	 * 
	 */
	protected Method countByExample(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("countByExample");
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(pojoCriteriaType, "example"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		sb.append("int count = this.");
		sb.append(getDaoShort());
		sb.append("countByExample");
		sb.append("(");
		sb.append("example");
		sb.append(");");
		method.addBodyLine(sb.toString());
		method.addBodyLine("logger.debug(\"count: {}\", count);");
		method.addBodyLine("return count;");
		return method;
	}

	/**
	 * add method
	 * 
	 */
	protected Method selectByExample(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectByExample");
		method.setReturnType(new FullyQualifiedJavaType("List<" + tableName + ">"));
		method.addParameter(new Parameter(pojoCriteriaType, "example"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		sb.append("return this.");
		sb.append(getDaoShort());
		if (introspectedTable.hasBLOBColumns()) {
			sb.append("selectByExampleWithoutBLOBs");
		} else {
			sb.append("selectByExample");
		}
		sb.append("(");
		sb.append("example");
		sb.append(");");
		method.addBodyLine(sb.toString());
		return method;
	}

	/**
	 * add method
	 * 
	 */
	protected Method getOtherInteger(String methodName, IntrospectedTable introspectedTable, String tableName, int type) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		String params = addParams(introspectedTable, method, type);
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		// method.addBodyLine("try {");
		sb.append("return this.");
		sb.append(getDaoShort());
		if (introspectedTable.hasBLOBColumns() && (!"updateByPrimaryKeySelective".equals(methodName) 
				&& !"deleteByPrimaryKey".equals(methodName)	&& !"deleteByExample".equals(methodName) 
				&& !"updateByExampleSelective".equals(methodName))) {
			sb.append(methodName + "WithoutBLOBs");
		} else {
			sb.append(methodName);
		}
		sb.append("(");
		sb.append(params);
		sb.append(");");
		method.addBodyLine(sb.toString());
		return method;
	}

	/**
	 * add method
	 * 
	 */
	protected Method getOtherInsertboolean(String methodName, IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName(methodName);
		method.setReturnType(returnType);
		method.addParameter(new Parameter(pojoType, "record"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		if (returnType==null) {
			sb.append("this.");
		} else {
			sb.append("return this.");
		}
		sb.append(getDaoShort());
		sb.append(methodName);
		sb.append("(");
		sb.append("record");
		sb.append(");");
		method.addBodyLine(sb.toString());
		return method;
	}

	/**
	 * add method 分页
	 *
	 */
	protected Method selectByPaged(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectByPaged");
//		method.setReturnType(new FullyQualifiedJavaType("List<" + tableName + ">"));
		method.setReturnType(new FullyQualifiedJavaType("ResultPagedData<" + tableName + ">"));
		method.addParameter(new Parameter(pojoCriteriaType, "example"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"),"pageIndex"));
		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"),"pageSize"));
		method.setVisibility(JavaVisibility.PUBLIC);

		method.addBodyLine("PageHelper.startPage(pageIndex , pageSize);");
		StringBuilder sb = new StringBuilder();
		sb.append("List<" + tableName + "> list= this.");
		sb.append(getDaoShort());
		if (introspectedTable.hasBLOBColumns()) {
			sb.append("selectByExampleWithoutBLOBs");
		} else {
			sb.append("selectByExample");
		}
		sb.append("(");
		sb.append("example");
		sb.append(");");
		method.addBodyLine(sb.toString());
		method.addBodyLine("return new ResultPagedData<" + tableName + ">(list);");
		return method;
	}

	/**
	 * type: pojo 1 key 2 example 3 pojo+example 4
	 */
	protected String addParams(IntrospectedTable introspectedTable, Method method, int type1) {
		switch (type1) {
		case 1:
			method.addParameter(new Parameter(pojoType, "record"));
			return "record";
		case 2:
			if (introspectedTable.getRules().generatePrimaryKeyClass()) {
				FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
				method.addParameter(new Parameter(type, "key"));
			} else {
				for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
					FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
					method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
				}
			}
			StringBuffer sb = new StringBuffer();
			for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
				sb.append(introspectedColumn.getJavaProperty());
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		case 3:
			method.addParameter(new Parameter(pojoCriteriaType, "example"));
			return "example";
		case 4:
			method.addParameter(0, new Parameter(pojoType, "record"));
			method.addParameter(1, new Parameter(pojoCriteriaType, "example"));
			return "record, example";
		default:
			break;
		}
		return null;
	}

	protected void addComment(JavaElement field, String comment) {
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**");
		sb.append(" * ");
		comment = comment.replaceAll("\n", "<br>\n\t * ");
		sb.append(comment);
		field.addJavaDocLine(sb.toString());
		field.addJavaDocLine(" */");
	}

	/**
	 * add field
	 * 
	 * @param topLevelClass
	 */
	protected void addField(TopLevelClass topLevelClass) {
		// add success
		Field field = new Field();
		field.setName("success"); // set var name
		field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance()); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		addComment(field, "excute result");
		topLevelClass.addField(field);
		// set result
		field = new Field();
		field.setName("message"); // set result
		field.setType(FullyQualifiedJavaType.getStringInstance()); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		addComment(field, "message result");
		topLevelClass.addField(field);
	}

	/**
	 * add method
	 * 
	 */
	protected void addMethod(TopLevelClass topLevelClass) {
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setSuccess");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "success"));
		method.addBodyLine("this.success = success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
		method.setName("isSuccess");
		method.addBodyLine("return success;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("setMessage");
		method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "message"));
		method.addBodyLine("this.message = message;");
		topLevelClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.setName("getMessage");
		method.addBodyLine("return message;");
		topLevelClass.addMethod(method);
	}

	/**
	 * add method
	 * 
	 */
	protected void addMethod(TopLevelClass topLevelClass, String tableName) {
		Method method2 = new Method();
		for (int i = 0; i < methods.size(); i++) {
			Method method = new Method();
			method2 = methods.get(i);
			method = method2;
			method.removeAllBodyLines();
			method.removeAnnotation();
			StringBuilder sb = new StringBuilder();
			sb.append("return this.");
			sb.append(getDaoShort());
			sb.append(method.getName());
			sb.append("(");
			List<Parameter> list = method.getParameters();
			for (int j = 0; j < list.size(); j++) {
				sb.append(list.get(j).getName());
				sb.append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append(");");
			method.addBodyLine(sb.toString());
			topLevelClass.addMethod(method);
		}
		methods.clear();
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toLowerCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * BaseUsers to baseUsers
	 * 
	 * @param tableName
	 * @return
	 */
	protected String toUpperCase(String tableName) {
		StringBuilder sb = new StringBuilder(tableName);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * import must class
	 */
	private void addImport(Interface interfaces, TopLevelClass topLevelClass) {
		interfaces.addImportedType(pojoType);
		interfaces.addImportedType(pojoCriteriaType);
		interfaces.addImportedType(listType);
		topLevelClass.addImportedType(daoType);
		topLevelClass.addImportedType(interfaceType);
		topLevelClass.addImportedType(pojoType);
		topLevelClass.addImportedType(pojoCriteriaType);
		topLevelClass.addImportedType(listType);
		topLevelClass.addImportedType(slf4jLogger);
		topLevelClass.addImportedType(slf4jLoggerFactory);

		if (enableAnnotation) {
			topLevelClass.addImportedType(service);
			topLevelClass.addImportedType(autowired);
		}
		if(enablePaged){
			topLevelClass.addImportedType(new FullyQualifiedJavaType("com.github.pagehelper.PageHelper"));
		}

	}

	/**
	 * import logger
	 */
	private void addLogger(TopLevelClass topLevelClass) {
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("LoggerFactory.getLogger(" + topLevelClass.getType().getShortName() + ".class)"); // set value
		field.setName("logger"); // set var name
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("Logger")); // type
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);
	}

	private String getDaoShort() {
		return toLowerCase(daoType.getShortName()) + ".";
	}
	
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		returnType = method.getReturnType();
		return true;
	}
}
