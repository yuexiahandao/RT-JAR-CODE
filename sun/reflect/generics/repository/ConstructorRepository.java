/*     */ package sun.reflect.generics.repository;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import sun.reflect.generics.factory.GenericsFactory;
/*     */ import sun.reflect.generics.parser.SignatureParser;
/*     */ import sun.reflect.generics.tree.FieldTypeSignature;
/*     */ import sun.reflect.generics.tree.MethodTypeSignature;
/*     */ import sun.reflect.generics.tree.TypeSignature;
/*     */ import sun.reflect.generics.visitor.Reifier;
/*     */ 
/*     */ public class ConstructorRepository extends GenericDeclRepository<MethodTypeSignature>
/*     */ {
/*     */   private Type[] paramTypes;
/*     */   private Type[] exceptionTypes;
/*     */ 
/*     */   protected ConstructorRepository(String paramString, GenericsFactory paramGenericsFactory)
/*     */   {
/*  51 */     super(paramString, paramGenericsFactory);
/*     */   }
/*     */ 
/*     */   protected MethodTypeSignature parse(String paramString) {
/*  55 */     return SignatureParser.make().parseMethodSig(paramString);
/*     */   }
/*     */ 
/*     */   public static ConstructorRepository make(String paramString, GenericsFactory paramGenericsFactory)
/*     */   {
/*  69 */     return new ConstructorRepository(paramString, paramGenericsFactory);
/*     */   }
/*     */ 
/*     */   public Type[] getParameterTypes()
/*     */   {
/*  86 */     if (this.paramTypes == null)
/*     */     {
/*  88 */       TypeSignature[] arrayOfTypeSignature = ((MethodTypeSignature)getTree()).getParameterTypes();
/*     */ 
/*  90 */       Type[] arrayOfType = new Type[arrayOfTypeSignature.length];
/*     */ 
/*  92 */       for (int i = 0; i < arrayOfTypeSignature.length; i++) {
/*  93 */         Reifier localReifier = getReifier();
/*  94 */         arrayOfTypeSignature[i].accept(localReifier);
/*     */ 
/*  96 */         arrayOfType[i] = localReifier.getResult();
/*     */       }
/*  98 */       this.paramTypes = arrayOfType;
/*     */     }
/* 100 */     return (Type[])this.paramTypes.clone();
/*     */   }
/*     */ 
/*     */   public Type[] getExceptionTypes() {
/* 104 */     if (this.exceptionTypes == null)
/*     */     {
/* 106 */       FieldTypeSignature[] arrayOfFieldTypeSignature = ((MethodTypeSignature)getTree()).getExceptionTypes();
/*     */ 
/* 108 */       Type[] arrayOfType = new Type[arrayOfFieldTypeSignature.length];
/*     */ 
/* 110 */       for (int i = 0; i < arrayOfFieldTypeSignature.length; i++) {
/* 111 */         Reifier localReifier = getReifier();
/* 112 */         arrayOfFieldTypeSignature[i].accept(localReifier);
/*     */ 
/* 114 */         arrayOfType[i] = localReifier.getResult();
/*     */       }
/* 116 */       this.exceptionTypes = arrayOfType;
/*     */     }
/* 118 */     return (Type[])this.exceptionTypes.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.repository.ConstructorRepository
 * JD-Core Version:    0.6.2
 */