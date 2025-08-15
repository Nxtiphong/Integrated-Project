package tt2.int221.backend.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.utils.FileStorageProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
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

    private boolean isValidContentType(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        List<String> supportedContentTypes = Arrays.stream(fileStorageProperties.getSupportFileTypes()).toList();
        return supportedContentTypes.contains(contentType);
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (!isValidContentType(file)) {
            throw new IOException("Invalid content type.");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new IOException("Cannot upload files in directory." + fileName);
            }
            Path targetFile = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file: " + fileName, e);
        }
    }

    public Resource loadFile(String fileName) throws IOException {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new NotfoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load file: " + fileName, e);
        }
    }

    public void removeFile(String fileName)  {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            if (!Files.exists(filePath)) {
                throw new NotfoundException("File not found: " + fileName);
            }
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not remove file: " + fileName, e);
        }
    }

    public List<String> storeMultipartFile(List<MultipartFile> files) {
        List<String> result = new ArrayList<>(files.size());

        files.forEach(file -> {
            try {
                if (!isValidContentType(file)) {
                    throw new IOException("Invalid content type." + file.getOriginalFilename());
                }

                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                if (fileName.contains("..")) {
                    throw new IOException("Cannot upload files in directory." + fileName);
                }
                Path targetFile = this.fileStoragePath.resolve(fileName);
                Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

                result.add(file.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
            }
        });
        return result;
    }

    public List<String> getMatchedFiles(String pattern) {
        List<String> matchesList = new ArrayList<>();
        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
                    throws IOException {
                FileSystem fs = FileSystems.getDefault();
                PathMatcher matcher = fs.getPathMatcher("glob:" + pattern);
                Path name = file.getFileName();
                if (matcher.matches(name)) {
                    matchesList.add(name.toString());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(this.fileStoragePath, matcherVisitor);
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return matchesList;
    }
}
