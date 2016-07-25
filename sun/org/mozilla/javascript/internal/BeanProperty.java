/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ class BeanProperty
/*     */ {
/*     */   MemberBox getter;
/*     */   MemberBox setter;
/*     */   NativeJavaMethod setters;
/*     */ 
/*     */   BeanProperty(MemberBox paramMemberBox1, MemberBox paramMemberBox2, NativeJavaMethod paramNativeJavaMethod)
/*     */   {
/* 903 */     this.getter = paramMemberBox1;
/* 904 */     this.setter = paramMemberBox2;
/* 905 */     this.setters = paramNativeJavaMethod;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.BeanProperty
 * JD-Core Version:    0.6.2
 */