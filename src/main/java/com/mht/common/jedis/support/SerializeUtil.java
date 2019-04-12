package com.mht.common.jedis.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;

/**
 * 
 * @ClassName: SerializeUtil
 * @Description: 将list与自定义对象进行序列化
 * @author 华强
 * @date 2017年6月5日 下午1:39:04 
 * @version 1.0.0
 */
public class SerializeUtil {
    private static final Log LOG = LogFactory.getLog(SerializeUtil.class);

    /**
     * 将序列化对象转换成序列的byte型
     *
     * @param object
     * @return
     * @author xutaog 2015年9月18日
     */
    public static byte[] serialize(Serializable object) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            // 序列化
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            LOG.error("serialize fail", e);
        }
        return null;
    }

    /**
     * 将对象反序列成对象
     *
     * @param bytes
     * @return
     * @author xutaog 2015年9月18日
     */
    public static Object unserialize(byte[] bytes) {
        if (null != bytes) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
                // 反序列化
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                LOG.error("unserialize fail", e);
            }
        }
        return null;
    }
}