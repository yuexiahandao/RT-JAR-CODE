/*    */ package com.sun.corba.se.spi.servicecontext;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*    */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*    */ import org.omg.CORBA.SystemException;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class MaxStreamFormatVersionServiceContext extends ServiceContext
/*    */ {
/*    */   private byte maxStreamFormatVersion;
/* 46 */   public static final MaxStreamFormatVersionServiceContext singleton = new MaxStreamFormatVersionServiceContext();
/*    */   public static final int SERVICE_CONTEXT_ID = 17;
/*    */ 
/*    */   public MaxStreamFormatVersionServiceContext()
/*    */   {
/* 50 */     this.maxStreamFormatVersion = ORBUtility.getMaxStreamFormatVersion();
/*    */   }
/*    */ 
/*    */   public MaxStreamFormatVersionServiceContext(byte paramByte) {
/* 54 */     this.maxStreamFormatVersion = paramByte;
/*    */   }
/*    */ 
/*    */   public MaxStreamFormatVersionServiceContext(InputStream paramInputStream, GIOPVersion paramGIOPVersion)
/*    */   {
/* 59 */     super(paramInputStream, paramGIOPVersion);
/*    */ 
/* 61 */     this.maxStreamFormatVersion = paramInputStream.read_octet();
/*    */   }
/*    */ 
/*    */   public int getId() {
/* 65 */     return 17;
/*    */   }
/*    */ 
/*    */   public void writeData(OutputStream paramOutputStream) throws SystemException {
/* 69 */     paramOutputStream.write_octet(this.maxStreamFormatVersion);
/*    */   }
/*    */ 
/*    */   public byte getMaximumStreamFormatVersion()
/*    */   {
/* 74 */     return this.maxStreamFormatVersion;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 79 */     return "MaxStreamFormatVersionServiceContext[" + this.maxStreamFormatVersion + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.MaxStreamFormatVersionServiceContext
 * JD-Core Version:    0.6.2
 */