/*    */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import java.io.IOException;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class CancelRequestMessage_1_2 extends Message_1_1
/*    */   implements CancelRequestMessage
/*    */ {
/* 41 */   private int request_id = 0;
/*    */ 
/*    */   CancelRequestMessage_1_2()
/*    */   {
/*    */   }
/*    */ 
/*    */   CancelRequestMessage_1_2(int paramInt) {
/* 48 */     super(1195986768, GIOPVersion.V1_2, (byte)0, (byte)2, 4);
/*    */ 
/* 50 */     this.request_id = paramInt;
/*    */   }
/*    */ 
/*    */   public int getRequestId()
/*    */   {
/* 56 */     return this.request_id;
/*    */   }
/*    */ 
/*    */   public void read(InputStream paramInputStream)
/*    */   {
/* 62 */     super.read(paramInputStream);
/* 63 */     this.request_id = paramInputStream.read_ulong();
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream) {
/* 67 */     super.write(paramOutputStream);
/* 68 */     paramOutputStream.write_ulong(this.request_id);
/*    */   }
/*    */ 
/*    */   public void callback(MessageHandler paramMessageHandler)
/*    */     throws IOException
/*    */   {
/* 74 */     paramMessageHandler.handleInput(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.CancelRequestMessage_1_2
 * JD-Core Version:    0.6.2
 */