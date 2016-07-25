/*     */ package javax.xml.transform.sax;
/*     */ 
/*     */ import javax.xml.transform.Result;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class SAXResult
/*     */   implements Result
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.sax.SAXResult/feature";
/*     */   private ContentHandler handler;
/*     */   private LexicalHandler lexhandler;
/*     */   private String systemId;
/*     */ 
/*     */   public SAXResult()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SAXResult(ContentHandler handler)
/*     */   {
/*  60 */     setHandler(handler);
/*     */   }
/*     */ 
/*     */   public void setHandler(ContentHandler handler)
/*     */   {
/*  69 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   public ContentHandler getHandler()
/*     */   {
/*  78 */     return this.handler;
/*     */   }
/*     */ 
/*     */   public void setLexicalHandler(LexicalHandler handler)
/*     */   {
/*  93 */     this.lexhandler = handler;
/*     */   }
/*     */ 
/*     */   public LexicalHandler getLexicalHandler()
/*     */   {
/* 102 */     return this.lexhandler;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 112 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 122 */     return this.systemId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.sax.SAXResult
 * JD-Core Version:    0.6.2
 */