/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class ParameterMode
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 23 */   private static int __size = 3;
/* 24 */   private static ParameterMode[] __array = new ParameterMode[__size];
/*    */   public static final int _PARAM_IN = 0;
/* 27 */   public static final ParameterMode PARAM_IN = new ParameterMode(0);
/*    */   public static final int _PARAM_OUT = 1;
/* 29 */   public static final ParameterMode PARAM_OUT = new ParameterMode(1);
/*    */   public static final int _PARAM_INOUT = 2;
/* 31 */   public static final ParameterMode PARAM_INOUT = new ParameterMode(2);
/*    */ 
/*    */   public int value()
/*    */   {
/* 35 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static ParameterMode from_int(int paramInt)
/*    */   {
/* 40 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 41 */       return __array[paramInt];
/*    */     }
/* 43 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected ParameterMode(int paramInt)
/*    */   {
/* 48 */     this.__value = paramInt;
/* 49 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ParameterMode
 * JD-Core Version:    0.6.2
 */