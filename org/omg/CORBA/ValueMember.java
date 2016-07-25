/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.IDLEntity;
/*     */ 
/*     */ public final class ValueMember
/*     */   implements IDLEntity
/*     */ {
/*     */   public String name;
/*     */   public String id;
/*     */   public String defined_in;
/*     */   public String version;
/*     */   public TypeCode type;
/*     */   public IDLType type_def;
/*     */   public short access;
/*     */ 
/*     */   public ValueMember()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ValueMember(String paramString1, String paramString2, String paramString3, String paramString4, TypeCode paramTypeCode, IDLType paramIDLType, short paramShort)
/*     */   {
/* 113 */     this.name = paramString1;
/* 114 */     this.id = paramString2;
/* 115 */     this.defined_in = paramString3;
/* 116 */     this.version = paramString4;
/* 117 */     this.type = paramTypeCode;
/* 118 */     this.type_def = paramIDLType;
/* 119 */     this.access = paramShort;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ValueMember
 * JD-Core Version:    0.6.2
 */