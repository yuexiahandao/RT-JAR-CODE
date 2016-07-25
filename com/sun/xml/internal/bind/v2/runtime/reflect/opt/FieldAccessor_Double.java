/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class FieldAccessor_Double extends Accessor
/*    */ {
/*    */   public FieldAccessor_Double()
/*    */   {
/* 40 */     super(Double.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Double.valueOf(((Bean)bean).f_double);
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).f_double = (value == null ? Const.default_value_double : ((Double)value).doubleValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.FieldAccessor_Double
 * JD-Core Version:    0.6.2
 */