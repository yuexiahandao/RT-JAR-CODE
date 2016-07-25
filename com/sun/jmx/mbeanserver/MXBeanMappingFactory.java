/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ 
/*     */ public abstract class MXBeanMappingFactory
/*     */ {
/* 102 */   public static final MXBeanMappingFactory DEFAULT = new DefaultMXBeanMappingFactory();
/*     */ 
/*     */   public abstract MXBeanMapping mappingForType(Type paramType, MXBeanMappingFactory paramMXBeanMappingFactory)
/*     */     throws OpenDataException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MXBeanMappingFactory
 * JD-Core Version:    0.6.2
 */