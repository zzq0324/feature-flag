package cn.zzq0324.feature.flag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

public class FeatureFlagInstanceImpl extends FeatureFlagInstance {

    public FeatureFlagInstanceImpl(String flagName) {
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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        // 转换成Date
        startDate = parseTime(startTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        // 转换成Date
        endDate = parseTime(endTime);
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;

        this.whiteSet = StringUtils.commaDelimitedListToSet(whiteList);
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
        // 设置成set，避免每次临时转换
        this.blackSet = StringUtils.commaDelimitedListToSet(whiteList);
    }

    public void setLaunchPercent(int launchPercent) {
        this.launchPercent = launchPercent;
    }

    @Override
    protected String getDesc() {
        return desc;
    }

    @Override
    protected Set<String> getWhiteSet() {
        return whiteSet;
    }

    @Override
    protected Set<String> getBlackSet() {
        return blackSet;
    }

    @Override
    public int getLaunchPercent() {
        return launchPercent;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }
}