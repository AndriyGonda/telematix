package org.telematix.api;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.telematix.services.StorageService;

@RestController
@CrossOrigin()
@RequestMapping("/api")
public class ImagesController {
    private final StorageService storageService;

    public ImagesController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(path= "/images/{name:.+}",  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable("name") String name) {
        Resource file = storageService.getImageByName(name);
        return ResponseEntity
                .ok()
                .body(file);
    }
}
