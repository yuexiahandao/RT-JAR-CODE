/*     */ package com.sun.management;
/*     */ 
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import sun.management.VMOptionCompositeData;
/*     */ 
/*     */ public class VMOption
/*     */ {
/*     */   private String name;
/*     */   private String value;
/*     */   private boolean writeable;
/*     */   private Origin origin;
/*     */ 
/*     */   public VMOption(String paramString1, String paramString2, boolean paramBoolean, Origin paramOrigin)
/*     */   {
/* 115 */     this.name = paramString1;
/* 116 */     this.value = paramString2;
/* 117 */     this.writeable = paramBoolean;
/* 118 */     this.origin = paramOrigin;
/*     */   }
/*     */ 
/*     */   private VMOption(CompositeData paramCompositeData)
/*     */   {
/* 127 */     VMOptionCompositeData.validateCompositeData(paramCompositeData);
/*     */ 
/* 129 */     this.name = VMOptionCompositeData.getName(paramCompositeData);
/* 130 */     this.value = VMOptionCompositeData.getValue(paramCompositeData);
/* 131 */     this.writeable = VMOptionCompositeData.isWriteable(paramCompositeData);
/* 132 */     this.origin = VMOptionCompositeData.getOrigin(paramCompositeData);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 141 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 152 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Origin getOrigin()
/*     */   {
/* 162 */     return this.origin;
/*     */   }
/*     */ 
/*     */   public boolean isWriteable()
/*     */   {
/* 174 */     return this.writeable;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 178 */     return "VM option: " + getName() + " value: " + this.value + " " + " origin: " + this.origin + " " + (this.writeable ? "(read-only)" : "(read-write)");
/*     */   }
/*     */ 
/*     */   public static VMOption from(CompositeData paramCompositeData)
/*     */   {
/* 225 */     if (paramCompositeData == null) {
/* 226 */       return null;
/*     */     }
/*     */ 
/* 229 */     if ((paramCompositeData instanceof VMOptionCompositeData)) {
/* 230 */       return ((VMOptionCompositeData)paramCompositeData).getVMOption();
/*     */     }
/* 232 */     return new VMOption(paramCompositeData);
/*     */   }
/*     */ 
/*     */   public static enum Origin
/*     */   {
/*  72 */     DEFAULT, 
/*     */ 
/*  79 */     VM_CREATION, 
/*     */ 
/*  83 */     ENVIRON_VAR, 
/*     */ 
/*  87 */     CONFIG_FILE, 
/*     */ 
/*  92 */     MANAGEMENT, 
/*     */ 
/*  96 */     ERGONOMIC, 
/*     */ 
/* 100 */     OTHER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.management.VMOption
 * JD-Core Version:    0.6.2
 */