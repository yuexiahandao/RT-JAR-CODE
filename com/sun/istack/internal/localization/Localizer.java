/*     */ package com.sun.istack.internal.localization;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class Localizer
/*     */ {
/*     */   private final Locale _locale;
/*     */   private final HashMap _resourceBundles;
/*     */ 
/*     */   public Localizer()
/*     */   {
/*  46 */     this(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public Localizer(Locale l) {
/*  50 */     this._locale = l;
/*  51 */     this._resourceBundles = new HashMap();
/*     */   }
/*     */ 
/*     */   public Locale getLocale() {
/*  55 */     return this._locale;
/*     */   }
/*     */ 
/*     */   public String localize(Localizable l) {
/*  59 */     String key = l.getKey();
/*  60 */     if (key == Localizable.NOT_LOCALIZABLE)
/*     */     {
/*  62 */       return (String)l.getArguments()[0];
/*     */     }
/*  64 */     String bundlename = l.getResourceBundleName();
/*     */     try
/*     */     {
/*  67 */       ResourceBundle bundle = (ResourceBundle)this._resourceBundles.get(bundlename);
/*     */ 
/*  70 */       if (bundle == null) {
/*     */         try {
/*  72 */           bundle = ResourceBundle.getBundle(bundlename, this._locale);
/*     */         }
/*     */         catch (MissingResourceException e)
/*     */         {
/*  81 */           int i = bundlename.lastIndexOf('.');
/*  82 */           if (i != -1) {
/*  83 */             String alternateBundleName = bundlename.substring(i + 1);
/*     */             try
/*     */             {
/*  86 */               bundle = ResourceBundle.getBundle(alternateBundleName, this._locale);
/*     */             }
/*     */             catch (MissingResourceException e2)
/*     */             {
/*  92 */               return getDefaultMessage(l);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*  97 */         this._resourceBundles.put(bundlename, bundle);
/*     */       }
/*     */ 
/* 100 */       if (bundle == null) {
/* 101 */         return getDefaultMessage(l);
/*     */       }
/*     */ 
/* 104 */       if (key == null)
/* 105 */         key = "undefined";
/*     */       String msg;
/*     */       try
/*     */       {
/* 109 */         msg = bundle.getString(key);
/*     */       }
/*     */       catch (MissingResourceException e) {
/* 112 */         msg = bundle.getString("undefined");
/*     */       }
/*     */ 
/* 116 */       Object[] args = l.getArguments();
/* 117 */       for (int i = 0; i < args.length; i++) {
/* 118 */         if ((args[i] instanceof Localizable)) {
/* 119 */           args[i] = localize((Localizable)args[i]);
/*     */         }
/*     */       }
/* 122 */       return MessageFormat.format(msg, args);
/*     */     }
/*     */     catch (MissingResourceException e) {
/*     */     }
/* 126 */     return getDefaultMessage(l);
/*     */   }
/*     */ 
/*     */   private String getDefaultMessage(Localizable l)
/*     */   {
/* 132 */     String key = l.getKey();
/* 133 */     Object[] args = l.getArguments();
/* 134 */     StringBuilder sb = new StringBuilder();
/* 135 */     sb.append("[failed to localize] ");
/* 136 */     sb.append(key);
/* 137 */     if (args != null) {
/* 138 */       sb.append('(');
/* 139 */       for (int i = 0; i < args.length; i++) {
/* 140 */         if (i != 0)
/* 141 */           sb.append(", ");
/* 142 */         sb.append(String.valueOf(args[i]));
/*     */       }
/* 144 */       sb.append(')');
/*     */     }
/* 146 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.localization.Localizer
 * JD-Core Version:    0.6.2
 */