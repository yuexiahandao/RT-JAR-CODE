/*    */ package com.sun.script.util;
/*    */ 
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import javax.script.Bindings;
/*    */ 
/*    */ public abstract class BindingsImpl extends BindingsBase
/*    */ {
/* 41 */   protected Bindings global = null;
/*    */ 
/* 44 */   protected Bindings local = null;
/*    */ 
/*    */   public void setGlobal(Bindings paramBindings) {
/* 47 */     this.global = paramBindings;
/*    */   }
/*    */ 
/*    */   public void setLocal(Bindings paramBindings) {
/* 51 */     this.local = paramBindings;
/*    */   }
/*    */ 
/*    */   public Set<Map.Entry<String, Object>> entrySet() {
/* 55 */     return new BindingsEntrySet(this);
/*    */   }
/*    */ 
/*    */   public Object get(Object paramObject) {
/* 59 */     checkKey(paramObject);
/*    */ 
/* 61 */     Object localObject = null;
/* 62 */     if ((this.local != null) && (null != (localObject = this.local.get(paramObject)))) {
/* 63 */       return localObject;
/*    */     }
/*    */ 
/* 66 */     localObject = getImpl((String)paramObject);
/*    */ 
/* 68 */     if (localObject != null)
/* 69 */       return localObject;
/* 70 */     if (this.global != null) {
/* 71 */       return this.global.get(paramObject);
/*    */     }
/* 73 */     return null;
/*    */   }
/*    */ 
/*    */   public Object remove(Object paramObject)
/*    */   {
/* 78 */     checkKey(paramObject);
/* 79 */     Object localObject = get(paramObject);
/* 80 */     if (localObject != null) {
/* 81 */       removeImpl((String)paramObject);
/*    */     }
/* 83 */     return localObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.util.BindingsImpl
 * JD-Core Version:    0.6.2
 */