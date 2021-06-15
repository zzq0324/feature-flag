package cn.zzq0324.feature.flag;

import cn.zzq0324.feature.flag.mock.CustomFeatureFlagInstance;
import org.junit.Assert;
import org.junit.Test;

/**
 * description: FeatureFlagInstanceTest <br>
 * date: 2021/6/11 4:04 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class FeatureFlagInstanceTest {

    private static final CustomFeatureFlagInstance instance = new CustomFeatureFlagInstance("enable_open_search");

    @Test
    public void testIsInTimeSection() {
        Assert.assertTrue(instance.isInTimeSection());
    }

    @Test
    public void testIsInWhiteList() {
        Assert.assertTrue(instance.isInWhiteList("u1"));

        Assert.assertFalse(instance.isInWhiteList("u111"));
    }

    @Test
    public void testIsInBlackList() {
        Assert.assertTrue(instance.isInBlackList("u11"));

        Assert.assertFalse(instance.isInBlackList("u22"));
    }

    @Test
    public void testSeededHash() {
        String bizId = "test123456";
        long hash = instance.seededHash(bizId);

        // 同样的flag和bizId，无论多少次结果应该一致
        for (int i = 0; i < 1000; i++) {
            Assert.assertEquals(hash, instance.seededHash(bizId));
        }
    }

    @Test
    public void testIsFeatureOn() {
        Assert.assertTrue(instance.isFeatureOn("u1"));
        Assert.assertFalse(instance.isFeatureOn("u11"));
        Assert.assertFalse(instance.isFeatureOn(null));
    }
}
