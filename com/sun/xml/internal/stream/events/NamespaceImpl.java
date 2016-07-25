/*    */ package com.sun.xml.internal.stream.events;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.events.Namespace;
/*    */ 
/*    */ public class NamespaceImpl extends AttributeImpl
/*    */   implements Namespace
/*    */ {
/*    */   public NamespaceImpl()
/*    */   {
/* 40 */     init();
/*    */   }
/*    */ 
/*    */   public NamespaceImpl(String namespaceURI)
/*    */   {
/* 45 */     super("xmlns", "http://www.w3.org/2000/xmlns/", "", namespaceURI, null);
/* 46 */     init();
/*    */   }
/*    */ 
/*    */   public NamespaceImpl(String prefix, String namespaceURI) {
/* 50 */     super("xmlns", "http://www.w3.org/2000/xmlns/", prefix, namespaceURI, null);
/* 51 */     init();
/*    */   }
/*    */ 
/*    */   public boolean isDefaultNamespaceDeclaration() {
/* 55 */     QName name = getName();
/*    */ 
/* 57 */     if ((name != null) && (name.getLocalPart().equals("")))
/* 58 */       return true;
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   void setPrefix(String prefix) {
/* 63 */     if (prefix == null)
/* 64 */       setName(new QName("http://www.w3.org/2000/xmlns/", "", "xmlns"));
/*    */     else
/* 66 */       setName(new QName("http://www.w3.org/2000/xmlns/", prefix, "xmlns"));
/*    */   }
/*    */ 
/*    */   public String getPrefix()
/*    */   {
/* 72 */     QName name = getName();
/* 73 */     if (name != null)
/* 74 */       return name.getLocalPart();
/* 75 */     return null;
/*    */   }
/*    */ 
/*    */   public String getNamespaceURI()
/*    */   {
/* 81 */     return getValue();
/*    */   }
/*    */ 
/*    */   void setNamespaceURI(String uri)
/*    */   {
/* 87 */     setValue(uri);
/*    */   }
/*    */ 
/*    */   protected void init() {
/* 91 */     setEventType(13);
/*    */   }
/*    */ 
/*    */   public int getEventType() {
/* 95 */     return 13;
/*    */   }
/*    */ 
/*    */   public boolean isNamespace() {
/* 99 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.NamespaceImpl
 * JD-Core Version:    0.6.2
 */