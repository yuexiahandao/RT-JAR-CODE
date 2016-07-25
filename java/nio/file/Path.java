package java.nio.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

public abstract interface Path extends Comparable<Path>, Iterable<Path>, Watchable
{
  public abstract FileSystem getFileSystem();

  public abstract boolean isAbsolute();

  public abstract Path getRoot();

  public abstract Path getFileName();

  public abstract Path getParent();

  public abstract int getNameCount();

  public abstract Path getName(int paramInt);

  public abstract Path subpath(int paramInt1, int paramInt2);

  public abstract boolean startsWith(Path paramPath);

  public abstract boolean startsWith(String paramString);

  public abstract boolean endsWith(Path paramPath);

  public abstract boolean endsWith(String paramString);

  public abstract Path normalize();

  public abstract Path resolve(Path paramPath);

  public abstract Path resolve(String paramString);

  public abstract Path resolveSibling(Path paramPath);

  public abstract Path resolveSibling(String paramString);

  public abstract Path relativize(Path paramPath);

  public abstract URI toUri();

  public abstract Path toAbsolutePath();

  public abstract Path toRealPath(LinkOption[] paramArrayOfLinkOption)
    throws IOException;

  public abstract File toFile();

  public abstract WatchKey register(WatchService paramWatchService, WatchEvent.Kind<?>[] paramArrayOfKind, WatchEvent.Modifier[] paramArrayOfModifier)
    throws IOException;

  public abstract WatchKey register(WatchService paramWatchService, WatchEvent.Kind<?>[] paramArrayOfKind)
    throws IOException;

  public abstract Iterator<Path> iterator();

  public abstract int compareTo(Path paramPath);

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();

  public abstract String toString();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.Path
 * JD-Core Version:    0.6.2
 */