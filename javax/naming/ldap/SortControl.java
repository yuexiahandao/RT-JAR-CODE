/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import com.sun.jndi.ldap.BerEncoder;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class SortControl extends BasicControl
/*     */ {
/*     */   public static final String OID = "1.2.840.113556.1.4.473";
/*     */   private static final long serialVersionUID = -1965961680233330744L;
/*     */ 
/*     */   public SortControl(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 135 */     super("1.2.840.113556.1.4.473", paramBoolean, null);
/* 136 */     this.value = setEncodedValue(new SortKey[] { new SortKey(paramString) });
/*     */   }
/*     */ 
/*     */   public SortControl(String[] paramArrayOfString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 158 */     super("1.2.840.113556.1.4.473", paramBoolean, null);
/* 159 */     SortKey[] arrayOfSortKey = new SortKey[paramArrayOfString.length];
/* 160 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 161 */       arrayOfSortKey[i] = new SortKey(paramArrayOfString[i]);
/*     */     }
/* 163 */     this.value = setEncodedValue(arrayOfSortKey);
/*     */   }
/*     */ 
/*     */   public SortControl(SortKey[] paramArrayOfSortKey, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 184 */     super("1.2.840.113556.1.4.473", paramBoolean, null);
/* 185 */     this.value = setEncodedValue(paramArrayOfSortKey);
/*     */   }
/*     */ 
/*     */   private byte[] setEncodedValue(SortKey[] paramArrayOfSortKey)
/*     */     throws IOException
/*     */   {
/* 201 */     BerEncoder localBerEncoder = new BerEncoder(30 * paramArrayOfSortKey.length + 10);
/*     */ 
/* 204 */     localBerEncoder.beginSeq(48);
/*     */ 
/* 206 */     for (int i = 0; i < paramArrayOfSortKey.length; i++) {
/* 207 */       localBerEncoder.beginSeq(48);
/* 208 */       localBerEncoder.encodeString(paramArrayOfSortKey[i].getAttributeID(), true);
/*     */       String str;
/* 210 */       if ((str = paramArrayOfSortKey[i].getMatchingRuleID()) != null) {
/* 211 */         localBerEncoder.encodeString(str, 128, true);
/*     */       }
/* 213 */       if (!paramArrayOfSortKey[i].isAscending()) {
/* 214 */         localBerEncoder.encodeBoolean(true, 129);
/*     */       }
/* 216 */       localBerEncoder.endSeq();
/*     */     }
/* 218 */     localBerEncoder.endSeq();
/*     */ 
/* 220 */     return localBerEncoder.getTrimmedBuf();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.SortControl
 * JD-Core Version:    0.6.2
 */