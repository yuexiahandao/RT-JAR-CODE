/*      */ package java.io;
/*      */ 
/*      */ import java.nio.channels.FileChannel;
/*      */ import sun.misc.IoTrace;
/*      */ import sun.nio.ch.FileChannelImpl;
/*      */ 
/*      */ public class RandomAccessFile
/*      */   implements DataOutput, DataInput, Closeable
/*      */ {
/*      */   private FileDescriptor fd;
/*   63 */   private FileChannel channel = null;
/*      */   private boolean rw;
/*      */   private final String path;
/*   69 */   private Object closeLock = new Object();
/*   70 */   private volatile boolean closed = false;
/*      */   private static final int O_RDONLY = 1;
/*      */   private static final int O_RDWR = 2;
/*      */   private static final int O_SYNC = 4;
/*      */   private static final int O_DSYNC = 8;
/*      */ 
/*      */   public RandomAccessFile(String paramString1, String paramString2)
/*      */     throws FileNotFoundException
/*      */   {
/*  122 */     this(paramString1 != null ? new File(paramString1) : null, paramString2);
/*      */   }
/*      */ 
/*      */   public RandomAccessFile(File paramFile, String paramString)
/*      */     throws FileNotFoundException
/*      */   {
/*  204 */     String str = paramFile != null ? paramFile.getPath() : null;
/*  205 */     int i = -1;
/*  206 */     if (paramString.equals("r")) {
/*  207 */       i = 1;
/*  208 */     } else if (paramString.startsWith("rw")) {
/*  209 */       i = 2;
/*  210 */       this.rw = true;
/*  211 */       if (paramString.length() > 2) {
/*  212 */         if (paramString.equals("rws"))
/*  213 */           i |= 4;
/*  214 */         else if (paramString.equals("rwd"))
/*  215 */           i |= 8;
/*      */         else
/*  217 */           i = -1;
/*      */       }
/*      */     }
/*  220 */     if (i < 0) {
/*  221 */       throw new IllegalArgumentException("Illegal mode \"" + paramString + "\" must be one of " + "\"r\", \"rw\", \"rws\"," + " or \"rwd\"");
/*      */     }
/*      */ 
/*  225 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  226 */     if (localSecurityManager != null) {
/*  227 */       localSecurityManager.checkRead(str);
/*  228 */       if (this.rw) {
/*  229 */         localSecurityManager.checkWrite(str);
/*      */       }
/*      */     }
/*  232 */     if (str == null) {
/*  233 */       throw new NullPointerException();
/*      */     }
/*  235 */     if (paramFile.isInvalid()) {
/*  236 */       throw new FileNotFoundException("Invalid file path");
/*      */     }
/*  238 */     this.fd = new FileDescriptor();
/*  239 */     this.fd.incrementAndGetUseCount();
/*  240 */     this.path = str;
/*  241 */     open(str, i);
/*      */   }
/*      */ 
/*      */   public final FileDescriptor getFD()
/*      */     throws IOException
/*      */   {
/*  253 */     if (this.fd != null) return this.fd;
/*  254 */     throw new IOException();
/*      */   }
/*      */ 
/*      */   public final FileChannel getChannel()
/*      */   {
/*  276 */     synchronized (this) {
/*  277 */       if (this.channel == null) {
/*  278 */         this.channel = FileChannelImpl.open(this.fd, this.path, true, this.rw, this);
/*      */ 
/*  289 */         this.fd.incrementAndGetUseCount();
/*      */       }
/*  291 */       return this.channel;
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void open(String paramString, int paramInt)
/*      */     throws FileNotFoundException;
/*      */ 
/*      */   public int read()
/*      */     throws IOException
/*      */   {
/*  327 */     Object localObject1 = IoTrace.fileReadBegin(this.path);
/*  328 */     int i = 0;
/*      */     try {
/*  330 */       i = read0();
/*      */     } finally {
/*  332 */       IoTrace.fileReadEnd(localObject1, i == -1 ? 0L : 1L);
/*      */     }
/*  334 */     return i;
/*      */   }
/*      */ 
/*      */   private native int read0()
/*      */     throws IOException;
/*      */ 
/*      */   private int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  347 */     Object localObject1 = IoTrace.fileReadBegin(this.path);
/*  348 */     int i = 0;
/*      */     try {
/*  350 */       i = readBytes0(paramArrayOfByte, paramInt1, paramInt2);
/*      */     } finally {
/*  352 */       IoTrace.fileReadEnd(localObject1, i == -1 ? 0L : i);
/*      */     }
/*  354 */     return i;
/*      */   }
/*      */ 
/*      */   private native int readBytes0(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */ 
/*      */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  385 */     return readBytes(paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public int read(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  408 */     return readBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public final void readFully(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  424 */     readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  442 */     int i = 0;
/*      */     do {
/*  444 */       int j = read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
/*  445 */       if (j < 0)
/*  446 */         throw new EOFException();
/*  447 */       i += j;
/*  448 */     }while (i < paramInt2);
/*      */   }
/*      */ 
/*      */   public int skipBytes(int paramInt)
/*      */     throws IOException
/*      */   {
/*  472 */     if (paramInt <= 0) {
/*  473 */       return 0;
/*      */     }
/*  475 */     long l1 = getFilePointer();
/*  476 */     long l2 = length();
/*  477 */     long l3 = l1 + paramInt;
/*  478 */     if (l3 > l2) {
/*  479 */       l3 = l2;
/*      */     }
/*  481 */     seek(l3);
/*      */ 
/*  484 */     return (int)(l3 - l1);
/*      */   }
/*      */ 
/*      */   public void write(int paramInt)
/*      */     throws IOException
/*      */   {
/*  497 */     Object localObject1 = IoTrace.fileWriteBegin(this.path);
/*  498 */     int i = 0;
/*      */     try {
/*  500 */       write0(paramInt);
/*  501 */       i = 1;
/*      */     } finally {
/*  503 */       IoTrace.fileWriteEnd(localObject1, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void write0(int paramInt)
/*      */     throws IOException;
/*      */ 
/*      */   private void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  517 */     Object localObject1 = IoTrace.fileWriteBegin(this.path);
/*  518 */     int i = 0;
/*      */     try {
/*  520 */       writeBytes0(paramArrayOfByte, paramInt1, paramInt2);
/*  521 */       i = paramInt2;
/*      */     } finally {
/*  523 */       IoTrace.fileWriteEnd(localObject1, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void writeBytes0(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */ 
/*      */   public void write(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  537 */     writeBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  550 */     writeBytes(paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public native long getFilePointer()
/*      */     throws IOException;
/*      */ 
/*      */   public native void seek(long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   public native long length()
/*      */     throws IOException;
/*      */ 
/*      */   public native void setLength(long paramLong)
/*      */     throws IOException;
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  624 */     synchronized (this.closeLock) {
/*  625 */       if (this.closed) {
/*  626 */         return;
/*      */       }
/*  628 */       this.closed = true;
/*      */     }
/*  630 */     if (this.channel != null)
/*      */     {
/*  636 */       this.fd.decrementAndGetUseCount();
/*  637 */       this.channel.close();
/*      */     }
/*      */ 
/*  644 */     this.fd.decrementAndGetUseCount();
/*  645 */     close0();
/*      */   }
/*      */ 
/*      */   public final boolean readBoolean()
/*      */     throws IOException
/*      */   {
/*  666 */     int i = read();
/*  667 */     if (i < 0)
/*  668 */       throw new EOFException();
/*  669 */     return i != 0;
/*      */   }
/*      */ 
/*      */   public final byte readByte()
/*      */     throws IOException
/*      */   {
/*  691 */     int i = read();
/*  692 */     if (i < 0)
/*  693 */       throw new EOFException();
/*  694 */     return (byte)i;
/*      */   }
/*      */ 
/*      */   public final int readUnsignedByte()
/*      */     throws IOException
/*      */   {
/*  711 */     int i = read();
/*  712 */     if (i < 0)
/*  713 */       throw new EOFException();
/*  714 */     return i;
/*      */   }
/*      */ 
/*      */   public final short readShort()
/*      */     throws IOException
/*      */   {
/*  738 */     int i = read();
/*  739 */     int j = read();
/*  740 */     if ((i | j) < 0)
/*  741 */       throw new EOFException();
/*  742 */     return (short)((i << 8) + (j << 0));
/*      */   }
/*      */ 
/*      */   public final int readUnsignedShort()
/*      */     throws IOException
/*      */   {
/*  766 */     int i = read();
/*  767 */     int j = read();
/*  768 */     if ((i | j) < 0)
/*  769 */       throw new EOFException();
/*  770 */     return (i << 8) + (j << 0);
/*      */   }
/*      */ 
/*      */   public final char readChar()
/*      */     throws IOException
/*      */   {
/*  794 */     int i = read();
/*  795 */     int j = read();
/*  796 */     if ((i | j) < 0)
/*  797 */       throw new EOFException();
/*  798 */     return (char)((i << 8) + (j << 0));
/*      */   }
/*      */ 
/*      */   public final int readInt()
/*      */     throws IOException
/*      */   {
/*  822 */     int i = read();
/*  823 */     int j = read();
/*  824 */     int k = read();
/*  825 */     int m = read();
/*  826 */     if ((i | j | k | m) < 0)
/*  827 */       throw new EOFException();
/*  828 */     return (i << 24) + (j << 16) + (k << 8) + (m << 0);
/*      */   }
/*      */ 
/*      */   public final long readLong()
/*      */     throws IOException
/*      */   {
/*  860 */     return (readInt() << 32) + (readInt() & 0xFFFFFFFF);
/*      */   }
/*      */ 
/*      */   public final float readFloat()
/*      */     throws IOException
/*      */   {
/*  883 */     return Float.intBitsToFloat(readInt());
/*      */   }
/*      */ 
/*      */   public final double readDouble()
/*      */     throws IOException
/*      */   {
/*  906 */     return Double.longBitsToDouble(readLong());
/*      */   }
/*      */ 
/*      */   public final String readLine()
/*      */     throws IOException
/*      */   {
/*  934 */     StringBuffer localStringBuffer = new StringBuffer();
/*  935 */     int i = -1;
/*  936 */     int j = 0;
/*      */ 
/*  938 */     while (j == 0) {
/*  939 */       switch (i = read()) {
/*      */       case -1:
/*      */       case 10:
/*  942 */         j = 1;
/*  943 */         break;
/*      */       case 13:
/*  945 */         j = 1;
/*  946 */         long l = getFilePointer();
/*  947 */         if (read() != 10)
/*  948 */           seek(l); break;
/*      */       default:
/*  952 */         localStringBuffer.append((char)i);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  957 */     if ((i == -1) && (localStringBuffer.length() == 0)) {
/*  958 */       return null;
/*      */     }
/*  960 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public final String readUTF()
/*      */     throws IOException
/*      */   {
/*  989 */     return DataInputStream.readUTF(this);
/*      */   }
/*      */ 
/*      */   public final void writeBoolean(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1003 */     write(paramBoolean ? 1 : 0);
/*      */   }
/*      */ 
/*      */   public final void writeByte(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1015 */     write(paramInt);
/*      */   }
/*      */ 
/*      */   public final void writeShort(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1027 */     write(paramInt >>> 8 & 0xFF);
/* 1028 */     write(paramInt >>> 0 & 0xFF);
/*      */   }
/*      */ 
/*      */   public final void writeChar(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1041 */     write(paramInt >>> 8 & 0xFF);
/* 1042 */     write(paramInt >>> 0 & 0xFF);
/*      */   }
/*      */ 
/*      */   public final void writeInt(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1054 */     write(paramInt >>> 24 & 0xFF);
/* 1055 */     write(paramInt >>> 16 & 0xFF);
/* 1056 */     write(paramInt >>> 8 & 0xFF);
/* 1057 */     write(paramInt >>> 0 & 0xFF);
/*      */   }
/*      */ 
/*      */   public final void writeLong(long paramLong)
/*      */     throws IOException
/*      */   {
/* 1069 */     write((int)(paramLong >>> 56) & 0xFF);
/* 1070 */     write((int)(paramLong >>> 48) & 0xFF);
/* 1071 */     write((int)(paramLong >>> 40) & 0xFF);
/* 1072 */     write((int)(paramLong >>> 32) & 0xFF);
/* 1073 */     write((int)(paramLong >>> 24) & 0xFF);
/* 1074 */     write((int)(paramLong >>> 16) & 0xFF);
/* 1075 */     write((int)(paramLong >>> 8) & 0xFF);
/* 1076 */     write((int)(paramLong >>> 0) & 0xFF);
/*      */   }
/*      */ 
/*      */   public final void writeFloat(float paramFloat)
/*      */     throws IOException
/*      */   {
/* 1092 */     writeInt(Float.floatToIntBits(paramFloat));
/*      */   }
/*      */ 
/*      */   public final void writeDouble(double paramDouble)
/*      */     throws IOException
/*      */   {
/* 1107 */     writeLong(Double.doubleToLongBits(paramDouble));
/*      */   }
/*      */ 
/*      */   public final void writeBytes(String paramString)
/*      */     throws IOException
/*      */   {
/* 1120 */     int i = paramString.length();
/* 1121 */     byte[] arrayOfByte = new byte[i];
/* 1122 */     paramString.getBytes(0, i, arrayOfByte, 0);
/* 1123 */     writeBytes(arrayOfByte, 0, i);
/*      */   }
/*      */ 
/*      */   public final void writeChars(String paramString)
/*      */     throws IOException
/*      */   {
/* 1137 */     int i = paramString.length();
/* 1138 */     int j = 2 * i;
/* 1139 */     byte[] arrayOfByte = new byte[j];
/* 1140 */     char[] arrayOfChar = new char[i];
/* 1141 */     paramString.getChars(0, i, arrayOfChar, 0);
/* 1142 */     int k = 0; for (int m = 0; k < i; k++) {
/* 1143 */       arrayOfByte[(m++)] = ((byte)(arrayOfChar[k] >>> '\b'));
/* 1144 */       arrayOfByte[(m++)] = ((byte)(arrayOfChar[k] >>> '\000'));
/*      */     }
/* 1146 */     writeBytes(arrayOfByte, 0, j);
/*      */   }
/*      */ 
/*      */   public final void writeUTF(String paramString)
/*      */     throws IOException
/*      */   {
/* 1166 */     DataOutputStream.writeUTF(paramString, this);
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   private native void close0() throws IOException;
/*      */ 
/*      */   static {
/* 1174 */     initIDs();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.RandomAccessFile
 * JD-Core Version:    0.6.2
 */