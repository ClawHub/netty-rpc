package com.clawhub.nettyrpc.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The type Class info.
 */
public class ClassInfo implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 2432610677496077297L;
    /**
     * 类名
     */
    private String className;
    /**
     * 函数名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] types;
    /**
     * 参数列表
     */
    private Object[] objects;

    /**
     * Gets serial version uid.
     *
     * @return the serial version uid
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets class name.
     *
     * @param className the class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets method name.
     *
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Sets method name.
     *
     * @param methodName the method name
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Get types class [ ].
     *
     * @return the class [ ]
     */
    public Class<?>[] getTypes() {
        return types;
    }

    /**
     * Sets types.
     *
     * @param types the types
     */
    public void setTypes(Class<?>[] types) {
        this.types = types;
    }

    /**
     * Get objects object [ ].
     *
     * @return the object [ ]
     */
    public Object[] getObjects() {
        return objects;
    }

    /**
     * Sets objects.
     *
     * @param objects the objects
     */
    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", types=" + Arrays.toString(types) +
                ", objects=" + Arrays.toString(objects) +
                '}';
    }
}
