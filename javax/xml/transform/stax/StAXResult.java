/*     */ package javax.xml.transform.stax;
/*     */ 
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ 
/*     */ public class StAXResult
/*     */   implements Result
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.stax.StAXResult/feature";
/*  63 */   private XMLEventWriter xmlEventWriter = null;
/*     */ 
/*  69 */   private XMLStreamWriter xmlStreamWriter = null;
/*     */ 
/*  72 */   private String systemId = null;
/*     */ 
/*     */   public StAXResult(XMLEventWriter xmlEventWriter)
/*     */   {
/*  89 */     if (xmlEventWriter == null) {
/*  90 */       throw new IllegalArgumentException("StAXResult(XMLEventWriter) with XMLEventWriter == null");
/*     */     }
/*     */ 
/*  94 */     this.xmlEventWriter = xmlEventWriter;
/*     */   }
/*     */ 
/*     */   public StAXResult(XMLStreamWriter xmlStreamWriter)
/*     */   {
/* 112 */     if (xmlStreamWriter == null) {
/* 113 */       throw new IllegalArgumentException("StAXResult(XMLStreamWriter) with XMLStreamWriter == null");
/*     */     }
/*     */ 
/* 117 */     this.xmlStreamWriter = xmlStreamWriter;
/*     */   }
/*     */ 
/*     */   public XMLEventWriter getXMLEventWriter()
/*     */   {
/* 133 */     return this.xmlEventWriter;
/*     */   }
/*     */ 
/*     */   public XMLStreamWriter getXMLStreamWriter()
/*     */   {
/* 149 */     return this.xmlStreamWriter;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 169 */     throw new UnsupportedOperationException("StAXResult#setSystemId(systemId) cannot set the system identifier for a StAXResult");
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 181 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.stax.StAXResult
 * JD-Core Version:    0.6.2
 */