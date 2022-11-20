package pl.wixatech.hackyeahbackend.upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.out;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {
    
    private final DocumentService documentService;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public UploadFileResponse uploadFile(@RequestPart("file") MultipartFile file) {
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            File fileToSave = new File(file.getOriginalFilename());
            copyInputStreamToFileJava9(inputStream, fileToSave);
            documentService.saveDocument(null, fileToSave.getAbsolutePath());
        } catch (IOException e) {
            out.println(e.getMessage());
            return new UploadFileResponse(false);
        }
        return new UploadFileResponse(true);
    }
    private static void copyInputStreamToFileJava9(InputStream input, File file)
            throws IOException {

        // append = false
        try (OutputStream output = new FileOutputStream(file, false)) {
            input.transferTo(output);
        }

    }

    @GetMapping(path = "/document/{id}")
    public String getFile(@PathVariable Long id) {
        Document byId = documentService.getById(id);
        File file = new File(byId.getFilePath());
        return file.getName();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> getTaskInstructionContent(@PathVariable Long id) throws IOException {
        Document byId = documentService.getById(id);
        File file = new File(byId.getFilePath());
        Path path = Paths.get(byId.getFilePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
