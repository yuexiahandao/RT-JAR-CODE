/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class PropertyFactory
/*     */ {
/*     */   private static final Constructor<? extends Property>[] propImpls;
/*     */ 
/*     */   public static Property create(JAXBContextImpl grammar, RuntimePropertyInfo info)
/*     */   {
/*  89 */     PropertyKind kind = info.kind();
/*     */ 
/*  91 */     switch (1.$SwitchMap$com$sun$xml$internal$bind$v2$model$core$PropertyKind[kind.ordinal()]) {
/*     */     case 1:
/*  93 */       return new AttributeProperty(grammar, (RuntimeAttributePropertyInfo)info);
/*     */     case 2:
/*  95 */       return new ValueProperty(grammar, (RuntimeValuePropertyInfo)info);
/*     */     case 3:
/*  97 */       if (((RuntimeElementPropertyInfo)info).isValueList())
/*  98 */         return new ListElementProperty(grammar, (RuntimeElementPropertyInfo)info);
/*     */       break;
/*     */     case 4:
/*     */     case 5:
/* 102 */       break;
/*     */     default:
/* 104 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */       break;
/*     */     }
/*     */ 
/* 108 */     boolean isCollection = info.isCollection();
/* 109 */     boolean isLeaf = isLeaf(info);
/*     */ 
/* 111 */     Constructor c = propImpls[(6 + 0 + kind.propertyIndex)];
/*     */     try {
/* 113 */       return (Property)c.newInstance(new Object[] { grammar, info });
/*     */     } catch (InstantiationException e) {
/* 115 */       throw new InstantiationError(e.getMessage());
/*     */     } catch (IllegalAccessException e) {
/* 117 */       throw new IllegalAccessError(e.getMessage());
/*     */     } catch (InvocationTargetException e) {
/* 119 */       Throwable t = e.getCause();
/* 120 */       if ((t instanceof Error))
/* 121 */         throw ((Error)t);
/* 122 */       if ((t instanceof RuntimeException)) {
/* 123 */         throw ((RuntimeException)t);
/*     */       }
/* 125 */       throw new AssertionError(t);
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isLeaf(RuntimePropertyInfo info)
/*     */   {
/* 134 */     Collection types = info.ref();
/* 135 */     if (types.size() != 1) return false;
/*     */ 
/* 137 */     RuntimeTypeInfo rti = (RuntimeTypeInfo)types.iterator().next();
/* 138 */     if (!(rti instanceof RuntimeNonElement)) return false;
/*     */ 
/* 140 */     if (info.id() == ID.IDREF)
/*     */     {
/* 142 */       return true;
/*     */     }
/* 144 */     if (((RuntimeNonElement)rti).getTransducer() == null)
/*     */     {
/* 148 */       return false;
/*     */     }
/* 150 */     if (!info.getIndividualType().equals(rti.getType())) {
/* 151 */       return false;
/*     */     }
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  57 */     Class[] implClasses = { SingleElementLeafProperty.class, null, null, ArrayElementLeafProperty.class, null, null, SingleElementNodeProperty.class, SingleReferenceNodeProperty.class, SingleMapNodeProperty.class, ArrayElementNodeProperty.class, ArrayReferenceNodeProperty.class, null };
/*     */ 
/*  75 */     propImpls = new Constructor[implClasses.length];
/*  76 */     for (int i = 0; i < propImpls.length; i++)
/*  77 */       if (implClasses[i] != null)
/*     */       {
/*  79 */         propImpls[i] = implClasses[i].getConstructors()[0];
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory
 * JD-Core Version:    0.6.2
 */