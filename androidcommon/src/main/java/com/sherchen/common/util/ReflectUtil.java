package com.sherchen.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.TreeMap;

/**
 * @since 2017-5-16
 * @author Sherchen
 * 反射工具类
 */
public class ReflectUtil {

	static TreeMap<String, Method> mapMethod = new TreeMap<String, Method>();

    /**
     * 调用方法
     * @param cls 类名
     * @param clsObj 类对象
     * @param methodName 类方法名
     * @param classes 类方法的参数类型数组
     * @param objects 调用类方法的参数值数组
     * @return
     */
	public static Object invoke(Class<?> cls, Object clsObj, String methodName,
			Class<?>[] classes, Object[] objects) {
		Method m = null;
		try {
			String key = cls + "." + methodName;
			if (mapMethod.containsKey(key)) {
				m = mapMethod.get(key);
			} else {
				try {
					m = cls.getDeclaredMethod(methodName, classes);
				} catch (NoSuchMethodException ex) {
					ex.printStackTrace();
				}
				mapMethod.put(key, m);
			}
			if (m != null) {
				return m.invoke(clsObj, objects);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    /**
     * 调用方法
     * @param clsObj 类对象
     * @param methodName 类方法名
     * @param classes 类方法的参数类型数组
     * @param objects 调用类方法的参数值数组
     * @return
     */
	public static Object invoke(Object clsObj, String methodName, Class<?>[] classes,
			Object[] objects) {
		if (clsObj != null) {
			return invoke(clsObj.getClass(), clsObj, methodName, classes, objects);
		}
		return null;
	}

    /**
     * 调用方法
     * @param obj 类对象
     * @param methodName 类方法名
     * @param classes 类方法的参数类型数组
     * @param objs 调用类方法的参数值数组
     * @return
     */
	public static Object invokeMethod(Object obj, String methodName, Class<?>[] classes, Object[] objs){
		try {
			Method method = obj.getClass().getDeclaredMethod(methodName, classes);
			method.setAccessible(true);
			return method.invoke(obj, objs);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		return null;
	}


    /**
     * 获取直接超类的第index个泛型类型
     * @param clazz 子类类型
     * @param index 直接超类的泛型索引
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("The index " + (index < 0 ? "is lessen than 0" : "is out of total"));
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 获取直接超类的第1个泛型类型
     * @param clazz 子类类型
     * @return
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 获取泛型方法的第index个泛型类型
     * @param method 泛型方法
     * @param index 泛型方法的索引
     * @return
     */
    public static Class getMethodGenericReturnType(Method method, int index) {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();
            if (index >= typeArguments.length || index < 0) {
                throw new RuntimeException("The index " + (index < 0 ? "is lessen than 0" : "is out of total"));
            }
            return (Class) typeArguments[index];
        }
        return Object.class;
    }

    /**
     * 获取泛型方法的第0个泛型类型
     * @param method 泛型方法
     * @return
     */
    public static Class getMethodGenericReturnType(Method method) {
        return getMethodGenericReturnType(method, 0);
    }

    /**
     * 获取field的泛型
     * @param field
     * @param index
     * @return
     */
    public static Class getFieldGenericType(Field field, int index) {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (index >= fieldArgTypes.length || index < 0) {
                throw new RuntimeException("The index " + (index < 0 ? "is lessen than 0" : "is out of total"));
            }
            return (Class) fieldArgTypes[index];
        }
        return Object.class;
    }

    @SuppressWarnings("unchecked")
    public static Class getFieldGenericType(Field field) {
        return getFieldGenericType(field, 0);
    }

}
