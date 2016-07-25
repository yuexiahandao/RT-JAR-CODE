/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.portable.IDLEntity;
/*     */ import org.omg.IOP.TaggedProfile;
/*     */ 
/*     */ public final class TargetAddress
/*     */   implements IDLEntity
/*     */ {
/*     */   private byte[] ___object_key;
/*     */   private TaggedProfile ___profile;
/*     */   private IORAddressingInfo ___ior;
/*     */   private short __discriminator;
/*  42 */   private boolean __uninitialized = true;
/*     */ 
/*     */   public short discriminator()
/*     */   {
/*  50 */     if (this.__uninitialized)
/*  51 */       throw new BAD_OPERATION();
/*  52 */     return this.__discriminator;
/*     */   }
/*     */ 
/*     */   public byte[] object_key()
/*     */   {
/*  57 */     if (this.__uninitialized)
/*  58 */       throw new BAD_OPERATION();
/*  59 */     verifyobject_key(this.__discriminator);
/*  60 */     return this.___object_key;
/*     */   }
/*     */ 
/*     */   public void object_key(byte[] paramArrayOfByte)
/*     */   {
/*  65 */     this.__discriminator = 0;
/*  66 */     this.___object_key = paramArrayOfByte;
/*  67 */     this.__uninitialized = false;
/*     */   }
/*     */ 
/*     */   private void verifyobject_key(short paramShort)
/*     */   {
/*  72 */     if (paramShort != 0)
/*  73 */       throw new BAD_OPERATION();
/*     */   }
/*     */ 
/*     */   public TaggedProfile profile()
/*     */   {
/*  78 */     if (this.__uninitialized)
/*  79 */       throw new BAD_OPERATION();
/*  80 */     verifyprofile(this.__discriminator);
/*  81 */     return this.___profile;
/*     */   }
/*     */ 
/*     */   public void profile(TaggedProfile paramTaggedProfile)
/*     */   {
/*  86 */     this.__discriminator = 1;
/*  87 */     this.___profile = paramTaggedProfile;
/*  88 */     this.__uninitialized = false;
/*     */   }
/*     */ 
/*     */   private void verifyprofile(short paramShort)
/*     */   {
/*  93 */     if (paramShort != 1)
/*  94 */       throw new BAD_OPERATION();
/*     */   }
/*     */ 
/*     */   public IORAddressingInfo ior()
/*     */   {
/*  99 */     if (this.__uninitialized)
/* 100 */       throw new BAD_OPERATION();
/* 101 */     verifyior(this.__discriminator);
/* 102 */     return this.___ior;
/*     */   }
/*     */ 
/*     */   public void ior(IORAddressingInfo paramIORAddressingInfo)
/*     */   {
/* 107 */     this.__discriminator = 2;
/* 108 */     this.___ior = paramIORAddressingInfo;
/* 109 */     this.__uninitialized = false;
/*     */   }
/*     */ 
/*     */   private void verifyior(short paramShort)
/*     */   {
/* 114 */     if (paramShort != 2)
/* 115 */       throw new BAD_OPERATION();
/*     */   }
/*     */ 
/*     */   public void _default()
/*     */   {
/* 120 */     this.__discriminator = -32768;
/* 121 */     this.__uninitialized = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.TargetAddress
 * JD-Core Version:    0.6.2
 */