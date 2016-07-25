/*     */ package sun.reflect.generics.repository;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import sun.reflect.generics.factory.GenericsFactory;
/*     */ import sun.reflect.generics.parser.SignatureParser;
/*     */ import sun.reflect.generics.tree.ClassSignature;
/*     */ import sun.reflect.generics.tree.ClassTypeSignature;
/*     */ import sun.reflect.generics.tree.TypeTree;
/*     */ import sun.reflect.generics.visitor.Reifier;
/*     */ 
/*     */ public class ClassRepository extends GenericDeclRepository<ClassSignature>
/*     */ {
/*     */   private Type superclass;
/*     */   private Type[] superInterfaces;
/*     */ 
/*     */   private ClassRepository(String paramString, GenericsFactory paramGenericsFactory)
/*     */   {
/*  48 */     super(paramString, paramGenericsFactory);
/*     */   }
/*     */ 
/*     */   protected ClassSignature parse(String paramString) {
/*  52 */     return SignatureParser.make().parseClassSig(paramString);
/*     */   }
/*     */ 
/*     */   public static ClassRepository make(String paramString, GenericsFactory paramGenericsFactory)
/*     */   {
/*  65 */     return new ClassRepository(paramString, paramGenericsFactory);
/*     */   }
/*     */ 
/*     */   public Type getSuperclass()
/*     */   {
/*  81 */     if (this.superclass == null) {
/*  82 */       Reifier localReifier = getReifier();
/*     */ 
/*  84 */       ((ClassSignature)getTree()).getSuperclass().accept(localReifier);
/*     */ 
/*  86 */       this.superclass = localReifier.getResult();
/*     */     }
/*  88 */     return this.superclass;
/*     */   }
/*     */ 
/*     */   public Type[] getSuperInterfaces() {
/*  92 */     if (this.superInterfaces == null)
/*     */     {
/*  94 */       ClassTypeSignature[] arrayOfClassTypeSignature = ((ClassSignature)getTree()).getSuperInterfaces();
/*     */ 
/*  96 */       Type[] arrayOfType = new Type[arrayOfClassTypeSignature.length];
/*     */ 
/*  98 */       for (int i = 0; i < arrayOfClassTypeSignature.length; i++) {
/*  99 */         Reifier localReifier = getReifier();
/* 100 */         arrayOfClassTypeSignature[i].accept(localReifier);
/*     */ 
/* 102 */         arrayOfType[i] = localReifier.getResult();
/*     */       }
/* 104 */       this.superInterfaces = arrayOfType;
/*     */     }
/* 106 */     return (Type[])this.superInterfaces.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.repository.ClassRepository
 * JD-Core Version:    0.6.2
 */