/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ 
/*    */ public class MethodAccessor_Character extends Accessor
/*    */ {
/*    */   public MethodAccessor_Character()
/*    */   {
/* 40 */     super(Character.class);
/*    */   }
/*    */ 
/*    */   public Object get(Object bean) {
/* 44 */     return Character.valueOf(((Bean)bean).get_char());
/*    */   }
/*    */ 
/*    */   public void set(Object bean, Object value) {
/* 48 */     ((Bean)bean).set_char(value == null ? Const.default_value_char : ((Character)value).charValue());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.MethodAccessor_Character
 * JD-Core Version:    0.6.2
 */