/*     */ package com.sun.corba.se.impl.naming.cosnaming;
/*     */ 
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CosNaming.Binding;
/*     */ import org.omg.CosNaming.BindingHolder;
/*     */ import org.omg.CosNaming.BindingIteratorPOA;
/*     */ import org.omg.CosNaming.BindingListHolder;
/*     */ 
/*     */ public abstract class BindingIteratorImpl extends BindingIteratorPOA
/*     */ {
/*     */   protected ORB orb;
/*     */ 
/*     */   public BindingIteratorImpl(ORB paramORB)
/*     */     throws Exception
/*     */   {
/*  73 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   public synchronized boolean next_one(BindingHolder paramBindingHolder)
/*     */   {
/*  88 */     return NextOne(paramBindingHolder);
/*     */   }
/*     */ 
/*     */   public synchronized boolean next_n(int paramInt, BindingListHolder paramBindingListHolder)
/*     */   {
/* 104 */     if (paramInt == 0) {
/* 105 */       throw new BAD_PARAM(" 'how_many' parameter is set to 0 which is invalid");
/*     */     }
/*     */ 
/* 108 */     return list(paramInt, paramBindingListHolder);
/*     */   }
/*     */ 
/*     */   public boolean list(int paramInt, BindingListHolder paramBindingListHolder)
/*     */   {
/* 123 */     int i = Math.min(RemainingElements(), paramInt);
/*     */ 
/* 126 */     Binding[] arrayOfBinding = new Binding[i];
/* 127 */     BindingHolder localBindingHolder = new BindingHolder();
/* 128 */     int j = 0;
/*     */ 
/* 130 */     while ((j < i) && (NextOne(localBindingHolder) == true)) {
/* 131 */       arrayOfBinding[j] = localBindingHolder.value;
/* 132 */       j++;
/*     */     }
/*     */ 
/* 135 */     if (j == 0)
/*     */     {
/* 137 */       paramBindingListHolder.value = new Binding[0];
/* 138 */       return false;
/*     */     }
/*     */ 
/* 142 */     paramBindingListHolder.value = arrayOfBinding;
/*     */ 
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized void destroy()
/*     */   {
/* 160 */     Destroy();
/*     */   }
/*     */ 
/*     */   protected abstract boolean NextOne(BindingHolder paramBindingHolder);
/*     */ 
/*     */   protected abstract void Destroy();
/*     */ 
/*     */   protected abstract int RemainingElements();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.BindingIteratorImpl
 * JD-Core Version:    0.6.2
 */