/*    */ package com.sun.corba.se.spi.servicecontext;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import org.omg.CORBA.SystemException;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class UnknownServiceContext extends ServiceContext
/*    */ {
/* 69 */   private int id = -1;
/* 70 */   private byte[] data = null;
/*    */ 
/*    */   public UnknownServiceContext(int paramInt, byte[] paramArrayOfByte)
/*    */   {
/* 37 */     this.id = paramInt;
/* 38 */     this.data = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public UnknownServiceContext(int paramInt, InputStream paramInputStream)
/*    */   {
/* 43 */     this.id = paramInt;
/*    */ 
/* 45 */     int i = paramInputStream.read_long();
/* 46 */     this.data = new byte[i];
/* 47 */     paramInputStream.read_octet_array(this.data, 0, i);
/*    */   }
/*    */   public int getId() {
/* 50 */     return this.id;
/*    */   }
/*    */ 
/*    */   public void writeData(OutputStream paramOutputStream) throws SystemException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream, GIOPVersion paramGIOPVersion) throws SystemException
/*    */   {
/* 59 */     paramOutputStream.write_long(this.id);
/* 60 */     paramOutputStream.write_long(this.data.length);
/* 61 */     paramOutputStream.write_octet_array(this.data, 0, this.data.length);
/*    */   }
/*    */ 
/*    */   public byte[] getData()
/*    */   {
/* 66 */     return this.data;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.UnknownServiceContext
 * JD-Core Version:    0.6.2
 */