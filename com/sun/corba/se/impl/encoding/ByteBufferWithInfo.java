/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class ByteBufferWithInfo
/*     */ {
/*     */   private com.sun.corba.se.spi.orb.ORB orb;
/*     */   private boolean debug;
/*     */   private int index;
/*     */   public ByteBuffer byteBuffer;
/*     */   public int buflen;
/*     */   public int needed;
/*     */   public boolean fragmented;
/*     */ 
/*     */   public ByteBufferWithInfo(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/*  63 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/*  64 */     this.debug = this.orb.transportDebugFlag;
/*  65 */     this.byteBuffer = paramByteBuffer;
/*  66 */     if (paramByteBuffer != null)
/*     */     {
/*  68 */       this.buflen = paramByteBuffer.limit();
/*     */     }
/*  70 */     position(paramInt);
/*  71 */     this.needed = 0;
/*  72 */     this.fragmented = false;
/*     */   }
/*     */ 
/*     */   public ByteBufferWithInfo(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer)
/*     */   {
/*  77 */     this(paramORB, paramByteBuffer, 0);
/*     */   }
/*     */ 
/*     */   public ByteBufferWithInfo(org.omg.CORBA.ORB paramORB, BufferManagerWrite paramBufferManagerWrite)
/*     */   {
/*  83 */     this(paramORB, paramBufferManagerWrite, true);
/*     */   }
/*     */ 
/*     */   public ByteBufferWithInfo(org.omg.CORBA.ORB paramORB, BufferManagerWrite paramBufferManagerWrite, boolean paramBoolean)
/*     */   {
/*  95 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/*  96 */     this.debug = this.orb.transportDebugFlag;
/*     */ 
/*  98 */     int i = paramBufferManagerWrite.getBufferSize();
/*     */ 
/* 100 */     if (paramBoolean)
/*     */     {
/* 102 */       ByteBufferPool localByteBufferPool = this.orb.getByteBufferPool();
/* 103 */       this.byteBuffer = localByteBufferPool.getByteBuffer(i);
/*     */ 
/* 105 */       if (this.debug)
/*     */       {
/* 108 */         int j = System.identityHashCode(this.byteBuffer);
/* 109 */         StringBuffer localStringBuffer = new StringBuffer(80);
/* 110 */         localStringBuffer.append("constructor (ORB, BufferManagerWrite) - got ").append("ByteBuffer id (").append(j).append(") from ByteBufferPool.");
/*     */ 
/* 113 */         String str = localStringBuffer.toString();
/* 114 */         dprint(str);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 120 */       this.byteBuffer = ByteBuffer.allocate(i);
/*     */     }
/*     */ 
/* 123 */     position(0);
/* 124 */     this.buflen = i;
/* 125 */     this.byteBuffer.limit(this.buflen);
/* 126 */     this.needed = 0;
/* 127 */     this.fragmented = false;
/*     */   }
/*     */ 
/*     */   public ByteBufferWithInfo(ByteBufferWithInfo paramByteBufferWithInfo)
/*     */   {
/* 133 */     this.orb = paramByteBufferWithInfo.orb;
/* 134 */     this.debug = paramByteBufferWithInfo.debug;
/* 135 */     this.byteBuffer = paramByteBufferWithInfo.byteBuffer;
/* 136 */     this.buflen = paramByteBufferWithInfo.buflen;
/* 137 */     this.byteBuffer.limit(this.buflen);
/* 138 */     position(paramByteBufferWithInfo.position());
/* 139 */     this.needed = paramByteBufferWithInfo.needed;
/* 140 */     this.fragmented = paramByteBufferWithInfo.fragmented;
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 146 */     return position();
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 152 */     return this.buflen;
/*     */   }
/*     */ 
/*     */   public int position()
/*     */   {
/* 166 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void position(int paramInt)
/*     */   {
/* 176 */     this.byteBuffer.position(paramInt);
/* 177 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public void setLength(int paramInt)
/*     */   {
/* 183 */     this.buflen = paramInt;
/* 184 */     this.byteBuffer.limit(this.buflen);
/*     */   }
/*     */ 
/*     */   public void growBuffer(com.sun.corba.se.spi.orb.ORB paramORB)
/*     */   {
/* 195 */     int i = this.byteBuffer.limit() * 2;
/*     */ 
/* 197 */     while (position() + this.needed >= i) {
/* 198 */       i *= 2;
/*     */     }
/* 200 */     ByteBufferPool localByteBufferPool = paramORB.getByteBufferPool();
/* 201 */     ByteBuffer localByteBuffer = localByteBufferPool.getByteBuffer(i);
/*     */     int j;
/*     */     StringBuffer localStringBuffer;
/*     */     String str;
/* 203 */     if (this.debug)
/*     */     {
/* 206 */       j = System.identityHashCode(localByteBuffer);
/* 207 */       localStringBuffer = new StringBuffer(80);
/* 208 */       localStringBuffer.append("growBuffer() - got ByteBuffer id (");
/* 209 */       localStringBuffer.append(j).append(") from ByteBufferPool.");
/* 210 */       str = localStringBuffer.toString();
/* 211 */       dprint(str);
/*     */     }
/*     */ 
/* 214 */     this.byteBuffer.position(0);
/* 215 */     localByteBuffer.put(this.byteBuffer);
/*     */ 
/* 218 */     if (this.debug)
/*     */     {
/* 221 */       j = System.identityHashCode(this.byteBuffer);
/* 222 */       localStringBuffer = new StringBuffer(80);
/* 223 */       localStringBuffer.append("growBuffer() - releasing ByteBuffer id (");
/* 224 */       localStringBuffer.append(j).append(") to ByteBufferPool.");
/* 225 */       str = localStringBuffer.toString();
/* 226 */       dprint(str);
/*     */     }
/* 228 */     localByteBufferPool.releaseByteBuffer(this.byteBuffer);
/*     */ 
/* 231 */     this.byteBuffer = localByteBuffer;
/*     */ 
/* 234 */     this.buflen = i;
/* 235 */     this.byteBuffer.limit(this.buflen);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 240 */     StringBuffer localStringBuffer = new StringBuffer("ByteBufferWithInfo:");
/*     */ 
/* 242 */     localStringBuffer.append(" buflen = " + this.buflen);
/* 243 */     localStringBuffer.append(" byteBuffer.limit = " + this.byteBuffer.limit());
/* 244 */     localStringBuffer.append(" index = " + this.index);
/* 245 */     localStringBuffer.append(" position = " + position());
/* 246 */     localStringBuffer.append(" needed = " + this.needed);
/* 247 */     localStringBuffer.append(" byteBuffer = " + (this.byteBuffer == null ? "null" : "not null"));
/* 248 */     localStringBuffer.append(" fragmented = " + this.fragmented);
/*     */ 
/* 250 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 255 */     ORBUtility.dprint("ByteBufferWithInfo", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.ByteBufferWithInfo
 * JD-Core Version:    0.6.2
 */