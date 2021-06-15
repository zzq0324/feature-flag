package cn.zzq0324.feature.flag.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * description: Spring上下文持有者 <br>
 * date: 2021/6/11 2:23 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Configuration
public class SpringContextHolder implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;

        logger.info("set applicationContext to SpringContextHolder successfully.");
    }

    /**
     * 获取spring上下文信息
     *
     * @return 返回spring上下文信息
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();

        return applicationContext;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext is null.");
        }
    }
}
