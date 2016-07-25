/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import com.sun.jndi.ldap.BerDecoder;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class PagedResultsResponseControl extends BasicControl
/*     */ {
/*     */   public static final String OID = "1.2.840.113556.1.4.319";
/*     */   private static final long serialVersionUID = -8819778744844514666L;
/*     */   private int resultSize;
/*     */   private byte[] cookie;
/*     */ 
/*     */   public PagedResultsResponseControl(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  99 */     super(paramString, paramBoolean, paramArrayOfByte);
/*     */ 
/* 102 */     BerDecoder localBerDecoder = new BerDecoder(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 104 */     localBerDecoder.parseSeq(null);
/* 105 */     this.resultSize = localBerDecoder.parseInt();
/* 106 */     this.cookie = localBerDecoder.parseOctetString(4, null);
/*     */   }
/*     */ 
/*     */   public int getResultSize()
/*     */   {
/* 115 */     return this.resultSize;
/*     */   }
/*     */ 
/*     */   public byte[] getCookie()
/*     */   {
/* 127 */     if (this.cookie.length == 0) {
/* 128 */       return null;
/*     */     }
/* 130 */     return this.cookie;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.PagedResultsResponseControl
 * JD-Core Version:    0.6.2
 */