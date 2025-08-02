package com.laptopshop.laptopshop.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String handleSaveUploadFile(MultipartFile avatarFile, String target) {
        if (avatarFile.isEmpty())
            return ""; // trường hợp ko view ko upload file
        String finalName = "";
        try {
            byte[] bytes = avatarFile.getBytes();
            String rootPath = this.servletContext.getRealPath("/resources/images");
            File dir = new File(rootPath + File.separator + target);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Create the file on server
            finalName = System.currentTimeMillis() + "-" + avatarFile.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return finalName;
    }

    public File getFileImage(String target, String fileName) {
        File dir = new File("/images" + File.separator + target);
        File serverFile = new File(dir + File.separator + fileName);
        return serverFile;// lấy đường dẫn relative /images/avatar/ảnh
    }
}
