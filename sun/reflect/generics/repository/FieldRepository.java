/*    */ package sun.reflect.generics.repository;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import sun.reflect.generics.factory.GenericsFactory;
/*    */ import sun.reflect.generics.parser.SignatureParser;
/*    */ import sun.reflect.generics.tree.TypeSignature;
/*    */ import sun.reflect.generics.visitor.Reifier;
/*    */ 
/*    */ public class FieldRepository extends AbstractRepository<TypeSignature>
/*    */ {
/*    */   private Type genericType;
/*    */ 
/*    */   protected FieldRepository(String paramString, GenericsFactory paramGenericsFactory)
/*    */   {
/* 48 */     super(paramString, paramGenericsFactory);
/*    */   }
/*    */ 
/*    */   protected TypeSignature parse(String paramString) {
/* 52 */     return SignatureParser.make().parseTypeSig(paramString);
/*    */   }
/*    */ 
/*    */   public static FieldRepository make(String paramString, GenericsFactory paramGenericsFactory)
/*    */   {
/* 66 */     return new FieldRepository(paramString, paramGenericsFactory);
/*    */   }
/*    */ 
/*    */   public Type getGenericType()
/*    */   {
/* 83 */     if (this.genericType == null) {
/* 84 */       Reifier localReifier = getReifier();
/* 85 */       ((TypeSignature)getTree()).accept(localReifier);
/*    */ 
/* 87 */       this.genericType = localReifier.getResult();
/*    */     }
/* 89 */     return this.genericType;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.repository.FieldRepository
 * JD-Core Version:    0.6.2
 */