/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ 
/*     */ final class MemberBox
/*     */ {
/*     */   private transient Member memberObject;
/*     */   transient Class<?>[] argTypes;
/*     */   transient Object delegateTo;
/*     */   transient boolean vararg;
/* 257 */   private static final Class<?>[] primitives = { Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE };
/*     */ 
/*     */   MemberBox(Method paramMethod)
/*     */   {
/*  68 */     init(paramMethod);
/*     */   }
/*     */ 
/*     */   MemberBox(Constructor<?> paramConstructor)
/*     */   {
/*  73 */     init(paramConstructor);
/*     */   }
/*     */ 
/*     */   private void init(Method paramMethod)
/*     */   {
/*  78 */     this.memberObject = paramMethod;
/*  79 */     this.argTypes = paramMethod.getParameterTypes();
/*  80 */     this.vararg = VMBridge.instance.isVarArgs(paramMethod);
/*     */   }
/*     */ 
/*     */   private void init(Constructor<?> paramConstructor)
/*     */   {
/*  85 */     this.memberObject = paramConstructor;
/*  86 */     this.argTypes = paramConstructor.getParameterTypes();
/*  87 */     this.vararg = VMBridge.instance.isVarArgs(paramConstructor);
/*     */   }
/*     */ 
/*     */   Method method()
/*     */   {
/*  92 */     return (Method)this.memberObject;
/*     */   }
/*     */ 
/*     */   Constructor<?> ctor()
/*     */   {
/*  97 */     return (Constructor)this.memberObject;
/*     */   }
/*     */ 
/*     */   Member member()
/*     */   {
/* 102 */     return this.memberObject;
/*     */   }
/*     */ 
/*     */   boolean isMethod()
/*     */   {
/* 107 */     return this.memberObject instanceof Method;
/*     */   }
/*     */ 
/*     */   boolean isCtor()
/*     */   {
/* 112 */     return this.memberObject instanceof Constructor;
/*     */   }
/*     */ 
/*     */   boolean isStatic()
/*     */   {
/* 117 */     return Modifier.isStatic(this.memberObject.getModifiers());
/*     */   }
/*     */ 
/*     */   String getName()
/*     */   {
/* 122 */     return this.memberObject.getName();
/*     */   }
/*     */ 
/*     */   Class<?> getDeclaringClass()
/*     */   {
/* 127 */     return this.memberObject.getDeclaringClass();
/*     */   }
/*     */ 
/*     */   String toJavaDeclaration()
/*     */   {
/* 132 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */     Object localObject;
/* 133 */     if (isMethod()) {
/* 134 */       localObject = method();
/* 135 */       localStringBuffer.append(((Method)localObject).getReturnType());
/* 136 */       localStringBuffer.append(' ');
/* 137 */       localStringBuffer.append(((Method)localObject).getName());
/*     */     } else {
/* 139 */       localObject = ctor();
/* 140 */       String str = ((Constructor)localObject).getDeclaringClass().getName();
/* 141 */       int i = str.lastIndexOf('.');
/* 142 */       if (i >= 0) {
/* 143 */         str = str.substring(i + 1);
/*     */       }
/* 145 */       localStringBuffer.append(str);
/*     */     }
/* 147 */     localStringBuffer.append(JavaMembers.liveConnectSignature(this.argTypes));
/* 148 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     return this.memberObject.toString();
/*     */   }
/*     */ 
/*     */   Object invoke(Object paramObject, Object[] paramArrayOfObject)
/*     */   {
/* 159 */     Object localObject1 = method();
/*     */     try
/*     */     {
/* 164 */       if (System.getSecurityManager() != null) {
/* 165 */         return MethodUtil.invoke((Method)localObject1, paramObject, paramArrayOfObject);
/*     */       }
/* 167 */       return ((Method)localObject1).invoke(paramObject, paramArrayOfObject);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 170 */       localObject2 = searchAccessibleMethod((Method)localObject1, this.argTypes);
/* 171 */       if (localObject2 != null) {
/* 172 */         this.memberObject = ((Member)localObject2);
/* 173 */         localObject1 = localObject2;
/*     */       }
/* 175 */       else if (!VMBridge.instance.tryToMakeAccessible(localObject1)) {
/* 176 */         throw Context.throwAsScriptRuntimeEx(localIllegalAccessException);
/*     */       }
/*     */ 
/* 182 */       if (System.getSecurityManager() != null) {
/* 183 */         return MethodUtil.invoke((Method)localObject1, paramObject, paramArrayOfObject);
/*     */       }
/* 185 */       return ((Method)localObject1).invoke(paramObject, paramArrayOfObject);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/* 190 */       Object localObject2 = localInvocationTargetException;
/*     */       do
/* 192 */         localObject2 = ((InvocationTargetException)localObject2).getTargetException();
/* 193 */       while ((localObject2 instanceof InvocationTargetException));
/* 194 */       if ((localObject2 instanceof ContinuationPending))
/* 195 */         throw ((ContinuationPending)localObject2);
/* 196 */       throw Context.throwAsScriptRuntimeEx((Throwable)localObject2);
/*     */     } catch (Exception localException) {
/* 198 */       throw Context.throwAsScriptRuntimeEx(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   Object newInstance(Object[] paramArrayOfObject)
/*     */   {
/* 204 */     Constructor localConstructor = ctor();
/*     */     try
/*     */     {
/* 207 */       return localConstructor.newInstance(paramArrayOfObject);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 209 */       if (!VMBridge.instance.tryToMakeAccessible(localConstructor)) {
/* 210 */         throw Context.throwAsScriptRuntimeEx(localIllegalAccessException);
/*     */       }
/*     */ 
/* 213 */       return localConstructor.newInstance(paramArrayOfObject);
/*     */     } catch (Exception localException) {
/* 215 */       throw Context.throwAsScriptRuntimeEx(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Method searchAccessibleMethod(Method paramMethod, Class<?>[] paramArrayOfClass)
/*     */   {
/* 221 */     int i = paramMethod.getModifiers();
/* 222 */     if ((Modifier.isPublic(i)) && (!Modifier.isStatic(i))) {
/* 223 */       Class localClass1 = paramMethod.getDeclaringClass();
/* 224 */       if (!Modifier.isPublic(localClass1.getModifiers())) {
/* 225 */         String str = paramMethod.getName();
/* 226 */         Class[] arrayOfClass = localClass1.getInterfaces();
/* 227 */         int j = 0; for (int k = arrayOfClass.length; j != k; j++) {
/* 228 */           Class localClass2 = arrayOfClass[j];
/* 229 */           if (Modifier.isPublic(localClass2.getModifiers()))
/*     */             try {
/* 231 */               return localClass2.getMethod(str, paramArrayOfClass);
/*     */             } catch (NoSuchMethodException localNoSuchMethodException2) {
/*     */             } catch (SecurityException localSecurityException2) {
/*     */             }
/*     */         }
/*     */         while (true) {
/* 237 */           localClass1 = localClass1.getSuperclass();
/* 238 */           if (localClass1 == null) break;
/* 239 */           if (Modifier.isPublic(localClass1.getModifiers()))
/*     */             try {
/* 241 */               Method localMethod = localClass1.getMethod(str, paramArrayOfClass);
/* 242 */               k = localMethod.getModifiers();
/* 243 */               if ((Modifier.isPublic(k)) && (!Modifier.isStatic(k)))
/*     */               {
/* 246 */                 return localMethod;
/*     */               }
/*     */             } catch (NoSuchMethodException localNoSuchMethodException1) {
/*     */             } catch (SecurityException localSecurityException1) {
/*     */             }
/*     */         }
/*     */       }
/*     */     }
/* 254 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.MemberBox
 * JD-Core Version:    0.6.2
 */