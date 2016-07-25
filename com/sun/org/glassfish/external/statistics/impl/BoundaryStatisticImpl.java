/*    */ package com.sun.org.glassfish.external.statistics.impl;
/*    */ 
/*    */ import com.sun.org.glassfish.external.statistics.BoundaryStatistic;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.util.Map;
/*    */ 
/*    */ public final class BoundaryStatisticImpl extends StatisticImpl
/*    */   implements BoundaryStatistic, InvocationHandler
/*    */ {
/*    */   private final long lowerBound;
/*    */   private final long upperBound;
/* 42 */   private final BoundaryStatistic bs = (InvocationHandler)Proxy.newProxyInstance(InvocationHandler.class.getClassLoader(), new Class[] { InvocationHandler.class }, this);
/*    */ 
/*    */   public BoundaryStatisticImpl(long lower, long upper, String name, String unit, String desc, long startTime, long sampleTime)
/*    */   {
/* 51 */     super(name, unit, desc, startTime, sampleTime);
/* 52 */     this.upperBound = upper;
/* 53 */     this.lowerBound = lower;
/*    */   }
/*    */ 
/*    */   public synchronized BoundaryStatistic getStatistic() {
/* 57 */     return this.bs;
/*    */   }
/*    */ 
/*    */   public synchronized Map getStaticAsMap() {
/* 61 */     Map m = super.getStaticAsMap();
/* 62 */     m.put("lowerbound", Long.valueOf(getLowerBound()));
/* 63 */     m.put("upperbound", Long.valueOf(getUpperBound()));
/* 64 */     return m;
/*    */   }
/*    */ 
/*    */   public synchronized long getLowerBound() {
/* 68 */     return this.lowerBound;
/*    */   }
/*    */ 
/*    */   public synchronized long getUpperBound() {
/* 72 */     return this.upperBound;
/*    */   }
/*    */ 
/*    */   public synchronized void reset()
/*    */   {
/* 77 */     super.reset();
/* 78 */     this.sampleTime = -1L;
/*    */   }
/*    */ 
/*    */   public Object invoke(Object proxy, Method m, Object[] args) throws Throwable
/*    */   {
/* 83 */     checkMethod(m);
/*    */     Object result;
/*    */     try {
/* 87 */       result = m.invoke(this, args);
/*    */     } catch (InvocationTargetException e) {
/* 89 */       throw e.getTargetException();
/*    */     } catch (Exception e) {
/* 91 */       throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
/*    */     }
/*    */ 
/* 94 */     return result;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.external.statistics.impl.BoundaryStatisticImpl
 * JD-Core Version:    0.6.2
 */