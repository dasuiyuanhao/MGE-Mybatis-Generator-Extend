package com.coolead.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * 对象验证器，配合注解ValidatorAspectAnnotation可以捕获异常并返回Result对象
 * @author caizhiqin
 *
 */
public class BeanValidator {
	/**
	 * 校验参数，返回一个带有返回结果的result
	 * @param <T>
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> void validate(T object, Class<?>... groups) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			String message = constraintViolations.iterator().next().getMessage();
			throw new ValidationException(message);
		}
	}
}