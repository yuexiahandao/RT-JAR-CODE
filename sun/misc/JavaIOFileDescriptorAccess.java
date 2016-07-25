package sun.misc;

import java.io.FileDescriptor;

public abstract interface JavaIOFileDescriptorAccess
{
  public abstract void set(FileDescriptor paramFileDescriptor, int paramInt);

  public abstract int get(FileDescriptor paramFileDescriptor);

  public abstract void setHandle(FileDescriptor paramFileDescriptor, long paramLong);

  public abstract long getHandle(FileDescriptor paramFileDescriptor);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JavaIOFileDescriptorAccess
 * JD-Core Version:    0.6.2
 */