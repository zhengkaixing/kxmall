package com.kxmall.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * bean拷贝工具（基于 Hutool 反射实现，兼容 JDK 17 模块系统）
 * <p>
 * 请区分好`浅拷贝`和`深拷贝`再做使用
 *
 * @author 郅兴开源团队-小黑
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCopyUtils {

    /**
     * 与原先 CGLIB BeanCopier 对齐：同名属性拷贝，类型无法转换时跳过，不中断整次拷贝
     */
    private static final CopyOptions COPY_OPTIONS = CopyOptions.create().setIgnoreError(true);

    /**
     * 单对象基于class创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, Class<V> desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        final V target = ReflectUtil.newInstanceIfPossible(desc);
        return copy(source, target);
    }

    /**
     * 单对象基于对象创建拷贝
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    public static <T, V> V copy(T source, V desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        BeanUtil.copyProperties(source, desc, COPY_OPTIONS);
        return desc;
    }

    /**
     * 列表对象基于class创建拷贝
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    public static <T, V> List<V> copyList(List<T> sourceList, Class<V> desc) {
        if (ObjectUtil.isNull(sourceList)) {
            return null;
        }
        if (CollUtil.isEmpty(sourceList)) {
            return CollUtil.newArrayList();
        }
        return BeanUtil.copyToList(sourceList, desc, COPY_OPTIONS);
    }

    /**
     * bean拷贝到map
     *
     * @param bean 数据来源实体
     * @return map对象
     */
    public static <T> Map<String, Object> copyToMap(T bean) {
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        return BeanUtil.beanToMap(bean);
    }

    /**
     * map拷贝到bean
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(beanClass)) {
            return null;
        }
        return BeanUtil.toBean(map, beanClass);
    }

    /**
     * map拷贝到bean
     *
     * @param map  数据来源
     * @param bean bean对象
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(bean)) {
            return null;
        }
        BeanUtil.fillBeanWithMap(map, bean, false);
        return bean;
    }

}
