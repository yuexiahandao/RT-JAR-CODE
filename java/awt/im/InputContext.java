/*     */ package java.awt.im;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.beans.Transient;
/*     */ import java.util.Locale;
/*     */ import sun.awt.im.InputMethodContext;
/*     */ 
/*     */ public class InputContext
/*     */ {
/*     */   public static InputContext getInstance()
/*     */   {
/*  92 */     return new InputMethodContext();
/*     */   }
/*     */ 
/*     */   public boolean selectInputMethod(Locale paramLocale)
/*     */   {
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   public void setCharacterSubsets(Character.Subset[] paramArrayOfSubset)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setCompositionEnabled(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public boolean isCompositionEnabled()
/*     */   {
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   public void reconvert()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispatchEvent(AWTEvent paramAWTEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removeNotify(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endComposition()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object getInputMethodControlObject()
/*     */   {
/* 336 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.im.InputContext
 * JD-Core Version:    0.6.2
 */