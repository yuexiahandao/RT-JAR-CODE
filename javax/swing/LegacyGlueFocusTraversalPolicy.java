/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import java.awt.Window;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ final class LegacyGlueFocusTraversalPolicy extends FocusTraversalPolicy
/*     */   implements Serializable
/*     */ {
/*     */   private transient FocusTraversalPolicy delegatePolicy;
/*     */   private transient DefaultFocusManager delegateManager;
/*  51 */   private HashMap<Component, Component> forwardMap = new HashMap(); private HashMap<Component, Component> backwardMap = new HashMap();
/*     */ 
/*     */   LegacyGlueFocusTraversalPolicy(FocusTraversalPolicy paramFocusTraversalPolicy)
/*     */   {
/*  55 */     this.delegatePolicy = paramFocusTraversalPolicy;
/*     */   }
/*     */   LegacyGlueFocusTraversalPolicy(DefaultFocusManager paramDefaultFocusManager) {
/*  58 */     this.delegateManager = paramDefaultFocusManager;
/*     */   }
/*     */ 
/*     */   void setNextFocusableComponent(Component paramComponent1, Component paramComponent2) {
/*  62 */     this.forwardMap.put(paramComponent1, paramComponent2);
/*  63 */     this.backwardMap.put(paramComponent2, paramComponent1);
/*     */   }
/*     */   void unsetNextFocusableComponent(Component paramComponent1, Component paramComponent2) {
/*  66 */     this.forwardMap.remove(paramComponent1);
/*  67 */     this.backwardMap.remove(paramComponent2);
/*     */   }
/*     */ 
/*     */   public Component getComponentAfter(Container paramContainer, Component paramComponent)
/*     */   {
/*  72 */     Component localComponent1 = paramComponent;
/*  73 */     HashSet localHashSet = new HashSet();
/*     */     do
/*     */     {
/*  76 */       Component localComponent2 = localComponent1;
/*  77 */       localComponent1 = (Component)this.forwardMap.get(localComponent1);
/*  78 */       if (localComponent1 == null) {
/*  79 */         if ((this.delegatePolicy != null) && (localComponent2.isFocusCycleRoot(paramContainer)))
/*     */         {
/*  81 */           return this.delegatePolicy.getComponentAfter(paramContainer, localComponent2);
/*     */         }
/*  83 */         if (this.delegateManager != null) {
/*  84 */           return this.delegateManager.getComponentAfter(paramContainer, paramComponent);
/*     */         }
/*     */ 
/*  87 */         return null;
/*     */       }
/*     */ 
/*  90 */       if (localHashSet.contains(localComponent1))
/*     */       {
/*  92 */         return null;
/*     */       }
/*  94 */       localHashSet.add(localComponent1);
/*  95 */     }while (!accept(localComponent1));
/*     */ 
/*  97 */     return localComponent1;
/*     */   }
/*     */ 
/*     */   public Component getComponentBefore(Container paramContainer, Component paramComponent) {
/* 101 */     Component localComponent1 = paramComponent;
/* 102 */     HashSet localHashSet = new HashSet();
/*     */     do
/*     */     {
/* 105 */       Component localComponent2 = localComponent1;
/* 106 */       localComponent1 = (Component)this.backwardMap.get(localComponent1);
/* 107 */       if (localComponent1 == null) {
/* 108 */         if ((this.delegatePolicy != null) && (localComponent2.isFocusCycleRoot(paramContainer)))
/*     */         {
/* 110 */           return this.delegatePolicy.getComponentBefore(paramContainer, localComponent2);
/*     */         }
/* 112 */         if (this.delegateManager != null) {
/* 113 */           return this.delegateManager.getComponentBefore(paramContainer, paramComponent);
/*     */         }
/*     */ 
/* 116 */         return null;
/*     */       }
/*     */ 
/* 119 */       if (localHashSet.contains(localComponent1))
/*     */       {
/* 121 */         return null;
/*     */       }
/* 123 */       localHashSet.add(localComponent1);
/* 124 */     }while (!accept(localComponent1));
/*     */ 
/* 126 */     return localComponent1;
/*     */   }
/*     */   public Component getFirstComponent(Container paramContainer) {
/* 129 */     if (this.delegatePolicy != null)
/* 130 */       return this.delegatePolicy.getFirstComponent(paramContainer);
/* 131 */     if (this.delegateManager != null) {
/* 132 */       return this.delegateManager.getFirstComponent(paramContainer);
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getLastComponent(Container paramContainer) {
/* 138 */     if (this.delegatePolicy != null)
/* 139 */       return this.delegatePolicy.getLastComponent(paramContainer);
/* 140 */     if (this.delegateManager != null) {
/* 141 */       return this.delegateManager.getLastComponent(paramContainer);
/*     */     }
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   public Component getDefaultComponent(Container paramContainer) {
/* 147 */     if (this.delegatePolicy != null) {
/* 148 */       return this.delegatePolicy.getDefaultComponent(paramContainer);
/*     */     }
/* 150 */     return getFirstComponent(paramContainer);
/*     */   }
/*     */ 
/*     */   private boolean accept(Component paramComponent) {
/* 154 */     if ((!paramComponent.isVisible()) || (!paramComponent.isDisplayable()) || (!paramComponent.isFocusable()) || (!paramComponent.isEnabled()))
/*     */     {
/* 156 */       return false;
/*     */     }
/*     */ 
/* 162 */     if (!(paramComponent instanceof Window)) {
/* 163 */       for (Container localContainer = paramComponent.getParent(); 
/* 164 */         localContainer != null; 
/* 165 */         localContainer = localContainer.getParent())
/*     */       {
/* 167 */         if ((!localContainer.isEnabled()) && (!localContainer.isLightweight())) {
/* 168 */           return false;
/*     */         }
/* 170 */         if ((localContainer instanceof Window))
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 176 */     return true;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 179 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 181 */     if ((this.delegatePolicy instanceof Serializable))
/* 182 */       paramObjectOutputStream.writeObject(this.delegatePolicy);
/*     */     else {
/* 184 */       paramObjectOutputStream.writeObject(null);
/*     */     }
/*     */ 
/* 187 */     if ((this.delegateManager instanceof Serializable))
/* 188 */       paramObjectOutputStream.writeObject(this.delegateManager);
/*     */     else
/* 190 */       paramObjectOutputStream.writeObject(null);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 196 */     paramObjectInputStream.defaultReadObject();
/* 197 */     this.delegatePolicy = ((FocusTraversalPolicy)paramObjectInputStream.readObject());
/* 198 */     this.delegateManager = ((DefaultFocusManager)paramObjectInputStream.readObject());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.LegacyGlueFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */