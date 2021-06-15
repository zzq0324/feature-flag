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

    // 开始时间
    @Value("${flags.$flagName.startTime}")
    private String startTime;

    // 结束时间
    @Value("${flags.$flagName.endTime}")
    private String endTime;

    // 白名单，多个以逗号隔开
    @Value("${flags.$flagName.whiteList}")
    private String whiteList;

    // 黑名单，多个以逗号隔开
    @Value("${flags.$flagName.blackList}")
    private String blackList;

    // 灰度比例
    @Value("${flags.$flagName.launchPercent}")
    private int launchPercent;

    private Date startDate;
    private Date endDate;
    private Set<String> whiteSet;
    private Set<String> blackSet;

    @Override
    protected String getDesc() {
        return desc;
    }

    @Override
    protected Set<String> getWhiteSet() {
        if (whiteSet == null) {
            whiteSet = StringUtils.commaDelimitedListToSet(whiteList);
        }

        return whiteSet;
    }

    @Override
    protected Set<String> getBlackSet() {
        if (blackSet == null) {
            blackSet = StringUtils.commaDelimitedListToSet(blackList);
        }

        return blackSet;
    }

    @Override
    public int getLaunchPercent() {
        return launchPercent;
    }

    @Override
    public Date getStartDate() {
        if (!StringUtils.isEmpty(startTime) && startDate == null) {
            startDate = parseTime(startTime);
        }

        return startDate;
    }

    @Override
    public Date getEndDate() {
        if (!StringUtils.isEmpty(endTime) && endDate == null) {
            endDate = parseTime(endTime);
        }

        return endDate;
    }
}