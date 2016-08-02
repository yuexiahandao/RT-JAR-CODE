package sun.misc;

import java.io.Console;
import java.io.FileDescriptor;
import java.net.HttpCookie;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.ProtectionDomain;
import java.util.jar.JarFile;
import java.util.zip.Adler32;
import java.util.zip.ZipFile;
import javax.security.auth.kerberos.KeyTab;

/**
 * 这是实现调用另一个包中私有方法，而无需使用反射机制信息库。
 * The code returns the enum constants by reading the class and returning the constants back
 * (without needing to do reflection calls). This is dynamic in a way that if a new enum constant is added to the enum,
 * the getValues() method will return the added enums (no need to change code all over the show).
 *
 * 可以查看：http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/sun/misc/SharedSecrets.java
 */
public class SharedSecrets {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static JavaUtilJarAccess javaUtilJarAccess;
    private static JavaLangAccess javaLangAccess;
    private static JavaIOAccess javaIOAccess;
    private static JavaNetAccess javaNetAccess;
    private static JavaNetHttpCookieAccess javaNetHttpCookieAccess;
    private static JavaNioAccess javaNioAccess;
    private static JavaIOFileDescriptorAccess javaIOFileDescriptorAccess;
    private static JavaSecurityProtectionDomainAccess javaSecurityProtectionDomainAccess;
    private static JavaSecurityAccess javaSecurityAccess;
    private static JavaxSecurityAuthKerberosAccess javaxSecurityAuthKerberosAccess;
    private static JavaUtilZipAccess javaUtilZipAccess;
    private static JavaUtilZipFileAccess javaUtilZipFileAccess;
    private static JavaAWTAccess javaAWTAccess;

    public static JavaUtilJarAccess javaUtilJarAccess() {
        if (javaUtilJarAccess == null) {
            unsafe.ensureClassInitialized(JarFile.class);
        }
        return javaUtilJarAccess;
    }

    public static void setJavaUtilJarAccess(JavaUtilJarAccess paramJavaUtilJarAccess) {
        javaUtilJarAccess = paramJavaUtilJarAccess;
    }

    public static void setJavaLangAccess(JavaLangAccess paramJavaLangAccess) {
        javaLangAccess = paramJavaLangAccess;
    }

    public static JavaLangAccess getJavaLangAccess() {
        return javaLangAccess;
    }

    public static void setJavaNetAccess(JavaNetAccess paramJavaNetAccess) {
        javaNetAccess = paramJavaNetAccess;
    }

    public static JavaNetAccess getJavaNetAccess() {
        return javaNetAccess;
    }

    public static void setJavaNetHttpCookieAccess(JavaNetHttpCookieAccess paramJavaNetHttpCookieAccess) {
        javaNetHttpCookieAccess = paramJavaNetHttpCookieAccess;
    }

    public static JavaNetHttpCookieAccess getJavaNetHttpCookieAccess() {
        if (javaNetHttpCookieAccess == null)
            unsafe.ensureClassInitialized(HttpCookie.class);
        return javaNetHttpCookieAccess;
    }

    public static void setJavaNioAccess(JavaNioAccess paramJavaNioAccess) {
        javaNioAccess = paramJavaNioAccess;
    }

    public static JavaNioAccess getJavaNioAccess() {
        if (javaNioAccess == null) {
            unsafe.ensureClassInitialized(ByteOrder.class);
        }
        return javaNioAccess;
    }

    public static void setJavaIOAccess(JavaIOAccess paramJavaIOAccess) {
        javaIOAccess = paramJavaIOAccess;
    }

    public static JavaIOAccess getJavaIOAccess() {
        if (javaIOAccess == null) {
            unsafe.ensureClassInitialized(Console.class);
        }
        return javaIOAccess;
    }

    public static void setJavaIOFileDescriptorAccess(JavaIOFileDescriptorAccess paramJavaIOFileDescriptorAccess) {
        javaIOFileDescriptorAccess = paramJavaIOFileDescriptorAccess;
    }

    public static JavaIOFileDescriptorAccess getJavaIOFileDescriptorAccess() {
        if (javaIOFileDescriptorAccess == null) {
            unsafe.ensureClassInitialized(FileDescriptor.class);
        }
        return javaIOFileDescriptorAccess;
    }

    public static void setJavaSecurityProtectionDomainAccess(JavaSecurityProtectionDomainAccess paramJavaSecurityProtectionDomainAccess) {
        javaSecurityProtectionDomainAccess = paramJavaSecurityProtectionDomainAccess;
    }

    public static JavaSecurityProtectionDomainAccess getJavaSecurityProtectionDomainAccess() {
        if (javaSecurityProtectionDomainAccess == null)
            unsafe.ensureClassInitialized(ProtectionDomain.class);
        return javaSecurityProtectionDomainAccess;
    }

    public static void setJavaSecurityAccess(JavaSecurityAccess paramJavaSecurityAccess) {
        javaSecurityAccess = paramJavaSecurityAccess;
    }

    public static JavaSecurityAccess getJavaSecurityAccess() {
        if (javaSecurityAccess == null) {
            unsafe.ensureClassInitialized(AccessController.class);
        }
        return javaSecurityAccess;
    }

    public static void setJavaxSecurityAuthKerberosAccess(JavaxSecurityAuthKerberosAccess paramJavaxSecurityAuthKerberosAccess) {
        javaxSecurityAuthKerberosAccess = paramJavaxSecurityAuthKerberosAccess;
    }

    public static JavaxSecurityAuthKerberosAccess getJavaxSecurityAuthKerberosAccess() {
        if (javaxSecurityAuthKerberosAccess == null)
            unsafe.ensureClassInitialized(KeyTab.class);
        return javaxSecurityAuthKerberosAccess;
    }

    public static void setJavaUtilZipAccess(JavaUtilZipAccess paramJavaUtilZipAccess) {
        javaUtilZipAccess = paramJavaUtilZipAccess;
    }

    public static JavaUtilZipAccess getJavaUtilZipAccess() {
        if (javaUtilZipAccess == null) {
            unsafe.ensureClassInitialized(Adler32.class);
        }
        return javaUtilZipAccess;
    }

    public static JavaUtilZipFileAccess getJavaUtilZipFileAccess() {
        if (javaUtilZipFileAccess == null)
            unsafe.ensureClassInitialized(ZipFile.class);
        return javaUtilZipFileAccess;
    }

    public static void setJavaUtilZipFileAccess(JavaUtilZipFileAccess paramJavaUtilZipFileAccess) {
        javaUtilZipFileAccess = paramJavaUtilZipFileAccess;
    }

    public static void setJavaAWTAccess(JavaAWTAccess paramJavaAWTAccess) {
        javaAWTAccess = paramJavaAWTAccess;
    }

    public static JavaAWTAccess getJavaAWTAccess() {
        return javaAWTAccess;
    }
}
