/*     */ package com.sun.corba.se.impl.naming.pcosnaming;
/*     */ 
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.PortableServer.ForwardRequest;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.Servant;
/*     */ import org.omg.PortableServer.ServantLocator;
/*     */ import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;
/*     */ 
/*     */ public class ServantManagerImpl extends LocalObject
/*     */   implements ServantLocator
/*     */ {
/*     */   private static final long serialVersionUID = 4028710359865748280L;
/*     */   private ORB orb;
/*     */   private NameService theNameService;
/*     */   private File logDir;
/*     */   private Hashtable contexts;
/*     */   private CounterDB counterDb;
/*     */   private int counter;
/*     */   private static final String objKeyPrefix = "NC";
/*     */ 
/*     */   ServantManagerImpl(ORB paramORB, File paramFile, NameService paramNameService)
/*     */   {
/*  78 */     this.logDir = paramFile;
/*  79 */     this.orb = paramORB;
/*     */ 
/*  81 */     this.counterDb = new CounterDB(paramFile);
/*  82 */     this.contexts = new Hashtable();
/*  83 */     this.theNameService = paramNameService;
/*     */   }
/*     */ 
/*     */   public Servant preinvoke(byte[] paramArrayOfByte, POA paramPOA, String paramString, CookieHolder paramCookieHolder)
/*     */     throws ForwardRequest
/*     */   {
/*  91 */     String str = new String(paramArrayOfByte);
/*     */ 
/*  93 */     Object localObject = (Servant)this.contexts.get(str);
/*     */ 
/*  95 */     if (localObject == null)
/*     */     {
/*  97 */       localObject = readInContext(str);
/*     */     }
/*     */ 
/* 100 */     return localObject;
/*     */   }
/*     */ 
/*     */   public void postinvoke(byte[] paramArrayOfByte, POA paramPOA, String paramString, Object paramObject, Servant paramServant)
/*     */   {
/*     */   }
/*     */ 
/*     */   public NamingContextImpl readInContext(String paramString)
/*     */   {
/* 111 */     NamingContextImpl localNamingContextImpl = (NamingContextImpl)this.contexts.get(paramString);
/* 112 */     if (localNamingContextImpl != null)
/*     */     {
/* 115 */       return localNamingContextImpl;
/*     */     }
/*     */ 
/* 118 */     File localFile = new File(this.logDir, paramString);
/* 119 */     if (localFile.exists())
/*     */       try {
/* 121 */         FileInputStream localFileInputStream = new FileInputStream(localFile);
/* 122 */         ObjectInputStream localObjectInputStream = new ObjectInputStream(localFileInputStream);
/* 123 */         localNamingContextImpl = (NamingContextImpl)localObjectInputStream.readObject();
/* 124 */         localNamingContextImpl.setORB(this.orb);
/* 125 */         localNamingContextImpl.setServantManagerImpl(this);
/* 126 */         localNamingContextImpl.setRootNameService(this.theNameService);
/* 127 */         localObjectInputStream.close();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/* 132 */     if (localNamingContextImpl != null)
/*     */     {
/* 134 */       this.contexts.put(paramString, localNamingContextImpl);
/*     */     }
/* 136 */     return localNamingContextImpl;
/*     */   }
/*     */ 
/*     */   public NamingContextImpl addContext(String paramString, NamingContextImpl paramNamingContextImpl)
/*     */   {
/* 142 */     File localFile = new File(this.logDir, paramString);
/*     */ 
/* 144 */     if (localFile.exists())
/*     */     {
/* 146 */       paramNamingContextImpl = readInContext(paramString);
/*     */     }
/*     */     else
/*     */       try {
/* 150 */         FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
/* 151 */         ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
/* 152 */         localObjectOutputStream.writeObject(paramNamingContextImpl);
/* 153 */         localObjectOutputStream.close();
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */     try {
/* 159 */       this.contexts.remove(paramString);
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/*     */     }
/* 164 */     this.contexts.put(paramString, paramNamingContextImpl);
/*     */ 
/* 166 */     return paramNamingContextImpl;
/*     */   }
/*     */ 
/*     */   public void updateContext(String paramString, NamingContextImpl paramNamingContextImpl)
/*     */   {
/* 172 */     File localFile = new File(this.logDir, paramString);
/* 173 */     if (localFile.exists())
/*     */     {
/* 175 */       localFile.delete();
/* 176 */       localFile = new File(this.logDir, paramString);
/*     */     }
/*     */     try
/*     */     {
/* 180 */       FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
/* 181 */       ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
/* 182 */       localObjectOutputStream.writeObject(paramNamingContextImpl);
/* 183 */       localObjectOutputStream.close();
/*     */     } catch (Exception localException) {
/* 185 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getRootObjectKey()
/*     */   {
/* 191 */     return "NC0";
/*     */   }
/*     */ 
/*     */   public String getNewObjectKey()
/*     */   {
/* 196 */     return "NC" + this.counterDb.getNextCounter();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.ServantManagerImpl
 * JD-Core Version:    0.6.2
 */