/*     */ package com.sun.awt;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Window;
/*     */ import java.awt.geom.Point2D;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.WindowAccessor;
/*     */ 
/*     */ public final class SecurityWarning
/*     */ {
/*     */   public static Dimension getSize(Window paramWindow)
/*     */   {
/*  71 */     if (paramWindow == null) {
/*  72 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/*  75 */     if (paramWindow.getWarningString() == null) {
/*  76 */       throw new IllegalArgumentException("The window must have a non-null warning string.");
/*     */     }
/*     */ 
/*  82 */     return AWTAccessor.getWindowAccessor().getSecurityWarningSize(paramWindow);
/*     */   }
/*     */ 
/*     */   public static void setPosition(Window paramWindow, Point2D paramPoint2D, float paramFloat1, float paramFloat2)
/*     */   {
/* 144 */     if (paramWindow == null) {
/* 145 */       throw new NullPointerException("The window argument should not be null.");
/*     */     }
/*     */ 
/* 148 */     if (paramWindow.getWarningString() == null) {
/* 149 */       throw new IllegalArgumentException("The window must have a non-null warning string.");
/*     */     }
/*     */ 
/* 152 */     if (paramPoint2D == null) {
/* 153 */       throw new NullPointerException("The point argument must not be null");
/*     */     }
/*     */ 
/* 156 */     if ((paramFloat1 < 0.0F) || (paramFloat1 > 1.0F)) {
/* 157 */       throw new IllegalArgumentException("alignmentX must be in the range [0.0f ... 1.0f].");
/*     */     }
/*     */ 
/* 160 */     if ((paramFloat2 < 0.0F) || (paramFloat2 > 1.0F)) {
/* 161 */       throw new IllegalArgumentException("alignmentY must be in the range [0.0f ... 1.0f].");
/*     */     }
/*     */ 
/* 165 */     AWTAccessor.getWindowAccessor().setSecurityWarningPosition(paramWindow, paramPoint2D, paramFloat1, paramFloat2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.awt.SecurityWarning
 * JD-Core Version:    0.6.2
 */