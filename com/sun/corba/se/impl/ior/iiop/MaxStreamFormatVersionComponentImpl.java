/*    */ package com.sun.corba.se.impl.ior.iiop;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*    */ import com.sun.corba.se.spi.ior.TaggedComponentBase;
/*    */ import com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public class MaxStreamFormatVersionComponentImpl extends TaggedComponentBase
/*    */   implements MaxStreamFormatVersionComponent
/*    */ {
/*    */   private byte version;
/* 51 */   public static final MaxStreamFormatVersionComponentImpl singleton = new MaxStreamFormatVersionComponentImpl();
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 56 */     if (!(paramObject instanceof MaxStreamFormatVersionComponentImpl)) {
/* 57 */       return false;
/*    */     }
/* 59 */     MaxStreamFormatVersionComponentImpl localMaxStreamFormatVersionComponentImpl = (MaxStreamFormatVersionComponentImpl)paramObject;
/*    */ 
/* 62 */     return this.version == localMaxStreamFormatVersionComponentImpl.version;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 67 */     return this.version;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return "MaxStreamFormatVersionComponentImpl[version=" + this.version + "]";
/*    */   }
/*    */ 
/*    */   public MaxStreamFormatVersionComponentImpl()
/*    */   {
/* 77 */     this.version = ORBUtility.getMaxStreamFormatVersion();
/*    */   }
/*    */ 
/*    */   public MaxStreamFormatVersionComponentImpl(byte paramByte) {
/* 81 */     this.version = paramByte;
/*    */   }
/*    */ 
/*    */   public byte getMaxStreamFormatVersion()
/*    */   {
/* 86 */     return this.version;
/*    */   }
/*    */ 
/*    */   public void writeContents(OutputStream paramOutputStream)
/*    */   {
/* 91 */     paramOutputStream.write_octet(this.version);
/*    */   }
/*    */ 
/*    */   public int getId()
/*    */   {
/* 96 */     return 38;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.iiop.MaxStreamFormatVersionComponentImpl
 * JD-Core Version:    0.6.2
 */