package com.yeeiee.utils;

import com.yeeiee.exception.DownloadFileException;
import com.yeeiee.system.domain.vo.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 响应对象工具
 * </p>
 *
 * @author chen
 * @since 2025-05-29
 */
public final class ResponseObjectUtil {

    /**
     * sse响应
     *
     * @param response 响应对象
     * @param result   响应结果
     * @param <T>      响应结果类型
     * @throws IOException e
     */
    public static <T> void writeStream(HttpServletResponse response, R<T> result) throws IOException {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        val writer = response.getWriter();
        writer.write("event: error\n");
        writer.write(String.format("data: %s\n\n", JsonUtil.toJsonString(result)));
    }

    /**
     * json响应
     *
     * @param response 响应对象
     * @param result   响应结果
     * @param <T>      响应结果类型
     * @throws IOException e
     */
    public static <T> void writeJson(HttpServletResponse response, R<T> result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        val writer = response.getWriter();
        writer.write(JsonUtil.toJsonString(result));
    }

    /**
     * 文件下载
     *
     * @param response 响应
     * @param filename 文件名
     * @param content  附件内容
     */
    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) {
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        // 获取 MIME 类型
        String contentType = FileTypeUtils.getMineType(content, filename);
        response.setContentType(contentType);

        try {
            val outputStream = response.getOutputStream();
            outputStream.write(content);
        } catch (IOException e) {
            throw new DownloadFileException("文件下载异常: " + ExceptionUtils.getRootCauseMessage(e));
        }
    }
}
