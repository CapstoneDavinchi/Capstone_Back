package Capstone.Davinchi_Server.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RestController
@RequiredArgsConstructor
@RequestMapping("/GCS")
@Tag(name = "GCS 파일 업로드 테스트 API")
public class StorageController {
    //파일 업로드 테스트 Controller
    //실제 사용 X

    private final StorageService storageService;
    @PostMapping(value ="/uploads", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String upload(@RequestPart MultipartFile multipartFile) {
        return storageService.upload(multipartFile);
    }

    @PostMapping("/deletes")
    public String delete(@RequestParam String imgUrl) {
        return storageService.delete(imgUrl);
    }
}
