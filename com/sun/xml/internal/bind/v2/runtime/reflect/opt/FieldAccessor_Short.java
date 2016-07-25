/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class FieldAccessor_Short extends Accessor
/*    */ {
/*    */   public FieldAccessor_Short()
/*    */   {
/* 40 */     super(Short.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Short.valueOf(((Bean)bean).f_short);
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).f_short = (value == null ? Const.default_value_short : ((Short)value).shortValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.FieldAccessor_Short
 * JD-Core Version:    0.6.2
 */