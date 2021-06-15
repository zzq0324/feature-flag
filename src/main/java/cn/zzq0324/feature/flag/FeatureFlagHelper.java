package cn.zzq0324.feature.flag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 特性开关工具类 <br>
 * date: 2021/6/12 4:01 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class FeatureFlagHelper {

    private static Logger logger = LoggerFactory.getLogger(FeatureFlagHelper.class);

    // key-flagName, value-flag实例
    private static Map<String, FeatureFlagInstance> FLAG_MAP = new ConcurrentHashMap<>();

    /**
     * 根据bizId计算hash判断是否灰度
     *
     * @param flagName 特性开关名称
     * @param bizId    业务id，具体看使用方赋予的含义，例如用户id、员工id、城市等
     * @return 返回是否开启灰度
     */
    public static boolean isFeatureOn(String flagName, String bizId) {
        try {
            FeatureFlagInstance instance =
                FLAG_MAP.computeIfAbsent(flagName, k -> FeatureFlagInstanceRegister.registerIfNotExist(k));

            return instance.isFeatureOn(bizId);
        } catch (Exception e) {
            logger.error("flagName: {}, bizId: {} get feature flag error.", flagName, bizId, e);
        }

        // 执行错误降级，不灰度
        return false;
    }

    /**
     * 根据随机算法判断是否灰度
     *
     * @param flagName 特性开关名称
     * @return 返回是否开启灰度
     */
    public static boolean isFeatureOn(String flagName) {
        return isFeatureOn(flagName, null);
    }
}
