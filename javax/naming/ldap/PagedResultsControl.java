/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import com.sun.jndi.ldap.BerEncoder;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class PagedResultsControl extends BasicControl
/*     */ {
/*     */   public static final String OID = "1.2.840.113556.1.4.319";
/* 121 */   private static final byte[] EMPTY_COOKIE = new byte[0];
/*     */   private static final long serialVersionUID = 6684806685736844298L;
/*     */ 
/*     */   public PagedResultsControl(int paramInt, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 141 */     super("1.2.840.113556.1.4.319", paramBoolean, null);
/* 142 */     this.value = setEncodedValue(paramInt, EMPTY_COOKIE);
/*     */   }
/*     */ 
/*     */   public PagedResultsControl(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 167 */     super("1.2.840.113556.1.4.319", paramBoolean, null);
/* 168 */     if (paramArrayOfByte == null) {
/* 169 */       paramArrayOfByte = EMPTY_COOKIE;
/*     */     }
/* 171 */     this.value = setEncodedValue(paramInt, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   private byte[] setEncodedValue(int paramInt, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 189 */     BerEncoder localBerEncoder = new BerEncoder(10 + paramArrayOfByte.length);
/*     */ 
/* 191 */     localBerEncoder.beginSeq(48);
/* 192 */     localBerEncoder.encodeInt(paramInt);
/* 193 */     localBerEncoder.encodeOctetString(paramArrayOfByte, 4);
/* 194 */     localBerEncoder.endSeq();
/*     */ 
/* 196 */     return localBerEncoder.getTrimmedBuf();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.PagedResultsControl
 * JD-Core Version:    0.6.2
 */