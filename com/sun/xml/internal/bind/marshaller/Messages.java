/*    */ package com.sun.xml.internal.bind.marshaller;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ public class Messages
/*    */ {
/*    */   public static final String NOT_MARSHALLABLE = "MarshallerImpl.NotMarshallable";
/*    */   public static final String UNSUPPORTED_RESULT = "MarshallerImpl.UnsupportedResult";
/*    */   public static final String UNSUPPORTED_ENCODING = "MarshallerImpl.UnsupportedEncoding";
/*    */   public static final String NULL_WRITER = "MarshallerImpl.NullWriterParam";
/*    */   public static final String ASSERT_FAILED = "SAXMarshaller.AssertFailed";
/*    */ 
/*    */   /** @deprecated */
/*    */   public static final String ERR_MISSING_OBJECT = "SAXMarshaller.MissingObject";
/*    */ 
/*    */   /** @deprecated */
/*    */   public static final String ERR_MISSING_OBJECT2 = "SAXMarshaller.MissingObject2";
/*    */ 
/*    */   /** @deprecated */
/*    */   public static final String ERR_DANGLING_IDREF = "SAXMarshaller.DanglingIDREF";
/*    */ 
/*    */   /** @deprecated */
/*    */   public static final String ERR_NOT_IDENTIFIABLE = "SAXMarshaller.NotIdentifiable";
/*    */   public static final String DOM_IMPL_DOESNT_SUPPORT_CREATELEMENTNS = "SAX2DOMEx.DomImplDoesntSupportCreateElementNs";
/*    */ 
/*    */   public static String format(String property)
/*    */   {
/* 39 */     return format(property, null);
/*    */   }
/*    */ 
/*    */   public static String format(String property, Object arg1) {
/* 43 */     return format(property, new Object[] { arg1 });
/*    */   }
/*    */ 
/*    */   public static String format(String property, Object arg1, Object arg2) {
/* 47 */     return format(property, new Object[] { arg1, arg2 });
/*    */   }
/*    */ 
/*    */   public static String format(String property, Object arg1, Object arg2, Object arg3) {
/* 51 */     return format(property, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */ 
/*    */   static String format(String property, Object[] args)
/*    */   {
/* 58 */     String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
/* 59 */     return MessageFormat.format(text, args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.Messages
 * JD-Core Version:    0.6.2
 */