/*     */ package com.sun.corba.se.spi.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class ParserImplBase
/*     */ {
/*     */   private ORBUtilSystemException wrapper;
/*     */ 
/*     */   protected abstract PropertyParser makeParser();
/*     */ 
/*     */   protected void complete()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ParserImplBase()
/*     */   {
/*  65 */     this.wrapper = ORBUtilSystemException.get("orb.lifecycle");
/*     */   }
/*     */ 
/*     */   public void init(DataCollector paramDataCollector)
/*     */   {
/*  71 */     PropertyParser localPropertyParser = makeParser();
/*  72 */     paramDataCollector.setParser(localPropertyParser);
/*  73 */     Properties localProperties = paramDataCollector.getProperties();
/*  74 */     Map localMap = localPropertyParser.parse(localProperties);
/*  75 */     setFields(localMap);
/*     */   }
/*     */ 
/*     */   private Field getAnyField(String paramString)
/*     */   {
/*  80 */     Field localField = null;
/*     */     try
/*     */     {
/*  83 */       Class localClass = getClass();
/*  84 */       localField = localClass.getDeclaredField(paramString);
/*  85 */       while (localField == null) {
/*  86 */         localClass = localClass.getSuperclass();
/*  87 */         if (localClass == null) {
/*     */           break;
/*     */         }
/*  90 */         localField = localClass.getDeclaredField(paramString);
/*     */       }
/*     */     } catch (Exception localException) {
/*  93 */       throw this.wrapper.fieldNotFound(localException, paramString);
/*     */     }
/*     */ 
/*  96 */     if (localField == null) {
/*  97 */       throw this.wrapper.fieldNotFound(paramString);
/*     */     }
/*  99 */     return localField;
/*     */   }
/*     */ 
/*     */   protected void setFields(Map paramMap)
/*     */   {
/* 104 */     Set localSet = paramMap.entrySet();
/* 105 */     Iterator localIterator = localSet.iterator();
/* 106 */     while (localIterator.hasNext()) {
/* 107 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 108 */       final String str = (String)localEntry.getKey();
/* 109 */       final Object localObject = localEntry.getValue();
/*     */       try
/*     */       {
/* 112 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Object run()
/*     */             throws IllegalAccessException, IllegalArgumentException
/*     */           {
/* 117 */             Field localField = ParserImplBase.this.getAnyField(str);
/* 118 */             localField.setAccessible(true);
/* 119 */             localField.set(ParserImplBase.this, localObject);
/* 120 */             return null;
/*     */           }
/*     */ 
/*     */         });
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException)
/*     */       {
/* 127 */         throw this.wrapper.errorSettingField(localPrivilegedActionException.getCause(), str, localObject.toString());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 134 */     complete();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ParserImplBase
 * JD-Core Version:    0.6.2
 */