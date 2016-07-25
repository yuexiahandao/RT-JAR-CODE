/*     */ package java.nio.charset;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.spi.CharsetProvider;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.ServiceConfigurationError;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import sun.misc.ASCIICaseInsensitiveComparator;
/*     */ import sun.misc.VM;
/*     */ import sun.nio.cs.StandardCharsets;
/*     */ import sun.nio.cs.ThreadLocalCoders;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class Charset
/*     */   implements Comparable<Charset>
/*     */ {
/* 282 */   private static volatile String bugLevel = null;
/*     */ 
/* 325 */   private static CharsetProvider standardProvider = new StandardCharsets();
/*     */ 
/* 330 */   private static volatile Object[] cache1 = null;
/* 331 */   private static volatile Object[] cache2 = null;
/*     */ 
/* 389 */   private static ThreadLocal<ThreadLocal> gate = new ThreadLocal();
/*     */   private static volatile Charset defaultCharset;
/*     */   private final String name;
/*     */   private final String[] aliases;
/* 629 */   private Set<String> aliasSet = null;
/*     */ 
/*     */   static boolean atBugLevel(String paramString)
/*     */   {
/* 285 */     String str = bugLevel;
/* 286 */     if (str == null) {
/* 287 */       if (!VM.isBooted())
/* 288 */         return false;
/* 289 */       bugLevel = str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.nio.cs.bugLevel", ""));
/*     */     }
/*     */ 
/* 292 */     return str.equals(paramString);
/*     */   }
/*     */ 
/*     */   private static void checkName(String paramString)
/*     */   {
/* 305 */     int i = paramString.length();
/* 306 */     if ((!atBugLevel("1.4")) && 
/* 307 */       (i == 0)) {
/* 308 */       throw new IllegalCharsetNameException(paramString);
/*     */     }
/* 310 */     for (int j = 0; j < i; j++) {
/* 311 */       int k = paramString.charAt(j);
/* 312 */       if (((k < 65) || (k > 90)) && 
/* 313 */         ((k < 97) || (k > 122)) && 
/* 314 */         ((k < 48) || (k > 57)) && 
/* 315 */         ((k != 45) || (j == 0)) && 
/* 316 */         ((k != 43) || (j == 0)) && 
/* 317 */         ((k != 58) || (j == 0)) && 
/* 318 */         ((k != 95) || (j == 0)) && (
/* 319 */         (k != 46) || (j == 0)))
/* 320 */         throw new IllegalCharsetNameException(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void cache(String paramString, Charset paramCharset)
/*     */   {
/* 334 */     cache2 = cache1;
/* 335 */     cache1 = new Object[] { paramString, paramCharset };
/*     */   }
/*     */ 
/*     */   private static Iterator providers()
/*     */   {
/* 343 */     return new Iterator()
/*     */     {
/* 345 */       ClassLoader cl = ClassLoader.getSystemClassLoader();
/* 346 */       ServiceLoader<CharsetProvider> sl = ServiceLoader.load(CharsetProvider.class, this.cl);
/*     */ 
/* 348 */       Iterator<CharsetProvider> i = this.sl.iterator();
/*     */ 
/* 350 */       Object next = null;
/*     */ 
/*     */       private boolean getNext() {
/* 353 */         while (this.next == null) {
/*     */           try {
/* 355 */             if (!this.i.hasNext())
/* 356 */               return false;
/* 357 */             this.next = this.i.next(); } catch (ServiceConfigurationError localServiceConfigurationError) {
/*     */           }
/* 359 */           if (!(localServiceConfigurationError.getCause() instanceof SecurityException))
/*     */           {
/* 363 */             throw localServiceConfigurationError;
/*     */           }
/*     */         }
/* 366 */         return true;
/*     */       }
/*     */ 
/*     */       public boolean hasNext() {
/* 370 */         return getNext();
/*     */       }
/*     */ 
/*     */       public Object next() {
/* 374 */         if (!getNext())
/* 375 */           throw new NoSuchElementException();
/* 376 */         Object localObject = this.next;
/* 377 */         this.next = null;
/* 378 */         return localObject;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 382 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static Charset lookupViaProviders(String paramString)
/*     */   {
/* 401 */     if (!VM.isBooted()) {
/* 402 */       return null;
/*     */     }
/* 404 */     if (gate.get() != null)
/*     */     {
/* 406 */       return null;
/*     */     }try {
/* 408 */       gate.set(gate);
/*     */ 
/* 410 */       return (Charset)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Charset run() {
/* 413 */           for (Iterator localIterator = Charset.access$000(); localIterator.hasNext(); ) {
/* 414 */             CharsetProvider localCharsetProvider = (CharsetProvider)localIterator.next();
/* 415 */             Charset localCharset = localCharsetProvider.charsetForName(this.val$charsetName);
/* 416 */             if (localCharset != null)
/* 417 */               return localCharset;
/*     */           }
/* 419 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */     finally {
/* 424 */       gate.set(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Charset lookupExtendedCharset(String paramString)
/*     */   {
/* 454 */     CharsetProvider localCharsetProvider = ExtendedProviderHolder.extendedProvider;
/* 455 */     return localCharsetProvider != null ? localCharsetProvider.charsetForName(paramString) : null;
/*     */   }
/*     */ 
/*     */   private static Charset lookup(String paramString) {
/* 459 */     if (paramString == null)
/* 460 */       throw new IllegalArgumentException("Null charset name");
/*     */     Object[] arrayOfObject;
/* 463 */     if (((arrayOfObject = cache1) != null) && (paramString.equals(arrayOfObject[0]))) {
/* 464 */       return (Charset)arrayOfObject[1];
/*     */     }
/*     */ 
/* 468 */     return lookup2(paramString);
/*     */   }
/*     */ 
/*     */   private static Charset lookup2(String paramString)
/*     */   {
/*     */     Object[] arrayOfObject;
/* 473 */     if (((arrayOfObject = cache2) != null) && (paramString.equals(arrayOfObject[0]))) {
/* 474 */       cache2 = cache1;
/* 475 */       cache1 = arrayOfObject;
/* 476 */       return (Charset)arrayOfObject[1];
/*     */     }
/*     */     Charset localCharset;
/* 480 */     if (((localCharset = standardProvider.charsetForName(paramString)) != null) || ((localCharset = lookupExtendedCharset(paramString)) != null) || ((localCharset = lookupViaProviders(paramString)) != null))
/*     */     {
/* 484 */       cache(paramString, localCharset);
/* 485 */       return localCharset;
/*     */     }
/*     */ 
/* 489 */     checkName(paramString);
/* 490 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isSupported(String paramString)
/*     */   {
/* 510 */     return lookup(paramString) != null;
/*     */   }
/*     */ 
/*     */   public static Charset forName(String paramString)
/*     */   {
/* 533 */     Charset localCharset = lookup(paramString);
/* 534 */     if (localCharset != null)
/* 535 */       return localCharset;
/* 536 */     throw new UnsupportedCharsetException(paramString);
/*     */   }
/*     */ 
/*     */   private static void put(Iterator<Charset> paramIterator, Map<String, Charset> paramMap)
/*     */   {
/* 543 */     while (paramIterator.hasNext()) {
/* 544 */       Charset localCharset = (Charset)paramIterator.next();
/* 545 */       if (!paramMap.containsKey(localCharset.name()))
/* 546 */         paramMap.put(localCharset.name(), localCharset);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SortedMap<String, Charset> availableCharsets()
/*     */   {
/* 577 */     return (SortedMap)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public SortedMap<String, Charset> run() {
/* 580 */         TreeMap localTreeMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
/*     */ 
/* 583 */         Charset.put(Charset.standardProvider.charsets(), localTreeMap);
/* 584 */         CharsetProvider localCharsetProvider1 = Charset.ExtendedProviderHolder.extendedProvider;
/* 585 */         if (localCharsetProvider1 != null)
/* 586 */           Charset.put(localCharsetProvider1.charsets(), localTreeMap);
/* 587 */         for (Iterator localIterator = Charset.access$000(); localIterator.hasNext(); ) {
/* 588 */           CharsetProvider localCharsetProvider2 = (CharsetProvider)localIterator.next();
/* 589 */           Charset.put(localCharsetProvider2.charsets(), localTreeMap);
/*     */         }
/* 591 */         return Collections.unmodifiableSortedMap(localTreeMap);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public static Charset defaultCharset()
/*     */   {
/* 610 */     if (defaultCharset == null) {
/* 611 */       synchronized (Charset.class) {
/* 612 */         String str = (String)AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
/*     */ 
/* 614 */         Charset localCharset = lookup(str);
/* 615 */         if (localCharset != null)
/* 616 */           defaultCharset = localCharset;
/*     */         else
/* 618 */           defaultCharset = forName("UTF-8");
/*     */       }
/*     */     }
/* 621 */     return defaultCharset;
/*     */   }
/*     */ 
/*     */   protected Charset(String paramString, String[] paramArrayOfString)
/*     */   {
/* 645 */     checkName(paramString);
/* 646 */     String[] arrayOfString = paramArrayOfString == null ? new String[0] : paramArrayOfString;
/* 647 */     for (int i = 0; i < arrayOfString.length; i++)
/* 648 */       checkName(arrayOfString[i]);
/* 649 */     this.name = paramString;
/* 650 */     this.aliases = arrayOfString;
/*     */   }
/*     */ 
/*     */   public final String name()
/*     */   {
/* 659 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final Set<String> aliases()
/*     */   {
/* 668 */     if (this.aliasSet != null)
/* 669 */       return this.aliasSet;
/* 670 */     int i = this.aliases.length;
/* 671 */     HashSet localHashSet = new HashSet(i);
/* 672 */     for (int j = 0; j < i; j++)
/* 673 */       localHashSet.add(this.aliases[j]);
/* 674 */     this.aliasSet = Collections.unmodifiableSet(localHashSet);
/* 675 */     return this.aliasSet;
/*     */   }
/*     */ 
/*     */   public String displayName()
/*     */   {
/* 688 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final boolean isRegistered()
/*     */   {
/* 700 */     return (!this.name.startsWith("X-")) && (!this.name.startsWith("x-"));
/*     */   }
/*     */ 
/*     */   public String displayName(Locale paramLocale)
/*     */   {
/* 716 */     return this.name;
/*     */   }
/*     */ 
/*     */   public abstract boolean contains(Charset paramCharset);
/*     */ 
/*     */   public abstract CharsetDecoder newDecoder();
/*     */ 
/*     */   public abstract CharsetEncoder newEncoder();
/*     */ 
/*     */   public boolean canEncode()
/*     */   {
/* 776 */     return true;
/*     */   }
/*     */ 
/*     */   public final CharBuffer decode(ByteBuffer paramByteBuffer)
/*     */   {
/*     */     try
/*     */     {
/* 806 */       return ThreadLocalCoders.decoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(paramByteBuffer);
/*     */     }
/*     */     catch (CharacterCodingException localCharacterCodingException)
/*     */     {
/* 811 */       throw new Error(localCharacterCodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final ByteBuffer encode(CharBuffer paramCharBuffer)
/*     */   {
/*     */     try
/*     */     {
/* 842 */       return ThreadLocalCoders.encoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).encode(paramCharBuffer);
/*     */     }
/*     */     catch (CharacterCodingException localCharacterCodingException)
/*     */     {
/* 847 */       throw new Error(localCharacterCodingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final ByteBuffer encode(String paramString)
/*     */   {
/* 865 */     return encode(CharBuffer.wrap(paramString));
/*     */   }
/*     */ 
/*     */   public final int compareTo(Charset paramCharset)
/*     */   {
/* 881 */     return name().compareToIgnoreCase(paramCharset.name());
/*     */   }
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 890 */     return name().hashCode();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object paramObject)
/*     */   {
/* 903 */     if (!(paramObject instanceof Charset))
/* 904 */       return false;
/* 905 */     if (this == paramObject)
/* 906 */       return true;
/* 907 */     return this.name.equals(((Charset)paramObject).name());
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 916 */     return name();
/*     */   }
/*     */ 
/*     */   private static class ExtendedProviderHolder
/*     */   {
/* 430 */     static final CharsetProvider extendedProvider = extendedProvider();
/*     */ 
/*     */     private static CharsetProvider extendedProvider() {
/* 433 */       return (CharsetProvider)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public CharsetProvider run() {
/*     */           try {
/* 437 */             Class localClass = Class.forName("sun.nio.cs.ext.ExtendedCharsets");
/*     */ 
/* 439 */             return (CharsetProvider)localClass.newInstance();
/*     */           }
/*     */           catch (ClassNotFoundException localClassNotFoundException)
/*     */           {
/*     */           }
/*     */           catch (InstantiationException|IllegalAccessException localInstantiationException) {
/* 445 */             throw new Error(localInstantiationException);
/*     */           }
/* 447 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.charset.Charset
 * JD-Core Version:    0.6.2
 */