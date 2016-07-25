/*     */ package javax.xml.transform.stax;
/*     */ 
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public class StAXSource
/*     */   implements Source
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.stax.StAXSource/feature";
/*  67 */   private XMLEventReader xmlEventReader = null;
/*     */ 
/*  70 */   private XMLStreamReader xmlStreamReader = null;
/*     */ 
/*  73 */   private String systemId = null;
/*     */ 
/*     */   public StAXSource(XMLEventReader xmlEventReader)
/*     */     throws XMLStreamException
/*     */   {
/* 100 */     if (xmlEventReader == null) {
/* 101 */       throw new IllegalArgumentException("StAXSource(XMLEventReader) with XMLEventReader == null");
/*     */     }
/*     */ 
/* 111 */     XMLEvent event = xmlEventReader.peek();
/* 112 */     int eventType = event.getEventType();
/* 113 */     if ((eventType != 7) && (eventType != 1))
/*     */     {
/* 115 */       throw new IllegalStateException("StAXSource(XMLEventReader) with XMLEventReader not in XMLStreamConstants.START_DOCUMENT or XMLStreamConstants.START_ELEMENT state");
/*     */     }
/*     */ 
/* 121 */     this.xmlEventReader = xmlEventReader;
/* 122 */     this.systemId = event.getLocation().getSystemId();
/*     */   }
/*     */ 
/*     */   public StAXSource(XMLStreamReader xmlStreamReader)
/*     */   {
/* 147 */     if (xmlStreamReader == null) {
/* 148 */       throw new IllegalArgumentException("StAXSource(XMLStreamReader) with XMLStreamReader == null");
/*     */     }
/*     */ 
/* 152 */     int eventType = xmlStreamReader.getEventType();
/* 153 */     if ((eventType != 7) && (eventType != 1))
/*     */     {
/* 155 */       throw new IllegalStateException("StAXSource(XMLStreamReader) with XMLStreamReadernot in XMLStreamConstants.START_DOCUMENT or XMLStreamConstants.START_ELEMENT state");
/*     */     }
/*     */ 
/* 161 */     this.xmlStreamReader = xmlStreamReader;
/* 162 */     this.systemId = xmlStreamReader.getLocation().getSystemId();
/*     */   }
/*     */ 
/*     */   public XMLEventReader getXMLEventReader()
/*     */   {
/* 178 */     return this.xmlEventReader;
/*     */   }
/*     */ 
/*     */   public XMLStreamReader getXMLStreamReader()
/*     */   {
/* 194 */     return this.xmlStreamReader;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 214 */     throw new UnsupportedOperationException("StAXSource#setSystemId(systemId) cannot set the system identifier for a StAXSource");
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 234 */     return this.systemId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.stax.StAXSource
 * JD-Core Version:    0.6.2
 */