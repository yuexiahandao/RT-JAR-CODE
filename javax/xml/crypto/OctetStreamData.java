/*     */ package javax.xml.crypto;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class OctetStreamData
/*     */   implements Data
/*     */ {
/*     */   private InputStream octetStream;
/*     */   private String uri;
/*     */   private String mimeType;
/*     */ 
/*     */   public OctetStreamData(InputStream paramInputStream)
/*     */   {
/*  51 */     if (paramInputStream == null) {
/*  52 */       throw new NullPointerException("octetStream is null");
/*     */     }
/*  54 */     this.octetStream = paramInputStream;
/*     */   }
/*     */ 
/*     */   public OctetStreamData(InputStream paramInputStream, String paramString1, String paramString2)
/*     */   {
/*  70 */     if (paramInputStream == null) {
/*  71 */       throw new NullPointerException("octetStream is null");
/*     */     }
/*  73 */     this.octetStream = paramInputStream;
/*  74 */     this.uri = paramString1;
/*  75 */     this.mimeType = paramString2;
/*     */   }
/*     */ 
/*     */   public InputStream getOctetStream()
/*     */   {
/*  84 */     return this.octetStream;
/*     */   }
/*     */ 
/*     */   public String getURI()
/*     */   {
/*  94 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public String getMimeType()
/*     */   {
/* 104 */     return this.mimeType;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.OctetStreamData
 * JD-Core Version:    0.6.2
 */