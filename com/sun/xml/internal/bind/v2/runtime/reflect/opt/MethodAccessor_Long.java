/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class MethodAccessor_Long extends Accessor
/*    */ {
/*    */   public MethodAccessor_Long()
/*    */   {
/* 40 */     super(Long.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Long.valueOf(((Bean)bean).get_long());
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).set_long(value == null ? Const.default_value_long : ((Long)value).longValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.MethodAccessor_Long
 * JD-Core Version:    0.6.2
 */