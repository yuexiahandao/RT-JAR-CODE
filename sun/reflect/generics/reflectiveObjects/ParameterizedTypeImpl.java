/*     */ package sun.reflect.generics.reflectiveObjects;
/*     */ 
/*     */ import java.lang.reflect.MalformedParameterizedTypeException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ParameterizedTypeImpl
/*     */   implements ParameterizedType
/*     */ {
/*     */   private Type[] actualTypeArguments;
/*     */   private Class<?> rawType;
/*     */   private Type ownerType;
/*     */ 
/*     */   private ParameterizedTypeImpl(Class<?> paramClass, Type[] paramArrayOfType, Type paramType)
/*     */   {
/*  48 */     this.actualTypeArguments = paramArrayOfType;
/*  49 */     this.rawType = paramClass;
/*  50 */     if (paramType != null)
/*  51 */       this.ownerType = paramType;
/*  52 */     else this.ownerType = paramClass.getDeclaringClass();
/*  53 */     validateConstructorArguments();
/*     */   }
/*     */ 
/*     */   private void validateConstructorArguments() {
/*  57 */     TypeVariable[] arrayOfTypeVariable = this.rawType.getTypeParameters();
/*     */ 
/*  59 */     if (arrayOfTypeVariable.length != this.actualTypeArguments.length) {
/*  60 */       throw new MalformedParameterizedTypeException();
/*     */     }
/*  62 */     for (int i = 0; i < this.actualTypeArguments.length; i++);
/*     */   }
/*     */ 
/*     */   public static ParameterizedTypeImpl make(Class<?> paramClass, Type[] paramArrayOfType, Type paramType)
/*     */   {
/*  95 */     return new ParameterizedTypeImpl(paramClass, paramArrayOfType, paramType);
/*     */   }
/*     */ 
/*     */   public Type[] getActualTypeArguments()
/*     */   {
/* 118 */     return (Type[])this.actualTypeArguments.clone();
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/* 129 */     return this.rawType;
/*     */   }
/*     */ 
/*     */   public Type getOwnerType()
/*     */   {
/* 151 */     return this.ownerType;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 163 */     if ((paramObject instanceof ParameterizedType))
/*     */     {
/* 165 */       ParameterizedType localParameterizedType = (ParameterizedType)paramObject;
/*     */ 
/* 167 */       if (this == localParameterizedType) {
/* 168 */         return true;
/*     */       }
/* 170 */       Type localType1 = localParameterizedType.getOwnerType();
/* 171 */       Type localType2 = localParameterizedType.getRawType();
/*     */ 
/* 193 */       return (this.ownerType == null ? localType1 == null : this.ownerType.equals(localType1)) && (this.rawType == null ? localType2 == null : this.rawType.equals(localType2)) && (Arrays.equals(this.actualTypeArguments, localParameterizedType.getActualTypeArguments()));
/*     */     }
/*     */ 
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 208 */     return Arrays.hashCode(this.actualTypeArguments) ^ (this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ (this.rawType == null ? 0 : this.rawType.hashCode());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 215 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 217 */     if (this.ownerType != null) {
/* 218 */       if ((this.ownerType instanceof Class))
/* 219 */         localStringBuilder.append(((Class)this.ownerType).getName());
/*     */       else {
/* 221 */         localStringBuilder.append(this.ownerType.toString());
/*     */       }
/* 223 */       localStringBuilder.append(".");
/*     */ 
/* 225 */       if ((this.ownerType instanceof ParameterizedTypeImpl))
/*     */       {
/* 228 */         localStringBuilder.append(this.rawType.getName().replace(((ParameterizedTypeImpl)this.ownerType).rawType.getName() + "$", ""));
/*     */       }
/*     */       else
/* 231 */         localStringBuilder.append(this.rawType.getName());
/*     */     } else {
/* 233 */       localStringBuilder.append(this.rawType.getName());
/*     */     }
/* 235 */     if ((this.actualTypeArguments != null) && (this.actualTypeArguments.length > 0))
/*     */     {
/* 237 */       localStringBuilder.append("<");
/* 238 */       int i = 1;
/* 239 */       for (Type localType : this.actualTypeArguments) {
/* 240 */         if (i == 0)
/* 241 */           localStringBuilder.append(", ");
/* 242 */         if ((localType instanceof Class))
/* 243 */           localStringBuilder.append(((Class)localType).getName());
/*     */         else
/* 245 */           localStringBuilder.append(localType.toString());
/* 246 */         i = 0;
/*     */       }
/* 248 */       localStringBuilder.append(">");
/*     */     }
/*     */ 
/* 251 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
 * JD-Core Version:    0.6.2
 */