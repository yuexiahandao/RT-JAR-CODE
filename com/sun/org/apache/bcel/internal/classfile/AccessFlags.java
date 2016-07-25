/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class AccessFlags
/*     */   implements Serializable
/*     */ {
/*     */   protected int access_flags;
/*     */ 
/*     */   public AccessFlags()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AccessFlags(int a)
/*     */   {
/*  78 */     this.access_flags = a;
/*     */   }
/*     */ 
/*     */   public final int getAccessFlags()
/*     */   {
/*  84 */     return this.access_flags;
/*     */   }
/*     */ 
/*     */   public final int getModifiers()
/*     */   {
/*  89 */     return this.access_flags;
/*     */   }
/*     */ 
/*     */   public final void setAccessFlags(int access_flags)
/*     */   {
/*  95 */     this.access_flags = access_flags;
/*     */   }
/*     */ 
/*     */   public final void setModifiers(int access_flags)
/*     */   {
/* 102 */     setAccessFlags(access_flags);
/*     */   }
/*     */ 
/*     */   private final void setFlag(int flag, boolean set) {
/* 106 */     if ((this.access_flags & flag) != 0) {
/* 107 */       if (!set)
/* 108 */         this.access_flags ^= flag;
/*     */     }
/* 110 */     else if (set)
/* 111 */       this.access_flags |= flag;
/*     */   }
/*     */ 
/*     */   public final void isPublic(boolean flag) {
/* 115 */     setFlag(1, flag);
/*     */   }
/* 117 */   public final boolean isPublic() { return (this.access_flags & 0x1) != 0; }
/*     */ 
/*     */   public final void isPrivate(boolean flag) {
/* 120 */     setFlag(2, flag);
/*     */   }
/* 122 */   public final boolean isPrivate() { return (this.access_flags & 0x2) != 0; }
/*     */ 
/*     */   public final void isProtected(boolean flag) {
/* 125 */     setFlag(4, flag);
/*     */   }
/* 127 */   public final boolean isProtected() { return (this.access_flags & 0x4) != 0; }
/*     */ 
/*     */   public final void isStatic(boolean flag) {
/* 130 */     setFlag(8, flag);
/*     */   }
/* 132 */   public final boolean isStatic() { return (this.access_flags & 0x8) != 0; }
/*     */ 
/*     */   public final void isFinal(boolean flag) {
/* 135 */     setFlag(16, flag);
/*     */   }
/* 137 */   public final boolean isFinal() { return (this.access_flags & 0x10) != 0; }
/*     */ 
/*     */   public final void isSynchronized(boolean flag) {
/* 140 */     setFlag(32, flag);
/*     */   }
/* 142 */   public final boolean isSynchronized() { return (this.access_flags & 0x20) != 0; }
/*     */ 
/*     */   public final void isVolatile(boolean flag) {
/* 145 */     setFlag(64, flag);
/*     */   }
/* 147 */   public final boolean isVolatile() { return (this.access_flags & 0x40) != 0; }
/*     */ 
/*     */   public final void isTransient(boolean flag) {
/* 150 */     setFlag(128, flag);
/*     */   }
/* 152 */   public final boolean isTransient() { return (this.access_flags & 0x80) != 0; }
/*     */ 
/*     */   public final void isNative(boolean flag) {
/* 155 */     setFlag(256, flag);
/*     */   }
/* 157 */   public final boolean isNative() { return (this.access_flags & 0x100) != 0; }
/*     */ 
/*     */   public final void isInterface(boolean flag) {
/* 160 */     setFlag(512, flag);
/*     */   }
/* 162 */   public final boolean isInterface() { return (this.access_flags & 0x200) != 0; }
/*     */ 
/*     */   public final void isAbstract(boolean flag) {
/* 165 */     setFlag(1024, flag);
/*     */   }
/* 167 */   public final boolean isAbstract() { return (this.access_flags & 0x400) != 0; }
/*     */ 
/*     */   public final void isStrictfp(boolean flag) {
/* 170 */     setFlag(2048, flag);
/*     */   }
/* 172 */   public final boolean isStrictfp() { return (this.access_flags & 0x800) != 0; }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.AccessFlags
 * JD-Core Version:    0.6.2
 */