/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class NotFoundReason
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 18 */   private static int __size = 3;
/* 19 */   private static NotFoundReason[] __array = new NotFoundReason[__size];
/*    */   public static final int _missing_node = 0;
/* 22 */   public static final NotFoundReason missing_node = new NotFoundReason(0);
/*    */   public static final int _not_context = 1;
/* 24 */   public static final NotFoundReason not_context = new NotFoundReason(1);
/*    */   public static final int _not_object = 2;
/* 26 */   public static final NotFoundReason not_object = new NotFoundReason(2);
/*    */ 
/*    */   public int value()
/*    */   {
/* 30 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static NotFoundReason from_int(int paramInt)
/*    */   {
/* 35 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 36 */       return __array[paramInt];
/*    */     }
/* 38 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected NotFoundReason(int paramInt)
/*    */   {
/* 43 */     this.__value = paramInt;
/* 44 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotFoundReason
 * JD-Core Version:    0.6.2
 */