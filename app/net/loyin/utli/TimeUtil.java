package net.loyin.utli;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenjianhui on 2015/9/24.
 */
public class TimeUtil {

    public static String getTime() {
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_datetime = "";
        create_datetime = df.format(dt);
        return create_datetime;
    }
}
