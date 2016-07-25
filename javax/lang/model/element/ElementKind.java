/*     */ package javax.lang.model.element;
/*     */ 
/*     */ public enum ElementKind
/*     */ {
/*  44 */   PACKAGE, 
/*     */ 
/*  48 */   ENUM, 
/*     */ 
/*  50 */   CLASS, 
/*     */ 
/*  52 */   ANNOTATION_TYPE, 
/*     */ 
/*  57 */   INTERFACE, 
/*     */ 
/*  61 */   ENUM_CONSTANT, 
/*     */ 
/*  66 */   FIELD, 
/*     */ 
/*  68 */   PARAMETER, 
/*     */ 
/*  70 */   LOCAL_VARIABLE, 
/*     */ 
/*  72 */   EXCEPTION_PARAMETER, 
/*     */ 
/*  76 */   METHOD, 
/*     */ 
/*  78 */   CONSTRUCTOR, 
/*     */ 
/*  80 */   STATIC_INIT, 
/*     */ 
/*  82 */   INSTANCE_INIT, 
/*     */ 
/*  85 */   TYPE_PARAMETER, 
/*     */ 
/*  91 */   OTHER, 
/*     */ 
/*  97 */   RESOURCE_VARIABLE;
/*     */ 
/*     */   public boolean isClass()
/*     */   {
/* 107 */     return (this == CLASS) || (this == ENUM);
/*     */   }
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/* 117 */     return (this == INTERFACE) || (this == ANNOTATION_TYPE);
/*     */   }
/*     */ 
/*     */   public boolean isField()
/*     */   {
/* 127 */     return (this == FIELD) || (this == ENUM_CONSTANT);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.ElementKind
 * JD-Core Version:    0.6.2
 */