/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Coordinator;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ 
/*     */ final class AdaptedAccessor<BeanT, InMemValueT, OnWireValueT> extends Accessor<BeanT, OnWireValueT>
/*     */ {
/*     */   private final Accessor<BeanT, InMemValueT> core;
/*     */   private final Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter;
/*     */   private XmlAdapter<OnWireValueT, InMemValueT> staticAdapter;
/*     */ 
/*     */   AdaptedAccessor(Class<OnWireValueT> targetType, Accessor<BeanT, InMemValueT> extThis, Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter)
/*     */   {
/*  46 */     super(targetType);
/*  47 */     this.core = extThis;
/*  48 */     this.adapter = adapter;
/*     */   }
/*     */ 
/*     */   public boolean isAdapted()
/*     */   {
/*  53 */     return true;
/*     */   }
/*     */ 
/*     */   public OnWireValueT get(BeanT bean) throws AccessorException {
/*  57 */     Object v = this.core.get(bean);
/*     */ 
/*  59 */     XmlAdapter a = getAdapter();
/*     */     try {
/*  61 */       return a.marshal(v);
/*     */     } catch (Exception e) {
/*  63 */       throw new AccessorException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(BeanT bean, OnWireValueT o) throws AccessorException {
/*  68 */     XmlAdapter a = getAdapter();
/*     */     try {
/*  70 */       this.core.set(bean, o == null ? null : a.unmarshal(o));
/*     */     } catch (Exception e) {
/*  72 */       throw new AccessorException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getUnadapted(BeanT bean) throws AccessorException {
/*  77 */     return this.core.getUnadapted(bean);
/*     */   }
/*     */ 
/*     */   public void setUnadapted(BeanT bean, Object value) throws AccessorException {
/*  81 */     this.core.setUnadapted(bean, value);
/*     */   }
/*     */ 
/*     */   private XmlAdapter<OnWireValueT, InMemValueT> getAdapter()
/*     */   {
/*  92 */     Coordinator coordinator = Coordinator._getInstance();
/*  93 */     if (coordinator != null) {
/*  94 */       return coordinator.getAdapter(this.adapter);
/*     */     }
/*  96 */     synchronized (this) {
/*  97 */       if (this.staticAdapter == null)
/*  98 */         this.staticAdapter = ((XmlAdapter)ClassFactory.create(this.adapter));
/*     */     }
/* 100 */     return this.staticAdapter;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.AdaptedAccessor
 * JD-Core Version:    0.6.2
 */