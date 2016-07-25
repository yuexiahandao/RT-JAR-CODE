/*    */ package com.sun.jmx.mbeanserver;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.WeakHashMap;
/*    */ import javax.management.Descriptor;
/*    */ import javax.management.ImmutableDescriptor;
/*    */ import javax.management.JMX;
/*    */ 
/*    */ public class DescriptorCache
/*    */ {
/* 62 */   private static final DescriptorCache instance = new DescriptorCache();
/* 63 */   private final WeakHashMap<ImmutableDescriptor, WeakReference<ImmutableDescriptor>> map = new WeakHashMap();
/*    */ 
/*    */   static DescriptorCache getInstance()
/*    */   {
/* 39 */     return instance;
/*    */   }
/*    */ 
/*    */   public static DescriptorCache getInstance(JMX paramJMX) {
/* 43 */     if (paramJMX != null) {
/* 44 */       return instance;
/*    */     }
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   public ImmutableDescriptor get(ImmutableDescriptor paramImmutableDescriptor) {
/* 50 */     WeakReference localWeakReference = (WeakReference)this.map.get(paramImmutableDescriptor);
/* 51 */     ImmutableDescriptor localImmutableDescriptor = localWeakReference == null ? null : (ImmutableDescriptor)localWeakReference.get();
/* 52 */     if (localImmutableDescriptor != null)
/* 53 */       return localImmutableDescriptor;
/* 54 */     this.map.put(paramImmutableDescriptor, new WeakReference(paramImmutableDescriptor));
/* 55 */     return paramImmutableDescriptor;
/*    */   }
/*    */ 
/*    */   public ImmutableDescriptor union(Descriptor[] paramArrayOfDescriptor) {
/* 59 */     return get(ImmutableDescriptor.union(paramArrayOfDescriptor));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.DescriptorCache
 * JD-Core Version:    0.6.2
 */