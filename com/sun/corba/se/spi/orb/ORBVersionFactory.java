/*     */ package com.sun.corba.se.spi.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.orb.ORBVersionImpl;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ 
/*     */ public class ORBVersionFactory
/*     */ {
/*     */   public static ORBVersion getFOREIGN()
/*     */   {
/*  38 */     return ORBVersionImpl.FOREIGN;
/*     */   }
/*     */ 
/*     */   public static ORBVersion getOLD()
/*     */   {
/*  43 */     return ORBVersionImpl.OLD;
/*     */   }
/*     */ 
/*     */   public static ORBVersion getNEW()
/*     */   {
/*  48 */     return ORBVersionImpl.NEW;
/*     */   }
/*     */ 
/*     */   public static ORBVersion getJDK1_3_1_01()
/*     */   {
/*  53 */     return ORBVersionImpl.JDK1_3_1_01;
/*     */   }
/*     */ 
/*     */   public static ORBVersion getNEWER()
/*     */   {
/*  58 */     return ORBVersionImpl.NEWER;
/*     */   }
/*     */ 
/*     */   public static ORBVersion getPEORB()
/*     */   {
/*  63 */     return ORBVersionImpl.PEORB;
/*     */   }
/*     */ 
/*     */   public static ORBVersion getORBVersion()
/*     */   {
/*  70 */     return ORBVersionImpl.PEORB;
/*     */   }
/*     */ 
/*     */   public static ORBVersion create(InputStream paramInputStream)
/*     */   {
/*  75 */     byte b = paramInputStream.read_octet();
/*  76 */     return byteToVersion(b);
/*     */   }
/*     */ 
/*     */   private static ORBVersion byteToVersion(byte paramByte)
/*     */   {
/* 103 */     switch (paramByte) { case 0:
/* 104 */       return ORBVersionImpl.FOREIGN;
/*     */     case 1:
/* 105 */       return ORBVersionImpl.OLD;
/*     */     case 2:
/* 106 */       return ORBVersionImpl.NEW;
/*     */     case 3:
/* 107 */       return ORBVersionImpl.JDK1_3_1_01;
/*     */     case 10:
/* 108 */       return ORBVersionImpl.NEWER;
/*     */     case 20:
/* 109 */       return ORBVersionImpl.PEORB; }
/* 110 */     return new ORBVersionImpl(paramByte);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ORBVersionFactory
 * JD-Core Version:    0.6.2
 */