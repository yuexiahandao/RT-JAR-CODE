/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ 
/*     */ public abstract class BufferManagerWrite
/*     */ {
/*     */   protected ORB orb;
/*     */   protected ORBUtilSystemException wrapper;
/*     */   protected Object outputObject;
/* 149 */   protected boolean sentFullMessage = false;
/*     */ 
/*     */   BufferManagerWrite(ORB paramORB)
/*     */   {
/*  51 */     this.orb = paramORB;
/*  52 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*     */   }
/*     */ 
/*     */   public abstract boolean sentFragment();
/*     */ 
/*     */   public boolean sentFullMessage()
/*     */   {
/*  66 */     return this.sentFullMessage;
/*     */   }
/*     */ 
/*     */   public abstract int getBufferSize();
/*     */ 
/*     */   public abstract void overflow(ByteBufferWithInfo paramByteBufferWithInfo);
/*     */ 
/*     */   public abstract void sendMessage();
/*     */ 
/*     */   public void setOutputObject(Object paramObject)
/*     */   {
/* 135 */     this.outputObject = paramObject;
/*     */   }
/*     */ 
/*     */   public abstract void close();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.BufferManagerWrite
 * JD-Core Version:    0.6.2
 */