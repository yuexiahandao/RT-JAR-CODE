/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class DefaultValueLoaderDecorator extends Loader
/*    */ {
/*    */   private final Loader l;
/*    */   private final String defaultValue;
/*    */ 
/*    */   public DefaultValueLoaderDecorator(Loader l, String defaultValue)
/*    */   {
/* 40 */     this.l = l;
/* 41 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */   public void startElement(UnmarshallingContext.State state, TagName ea)
/*    */     throws SAXException
/*    */   {
/* 47 */     if (state.getElementDefaultValue() == null) {
/* 48 */       state.setElementDefaultValue(this.defaultValue);
/*    */     }
/* 50 */     state.setLoader(this.l);
/* 51 */     this.l.startElement(state, ea);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator
 * JD-Core Version:    0.6.2
 */