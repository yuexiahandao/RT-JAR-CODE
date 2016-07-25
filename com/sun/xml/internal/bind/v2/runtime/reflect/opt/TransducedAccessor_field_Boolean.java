/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*    */ 
/*    */ import com.sun.xml.internal.bind.DatatypeConverterImpl;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor;
/*    */ 
/*    */ public final class TransducedAccessor_field_Boolean extends DefaultTransducedAccessor
/*    */ {
/*    */   public String print(Object o)
/*    */   {
/* 44 */     return DatatypeConverterImpl._printBoolean(((Bean)o).f_boolean);
/*    */   }
/*    */ 
/*    */   public void parse(Object o, CharSequence lexical) {
/* 48 */     Boolean b = DatatypeConverterImpl._parseBoolean(lexical);
/*    */ 
/* 50 */     if (b != null)
/* 51 */       ((Bean)o).f_boolean = b.booleanValue();
/*    */   }
/*    */ 
/*    */   public boolean hasValue(Object o) {
/* 55 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.TransducedAccessor_field_Boolean
 * JD-Core Version:    0.6.2
 */