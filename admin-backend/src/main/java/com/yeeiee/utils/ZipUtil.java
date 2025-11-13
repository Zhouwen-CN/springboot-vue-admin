package com.yeeiee.utils;

import com.yeeiee.exception.IoRuntimeException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * zip工具类
 * </p>
 *
 * @author chen
 * @since 2025-09-18
 */
@Slf4j
public final class ZipUtil {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * zip压缩
     *
     * @param out   输出流
     * @param paths 文件路径列表
     * @param ins   输入流列表
     */
    public static void zip(OutputStream out, List<String> paths, List<? extends InputStream> ins) {
        if (CollectionUtils.isEmpty(paths) || CollectionUtils.isEmpty(ins)) {
            throw new IllegalArgumentException("Zip paths or ins is empty");
        }
        if (paths.size() != ins.size()) {
            throw new IllegalArgumentException("Zip paths and ins length not equal");
        }

        ZipOutputStream zip;
        if (out instanceof ZipOutputStream zipOutputStream) {
            zip = zipOutputStream;
        } else {
            zip = new ZipOutputStream(out, DEFAULT_CHARSET);
        }

        add(zip, paths, ins);

        flush(zip);
        close(zip);
    }

    /**
     * 遍历路径列表和数据流列表
     *
     * @param zip   ZipOutputStream
     * @param paths 文件路径列表
     * @param ins   输入流列表
     */
    private static void add(ZipOutputStream zip, List<String> paths, List<? extends InputStream> ins) {
        val size = paths.size();
        for (int i = 0; i < size; i++) {
            var path = paths.get(i);
            if (org.apache.commons.lang3.StringUtils.isBlank(path)) {
                log.warn("Zip path is blank");
                continue;
            }
            val in = ins.get(i);
            if (in == null) {
                // 空目录需要检查路径规范性，目录以"/"结尾
                path = org.apache.commons.lang3.StringUtils.appendIfMissing(path, "/");
            }

            putEntry(zip, path, in);
        }
    }

    /**
     * 将输入流，逐个添加到 ZipOutputStream 中
     *
     * @param zip  ZipOutputStream
     * @param path 文件路径
     * @param in   输入流
     */
    private static void putEntry(ZipOutputStream zip, String path, InputStream in) {
        try {
            zip.putNextEntry(new ZipEntry(path));
            if (in != null) {
                in.transferTo(zip);
            }
            zip.closeEntry();
        } catch (IOException e) {
            throw new IoRuntimeException("Zip compress error: " + ExceptionUtils.getRootCauseMessage(e));
        } finally {
            close(in);
        }
    }

    /**
     * 关闭流
     *
     * @param closeable Closeable
     */
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // do noting
            }
        }
    }

    /**
     * 刷写流
     *
     * @param flushable Flushable
     */
    private static void flush(Flushable flushable) {
        if (null != flushable) {
            try {
                flushable.flush();
            } catch (Exception e) {
                // do noting
            }
        }
    }
}