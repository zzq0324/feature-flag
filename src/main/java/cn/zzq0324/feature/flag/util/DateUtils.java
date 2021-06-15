package cn.zzq0324.feature.flag.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: 日期工具类 <br>
 * date: 2021/6/14 9:26 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class DateUtils {

    public static final String FULL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date parse(String text) throws ParseException {
        // SimpleDateFormat线程不安全，每次创建
        SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATE_PATTERN);

        return dateFormat.parse(text);
    }
}
