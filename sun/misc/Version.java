package sun.misc;

import java.io.PrintStream;

/**
 * 这个类是关于版本信息的类
 */
public class Version {
    // 版本说明
    private static final String launcher_name = "java";
    private static final String java_version = "1.7.0_79";
    private static final String java_runtime_name = "Java(TM) SE Runtime Environment";
    private static final String java_runtime_version = "1.7.0_79-b15";
    private static boolean versionsInitialized = false;
    private static int jvm_major_version = 0;
    private static int jvm_minor_version = 0;
    private static int jvm_micro_version = 0;
    private static int jvm_update_version = 0;
    private static int jvm_build_number = 0;
    private static String jvm_special_version = null;
    private static int jdk_major_version = 0;
    private static int jdk_minor_version = 0;
    private static int jdk_micro_version = 0;
    private static int jdk_update_version = 0;
    private static int jdk_build_number = 0;
    private static String jdk_special_version = null;
    private static boolean jvmVersionInfoAvailable;

    // 设置版本信息
    public static void init() {
        System.setProperty("java.version", "1.7.0_79");
        System.setProperty("java.runtime.version", "1.7.0_79-b15");
        System.setProperty("java.runtime.name", "Java(TM) SE Runtime Environment");
    }

    public static void print() {
        print(System.err);
    }

    public static void println() {
        print(System.err);
        System.err.println();
    }

    public static void print(PrintStream paramPrintStream) {
        int i = 0;

        String str1 = System.getProperty("java.awt.headless");
        if ((str1 != null) && (str1.equalsIgnoreCase("true"))) {
            i = 1;
        }

        paramPrintStream.println("java version \"1.7.0_79\"");

        paramPrintStream.print("Java(TM) SE Runtime Environment (build 1.7.0_79-b15");

        if (("Java(TM) SE Runtime Environment".indexOf("Embedded") != -1) && (i != 0)) {
            paramPrintStream.print(", headless");
        }
        paramPrintStream.println(')');

        String str2 = System.getProperty("java.vm.name");
        String str3 = System.getProperty("java.vm.version");
        String str4 = System.getProperty("java.vm.info");
        paramPrintStream.println(str2 + " (build " + str3 + ", " + str4 + ")");
    }

    public static synchronized int jvmMajorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_major_version;
    }

    public static synchronized int jvmMinorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_minor_version;
    }

    public static synchronized int jvmMicroVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_micro_version;
    }

    public static synchronized int jvmUpdateVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_update_version;
    }

    public static synchronized String jvmSpecialVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        if (jvm_special_version == null) {
            jvm_special_version = getJvmSpecialVersion();
        }
        return jvm_special_version;
    }

    public static native String getJvmSpecialVersion();

    public static synchronized int jvmBuildNumber() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jvm_build_number;
    }

    public static synchronized int jdkMajorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_major_version;
    }

    public static synchronized int jdkMinorVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_minor_version;
    }

    public static synchronized int jdkMicroVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_micro_version;
    }

    public static synchronized int jdkUpdateVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_update_version;
    }

    public static synchronized String jdkSpecialVersion() {
        if (!versionsInitialized) {
            initVersions();
        }
        if (jdk_special_version == null) {
            jdk_special_version = getJdkSpecialVersion();
        }
        return jdk_special_version;
    }

    public static native String getJdkSpecialVersion();

    public static synchronized int jdkBuildNumber() {
        if (!versionsInitialized) {
            initVersions();
        }
        return jdk_build_number;
    }

    private static synchronized void initVersions() {
        if (versionsInitialized) {
            return;
        }
        jvmVersionInfoAvailable = getJvmVersionInfo();
        if (!jvmVersionInfoAvailable) {
            Object localObject = System.getProperty("java.vm.version");
            if ((((CharSequence) localObject).length() >= 5) && (Character.isDigit(((CharSequence) localObject).charAt(0))) && (((CharSequence) localObject).charAt(1) == '.') && (Character.isDigit(((CharSequence) localObject).charAt(2))) && (((CharSequence) localObject).charAt(3) == '.') && (Character.isDigit(((CharSequence) localObject).charAt(4)))) {
                jvm_major_version = Character.digit(((CharSequence) localObject).charAt(0), 10);
                jvm_minor_version = Character.digit(((CharSequence) localObject).charAt(2), 10);
                jvm_micro_version = Character.digit(((CharSequence) localObject).charAt(4), 10);
                localObject = ((CharSequence) localObject).subSequence(5, ((CharSequence) localObject).length());
                if ((((CharSequence) localObject).charAt(0) == '_') && (((CharSequence) localObject).length() >= 3) && (Character.isDigit(((CharSequence) localObject).charAt(1))) && (Character.isDigit(((CharSequence) localObject).charAt(2)))) {
                    int i = 3;
                    try {
                        String str1 = ((CharSequence) localObject).subSequence(1, 3).toString();
                        jvm_update_version = Integer.valueOf(str1).intValue();
                        if (((CharSequence) localObject).length() >= 4) {
                            char c = ((CharSequence) localObject).charAt(3);
                            if ((c >= 'a') && (c <= 'z')) {
                                jvm_special_version = Character.toString(c);
                                i++;
                            }
                        }
                    } catch (NumberFormatException localNumberFormatException) {
                        return;
                    }
                    localObject = ((CharSequence) localObject).subSequence(i, ((CharSequence) localObject).length());
                }
                if (((CharSequence) localObject).charAt(0) == '-') {
                    localObject = ((CharSequence) localObject).subSequence(1, ((CharSequence) localObject).length());
                    String[] arrayOfString1 = ((CharSequence) localObject).toString().split("-");
                    for (String str2 : arrayOfString1) {
                        if ((str2.charAt(0) == 'b') && (str2.length() == 3) && (Character.isDigit(str2.charAt(1))) && (Character.isDigit(str2.charAt(2)))) {
                            jvm_build_number = Integer.valueOf(str2.substring(1, 3)).intValue();

                            break;
                        }
                    }
                }
            }
        }
        getJdkVersionInfo();
        versionsInitialized = true;
    }

    private static native boolean getJvmVersionInfo();

    private static native void getJdkVersionInfo();

    static {
        init();
    }
}