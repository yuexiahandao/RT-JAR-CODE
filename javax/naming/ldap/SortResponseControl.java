/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import com.sun.jndi.ldap.BerDecoder;
/*     */ import com.sun.jndi.ldap.LdapCtx;
/*     */ import java.io.IOException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public final class SortResponseControl extends BasicControl
/*     */ {
/*     */   public static final String OID = "1.2.840.113556.1.4.474";
/*     */   private static final long serialVersionUID = 5142939176006310877L;
/*  95 */   private int resultCode = 0;
/*     */ 
/* 102 */   private String badAttrId = null;
/*     */ 
/*     */   public SortResponseControl(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 118 */     super(paramString, paramBoolean, paramArrayOfByte);
/*     */ 
/* 121 */     BerDecoder localBerDecoder = new BerDecoder(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 123 */     localBerDecoder.parseSeq(null);
/* 124 */     this.resultCode = localBerDecoder.parseEnumeration();
/* 125 */     if ((localBerDecoder.bytesLeft() > 0) && (localBerDecoder.peekByte() == 128))
/* 126 */       this.badAttrId = localBerDecoder.parseStringWithTag(128, true, null);
/*     */   }
/*     */ 
/*     */   public boolean isSorted()
/*     */   {
/* 137 */     return this.resultCode == 0;
/*     */   }
/*     */ 
/*     */   public int getResultCode()
/*     */   {
/* 146 */     return this.resultCode;
/*     */   }
/*     */ 
/*     */   public String getAttributeID()
/*     */   {
/* 156 */     return this.badAttrId;
/*     */   }
/*     */ 
/*     */   public NamingException getException()
/*     */   {
/* 167 */     return LdapCtx.mapErrorCode(this.resultCode, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.SortResponseControl
 * JD-Core Version:    0.6.2
 */