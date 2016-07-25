/*    */ package com.sun.corba.se.spi.copyobject;
/*    */ 
/*    */ import com.sun.corba.se.impl.copyobject.FallbackObjectCopierImpl;
/*    */ import com.sun.corba.se.impl.copyobject.JavaStreamObjectCopierImpl;
/*    */ import com.sun.corba.se.impl.copyobject.ORBStreamObjectCopierImpl;
/*    */ import com.sun.corba.se.impl.copyobject.ReferenceObjectCopierImpl;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ 
/*    */ public abstract class CopyobjectDefaults
/*    */ {
/* 64 */   private static final ObjectCopier referenceObjectCopier = new ReferenceObjectCopierImpl();
/*    */ 
/* 66 */   private static ObjectCopierFactory referenceObjectCopierFactory = new ObjectCopierFactory()
/*    */   {
/*    */     public ObjectCopier make()
/*    */     {
/* 70 */       return CopyobjectDefaults.referenceObjectCopier;
/*    */     }
/* 66 */   };
/*    */ 
/*    */   public static ObjectCopierFactory makeORBStreamObjectCopierFactory(ORB paramORB)
/*    */   {
/* 46 */     return new ObjectCopierFactory()
/*    */     {
/*    */       public ObjectCopier make() {
/* 49 */         return new ORBStreamObjectCopierImpl(this.val$orb);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public static ObjectCopierFactory makeJavaStreamObjectCopierFactory(ORB paramORB)
/*    */   {
/* 56 */     return new ObjectCopierFactory()
/*    */     {
/*    */       public ObjectCopier make() {
/* 59 */         return new JavaStreamObjectCopierImpl(this.val$orb);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public static ObjectCopierFactory getReferenceObjectCopierFactory()
/*    */   {
/* 79 */     return referenceObjectCopierFactory;
/*    */   }
/*    */ 
/*    */   public static ObjectCopierFactory makeFallbackObjectCopierFactory(ObjectCopierFactory paramObjectCopierFactory1, final ObjectCopierFactory paramObjectCopierFactory2)
/*    */   {
/* 91 */     return new ObjectCopierFactory()
/*    */     {
/*    */       public ObjectCopier make() {
/* 94 */         ObjectCopier localObjectCopier1 = this.val$f1.make();
/* 95 */         ObjectCopier localObjectCopier2 = paramObjectCopierFactory2.make();
/* 96 */         return new FallbackObjectCopierImpl(localObjectCopier1, localObjectCopier2);
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.copyobject.CopyobjectDefaults
 * JD-Core Version:    0.6.2
 */