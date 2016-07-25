/*     */ package org.ietf.jgss;
/*     */ 
/*     */ public class MessageProp
/*     */ {
/*     */   private boolean privacyState;
/*     */   private int qop;
/*     */   private boolean dupToken;
/*     */   private boolean oldToken;
/*     */   private boolean unseqToken;
/*     */   private boolean gapToken;
/*     */   private int minorStatus;
/*     */   private String minorString;
/*     */ 
/*     */   public MessageProp(boolean paramBoolean)
/*     */   {
/*  73 */     this(0, paramBoolean);
/*     */   }
/*     */ 
/*     */   public MessageProp(int paramInt, boolean paramBoolean)
/*     */   {
/*  83 */     this.qop = paramInt;
/*  84 */     this.privacyState = paramBoolean;
/*  85 */     resetStatusValues();
/*     */   }
/*     */ 
/*     */   public int getQOP()
/*     */   {
/*  95 */     return this.qop;
/*     */   }
/*     */ 
/*     */   public boolean getPrivacy()
/*     */   {
/* 107 */     return this.privacyState;
/*     */   }
/*     */ 
/*     */   public void setQOP(int paramInt)
/*     */   {
/* 117 */     this.qop = paramInt;
/*     */   }
/*     */ 
/*     */   public void setPrivacy(boolean paramBoolean)
/*     */   {
/* 130 */     this.privacyState = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isDuplicateToken()
/*     */   {
/* 140 */     return this.dupToken;
/*     */   }
/*     */ 
/*     */   public boolean isOldToken()
/*     */   {
/* 151 */     return this.oldToken;
/*     */   }
/*     */ 
/*     */   public boolean isUnseqToken()
/*     */   {
/* 160 */     return this.unseqToken;
/*     */   }
/*     */ 
/*     */   public boolean isGapToken()
/*     */   {
/* 171 */     return this.gapToken;
/*     */   }
/*     */ 
/*     */   public int getMinorStatus()
/*     */   {
/* 181 */     return this.minorStatus;
/*     */   }
/*     */ 
/*     */   public String getMinorString()
/*     */   {
/* 192 */     return this.minorString;
/*     */   }
/*     */ 
/*     */   public void setSupplementaryStates(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt, String paramString)
/*     */   {
/* 216 */     this.dupToken = paramBoolean1;
/* 217 */     this.oldToken = paramBoolean2;
/* 218 */     this.unseqToken = paramBoolean3;
/* 219 */     this.gapToken = paramBoolean4;
/* 220 */     this.minorStatus = paramInt;
/* 221 */     this.minorString = paramString;
/*     */   }
/*     */ 
/*     */   private void resetStatusValues()
/*     */   {
/* 228 */     this.dupToken = false;
/* 229 */     this.oldToken = false;
/* 230 */     this.unseqToken = false;
/* 231 */     this.gapToken = false;
/* 232 */     this.minorStatus = 0;
/* 233 */     this.minorString = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.MessageProp
 * JD-Core Version:    0.6.2
 */