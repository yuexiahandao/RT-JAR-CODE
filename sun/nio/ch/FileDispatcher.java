package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;

abstract class FileDispatcher extends NativeDispatcher
{
  public static final int NO_LOCK = -1;
  public static final int LOCKED = 0;
  public static final int RET_EX_LOCK = 1;
  public static final int INTERRUPTED = 2;

  abstract int force(FileDescriptor paramFileDescriptor, boolean paramBoolean)
    throws IOException;

  abstract int truncate(FileDescriptor paramFileDescriptor, long paramLong)
    throws IOException;

  abstract long size(FileDescriptor paramFileDescriptor)
    throws IOException;

  abstract int lock(FileDescriptor paramFileDescriptor, boolean paramBoolean1, long paramLong1, long paramLong2, boolean paramBoolean2)
    throws IOException;

  abstract void release(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    throws IOException;

  abstract FileDescriptor duplicateForMapping(FileDescriptor paramFileDescriptor)
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.FileDispatcher
 * JD-Core Version:    0.6.2
 */