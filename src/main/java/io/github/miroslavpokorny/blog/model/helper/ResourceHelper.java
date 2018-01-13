package io.github.miroslavpokorny.blog.model.helper;

import org.apache.commons.io.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ResourceHelper {
    private static final String RESOURCE_IMAGES = "/images/";

    public static String getResourceDir(String relativePath, HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath(String.format("/%s/", relativePath));
    }

    public static String getImagesResourceDir(HttpServletRequest request) {
        return getResourceDir(RESOURCE_IMAGES, request);
    }

    public static String getResourceFullPath(String relativePath, HttpServletRequest request, String resourceName) {
        return String.format("%s%s", getResourceDir(relativePath, request), resourceName);
    }

    public static boolean createDirsIfNotExist(String path) {
        File dir = new File(path);
        return !dir.exists() && dir.mkdirs();
    }

    public static boolean copyFile(MultipartFile originalFile, String fullResourcePath) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fullResourcePath)));
            FileCopyUtils.copy(originalFile.getInputStream(), stream);
            stream.close();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean removeResourceIfExist(String relativePath, HttpServletRequest request, String resourceName) {
        String fullPath = getResourceFullPath(relativePath, request, resourceName);
        File file = new File(fullPath);
        return !file.exists() || file.delete();
    }

    public static boolean removeImageResourceIfExist(HttpServletRequest request, String resourceName) {
        return removeResourceIfExist(RESOURCE_IMAGES, request, resourceName);
    }
}
