/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import com.sun.org.apache.bcel.internal.Repository;
/*     */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*     */ 
/*     */ public abstract class ReferenceType extends Type
/*     */ {
/*     */   protected ReferenceType(byte t, String s)
/*     */   {
/*  72 */     super(t, s);
/*     */   }
/*     */ 
/*     */   ReferenceType()
/*     */   {
/*  78 */     super((byte)14, "<null object>");
/*     */   }
/*     */ 
/*     */   public boolean isCastableTo(Type t)
/*     */   {
/*  90 */     if (equals(Type.NULL)) {
/*  91 */       return true;
/*     */     }
/*  93 */     return isAssignmentCompatibleWith(t);
/*     */   }
/*     */ 
/*     */   public boolean isAssignmentCompatibleWith(Type t)
/*     */   {
/* 105 */     if (!(t instanceof ReferenceType)) {
/* 106 */       return false;
/*     */     }
/* 108 */     ReferenceType T = (ReferenceType)t;
/*     */ 
/* 110 */     if (equals(Type.NULL)) {
/* 111 */       return true;
/*     */     }
/*     */ 
/* 115 */     if (((this instanceof ObjectType)) && (((ObjectType)this).referencesClass()))
/*     */     {
/* 119 */       if (((T instanceof ObjectType)) && (((ObjectType)T).referencesClass())) {
/* 120 */         if (equals(T)) {
/* 121 */           return true;
/*     */         }
/* 123 */         if (Repository.instanceOf(((ObjectType)this).getClassName(), ((ObjectType)T).getClassName()))
/*     */         {
/* 125 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 130 */       if (((T instanceof ObjectType)) && (((ObjectType)T).referencesInterface()) && 
/* 131 */         (Repository.implementationOf(((ObjectType)this).getClassName(), ((ObjectType)T).getClassName())))
/*     */       {
/* 133 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 139 */     if (((this instanceof ObjectType)) && (((ObjectType)this).referencesInterface()))
/*     */     {
/* 142 */       if (((T instanceof ObjectType)) && (((ObjectType)T).referencesClass()) && 
/* 143 */         (T.equals(Type.OBJECT))) return true;
/*     */ 
/* 149 */       if (((T instanceof ObjectType)) && (((ObjectType)T).referencesInterface())) {
/* 150 */         if (equals(T)) return true;
/* 151 */         if (Repository.implementationOf(((ObjectType)this).getClassName(), ((ObjectType)T).getClassName()))
/*     */         {
/* 153 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 160 */     if ((this instanceof ArrayType))
/*     */     {
/* 163 */       if (((T instanceof ObjectType)) && (((ObjectType)T).referencesClass()) && 
/* 164 */         (T.equals(Type.OBJECT))) return true;
/*     */ 
/* 170 */       if ((T instanceof ArrayType))
/*     */       {
/* 173 */         Type sc = ((ArrayType)this).getElementType();
/* 174 */         Type tc = ((ArrayType)this).getElementType();
/*     */ 
/* 176 */         if (((sc instanceof BasicType)) && ((tc instanceof BasicType)) && (sc.equals(tc))) {
/* 177 */           return true;
/*     */         }
/*     */ 
/* 182 */         if (((tc instanceof ReferenceType)) && ((sc instanceof ReferenceType)) && (((ReferenceType)sc).isAssignmentCompatibleWith((ReferenceType)tc)))
/*     */         {
/* 184 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 193 */       if (((T instanceof ObjectType)) && (((ObjectType)T).referencesInterface())) {
/* 194 */         for (int ii = 0; ii < Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS.length; ii++) {
/* 195 */           if (T.equals(new ObjectType(Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS[ii]))) return true;
/*     */         }
/*     */       }
/*     */     }
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */   public ReferenceType getFirstCommonSuperclass(ReferenceType t)
/*     */   {
/* 218 */     if (equals(Type.NULL)) return t;
/* 219 */     if (t.equals(Type.NULL)) return this;
/* 220 */     if (equals(t)) return this;
/*     */ 
/* 231 */     if (((this instanceof ArrayType)) && ((t instanceof ArrayType))) {
/* 232 */       ArrayType arrType1 = (ArrayType)this;
/* 233 */       ArrayType arrType2 = (ArrayType)t;
/* 234 */       if ((arrType1.getDimensions() == arrType2.getDimensions()) && ((arrType1.getBasicType() instanceof ObjectType)) && ((arrType2.getBasicType() instanceof ObjectType)))
/*     */       {
/* 238 */         return new ArrayType(((ObjectType)arrType1.getBasicType()).getFirstCommonSuperclass((ObjectType)arrType2.getBasicType()), arrType1.getDimensions());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 246 */     if (((this instanceof ArrayType)) || ((t instanceof ArrayType))) {
/* 247 */       return Type.OBJECT;
/*     */     }
/*     */ 
/* 250 */     if ((((this instanceof ObjectType)) && (((ObjectType)this).referencesInterface())) || (((t instanceof ObjectType)) && (((ObjectType)t).referencesInterface())))
/*     */     {
/* 252 */       return Type.OBJECT;
/*     */     }
/*     */ 
/* 259 */     ObjectType thiz = (ObjectType)this;
/* 260 */     ObjectType other = (ObjectType)t;
/* 261 */     JavaClass[] thiz_sups = Repository.getSuperClasses(thiz.getClassName());
/* 262 */     JavaClass[] other_sups = Repository.getSuperClasses(other.getClassName());
/*     */ 
/* 264 */     if ((thiz_sups == null) || (other_sups == null)) {
/* 265 */       return null;
/*     */     }
/*     */ 
/* 269 */     JavaClass[] this_sups = new JavaClass[thiz_sups.length + 1];
/* 270 */     JavaClass[] t_sups = new JavaClass[other_sups.length + 1];
/* 271 */     System.arraycopy(thiz_sups, 0, this_sups, 1, thiz_sups.length);
/* 272 */     System.arraycopy(other_sups, 0, t_sups, 1, other_sups.length);
/* 273 */     this_sups[0] = Repository.lookupClass(thiz.getClassName());
/* 274 */     t_sups[0] = Repository.lookupClass(other.getClassName());
/*     */ 
/* 276 */     for (int i = 0; i < t_sups.length; i++) {
/* 277 */       for (int j = 0; j < this_sups.length; j++) {
/* 278 */         if (this_sups[j].equals(t_sups[i])) return new ObjectType(this_sups[j].getClassName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public ReferenceType firstCommonSuperclass(ReferenceType t)
/*     */   {
/* 302 */     if (equals(Type.NULL)) return t;
/* 303 */     if (t.equals(Type.NULL)) return this;
/* 304 */     if (equals(t)) return this;
/*     */ 
/* 313 */     if (((this instanceof ArrayType)) || ((t instanceof ArrayType))) {
/* 314 */       return Type.OBJECT;
/*     */     }
/*     */ 
/* 317 */     if ((((this instanceof ObjectType)) && (((ObjectType)this).referencesInterface())) || (((t instanceof ObjectType)) && (((ObjectType)t).referencesInterface())))
/*     */     {
/* 319 */       return Type.OBJECT;
/*     */     }
/*     */ 
/* 326 */     ObjectType thiz = (ObjectType)this;
/* 327 */     ObjectType other = (ObjectType)t;
/* 328 */     JavaClass[] thiz_sups = Repository.getSuperClasses(thiz.getClassName());
/* 329 */     JavaClass[] other_sups = Repository.getSuperClasses(other.getClassName());
/*     */ 
/* 331 */     if ((thiz_sups == null) || (other_sups == null)) {
/* 332 */       return null;
/*     */     }
/*     */ 
/* 336 */     JavaClass[] this_sups = new JavaClass[thiz_sups.length + 1];
/* 337 */     JavaClass[] t_sups = new JavaClass[other_sups.length + 1];
/* 338 */     System.arraycopy(thiz_sups, 0, this_sups, 1, thiz_sups.length);
/* 339 */     System.arraycopy(other_sups, 0, t_sups, 1, other_sups.length);
/* 340 */     this_sups[0] = Repository.lookupClass(thiz.getClassName());
/* 341 */     t_sups[0] = Repository.lookupClass(other.getClassName());
/*     */ 
/* 343 */     for (int i = 0; i < t_sups.length; i++) {
/* 344 */       for (int j = 0; j < this_sups.length; j++) {
/* 345 */         if (this_sups[j].equals(t_sups[i])) return new ObjectType(this_sups[j].getClassName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 350 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ReferenceType
 * JD-Core Version:    0.6.2
 */