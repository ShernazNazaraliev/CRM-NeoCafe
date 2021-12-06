package com.example.NeoCafe.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.NeoCafe.entity.Menu;
import com.example.NeoCafe.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class ImageService {

    @Autowired
    private MenuRepository menuRepository;

    @Transactional
    public void setImage(MultipartFile multipartFile, Long id) throws IOException {

        final String urlKey = "ключ от cloudinary";
        File file;
        Menu menu = menuRepository.getById(id);
        file = Files.createTempFile(System.currentTimeMillis() + "",
                        multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().length() - 4)) // .jpg
                .toFile();
        multipartFile.transferTo(file);

        Cloudinary cloudinary = new Cloudinary(urlKey);
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        menu.setImagesUrl((String) uploadResult.get("url"));
        menuRepository.save(menu);
    }

}
