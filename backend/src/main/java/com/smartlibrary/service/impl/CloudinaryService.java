package com.smartlibrary.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile file) {

        try {

            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder",
                                    "smart-library/books"
                            )
                    );

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {

            throw new RuntimeException("Image upload failed");
        }
    }

    public void delete(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank())
            return;

        try {

            String publicId = imageUrl
                    .substring(imageUrl.indexOf("smart-library"))
                    .replaceFirst("\\.[^.]+$", "");

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

        } catch (Exception ignored) {
        }
    }
}