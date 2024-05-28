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

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StorageService {


    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String upload(MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String ext = multipartFile.getContentType();
        Storage storage = null;

        try{
            InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();

            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();
        }catch (IOException e){
            throw new ApiException(ApiResponseStatus.CREATE_STORAGE_FAIL);
        }
        String imgUrl = "https://storage.googleapis.com/" + bucketName + "/" + uuid;
        try{
            if (multipartFile.isEmpty()) {
                imgUrl = null;
            } else {
                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext).build();

                Blob blob = storage.create(blobInfo, multipartFile.getInputStream());
            }
        }catch (IOException e){
            throw new ApiException(ApiResponseStatus.IMAGE_UPLOAD_FAIL);
        }

        return imgUrl;
    }
}
