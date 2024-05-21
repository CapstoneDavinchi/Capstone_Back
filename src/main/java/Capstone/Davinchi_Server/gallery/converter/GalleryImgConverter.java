package Capstone.Davinchi_Server.gallery.converter;

import Capstone.Davinchi_Server.gallery.entity.GalleryPost;
import Capstone.Davinchi_Server.gallery.entity.GalleryPostImg;

public class GalleryImgConverter {
    public static GalleryPostImg toGalleryImg(String originalFilename, String imgUrl, GalleryPost galleryPost){
        return GalleryPostImg.builder()
                .originalFilename(originalFilename)
                .imageUrl(imgUrl)
                .galleryPost(galleryPost)
                .build();
    }
}
