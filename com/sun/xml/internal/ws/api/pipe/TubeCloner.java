/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TubeCloner
/*     */ {
/*  43 */   protected final Map<Object, Object> master2copy = new HashMap();
/*     */ 
/*     */   public static Tube clone(Tube p)
/*     */   {
/*  63 */     return new PipeCloner().copy(p);
/*     */   }
/*     */ 
/*     */   public <T extends Tube> T copy(T t)
/*     */   {
/*  89 */     Tube r = (Tube)this.master2copy.get(t);
/*  90 */     if (r == null) {
/*  91 */       r = t.copy(this);
/*     */ 
/*  93 */       assert (this.master2copy.get(t) == r) : ("the tube must call the add(...) method to register itself before start copying other pipes, but " + t + " hasn't done so");
/*     */     }
/*  95 */     return r;
/*     */   }
/*     */ 
/*     */   public void add(Tube original, Tube copy)
/*     */   {
/* 109 */     assert (!this.master2copy.containsKey(original));
/* 110 */     assert ((original != null) && (copy != null));
/* 111 */     this.master2copy.put(original, copy);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.TubeCloner
 * JD-Core Version:    0.6.2
 */