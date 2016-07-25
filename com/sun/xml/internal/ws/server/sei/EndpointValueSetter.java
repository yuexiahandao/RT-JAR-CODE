/*     */ package com.sun.xml.internal.ws.server.sei;
/*     */ 
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import javax.xml.ws.Holder;
/*     */ 
/*     */ abstract class EndpointValueSetter
/*     */ {
/*  68 */   private static final EndpointValueSetter[] POOL = new EndpointValueSetter[16];
/*     */ 
/*     */   abstract void put(Object paramObject, Object[] paramArrayOfObject);
/*     */ 
/*     */   public static EndpointValueSetter get(ParameterImpl p)
/*     */   {
/*  79 */     int idx = p.getIndex();
/*  80 */     if (p.isIN()) {
/*  81 */       if (idx < POOL.length) {
/*  82 */         return POOL[idx];
/*     */       }
/*  84 */       return new Param(idx);
/*     */     }
/*     */ 
/*  87 */     return new HolderParam(idx);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  71 */     for (int i = 0; i < POOL.length; i++)
/*  72 */       POOL[i] = new Param(i);
/*     */   }
/*     */ 
/*     */   static final class HolderParam extends EndpointValueSetter.Param
/*     */   {
/*     */     public HolderParam(int idx)
/*     */     {
/* 111 */       super();
/*     */     }
/*     */ 
/*     */     void put(Object obj, Object[] args)
/*     */     {
/* 116 */       Holder holder = new Holder();
/* 117 */       if (obj != null) {
/* 118 */         holder.value = obj;
/*     */       }
/* 120 */       args[this.idx] = holder;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Param extends EndpointValueSetter
/*     */   {
/*     */     protected final int idx;
/*     */ 
/*     */     public Param(int idx)
/*     */     {
/*  97 */       super();
/*  98 */       this.idx = idx;
/*     */     }
/*     */ 
/*     */     void put(Object obj, Object[] args) {
/* 102 */       if (obj != null)
/* 103 */         args[this.idx] = obj;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.EndpointValueSetter
 * JD-Core Version:    0.6.2
 */