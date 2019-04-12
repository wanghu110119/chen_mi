package com.mht.modules.sys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * ehcache 缓存工具类
 * 
 * cacheName在ehcache.xml中配置
 */
public class EhcacheUtil {

    public static CacheManager manager = null;
    static {
        File file = new File(EhcacheUtil.class.getResource("/").getPath() + "/cache/ehcache-local.xml");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
            manager = CacheManager.create(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static Object get(String cacheName, Object key) {

        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            Element element = cache.get(key);
            if (element != null) {
                return element.getObjectValue();
            }
        }
        return null;
    }

    public static void put(String cacheName, Object key, Object value) {
        Cache cache = manager.getCache(cacheName);
        String[] cacheNames = manager.getCacheNames();
        for (String string : cacheNames) {
            System.out.println("Cache名称：" + string);
        }
        if (cache != null) {
            cache.put(new Element(key, value));
        }
    }

    public static boolean remove(String cacheName, Object key) {
        Cache cache = manager.getCache(cacheName);
        if (cache != null) {
            return cache.remove(key);
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        /*
         * String key = "key"; String value = "hello"; EhcacheUtil.put("mytest",
         * key, value); System.out.println(EhcacheUtil.get("mytest", key));
         */
        /*
         * URL url = new
         * Object().getClass().getResource("/cache/ehcache-local.xml"); manager
         * = CacheManager.create(url);
         */

        System.out.println(manager.getCacheNames().length);
    }

}