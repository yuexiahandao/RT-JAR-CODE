package sun.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * 服务类
 * 在了解这里面的原理之后，或许我们可以自己定义一些Java服务。
 */
public final class Service {
    /**
     * 默认服务防置的路径
     */
    private static final String prefix = "META-INF/services/";

    private static void fail(Class paramClass, String paramString, Throwable paramThrowable)
            throws ServiceConfigurationError {
        ServiceConfigurationError localServiceConfigurationError = new ServiceConfigurationError(paramClass.getName() + ": " + paramString);

        localServiceConfigurationError.initCause(paramThrowable);
        throw localServiceConfigurationError;
    }

    private static void fail(Class paramClass, String paramString)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
    }

    private static void fail(Class paramClass, URL paramURL, int paramInt, String paramString)
            throws ServiceConfigurationError {
        fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
    }

    /**
     * 解析一行
     * @param paramClass
     * @param paramURL
     * @param paramBufferedReader
     * @param paramInt
     * @param paramList
     * @param paramSet
     * @return
     * @throws IOException
     * @throws ServiceConfigurationError
     */
    private static int parseLine(Class paramClass, URL paramURL, BufferedReader paramBufferedReader, int paramInt, List paramList, Set paramSet)
            throws IOException, ServiceConfigurationError {
        // 读入一行，读完就退出
        String str = paramBufferedReader.readLine();
        if (str == null) {
            return -1;
        }
        // 找到#所在位置
        int i = str.indexOf('#');
        // 截取前面的字段
        if (i >= 0) str = str.substring(0, i);
        // 去除空格
        str = str.trim();
        int j = str.length();
        if (j != 0) {
            if ((str.indexOf(' ') >= 0) || (str.indexOf('\t') >= 0))
                fail(paramClass, paramURL, paramInt, "Illegal configuration-file syntax");
            // 取得0位置的Unicode字符
            int k = str.codePointAt(0);
            // 是不是java标识符的首字符
            if (!Character.isJavaIdentifierStart(k))
                fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
            for (int m = Character.charCount(k); m < j; m += Character.charCount(k)) {
                k = str.codePointAt(m);
                if ((!Character.isJavaIdentifierPart(k)) && (k != 46))
                    fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
            }
            if (!paramSet.contains(str)) {
                paramList.add(str);
                paramSet.add(str);
            }
        }
        return paramInt + 1;
    }

    /**
     *
     * @param paramClass
     * @param paramURL
     * @param paramSet
     * @return
     * @throws ServiceConfigurationError
     */
    private static Iterator parse(Class paramClass, URL paramURL, Set paramSet)
            throws ServiceConfigurationError {
        InputStream localInputStream = null;
        BufferedReader localBufferedReader = null;
        ArrayList localArrayList = new ArrayList();
        try {
            // 读取文件
            localInputStream = paramURL.openStream();
            localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "utf-8"));
            int i = 1;
            // 其实就是一些检查，然后将字符串放入localArrayList和paramSet里面
            while ((i = parseLine(paramClass, paramURL, localBufferedReader, i, localArrayList, paramSet)) >= 0) ;
        } catch (IOException localIOException2) {
            fail(paramClass, ": " + localIOException2);
        } finally {
            try {
                if (localBufferedReader != null) localBufferedReader.close();
                if (localInputStream != null) localInputStream.close();
            } catch (IOException localIOException4) {
                fail(paramClass, ": " + localIOException4);
            }

        }
        return localArrayList.iterator();
    }

    /**
     * 取得服务的提供者
     * @param paramClass 可以作为serviceDescriptor,服务的描述类，可以参考ServiceDescriptor接口
     * @param paramClassLoader 类加载器
     * @return 迭代对象
     * @throws ServiceConfigurationError
     */
    public static Iterator providers(Class paramClass, ClassLoader paramClassLoader)
            throws ServiceConfigurationError {
        return new LazyIterator(paramClass, paramClassLoader, null);
    }

    /**
     * 没有指定类加载器的话，使用当前线程的上下文加载器。
     * @param paramClass 可以作为serviceDescriptor,服务的描述类，可以参考ServiceDescriptor接口
     * @return
     * @throws ServiceConfigurationError
     */
    public static Iterator providers(Class paramClass)
            throws ServiceConfigurationError {
        ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
        return providers(paramClass, localClassLoader);
    }

    public static Iterator installedProviders(Class paramClass)
            throws ServiceConfigurationError {
        ClassLoader localClassLoader1 = ClassLoader.getSystemClassLoader();
        ClassLoader localClassLoader2 = null;
        while (localClassLoader1 != null) {
            localClassLoader2 = localClassLoader1;
            localClassLoader1 = localClassLoader1.getParent();
        }
        return providers(paramClass, localClassLoader2);
    }

    /**
     * 一个内部类的实现，懒迭代，只有真正调用的时候才去找数据，
     * 而不是直接将数据返回。
     */
    private static class LazyIterator
            implements Iterator {

        // service的class类型
        Class service;
        // 使用的类加载器
        ClassLoader loader;
        // 配置信息，这是一个数组
        Enumeration configs = null;
        //
        Iterator pending = null;
        // 返回的结果set
        Set returned = new TreeSet();
        // 下一个service的名字
        String nextName = null;

        // 构造方法
        private LazyIterator(Class paramClass, ClassLoader paramClassLoader) {
            this.service = paramClass;
            this.loader = paramClassLoader;
        }

        // 检查是不是还有下一个元素，迭代器
        public boolean hasNext() throws ServiceConfigurationError {
            // 参数不为空，就是有下一个元素
            if (this.nextName != null) {
                return true;
            }
            if (this.configs == null) {
                try {
                    // 在"META-INF/services/"+ serviceName 目录下有相应的配置，这里可以定义自己的要加载的类
                    // 重点是这些文件在哪？后来发现在jre/lib/resource.jar包中有这些文件
                    String str = "META-INF/services/" + this.service.getName();
                    // 加载目录下所有文件
                    if (this.loader == null)
                        this.configs = ClassLoader.getSystemResources(str);
                    else
                        this.configs = this.loader.getResources(str);
                } catch (IOException localIOException) {
                    Service.fail(this.service, ": " + localIOException);
                }
            }
            while ((this.pending == null) || (!this.pending.hasNext())) {
                // pending为空
                if (!this.configs.hasMoreElements()) {
                    // 没有配置信息
                    return false;
                }
                this.pending = Service.parse(this.service, (URL) this.configs.nextElement(), this.returned);
            }
            this.nextName = ((String) this.pending.next());
            return true;
        }

        // 取下一个元素
        public Object next() throws ServiceConfigurationError {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String str = this.nextName;
            this.nextName = null;
            Class localClass = null;
            try {
                // 加载对应的类
                localClass = Class.forName(str, false, this.loader);
            } catch (ClassNotFoundException localClassNotFoundException) {
                Service.fail(this.service, "Provider " + str + " not found");
            }

            // 判断service的class类型是不是localClass的（超类、父类）
            if (!this.service.isAssignableFrom(localClass)) {
                Service.fail(this.service, "Provider " + str + " not a subtype");
            }
            try {
                // 类型转换
                return this.service.cast(localClass.newInstance());
            } catch (Throwable localThrowable) {
                Service.fail(this.service, "Provider " + str + " could not be instantiated", localThrowable);
            }

            return null;
        }

        // 删除元素
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}