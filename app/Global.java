import org.apache.commons.io.IOUtils;
import play.GlobalSettings;
import play.Logger;
import play.Play;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * Created by Ivan Kirilyuk on 24.01.15.
 */
public class Global extends GlobalSettings {

    public static final String FFMPEG_TOOLS_ROOT_DIR = "ffmpeg";

    @Override
    public void beforeStart(play.Application application) {
//        String APP_ROOT_DIR = application.path().getAbsolutePath();
//        InputStream ffprobe = this.getClass().getResourceAsStream("ffprobe");
//        InputStream ffmpeg = this.getClass().getResourceAsStream("ffmpeg");
//
//        File root = Paths.get(APP_ROOT_DIR, FFMPEG_TOOLS_ROOT_DIR).toFile();
//        if (root.mkdir()) {
//
//            //unpack ffprobe
//            File outFfprobe = Paths.get(root.getAbsolutePath(), "ffprobe").toFile();
//            try {
//                if (outFfprobe.createNewFile()) {
//                    IOUtils.copy(ffprobe, new FileOutputStream(outFfprobe));
//                    if (!outFfprobe.setExecutable(true)) {
//                        Logger.error("Cannot set ffprobe as executable");
//                    }
//                } else {
//                    Logger.error("Cannot unpack ffprobe");
//                }
//            } catch (IOException e) {
//                Logger.error("Exception while unpacking ffprobe: {}", e);
//            }
//
//            //unpack ffmpeg
//            File outFfmpeg = Paths.get(root.getAbsolutePath(), "ffmpeg").toFile();
//            try {
//                if (outFfmpeg.createNewFile()) {
//                    IOUtils.copy(ffmpeg, new FileOutputStream(outFfmpeg));
//                    if (!outFfmpeg.setExecutable(true)) {
//                        Logger.error("Cannot set ffmpeg as executable");
//                    }
//                } else {
//                    Logger.error("Cannot unpack ffmpeg");
//                }
//            } catch (IOException e) {
//                Logger.error("Exception while unpacking ffmpeg: {}", e);
//            }
//        } else {
//            Logger.error("Cannot create root dir for ffmpeg tools - {}", root.getAbsoluteFile());
//        }
//    }
    }
}
