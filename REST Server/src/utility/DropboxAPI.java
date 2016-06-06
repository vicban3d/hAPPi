package utility;

/**
 * Created by victor on 1/19/2016.
 */


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import com.dropbox.core.v2.DbxSharing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DropboxAPI implements FileUploader {

    @Override
    public String uploadFile(String username, String targetName, String path) throws DbxException, IOException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        String access = "-B5QkYWKi2YAAAAAAAAAE7Ny2MxSaCShIU17yiMU59E2wy07Lpbm3Z6bGYI2RreK";
        DbxClientV2 client = new DbxClientV2(config, access);
        InputStream in = new FileInputStream(path);
        if (!listFolder("/Apps").contains(username)){
            client.files.createFolder("/Apps/" + username);
        }

        if (listFolder("/Apps/" + username).contains(targetName + ".apk")){
            deleteFile(username, targetName + ".apk");
        }
        client.files.uploadBuilder("/Apps/" + username + "/" + targetName + ".apk").run(in);
        DbxSharing.PathLinkMetadata link = client.sharing.createSharedLink("/Apps/" + username + "/" + targetName + ".apk");
        return link.url;
    }

    @Override
    public void deleteFile(String username, String fileName) throws DbxException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        String access = "-B5QkYWKi2YAAAAAAAAAE7Ny2MxSaCShIU17yiMU59E2wy07Lpbm3Z6bGYI2RreK";
        DbxClientV2 client = new DbxClientV2(config, access);
        if (listFolder("/Apps/" + username).contains(fileName)) {
            client.files.delete("/Apps/" + username + "/" + fileName);
        }
    }

    private ArrayList<String> listFolder(String folderName) throws DbxException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        String access = "-B5QkYWKi2YAAAAAAAAAE7Ny2MxSaCShIU17yiMU59E2wy07Lpbm3Z6bGYI2RreK";
        DbxClientV2 client = new DbxClientV2(config, access);
        ArrayList<DbxFiles.Metadata> data =  client.files.listFolder(folderName).entries;
        ArrayList<String> result = new ArrayList<>();
        for (DbxFiles.Metadata datum : data){
            result.add(datum.name);
        }
        return result;
    }
}
