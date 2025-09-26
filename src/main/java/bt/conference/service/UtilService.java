package bt.conference.service;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.TimeZone;

@Service
public class UtilService {
    public static Date toUtc(Date date) {
        // Get the instant in milliseconds
        long time = date.getTime();

        // Offset of default timezone in milliseconds
        TimeZone tz = TimeZone.getDefault();
        int offset = tz.getOffset(time);

        // Subtract offset to get UTC
        return new Date(time - offset);
    }
}
