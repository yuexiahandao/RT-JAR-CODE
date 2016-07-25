/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import com.sun.org.omg.CORBA.Repository;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import com.sun.org.omg.SendingContext.CodeBaseHelper;
/*     */ import com.sun.org.omg.SendingContext._CodeBaseImplBase;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class CachedCodeBase extends _CodeBaseImplBase
/*     */ {
/*     */   private Hashtable implementations;
/*     */   private Hashtable fvds;
/*     */   private Hashtable bases;
/*     */   private volatile CodeBase delegate;
/*     */   private CorbaConnection conn;
/*  60 */   private static java.lang.Object iorMapLock = new java.lang.Object();
/*  61 */   private static Hashtable<IOR, CodeBase> iorMap = new Hashtable();
/*     */ 
/*     */   public static synchronized void cleanCache(ORB paramORB) {
/*  64 */     synchronized (iorMapLock) {
/*  65 */       for (IOR localIOR : iorMap.keySet())
/*  66 */         if (localIOR.getORB() == paramORB)
/*  67 */           iorMap.remove(localIOR);
/*     */     }
/*     */   }
/*     */ 
/*     */   public CachedCodeBase(CorbaConnection paramCorbaConnection)
/*     */   {
/*  74 */     this.conn = paramCorbaConnection;
/*     */   }
/*     */ 
/*     */   public Repository get_ir() {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized String implementation(String paramString) {
/*  82 */     String str = null;
/*     */ 
/*  84 */     if (this.implementations == null)
/*  85 */       this.implementations = new Hashtable();
/*     */     else {
/*  87 */       str = (String)this.implementations.get(paramString);
/*     */     }
/*  89 */     if ((str == null) && (connectedCodeBase())) {
/*  90 */       str = this.delegate.implementation(paramString);
/*     */ 
/*  92 */       if (str != null) {
/*  93 */         this.implementations.put(paramString, str);
/*     */       }
/*     */     }
/*  96 */     return str;
/*     */   }
/*     */ 
/*     */   public synchronized String[] implementations(String[] paramArrayOfString) {
/* 100 */     String[] arrayOfString = new String[paramArrayOfString.length];
/*     */ 
/* 102 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 103 */       arrayOfString[i] = implementation(paramArrayOfString[i]);
/*     */     }
/* 105 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public synchronized FullValueDescription meta(String paramString) {
/* 109 */     FullValueDescription localFullValueDescription = null;
/*     */ 
/* 111 */     if (this.fvds == null)
/* 112 */       this.fvds = new Hashtable();
/*     */     else {
/* 114 */       localFullValueDescription = (FullValueDescription)this.fvds.get(paramString);
/*     */     }
/* 116 */     if ((localFullValueDescription == null) && (connectedCodeBase())) {
/* 117 */       localFullValueDescription = this.delegate.meta(paramString);
/*     */ 
/* 119 */       if (localFullValueDescription != null) {
/* 120 */         this.fvds.put(paramString, localFullValueDescription);
/*     */       }
/*     */     }
/* 123 */     return localFullValueDescription;
/*     */   }
/*     */ 
/*     */   public synchronized FullValueDescription[] metas(String[] paramArrayOfString) {
/* 127 */     FullValueDescription[] arrayOfFullValueDescription = new FullValueDescription[paramArrayOfString.length];
/*     */ 
/* 130 */     for (int i = 0; i < arrayOfFullValueDescription.length; i++) {
/* 131 */       arrayOfFullValueDescription[i] = meta(paramArrayOfString[i]);
/*     */     }
/* 133 */     return arrayOfFullValueDescription;
/*     */   }
/*     */ 
/*     */   public synchronized String[] bases(String paramString)
/*     */   {
/* 138 */     String[] arrayOfString = null;
/*     */ 
/* 140 */     if (this.bases == null)
/* 141 */       this.bases = new Hashtable();
/*     */     else {
/* 143 */       arrayOfString = (String[])this.bases.get(paramString);
/*     */     }
/* 145 */     if ((arrayOfString == null) && (connectedCodeBase())) {
/* 146 */       arrayOfString = this.delegate.bases(paramString);
/*     */ 
/* 148 */       if (arrayOfString != null) {
/* 149 */         this.bases.put(paramString, arrayOfString);
/*     */       }
/*     */     }
/* 152 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private synchronized boolean connectedCodeBase()
/*     */   {
/* 159 */     if (this.delegate != null) {
/* 160 */       return true;
/*     */     }
/*     */ 
/* 169 */     if (this.conn.getCodeBaseIOR() == null)
/*     */     {
/* 172 */       if (this.conn.getBroker().transportDebugFlag) {
/* 173 */         this.conn.dprint("CodeBase unavailable on connection: " + this.conn);
/*     */       }
/* 175 */       return false;
/*     */     }
/*     */ 
/* 178 */     synchronized (iorMapLock)
/*     */     {
/* 182 */       if (this.delegate != null) {
/* 183 */         return true;
/*     */       }
/*     */ 
/* 186 */       this.delegate = ((CodeBase)iorMap.get(this.conn.getCodeBaseIOR()));
/*     */ 
/* 188 */       if (this.delegate != null) {
/* 189 */         return true;
/*     */       }
/*     */ 
/* 192 */       this.delegate = CodeBaseHelper.narrow(getObjectFromIOR());
/*     */ 
/* 195 */       iorMap.put(this.conn.getCodeBaseIOR(), this.delegate);
/*     */     }
/*     */ 
/* 199 */     return true;
/*     */   }
/*     */ 
/*     */   private final org.omg.CORBA.Object getObjectFromIOR() {
/* 203 */     return CDRInputStream_1_0.internalIORToObject(this.conn.getCodeBaseIOR(), null, this.conn.getBroker());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CachedCodeBase
 * JD-Core Version:    0.6.2
 */