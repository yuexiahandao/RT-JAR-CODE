/*    */ package com.sun.corba.se.spi.encoding;
/*    */ 
/*    */ import com.sun.corba.se.impl.encoding.BufferManagerWrite;
/*    */ import com.sun.corba.se.impl.encoding.CDROutputStream;
/*    */ import com.sun.corba.se.pept.encoding.OutputObject;
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class CorbaOutputObject extends CDROutputStream
/*    */   implements OutputObject
/*    */ {
/*    */   public CorbaOutputObject(ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte1, boolean paramBoolean1, BufferManagerWrite paramBufferManagerWrite, byte paramByte2, boolean paramBoolean2)
/*    */   {
/* 47 */     super(paramORB, paramGIOPVersion, paramByte1, paramBoolean1, paramBufferManagerWrite, paramByte2, paramBoolean2);
/*    */   }
/*    */ 
/*    */   public abstract void writeTo(CorbaConnection paramCorbaConnection)
/*    */     throws IOException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.encoding.CorbaOutputObject
 * JD-Core Version:    0.6.2
 */