/*     */ package com.sun.demo.jvmti.hprof;
/*     */ 
/*     */ public class Tracker
/*     */ {
/*  44 */   private static int engaged = 0;
/*     */ 
/*     */   private static native void nativeObjectInit(Object paramObject1, Object paramObject2);
/*     */ 
/*     */   public static void ObjectInit(Object paramObject)
/*     */   {
/*  56 */     if (engaged != 0) {
/*  57 */       if (paramObject == null) {
/*  58 */         throw new IllegalArgumentException("Null object.");
/*     */       }
/*  60 */       nativeObjectInit(Thread.currentThread(), paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void nativeNewArray(Object paramObject1, Object paramObject2);
/*     */ 
/*     */   public static void NewArray(Object paramObject)
/*     */   {
/*  72 */     if (engaged != 0) {
/*  73 */       if (paramObject == null) {
/*  74 */         throw new IllegalArgumentException("Null object.");
/*     */       }
/*  76 */       nativeNewArray(Thread.currentThread(), paramObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void nativeCallSite(Object paramObject, int paramInt1, int paramInt2);
/*     */ 
/*     */   public static void CallSite(int paramInt1, int paramInt2)
/*     */   {
/*  90 */     if (engaged != 0) {
/*  91 */       if (paramInt1 < 0) {
/*  92 */         throw new IllegalArgumentException("Negative class index");
/*     */       }
/*     */ 
/*  95 */       if (paramInt2 < 0) {
/*  96 */         throw new IllegalArgumentException("Negative method index");
/*     */       }
/*     */ 
/*  99 */       nativeCallSite(Thread.currentThread(), paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static native void nativeReturnSite(Object paramObject, int paramInt1, int paramInt2);
/*     */ 
/*     */   public static void ReturnSite(int paramInt1, int paramInt2)
/*     */   {
/* 111 */     if (engaged != 0) {
/* 112 */       if (paramInt1 < 0) {
/* 113 */         throw new IllegalArgumentException("Negative class index");
/*     */       }
/*     */ 
/* 116 */       if (paramInt2 < 0) {
/* 117 */         throw new IllegalArgumentException("Negative method index");
/*     */       }
/*     */ 
/* 120 */       nativeReturnSite(Thread.currentThread(), paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.demo.jvmti.hprof.Tracker
 * JD-Core Version:    0.6.2
 */