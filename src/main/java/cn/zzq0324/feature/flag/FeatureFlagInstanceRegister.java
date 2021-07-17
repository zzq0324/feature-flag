package cn.zzq0324.feature.flag;

import cn.zzq0324.feature.flag.spring.SpringContextHolder;
import cn.zzq0324.feature.flag.support.JdkCompiler;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * description: feature flag实例生成器 <br>
 * date: 2021/6/11 12:55 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class FeatureFlagInstanceRegister {

    private static Logger logger = LoggerFactory.getLogger(FeatureFlagInstanceRegister.class);

    // 模板类路径
    private static String FEATURE_FLAG_TPL_PATH = "generator/FeatureFlagInstance.tpl";

    /**
     * 如果flag对应的bean不存在，则生成
     *
     * @param flagName flag名称
     * @return 返回开关实例对象
     */
    public static FeatureFlagInstance registerIfNotExist(String flagName) {
        if (!isContainsBean(flagName)) {
            try {
                dynamicLoadClassAndRegisterBean(flagName);
            } catch (Exception e) {
                throw new IllegalArgumentException("dynamicLoadClassAndRegisterBean error", e);
            }
        }

        FeatureFlagInstance instance = getInstanceFromSpringContext(flagName);

        Preconditions.checkNotNull(instance);

        return instance;
    }

    /**
     * 动态生成FeatureFlag类并注册成Spring Bean
     *
     * @param flagName 开关名称
     */
    private synchronized static void dynamicLoadClassAndRegisterBean(String flagName)
        throws IOException, ClassNotFoundException {
        // 二次确认，避免并发下重复生成导致可能的冲突或者报错
        if (isContainsBean(flagName)) {
            return;
        }

        // 编译并加载class
        Class<? extends FeatureFlagInstance> beanType = compileAndLoadClass(flagName);

        // 注册Bean到Spring
        registerBean(flagName, beanType);
        logger.info("register bean for flag: {} successfully.", flagName);
    }

    protected static Class<? extends FeatureFlagInstance> compileAndLoadClass(String flagName)
        throws IOException, ClassNotFoundException {
        // 获取模板内容
        String sourceTemplate = loadSourceTemplate();
        String className = flagNameToClassName(flagName);

        // 替换模板变量
        String source = sourceTemplate.replace("$flagName", flagName);
        source = source.replace("$className", className);
        logger.info("flag: {} source as follow: \n{}", flagName, source);

        // 动态编译
        Class<? extends FeatureFlagInstance> beanType =
            JdkCompiler.compile(FeatureFlagInstance.class.getPackage().getName(), className, source);

        logger.info("compile and load class[{}] for flag: {} successfully.", beanType.getName(), flagName);

        return beanType;
    }

    private static void registerBean(String flagName, Class<? extends FeatureFlagInstance> beanType) {
        ConfigurableApplicationContext applicationContext =
            (ConfigurableApplicationContext)SpringContextHolder.getApplicationContext();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry)applicationContext.getBeanFactory();

        // 注册Bean
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanType);
        // 设置构造器参数
        beanDefinitionBuilder.addConstructorArgValue(flagName);
        beanFactory.registerBeanDefinition(flagName, beanDefinitionBuilder.getBeanDefinition());
    }

    protected static String loadSourceTemplate() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(FEATURE_FLAG_TPL_PATH);
        try (InputStream is = classPathResource.getInputStream()) {
            return StreamUtils.copyToString(is, Charset.defaultCharset());
        }
    }

    /**
     * 将特性开关名称转为类名，将横杠转为下划线
     *
     * @param flagName 开关名称
     * @return 返回类名
     */
    protected static String flagNameToClassName(String flagName) {
        flagName = flagName.replaceAll("-", "_");

        return flagName;
    }

    protected static FeatureFlagInstance getInstanceFromSpringContext(String flagName) {
        return SpringContextHolder.getApplicationContext().getBean(flagName, FeatureFlagInstance.class);
    }

    protected static boolean isContainsBean(String flagName) {
        return SpringContextHolder.getApplicationContext().containsBean(flagName);
    }
}
