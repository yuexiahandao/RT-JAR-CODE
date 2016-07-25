/*     */ package sun.reflect.generics.visitor;
/*     */ 
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.reflect.generics.factory.GenericsFactory;
/*     */ import sun.reflect.generics.tree.ArrayTypeSignature;
/*     */ import sun.reflect.generics.tree.BooleanSignature;
/*     */ import sun.reflect.generics.tree.BottomSignature;
/*     */ import sun.reflect.generics.tree.ByteSignature;
/*     */ import sun.reflect.generics.tree.CharSignature;
/*     */ import sun.reflect.generics.tree.ClassTypeSignature;
/*     */ import sun.reflect.generics.tree.DoubleSignature;
/*     */ import sun.reflect.generics.tree.FloatSignature;
/*     */ import sun.reflect.generics.tree.FormalTypeParameter;
/*     */ import sun.reflect.generics.tree.IntSignature;
/*     */ import sun.reflect.generics.tree.LongSignature;
/*     */ import sun.reflect.generics.tree.ShortSignature;
/*     */ import sun.reflect.generics.tree.SimpleClassTypeSignature;
/*     */ import sun.reflect.generics.tree.TypeArgument;
/*     */ import sun.reflect.generics.tree.TypeSignature;
/*     */ import sun.reflect.generics.tree.TypeVariableSignature;
/*     */ import sun.reflect.generics.tree.VoidDescriptor;
/*     */ import sun.reflect.generics.tree.Wildcard;
/*     */ 
/*     */ public class Reifier
/*     */   implements TypeTreeVisitor<Type>
/*     */ {
/*     */   private Type resultType;
/*     */   private GenericsFactory factory;
/*     */ 
/*     */   private Reifier(GenericsFactory paramGenericsFactory)
/*     */   {
/*  45 */     this.factory = paramGenericsFactory;
/*     */   }
/*     */   private GenericsFactory getFactory() {
/*  48 */     return this.factory;
/*     */   }
/*     */ 
/*     */   public static Reifier make(GenericsFactory paramGenericsFactory)
/*     */   {
/*  60 */     return new Reifier(paramGenericsFactory);
/*     */   }
/*     */ 
/*     */   private Type[] reifyTypeArguments(TypeArgument[] paramArrayOfTypeArgument)
/*     */   {
/*  66 */     Type[] arrayOfType = new Type[paramArrayOfTypeArgument.length];
/*  67 */     for (int i = 0; i < paramArrayOfTypeArgument.length; i++) {
/*  68 */       paramArrayOfTypeArgument[i].accept(this);
/*  69 */       arrayOfType[i] = this.resultType;
/*     */     }
/*  71 */     return arrayOfType;
/*     */   }
/*     */ 
/*     */   public Type getResult()
/*     */   {
/*  80 */     assert (this.resultType != null); return this.resultType;
/*     */   }
/*     */   public void visitFormalTypeParameter(FormalTypeParameter paramFormalTypeParameter) {
/*  83 */     this.resultType = getFactory().makeTypeVariable(paramFormalTypeParameter.getName(), paramFormalTypeParameter.getBounds());
/*     */   }
/*     */ 
/*     */   public void visitClassTypeSignature(ClassTypeSignature paramClassTypeSignature)
/*     */   {
/* 106 */     List localList = paramClassTypeSignature.getPath();
/* 107 */     assert (!localList.isEmpty());
/* 108 */     Iterator localIterator = localList.iterator();
/* 109 */     SimpleClassTypeSignature localSimpleClassTypeSignature = (SimpleClassTypeSignature)localIterator.next();
/* 110 */     StringBuilder localStringBuilder = new StringBuilder(localSimpleClassTypeSignature.getName());
/* 111 */     boolean bool = localSimpleClassTypeSignature.getDollar();
/*     */ 
/* 115 */     while ((localIterator.hasNext()) && (localSimpleClassTypeSignature.getTypeArguments().length == 0)) {
/* 116 */       localSimpleClassTypeSignature = (SimpleClassTypeSignature)localIterator.next();
/* 117 */       bool = localSimpleClassTypeSignature.getDollar();
/* 118 */       localStringBuilder.append(bool ? "$" : ".").append(localSimpleClassTypeSignature.getName());
/*     */     }
/*     */ 
/* 123 */     assert ((!localIterator.hasNext()) || (localSimpleClassTypeSignature.getTypeArguments().length > 0));
/*     */ 
/* 125 */     Type localType = getFactory().makeNamedType(localStringBuilder.toString());
/*     */ 
/* 127 */     if (localSimpleClassTypeSignature.getTypeArguments().length == 0)
/*     */     {
/* 129 */       assert (!localIterator.hasNext());
/* 130 */       this.resultType = localType;
/*     */     } else {
/* 132 */       assert (localSimpleClassTypeSignature.getTypeArguments().length > 0);
/*     */ 
/* 138 */       Type[] arrayOfType = reifyTypeArguments(localSimpleClassTypeSignature.getTypeArguments());
/*     */ 
/* 140 */       ParameterizedType localParameterizedType = getFactory().makeParameterizedType(localType, arrayOfType, null);
/*     */ 
/* 142 */       bool = false;
/* 143 */       while (localIterator.hasNext()) {
/* 144 */         localSimpleClassTypeSignature = (SimpleClassTypeSignature)localIterator.next();
/* 145 */         bool = localSimpleClassTypeSignature.getDollar();
/* 146 */         localStringBuilder.append(bool ? "$" : ".").append(localSimpleClassTypeSignature.getName());
/* 147 */         localType = getFactory().makeNamedType(localStringBuilder.toString());
/* 148 */         arrayOfType = reifyTypeArguments(localSimpleClassTypeSignature.getTypeArguments());
/*     */ 
/* 151 */         localParameterizedType = getFactory().makeParameterizedType(localType, arrayOfType, localParameterizedType);
/*     */       }
/* 153 */       this.resultType = localParameterizedType;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitArrayTypeSignature(ArrayTypeSignature paramArrayTypeSignature)
/*     */   {
/* 159 */     paramArrayTypeSignature.getComponentType().accept(this);
/* 160 */     Type localType = this.resultType;
/* 161 */     this.resultType = getFactory().makeArrayType(localType);
/*     */   }
/*     */ 
/*     */   public void visitTypeVariableSignature(TypeVariableSignature paramTypeVariableSignature) {
/* 165 */     this.resultType = getFactory().findTypeVariable(paramTypeVariableSignature.getIdentifier());
/*     */   }
/*     */ 
/*     */   public void visitWildcard(Wildcard paramWildcard) {
/* 169 */     this.resultType = getFactory().makeWildcard(paramWildcard.getUpperBounds(), paramWildcard.getLowerBounds());
/*     */   }
/*     */ 
/*     */   public void visitSimpleClassTypeSignature(SimpleClassTypeSignature paramSimpleClassTypeSignature)
/*     */   {
/* 174 */     this.resultType = getFactory().makeNamedType(paramSimpleClassTypeSignature.getName());
/*     */   }
/*     */ 
/*     */   public void visitBottomSignature(BottomSignature paramBottomSignature)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void visitByteSignature(ByteSignature paramByteSignature) {
/* 182 */     this.resultType = getFactory().makeByte();
/*     */   }
/*     */ 
/*     */   public void visitBooleanSignature(BooleanSignature paramBooleanSignature) {
/* 186 */     this.resultType = getFactory().makeBool();
/*     */   }
/*     */ 
/*     */   public void visitShortSignature(ShortSignature paramShortSignature) {
/* 190 */     this.resultType = getFactory().makeShort();
/*     */   }
/*     */ 
/*     */   public void visitCharSignature(CharSignature paramCharSignature) {
/* 194 */     this.resultType = getFactory().makeChar();
/*     */   }
/*     */ 
/*     */   public void visitIntSignature(IntSignature paramIntSignature) {
/* 198 */     this.resultType = getFactory().makeInt();
/*     */   }
/*     */ 
/*     */   public void visitLongSignature(LongSignature paramLongSignature) {
/* 202 */     this.resultType = getFactory().makeLong();
/*     */   }
/*     */ 
/*     */   public void visitFloatSignature(FloatSignature paramFloatSignature) {
/* 206 */     this.resultType = getFactory().makeFloat();
/*     */   }
/*     */ 
/*     */   public void visitDoubleSignature(DoubleSignature paramDoubleSignature) {
/* 210 */     this.resultType = getFactory().makeDouble();
/*     */   }
/*     */ 
/*     */   public void visitVoidDescriptor(VoidDescriptor paramVoidDescriptor) {
/* 214 */     this.resultType = getFactory().makeVoid();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.visitor.Reifier
 * JD-Core Version:    0.6.2
 */