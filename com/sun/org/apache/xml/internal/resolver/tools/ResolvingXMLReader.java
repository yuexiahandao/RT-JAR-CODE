/*    */ package com.sun.org.apache.xml.internal.resolver.tools;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*    */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*    */ import javax.xml.parsers.SAXParser;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ 
/*    */ public class ResolvingXMLReader extends ResolvingXMLFilter
/*    */ {
/* 51 */   public static boolean namespaceAware = true;
/*    */ 
/* 54 */   public static boolean validating = false;
/*    */ 
/*    */   public ResolvingXMLReader()
/*    */   {
/* 65 */     SAXParserFactory spf = this.catalogManager.useServicesMechanism() ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*    */ 
/* 67 */     spf.setNamespaceAware(namespaceAware);
/* 68 */     spf.setValidating(validating);
/*    */     try {
/* 70 */       SAXParser parser = spf.newSAXParser();
/* 71 */       setParent(parser.getXMLReader());
/*    */     } catch (Exception ex) {
/* 73 */       ex.printStackTrace();
/*    */     }
/*    */   }
/*    */ 
/*    */   public ResolvingXMLReader(CatalogManager manager)
/*    */   {
/* 85 */     super(manager);
/* 86 */     SAXParserFactory spf = this.catalogManager.useServicesMechanism() ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*    */ 
/* 88 */     spf.setNamespaceAware(namespaceAware);
/* 89 */     spf.setValidating(validating);
/*    */     try {
/* 91 */       SAXParser parser = spf.newSAXParser();
/* 92 */       setParent(parser.getXMLReader());
/*    */     } catch (Exception ex) {
/* 94 */       ex.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.tools.ResolvingXMLReader
 * JD-Core Version:    0.6.2
 */