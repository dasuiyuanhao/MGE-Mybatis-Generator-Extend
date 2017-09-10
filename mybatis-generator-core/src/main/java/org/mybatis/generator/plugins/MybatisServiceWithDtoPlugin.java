package org.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getSetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * generate Service层
 * 包括service、Dto DtoConverter QueryDto DtoAddGroup DtUpdateGroup
 * 分页查询功能
 * @author LiuXiaoliang
 *
 * @date 2017年9月4日
 */
public class MybatisServiceWithDtoPlugin extends PluginAdapter{

	protected IntrospectedTable introspectedTable;

	private FullyQualifiedJavaType slf4jLogger;
	private FullyQualifiedJavaType slf4jLoggerFactory;
	private FullyQualifiedJavaType dtoType;
	private FullyQualifiedJavaType dtoConverterType;
	private FullyQualifiedJavaType dtoQueryType;
	private FullyQualifiedJavaType dtoAddGroupType;
	private FullyQualifiedJavaType dtoUpdateGroupType;

	private FullyQualifiedJavaType daoType;
	private FullyQualifiedJavaType serviceType;//Service
	private FullyQualifiedJavaType interfaceType;//Service接口
	private FullyQualifiedJavaType pojoType;
	private FullyQualifiedJavaType pojoCriteriaType;
	private FullyQualifiedJavaType listType;
	private FullyQualifiedJavaType autowired;
	private FullyQualifiedJavaType service;//注解
	private FullyQualifiedJavaType returnType;
	private String project;
	private String pojoUrl;
	private String dtoPack;
	private String dtoConverterPack;
	private String servicePack;
	private String serviceImplPack;
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

	public MybatisServiceWithDtoPlugin() {
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

//		String enableUpdateByExampleSelective = properties.getProperty("enableUpdateByExampleSelective");
		String enableUpdateByExampleSelective = properties.getProperty("enableUpdateByConditionSelective");

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

		this.dtoPack = properties.getProperty("dtoPackage");
		this.dtoConverterPack = properties.getProperty("dtoConverterPackage");


		return true;
	}

	/**
	 *
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		this.introspectedTable=introspectedTable;

		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
		// 取service名称【例如com.biyao.sj.service.product】
		String table = introspectedTable.getBaseRecordType();
		String tableName = table.replaceAll(this.pojoUrl + ".", "");
		interfaceType = new FullyQualifiedJavaType(servicePack + ".I" + tableName + "Service");

		// 【com.biyao.sj.dao.mapper.PetMapper】
		daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

		// 【com.biyao.sj.service.impl.PetService】logger.info(toLowerCase(daoType.getShortName()));
		serviceType = new FullyQualifiedJavaType(serviceImplPack + "." + tableName + "ServiceImpl");

		// 【com.biyao.sj.model.Pet】
		pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);

		// 【com.coolead.domain.Criteria】//此外报错[已修2016-03-22，增加:"tableName +"]
		pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "."  + tableName + "Example");
		listType = new FullyQualifiedJavaType("java.util.List");
		Interface interface1 = new Interface(interfaceType);
		TopLevelClass topLevelClass = new TopLevelClass(serviceType);
		//Dto
		// 【com.biyao.sj.service.impl.PetService】logger.info(toLowerCase(daoType.getShortName()));
		dtoType = new FullyQualifiedJavaType(dtoPack + "." + tableName + "Dto");
		TopLevelClass dtoClass = new TopLevelClass(dtoType);
		dtoConverterType = new FullyQualifiedJavaType(dtoConverterPack + "." + tableName + "DtoConverter");
		TopLevelClass dtoConverterClass = new TopLevelClass(dtoConverterType);
		dtoQueryType = new FullyQualifiedJavaType(dtoPack + "." + tableName + "QueryDto");
		TopLevelClass dtoQueryClass = new TopLevelClass(dtoQueryType);
		dtoAddGroupType = new FullyQualifiedJavaType(dtoPack + ".group." + tableName + "DtoAddGroup");
		Interface dtoAddGroupClass = new Interface(dtoAddGroupType);
		dtoUpdateGroupType = new FullyQualifiedJavaType(dtoPack + ".group." + tableName + "DtoUpdateGroup");
		Interface dtoUpdateGroupClass = new Interface(dtoUpdateGroupType);

		//Dto DtoConverter QueryDto DtoAddGroup DtUpdateGroup
		addDtoClass(dtoClass,dtoConverterClass,dtoQueryClass,dtoAddGroupClass,dtoUpdateGroupClass,introspectedTable, tableName, files);
		/**service 开始*/
		// 导入必须的类
		addImport(interface1, topLevelClass);

