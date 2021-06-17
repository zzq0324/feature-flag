package cn.zzq0324.feature.flag.mock;

import cn.zzq0324.feature.flag.FeatureFlagInstance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * description: 自定义feature flag实例，用于单元测试使用 <br>
 * date: 2021/6/11 3:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class CustomFeatureFlagInstance extends FeatureFlagInstance {

    public CustomFeatureFlagInstance(String flagName) {
        super(flagName);
    }

    @Override
    protected String getDesc() {
        return "是否开启开放搜索";
    }

    @Override
    protected Set<String> getWhiteSet() {
        Set<String> whiteList = new HashSet<>();
        whiteList.addAll(Arrays.asList("u1", "u2", "u3", "u4", "u5"));

        return whiteList;
    }

    @Override
    protected Set<String> getBlackSet() {
        Set<String> blackList = new HashSet<>();
        blackList.addAll(Arrays.asList("u11", "u12", "u13", "u14", "u15"));

        return blackList;
    }

    @Override
    public int getLaunchPercent() {
        return 0;
    }

    @Override
    public long getStartTime() {
        return 0;
    }

    @Override
    public long getEndTime() {
        return Long.MAX_VALUE;
    }
}
