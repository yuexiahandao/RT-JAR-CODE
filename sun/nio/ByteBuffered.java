package sun.nio;

import java.io.IOException;
import java.nio.ByteBuffer;

// 定义的ByteBuffered接口
public abstract interface ByteBuffered {
    public abstract ByteBuffer getByteBuffer()
            throws IOException;
}