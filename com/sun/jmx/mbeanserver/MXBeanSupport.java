/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class MXBeanSupport extends MBeanSupport<ConvertingMethod>
/*     */ {
/* 174 */   private final Object lock = new Object();
/*     */   private MXBeanLookup mxbeanLookup;
/*     */   private ObjectName objectName;
/*     */ 
/*     */   public <T> MXBeanSupport(T paramT, Class<T> paramClass)
/*     */     throws NotCompliantMBeanException
/*     */   {
/*  66 */     super(paramT, paramClass);
/*     */   }
/*     */ 
/*     */   MBeanIntrospector<ConvertingMethod> getMBeanIntrospector()
/*     */   {
/*  71 */     return MXBeanIntrospector.getInstance();
/*     */   }
/*     */ 
/*     */   Object getCookie()
/*     */   {
/*  76 */     return this.mxbeanLookup;
/*     */   }
/*     */ 
/*     */   static <T> Class<? super T> findMXBeanInterface(Class<T> paramClass) {
/*  80 */     if (paramClass == null)
/*  81 */       throw new IllegalArgumentException("Null resource class");
/*  82 */     Set localSet1 = transitiveInterfaces(paramClass);
/*  83 */     Set localSet2 = Util.newSet();
/*  84 */     for (Object localObject = localSet1.iterator(); ((Iterator)localObject).hasNext(); ) { localClass1 = (Class)((Iterator)localObject).next();
/*  85 */       if (JMX.isMXBeanInterface(localClass1))
/*  86 */         localSet2.add(localClass1);
/*     */     }
/*  90 */     Class localClass1;
/*  89 */     if (localSet2.size() > 1) { localObject = localSet2.iterator();
/*     */       label167: 
/*     */       while (true) { if (!((Iterator)localObject).hasNext()) break label170; localClass1 = (Class)((Iterator)localObject).next();
/*  91 */         Iterator localIterator = localSet2.iterator();
/*     */         while (true) { if (!localIterator.hasNext())
/*     */             break label167;
/*  93 */           Class localClass2 = (Class)localIterator.next();
/*  94 */           if ((localClass1 != localClass2) && (localClass2.isAssignableFrom(localClass1))) {
/*  95 */             localIterator.remove();
/*  96 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 100 */       label170: localObject = "Class " + paramClass.getName() + " implements more than " + "one MXBean interface: " + localSet2;
/*     */ 
/* 103 */       throw new IllegalArgumentException((String)localObject);
/*     */     }
/* 105 */     if (localSet2.iterator().hasNext()) {
/* 106 */       return (Class)Util.cast(localSet2.iterator().next());
/*     */     }
/* 108 */     localObject = "Class " + paramClass.getName() + " is not a JMX compliant MXBean";
/*     */ 
/* 111 */     throw new IllegalArgumentException((String)localObject);
/*     */   }
/*     */ 
/*     */   private static Set<Class<?>> transitiveInterfaces(Class<?> paramClass)
/*     */   {
/* 119 */     Set localSet = Util.newSet();
/* 120 */     transitiveInterfaces(paramClass, localSet);
/* 121 */     return localSet;
/*     */   }
/*     */   private static void transitiveInterfaces(Class<?> paramClass, Set<Class<?>> paramSet) {
/* 124 */     if (paramClass == null)
/* 125 */       return;
/* 126 */     if (paramClass.isInterface())
/* 127 */       paramSet.add(paramClass);
/* 128 */     transitiveInterfaces(paramClass.getSuperclass(), paramSet);
/* 129 */     for (Class localClass : paramClass.getInterfaces())
/* 130 */       transitiveInterfaces(localClass, paramSet);
/*     */   }
/*     */ 
/*     */   public void register(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */     throws InstanceAlreadyExistsException
/*     */   {
/* 154 */     if (paramObjectName == null) {
/* 155 */       throw new IllegalArgumentException("Null object name");
/*     */     }
/*     */ 
/* 158 */     synchronized (this.lock) {
/* 159 */       this.mxbeanLookup = MXBeanLookup.lookupFor(paramMBeanServer);
/* 160 */       this.mxbeanLookup.addReference(paramObjectName, getResource());
/* 161 */       this.objectName = paramObjectName;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregister()
/*     */   {
/* 167 */     synchronized (this.lock) {
/* 168 */       if ((this.mxbeanLookup != null) && 
/* 169 */         (this.mxbeanLookup.removeReference(this.objectName, getResource())))
/* 170 */         this.objectName = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MXBeanSupport
 * JD-Core Version:    0.6.2
 */