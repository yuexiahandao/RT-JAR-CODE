/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class MXBeanProxy
/*     */ {
/* 174 */   private final Map<Method, Handler> handlerMap = Util.newMap();
/*     */ 
/*     */   public MXBeanProxy(Class<?> paramClass)
/*     */   {
/*  50 */     if (paramClass == null)
/*  51 */       throw new IllegalArgumentException("Null parameter");
/*     */     MBeanAnalyzer localMBeanAnalyzer;
/*     */     try
/*     */     {
/*  55 */       localMBeanAnalyzer = MXBeanIntrospector.getInstance().getAnalyzer(paramClass);
/*     */     }
/*     */     catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/*  58 */       throw new IllegalArgumentException(localNotCompliantMBeanException);
/*     */     }
/*  60 */     localMBeanAnalyzer.visit(new Visitor(null));
/*     */   }
/*     */ 
/*     */   public Object invoke(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Method paramMethod, Object[] paramArrayOfObject)
/*     */     throws Throwable
/*     */   {
/* 160 */     Handler localHandler = (Handler)this.handlerMap.get(paramMethod);
/* 161 */     ConvertingMethod localConvertingMethod = localHandler.getConvertingMethod();
/* 162 */     MXBeanLookup localMXBeanLookup1 = MXBeanLookup.lookupFor(paramMBeanServerConnection);
/* 163 */     MXBeanLookup localMXBeanLookup2 = MXBeanLookup.getLookup();
/*     */     try {
/* 165 */       MXBeanLookup.setLookup(localMXBeanLookup1);
/* 166 */       Object[] arrayOfObject = localConvertingMethod.toOpenParameters(localMXBeanLookup1, paramArrayOfObject);
/* 167 */       Object localObject1 = localHandler.invoke(paramMBeanServerConnection, paramObjectName, arrayOfObject);
/* 168 */       return localConvertingMethod.fromOpenReturnValue(localMXBeanLookup1, localObject1);
/*     */     } finally {
/* 170 */       MXBeanLookup.setLookup(localMXBeanLookup2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class GetHandler extends MXBeanProxy.Handler
/*     */   {
/*     */     GetHandler(String paramString, ConvertingMethod paramConvertingMethod)
/*     */     {
/* 115 */       super(paramConvertingMethod);
/*     */     }
/*     */ 
/*     */     Object invoke(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Object[] paramArrayOfObject)
/*     */       throws Exception
/*     */     {
/* 121 */       assert ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0));
/* 122 */       return paramMBeanServerConnection.getAttribute(paramObjectName, getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class Handler
/*     */   {
/*     */     private final String name;
/*     */     private final ConvertingMethod convertingMethod;
/*     */ 
/*     */     Handler(String paramString, ConvertingMethod paramConvertingMethod)
/*     */     {
/*  94 */       this.name = paramString;
/*  95 */       this.convertingMethod = paramConvertingMethod;
/*     */     }
/*     */ 
/*     */     String getName() {
/*  99 */       return this.name;
/*     */     }
/*     */ 
/*     */     ConvertingMethod getConvertingMethod() {
/* 103 */       return this.convertingMethod;
/*     */     }
/*     */ 
/*     */     abstract Object invoke(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Object[] paramArrayOfObject)
/*     */       throws Exception;
/*     */   }
/*     */ 
/*     */   private static class InvokeHandler extends MXBeanProxy.Handler
/*     */   {
/*     */     private final String[] signature;
/*     */ 
/*     */     InvokeHandler(String paramString, String[] paramArrayOfString, ConvertingMethod paramConvertingMethod)
/*     */     {
/* 144 */       super(paramConvertingMethod);
/* 145 */       this.signature = paramArrayOfString;
/*     */     }
/*     */ 
/*     */     Object invoke(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Object[] paramArrayOfObject) throws Exception
/*     */     {
/* 150 */       return paramMBeanServerConnection.invoke(paramObjectName, getName(), paramArrayOfObject, this.signature);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SetHandler extends MXBeanProxy.Handler
/*     */   {
/*     */     SetHandler(String paramString, ConvertingMethod paramConvertingMethod)
/*     */     {
/* 128 */       super(paramConvertingMethod);
/*     */     }
/*     */ 
/*     */     Object invoke(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, Object[] paramArrayOfObject)
/*     */       throws Exception
/*     */     {
/* 134 */       assert (paramArrayOfObject.length == 1);
/* 135 */       Attribute localAttribute = new Attribute(getName(), paramArrayOfObject[0]);
/* 136 */       paramMBeanServerConnection.setAttribute(paramObjectName, localAttribute);
/* 137 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Visitor
/*     */     implements MBeanAnalyzer.MBeanVisitor<ConvertingMethod>
/*     */   {
/*     */     private Visitor()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visitAttribute(String paramString, ConvertingMethod paramConvertingMethod1, ConvertingMethod paramConvertingMethod2)
/*     */     {
/*     */       Method localMethod;
/*  68 */       if (paramConvertingMethod1 != null) {
/*  69 */         paramConvertingMethod1.checkCallToOpen();
/*  70 */         localMethod = paramConvertingMethod1.getMethod();
/*  71 */         MXBeanProxy.this.handlerMap.put(localMethod, new MXBeanProxy.GetHandler(paramString, paramConvertingMethod1));
/*     */       }
/*     */ 
/*  74 */       if (paramConvertingMethod2 != null)
/*     */       {
/*  76 */         localMethod = paramConvertingMethod2.getMethod();
/*  77 */         MXBeanProxy.this.handlerMap.put(localMethod, new MXBeanProxy.SetHandler(paramString, paramConvertingMethod2));
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visitOperation(String paramString, ConvertingMethod paramConvertingMethod)
/*     */     {
/*  84 */       paramConvertingMethod.checkCallToOpen();
/*  85 */       Method localMethod = paramConvertingMethod.getMethod();
/*  86 */       String[] arrayOfString = paramConvertingMethod.getOpenSignature();
/*  87 */       MXBeanProxy.this.handlerMap.put(localMethod, new MXBeanProxy.InvokeHandler(paramString, arrayOfString, paramConvertingMethod));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MXBeanProxy
 * JD-Core Version:    0.6.2
 */