/*     */ package java.text;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.spi.BreakIteratorProvider;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import sun.util.LocaleServiceProviderPool;
/*     */ import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
/*     */ 
/*     */ public abstract class BreakIterator
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int DONE = -1;
/*     */   private static final int CHARACTER_INDEX = 0;
/*     */   private static final int WORD_INDEX = 1;
/*     */   private static final int LINE_INDEX = 2;
/*     */   private static final int SENTENCE_INDEX = 3;
/* 442 */   private static final SoftReference[] iterCache = new SoftReference[4];
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 254 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 257 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public abstract int first();
/*     */ 
/*     */   public abstract int last();
/*     */ 
/*     */   public abstract int next(int paramInt);
/*     */ 
/*     */   public abstract int next();
/*     */ 
/*     */   public abstract int previous();
/*     */ 
/*     */   public abstract int following(int paramInt);
/*     */ 
/*     */   public int preceding(int paramInt)
/*     */   {
/* 362 */     int i = following(paramInt);
/* 363 */     while ((i >= paramInt) && (i != -1))
/* 364 */       i = previous();
/* 365 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean isBoundary(int paramInt)
/*     */   {
/* 386 */     if (paramInt == 0) {
/* 387 */       return true;
/*     */     }
/* 389 */     int i = following(paramInt - 1);
/* 390 */     if (i == -1) {
/* 391 */       throw new IllegalArgumentException();
/*     */     }
/* 393 */     return i == paramInt;
/*     */   }
/*     */ 
/*     */   public abstract int current();
/*     */ 
/*     */   public abstract CharacterIterator getText();
/*     */ 
/*     */   public void setText(String paramString)
/*     */   {
/* 428 */     setText(new StringCharacterIterator(paramString));
/*     */   }
/*     */ 
/*     */   public abstract void setText(CharacterIterator paramCharacterIterator);
/*     */ 
/*     */   public static BreakIterator getWordInstance()
/*     */   {
/* 452 */     return getWordInstance(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static BreakIterator getWordInstance(Locale paramLocale)
/*     */   {
/* 465 */     return getBreakInstance(paramLocale, 1, "WordData", "WordDictionary");
/*     */   }
/*     */ 
/*     */   public static BreakIterator getLineInstance()
/*     */   {
/* 479 */     return getLineInstance(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static BreakIterator getLineInstance(Locale paramLocale)
/*     */   {
/* 492 */     return getBreakInstance(paramLocale, 2, "LineData", "LineDictionary");
/*     */   }
/*     */ 
/*     */   public static BreakIterator getCharacterInstance()
/*     */   {
/* 506 */     return getCharacterInstance(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static BreakIterator getCharacterInstance(Locale paramLocale)
/*     */   {
/* 519 */     return getBreakInstance(paramLocale, 0, "CharacterData", "CharacterDictionary");
/*     */   }
/*     */ 
/*     */   public static BreakIterator getSentenceInstance()
/*     */   {
/* 533 */     return getSentenceInstance(Locale.getDefault());
/*     */   }
/*     */ 
/*     */   public static BreakIterator getSentenceInstance(Locale paramLocale)
/*     */   {
/* 546 */     return getBreakInstance(paramLocale, 3, "SentenceData", "SentenceDictionary");
/*     */   }
/*     */ 
/*     */   private static BreakIterator getBreakInstance(Locale paramLocale, int paramInt, String paramString1, String paramString2)
/*     */   {
/* 556 */     if (iterCache[paramInt] != null) {
/* 557 */       localObject = (BreakIteratorCache)iterCache[paramInt].get();
/* 558 */       if ((localObject != null) && 
/* 559 */         (((BreakIteratorCache)localObject).getLocale().equals(paramLocale))) {
/* 560 */         return ((BreakIteratorCache)localObject).createBreakInstance();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 565 */     Object localObject = createBreakInstance(paramLocale, paramInt, paramString1, paramString2);
/*     */ 
/* 569 */     BreakIteratorCache localBreakIteratorCache = new BreakIteratorCache(paramLocale, (BreakIterator)localObject);
/* 570 */     iterCache[paramInt] = new SoftReference(localBreakIteratorCache);
/* 571 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static ResourceBundle getBundle(String paramString, final Locale paramLocale) {
/* 575 */     return (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 577 */         return ResourceBundle.getBundle(this.val$baseName, paramLocale);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static BreakIterator createBreakInstance(Locale paramLocale, int paramInt, String paramString1, String paramString2)
/*     */   {
/* 589 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(BreakIteratorProvider.class);
/*     */ 
/* 591 */     if (localLocaleServiceProviderPool.hasProviders()) {
/* 592 */       localObject = (BreakIterator)localLocaleServiceProviderPool.getLocalizedObject(BreakIteratorGetter.INSTANCE, paramLocale, new Object[] { Integer.valueOf(paramInt) });
/*     */ 
/* 595 */       if (localObject != null) {
/* 596 */         return localObject;
/*     */       }
/*     */     }
/*     */ 
/* 600 */     Object localObject = getBundle("sun.text.resources.BreakIteratorInfo", paramLocale);
/*     */ 
/* 602 */     String[] arrayOfString = ((ResourceBundle)localObject).getStringArray("BreakIteratorClasses");
/*     */ 
/* 604 */     String str1 = ((ResourceBundle)localObject).getString(paramString1);
/*     */     try
/*     */     {
/* 607 */       if (arrayOfString[paramInt].equals("RuleBasedBreakIterator")) {
/* 608 */         return new RuleBasedBreakIterator(str1);
/*     */       }
/* 610 */       if (arrayOfString[paramInt].equals("DictionaryBasedBreakIterator")) {
/* 611 */         String str2 = ((ResourceBundle)localObject).getString(paramString2);
/* 612 */         return new DictionaryBasedBreakIterator(str1, str2);
/*     */       }
/*     */ 
/* 615 */       throw new IllegalArgumentException("Invalid break iterator class \"" + arrayOfString[paramInt] + "\"");
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 620 */       throw new InternalError(localException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static synchronized Locale[] getAvailableLocales()
/*     */   {
/* 639 */     LocaleServiceProviderPool localLocaleServiceProviderPool = LocaleServiceProviderPool.getPool(BreakIteratorProvider.class);
/*     */ 
/* 641 */     return localLocaleServiceProviderPool.getAvailableLocales();
/*     */   }
/*     */ 
/*     */   static long getLong(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 664 */     long l = paramArrayOfByte[paramInt] & 0xFF;
/* 665 */     for (int i = 1; i < 8; i++) {
/* 666 */       l = l << 8 | paramArrayOfByte[(paramInt + i)] & 0xFF;
/*     */     }
/* 668 */     return l;
/*     */   }
/*     */ 
/*     */   static int getInt(byte[] paramArrayOfByte, int paramInt) {
/* 672 */     int i = paramArrayOfByte[paramInt] & 0xFF;
/* 673 */     for (int j = 1; j < 4; j++) {
/* 674 */       i = i << 8 | paramArrayOfByte[(paramInt + j)] & 0xFF;
/*     */     }
/* 676 */     return i;
/*     */   }
/*     */ 
/*     */   static short getShort(byte[] paramArrayOfByte, int paramInt) {
/* 680 */     short s = (short)(paramArrayOfByte[paramInt] & 0xFF);
/* 681 */     s = (short)(s << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF);
/* 682 */     return s;
/*     */   }
/*     */ 
/*     */   private static final class BreakIteratorCache
/*     */   {
/*     */     private BreakIterator iter;
/*     */     private Locale locale;
/*     */ 
/*     */     BreakIteratorCache(Locale paramLocale, BreakIterator paramBreakIterator)
/*     */     {
/* 650 */       this.locale = paramLocale;
/* 651 */       this.iter = ((BreakIterator)paramBreakIterator.clone());
/*     */     }
/*     */ 
/*     */     Locale getLocale() {
/* 655 */       return this.locale;
/*     */     }
/*     */ 
/*     */     BreakIterator createBreakInstance() {
/* 659 */       return (BreakIterator)this.iter.clone();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class BreakIteratorGetter
/*     */     implements LocaleServiceProviderPool.LocalizedObjectGetter<BreakIteratorProvider, BreakIterator>
/*     */   {
/* 691 */     private static final BreakIteratorGetter INSTANCE = new BreakIteratorGetter();
/*     */ 
/*     */     public BreakIterator getObject(BreakIteratorProvider paramBreakIteratorProvider, Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 698 */       assert (paramArrayOfObject.length == 1);
/*     */ 
/* 700 */       switch (((Integer)paramArrayOfObject[0]).intValue()) {
/*     */       case 0:
/* 702 */         return paramBreakIteratorProvider.getCharacterInstance(paramLocale);
/*     */       case 1:
/* 704 */         return paramBreakIteratorProvider.getWordInstance(paramLocale);
/*     */       case 2:
/* 706 */         return paramBreakIteratorProvider.getLineInstance(paramLocale);
/*     */       case 3:
/* 708 */         return paramBreakIteratorProvider.getSentenceInstance(paramLocale);
/*     */       }
/* 710 */       if (!$assertionsDisabled) throw new AssertionError("should not happen");
/*     */ 
/* 712 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.BreakIterator
 * JD-Core Version:    0.6.2
 */