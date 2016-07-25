/*    */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ public final class OutputSettings
/*    */ {
/* 33 */   private String _cdata_section_elements = null;
/* 34 */   private String _doctype_public = null;
/* 35 */   private String _encoding = null;
/* 36 */   private String _indent = null;
/* 37 */   private String _media_type = null;
/* 38 */   private String _method = null;
/* 39 */   private String _omit_xml_declaration = null;
/* 40 */   private String _standalone = null;
/* 41 */   private String _version = null;
/*    */ 
/*    */   public Properties getProperties() {
/* 44 */     Properties properties = new Properties();
/* 45 */     return properties;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.OutputSettings
 * JD-Core Version:    0.6.2
 */