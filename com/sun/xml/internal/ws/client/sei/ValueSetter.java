/*     */ package com.sun.xml.internal.ws.client.sei;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.RawAccessor;
/*     */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*     */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Holder;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ abstract class ValueSetter
/*     */ {
/*  74 */   private static final ValueSetter RETURN_VALUE = new ReturnValue(null);
/*     */ 
/*  79 */   private static final ValueSetter[] POOL = new ValueSetter[16];
/*     */ 
/* 137 */   static final ValueSetter SINGLE_VALUE = new SingleValue(null);
/*     */ 
/*     */   abstract Object put(Object paramObject, Object[] paramArrayOfObject);
/*     */ 
/*     */   static ValueSetter getSync(ParameterImpl p)
/*     */   {
/*  90 */     int idx = p.getIndex();
/*     */ 
/*  92 */     if (idx == -1)
/*  93 */       return RETURN_VALUE;
/*  94 */     if (idx < POOL.length) {
/*  95 */       return POOL[idx];
/*     */     }
/*  97 */     return new Param(idx);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  82 */     for (int i = 0; i < POOL.length; i++)
/*  83 */       POOL[i] = new Param(i);
/*     */   }
/*     */ 
/*     */   static final class AsyncBeanValueSetter extends ValueSetter
/*     */   {
/*     */     private final RawAccessor accessor;
/*     */ 
/*     */     AsyncBeanValueSetter(ParameterImpl p, Class wrapper)
/*     */     {
/* 159 */       super();
/* 160 */       QName name = p.getName();
/*     */       try {
/* 162 */         this.accessor = p.getOwner().getJAXBContext().getElementPropertyAccessor(wrapper, name.getNamespaceURI(), name.getLocalPart());
/*     */       }
/*     */       catch (JAXBException e) {
/* 165 */         throw new WebServiceException(wrapper + " do not have a property of the name " + name, e);
/*     */       }
/*     */     }
/*     */ 
/*     */     Object put(Object obj, Object[] args)
/*     */     {
/* 178 */       assert (args != null);
/* 179 */       assert (args.length == 1);
/* 180 */       assert (args[0] != null);
/*     */ 
/* 182 */       Object bean = args[0];
/*     */       try {
/* 184 */         this.accessor.set(bean, obj);
/*     */       } catch (Exception e) {
/* 186 */         throw new WebServiceException(e);
/*     */       }
/* 188 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Param extends ValueSetter
/*     */   {
/*     */     private final int idx;
/*     */ 
/*     */     public Param(int idx)
/*     */     {
/* 113 */       super();
/* 114 */       this.idx = idx;
/*     */     }
/*     */ 
/*     */     Object put(Object obj, Object[] args) {
/* 118 */       Object arg = args[this.idx];
/* 119 */       if (arg != null)
/*     */       {
/* 121 */         assert ((arg instanceof Holder));
/* 122 */         ((Holder)arg).value = obj;
/*     */       }
/*     */ 
/* 130 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ReturnValue extends ValueSetter
/*     */   {
/*     */     private ReturnValue()
/*     */     {
/* 101 */       super();
/*     */     }
/* 103 */     Object put(Object obj, Object[] args) { return obj; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static final class SingleValue extends ValueSetter
/*     */   {
/*     */     private SingleValue()
/*     */     {
/* 142 */       super();
/*     */     }
/*     */ 
/*     */     Object put(Object obj, Object[] args)
/*     */     {
/* 147 */       args[0] = obj;
/* 148 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.ValueSetter
 * JD-Core Version:    0.6.2
 */