/*     */ package java.beans;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class MethodDescriptor extends FeatureDescriptor
/*     */ {
/*  41 */   private final MethodRef methodRef = new MethodRef();
/*     */   private String[] paramNames;
/*     */   private List params;
/*     */   private ParameterDescriptor[] parameterDescriptors;
/*     */ 
/*     */   public MethodDescriptor(Method paramMethod)
/*     */   {
/*  56 */     this(paramMethod, null);
/*     */   }
/*     */ 
/*     */   public MethodDescriptor(Method paramMethod, ParameterDescriptor[] paramArrayOfParameterDescriptor)
/*     */   {
/*  71 */     setName(paramMethod.getName());
/*  72 */     setMethod(paramMethod);
/*  73 */     this.parameterDescriptors = paramArrayOfParameterDescriptor;
/*     */   }
/*     */ 
/*     */   public synchronized Method getMethod()
/*     */   {
/*  82 */     Method localMethod = this.methodRef.get();
/*  83 */     if (localMethod == null) {
/*  84 */       Class localClass = getClass0();
/*  85 */       String str = getName();
/*  86 */       if ((localClass != null) && (str != null)) {
/*  87 */         Class[] arrayOfClass = getParams();
/*  88 */         if (arrayOfClass == null) {
/*  89 */           for (int i = 0; i < 3; i++)
/*     */           {
/*  93 */             localMethod = Introspector.findMethod(localClass, str, i, null);
/*  94 */             if (localMethod != null)
/*     */               break;
/*     */           }
/*     */         }
/*     */         else {
/*  99 */           localMethod = Introspector.findMethod(localClass, str, arrayOfClass.length, arrayOfClass);
/*     */         }
/* 101 */         setMethod(localMethod);
/*     */       }
/*     */     }
/* 104 */     return localMethod;
/*     */   }
/*     */ 
/*     */   private synchronized void setMethod(Method paramMethod) {
/* 108 */     if (paramMethod == null) {
/* 109 */       return;
/*     */     }
/* 111 */     if (getClass0() == null) {
/* 112 */       setClass0(paramMethod.getDeclaringClass());
/*     */     }
/* 114 */     setParams(getParameterTypes(getClass0(), paramMethod));
/* 115 */     this.methodRef.set(paramMethod);
/*     */   }
/*     */ 
/*     */   private synchronized void setParams(Class[] paramArrayOfClass) {
/* 119 */     if (paramArrayOfClass == null) {
/* 120 */       return;
/*     */     }
/* 122 */     this.paramNames = new String[paramArrayOfClass.length];
/* 123 */     this.params = new ArrayList(paramArrayOfClass.length);
/* 124 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 125 */       this.paramNames[i] = paramArrayOfClass[i].getName();
/* 126 */       this.params.add(new WeakReference(paramArrayOfClass[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */   String[] getParamNames()
/*     */   {
/* 132 */     return this.paramNames;
/*     */   }
/*     */ 
/*     */   private synchronized Class[] getParams() {
/* 136 */     Class[] arrayOfClass = new Class[this.params.size()];
/*     */ 
/* 138 */     for (int i = 0; i < this.params.size(); i++) {
/* 139 */       Reference localReference = (Reference)this.params.get(i);
/* 140 */       Class localClass = (Class)localReference.get();
/* 141 */       if (localClass == null) {
/* 142 */         return null;
/*     */       }
/* 144 */       arrayOfClass[i] = localClass;
/*     */     }
/*     */ 
/* 147 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   public ParameterDescriptor[] getParameterDescriptors()
/*     */   {
/* 158 */     return this.parameterDescriptors;
/*     */   }
/*     */ 
/*     */   MethodDescriptor(MethodDescriptor paramMethodDescriptor1, MethodDescriptor paramMethodDescriptor2)
/*     */   {
/* 170 */     super(paramMethodDescriptor1, paramMethodDescriptor2);
/*     */ 
/* 172 */     Method localMethod = paramMethodDescriptor2.methodRef.get();
/* 173 */     this.methodRef.set(null != localMethod ? localMethod : paramMethodDescriptor1.methodRef.get());
/* 174 */     this.params = paramMethodDescriptor1.params;
/* 175 */     if (paramMethodDescriptor2.params != null) {
/* 176 */       this.params = paramMethodDescriptor2.params;
/*     */     }
/* 178 */     this.paramNames = paramMethodDescriptor1.paramNames;
/* 179 */     if (paramMethodDescriptor2.paramNames != null) {
/* 180 */       this.paramNames = paramMethodDescriptor2.paramNames;
/*     */     }
/*     */ 
/* 183 */     this.parameterDescriptors = paramMethodDescriptor1.parameterDescriptors;
/* 184 */     if (paramMethodDescriptor2.parameterDescriptors != null)
/* 185 */       this.parameterDescriptors = paramMethodDescriptor2.parameterDescriptors;
/*     */   }
/*     */ 
/*     */   MethodDescriptor(MethodDescriptor paramMethodDescriptor)
/*     */   {
/* 194 */     super(paramMethodDescriptor);
/*     */ 
/* 196 */     this.methodRef.set(paramMethodDescriptor.getMethod());
/* 197 */     this.params = paramMethodDescriptor.params;
/* 198 */     this.paramNames = paramMethodDescriptor.paramNames;
/*     */ 
/* 200 */     if (paramMethodDescriptor.parameterDescriptors != null) {
/* 201 */       int i = paramMethodDescriptor.parameterDescriptors.length;
/* 202 */       this.parameterDescriptors = new ParameterDescriptor[i];
/* 203 */       for (int j = 0; j < i; j++)
/* 204 */         this.parameterDescriptors[j] = new ParameterDescriptor(paramMethodDescriptor.parameterDescriptors[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   void appendTo(StringBuilder paramStringBuilder)
/*     */   {
/* 210 */     appendTo(paramStringBuilder, "method", this.methodRef.get());
/* 211 */     if (this.parameterDescriptors != null) {
/* 212 */       paramStringBuilder.append("; parameterDescriptors={");
/* 213 */       for (ParameterDescriptor localParameterDescriptor : this.parameterDescriptors) {
/* 214 */         paramStringBuilder.append(localParameterDescriptor).append(", ");
/*     */       }
/* 216 */       paramStringBuilder.setLength(paramStringBuilder.length() - 2);
/* 217 */       paramStringBuilder.append("}");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.MethodDescriptor
 * JD-Core Version:    0.6.2
 */