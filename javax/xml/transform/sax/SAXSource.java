/*     */ package javax.xml.transform.sax;
/*     */ 
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class SAXSource
/*     */   implements Source
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.sax.SAXSource/feature";
/*     */   private XMLReader reader;
/*     */   private InputSource inputSource;
/*     */ 
/*     */   public SAXSource()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SAXSource(XMLReader reader, InputSource inputSource)
/*     */   {
/*  79 */     this.reader = reader;
/*  80 */     this.inputSource = inputSource;
/*     */   }
/*     */ 
/*     */   public SAXSource(InputSource inputSource)
/*     */   {
/*  96 */     this.inputSource = inputSource;
/*     */   }
/*     */ 
/*     */   public void setXMLReader(XMLReader reader)
/*     */   {
/* 105 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */   public XMLReader getXMLReader()
/*     */   {
/* 114 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public void setInputSource(InputSource inputSource)
/*     */   {
/* 123 */     this.inputSource = inputSource;
/*     */   }
/*     */ 
/*     */   public InputSource getInputSource()
/*     */   {
/* 132 */     return this.inputSource;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 151 */     if (null == this.inputSource)
/* 152 */       this.inputSource = new InputSource(systemId);
/*     */     else
/* 154 */       this.inputSource.setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 166 */     if (this.inputSource == null) {
/* 167 */       return null;
/*     */     }
/* 169 */     return this.inputSource.getSystemId();
/*     */   }
/*     */ 
/*     */   public static InputSource sourceToInputSource(Source source)
/*     */   {
/* 194 */     if ((source instanceof SAXSource))
/* 195 */       return ((SAXSource)source).getInputSource();
/* 196 */     if ((source instanceof StreamSource)) {
/* 197 */       StreamSource ss = (StreamSource)source;
/* 198 */       InputSource isource = new InputSource(ss.getSystemId());
/*     */ 
/* 200 */       isource.setByteStream(ss.getInputStream());
/* 201 */       isource.setCharacterStream(ss.getReader());
/* 202 */       isource.setPublicId(ss.getPublicId());
/*     */ 
/* 204 */       return isource;
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.sax.SAXSource
 * JD-Core Version:    0.6.2
 */