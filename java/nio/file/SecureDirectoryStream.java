package java.nio.file;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.Set;

public abstract interface SecureDirectoryStream<T> extends DirectoryStream<T>
{
  public abstract SecureDirectoryStream<T> newDirectoryStream(T paramT, LinkOption[] paramArrayOfLinkOption)
    throws IOException;

  public abstract SeekableByteChannel newByteChannel(T paramT, Set<? extends OpenOption> paramSet, FileAttribute<?>[] paramArrayOfFileAttribute)
    throws IOException;

  public abstract void deleteFile(T paramT)
    throws IOException;

  public abstract void deleteDirectory(T paramT)
    throws IOException;

  public abstract void move(T paramT1, SecureDirectoryStream<T> paramSecureDirectoryStream, T paramT2)
    throws IOException;

  public abstract <V extends FileAttributeView> V getFileAttributeView(Class<V> paramClass);

  public abstract <V extends FileAttributeView> V getFileAttributeView(T paramT, Class<V> paramClass, LinkOption[] paramArrayOfLinkOption);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.SecureDirectoryStream
 * JD-Core Version:    0.6.2
 */