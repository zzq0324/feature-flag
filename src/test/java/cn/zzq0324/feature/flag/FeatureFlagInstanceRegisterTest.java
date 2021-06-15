package cn.zzq0324.feature.flag;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * description: FeatureFlagBeanRegisterTest <br>
 * date: 2021/6/11 11:16 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class FeatureFlagInstanceRegisterTest {

    private static String FLAG_NAME = "enable-open-search";

    @Test
    public void testLoadSourceTemplate() throws IOException {
        Assert.assertTrue(FeatureFlagInstanceRegister.loadSourceTemplate().contains("$flagName"));
    }

    @Test
    public void testCompileAndLoadClass() throws IOException, ClassNotFoundException {
        Class featureFlagClass = FeatureFlagInstanceRegister.compileAndLoadClass(FLAG_NAME);

        Assert.assertEquals(featureFlagClass.getName(), "cn.zzq0324.feature.flag.enable_open_search");
    }

    @Test
    public void testRegisterIfNotExist() {
        // bean不存在
        Assert.assertFalse(FeatureFlagInstanceRegister.isContainsBean(FLAG_NAME));

        // 注册bean
        FeatureFlagInstance featureFlagInstance = FeatureFlagInstanceRegister.registerIfNotExist(FLAG_NAME);

        // bean存在
        Assert.assertTrue(FeatureFlagInstanceRegister.isContainsBean(FLAG_NAME));

        Assert.assertEquals(FLAG_NAME, featureFlagInstance.getFlagName());
        Assert.assertEquals("控制是否开启开放搜索", featureFlagInstance.getDesc());

        Assert.assertEquals("enable-open-search", featureFlagInstance.getFlagName());

        Assert.assertNotNull(featureFlagInstance.getStartDate());
        Assert.assertNull(featureFlagInstance.getEndDate());

        Assert.assertEquals(95, featureFlagInstance.getLaunchPercent());

        Assert.assertTrue(featureFlagInstance.getWhiteSet().contains("u1"));
        Assert.assertTrue(featureFlagInstance.getWhiteSet().contains("u2"));
        Assert.assertTrue(featureFlagInstance.getWhiteSet().contains("u3"));
        Assert.assertFalse(featureFlagInstance.getWhiteSet().contains("u4"));

        Assert.assertTrue(featureFlagInstance.getBlackSet().size() == 0);
    }

    /**
     * 未配置属性导致的bean创建异常
     */
    @Test
    public void testRegisterNoneConfig() {
        Assert.assertThrows(BeanCreationException.class,
            () -> FeatureFlagInstanceRegister.registerIfNotExist(FLAG_NAME + "-test"));
    }

    /**
     * 测试bizId为null的场景下随机
     */
    @Test
    public void testBizIdIsNull() {
        int featureOnCount = 0;
        int featureCloseCount = 0;

        // 注册bean
        FeatureFlagInstance featureFlagInstance = FeatureFlagInstanceRegister.registerIfNotExist(FLAG_NAME);

        for (int i = 0; i < 100000; i++) {
            if (featureFlagInstance.isFeatureOn(null)) {
                featureOnCount++;
            } else {
                featureCloseCount++;
            }
        }

        // 上取整大约19倍
        Assert.assertEquals(19, Math.round(1.0 * featureOnCount / featureCloseCount));
    }
}
