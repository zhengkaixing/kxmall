package com.kxmall.common.annotation;

import com.kxmall.common.excel.CellMergeStrategy;

import java.lang.annotation.*;

/**
 * excel 列单元格合并(合并列相同项)
 *
 * 需搭配 {@link CellMergeStrategy} 策略使用
 *
 * @author 郅兴开源团队-小黑
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

	/**
	 * col index
	 */
	int index() default -1;

}
