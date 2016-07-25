/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ 
/*     */ class LazyActionMap extends ActionMapUIResource
/*     */ {
/*     */   private transient Object _loader;
/*     */ 
/*     */   static void installLazyActionMap(JComponent paramJComponent, Class paramClass, String paramString)
/*     */   {
/*  60 */     Object localObject = (ActionMap)UIManager.get(paramString);
/*  61 */     if (localObject == null) {
/*  62 */       localObject = new LazyActionMap(paramClass);
/*  63 */       UIManager.getLookAndFeelDefaults().put(paramString, localObject);
/*     */     }
/*  65 */     SwingUtilities.replaceUIActionMap(paramJComponent, (ActionMap)localObject);
/*     */   }
/*     */ 
/*     */   static ActionMap getActionMap(Class paramClass, String paramString)
/*     */   {
/*  83 */     Object localObject = (ActionMap)UIManager.get(paramString);
/*  84 */     if (localObject == null) {
/*  85 */       localObject = new LazyActionMap(paramClass);
/*  86 */       UIManager.getLookAndFeelDefaults().put(paramString, localObject);
/*     */     }
/*  88 */     return localObject;
/*     */   }
/*     */ 
/*     */   private LazyActionMap(Class paramClass)
/*     */   {
/*  93 */     this._loader = paramClass;
/*     */   }
/*     */ 
/*     */   public void put(Action paramAction) {
/*  97 */     put(paramAction.getValue("Name"), paramAction);
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject, Action paramAction) {
/* 101 */     loadIfNecessary();
/* 102 */     super.put(paramObject, paramAction);
/*     */   }
/*     */ 
/*     */   public Action get(Object paramObject) {
/* 106 */     loadIfNecessary();
/* 107 */     return super.get(paramObject);
/*     */   }
/*     */ 
/*     */   public void remove(Object paramObject) {
/* 111 */     loadIfNecessary();
/* 112 */     super.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 116 */     loadIfNecessary();
/* 117 */     super.clear();
/*     */   }
/*     */ 
/*     */   public Object[] keys() {
/* 121 */     loadIfNecessary();
/* 122 */     return super.keys();
/*     */   }
/*     */ 
/*     */   public int size() {
/* 126 */     loadIfNecessary();
/* 127 */     return super.size();
/*     */   }
/*     */ 
/*     */   public Object[] allKeys() {
/* 131 */     loadIfNecessary();
/* 132 */     return super.allKeys();
/*     */   }
/*     */ 
/*     */   public void setParent(ActionMap paramActionMap) {
/* 136 */     loadIfNecessary();
/* 137 */     super.setParent(paramActionMap);
/*     */   }
/*     */ 
/*     */   private void loadIfNecessary() {
/* 141 */     if (this._loader != null) {
/* 142 */       Object localObject = this._loader;
/*     */ 
/* 144 */       this._loader = null;
/* 145 */       Class localClass = (Class)localObject;
/*     */       try {
/* 147 */         Method localMethod = localClass.getDeclaredMethod("loadActionMap", new Class[] { LazyActionMap.class });
/*     */ 
/* 149 */         localMethod.invoke(localClass, new Object[] { this });
/*     */       } catch (NoSuchMethodException localNoSuchMethodException) {
/* 151 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + localClass); 
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException)
/*     */       {
/* 154 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + localIllegalAccessException); 
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException)
/*     */       {
/* 157 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + localInvocationTargetException); 
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/* 160 */         if (!$assertionsDisabled) throw new AssertionError("LazyActionMap unable to load actions " + localIllegalArgumentException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.LazyActionMap
 * JD-Core Version:    0.6.2
 */