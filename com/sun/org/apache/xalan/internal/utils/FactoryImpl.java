/*    */ package com.sun.org.apache.xalan.internal.utils;
/*    */ 
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ 
/*    */ public class FactoryImpl
/*    */ {
/*    */   static final String DBF = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
/*    */   static final String SF = "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl";
/*    */ 
/*    */   public static DocumentBuilderFactory getDOMFactory(boolean useServicesMechanism)
/*    */   {
/* 41 */     DocumentBuilderFactory dbf = useServicesMechanism ? DocumentBuilderFactory.newInstance() : DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", FactoryImpl.class.getClassLoader());
/*    */ 
/* 47 */     return dbf;
/*    */   }
/*    */   public static SAXParserFactory getSAXFactory(boolean useServicesMechanism) {
/* 50 */     SAXParserFactory factory = useServicesMechanism ? SAXParserFactory.newInstance() : SAXParserFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", FactoryImpl.class.getClassLoader());
/*    */ 
/* 55 */     return factory;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.utils.FactoryImpl
 * JD-Core Version:    0.6.2
 */