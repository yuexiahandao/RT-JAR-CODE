package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;

public class FileKey {
    private long dwVolumeSerialNumber;
    private long nFileIndexHigh;
    private long nFileIndexLow;

    public static FileKey create(FileDescriptor paramFileDescriptor) {
        FileKey localFileKey = new FileKey();
        try {
            localFileKey.init(paramFileDescriptor);
        } catch (IOException localIOException) {
            throw new Error(localIOException);
        }
        return localFileKey;
    }

    public int hashCode() {
        return (int) (this.dwVolumeSerialNumber ^ this.dwVolumeSerialNumber >>> 32) + (int) (this.nFileIndexHigh ^ this.nFileIndexHigh >>> 32) + (int) (this.nFileIndexLow ^ this.nFileIndexHigh >>> 32);
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this)
            return true;
        if (!(paramObject instanceof FileKey))
            return false;
        FileKey localFileKey = (FileKey) paramObject;
        if ((this.dwVolumeSerialNumber != localFileKey.dwVolumeSerialNumber) || (this.nFileIndexHigh != localFileKey.nFileIndexHigh) || (this.nFileIndexLow != localFileKey.nFileIndexLow)) {
            return false;
        }
        return true;
    }

    private native void init(FileDescriptor paramFileDescriptor) throws IOException;

    private static native void initIDs();

    static {
        initIDs();
    }
}
