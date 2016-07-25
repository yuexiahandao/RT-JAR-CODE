/*     */ package sun.security.jgss.wrapper;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.util.HashMap;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.action.PutAllAction;
/*     */ 
/*     */ public final class SunNativeProvider extends Provider
/*     */ {
/*     */   private static final long serialVersionUID = -238911724858694204L;
/*     */   private static final String NAME = "SunNativeGSS";
/*     */   private static final String INFO = "Sun Native GSS provider";
/*     */   private static final String MF_CLASS = "sun.security.jgss.wrapper.NativeGSSFactory";
/*     */   private static final String LIB_PROP = "sun.security.jgss.lib";
/*     */   private static final String DEBUG_PROP = "sun.security.nativegss.debug";
/*  68 */   private static HashMap MECH_MAP = (HashMap)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public HashMap run() {
/*  71 */       SunNativeProvider.DEBUG = Boolean.parseBoolean(System.getProperty("sun.security.nativegss.debug"));
/*     */       try
/*     */       {
/*  74 */         System.loadLibrary("j2gss");
/*     */       } catch (Error localError) {
/*  76 */         SunNativeProvider.debug("No j2gss library found!");
/*  77 */         if (SunNativeProvider.DEBUG) localError.printStackTrace();
/*  78 */         return null;
/*     */       }
/*  80 */       String[] arrayOfString = new String[0];
/*  81 */       String str1 = System.getProperty("sun.security.jgss.lib");
/*     */       Object localObject;
/*  82 */       if ((str1 == null) || (str1.trim().equals(""))) {
/*  83 */         localObject = System.getProperty("os.name");
/*  84 */         if (((String)localObject).startsWith("SunOS"))
/*  85 */           arrayOfString = new String[] { "libgss.so" };
/*  86 */         else if (((String)localObject).startsWith("Linux")) {
/*  87 */           arrayOfString = new String[] { "libgssapi.so", "libgssapi_krb5.so", "libgssapi_krb5.so.2" };
/*     */         }
/*  92 */         else if (((String)localObject).contains("OS X")) {
/*  93 */           arrayOfString = new String[] { "/usr/lib/sasl2/libgssapiv2.2.so" };
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  98 */         arrayOfString = new String[] { str1 };
/*     */       }
/* 100 */       for (String str2 : arrayOfString) {
/* 101 */         if (GSSLibStub.init(str2)) {
/* 102 */           SunNativeProvider.debug("Loaded GSS library: " + str2);
/* 103 */           Oid[] arrayOfOid = GSSLibStub.indicateMechs();
/* 104 */           HashMap localHashMap = new HashMap();
/*     */ 
/* 106 */           for (int k = 0; k < arrayOfOid.length; k++) {
/* 107 */             SunNativeProvider.debug("Native MF for " + arrayOfOid[k]);
/* 108 */             localHashMap.put("GssApiMechanism." + arrayOfOid[k], "sun.security.jgss.wrapper.NativeGSSFactory");
/*     */           }
/*     */ 
/* 111 */           return localHashMap;
/*     */         }
/*     */       }
/* 114 */       return null;
/*     */     }
/*     */   });
/*     */ 
/*  56 */   static final Provider INSTANCE = new SunNativeProvider();
/*     */   static boolean DEBUG;
/*     */ 
/*     */   static void debug(String paramString)
/*     */   {
/*  59 */     if (DEBUG) {
/*  60 */       if (paramString == null) {
/*  61 */         throw new NullPointerException();
/*     */       }
/*  63 */       System.out.println("SunNativeGSS: " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SunNativeProvider()
/*     */   {
/* 121 */     super("SunNativeGSS", 1.0D, "Sun Native GSS provider");
/*     */ 
/* 123 */     if (MECH_MAP != null)
/* 124 */       AccessController.doPrivileged(new PutAllAction(this, MECH_MAP));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.wrapper.SunNativeProvider
 * JD-Core Version:    0.6.2
 */