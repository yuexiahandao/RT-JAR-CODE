/*    */ package com.sun.corba.se.impl.orbutil;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class DenseIntMapImpl
/*    */ {
/* 37 */   private ArrayList list = new ArrayList();
/*    */ 
/*    */   private void checkKey(int paramInt)
/*    */   {
/* 41 */     if (paramInt < 0)
/* 42 */       throw new IllegalArgumentException("Key must be >= 0.");
/*    */   }
/*    */ 
/*    */   public Object get(int paramInt)
/*    */   {
/* 50 */     checkKey(paramInt);
/*    */ 
/* 52 */     Object localObject = null;
/* 53 */     if (paramInt < this.list.size()) {
/* 54 */       localObject = this.list.get(paramInt);
/*    */     }
/* 56 */     return localObject;
/*    */   }
/*    */ 
/*    */   public void set(int paramInt, Object paramObject)
/*    */   {
/* 64 */     checkKey(paramInt);
/* 65 */     extend(paramInt);
/* 66 */     this.list.set(paramInt, paramObject);
/*    */   }
/*    */ 
/*    */   private void extend(int paramInt)
/*    */   {
/* 71 */     if (paramInt >= this.list.size()) {
/* 72 */       this.list.ensureCapacity(paramInt + 1);
/* 73 */       int i = this.list.size();
/* 74 */       while (i++ <= paramInt)
/* 75 */         this.list.add(null);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.DenseIntMapImpl
 * JD-Core Version:    0.6.2
 */