package cn.zzq0324.feature.flag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

public class $className extends FeatureFlagInstance {

    public $className(String flagName) {
        super(flagName);
    }

    // flag描述
    @Value("${flags.$flagName.desc}")
    private String desc;

    // 灰度比例
    @Value("${flags.$flagName.launchPercent}")
    private int launchPercent;

    private long startTime;
    private long endTime;
    private Set<String> whiteSet;
    private Set<String> blackSet;

    @Override
    protected String getDesc() {
        return desc;
    }

    @Override
    protected Set<String> getWhiteSet() {
        return whiteSet;
    }

    // 允许名单，多个以逗号隔开
    @Value("${flags.$flagName.whiteList}")
    public void setAllowSet(String value) {
        whiteSet = StringUtils.commaDelimitedListToSet(value);
    }

    @Override
    protected Set<String> getBlackSet() {
        return blackSet;
    }

    // 阻止名单，多个以逗号隔开
    @Value("${flags.$flagName.blackList}")
    public void setBlockSet(String value) {
        blackSet = StringUtils.commaDelimitedListToSet(value);
    }

    @Override
    public int getLaunchPercent() {
        return launchPercent;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    // 开始时间
    @Value("${flags.$flagName.startTime}")
    public void setStartTime(String value) {
        if (!StringUtils.isEmpty(value)) {
            startTime = parseTime(value).getTime();
        } else {
            startTime = 0;
        }
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    // 结束时间
    @Value("${flags.$flagName.endTime}")
    public void setEndTime(String value) {
        if (!StringUtils.isEmpty(value)) {
            endTime = parseTime(value).getTime();
        } else {
            endTime = Long.MAX_VALUE;
        }
    }
}