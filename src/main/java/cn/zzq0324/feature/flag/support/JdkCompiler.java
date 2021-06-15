package cn.zzq0324.feature.flag.support;

import com.itranswarp.compiler.JavaStringCompiler;

import java.io.IOException;
import java.util.Map;

/**
 * description: JdkCompiler <br>
 * date: 2021/6/11 10:31 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public class JdkCompiler {

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
    public static <T> Class<? extends T> compile(String classPackage, String classSimpleName, String source)
        throws IOException, ClassNotFoundException {
        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> compileResult = compiler.compile(classSimpleName + JAVA_FILE_SUFFIX, source);

        return (Class<T>)compiler.loadClass(classPackage + "." + classSimpleName, compileResult);
    }
}
