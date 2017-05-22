/**
 * Copyright 2014 Zhenguo Jin (jinzhenguo1990@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sherchen.common.util;

import android.content.Context;

/**
 * R类反射工具类
 * @author zhenguo
 */
public final class RUtils {

    public static final String POINT = ".";
    public static final String R = "R";
    public static final String JOIN = "$";
    public static final String ANIM = "anim";
    public static final String ATTR = "attr";
    public static final String COLOR = "color";
    public static final String DIMEN = "dimen";
    public static final String DRAWABLE = "drawable";
    public static final String ID = "id";
    public static final String LAYOUT = "layout";
    public static final String MENU = "menu";
    public static final String RAW = "raw";
    public static final String STRING = "string";
    public static final String STYLE = "style";
    public static final String STYLEABLE = "styleable";

    /**
     * Don't let anyone instantiate this class.
     */
    private RUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 根据name获取对应的R.anim.xx值
     *
     * @param context 上下文
     * @param name anim名称，比如fade_in
     */
    public static int getAnim(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + ANIM)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.attr.xx值
     * @param context 上下文
     * @param name attr名称
     */
    public static int getAttr(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + ATTR)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.color.xx值
     *
     * @param context 上下文
     * @param name color名称
     */
    public static int getColor(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN + COLOR)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.dimen.xx值
     *
     * @param context 上下文
     * @param name dimen名称
     */
    public static int getDimen(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN + DIMEN)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.drawable.xx值
     *
     * @param context 上下文
     * @param name drawable名称
     */
    public static int getDrawable(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + DRAWABLE).getDeclaredField(name)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.id.xx名称
     *
     * @param context 上下文
     * @param name id名称
     */
    public static int getId(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + ID)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.layout.xx值
     *
     * @param context 上下文
     * @param name layout名称
     */
    public static int getLayout(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + LAYOUT).getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.menu.xx值
     *
     * @param context 上下文
     * @param name menu名称
     */
    public static int getMenu(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + MENU)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据名称获取R.raw.xx值
     *
     * @param context 上下文
     * @param name raw名称
     */
    public static int getRaw(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(context.getPackageName() + POINT + R + JOIN + RAW)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.string.xx值
     *
     * @param context 上下文
     * @param name  string名称
     */
    public static int getString(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + STRING).getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.style.xx值
     *
     * @param context 上下文
     * @param name style名称
     */
    public static int getStyle(Context context, String name) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN + STYLE)
                    .getDeclaredField(name).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据name获取R.styleable.xx值
     *
     * @param context 上下文
     * @param name styleable名称
     */
    public static int[] getStyleable(Context context, String name) {
        try {
            return (int[]) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + STYLEABLE).getDeclaredField(name)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据styleable和attribute获取自定义属性值
     *
     * @param context    上下文
     * @param styleableName 自定义类型
     * @param attributeName 自定义类型下面的自定义属性
     */
    public static int getStyleableAttribute(Context context,
                                            String styleableName, String attributeName) {
        try {
            return (Integer) Class
                    .forName(
                            context.getPackageName() + POINT + R + JOIN
                                    + STYLEABLE)
                    .getDeclaredField(styleableName + "_" + attributeName)
                    .get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
