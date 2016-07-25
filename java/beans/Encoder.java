/*     */ package java.beans;
/*     */ 
/*     */ import com.sun.beans.finder.PersistenceDelegateFinder;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Encoder
/*     */ {
/*  49 */   private final PersistenceDelegateFinder finder = new PersistenceDelegateFinder();
/*  50 */   private Map bindings = new IdentityHashMap();
/*     */   private ExceptionListener exceptionListener;
/*  52 */   boolean executeStatements = true;
/*     */   private Map attributes;
/*     */ 
/*     */   protected void writeObject(Object paramObject)
/*     */   {
/*  70 */     if (paramObject == this) {
/*  71 */       return;
/*     */     }
/*  73 */     PersistenceDelegate localPersistenceDelegate = getPersistenceDelegate(paramObject == null ? null : paramObject.getClass());
/*  74 */     localPersistenceDelegate.writeObject(paramObject, this);
/*     */   }
/*     */ 
/*     */   public void setExceptionListener(ExceptionListener paramExceptionListener)
/*     */   {
/*  88 */     this.exceptionListener = paramExceptionListener;
/*     */   }
/*     */ 
/*     */   public ExceptionListener getExceptionListener()
/*     */   {
/* 100 */     return this.exceptionListener != null ? this.exceptionListener : Statement.defaultExceptionListener;
/*     */   }
/*     */ 
/*     */   Object getValue(Expression paramExpression) {
/*     */     try {
/* 105 */       return paramExpression == null ? null : paramExpression.getValue();
/*     */     }
/*     */     catch (Exception localException) {
/* 108 */       getExceptionListener().exceptionThrown(localException);
/* 109 */     }throw new RuntimeException("failed to evaluate: " + paramExpression.toString());
/*     */   }
/*     */ 
/*     */   public PersistenceDelegate getPersistenceDelegate(Class<?> paramClass)
/*     */   {
/* 197 */     PersistenceDelegate localPersistenceDelegate = this.finder.find(paramClass);
/* 198 */     if (localPersistenceDelegate == null) {
/* 199 */       localPersistenceDelegate = MetaData.getPersistenceDelegate(paramClass);
/* 200 */       if (localPersistenceDelegate != null) {
/* 201 */         this.finder.register(paramClass, localPersistenceDelegate);
/*     */       }
/*     */     }
/* 204 */     return localPersistenceDelegate;
/*     */   }
/*     */ 
/*     */   public void setPersistenceDelegate(Class<?> paramClass, PersistenceDelegate paramPersistenceDelegate)
/*     */   {
/* 218 */     this.finder.register(paramClass, paramPersistenceDelegate);
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 230 */     Expression localExpression = (Expression)this.bindings.remove(paramObject);
/* 231 */     return getValue(localExpression);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 247 */     if ((paramObject == null) || (paramObject == this) || (paramObject.getClass() == String.class))
/*     */     {
/* 249 */       return paramObject;
/*     */     }
/* 251 */     Expression localExpression = (Expression)this.bindings.get(paramObject);
/* 252 */     return getValue(localExpression);
/*     */   }
/*     */ 
/*     */   private Object writeObject1(Object paramObject) {
/* 256 */     Object localObject = get(paramObject);
/* 257 */     if (localObject == null) {
/* 258 */       writeObject(paramObject);
/* 259 */       localObject = get(paramObject);
/*     */     }
/* 261 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Statement cloneStatement(Statement paramStatement) {
/* 265 */     Object localObject1 = paramStatement.getTarget();
/* 266 */     Object localObject2 = writeObject1(localObject1);
/*     */ 
/* 268 */     Object[] arrayOfObject1 = paramStatement.getArguments();
/* 269 */     Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
/* 270 */     for (int i = 0; i < arrayOfObject1.length; i++) {
/* 271 */       arrayOfObject2[i] = writeObject1(arrayOfObject1[i]);
/*     */     }
/* 273 */     Expression localExpression = Statement.class.equals(paramStatement.getClass()) ? new Statement(localObject2, paramStatement.getMethodName(), arrayOfObject2) : new Expression(localObject2, paramStatement.getMethodName(), arrayOfObject2);
/*     */ 
/* 276 */     localExpression.loader = paramStatement.loader;
/* 277 */     return localExpression;
/*     */   }
/*     */ 
/*     */   public void writeStatement(Statement paramStatement)
/*     */   {
/* 301 */     Statement localStatement = cloneStatement(paramStatement);
/* 302 */     if ((paramStatement.getTarget() != this) && (this.executeStatements))
/*     */       try {
/* 304 */         localStatement.execute();
/*     */       } catch (Exception localException) {
/* 306 */         getExceptionListener().exceptionThrown(new Exception("Encoder: discarding statement " + localStatement, localException));
/*     */       }
/*     */   }
/*     */ 
/*     */   public void writeExpression(Expression paramExpression)
/*     */   {
/* 325 */     Object localObject = getValue(paramExpression);
/* 326 */     if (get(localObject) != null) {
/* 327 */       return;
/*     */     }
/* 329 */     this.bindings.put(localObject, (Expression)cloneStatement(paramExpression));
/* 330 */     writeObject(localObject);
/*     */   }
/*     */ 
/*     */   void clear() {
/* 334 */     this.bindings.clear();
/*     */   }
/*     */ 
/*     */   void setAttribute(Object paramObject1, Object paramObject2)
/*     */   {
/* 339 */     if (this.attributes == null) {
/* 340 */       this.attributes = new HashMap();
/*     */     }
/* 342 */     this.attributes.put(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   Object getAttribute(Object paramObject) {
/* 346 */     if (this.attributes == null) {
/* 347 */       return null;
/*     */     }
/* 349 */     return this.attributes.get(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.Encoder
 * JD-Core Version:    0.6.2
 */