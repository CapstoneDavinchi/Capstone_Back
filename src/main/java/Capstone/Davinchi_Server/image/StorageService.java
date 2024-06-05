package Capstone.Davinchi_Server.image;

import Capstone.Davinchi_Server.global.exception.ApiException;
import Capstone.Davinchi_Server.global.exception.ApiResponseStatus;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StorageService {


    @Value("${spring.cloud.gcp.storage.credentials.encoded-key}")
    private String encodedKeyFile;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String upload(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String ext = multipartFile.getContentType();
        Storage storage = null;
        String originalFilename = multipartFile.getOriginalFilename();
        String imgUrl = "https://storage.googleapis.com/" + bucketName + "/" + uuid + "/" + originalFilename;
        try{
            if (multipartFile.isEmpty()) {
                imgUrl = null;
            } else {
                byte[] decodedKey = Base64.getDecoder().decode(encodedKeyFile);
                ByteArrayInputStream keyFileStream = new ByteArrayInputStream(decodedKey);

                storage = StorageOptions.newBuilder()
                        .setCredentials(GoogleCredentials.fromStream(keyFileStream))
                        .build()
                        .getService();

                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid + "/" + originalFilename)
                        .setContentType(ext).build();

                Blob blob = storage.create(blobInfo, multipartFile.getInputStream());
            }
        }catch (IOException e){
            throw new ApiException(ApiResponseStatus.IMAGE_UPLOAD_FAIL);
        }

        return imgUrl;
    }

    public String delete(String imgUrl) {
        Storage storage = null;
        try {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKeyFile);
            ByteArrayInputStream keyFileStream = new ByteArrayInputStream(decodedKey);

            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFileStream))
                    .build()
                    .getService();

            storage.delete(bucketName, imgUrl);
        } catch (IOException e) {
            throw new ApiException(ApiResponseStatus.IMAGE_UPLOAD_FAIL);
        }
        return imgUrl;

    }

}
