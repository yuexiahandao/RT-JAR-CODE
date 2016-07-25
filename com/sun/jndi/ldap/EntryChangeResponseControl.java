/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class EntryChangeResponseControl extends BasicControl
/*     */ {
/*     */   public static final String OID = "2.16.840.1.113730.3.4.7";
/*     */   public static final int ADD = 1;
/*     */   public static final int DELETE = 2;
/*     */   public static final int MODIFY = 4;
/*     */   public static final int RENAME = 8;
/*     */   private int changeType;
/*  97 */   private String previousDN = null;
/*     */ 
/* 104 */   private long changeNumber = -1L;
/*     */   private static final long serialVersionUID = -2087354136750180511L;
/*     */ 
/*     */   public EntryChangeResponseControl(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 121 */     super(paramString, paramBoolean, paramArrayOfByte);
/*     */ 
/* 124 */     if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
/* 125 */       BerDecoder localBerDecoder = new BerDecoder(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */ 
/* 127 */       localBerDecoder.parseSeq(null);
/* 128 */       this.changeType = localBerDecoder.parseEnumeration();
/*     */ 
/* 130 */       if ((localBerDecoder.bytesLeft() > 0) && (localBerDecoder.peekByte() == 4)) {
/* 131 */         this.previousDN = localBerDecoder.parseString(true);
/*     */       }
/* 133 */       if ((localBerDecoder.bytesLeft() > 0) && (localBerDecoder.peekByte() == 2))
/* 134 */         this.changeNumber = localBerDecoder.parseInt();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getChangeType()
/*     */   {
/* 145 */     return this.changeType;
/*     */   }
/*     */ 
/*     */   public String getPreviousDN()
/*     */   {
/* 155 */     return this.previousDN;
/*     */   }
/*     */ 
/*     */   public long getChangeNumber()
/*     */   {
/* 165 */     return this.changeNumber;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.EntryChangeResponseControl
 * JD-Core Version:    0.6.2
 */