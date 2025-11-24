package com.yeeiee.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

/**
 * <p>
 * 文件 MIME 类型探测工具类
 * </p>
 *
 * @author chen
 * @since 2025-09-18
 */
@Slf4j
public final class FileTypeUtils {

    private static final Tika TIKA = new Tika();

    /**
     * 获得文件的 mineType，对于 doc，jar 等文件会有误差
     *
     * @param content 文件内容
     * @return mineType 无法识别时会返回“application/octet-stream”
     */
    public static String getMineType(byte[] content) {
        return TIKA.detect(content);
    }

    /**
     * 已知文件名，获取文件类型，在某些情况下比通过字节数组准确，例如使用 jar 文件时，通过名字更为准确
     *
     * @param fileName 文件名
     * @return mineType 无法识别时会返回“application/octet-stream”
     */
    public static String getMineType(String fileName) {
        return TIKA.detect(fileName);
    }

    /**
     * 在拥有文件和数据的情况下，最好使用此方法，最为准确
     *
     * @param content  文件内容
     * @param fileName 文件名
     * @return mineType 无法识别时会返回“application/octet-stream”
     */
    public static String getMineType(byte[] content, String fileName) {
        return TIKA.detect(content, fileName);
    }
}