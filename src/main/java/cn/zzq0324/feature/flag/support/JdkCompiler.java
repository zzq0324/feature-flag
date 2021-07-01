package cn.zzq0324.feature.flag.support;

import com.taobao.arthas.compiler.DynamicCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * description: JdkCompiler <br>
 * date: 2021/6/11 10:31 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class JdkCompiler {

    private static final Logger logger = LoggerFactory.getLogger(JdkCompiler.class);

    public static final String JAVA_FILE_SUFFIX = ".java";

    /**
     * 动态编译并返回Class
     *
     * @param classPackage    类的包路径
     * @param classSimpleName 类名
     * @param source          代码
     * @return 返回编译后的Class
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> Class<? extends T> compile(String classPackage, String classSimpleName, String source) {
        DynamicCompiler dynamicCompiler = new DynamicCompiler(Thread.currentThread().getContextClassLoader());

        String className = classPackage + "." + classSimpleName;
        dynamicCompiler.addSource(className, source);

        Map<String, Class<?>> classMap = dynamicCompiler.build();

        // 成功编译，未报错
        if (CollectionUtils.isEmpty(dynamicCompiler.getErrors())) {
            return (Class<? extends T>)classMap.get(className);
        }

        logger.error("Compile class: {} error, error info: {}", className,
            Arrays.toString(dynamicCompiler.getErrors().toArray()));

        throw new IllegalStateException("Compile error");
    }
}
