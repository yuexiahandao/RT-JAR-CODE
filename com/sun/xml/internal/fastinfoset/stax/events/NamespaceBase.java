/*    */ package com.sun.xml.internal.fastinfoset.stax.events;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.events.Namespace;
/*    */ 
/*    */ public class NamespaceBase extends AttributeBase
/*    */   implements Namespace
/*    */ {
/*    */   static final String DEFAULT_NS_PREFIX = "";
/*    */   static final String XML_NS_URI = "http://www.w3.org/XML/1998/namespace";
/*    */   static final String XML_NS_PREFIX = "xml";
/*    */   static final String XMLNS_ATTRIBUTE_NS_URI = "http://www.w3.org/2000/xmlns/";
/*    */   static final String XMLNS_ATTRIBUTE = "xmlns";
/*    */   static final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
/*    */   static final String W3C_XML_SCHEMA_INSTANCE_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";
/* 45 */   private boolean defaultDeclaration = false;
/*    */ 
/*    */   public NamespaceBase(String namespaceURI)
/*    */   {
/* 49 */     super("xmlns", "", namespaceURI);
/* 50 */     setEventType(13);
/*    */   }
/*    */ 
/*    */   public NamespaceBase(String prefix, String namespaceURI)
/*    */   {
/* 59 */     super("xmlns", prefix, namespaceURI);
/* 60 */     setEventType(13);
/* 61 */     if (Util.isEmptyString(prefix))
/* 62 */       this.defaultDeclaration = true;
/*    */   }
/*    */ 
/*    */   void setPrefix(String prefix)
/*    */   {
/* 67 */     if (prefix == null)
/* 68 */       setName(new QName("http://www.w3.org/2000/xmlns/", "", "xmlns"));
/*    */     else
/* 70 */       setName(new QName("http://www.w3.org/2000/xmlns/", prefix, "xmlns"));
/*    */   }
/*    */ 
/*    */   public String getPrefix() {
/* 74 */     if (this.defaultDeclaration) return "";
/* 75 */     return super.getLocalName();
/*    */   }
/*    */ 
/*    */   void setNamespaceURI(String uri)
/*    */   {
/* 84 */     setValue(uri);
/*    */   }
/*    */   public String getNamespaceURI() {
/* 87 */     return getValue();
/*    */   }
/*    */ 
/*    */   public boolean isNamespace()
/*    */   {
/* 92 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean isDefaultNamespaceDeclaration() {
/* 96 */     return this.defaultDeclaration;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.NamespaceBase
 * JD-Core Version:    0.6.2
 */