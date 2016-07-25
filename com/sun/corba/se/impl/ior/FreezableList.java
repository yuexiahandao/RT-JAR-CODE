/*     */ package com.sun.corba.se.impl.ior;
/*     */ 
/*     */ import com.sun.corba.se.spi.ior.MakeImmutable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class FreezableList extends AbstractList
/*     */ {
/*  44 */   private List delegate = null;
/*  45 */   private boolean immutable = false;
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  49 */     if (paramObject == null) {
/*  50 */       return false;
/*     */     }
/*  52 */     if (!(paramObject instanceof FreezableList)) {
/*  53 */       return false;
/*     */     }
/*  55 */     FreezableList localFreezableList = (FreezableList)paramObject;
/*     */ 
/*  57 */     return (this.delegate.equals(localFreezableList.delegate)) && (this.immutable == localFreezableList.immutable);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  63 */     return this.delegate.hashCode();
/*     */   }
/*     */ 
/*     */   public FreezableList(List paramList, boolean paramBoolean)
/*     */   {
/*  68 */     this.delegate = paramList;
/*  69 */     this.immutable = paramBoolean;
/*     */   }
/*     */ 
/*     */   public FreezableList(List paramList)
/*     */   {
/*  74 */     this(paramList, false);
/*     */   }
/*     */ 
/*     */   public void makeImmutable()
/*     */   {
/*  79 */     this.immutable = true;
/*     */   }
/*     */ 
/*     */   public boolean isImmutable()
/*     */   {
/*  84 */     return this.immutable;
/*     */   }
/*     */ 
/*     */   public void makeElementsImmutable()
/*     */   {
/*  89 */     Iterator localIterator = iterator();
/*  90 */     while (localIterator.hasNext()) {
/*  91 */       Object localObject = localIterator.next();
/*  92 */       if ((localObject instanceof MakeImmutable)) {
/*  93 */         MakeImmutable localMakeImmutable = (MakeImmutable)localObject;
/*  94 */         localMakeImmutable.makeImmutable();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 103 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   public Object get(int paramInt)
/*     */   {
/* 108 */     return this.delegate.get(paramInt);
/*     */   }
/*     */ 
/*     */   public Object set(int paramInt, Object paramObject)
/*     */   {
/* 113 */     if (this.immutable) {
/* 114 */       throw new UnsupportedOperationException();
/*     */     }
/* 116 */     return this.delegate.set(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, Object paramObject)
/*     */   {
/* 121 */     if (this.immutable) {
/* 122 */       throw new UnsupportedOperationException();
/*     */     }
/* 124 */     this.delegate.add(paramInt, paramObject);
/*     */   }
/*     */ 
/*     */   public Object remove(int paramInt)
/*     */   {
/* 129 */     if (this.immutable) {
/* 130 */       throw new UnsupportedOperationException();
/*     */     }
/* 132 */     return this.delegate.remove(paramInt);
/*     */   }
/*     */ 
/*     */   public List subList(int paramInt1, int paramInt2)
/*     */   {
/* 138 */     List localList = this.delegate.subList(paramInt1, paramInt2);
/* 139 */     FreezableList localFreezableList = new FreezableList(localList, this.immutable);
/* 140 */     return localFreezableList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.FreezableList
 * JD-Core Version:    0.6.2
 */