/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIDefaults.ActiveValue;
/*     */ import javax.swing.UIDefaults.LazyValue;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ 
/*     */ public class DesktopProperty
/*     */   implements UIDefaults.ActiveValue
/*     */ {
/*     */   private static boolean updatePending;
/*  52 */   private static final ReferenceQueue<DesktopProperty> queue = new ReferenceQueue();
/*     */   private WeakPCL pcl;
/*     */   private final String key;
/*     */   private Object value;
/*     */   private final Object fallback;
/*     */ 
/*     */   static void flushUnreferencedProperties()
/*     */   {
/*     */     WeakPCL localWeakPCL;
/*  79 */     while ((localWeakPCL = (WeakPCL)queue.poll()) != null)
/*  80 */       localWeakPCL.dispose();
/*     */   }
/*     */ 
/*     */   private static synchronized void setUpdatePending(boolean paramBoolean)
/*     */   {
/*  89 */     updatePending = paramBoolean;
/*     */   }
/*     */ 
/*     */   private static synchronized boolean isUpdatePending()
/*     */   {
/*  96 */     return updatePending;
/*     */   }
/*     */ 
/*     */   private static void updateAllUIs()
/*     */   {
/* 105 */     Class localClass = UIManager.getLookAndFeel().getClass();
/* 106 */     if (localClass.getPackage().equals(DesktopProperty.class.getPackage())) {
/* 107 */       XPStyle.invalidateStyle();
/*     */     }
/* 109 */     Frame[] arrayOfFrame1 = Frame.getFrames();
/* 110 */     for (Frame localFrame : arrayOfFrame1)
/* 111 */       updateWindowUI(localFrame);
/*     */   }
/*     */ 
/*     */   private static void updateWindowUI(Window paramWindow)
/*     */   {
/* 119 */     SwingUtilities.updateComponentTreeUI(paramWindow);
/* 120 */     Window[] arrayOfWindow1 = paramWindow.getOwnedWindows();
/* 121 */     for (Window localWindow : arrayOfWindow1)
/* 122 */       updateWindowUI(localWindow);
/*     */   }
/*     */ 
/*     */   public DesktopProperty(String paramString, Object paramObject)
/*     */   {
/* 134 */     this.key = paramString;
/* 135 */     this.fallback = paramObject;
/*     */ 
/* 145 */     flushUnreferencedProperties();
/*     */   }
/*     */ 
/*     */   public Object createValue(UIDefaults paramUIDefaults)
/*     */   {
/* 153 */     if (this.value == null) {
/* 154 */       this.value = configureValue(getValueFromDesktop());
/* 155 */       if (this.value == null) {
/* 156 */         this.value = configureValue(getDefaultValue());
/*     */       }
/*     */     }
/* 159 */     return this.value;
/*     */   }
/*     */ 
/*     */   protected Object getValueFromDesktop()
/*     */   {
/* 166 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*     */ 
/* 168 */     if (this.pcl == null) {
/* 169 */       this.pcl = new WeakPCL(this, getKey(), UIManager.getLookAndFeel());
/* 170 */       localToolkit.addPropertyChangeListener(getKey(), this.pcl);
/*     */     }
/*     */ 
/* 173 */     return localToolkit.getDesktopProperty(getKey());
/*     */   }
/*     */ 
/*     */   protected Object getDefaultValue()
/*     */   {
/* 180 */     return this.fallback;
/*     */   }
/*     */ 
/*     */   public void invalidate(LookAndFeel paramLookAndFeel)
/*     */   {
/* 189 */     invalidate();
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/* 197 */     this.value = null;
/*     */   }
/*     */ 
/*     */   protected void updateUI()
/*     */   {
/* 208 */     if (!isUpdatePending()) {
/* 209 */       setUpdatePending(true);
/* 210 */       Runnable local1 = new Runnable() {
/*     */         public void run() {
/* 212 */           DesktopProperty.access$000();
/* 213 */           DesktopProperty.setUpdatePending(false);
/*     */         }
/*     */       };
/* 216 */       SwingUtilities.invokeLater(local1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object configureValue(Object paramObject)
/*     */   {
/* 225 */     if (paramObject != null) {
/* 226 */       if ((paramObject instanceof Color)) {
/* 227 */         return new ColorUIResource((Color)paramObject);
/*     */       }
/* 229 */       if ((paramObject instanceof Font)) {
/* 230 */         return new FontUIResource((Font)paramObject);
/*     */       }
/* 232 */       if ((paramObject instanceof UIDefaults.LazyValue)) {
/* 233 */         paramObject = ((UIDefaults.LazyValue)paramObject).createValue(null);
/*     */       }
/* 235 */       else if ((paramObject instanceof UIDefaults.ActiveValue)) {
/* 236 */         paramObject = ((UIDefaults.ActiveValue)paramObject).createValue(null);
/*     */       }
/*     */     }
/* 239 */     return paramObject;
/*     */   }
/*     */ 
/*     */   protected String getKey()
/*     */   {
/* 246 */     return this.key;
/*     */   }
/*     */ 
/*     */   private static class WeakPCL extends WeakReference<DesktopProperty>
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private String key;
/*     */     private LookAndFeel laf;
/*     */ 
/*     */     WeakPCL(DesktopProperty paramDesktopProperty, String paramString, LookAndFeel paramLookAndFeel)
/*     */     {
/* 262 */       super(DesktopProperty.queue);
/* 263 */       this.key = paramString;
/* 264 */       this.laf = paramLookAndFeel;
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 268 */       DesktopProperty localDesktopProperty = (DesktopProperty)get();
/*     */ 
/* 270 */       if ((localDesktopProperty == null) || (this.laf != UIManager.getLookAndFeel()))
/*     */       {
/* 273 */         dispose();
/*     */       }
/*     */       else {
/* 276 */         localDesktopProperty.invalidate(this.laf);
/* 277 */         localDesktopProperty.updateUI();
/*     */       }
/*     */     }
/*     */ 
/*     */     void dispose() {
/* 282 */       Toolkit.getDefaultToolkit().removePropertyChangeListener(this.key, this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.DesktopProperty
 * JD-Core Version:    0.6.2
 */