/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import javax.xml.stream.Location;
/*    */ import javax.xml.stream.XMLEventReader;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ import javax.xml.stream.events.XMLEvent;
/*    */ 
/*    */ public final class StAXInputSource extends XMLInputSource
/*    */ {
/*    */   private final XMLStreamReader fStreamReader;
/*    */   private final XMLEventReader fEventReader;
/*    */   private final boolean fConsumeRemainingContent;
/*    */ 
/*    */   public StAXInputSource(XMLStreamReader source)
/*    */   {
/* 42 */     this(source, false);
/*    */   }
/*    */ 
/*    */   public StAXInputSource(XMLStreamReader source, boolean consumeRemainingContent) {
/* 46 */     super(null, source.getLocation().getSystemId(), null);
/* 47 */     if (source == null) {
/* 48 */       throw new IllegalArgumentException("XMLStreamReader parameter cannot be null.");
/*    */     }
/* 50 */     this.fStreamReader = source;
/* 51 */     this.fEventReader = null;
/* 52 */     this.fConsumeRemainingContent = consumeRemainingContent;
/*    */   }
/*    */ 
/*    */   public StAXInputSource(XMLEventReader source) {
/* 56 */     this(source, false);
/*    */   }
/*    */ 
/*    */   public StAXInputSource(XMLEventReader source, boolean consumeRemainingContent) {
/* 60 */     super(null, getEventReaderSystemId(source), null);
/* 61 */     if (source == null) {
/* 62 */       throw new IllegalArgumentException("XMLEventReader parameter cannot be null.");
/*    */     }
/* 64 */     this.fStreamReader = null;
/* 65 */     this.fEventReader = source;
/* 66 */     this.fConsumeRemainingContent = consumeRemainingContent;
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
/*    */   public boolean shouldConsumeRemainingContent() {
/* 78 */     return this.fConsumeRemainingContent;
/*    */   }
/*    */ 
/*    */   public void setSystemId(String systemId) {
/* 82 */     throw new UnsupportedOperationException("Cannot set the system ID on a StAXInputSource");
/*    */   }
/*    */ 
/*    */   private static String getEventReaderSystemId(XMLEventReader reader) {
/*    */     try {
/* 87 */       if (reader != null)
/* 88 */         return reader.peek().getLocation().getSystemId();
/*    */     }
/*    */     catch (XMLStreamException e) {
/*    */     }
/* 92 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.StAXInputSource
 * JD-Core Version:    0.6.2
 */