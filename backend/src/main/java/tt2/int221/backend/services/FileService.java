package tt2.int221.backend.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.utils.FileStorageProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Service
public class FileService {
    private final Path fileStoragePath;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileStoragePath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            if (!Files.exists(this.fileStoragePath)) {
                Files.createDirectories(this.fileStoragePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory where the upload files will be uploaded.", e);
        }
    }

    private boolean isValidContentType(MultipartFile file) {
        String contentType = file.getContentType();
        List<String> supportedContentTypes = Arrays.stream(fileStorageProperties.getSupportFileTypes()).toList();
        return supportedContentTypes.contains(contentType);
    }

    public String saveFile(MultipartFile file, String fileName, Integer saleItemId) throws IOException {
        if (!isValidContentType(file)) {
            throw new IOException("Invalid content type: " + file.getOriginalFilename());
        }

        if (fileName.contains("..")) {
            throw new IOException("Invalid file name: " + fileName);
        }

        try {
            Path saleItemDir = this.fileStoragePath.resolve(String.valueOf(saleItemId));
            Files.createDirectories(saleItemDir);

            String baseName = fileName;
            String extension = "";
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0) {
                baseName = fileName.substring(0, dotIndex);
                extension = fileName.substring(dotIndex);
            }

            Path targetFile = saleItemDir.resolve(fileName);
            int counter = 1;

            while (Files.exists(targetFile)) {
                String newFileName = baseName + "(" + counter + ")" + extension;
                targetFile = saleItemDir.resolve(newFileName);
                counter++;
            }

            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            return saleItemId + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file: " + fileName, e);
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new NotfoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load file: " + fileName, e);
        }
    }

    public void removeFile(String fileName, Integer saleItemId) {
        try {
            Path saleItemDir = this.fileStoragePath.resolve(String.valueOf(saleItemId));

            Path filePath = saleItemDir.resolve(fileName).normalize();

            if (!Files.exists(filePath)) {
                throw new NotfoundException("File not found: " + saleItemId + "/" + fileName);
            }
            Files.delete(filePath);

            if (Files.isDirectory(saleItemDir) && Files.list(saleItemDir).findAny().isEmpty()) {
                Files.delete(saleItemDir); // delete dir if empty
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not remove file: " + saleItemId + "/" + fileName, e);
        }
    }

    public String updateImage(String oldFileName, MultipartFile newFile, Integer saleItemId) throws IOException {
        if (oldFileName != null && !oldFileName.isBlank()) {
            Path oldFilePath = this.fileStoragePath.resolve(oldFileName).normalize();
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }
        }
        return saveFile(newFile, newFile.getOriginalFilename(), saleItemId);
    }

    public String buildCustomFileName(Integer saleItemId, Integer viewOrder, MultipartFile file) {
        String extension = "";
        String originalName = file.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        return saleItemId + "." + viewOrder + extension;
    }

    public String buildCustomFileName(Integer saleItemId, Integer viewOrder, String existingFileName) {
        String extension = "";
        if (existingFileName != null && existingFileName.contains(".")) {
            extension = existingFileName.substring(existingFileName.lastIndexOf("."));
        }
        return saleItemId + "." + viewOrder + extension;
    }
}
