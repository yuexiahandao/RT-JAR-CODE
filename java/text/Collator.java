/*     */ package java.text;
/*     */ 
/*     */ import java.text.spi.CollatorProvider;
/*     */ import java.util.Comparator;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import sun.misc.SoftCache;
/*     */ import sun.util.LocaleServiceProviderPool;
/*     */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*     */ import sun.util.resources.LocaleData;
/*     */ 
/*     */ public abstract class Collator
/*     */   implements Comparator<Object>, Cloneable
/*     */ {
/*     */   public static final int PRIMARY = 0;
/*     */   public static final int SECONDARY = 1;
/*     */   public static final int TERTIARY = 2;
/*     */   public static final int IDENTICAL = 3;
/*     */   public static final int NO_DECOMPOSITION = 0;
/*     */   public static final int CANONICAL_DECOMPOSITION = 1;
/*     */   public static final int FULL_DECOMPOSITION = 2;
/* 501 */   private int strength = 0;
/* 502 */   private int decmp = 0;
/* 503 */   private static SoftCache cache = new SoftCache();
/*     */   static final int LESS = -1;
/*     */   static final int EQUAL = 0;
/*     */   static final int GREATER = 1;
/*     */ 
/*     */   public static synchronized Collator getInstance()
/*     */   {
/* 224 */     return getInstance(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static synchronized Collator getInstance(Locale paramLocale)
/*     */   {
/* 237 */     Object localObject1 = (Collator)cache.get(paramLocale);
/* 238 */     if (localObject1 != null) {
/* 239 */       return (Collator)((Collator)localObject1).clone();
/*     */     }
/*     */ 
/* 244 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(CollatorProvider.class);
/*     */ 
/* 246 */     if (localLocaleServiceProviderPool.hasProviders()) {
/* 247 */       localObject2 = (Collator)localLocaleServiceProviderPool.getLocalizedObject(CollatorGetter.INSTANCE, paramLocale, new Object[] { paramLocale });
/*     */ 
/* 251 */       if (localObject2 != null) {
/* 252 */         return localObject2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     Object localObject2 = "";
/*     */     try {
/* 260 */       ResourceBundle localResourceBundle = LocaleData.getCollationData(paramLocale);
/*     */ 
/* 262 */       localObject2 = localResourceBundle.getString("Rule");
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException)
/*     */     {
/*     */     }
/*     */     try {
/* 268 */       localObject1 = new RuleBasedCollator(CollationRules.DEFAULTRULES + (String)localObject2, 1);
/*     */     }
/*     */     catch (ParseException localParseException1)
/*     */     {
/*     */       try
/*     */       {
/* 276 */         localObject1 = new RuleBasedCollator(CollationRules.DEFAULTRULES);
/*     */       }
/*     */       catch (ParseException localParseException2)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 284 */     ((Collator)localObject1).setDecomposition(0);
/*     */ 
/* 286 */     cache.put(paramLocale, localObject1);
/* 287 */     return (Collator)((Collator)localObject1).clone();
/*     */   }
/*     */ 
/*     */   public abstract int compare(String paramString1, String paramString2);
/*     */ 
/*     */   public int compare(Object paramObject1, Object paramObject2)
/*     */   {
/* 327 */     return compare((String)paramObject1, (String)paramObject2);
/*     */   }
/*     */ 
/*     */   public abstract CollationKey getCollationKey(String paramString);
/*     */ 
/*     */   public boolean equals(String paramString1, String paramString2)
/*     */   {
/* 354 */     return compare(paramString1, paramString2) == 0;
/*     */   }
/*     */ 
/*     */   public synchronized int getStrength()
/*     */   {
/* 370 */     return this.strength;
/*     */   }
/*     */ 
/*     */   public synchronized void setStrength(int paramInt)
/*     */   {
/* 387 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2) && (paramInt != 3))
/*     */     {
/* 391 */       throw new IllegalArgumentException("Incorrect comparison level.");
/* 392 */     }this.strength = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized int getDecomposition()
/*     */   {
/* 416 */     return this.decmp;
/*     */   }
/*     */ 
/*     */   public synchronized void setDecomposition(int paramInt)
/*     */   {
/* 430 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2))
/*     */     {
/* 433 */       throw new IllegalArgumentException("Wrong decomposition mode.");
/* 434 */     }this.decmp = paramInt;
/*     */   }
/*     */ 
/*     */   public static synchronized Locale[] getAvailableLocales()
/*     */   {
/* 451 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(CollatorProvider.class);
/*     */ 
/* 453 */     return localLocaleServiceProviderPool.getAvailableLocales();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 462 */       return (Collator)super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 464 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 476 */     if (this == paramObject) return true;
/* 477 */     if (paramObject == null) return false;
/* 478 */     if (getClass() != paramObject.getClass()) return false;
/* 479 */     Collator localCollator = (Collator)paramObject;
/* 480 */     return (this.strength == localCollator.strength) && (this.decmp == localCollator.decmp);
/*     */   }
/*     */ 
/*     */   public abstract int hashCode();
/*     */ 
/*     */   protected Collator()
/*     */   {
/* 497 */     this.strength = 2;
/* 498 */     this.decmp = 1;
/*     */   }
/*     */ 
/*     */   private static class CollatorGetter
/*     */     implements LocaleServiceProviderPool.LocalizedObjectGetter<CollatorProvider, Collator>
/*     */   {
/* 533 */     private static final CollatorGetter INSTANCE = new CollatorGetter();
/*     */ 
/*     */     public Collator getObject(CollatorProvider paramCollatorProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 539 */       assert (paramArrayOfObject.length == 1);
/* 540 */       Collator localCollator = paramCollatorProvider.getInstance(paramLocale);
/* 541 */       if (localCollator != null)
/*     */       {
/* 545 */         Collator.cache.put((Locale)paramArrayOfObject[0], localCollator);
/* 546 */         Collator.cache.put(paramLocale, localCollator);
/* 547 */         return (Collator)localCollator.clone();
/*     */       }
/*     */ 
/* 550 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.Collator
 * JD-Core Version:    0.6.2
 */