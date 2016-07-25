/*     */ package javax.smartcardio;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ 
/*     */ public final class TerminalFactory
/*     */ {
/*     */   private static final String PROP_NAME = "javax.smartcardio.TerminalFactory.DefaultType";
/* 129 */   private static final String defaultType = str;
/* 130 */   private static final TerminalFactory defaultFactory = localTerminalFactory;
/*     */   private final TerminalFactorySpi spi;
/*     */   private final Provider provider;
/*     */   private final String type;
/*     */ 
/*     */   private TerminalFactory(TerminalFactorySpi paramTerminalFactorySpi, Provider paramProvider, String paramString)
/*     */   {
/* 173 */     this.spi = paramTerminalFactorySpi;
/* 174 */     this.provider = paramProvider;
/* 175 */     this.type = paramString;
/*     */   }
/*     */ 
/*     */   public static String getDefaultType()
/*     */   {
/* 199 */     return defaultType;
/*     */   }
/*     */ 
/*     */   public static TerminalFactory getDefault()
/*     */   {
/* 212 */     return defaultFactory;
/*     */   }
/*     */ 
/*     */   public static TerminalFactory getInstance(String paramString, Object paramObject)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 243 */     GetInstance.Instance localInstance = GetInstance.getInstance("TerminalFactory", TerminalFactorySpi.class, paramString, paramObject);
/*     */ 
/* 245 */     return new TerminalFactory((TerminalFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static TerminalFactory getInstance(String paramString1, Object paramObject, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 282 */     GetInstance.Instance localInstance = GetInstance.getInstance("TerminalFactory", TerminalFactorySpi.class, paramString1, paramObject, paramString2);
/*     */ 
/* 284 */     return new TerminalFactory((TerminalFactorySpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static TerminalFactory getInstance(String paramString, Object paramObject, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 314 */     GetInstance.Instance localInstance = GetInstance.getInstance("TerminalFactory", TerminalFactorySpi.class, paramString, paramObject, paramProvider);
/*     */ 
/* 316 */     return new TerminalFactory((TerminalFactorySpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public Provider getProvider()
/*     */   {
/* 326 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 336 */     return this.type;
/*     */   }
/*     */ 
/*     */   public CardTerminals terminals()
/*     */   {
/* 349 */     return this.spi.engineTerminals();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 358 */     return "TerminalFactory for type " + this.type + " from provider " + this.provider.getName();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 102 */     String str = ((String)AccessController.doPrivileged(new GetPropertyAction("javax.smartcardio.TerminalFactory.DefaultType", "PC/SC"))).trim();
/*     */ 
/* 104 */     TerminalFactory localTerminalFactory = null;
/*     */     try {
/* 106 */       localTerminalFactory = getInstance(str, null);
/*     */     }
/*     */     catch (Exception localException1) {
/*     */     }
/* 110 */     if (localTerminalFactory == null)
/*     */       try
/*     */       {
/* 113 */         str = "PC/SC";
/* 114 */         Provider localProvider = Security.getProvider("SunPCSC");
/* 115 */         if (localProvider == null) {
/* 116 */           Class localClass = Class.forName("sun.security.smartcardio.SunPCSC");
/* 117 */           localProvider = (Provider)localClass.newInstance();
/*     */         }
/* 119 */         localTerminalFactory = getInstance(str, null, localProvider);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/* 124 */     if (localTerminalFactory == null) {
/* 125 */       str = "None";
/* 126 */       localTerminalFactory = new TerminalFactory(NoneFactorySpi.INSTANCE, NoneProvider.INSTANCE, "None");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class NoneCardTerminals extends CardTerminals
/*     */   {
/* 151 */     static final CardTerminals INSTANCE = new NoneCardTerminals();
/*     */ 
/*     */     public List<CardTerminal> list(CardTerminals.State paramState)
/*     */       throws CardException
/*     */     {
/* 156 */       if (paramState == null) {
/* 157 */         throw new NullPointerException();
/*     */       }
/* 159 */       return Collections.emptyList();
/*     */     }
/*     */     public boolean waitForChange(long paramLong) throws CardException {
/* 162 */       throw new IllegalStateException("no terminals");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class NoneFactorySpi extends TerminalFactorySpi
/*     */   {
/* 141 */     static final TerminalFactorySpi INSTANCE = new NoneFactorySpi();
/*     */ 
/*     */     protected CardTerminals engineTerminals()
/*     */     {
/* 146 */       return TerminalFactory.NoneCardTerminals.INSTANCE;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class NoneProvider extends Provider
/*     */   {
/* 134 */     static final Provider INSTANCE = new NoneProvider();
/*     */ 
/* 136 */     private NoneProvider() { super(1.0D, "none"); }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.smartcardio.TerminalFactory
 * JD-Core Version:    0.6.2
 */