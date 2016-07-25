/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class FieldAccessor_Boolean extends Accessor
/*    */ {
/*    */   public FieldAccessor_Boolean()
/*    */   {
/* 40 */     super(Boolean.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Boolean.valueOf(((Bean)bean).f_boolean);
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).f_boolean = (value == null ? Const.default_value_boolean : ((Boolean)value).booleanValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.FieldAccessor_Boolean
 * JD-Core Version:    0.6.2
 */