/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class PersistentSearchControl extends BasicControl
/*     */ {
/*     */   public static final String OID = "2.16.840.1.113730.3.4.3";
/*     */   public static final int ADD = 1;
/*     */   public static final int DELETE = 2;
/*     */   public static final int MODIFY = 4;
/*     */   public static final int RENAME = 8;
/*     */   public static final int ANY = 15;
/*  88 */   private int changeTypes = 15;
/*     */ 
/*  95 */   private boolean changesOnly = false;
/*     */ 
/* 102 */   private boolean returnControls = true;
/*     */   private static final long serialVersionUID = 6335140491154854116L;
/*     */ 
/*     */   public PersistentSearchControl()
/*     */     throws IOException
/*     */   {
/* 115 */     super("2.16.840.1.113730.3.4.3");
/* 116 */     this.value = setEncodedValue();
/*     */   }
/*     */ 
/*     */   public PersistentSearchControl(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */     throws IOException
/*     */   {
/* 132 */     super("2.16.840.1.113730.3.4.3", paramBoolean3, null);
/* 133 */     this.changeTypes = paramInt;
/* 134 */     this.changesOnly = paramBoolean1;
/* 135 */     this.returnControls = paramBoolean2;
/* 136 */     this.value = setEncodedValue();
/*     */   }
/*     */ 
/*     */   private byte[] setEncodedValue()
/*     */     throws IOException
/*     */   {
/* 151 */     BerEncoder localBerEncoder = new BerEncoder(32);
/*     */ 
/* 153 */     localBerEncoder.beginSeq(48);
/* 154 */     localBerEncoder.encodeInt(this.changeTypes);
/* 155 */     localBerEncoder.encodeBoolean(this.changesOnly);
/* 156 */     localBerEncoder.encodeBoolean(this.returnControls);
/* 157 */     localBerEncoder.endSeq();
/*     */ 
/* 159 */     return localBerEncoder.getTrimmedBuf();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.PersistentSearchControl
 * JD-Core Version:    0.6.2
 */