/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ class FieldAndMethods extends NativeJavaMethod
/*     */ {
/*     */   Field field;
/*     */   Object javaObject;
/*     */ 
/*     */   FieldAndMethods(Scriptable paramScriptable, MemberBox[] paramArrayOfMemberBox, Field paramField)
/*     */   {
/* 917 */     super(paramArrayOfMemberBox);
/* 918 */     this.field = paramField;
/* 919 */     setParentScope(paramScriptable);
/* 920 */     setPrototype(ScriptableObject.getFunctionPrototype(paramScriptable));
/*     */   }
/*     */ 
/*     */   public Object getDefaultValue(Class<?> paramClass)
/*     */   {
/* 926 */     if (paramClass == ScriptRuntime.FunctionClass)
/* 927 */       return this;
/*     */     Class localClass;
/*     */     try
/*     */     {
/* 931 */       localObject = this.field.get(this.javaObject);
/* 932 */       localClass = this.field.getType();
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 934 */       throw Context.reportRuntimeError1("msg.java.internal.private", this.field.getName());
/*     */     }
/*     */ 
/* 937 */     Context localContext = Context.getContext();
/* 938 */     Object localObject = localContext.getWrapFactory().wrap(localContext, this, localObject, localClass);
/* 939 */     if ((localObject instanceof Scriptable)) {
/* 940 */       localObject = ((Scriptable)localObject).getDefaultValue(paramClass);
/*     */     }
/* 942 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.FieldAndMethods
 * JD-Core Version:    0.6.2
 */