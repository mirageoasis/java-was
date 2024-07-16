package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;

public class PhotoReader {
    private final static Logger logger = LoggerUtil.getLogger();

    private final static String rootPath = System.getProperty("user.dir");
    private final static String photoPath = rootPath + "/src/main/resources/static/photo";

    public static byte[] readPhoto(String photoName) throws IOException {
        FilePath filePath = new FilePath(photoPath);
        FilePath join = filePath.join(photoName);

        File file = new File(join.getPath());
        byte[] fileContent = null;
        if (file.exists()) {
            fileContent = new byte[(int) file.length()];
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                bis.read(fileContent, 0, fileContent.length);
            }
        } else {
            return null;
        }

        return fileContent;
    }

    public static String savePhoto(String photoName, byte[] photo) throws IOException {
        FilePath filePath = new FilePath(photoPath);
        FilePath join = filePath.join(photoName);

        File file = new File(join.getPath());
        logger.info("file path: {}", file.getPath());

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            bos.write(photo, 0, photo.length);
        }

        return join.getPath();
    }
}
