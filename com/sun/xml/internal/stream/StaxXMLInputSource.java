/*    */ package com.sun.xml.internal.stream;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import javax.xml.stream.XMLEventReader;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class StaxXMLInputSource
/*    */ {
/*    */   XMLStreamReader fStreamReader;
/*    */   XMLEventReader fEventReader;
/*    */   XMLInputSource fInputSource;
/* 47 */   boolean fHasResolver = false;
/*    */ 
/*    */   public StaxXMLInputSource(XMLStreamReader streamReader)
/*    */   {
/* 51 */     this.fStreamReader = streamReader;
/*    */   }
/*    */ 
/*    */   public StaxXMLInputSource(XMLEventReader eventReader)
/*    */   {
/* 56 */     this.fEventReader = eventReader;
/*    */   }
/*    */ 
/*    */   public StaxXMLInputSource(XMLInputSource inputSource) {
/* 60 */     this.fInputSource = inputSource;
/*    */   }
/*    */ 
/*    */   public StaxXMLInputSource(XMLInputSource inputSource, boolean hasResolver)
/*    */   {
/* 65 */     this.fInputSource = inputSource;
/* 66 */     this.fHasResolver = hasResolver;
/*    */   }
/*    */ 
/*    */   public XMLStreamReader getXMLStreamReader() {
/* 70 */     return this.fStreamReader;
/*    */   }
/*    */ 
/*    */   public XMLEventReader getXMLEventReader() {
/* 74 */     return this.fEventReader;
/*    */   }
/*    */ 
/*    */   public XMLInputSource getXMLInputSource() {
/* 78 */     return this.fInputSource;
/*    */   }
/*    */ 
/*    */   public boolean hasXMLStreamOrXMLEventReader() {
/* 82 */     return (this.fStreamReader != null) || (this.fEventReader != null);
/*    */   }
/*    */ 
/*    */   public boolean hasResolver() {
/* 86 */     return this.fHasResolver;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.StaxXMLInputSource
 * JD-Core Version:    0.6.2
 */