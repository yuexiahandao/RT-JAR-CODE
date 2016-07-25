/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class NativeJavaMethod extends BaseFunction
/*     */ {
/*     */   private static final int PREFERENCE_EQUAL = 0;
/*     */   private static final int PREFERENCE_FIRST_ARG = 1;
/*     */   private static final int PREFERENCE_SECOND_ARG = 2;
/*     */   private static final int PREFERENCE_AMBIGUOUS = 3;
/*     */   private static final boolean debug = false;
/*     */   MemberBox[] methods;
/*     */   private String functionName;
/*     */ 
/*     */   NativeJavaMethod(MemberBox[] paramArrayOfMemberBox)
/*     */   {
/*  60 */     this.functionName = paramArrayOfMemberBox[0].getName();
/*  61 */     this.methods = paramArrayOfMemberBox;
/*     */   }
/*     */ 
/*     */   NativeJavaMethod(MemberBox paramMemberBox, String paramString)
/*     */   {
/*  66 */     this.functionName = paramString;
/*  67 */     this.methods = new MemberBox[] { paramMemberBox };
/*     */   }
/*     */ 
/*     */   public NativeJavaMethod(Method paramMethod, String paramString)
/*     */   {
/*  72 */     this(new MemberBox(paramMethod), paramString);
/*     */   }
/*     */ 
/*     */   public String getFunctionName()
/*     */   {
/*  78 */     return this.functionName;
/*     */   }
/*     */ 
/*     */   static String scriptSignature(Object[] paramArrayOfObject)
/*     */   {
/*  83 */     StringBuffer localStringBuffer = new StringBuffer();
/*  84 */     for (int i = 0; i != paramArrayOfObject.length; i++) {
/*  85 */       Object localObject1 = paramArrayOfObject[i];
/*     */       String str;
/*  88 */       if (localObject1 == null)
/*  89 */         str = "null";
/*  90 */       else if ((localObject1 instanceof Boolean))
/*  91 */         str = "boolean";
/*  92 */       else if ((localObject1 instanceof String))
/*  93 */         str = "string";
/*  94 */       else if ((localObject1 instanceof Number))
/*  95 */         str = "number";
/*  96 */       else if ((localObject1 instanceof Scriptable)) {
/*  97 */         if ((localObject1 instanceof Undefined)) {
/*  98 */           str = "undefined";
/*  99 */         } else if ((localObject1 instanceof Wrapper)) {
/* 100 */           Object localObject2 = ((Wrapper)localObject1).unwrap();
/* 101 */           str = localObject2.getClass().getName();
/* 102 */         } else if ((localObject1 instanceof Function)) {
/* 103 */           str = "function";
/*     */         } else {
/* 105 */           str = "object";
/*     */         }
/*     */       }
/* 108 */       else str = JavaMembers.javaSignature(localObject1.getClass());
/*     */ 
/* 111 */       if (i != 0) {
/* 112 */         localStringBuffer.append(',');
/*     */       }
/* 114 */       localStringBuffer.append(str);
/*     */     }
/* 116 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   String decompile(int paramInt1, int paramInt2)
/*     */   {
/* 122 */     StringBuffer localStringBuffer = new StringBuffer();
/* 123 */     int i = 0 != (paramInt2 & 0x1) ? 1 : 0;
/* 124 */     if (i == 0) {
/* 125 */       localStringBuffer.append("function ");
/* 126 */       localStringBuffer.append(getFunctionName());
/* 127 */       localStringBuffer.append("() {");
/*     */     }
/* 129 */     localStringBuffer.append("/*\n");
/* 130 */     localStringBuffer.append(toString());
/* 131 */     localStringBuffer.append(i != 0 ? "*/\n" : "*/}\n");
/* 132 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 138 */     StringBuffer localStringBuffer = new StringBuffer();
/* 139 */     int i = 0; for (int j = this.methods.length; i != j; i++) {
/* 140 */       Method localMethod = this.methods[i].method();
/* 141 */       localStringBuffer.append(JavaMembers.javaSignature(localMethod.getReturnType()));
/* 142 */       localStringBuffer.append(' ');
/* 143 */       localStringBuffer.append(localMethod.getName());
/* 144 */       localStringBuffer.append(JavaMembers.liveConnectSignature(this.methods[i].argTypes));
/* 145 */       localStringBuffer.append('\n');
/*     */     }
/* 147 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 155 */     if (this.methods.length == 0) {
/* 156 */       throw new RuntimeException("No methods defined for call");
/*     */     }
/*     */ 
/* 159 */     int i = findFunction(paramContext, this.methods, paramArrayOfObject);
/* 160 */     if (i < 0) {
/* 161 */       localObject1 = this.methods[0].method().getDeclaringClass();
/* 162 */       localObject2 = ((Class)localObject1).getName() + '.' + getFunctionName() + '(' + scriptSignature(paramArrayOfObject) + ')';
/*     */ 
/* 164 */       throw Context.reportRuntimeError1("msg.java.no_such_method", localObject2);
/*     */     }
/*     */ 
/* 167 */     Object localObject1 = this.methods[i];
/* 168 */     Object localObject2 = ((MemberBox)localObject1).argTypes;
/*     */     Object localObject3;
/* 170 */     if (((MemberBox)localObject1).vararg)
/*     */     {
/* 172 */       localObject3 = new Object[localObject2.length];
/* 173 */       for (int j = 0; j < localObject2.length - 1; j++)
/* 174 */         localObject3[j] = Context.jsToJava(paramArrayOfObject[j], localObject2[j]);
/*     */       Object localObject4;
/* 181 */       if ((paramArrayOfObject.length == localObject2.length) && ((paramArrayOfObject[(paramArrayOfObject.length - 1)] == null) || ((paramArrayOfObject[(paramArrayOfObject.length - 1)] instanceof NativeArray)) || ((paramArrayOfObject[(paramArrayOfObject.length - 1)] instanceof NativeJavaArray))))
/*     */       {
/* 187 */         localObject4 = Context.jsToJava(paramArrayOfObject[(paramArrayOfObject.length - 1)], localObject2[(localObject2.length - 1)]);
/*     */       }
/*     */       else
/*     */       {
/* 191 */         localObject6 = localObject2[(localObject2.length - 1)].getComponentType();
/*     */ 
/* 193 */         localObject4 = Array.newInstance((Class)localObject6, paramArrayOfObject.length - localObject2.length + 1);
/*     */ 
/* 195 */         for (int m = 0; m < Array.getLength(localObject4); m++) {
/* 196 */           Object localObject8 = Context.jsToJava(paramArrayOfObject[(localObject2.length - 1 + m)], (Class)localObject6);
/*     */ 
/* 198 */           Array.set(localObject4, m, localObject8);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 203 */       localObject3[(localObject2.length - 1)] = localObject4;
/*     */ 
/* 205 */       paramArrayOfObject = (Object[])localObject3;
/*     */     }
/*     */     else {
/* 208 */       localObject3 = paramArrayOfObject;
/* 209 */       for (int k = 0; k < paramArrayOfObject.length; k++) {
/* 210 */         localObject6 = paramArrayOfObject[k];
/* 211 */         localObject7 = Context.jsToJava(localObject6, localObject2[k]);
/* 212 */         if (localObject7 != localObject6) {
/* 213 */           if (localObject3 == paramArrayOfObject) {
/* 214 */             paramArrayOfObject = (Object[])paramArrayOfObject.clone();
/*     */           }
/* 216 */           paramArrayOfObject[k] = localObject7;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 221 */     if (((MemberBox)localObject1).isStatic()) {
/* 222 */       localObject3 = null;
/*     */     } else {
/* 224 */       localObject5 = paramScriptable2;
/* 225 */       localObject6 = ((MemberBox)localObject1).getDeclaringClass();
/*     */       while (true) {
/* 227 */         if (localObject5 == null) {
/* 228 */           throw Context.reportRuntimeError3("msg.nonjava.method", getFunctionName(), ScriptRuntime.toString(paramScriptable2), ((Class)localObject6).getName());
/*     */         }
/*     */ 
/* 232 */         if ((localObject5 instanceof Wrapper)) {
/* 233 */           localObject3 = ((Wrapper)localObject5).unwrap();
/* 234 */           if (((Class)localObject6).isInstance(localObject3)) {
/*     */             break;
/*     */           }
/*     */         }
/* 238 */         localObject5 = ((Scriptable)localObject5).getPrototype();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 245 */     Object localObject5 = ((MemberBox)localObject1).invoke(localObject3, paramArrayOfObject);
/* 246 */     Object localObject6 = ((MemberBox)localObject1).method().getReturnType();
/*     */ 
/* 256 */     Object localObject7 = paramContext.getWrapFactory().wrap(paramContext, paramScriptable1, localObject5, (Class)localObject6);
/*     */ 
/* 265 */     if ((localObject7 == null) && (localObject6 == Void.TYPE)) {
/* 266 */       localObject7 = Undefined.instance;
/*     */     }
/* 268 */     return localObject7;
/*     */   }
/*     */ 
/*     */   static int findFunction(Context paramContext, MemberBox[] paramArrayOfMemberBox, Object[] paramArrayOfObject)
/*     */   {
/* 279 */     if (paramArrayOfMemberBox.length == 0)
/* 280 */       return -1;
/* 281 */     if (paramArrayOfMemberBox.length == 1) {
/* 282 */       MemberBox localMemberBox1 = paramArrayOfMemberBox[0];
/* 283 */       localObject = localMemberBox1.argTypes;
/* 284 */       i = localObject.length;
/*     */ 
/* 286 */       if (localMemberBox1.vararg) {
/* 287 */         i--;
/* 288 */         if (i > paramArrayOfObject.length) {
/* 289 */           return -1;
/*     */         }
/*     */       }
/* 292 */       else if (i != paramArrayOfObject.length) {
/* 293 */         return -1;
/*     */       }
/*     */ 
/* 296 */       for (arrayOfClass2 = 0; arrayOfClass2 != i; arrayOfClass2++) {
/* 297 */         if (!NativeJavaObject.canConvert(paramArrayOfObject[arrayOfClass2], localObject[arrayOfClass2]))
/*     */         {
/* 300 */           return -1;
/*     */         }
/*     */       }
/*     */ 
/* 304 */       return 0;
/*     */     }
/*     */ 
/* 307 */     Class[] arrayOfClass1 = -1;
/* 308 */     Object localObject = null;
/* 309 */     int i = 0;
/*     */     Class[] arrayOfClass3;
/* 312 */     label476: for (Class[] arrayOfClass2 = 0; arrayOfClass2 < paramArrayOfMemberBox.length; arrayOfClass2++) {
/* 313 */       MemberBox localMemberBox2 = paramArrayOfMemberBox[arrayOfClass2];
/* 314 */       arrayOfClass3 = localMemberBox2.argTypes;
/* 315 */       int m = arrayOfClass3.length;
/* 316 */       if (localMemberBox2.vararg) {
/* 317 */         m--;
/* 318 */         if (m > paramArrayOfObject.length)
/* 319 */           continue;
/*     */       }
/*     */       else {
/* 322 */         if (m != paramArrayOfObject.length) {
/*     */           continue;
/*     */         }
/*     */       }
/* 326 */       for (int n = 0; n < m; n++) {
/* 327 */         if (!NativeJavaObject.canConvert(paramArrayOfObject[n], arrayOfClass3[n]))
/*     */         {
/*     */           break label476;
/*     */         }
/*     */       }
/*     */ 
/* 333 */       if (arrayOfClass1 < 0)
/*     */       {
/* 335 */         arrayOfClass1 = arrayOfClass2;
/*     */       }
/*     */       else
/*     */       {
/* 341 */         n = 0;
/*     */ 
/* 343 */         int i1 = 0;
/*     */ 
/* 345 */         for (int i2 = -1; i2 != i; i2++)
/*     */         {
/*     */           int i3;
/* 347 */           if (i2 == -1)
/* 348 */             i3 = arrayOfClass1;
/*     */           else {
/* 350 */             i3 = localObject[i2];
/*     */           }
/* 352 */           MemberBox localMemberBox4 = paramArrayOfMemberBox[i3];
/* 353 */           if ((paramContext.hasFeature(13)) && ((localMemberBox4.member().getModifiers() & 0x1) != (localMemberBox2.member().getModifiers() & 0x1)))
/*     */           {
/* 360 */             if ((localMemberBox4.member().getModifiers() & 0x1) == 0)
/* 361 */               n++;
/*     */             else
/* 363 */               i1++;
/*     */           } else {
/* 365 */             int i4 = preferSignature(paramArrayOfObject, arrayOfClass3, localMemberBox2.vararg, localMemberBox4.argTypes, localMemberBox4.vararg);
/*     */ 
/* 369 */             if (i4 == 3)
/*     */               break;
/* 371 */             if (i4 == 1) {
/* 372 */               n++;
/* 373 */             } else if (i4 == 2) {
/* 374 */               i1++;
/*     */             } else {
/* 376 */               if (i4 != 0) Kit.codeBug();
/*     */ 
/* 382 */               if ((!localMemberBox4.isStatic()) || (!localMemberBox4.getDeclaringClass().isAssignableFrom(localMemberBox2.getDeclaringClass())))
/*     */               {
/*     */                 break label476;
/*     */               }
/*     */ 
/* 393 */               if (i2 == -1) {
/* 394 */                 arrayOfClass1 = arrayOfClass2; break label476;
/*     */               }
/* 396 */               localObject[i2] = arrayOfClass2; break label476;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 407 */         if (n == 1 + i)
/*     */         {
/* 411 */           arrayOfClass1 = arrayOfClass2;
/* 412 */           i = 0;
/* 413 */         } else if (i1 != 1 + i)
/*     */         {
/* 421 */           if (localObject == null)
/*     */           {
/* 423 */             localObject = new int[paramArrayOfMemberBox.length - 1];
/*     */           }
/* 425 */           localObject[i] = arrayOfClass2;
/* 426 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 431 */     if (arrayOfClass1 < 0)
/*     */     {
/* 433 */       return -1;
/* 434 */     }if (i == 0)
/*     */     {
/* 436 */       return arrayOfClass1;
/*     */     }
/*     */ 
/* 440 */     StringBuffer localStringBuffer = new StringBuffer();
/* 441 */     for (int j = -1; j != i; j++)
/*     */     {
/*     */       int k;
/* 443 */       if (j == -1)
/* 444 */         arrayOfClass3 = arrayOfClass1;
/*     */       else {
/* 446 */         k = localObject[j];
/*     */       }
/* 448 */       localStringBuffer.append("\n    ");
/* 449 */       localStringBuffer.append(paramArrayOfMemberBox[k].toJavaDeclaration());
/*     */     }
/*     */ 
/* 452 */     MemberBox localMemberBox3 = paramArrayOfMemberBox[arrayOfClass1];
/* 453 */     String str1 = localMemberBox3.getName();
/* 454 */     String str2 = localMemberBox3.getDeclaringClass().getName();
/*     */ 
/* 456 */     if (paramArrayOfMemberBox[0].isMethod()) {
/* 457 */       throw Context.reportRuntimeError3("msg.constructor.ambiguous", str1, scriptSignature(paramArrayOfObject), localStringBuffer.toString());
/*     */     }
/*     */ 
/* 461 */     throw Context.reportRuntimeError4("msg.method.ambiguous", str2, str1, scriptSignature(paramArrayOfObject), localStringBuffer.toString());
/*     */   }
/*     */ 
/*     */   private static int preferSignature(Object[] paramArrayOfObject, Class<?>[] paramArrayOfClass1, boolean paramBoolean1, Class<?>[] paramArrayOfClass2, boolean paramBoolean2)
/*     */   {
/* 485 */     int i = 0;
/* 486 */     for (int j = 0; j < paramArrayOfObject.length; j++) {
/* 487 */       Class<?> localClass1 = (paramBoolean1) && (j >= paramArrayOfClass1.length) ? paramArrayOfClass1[(paramArrayOfClass1.length - 1)] : paramArrayOfClass1[j];
/* 488 */       Class<?> localClass2 = (paramBoolean2) && (j >= paramArrayOfClass2.length) ? paramArrayOfClass2[(paramArrayOfClass2.length - 1)] : paramArrayOfClass2[j];
/* 489 */       if (localClass1 != localClass2)
/*     */       {
/* 492 */         Object localObject = paramArrayOfObject[j];
/*     */ 
/* 496 */         int k = NativeJavaObject.getConversionWeight(localObject, localClass1);
/* 497 */         int m = NativeJavaObject.getConversionWeight(localObject, localClass2);
/*     */         int n;
/* 500 */         if (k < m)
/* 501 */           n = 1;
/* 502 */         else if (k > m) {
/* 503 */           n = 2;
/*     */         }
/* 506 */         else if (k == 0) {
/* 507 */           if (localClass1.isAssignableFrom(localClass2))
/* 508 */             n = 2;
/* 509 */           else if (localClass2.isAssignableFrom(localClass1))
/* 510 */             n = 1;
/*     */           else
/* 512 */             n = 3;
/*     */         }
/*     */         else {
/* 515 */           n = 3;
/*     */         }
/*     */ 
/* 519 */         i |= n;
/*     */ 
/* 521 */         if (i == 3)
/*     */           break;
/*     */       }
/*     */     }
/* 525 */     return i;
/*     */   }
/*     */ 
/*     */   private static void printDebug(String paramString, MemberBox paramMemberBox, Object[] paramArrayOfObject)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NativeJavaMethod
 * JD-Core Version:    0.6.2
 */