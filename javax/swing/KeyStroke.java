/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTKeyStroke;
/*     */ import java.awt.event.KeyEvent;
/*     */ 
/*     */ public class KeyStroke extends AWTKeyStroke
/*     */ {
/*     */   private static final long serialVersionUID = -9060180771037902530L;
/*     */ 
/*     */   private KeyStroke()
/*     */   {
/*     */   }
/*     */ 
/*     */   private KeyStroke(char paramChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/*  75 */     super(paramChar, paramInt1, paramInt2, paramBoolean);
/*     */   }
/*     */ 
/*     */   public static KeyStroke getKeyStroke(char paramChar)
/*     */   {
/*  87 */     synchronized (AWTKeyStroke.class) {
/*  88 */       registerSubclass(KeyStroke.class);
/*  89 */       return (KeyStroke)getAWTKeyStroke(paramChar);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static KeyStroke getKeyStroke(char paramChar, boolean paramBoolean)
/*     */   {
/* 107 */     return new KeyStroke(paramChar, 0, 0, paramBoolean);
/*     */   }
/*     */ 
/*     */   public static KeyStroke getKeyStroke(Character paramCharacter, int paramInt)
/*     */   {
/* 148 */     synchronized (AWTKeyStroke.class) {
/* 149 */       registerSubclass(KeyStroke.class);
/* 150 */       return (KeyStroke)getAWTKeyStroke(paramCharacter, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static KeyStroke getKeyStroke(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 199 */     synchronized (AWTKeyStroke.class) {
/* 200 */       registerSubclass(KeyStroke.class);
/* 201 */       return (KeyStroke)getAWTKeyStroke(paramInt1, paramInt2, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static KeyStroke getKeyStroke(int paramInt1, int paramInt2)
/*     */   {
/* 247 */     synchronized (AWTKeyStroke.class) {
/* 248 */       registerSubclass(KeyStroke.class);
/* 249 */       return (KeyStroke)getAWTKeyStroke(paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static KeyStroke getKeyStrokeForEvent(KeyEvent paramKeyEvent)
/*     */   {
/* 266 */     synchronized (AWTKeyStroke.class) {
/* 267 */       registerSubclass(KeyStroke.class);
/* 268 */       return (KeyStroke)getAWTKeyStrokeForEvent(paramKeyEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static KeyStroke getKeyStroke(String paramString)
/*     */   {
/* 304 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 305 */       return null;
/*     */     }
/* 307 */     synchronized (AWTKeyStroke.class) {
/* 308 */       registerSubclass(KeyStroke.class);
/*     */       try {
/* 310 */         return (KeyStroke)getAWTKeyStroke(paramString);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 312 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.KeyStroke
 * JD-Core Version:    0.6.2
 */