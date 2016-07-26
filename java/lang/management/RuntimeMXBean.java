package java.lang.management;

import java.util.List;
import java.util.Map;

public abstract interface RuntimeMXBean extends PlatformManagedObject {
    public abstract String getName();

    public abstract String getVmName();

    public abstract String getVmVendor();

    public abstract String getVmVersion();

    public abstract String getSpecName();

    public abstract String getSpecVendor();

    public abstract String getSpecVersion();

    public abstract String getManagementSpecVersion();

    public abstract String getClassPath();

    public abstract String getLibraryPath();

    public abstract boolean isBootClassPathSupported();

    public abstract String getBootClassPath();

    public abstract List<String> getInputArguments();

    public abstract long getUptime();

    public abstract long getStartTime();

    public abstract Map<String, String> getSystemProperties();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.RuntimeMXBean
 * JD-Core Version:    0.6.2
 */