/*     */ package com.sun.corba.se.impl.resolver;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.resolver.Resolver;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class BootstrapResolverImpl
/*     */   implements Resolver
/*     */ {
/*     */   private Delegate bootstrapDelegate;
/*     */   private ORBUtilSystemException wrapper;
/*     */ 
/*     */   public BootstrapResolverImpl(ORB paramORB, String paramString, int paramInt)
/*     */   {
/*  54 */     this.wrapper = ORBUtilSystemException.get(paramORB, "orb.resolver");
/*     */ 
/*  58 */     byte[] arrayOfByte = "INIT".getBytes();
/*  59 */     ObjectKey localObjectKey = paramORB.getObjectKeyFactory().create(arrayOfByte);
/*     */ 
/*  61 */     IIOPAddress localIIOPAddress = IIOPFactories.makeIIOPAddress(paramORB, paramString, paramInt);
/*  62 */     IIOPProfileTemplate localIIOPProfileTemplate = IIOPFactories.makeIIOPProfileTemplate(paramORB, GIOPVersion.V1_0, localIIOPAddress);
/*     */ 
/*  65 */     IORTemplate localIORTemplate = IORFactories.makeIORTemplate(localObjectKey.getTemplate());
/*  66 */     localIORTemplate.add(localIIOPProfileTemplate);
/*     */ 
/*  68 */     IOR localIOR = localIORTemplate.makeIOR(paramORB, "", localObjectKey.getId());
/*     */ 
/*  71 */     this.bootstrapDelegate = ORBUtility.makeClientDelegate(localIOR);
/*     */   }
/*     */ 
/*     */   private InputStream invoke(String paramString1, String paramString2)
/*     */   {
/*  84 */     int i = 1;
/*     */ 
/*  88 */     InputStream localInputStream = null;
/*     */ 
/*  95 */     while (i != 0) {
/*  96 */       org.omg.CORBA.Object localObject = null;
/*  97 */       i = 0;
/*     */ 
/*  99 */       OutputStream localOutputStream = this.bootstrapDelegate.request(localObject, paramString1, true);
/*     */ 
/* 102 */       if (paramString2 != null) {
/* 103 */         localOutputStream.write_string(paramString2);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 114 */         localInputStream = this.bootstrapDelegate.invoke(localObject, localOutputStream);
/*     */       } catch (ApplicationException localApplicationException) {
/* 116 */         throw this.wrapper.bootstrapApplicationException(localApplicationException);
/*     */       }
/*     */       catch (RemarshalException localRemarshalException) {
/* 119 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 123 */     return localInputStream;
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object resolve(String paramString)
/*     */   {
/* 128 */     InputStream localInputStream = null;
/* 129 */     org.omg.CORBA.Object localObject = null;
/*     */     try
/*     */     {
/* 132 */       localInputStream = invoke("get", paramString);
/*     */ 
/* 134 */       localObject = localInputStream.read_Object();
/*     */     }
/*     */     finally
/*     */     {
/* 139 */       this.bootstrapDelegate.releaseReply(null, localInputStream);
/*     */     }
/*     */ 
/* 142 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Set list()
/*     */   {
/* 147 */     InputStream localInputStream = null;
/* 148 */     HashSet localHashSet = new HashSet();
/*     */     try
/*     */     {
/* 151 */       localInputStream = invoke("list", null);
/*     */ 
/* 153 */       int i = localInputStream.read_long();
/* 154 */       for (int j = 0; j < i; j++) {
/* 155 */         localHashSet.add(localInputStream.read_string());
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 160 */       this.bootstrapDelegate.releaseReply(null, localInputStream);
/*     */     }
/*     */ 
/* 163 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.BootstrapResolverImpl
 * JD-Core Version:    0.6.2
 */