/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class Scope<BeanT, PropT, ItemT, PackT>
/*     */ {
/*     */   public final UnmarshallingContext context;
/*     */   private BeanT bean;
/*     */   private Accessor<BeanT, PropT> acc;
/*     */   private PackT pack;
/*     */   private Lister<BeanT, PropT, ItemT, PackT> lister;
/*     */ 
/*     */   Scope(UnmarshallingContext context)
/*     */   {
/*  52 */     this.context = context;
/*     */   }
/*     */ 
/*     */   public boolean hasStarted()
/*     */   {
/*  59 */     return this.bean != null;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  66 */     if (this.bean == null)
/*     */     {
/*  68 */       assert (clean());
/*  69 */       return;
/*     */     }
/*     */ 
/*  72 */     this.bean = null;
/*  73 */     this.acc = null;
/*  74 */     this.pack = null;
/*  75 */     this.lister = null;
/*     */   }
/*     */ 
/*     */   public void finish()
/*     */     throws AccessorException
/*     */   {
/*  83 */     if (hasStarted()) {
/*  84 */       this.lister.endPacking(this.pack, this.bean, this.acc);
/*  85 */       reset();
/*     */     }
/*  87 */     assert (clean());
/*     */   }
/*     */ 
/*     */   private boolean clean() {
/*  91 */     return (this.bean == null) && (this.acc == null) && (this.pack == null) && (this.lister == null);
/*     */   }
/*     */ 
/*     */   public void add(Accessor<BeanT, PropT> acc, Lister<BeanT, PropT, ItemT, PackT> lister, ItemT value)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/*  99 */       if (!hasStarted()) {
/* 100 */         this.bean = this.context.getCurrentState().getTarget();
/* 101 */         this.acc = acc;
/* 102 */         this.lister = lister;
/* 103 */         this.pack = lister.startPacking(this.bean, acc);
/*     */       }
/*     */ 
/* 106 */       lister.addToPack(this.pack, value);
/*     */     } catch (AccessorException e) {
/* 108 */       Loader.handleGenericException(e, true);
/*     */ 
/* 110 */       this.lister = Lister.getErrorInstance();
/* 111 */       this.acc = Accessor.getErrorInstance();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void start(Accessor<BeanT, PropT> acc, Lister<BeanT, PropT, ItemT, PackT> lister)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 123 */       if (!hasStarted()) {
/* 124 */         this.bean = this.context.getCurrentState().getTarget();
/* 125 */         this.acc = acc;
/* 126 */         this.lister = lister;
/* 127 */         this.pack = lister.startPacking(this.bean, acc);
/*     */       }
/*     */     } catch (AccessorException e) {
/* 130 */       Loader.handleGenericException(e, true);
/*     */ 
/* 132 */       this.lister = Lister.getErrorInstance();
/* 133 */       this.acc = Accessor.getErrorInstance();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.Scope
 * JD-Core Version:    0.6.2
 */