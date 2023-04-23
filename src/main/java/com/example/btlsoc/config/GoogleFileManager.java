package com.example.btlsoc.config;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleFileManager {
    @Autowired
    private GoogleDriveConfig googleDriveConfig;

    // Get all file
    public List<File> listEverything() throws IOException, GeneralSecurityException {
        String folderId = "17DbX_IneEaKFgqj_eKpNq_bMSs3bXcEj";
        String query = "'" + folderId + "' in parents";
        // Print the names and IDs for up to 10 files.
        FileList result = googleDriveConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(100)
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)") // get field of google drive file
                .execute();
        return result.getFiles();
    }

    public File getSongById(String fileId) throws IOException, GeneralSecurityException {
        // Get the file with the specified ID in the specified folder.
        File file = googleDriveConfig.getInstance().files().get(fileId).execute();
        return file;
    }


    public List<File> listFolderContent(String parentId) throws IOException, GeneralSecurityException {
        if (parentId == null) {
            parentId = "root";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriveConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)") // get field of google drive folder
                .execute();
        return result.getFiles();
    }


    public String uploadFile(MultipartFile file, String type, String role) {
        try {
            String folderId = "17DbX_IneEaKFgqj_eKpNq_bMSs3bXcEj";
            if (null != file) {

                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                File uploadFile = googleDriveConfig.getInstance()
                        .files()
                        .create(fileMetadata, new InputStreamContent(
                                file.getContentType(),
                                new ByteArrayInputStream(file.getBytes()))
                        )
                        .setFields("id").execute();

                if (!type.equals("private") && !role.equals("private")){
                    // Call Set Permission drive
                    googleDriveConfig.getInstance().permissions().create(uploadFile.getId(), setPermission(type, role)).execute();
                }

                return uploadFile.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Set permission drive file
    private Permission setPermission(String type, String role){
        Permission permission = new Permission();
        permission.setType(type);
        permission.setRole(role);
        return permission;
    }
}
