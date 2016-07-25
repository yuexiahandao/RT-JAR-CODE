/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Repository;
/*     */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*     */ 
/*     */ public final class ObjectType extends ReferenceType
/*     */ {
/*     */   private String class_name;
/*     */ 
/*     */   public ObjectType(String class_name)
/*     */   {
/*  76 */     super((byte)14, "L" + class_name.replace('.', '/') + ";");
/*  77 */     this.class_name = class_name.replace('/', '.');
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/*  82 */     return this.class_name;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  86 */     return this.class_name.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object type)
/*     */   {
/*  91 */     return (type instanceof ObjectType) ? ((ObjectType)type).class_name.equals(this.class_name) : false;
/*     */   }
/*     */ 
/*     */   public boolean referencesClass()
/*     */   {
/* 100 */     JavaClass jc = Repository.lookupClass(this.class_name);
/* 101 */     if (jc == null) {
/* 102 */       return false;
/*     */     }
/* 104 */     return jc.isClass();
/*     */   }
/*     */ 
/*     */   public boolean referencesInterface()
/*     */   {
/* 112 */     JavaClass jc = Repository.lookupClass(this.class_name);
/* 113 */     if (jc == null) {
/* 114 */       return false;
/*     */     }
/* 116 */     return !jc.isClass();
/*     */   }
/*     */ 
/*     */   public boolean subclassOf(ObjectType superclass) {
/* 120 */     if ((referencesInterface()) || (superclass.referencesInterface())) {
/* 121 */       return false;
/*     */     }
/* 123 */     return Repository.instanceOf(this.class_name, superclass.class_name);
/*     */   }
/*     */ 
/*     */   public boolean accessibleTo(ObjectType accessor)
/*     */   {
/* 130 */     JavaClass jc = Repository.lookupClass(this.class_name);
/*     */ 
/* 132 */     if (jc.isPublic()) {
/* 133 */       return true;
/*     */     }
/* 135 */     JavaClass acc = Repository.lookupClass(accessor.class_name);
/* 136 */     return acc.getPackageName().equals(jc.getPackageName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ObjectType
 * JD-Core Version:    0.6.2
 */