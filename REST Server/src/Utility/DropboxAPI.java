package Utility;

/**
 * Created by victor on 1/19/2016.
 */


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxSharing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DropboxAPI implements FileUploader {

    @Override
    public String uploadFile(String targetName, String path) throws DbxException, IOException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        String access = "-B5QkYWKi2YAAAAAAAAAE7Ny2MxSaCShIU17yiMU59E2wy07Lpbm3Z6bGYI2RreK";
        DbxClientV2 client = new DbxClientV2(config, access);
        InputStream in = new FileInputStream(path);
        client.files.uploadBuilder("/" + targetName + ".apk").run(in);
        DbxSharing.PathLinkMetadata link = client.sharing.createSharedLink("/" + targetName + ".apk");
        return link.url;
    }
}
