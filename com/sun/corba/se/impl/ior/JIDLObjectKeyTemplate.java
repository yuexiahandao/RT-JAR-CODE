/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*    */ import org.omg.CORBA.OctetSeqHolder;
/*    */ import org.omg.CORBA_2_3.portable.InputStream;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ public final class JIDLObjectKeyTemplate extends NewObjectKeyTemplateBase
/*    */ {
/*    */   public JIDLObjectKeyTemplate(ORB paramORB, int paramInt1, int paramInt2, InputStream paramInputStream)
/*    */   {
/* 51 */     super(paramORB, paramInt1, paramInt2, paramInputStream.read_long(), "", JIDL_OAID);
/*    */ 
/* 53 */     setORBVersion(paramInputStream);
/*    */   }
/*    */ 
/*    */   public JIDLObjectKeyTemplate(ORB paramORB, int paramInt1, int paramInt2, InputStream paramInputStream, OctetSeqHolder paramOctetSeqHolder)
/*    */   {
/* 62 */     super(paramORB, paramInt1, paramInt2, paramInputStream.read_long(), "", JIDL_OAID);
/*    */ 
/* 64 */     paramOctetSeqHolder.value = readObjectKey(paramInputStream);
/*    */ 
/* 66 */     setORBVersion(paramInputStream);
/*    */   }
/*    */ 
/*    */   public JIDLObjectKeyTemplate(ORB paramORB, int paramInt1, int paramInt2)
/*    */   {
/* 71 */     super(paramORB, -1347695872, paramInt1, paramInt2, "", JIDL_OAID);
/*    */ 
/* 74 */     setORBVersion(ORBVersionFactory.getORBVersion());
/*    */   }
/*    */ 
/*    */   protected void writeTemplate(OutputStream paramOutputStream)
/*    */   {
/* 79 */     paramOutputStream.write_long(getMagic());
/* 80 */     paramOutputStream.write_long(getSubcontractId());
/* 81 */     paramOutputStream.write_long(getServerId());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.JIDLObjectKeyTemplate
 * JD-Core Version:    0.6.2
 */