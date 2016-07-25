/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public class AttributeMode
/*    */   implements IDLEntity
/*    */ {
/*    */   private int __value;
/* 38 */   private static int __size = 2;
/* 39 */   private static AttributeMode[] __array = new AttributeMode[__size];
/*    */   public static final int _ATTR_NORMAL = 0;
/* 42 */   public static final AttributeMode ATTR_NORMAL = new AttributeMode(0);
/*    */   public static final int _ATTR_READONLY = 1;
/* 44 */   public static final AttributeMode ATTR_READONLY = new AttributeMode(1);
/*    */ 
/*    */   public int value()
/*    */   {
/* 48 */     return this.__value;
/*    */   }
/*    */ 
/*    */   public static AttributeMode from_int(int paramInt)
/*    */   {
/* 53 */     if ((paramInt >= 0) && (paramInt < __size)) {
/* 54 */       return __array[paramInt];
/*    */     }
/* 56 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   protected AttributeMode(int paramInt)
/*    */   {
/* 61 */     this.__value = paramInt;
/* 62 */     __array[this.__value] = this;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.AttributeMode
 * JD-Core Version:    0.6.2
 */