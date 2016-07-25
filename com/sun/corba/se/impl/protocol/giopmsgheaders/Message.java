package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

public abstract interface Message
{
  public static final int defaultBufferSize = 1024;
  public static final int GIOPBigEndian = 0;
  public static final int GIOPLittleEndian = 1;
  public static final int GIOPBigMagic = 1195986768;
  public static final int GIOPLittleMagic = 1347373383;
  public static final int GIOPMessageHeaderLength = 12;
  public static final byte LITTLE_ENDIAN_BIT = 1;
  public static final byte MORE_FRAGMENTS_BIT = 2;
  public static final byte FLAG_NO_FRAG_BIG_ENDIAN = 0;
  public static final byte TRAILING_TWO_BIT_BYTE_MASK = 3;
  public static final byte THREAD_POOL_TO_USE_MASK = 63;
  public static final byte CDR_ENC_VERSION = 0;
  public static final byte JAVA_ENC_VERSION = 1;
  public static final byte GIOPRequest = 0;
  public static final byte GIOPReply = 1;
  public static final byte GIOPCancelRequest = 2;
  public static final byte GIOPLocateRequest = 3;
  public static final byte GIOPLocateReply = 4;
  public static final byte GIOPCloseConnection = 5;
  public static final byte GIOPMessageError = 6;
  public static final byte GIOPFragment = 7;

  public abstract GIOPVersion getGIOPVersion();

  public abstract byte getEncodingVersion();

  public abstract boolean isLittleEndian();

  public abstract boolean moreFragmentsToFollow();

  public abstract int getType();

  public abstract int getSize();

  public abstract ByteBuffer getByteBuffer();

  public abstract int getThreadPoolToUse();

  public abstract void read(InputStream paramInputStream);

  public abstract void write(OutputStream paramOutputStream);

  public abstract void setSize(ByteBuffer paramByteBuffer, int paramInt);

  public abstract FragmentMessage createFragmentMessage();

  public abstract void callback(MessageHandler paramMessageHandler)
    throws IOException;

  public abstract void setByteBuffer(ByteBuffer paramByteBuffer);

  public abstract void setEncodingVersion(byte paramByte);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.Message
 * JD-Core Version:    0.6.2
 */