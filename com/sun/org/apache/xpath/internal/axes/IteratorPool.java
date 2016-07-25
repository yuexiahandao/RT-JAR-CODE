/*     */ package com.sun.org.apache.xpath.internal.axes;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public final class IteratorPool
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -460927331149566998L;
/*     */   private final DTMIterator m_orig;
/*     */   private final ArrayList m_freeStack;
/*     */ 
/*     */   public IteratorPool(DTMIterator original)
/*     */   {
/*  55 */     this.m_orig = original;
/*  56 */     this.m_freeStack = new ArrayList();
/*     */   }
/*     */ 
/*     */   public synchronized DTMIterator getInstanceOrThrow()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  68 */     if (this.m_freeStack.isEmpty())
/*     */     {
/*  72 */       return (DTMIterator)this.m_orig.clone();
/*     */     }
/*     */ 
/*  77 */     DTMIterator result = (DTMIterator)this.m_freeStack.remove(this.m_freeStack.size() - 1);
/*  78 */     return result;
/*     */   }
/*     */ 
/*     */   public synchronized DTMIterator getInstance()
/*     */   {
/*  90 */     if (this.m_freeStack.isEmpty())
/*     */     {
/*     */       try
/*     */       {
/*  96 */         return (DTMIterator)this.m_orig.clone();
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 100 */         throw new WrappedRuntimeException(ex);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 106 */     DTMIterator result = (DTMIterator)this.m_freeStack.remove(this.m_freeStack.size() - 1);
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */   public synchronized void freeInstance(DTMIterator obj)
/*     */   {
/* 119 */     this.m_freeStack.add(obj);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.axes.IteratorPool
 * JD-Core Version:    0.6.2
 */