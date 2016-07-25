/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import javax.naming.ldap.Control;
/*     */ 
/*     */ public class BasicControl
/*     */   implements Control
/*     */ {
/*     */   protected String id;
/*  50 */   protected boolean criticality = false;
/*     */ 
/*  57 */   protected byte[] value = null;
/*     */   private static final long serialVersionUID = -5914033725246428413L;
/*     */ 
/*     */   public BasicControl(String paramString)
/*     */   {
/*  69 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public BasicControl(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */   {
/*  81 */     this.id = paramString;
/*  82 */     this.criticality = paramBoolean;
/*  83 */     if (paramArrayOfByte != null)
/*  84 */       this.value = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/*  94 */     return this.id;
/*     */   }
/*     */ 
/*     */   public boolean isCritical()
/*     */   {
/* 103 */     return this.criticality;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedValue()
/*     */   {
/* 116 */     return this.value == null ? null : (byte[])this.value.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.BasicControl
 * JD-Core Version:    0.6.2
 */