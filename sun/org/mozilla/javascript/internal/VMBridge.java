/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class VMBridge
/*     */ {
/*  49 */   static final VMBridge instance = makeInstance();
/*     */ 
/*     */   private static VMBridge makeInstance()
/*     */   {
/*  53 */     String[] arrayOfString = { "sun.org.mozilla.javascript.internal.jdk15.VMBridge_jdk15" };
/*     */ 
/*  56 */     for (int i = 0; i != arrayOfString.length; i++) {
/*  57 */       String str = arrayOfString[i];
/*  58 */       Class localClass = Kit.classOrNull(str);
/*  59 */       if (localClass != null) {
/*  60 */         VMBridge localVMBridge = (VMBridge)Kit.newInstanceOrNull(localClass);
/*  61 */         if (localVMBridge != null) {
/*  62 */           return localVMBridge;
/*     */         }
/*     */       }
/*     */     }
/*  66 */     throw new IllegalStateException("Failed to create VMBridge instance");
/*     */   }
/*     */ 
/*     */   protected abstract Object getThreadContextHelper();
/*     */ 
/*     */   protected abstract Context getContext(Object paramObject);
/*     */ 
/*     */   protected abstract void setContext(Object paramObject, Context paramContext);
/*     */ 
/*     */   protected abstract ClassLoader getCurrentThreadClassLoader();
/*     */ 
/*     */   protected abstract boolean tryToMakeAccessible(Object paramObject);
/*     */ 
/*     */   protected Object getInterfaceProxyHelper(ContextFactory paramContextFactory, Class<?>[] paramArrayOfClass)
/*     */   {
/* 130 */     throw Context.reportRuntimeError("VMBridge.getInterfaceProxyHelper is not supported");
/*     */   }
/*     */ 
/*     */   protected Object newInterfaceProxy(Object paramObject1, ContextFactory paramContextFactory, InterfaceAdapter paramInterfaceAdapter, Object paramObject2, Scriptable paramScriptable)
/*     */   {
/* 153 */     throw Context.reportRuntimeError("VMBridge.newInterfaceProxy is not supported");
/*     */   }
/*     */ 
/*     */   protected abstract boolean isVarArgs(Member paramMember);
/*     */ 
/*     */   public Iterator<?> getJavaIterator(Context paramContext, Scriptable paramScriptable, Object paramObject)
/*     */   {
/* 171 */     if ((paramObject instanceof Wrapper)) {
/* 172 */       Object localObject = ((Wrapper)paramObject).unwrap();
/* 173 */       Iterator localIterator = null;
/* 174 */       if ((localObject instanceof Iterator))
/* 175 */         localIterator = (Iterator)localObject;
/* 176 */       return localIterator;
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.VMBridge
 * JD-Core Version:    0.6.2
 */