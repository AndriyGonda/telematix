package org.telematix.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
    public static final String COULD_NOT_READ_FILE = "Could not read file.";
    private final String imagesPath;

    public StorageService(@Value("${images.folder}") String imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String saveImage(MultipartFile multipartFile) {
        Path path = Paths.get(imagesPath);
        UUID uuid = UUID.randomUUID();
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!multipartFile.getContentType().contains("image")) throw new ServiceException("File is not image");
        String storingFilename = uuid + "." + fileExtension;
        Path filePath = path.resolve(storingFilename);
        File imageFile = getOrCreateFile(filePath.toString());
        try (OutputStream outputStream = new FileOutputStream(imageFile)) {
            outputStream.write(multipartFile.getBytes());
            return storingFilename;
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private File getOrCreateFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null && file.getParentFile().mkdirs() || file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public Resource getImageByName(String name) {
        try {
            Path path = Paths.get(imagesPath);
            Path filePath = path.resolve(name);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ServiceException(COULD_NOT_READ_FILE);
            }
        } catch (MalformedURLException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
