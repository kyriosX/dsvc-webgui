package validator;

import com.google.common.base.Strings;
import com.kyrioslab.jffmpegw.attributes.parser.InfoItem;
import com.kyrioslab.jffmpegw.attributes.parser.MultimediaInfo;
import com.kyrioslab.jffmpegw.attributes.parser.StreamInfo;
import org.apache.commons.lang3.math.NumberUtils;
import views.formdata.VideoConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by Ivan Kirilyuk on 18.01.15.
 *
 */
public class Validator {

    private static final String VIDEO_STREAM_TYPE = "video";
    private static final String AUDIO_STREAM_TYPE = "audio";

    public static List<ValidationException> validate(MultimediaInfo videoInfo, VideoConfig config) {
        List<ValidationException> exceptionList = new ArrayList<>();
        List<StreamInfo> streamList = config.getStreams();
        for (StreamInfo s : streamList) {

            String codec = s.getCodecName();
            String pixelFormat = s.getPixFmt();
            String bitRate = s.getBitRate();
            String width = s.getWidth();
            String height = s.getHeight();
            String frameRate = s.getAvgFrameRate();
            String aspect = s.getDisplayAspectRatio();

            String channels = s.getChannels();
            String samplingRate = s.getSampleRate();
            String volume = s.getVolume();


            if (s.getCodecType().equals(VIDEO_STREAM_TYPE)) {
                if (!Strings.isNullOrEmpty(codec)) {
                    if (!isSupported(codec, videoInfo.getFullSupportedVideoCodecs())) {
                        exceptionList.add(
                                new ValidationException("codec_name", "Video codec: " +
                                        codec + " is not supported.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(pixelFormat)) {
                    if (!isSupported(pixelFormat, videoInfo.getFullSupportedPixFormats())) {
                        exceptionList.add(
                                new ValidationException("codec_name", "Pixel format: " +
                                        pixelFormat + " is not supported.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(bitRate)) {
                    if (!NumberUtils.isNumber(bitRate)) {
                        exceptionList.add(
                                new ValidationException("bit_rate", "Video bit rate must be numeric value.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(width)) {
                    if (!NumberUtils.isNumber(width)) {
                        exceptionList.add(
                                new ValidationException("width", "Video width must be numeric value.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(height)) {
                    if (!NumberUtils.isNumber(height)) {
                        exceptionList.add(
                                new ValidationException("height", "Video height must be numeric value.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(frameRate)) {
                    if (!NumberUtils.isNumber(frameRate)) {
                        exceptionList.add(
                                new ValidationException("frame_rate", "Frame rate must be numeric value.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(aspect)) {
                    StringTokenizer t = new StringTokenizer(aspect, ":");
                    try {
                        String w = t.nextToken();
                        String h = t.nextToken();
                        if (!NumberUtils.isNumber(w) || !NumberUtils.isNumber(h)) {
                            exceptionList.add(
                                    new ValidationException("display_aspect_ratio", "Display aspect ratio is not valid.")
                            );
                        }
                    } catch (NoSuchElementException e) {
                        exceptionList.add(
                                new ValidationException("display_aspect_ratio", "Display aspect ratio is not valid.")
                        );
                    }
                }
            } else if (s.getCodecType().equals(AUDIO_STREAM_TYPE)) {
                if (!Strings.isNullOrEmpty(codec)) {
                    if (!isSupported(codec, videoInfo.getFullSupportedAudioCodecs())) {
                        exceptionList.add(
                                new ValidationException("codec_name", "Audio codec: " +
                                        codec + " is not supported.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(bitRate)) {
                    if (!NumberUtils.isNumber(bitRate)) {
                        exceptionList.add(
                                new ValidationException("bit_rate", "Audio bit rate must be numeric value.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(channels)) {
                    if (!NumberUtils.isNumber(channels)) {
                        exceptionList.add(
                                new ValidationException("channels", "Channels count must be numeric value.")
                        );
                    }
                }
                if (!Strings.isNullOrEmpty(samplingRate)) {
                    if (!NumberUtils.isNumber(samplingRate)) {
                        exceptionList.add(
                                new ValidationException("sampling_rate", "Sampling rate must be numeric value.")
                        );
                    }
                }if (!Strings.isNullOrEmpty(volume)) {
                    if (!NumberUtils.isNumber(volume)) {
                        exceptionList.add(
                                new ValidationException("volume", "Volume must be numeric value.")
                        );
                    }
                }
            }
        }
        return exceptionList;
    }

    private static boolean isSupported(String tested, List<InfoItem> supported) {
        for (InfoItem suppItem : supported) {
            if (suppItem.getName().equals(tested.trim())) {
                return true;
            }
        }
        return false;
    }
}
