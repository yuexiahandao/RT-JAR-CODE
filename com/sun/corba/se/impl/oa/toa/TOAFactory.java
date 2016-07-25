/*     */ package com.sun.corba.se.impl.oa.toa;
/*     */ 
/*     */ import com.sun.corba.se.impl.ior.ObjectKeyTemplateBase;
/*     */ import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TOAFactory
/*     */   implements ObjectAdapterFactory
/*     */ {
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private TOAImpl toa;
/*     */   private Map codebaseToTOA;
/*     */   private TransientObjectManager tom;
/*     */ 
/*     */   public ObjectAdapter find(ObjectAdapterId paramObjectAdapterId)
/*     */   {
/*  62 */     if (paramObjectAdapterId.equals(ObjectKeyTemplateBase.JIDL_OAID))
/*     */     {
/*  65 */       return getTOA();
/*     */     }
/*  67 */     throw this.wrapper.badToaOaid();
/*     */   }
/*     */ 
/*     */   public void init(ORB paramORB)
/*     */   {
/*  72 */     this.orb = paramORB;
/*  73 */     this.wrapper = ORBUtilSystemException.get(paramORB, "oa.lifecycle");
/*     */ 
/*  75 */     this.tom = new TransientObjectManager(paramORB);
/*  76 */     this.codebaseToTOA = new HashMap();
/*     */   }
/*     */ 
/*     */   public void shutdown(boolean paramBoolean)
/*     */   {
/*  81 */     if (Util.isInstanceDefined())
/*  82 */       Util.getInstance().unregisterTargetsForORB(this.orb);
/*     */   }
/*     */ 
/*     */   public synchronized TOA getTOA(String paramString)
/*     */   {
/*  88 */     Object localObject = (TOA)this.codebaseToTOA.get(paramString);
/*  89 */     if (localObject == null) {
/*  90 */       localObject = new TOAImpl(this.orb, this.tom, paramString);
/*     */ 
/*  92 */       this.codebaseToTOA.put(paramString, localObject);
/*     */     }
/*     */ 
/*  95 */     return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized TOA getTOA()
/*     */   {
/* 100 */     if (this.toa == null)
/*     */     {
/* 104 */       this.toa = new TOAImpl(this.orb, this.tom, null);
/*     */     }
/* 106 */     return this.toa;
/*     */   }
/*     */ 
/*     */   public ORB getORB()
/*     */   {
/* 111 */     return this.orb;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.toa.TOAFactory
 * JD-Core Version:    0.6.2
 */