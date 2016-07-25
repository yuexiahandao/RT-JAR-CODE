/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor;
/*    */ 
/*    */ public final class TransducedAccessor_method_Boolean extends DefaultTransducedAccessor
/*    */ {
/*    */   public String print(Object o)
/*    */   {
/* 44 */     return DatatypeConverterImpl._printBoolean(((Bean)o).get_boolean());
/*    */   }
/*    */ 
/*    */   public void parse(Object o, CharSequence lexical) {
/* 48 */     ((Bean)o).set_boolean(DatatypeConverterImpl._parseBoolean(lexical).booleanValue());
/*    */   }
/*    */ 
/*    */   public boolean hasValue(Object o) {
/* 52 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.TransducedAccessor_method_Boolean
 * JD-Core Version:    0.6.2
 */