/*     */ package com.sun.corba.se.spi.servicecontext;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ 
/*     */ public class ServiceContextData
/*     */ {
/*     */   private Class scClass;
/*     */   private Constructor scConstructor;
/*     */   private int scId;
/*     */ 
/*     */   private void dprint(String paramString)
/*     */   {
/*  44 */     ORBUtility.dprint(this, paramString);
/*     */   }
/*     */ 
/*     */   private void throwBadParam(String paramString, Throwable paramThrowable)
/*     */   {
/*  49 */     BAD_PARAM localBAD_PARAM = new BAD_PARAM(paramString);
/*  50 */     if (paramThrowable != null)
/*  51 */       localBAD_PARAM.initCause(paramThrowable);
/*  52 */     throw localBAD_PARAM;
/*     */   }
/*     */ 
/*     */   public ServiceContextData(Class paramClass)
/*     */   {
/*  57 */     if (ORB.ORBInitDebug) {
/*  58 */       dprint("ServiceContextData constructor called for class " + paramClass);
/*     */     }
/*  60 */     this.scClass = paramClass;
/*     */     try
/*     */     {
/*  63 */       if (ORB.ORBInitDebug) {
/*  64 */         dprint("Finding constructor for " + paramClass);
/*     */       }
/*     */ 
/*  67 */       Class[] arrayOfClass = new Class[2];
/*  68 */       arrayOfClass[0] = InputStream.class;
/*  69 */       arrayOfClass[1] = GIOPVersion.class;
/*     */       try {
/*  71 */         this.scConstructor = paramClass.getConstructor(arrayOfClass);
/*     */       } catch (NoSuchMethodException localNoSuchMethodException) {
/*  73 */         throwBadParam("Class does not have an InputStream constructor", localNoSuchMethodException);
/*     */       }
/*     */ 
/*  76 */       if (ORB.ORBInitDebug) {
/*  77 */         dprint("Finding SERVICE_CONTEXT_ID field in " + paramClass);
/*     */       }
/*     */ 
/*  80 */       Field localField = null;
/*     */       try {
/*  82 */         localField = paramClass.getField("SERVICE_CONTEXT_ID");
/*     */       } catch (NoSuchFieldException localNoSuchFieldException) {
/*  84 */         throwBadParam("Class does not have a SERVICE_CONTEXT_ID member", localNoSuchFieldException);
/*     */       } catch (SecurityException localSecurityException) {
/*  86 */         throwBadParam("Could not access SERVICE_CONTEXT_ID member", localSecurityException);
/*     */       }
/*     */ 
/*  89 */       if (ORB.ORBInitDebug) {
/*  90 */         dprint("Checking modifiers of SERVICE_CONTEXT_ID field in " + paramClass);
/*     */       }
/*  92 */       int i = localField.getModifiers();
/*  93 */       if ((!Modifier.isPublic(i)) || (!Modifier.isStatic(i)) || (!Modifier.isFinal(i)))
/*     */       {
/*  95 */         throwBadParam("SERVICE_CONTEXT_ID field is not public static final", null);
/*     */       }
/*  97 */       if (ORB.ORBInitDebug)
/*  98 */         dprint("Getting value of SERVICE_CONTEXT_ID in " + paramClass);
/*     */       try
/*     */       {
/* 101 */         this.scId = localField.getInt(null);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 103 */         throwBadParam("SERVICE_CONTEXT_ID not convertible to int", localIllegalArgumentException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 105 */         throwBadParam("Could not access value of SERVICE_CONTEXT_ID", localIllegalAccessException);
/*     */       }
/*     */     } catch (BAD_PARAM localBAD_PARAM) {
/* 108 */       if (ORB.ORBInitDebug)
/* 109 */         dprint("Exception in ServiceContextData constructor: " + localBAD_PARAM);
/* 110 */       throw localBAD_PARAM;
/*     */     } catch (Throwable localThrowable) {
/* 112 */       if (ORB.ORBInitDebug) {
/* 113 */         dprint("Unexpected Exception in ServiceContextData constructor: " + localThrowable);
/*     */       }
/*     */     }
/*     */ 
/* 117 */     if (ORB.ORBInitDebug)
/* 118 */       dprint("ServiceContextData constructor completed");
/*     */   }
/*     */ 
/*     */   public ServiceContext makeServiceContext(InputStream paramInputStream, GIOPVersion paramGIOPVersion)
/*     */   {
/* 126 */     Object[] arrayOfObject = new Object[2];
/* 127 */     arrayOfObject[0] = paramInputStream;
/* 128 */     arrayOfObject[1] = paramGIOPVersion;
/* 129 */     ServiceContext localServiceContext = null;
/*     */     try
/*     */     {
/* 132 */       localServiceContext = (ServiceContext)this.scConstructor.newInstance(arrayOfObject);
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 134 */       throwBadParam("InputStream constructor argument error", localIllegalArgumentException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 136 */       throwBadParam("InputStream constructor argument error", localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 138 */       throwBadParam("InputStream constructor called for abstract class", localInstantiationException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 140 */       throwBadParam("InputStream constructor threw exception " + localInvocationTargetException.getTargetException(), localInvocationTargetException);
/*     */     }
/*     */ 
/* 144 */     return localServiceContext;
/*     */   }
/*     */ 
/*     */   int getId()
/*     */   {
/* 149 */     return this.scId;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     return "ServiceContextData[ scClass=" + this.scClass + " scConstructor=" + this.scConstructor + " scId=" + this.scId + " ]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.servicecontext.ServiceContextData
 * JD-Core Version:    0.6.2
 */