		// 接口
		addInterface(interface1,dtoClass,dtoConverterClass,dtoQueryClass,
				introspectedTable, tableName, files);

		// service实现类
		addImpl(topLevelClass,dtoClass,dtoConverterClass,dtoQueryClass,
				introspectedTable, tableName, files);
		addLogger(topLevelClass);
		/**service 结束*/

		return files;
	}

	/**
	 * add interface of service
	 *
	 * @param tableName
	 * @param files
	 */
	protected void addInterface(Interface interface1,TopLevelClass dtoClass,TopLevelClass dtoConverterClass,TopLevelClass dtoQueryClass,
							IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {

		interface1.setVisibility(JavaVisibility.PUBLIC);

		// add method
		Method method;

		method = countByCondition(introspectedTable, tableName);
		method.removeAllBodyLines();
		interface1.addMethod(method);

		method = selectByPrimaryKey(introspectedTable, tableName);
		method.removeAllBodyLines();
		interface1.addMethod(method);

		method = selectByCondition(introspectedTable, tableName);
		method.removeAllBodyLines();
		interface1.addMethod(method);

//		if (enableDeleteByPrimaryKey) {
//			method = getOtherInteger("deleteByPrimaryKey", introspectedTable, tableName, 2);
//			method.removeAllBodyLines();
//			interface1.addMethod(method);
//		}
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
//		if (enableDeleteByExample) {
//			method = getOtherInteger("deleteByExample", introspectedTable, tableName, 3);
//			method.removeAllBodyLines();
//			interface1.addMethod(method);
//		}
		if (enableUpdateByExampleSelective) {
//			method = getOtherInteger("updateByConditionSelective", introspectedTable, tableName, 4);
			method = getOtherInteger("updateByCondition", introspectedTable, tableName, 4);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
//		if (enableUpdateByExample) {
//			method = getOtherInteger("updateByExample", introspectedTable, tableName, 4);
//			method.removeAllBodyLines();
//			interface1.addMethod(method);
//		}
		if (enableInsert) {
			method = getOtherInsertboolean("createRecord", introspectedTable, tableName);
			method.removeAllBodyLines();
			interface1.addMethod(method);
		}
//		if (enableInsertSelective) {
//			method = getOtherInsertboolean("insertSelective", introspectedTable, tableName);
//			method.removeAllBodyLines();
//			interface1.addMethod(method);
//		}
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
	 * service
	 * add implements class
	 *
	 * @param introspectedTable
	 * @param tableName
	 * @param files
	 */
	protected void addImpl(TopLevelClass topLevelClass,TopLevelClass dtoClass,TopLevelClass dtoConverterClass,TopLevelClass dtoQueryClass,
								IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
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
		Method method;
		method = methodOfQueryDtoToExample(introspectedTable, tableName);
		topLevelClass.addMethod(method);

		method=countByCondition(introspectedTable, tableName);
		topLevelClass.addMethod(method);
		method=selectByPrimaryKey(introspectedTable, tableName);
		topLevelClass.addMethod(method);
		method=selectByCondition(introspectedTable, tableName);
		topLevelClass.addMethod(method);


		/**
		 * type:  pojo 1 ;key 2 ;example 3 ;pojo+example 4
		 */
		if (enableDeleteByPrimaryKey) {
			topLevelClass.addMethod(getOtherInteger("deleteByPrimaryKey", introspectedTable, tableName, 2));
		}

		if (enableUpdateByPrimaryKeySelective) {
			method=getOtherInteger("updateByPrimaryKeySelective", introspectedTable, tableName, 1);
			topLevelClass.addMethod(method);
		}

		if (enableUpdateByPrimaryKey) {
			method=getOtherInteger("updateByPrimaryKey", introspectedTable, tableName, 1);
			topLevelClass.addMethod(method);
		}

//		if (enableDeleteByExample) {
//			topLevelClass.addMethod(getOtherInteger("deleteByExample", introspectedTable, tableName, 3));
//		}

		if (enableUpdateByExampleSelective) {
			method=getOtherInteger("updateByCondition", introspectedTable, tableName, 4);
			topLevelClass.addMethod(method);
		}

//		if (enableUpdateByExample) {
//			method=getOtherInteger("updateByExample", introspectedTable, tableName, 4);
//			topLevelClass.addMethod(method);
//		}

		if (enableInsert) {
			topLevelClass.addMethod(getOtherInsertboolean("createRecord", introspectedTable, tableName));
		}

//		if (enableInsertSelective) {
//			method=getOtherInsertboolean("insertSelective", introspectedTable, tableName);
//			topLevelClass.addMethod(method);
//		}
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
	 * 添加方法，将QueryDto转换为Example
	 *
	 */
	protected Method methodOfQueryDtoToExample(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("queryDtoToExample");
		method.setReturnType(pojoCriteriaType);
		method.addParameter(new Parameter(dtoQueryType, "data"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addBodyLine(pojoCriteriaType.getShortName()+" result ="+" new "+pojoCriteriaType.getShortName()+"();");
		method.addBodyLine("//添加转换逻辑");
		method.addBodyLine("return result;");
		return method;
	}


	/**
	 * 添加方法
	 *
	 */
	protected Method selectByPrimaryKey(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectByPrimaryKey");
		method.setReturnType(dtoType);
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
		sb.append("return "+dtoConverterType.getShortName()+".toDto(this.");
		sb.append(getDaoShort());
		sb.append("selectByPrimaryKey");
		sb.append("(");
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append("));");
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
	protected Method countByCondition(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("countByCondition");
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.addParameter(new Parameter(dtoQueryType, "data"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addBodyLine(pojoCriteriaType.getShortName()+" example ="+" this.queryDtoToExample(data);");
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
	protected Method selectByCondition(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectByCondition");
		String strReturnType="List<" + dtoType.getShortName() + ">";
		method.setReturnType(new FullyQualifiedJavaType(strReturnType));
		method.addParameter(new Parameter(dtoQueryType, "data"));
		method.setVisibility(JavaVisibility.PUBLIC);
		method.addBodyLine(pojoCriteriaType.getShortName()+" example ="+" this.queryDtoToExample(data);");
		StringBuilder sb = new StringBuilder();
//		sb.append(strReturnType+" result = "+"new ArrayList();\n");
		sb.append("List<" + tableName + ">"+" list = this.");
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
		method.addBodyLine("return "+tableName+"DtoConverter.toDtoList(list);");
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
				&& !"updateByConditionSelective".equals(methodName)
				&& !"updateByCondition".equals(methodName))) {
			if("updateByCondition".equals(methodName)){
				sb.append("updateByExampleSelective"+ "WithoutBLOBs");
			}
			else
				sb.append(methodName + "WithoutBLOBs");
		} else {
			if("updateByCondition".equals(methodName)){
				sb.append("updateByExampleSelective");
			}
			else
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
		method.addParameter(new Parameter(dtoType, "record"));
		method.setVisibility(JavaVisibility.PUBLIC);
		StringBuilder sb = new StringBuilder();
		if (returnType==null) {
			sb.append("this.");
		} else {
			sb.append("return this.");
		}
		sb.append(getDaoShort());
		if("createRecord".equals(methodName)){
			sb.append("insertSelective");
		}
		else
			sb.append(methodName);

		sb.append("(");
		sb.append(dtoConverterType.getShortName()+".toModel(record)");
		sb.append(");");
		method.addBodyLine(sb.toString());
		return method;
	}

	/**
	 * type: pojo 1 key 2 example 3 pojo+example 4
	 */
	protected String addParams(IntrospectedTable introspectedTable, Method method, int type1) {
		switch (type1) {
			case 1:
				method.addParameter(new Parameter(dtoType, "record"));
				return dtoConverterType.getShortName()+ ".toModel(record)";
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
//				method.addParameter(new Parameter(pojoCriteriaType, "example"));
				method.addParameter(new Parameter(dtoQueryType, "data"));
				return "queryDtoToExample(data)";
			case 4:
				method.addParameter(0, new Parameter(dtoType, "record"));
				method.addParameter(1, new Parameter(dtoQueryType, "data"));
				return dtoConverterType.getShortName()+ ".toModel(record)"+", queryDtoToExample(data)";
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
			sb.append(getServiceClassShort());
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
		interfaces.addImportedType(dtoQueryType);
		topLevelClass.addImportedType(dtoQueryType);
		interfaces.addImportedType(dtoType);
		topLevelClass.addImportedType(dtoType);
		topLevelClass.addImportedType(dtoConverterType);
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

	private String getServiceClassShort() {
		return toLowerCase(serviceType.getShortName()) + ".";
	}

	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		returnType = method.getReturnType();
		return true;
	}


	/**
	 * Dto DtoConverter QueryDto DtoAddGroup DtUpdateGroup
	 * add class
	 */
	protected void addDtoClass(TopLevelClass dtoClass,TopLevelClass dtoConverterClass,
							   TopLevelClass dtoQueryClass,Interface dtoAddGroupClass,Interface dtoUpdateGroupClass,
							   IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
		dtoClass.setVisibility(JavaVisibility.PUBLIC);
		dtoClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
		dtoClass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
		dtoClass.addJavaDocLine("/**");
		dtoClass.addJavaDocLine(" * ");
		dtoClass.addJavaDocLine(" */");

		// set implements interface
//		topLevelClass.addSuperInterface(interfaceType);

		//生成字段
		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
//			if (RootClassInfo.getInstance(dtoClass, warnings)
//					.containsProperty(introspectedColumn)) {
//				continue;
//			}
			//remarks
			String remarks=introspectedColumn.getRemarks()==null?"":introspectedColumn.getRemarks();
			Field field = getJavaBeansField(introspectedColumn);
			field.addJavaDocLine("/**");
			field.addJavaDocLine(" *"+remarks);
			field.addJavaDocLine(" */");
			String validateNull="@Null(message = \""+remarks+"必须为空\", groups = { "+dtoAddGroupClass.getType().getShortName()+".class })";
			String validateNotNull="@NotNull(message = \""+remarks+"不能为空\", groups = { "+dtoUpdateGroupClass.getType().getShortName()+".class })";
			if(introspectedColumn.isIdentity()){
//				@Null(message = "id必须为空", groups = { AttributeAddGroup.class })
//						@NotNull(message = "id不能为空", groups = { AttributeUpdateGroup.class })
				field.addJavaDocLine(validateNull);
				field.addJavaDocLine(validateNotNull);
			}
			else if(!introspectedColumn.isNullable()){
				field.addJavaDocLine(validateNull);
				field.addJavaDocLine(validateNotNull);
			}
			dtoClass.addField(field);
			dtoClass.addImportedType(field.getType());

			Method method = getJavaBeansGetter(introspectedColumn);
			dtoClass.addMethod(method);
			if (!introspectedTable.isImmutable()) {
				method = getJavaBeansSetter(introspectedColumn);
				dtoClass.addMethod(method);
			}
		}
		dtoClass.addImportedType("javax.validation.constraints.*");
		dtoClass.addImportedType(dtoAddGroupClass.getType().getFullyQualifiedName());
		dtoClass.addImportedType(dtoUpdateGroupClass.getType().getFullyQualifiedName());
		GeneratedJavaFile file = new GeneratedJavaFile(dtoClass, project, context.getJavaFormatter());
		files.add(file);

		//DtoConverter
		dtoConverterClass.setVisibility(JavaVisibility.PUBLIC);
		dtoConverterClass.addImportedType("java.util.List");
		dtoConverterClass.addImportedType("java.util.ArrayList");
		dtoConverterClass.addImportedType(dtoClass.getType());
		dtoConverterClass.addImportedType(pojoType);
		String dtoClassName=tableName +"Dto";
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setStatic(true);
		method.setName("toDto");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(tableName + ""), "data"));
		method.setReturnType(new FullyQualifiedJavaType(dtoClassName));
		method.addBodyLine(dtoClassName + " result = BeanConverter.convert(data, "+dtoClassName+".class);");
		method.addBodyLine("if(result==null) return result;");
		method.addBodyLine("//add special field handler");
		method.addBodyLine("");
		method.addBodyLine("return result;");
		dtoConverterClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setStatic(true);
		method.setName("toDtoList");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + tableName + ">"), "data"));
		method.setReturnType(new FullyQualifiedJavaType("List<" + dtoClassName + ">"));
		method.addBodyLine("List<" + dtoClassName + ">"+" result = new ArrayList();");
		method.addBodyLine("if(data==null) return result;");
		method.addBodyLine("for("+tableName+" item:data){");
		method.addBodyLine("	result.add(toDto(item));");
		method.addBodyLine("}");
		method.addBodyLine("return result;");
		dtoConverterClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setStatic(true);
		method.setName("toModel");
		method.addParameter(new Parameter(new FullyQualifiedJavaType(dtoClassName), "data"));
		method.setReturnType(new FullyQualifiedJavaType(tableName));
		method.addBodyLine(tableName + " result = BeanConverter.convert(data, "+tableName+".class);");
		method.addBodyLine("if(result==null) return null;");
		method.addBodyLine("//add special field handler");
		method.addBodyLine("");
		method.addBodyLine("return result;");
		dtoConverterClass.addMethod(method);

		method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setStatic(true);
		method.setName("toModelList");
		method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + dtoClassName + ">"), "data"));
		method.setReturnType(new FullyQualifiedJavaType("List<" + tableName + ">"));
		method.addBodyLine("List<" + tableName + ">"+" result = new ArrayList();");
		method.addBodyLine("if(data==null) return result;");
		method.addBodyLine("for("+dtoClassName+" item:data){");
		method.addBodyLine("	result.add(toModel(item));");
		method.addBodyLine("}");
		method.addBodyLine("return result;");
		dtoConverterClass.addMethod(method);

		file = new GeneratedJavaFile(dtoConverterClass, project, context.getJavaFormatter());
		files.add(file);

		//queryDto
		dtoQueryClass.setVisibility(JavaVisibility.PUBLIC);
		dtoQueryClass.setSuperClass("BaseQueryDto");
		dtoQueryClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
		dtoQueryClass.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
		dtoQueryClass.addJavaDocLine("/**");
		dtoQueryClass.addJavaDocLine(" * "+dtoQueryClass.getType().getShortName());
		dtoQueryClass.addJavaDocLine(" */");
		// set implements interface
//		topLevelClass.addSuperInterface(interfaceType);

//		//生成字段
//		for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
////			if (RootClassInfo.getInstance(dtoClass, warnings)
////					.containsProperty(introspectedColumn)) {
////				continue;
////			}
//			Field field = getJavaBeansField(introspectedColumn);
//			field.addJavaDocLine("/**");
//			field.addJavaDocLine(" *"+(introspectedColumn.getRemarks()==null?"":introspectedColumn.getRemarks()));
//			field.addJavaDocLine(" */");
//			dtoQueryClass.addField(field);
//			dtoQueryClass.addImportedType(field.getType());
//
//			Method method = getJavaBeansGetter(introspectedColumn);
//			dtoQueryClass.addMethod(method);
//			if (!introspectedTable.isImmutable()) {
//				method = getJavaBeansSetter(introspectedColumn);
//				dtoQueryClass.addMethod(method);
//			}
//		}
		file = new GeneratedJavaFile(dtoQueryClass, project, context.getJavaFormatter());
		files.add(file);

		//DtoAddGroup
		dtoAddGroupClass.setVisibility(JavaVisibility.PUBLIC);
		dtoAddGroupClass.addJavaDocLine("/**");
		dtoAddGroupClass.addJavaDocLine(" * 新增"+dtoClassName+"的分组校验");
		dtoAddGroupClass.addJavaDocLine(" */");
		file = new GeneratedJavaFile(dtoAddGroupClass, project, context.getJavaFormatter());
		files.add(file);
		//DtoUpdateGroup
		dtoUpdateGroupClass.setVisibility(JavaVisibility.PUBLIC);
		dtoUpdateGroupClass.addJavaDocLine("/**");
		dtoUpdateGroupClass.addJavaDocLine(" * 修改"+dtoClassName+"的分组校验");
		dtoUpdateGroupClass.addJavaDocLine(" */");
		file = new GeneratedJavaFile(dtoUpdateGroupClass, project, context.getJavaFormatter());
		files.add(file);

	}

	public Field getJavaBeansField(IntrospectedColumn introspectedColumn) {
		FullyQualifiedJavaType fqjt = introspectedColumn
				.getFullyQualifiedJavaType();
		String property = introspectedColumn.getJavaProperty();

		Field field = new Field();
		field.setVisibility(JavaVisibility.PRIVATE);
		field.setType(fqjt);
		field.setName(property);
		context.getCommentGenerator().addFieldComment(field,
				introspectedTable, introspectedColumn);

		return field;
	}

	public Method getJavaBeansGetter(IntrospectedColumn introspectedColumn) {
		FullyQualifiedJavaType fqjt = introspectedColumn
				.getFullyQualifiedJavaType();
		String property = introspectedColumn.getJavaProperty();

		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(fqjt);
		method.setName(getGetterMethodName(property, fqjt));
		context.getCommentGenerator().addGetterComment(method,
				introspectedTable, introspectedColumn);

		StringBuilder sb = new StringBuilder();
		sb.append("return "); //$NON-NLS-1$
		sb.append(property);
		sb.append(';');
		method.addBodyLine(sb.toString());

		return method;
	}

	public Method getJavaBeansSetter(IntrospectedColumn introspectedColumn) {
		FullyQualifiedJavaType fqjt = introspectedColumn
				.getFullyQualifiedJavaType();
		String property = introspectedColumn.getJavaProperty();

		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName(getSetterMethodName(property));
		method.addParameter(new Parameter(fqjt, property));
		context.getCommentGenerator().addSetterComment(method,
				introspectedTable, introspectedColumn);

		StringBuilder sb = new StringBuilder();
		if (isTrimStringsEnabled() && introspectedColumn.isStringColumn()) {
			sb.append("this."); //$NON-NLS-1$
			sb.append(property);
			sb.append(" = "); //$NON-NLS-1$
			sb.append(property);
			sb.append(" == null ? null : "); //$NON-NLS-1$
			sb.append(property);
			sb.append(".trim();"); //$NON-NLS-1$
			method.addBodyLine(sb.toString());
		} else {
			sb.append("this."); //$NON-NLS-1$
			sb.append(property);
			sb.append(" = "); //$NON-NLS-1$
			sb.append(property);
			sb.append(';');
			method.addBodyLine(sb.toString());
		}

		return method;
	}

	public boolean isTrimStringsEnabled() {
		Properties properties = context
				.getJavaModelGeneratorConfiguration().getProperties();
		boolean rc = isTrue(properties
				.getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS));
		return rc;
	}

	/**
	 * add method 分页
	 *
	 */
	protected Method selectByPaged(IntrospectedTable introspectedTable, String tableName) {
		Method method = new Method();
		method.setName("selectByPaged");
		String strReturnType="ResultPagedData<" + dtoType.getShortName() + ">";
		method.setReturnType(new FullyQualifiedJavaType(strReturnType));
//		method.addParameter(new Parameter(pojoCriteriaType, "example"));
//		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"),"pageIndex"));
//		method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"),"pageSize"));
		method.addParameter(new Parameter(dtoQueryType, "data"));
		method.setVisibility(JavaVisibility.PUBLIC);

		method.addBodyLine("PageHelper.startPage(data.getPageIndex() , data.getPageSize());");
		StringBuilder sb = new StringBuilder();
		sb.append("List<" + tableName + "> list= this.");
		sb.append(getDaoShort());
		if (introspectedTable.hasBLOBColumns()) {
			sb.append("selectByExampleWithoutBLOBs");
		} else {
			sb.append("selectByExample");
		}
		sb.append("(");
		sb.append("queryDtoToExample(data)");
		sb.append(");");
		method.addBodyLine(sb.toString());
		method.addBodyLine(strReturnType+" result = new ResultPagedData(list);");
		method.addBodyLine("result.setDatas(" + dtoConverterType.getShortName()+ ".toDtoList(list));");
		method.addBodyLine("return result;");
		return method;
	}



}
