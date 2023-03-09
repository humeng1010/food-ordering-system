package com.food.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtil {

    /**
     * 私有构造器
     */
    private ThreadLocalUtil() {
    }

    private static final  ThreadLocal<Map<String, Object>> THREAD_CONTEXT = new ThreadLocal<>();

    /**
     * 存入线程变量
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        Map<String, Object> map = THREAD_CONTEXT.get();
        // 第一次从ThreadLocalMap中根据threadLocal取出的value可能是null
        if (map == null) {
            map = new HashMap<>();
            // 把map作为value放进去
            THREAD_CONTEXT.set(map);
        }
        map.put(key, object);
    }

    /**
     * 取出线程变量
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        // 先获取Map
        Map<String, Object> map = THREAD_CONTEXT.get();
        // 从Map中得到USER_INFO
        return map != null ? map.get(key) : null;
    }

    /**
     * 移除当前线程的指定变量
     * @param key
     */
    public static void remove(String key) {
        Map<String, Object> map = THREAD_CONTEXT.get();
        map.remove(key);
    }

    /**
     * 移除当前线程的所有变量
     */
    public static void clear() {
        THREAD_CONTEXT.remove();
    }
}