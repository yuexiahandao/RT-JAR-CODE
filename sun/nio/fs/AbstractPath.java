package sun.nio.fs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.NoSuchElementException;

abstract class AbstractPath
        implements Path {
    public final boolean startsWith(String paramString) {
        return startsWith(getFileSystem().getPath(paramString, new String[0]));
    }

    public final boolean endsWith(String paramString) {
        return endsWith(getFileSystem().getPath(paramString, new String[0]));
    }

    public final Path resolve(String paramString) {
        return resolve(getFileSystem().getPath(paramString, new String[0]));
    }

    public final Path resolveSibling(Path paramPath) {
        if (paramPath == null)
            throw new NullPointerException();
        Path localPath = getParent();
        return localPath == null ? paramPath : localPath.resolve(paramPath);
    }

    public final Path resolveSibling(String paramString) {
        return resolveSibling(getFileSystem().getPath(paramString, new String[0]));
    }

    public final Iterator<Path> iterator() {
        return new Iterator() {
            private int i = 0;

            public boolean hasNext() {
                return this.i < AbstractPath.this.getNameCount();
            }

            public Path next() {
                if (this.i < AbstractPath.this.getNameCount()) {
                    Path localPath = AbstractPath.this.getName(this.i);
                    this.i += 1;
                    return localPath;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public final File toFile() {
        return new File(toString());
    }

    public final WatchKey register(WatchService paramWatchService, WatchEvent.Kind<?>[] paramArrayOfKind)
            throws IOException {
        return register(paramWatchService, paramArrayOfKind, new WatchEvent.Modifier[0]);
    }
}