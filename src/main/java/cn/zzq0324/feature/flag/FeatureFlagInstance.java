package cn.zzq0324.feature.flag;

import cn.zzq0324.feature.flag.algorithm.FNV;
import cn.zzq0324.feature.flag.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * description: Feature flag元数据信息 <br>
 * date: 2021/6/11 1:13 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public abstract class FeatureFlagInstance {

    private static Logger logger = LoggerFactory.getLogger(FeatureFlagInstance.class);

    // 灰度开关名称
    protected String flagName;

    public FeatureFlagInstance(String flagName) {
        this.flagName = flagName;
    }

    /**
     * 获取feature flag名称
     *
     * @return
     */
    public String getFlagName() {
        return this.flagName;
    }

    /**
     * 获取feature flag描述
     *
     * @return
     */
    protected abstract String getDesc();

    /**
     * 白名单列表
     *
     * @return
     */
    protected abstract Set<String> getWhiteSet();

    /**
     * 黑名单列表
     *
     * @return
     */
    protected abstract Set<String> getBlackSet();

    /**
     * 灰度比例
     *
     * @return
     */
    public abstract int getLaunchPercent();

    /**
     * 获取灰度开始时间
     *
     * @return
     */
    public abstract long getStartTime();

    /**
     * 获取灰度结束时间
     *
     * @return
     */
    public abstract long getEndTime();

    /**
     * 判断业务id是否在灰度范围内，通过bizId计算hash值
     *
     * @param bizId 业务id，根据业务的具体场景设置，例如可以是用户id、员工id、城市等
     * @return 返回是否灰度
     */
    public boolean isFeatureOn(String bizId) {
        // 判断是否在灰度时间段，不在灰度时间段直接返回
        if (!isInTimeSection()) {
            return false;
        }

        // 判断是否在黑名单
        if (isInBlackList(bizId)) {
            return false;
        }

        // 判断是否在白名单
        if (isInWhiteList(bizId)) {
            return true;
        }

        // 判断比例，如果为0代表还未灰度
        if (getLaunchPercent() == 0) {
            return false;
        }

        // 灰度100%
        if (getLaunchPercent() == 100) {
            return true;
        }

        long hash = 0;
        // 业务id为空，由于无法计算hash值，采用随机算法
        if (bizId == null) {
            hash = ThreadLocalRandom.current().nextInt(100);
        } else {
            hash = seededHash(bizId);
        }

        // 判断是否小于灰度比例，是的话直接返回
        return hash < getLaunchPercent();
    }

    /**
     * 判断业务id是否在白名单列表
     *
     * @param bizId 业务id
     * @return 返回是否在白名单列表
     */
    protected boolean isInWhiteList(String bizId) {
        return isContain(getWhiteSet(), bizId);
    }

    /**
     * 判断业务id是否在黑名单列表
     *
     * @param bizId 业务id
     * @return 返回是否在黑名单列表
     */
    protected boolean isInBlackList(String bizId) {
        return isContain(getBlackSet(), bizId);
    }

    /**
     * 判断是否在灰度范围内
     *
     * @return 返回是否在时间段内
     */
    protected boolean isInTimeSection() {
        Date now = new Date();

        return now.getTime() >= getStartTime() && now.getTime() <= getEndTime();
    }

    protected boolean isContain(Collection<String> list, String bizId) {
        return !CollectionUtils.isEmpty(list) && list.contains(bizId);
    }

    /**
     * 根据业务id和flagName计算hash值
     *
     * @param bizId 业务id
     * @return 返回计算后小于等于100的哈希值
     */
    protected long seededHash(String bizId) {
        // FNV算法，根据flagName生成种子，确保不同的flag可以灰度到不同范围的群体
        long seed = FNV.fnv1a_32(getFlagName());
        long h = seed % 100L;

        byte[] bytes = bizId.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            int t = 0xFF & bytes[i];
            // 为什么乘以31？因此31在JVM内部做过优化，通过位异5位-1完成，比较高效
            h = (h * 31L + t) % 100L;
        }

        return h;
    }

    protected Date parseTime(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }

        try {
            return DateUtils.parse(text);
        } catch (ParseException e) {
            logger.error("parse time error", e);

            throw new RuntimeException(e);
        }
    }
}
