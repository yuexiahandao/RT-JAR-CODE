/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*     */ import com.sun.corba.se.impl.logging.IORSystemException;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.ior.ObjectId;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBVersion;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import org.omg.CORBA.OctetSeqHolder;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public class WireObjectKeyTemplate
/*     */   implements ObjectKeyTemplate
/*     */ {
/*     */   private ORB orb;
/*     */   private IORSystemException wrapper;
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  61 */     if (paramObject == null) {
/*  62 */       return false;
/*     */     }
/*  64 */     return paramObject instanceof WireObjectKeyTemplate;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  69 */     return 53;
/*     */   }
/*     */ 
/*     */   private byte[] getId(InputStream paramInputStream)
/*     */   {
/*  75 */     CDRInputStream localCDRInputStream = (CDRInputStream)paramInputStream;
/*  76 */     int i = localCDRInputStream.getBufferLength();
/*  77 */     byte[] arrayOfByte = new byte[i];
/*  78 */     localCDRInputStream.read_octet_array(arrayOfByte, 0, i);
/*  79 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public WireObjectKeyTemplate(ORB paramORB)
/*     */   {
/*  84 */     initORB(paramORB);
/*     */   }
/*     */ 
/*     */   public WireObjectKeyTemplate(InputStream paramInputStream, OctetSeqHolder paramOctetSeqHolder)
/*     */   {
/*  89 */     paramOctetSeqHolder.value = getId(paramInputStream);
/*  90 */     initORB((ORB)paramInputStream.orb());
/*     */   }
/*     */ 
/*     */   private void initORB(ORB paramORB)
/*     */   {
/*  95 */     this.orb = paramORB;
/*  96 */     this.wrapper = IORSystemException.get(paramORB, "oa.ior");
/*     */   }
/*     */ 
/*     */   public void write(ObjectId paramObjectId, OutputStream paramOutputStream)
/*     */   {
/* 102 */     byte[] arrayOfByte = paramObjectId.getId();
/* 103 */     paramOutputStream.write_octet_array(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public void write(OutputStream paramOutputStream)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getSubcontractId()
/*     */   {
/* 113 */     return 2;
/*     */   }
/*     */ 
/*     */   public int getServerId()
/*     */   {
/* 123 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getORBId()
/*     */   {
/* 128 */     throw this.wrapper.orbIdNotAvailable();
/*     */   }
/*     */ 
/*     */   public ObjectAdapterId getObjectAdapterId()
/*     */   {
/* 133 */     throw this.wrapper.objectAdapterIdNotAvailable();
/*     */   }
/*     */ 
/*     */   public byte[] getAdapterId()
/*     */   {
/* 141 */     throw this.wrapper.adapterIdNotAvailable();
/*     */   }
/*     */ 
/*     */   public ORBVersion getORBVersion()
/*     */   {
/* 146 */     return ORBVersionFactory.getFOREIGN();
/*     */   }
/*     */ 
/*     */   public CorbaServerRequestDispatcher getServerRequestDispatcher(ORB paramORB, ObjectId paramObjectId)
/*     */   {
/* 151 */     byte[] arrayOfByte = paramObjectId.getId();
/* 152 */     String str = new String(arrayOfByte);
/* 153 */     return paramORB.getRequestDispatcherRegistry().getServerRequestDispatcher(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.WireObjectKeyTemplate
 * JD-Core Version:    0.6.2
 */