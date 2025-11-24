package com.yeeiee.common.utils;

import com.yeeiee.common.exception.DownloadFileException;
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
     * 写出响应
     *
     * @param response 响应对象
     * @param result   响应体
     * @param <T>      实体类型
     * @throws IOException io 异常
     */
    public static <T> void writeResponse(HttpServletResponse response, R<T> result) throws IOException {
        val resultAsJson = JsonUtil.toJsonString(result);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(resultAsJson);
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
