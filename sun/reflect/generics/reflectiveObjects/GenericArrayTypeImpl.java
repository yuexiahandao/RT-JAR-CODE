/*    */ package sun.reflect.generics.reflectiveObjects;
/*    */ 
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ public class GenericArrayTypeImpl
/*    */   implements GenericArrayType
/*    */ {
/*    */   private Type genericComponentType;
/*    */ 
/*    */   private GenericArrayTypeImpl(Type paramType)
/*    */   {
/* 41 */     this.genericComponentType = paramType;
/*    */   }
/*    */ 
/*    */   public static GenericArrayTypeImpl make(Type paramType)
/*    */   {
/* 51 */     return new GenericArrayTypeImpl(paramType);
/*    */   }
/*    */ 
/*    */   public Type getGenericComponentType()
/*    */   {
/* 64 */     return this.genericComponentType;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 68 */     Type localType = getGenericComponentType();
/* 69 */     StringBuilder localStringBuilder = new StringBuilder();
/*    */ 
/* 71 */     if ((localType instanceof Class))
/* 72 */       localStringBuilder.append(((Class)localType).getName());
/*    */     else
/* 74 */       localStringBuilder.append(localType.toString());
/* 75 */     localStringBuilder.append("[]");
/* 76 */     return localStringBuilder.toString();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 81 */     if ((paramObject instanceof GenericArrayType)) {
/* 82 */       GenericArrayType localGenericArrayType = (GenericArrayType)paramObject;
/*    */ 
/* 84 */       Type localType = localGenericArrayType.getGenericComponentType();
/* 85 */       return this.genericComponentType == null ? false : localType == null ? true : this.genericComponentType.equals(localType);
/*    */     }
/*    */ 
/* 89 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 94 */     return this.genericComponentType == null ? 0 : this.genericComponentType.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl
 * JD-Core Version:    0.6.2
 */