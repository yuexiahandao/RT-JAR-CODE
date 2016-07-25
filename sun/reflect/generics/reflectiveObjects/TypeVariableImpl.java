/*     */ package sun.reflect.generics.reflectiveObjects;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import sun.reflect.generics.factory.GenericsFactory;
/*     */ import sun.reflect.generics.tree.FieldTypeSignature;
/*     */ import sun.reflect.generics.visitor.Reifier;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class TypeVariableImpl<D extends GenericDeclaration> extends LazyReflectiveObjectGenerator
/*     */   implements TypeVariable<D>
/*     */ {
/*     */   D genericDeclaration;
/*     */   private String name;
/*     */   private Type[] bounds;
/*     */   private FieldTypeSignature[] boundASTs;
/*     */ 
/*     */   private TypeVariableImpl(D paramD, String paramString, FieldTypeSignature[] paramArrayOfFieldTypeSignature, GenericsFactory paramGenericsFactory)
/*     */   {
/*  61 */     super(paramGenericsFactory);
/*  62 */     this.genericDeclaration = paramD;
/*  63 */     this.name = paramString;
/*  64 */     this.boundASTs = paramArrayOfFieldTypeSignature;
/*     */   }
/*     */ 
/*     */   private FieldTypeSignature[] getBoundASTs()
/*     */   {
/*  74 */     assert (this.bounds == null);
/*  75 */     return this.boundASTs;
/*     */   }
/*     */ 
/*     */   public static <T extends GenericDeclaration> TypeVariableImpl<T> make(T paramT, String paramString, FieldTypeSignature[] paramArrayOfFieldTypeSignature, GenericsFactory paramGenericsFactory)
/*     */   {
/*  95 */     if ((!(paramT instanceof Class)) && (!(paramT instanceof Method)) && (!(paramT instanceof Constructor)))
/*     */     {
/*  98 */       throw new AssertionError("Unexpected kind of GenericDeclaration" + paramT.getClass().toString());
/*     */     }
/*     */ 
/* 101 */     return new TypeVariableImpl(paramT, paramString, paramArrayOfFieldTypeSignature, paramGenericsFactory);
/*     */   }
/*     */ 
/*     */   public Type[] getBounds()
/*     */   {
/* 128 */     if (this.bounds == null) {
/* 129 */       FieldTypeSignature[] arrayOfFieldTypeSignature = getBoundASTs();
/*     */ 
/* 132 */       Type[] arrayOfType = new Type[arrayOfFieldTypeSignature.length];
/*     */ 
/* 134 */       for (int i = 0; i < arrayOfFieldTypeSignature.length; i++) {
/* 135 */         Reifier localReifier = getReifier();
/* 136 */         arrayOfFieldTypeSignature[i].accept(localReifier);
/* 137 */         arrayOfType[i] = localReifier.getResult();
/*     */       }
/*     */ 
/* 140 */       this.bounds = arrayOfType;
/*     */     }
/*     */ 
/* 143 */     return (Type[])this.bounds.clone();
/*     */   }
/*     */ 
/*     */   public D getGenericDeclaration()
/*     */   {
/* 155 */     if ((this.genericDeclaration instanceof Class))
/* 156 */       ReflectUtil.checkPackageAccess((Class)this.genericDeclaration);
/* 157 */     else if (((this.genericDeclaration instanceof Method)) || ((this.genericDeclaration instanceof Constructor)))
/*     */     {
/* 159 */       ReflectUtil.conservativeCheckMemberAccess((Member)this.genericDeclaration);
/*     */     }
/* 161 */     else throw new AssertionError("Unexpected kind of GenericDeclaration");
/* 162 */     return this.genericDeclaration;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 171 */     return this.name;
/*     */   }
/* 173 */   public String toString() { return getName(); }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 177 */     if (((paramObject instanceof TypeVariable)) && (paramObject.getClass() == TypeVariableImpl.class))
/*     */     {
/* 179 */       TypeVariable localTypeVariable = (TypeVariable)paramObject;
/*     */ 
/* 181 */       GenericDeclaration localGenericDeclaration = localTypeVariable.getGenericDeclaration();
/* 182 */       String str = localTypeVariable.getName();
/*     */ 
/* 184 */       return (this.genericDeclaration == null ? localGenericDeclaration == null : this.genericDeclaration.equals(localGenericDeclaration)) && (this.name == null ? str == null : this.name.equals(str));
/*     */     }
/*     */ 
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 198 */     return this.genericDeclaration.hashCode() ^ this.name.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.reflectiveObjects.TypeVariableImpl
 * JD-Core Version:    0.6.2
 */