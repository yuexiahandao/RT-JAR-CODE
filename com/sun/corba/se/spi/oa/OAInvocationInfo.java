/*     */ package com.sun.corba.se.spi.oa;
/*     */ 
/*     */ import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;
/*     */ 
/*     */ public class OAInvocationInfo extends ServantObject
/*     */ {
/*     */   private Object servantContainer;
/*     */   private ObjectAdapter oa;
/*     */   private byte[] oid;
/*     */   private CookieHolder cookieHolder;
/*     */   private String operation;
/*     */   private ObjectCopierFactory factory;
/*     */ 
/*     */   public OAInvocationInfo(ObjectAdapter paramObjectAdapter, byte[] paramArrayOfByte)
/*     */   {
/*  63 */     this.oa = paramObjectAdapter;
/*  64 */     this.oid = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public OAInvocationInfo(OAInvocationInfo paramOAInvocationInfo, String paramString)
/*     */   {
/*  70 */     this.servant = paramOAInvocationInfo.servant;
/*  71 */     this.servantContainer = paramOAInvocationInfo.servantContainer;
/*  72 */     this.cookieHolder = paramOAInvocationInfo.cookieHolder;
/*  73 */     this.oa = paramOAInvocationInfo.oa;
/*  74 */     this.oid = paramOAInvocationInfo.oid;
/*  75 */     this.factory = paramOAInvocationInfo.factory;
/*     */ 
/*  77 */     this.operation = paramString;
/*     */   }
/*     */ 
/*     */   public ObjectAdapter oa() {
/*  81 */     return this.oa; } 
/*  82 */   public byte[] id() { return this.oid; } 
/*  83 */   public Object getServantContainer() { return this.servantContainer; }
/*     */ 
/*     */ 
/*     */   public CookieHolder getCookieHolder()
/*     */   {
/*  89 */     if (this.cookieHolder == null) {
/*  90 */       this.cookieHolder = new CookieHolder();
/*     */     }
/*  92 */     return this.cookieHolder;
/*     */   }
/*     */   public String getOperation() {
/*  95 */     return this.operation; } 
/*  96 */   public ObjectCopierFactory getCopierFactory() { return this.factory; }
/*     */ 
/*     */   public void setOperation(String paramString) {
/*  99 */     this.operation = paramString; } 
/* 100 */   public void setCopierFactory(ObjectCopierFactory paramObjectCopierFactory) { this.factory = paramObjectCopierFactory; }
/*     */ 
/*     */   public void setServant(Object paramObject)
/*     */   {
/* 104 */     this.servantContainer = paramObject;
/* 105 */     if ((paramObject instanceof Tie))
/* 106 */       this.servant = ((Tie)paramObject).getTarget();
/*     */     else
/* 108 */       this.servant = paramObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.oa.OAInvocationInfo
 * JD-Core Version:    0.6.2
 */