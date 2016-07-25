/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class Debug
/*     */ {
/*     */   private String prefix;
/*  45 */   private static String args = (String)AccessController.doPrivileged(new GetPropertyAction("java.security.debug"));
/*     */ 
/* 274 */   private static final char[] hexDigits = "0123456789abcdef".toCharArray();
/*     */ 
/*     */   public static void Help()
/*     */   {
/*  70 */     System.err.println();
/*  71 */     System.err.println("all           turn on all debugging");
/*  72 */     System.err.println("access        print all checkPermission results");
/*  73 */     System.err.println("combiner      SubjectDomainCombiner debugging");
/*  74 */     System.err.println("gssloginconfig");
/*  75 */     System.err.println("configfile    JAAS ConfigFile loading");
/*  76 */     System.err.println("configparser  JAAS ConfigFile parsing");
/*  77 */     System.err.println("              GSS LoginConfigImpl debugging");
/*  78 */     System.err.println("jar           jar verification");
/*  79 */     System.err.println("logincontext  login context results");
/*  80 */     System.err.println("policy        loading and granting");
/*  81 */     System.err.println("provider      security provider debugging");
/*  82 */     System.err.println("scl           permissions SecureClassLoader assigns");
/*  83 */     System.err.println();
/*  84 */     System.err.println("The following can be used with access:");
/*  85 */     System.err.println();
/*  86 */     System.err.println("stack         include stack trace");
/*  87 */     System.err.println("domain        dump all domains in context");
/*  88 */     System.err.println("failure       before throwing exception, dump stack");
/*  89 */     System.err.println("              and domain that didn't have permission");
/*  90 */     System.err.println();
/*  91 */     System.err.println("The following can be used with stack and domain:");
/*  92 */     System.err.println();
/*  93 */     System.err.println("permission=<classname>");
/*  94 */     System.err.println("              only dump output if specified permission");
/*  95 */     System.err.println("              is being checked");
/*  96 */     System.err.println("codebase=<URL>");
/*  97 */     System.err.println("              only dump output if specified codebase");
/*  98 */     System.err.println("              is being checked");
/*     */ 
/* 100 */     System.err.println();
/* 101 */     System.err.println("Note: Separate multiple options with a comma");
/* 102 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   public static Debug getInstance(String paramString)
/*     */   {
/* 113 */     return getInstance(paramString, paramString);
/*     */   }
/*     */ 
/*     */   public static Debug getInstance(String paramString1, String paramString2)
/*     */   {
/* 122 */     if (isOn(paramString1)) {
/* 123 */       Debug localDebug = new Debug();
/* 124 */       localDebug.prefix = paramString2;
/* 125 */       return localDebug;
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isOn(String paramString)
/*     */   {
/* 137 */     if (args == null) {
/* 138 */       return false;
/*     */     }
/* 140 */     if (args.indexOf("all") != -1) {
/* 141 */       return true;
/*     */     }
/* 143 */     return args.indexOf(paramString) != -1;
/*     */   }
/*     */ 
/*     */   public void println(String paramString)
/*     */   {
/* 154 */     System.err.println(this.prefix + ": " + paramString);
/*     */   }
/*     */ 
/*     */   public void println()
/*     */   {
/* 163 */     System.err.println(this.prefix + ":");
/*     */   }
/*     */ 
/*     */   public static void println(String paramString1, String paramString2)
/*     */   {
/* 172 */     System.err.println(paramString1 + ": " + paramString2);
/*     */   }
/*     */ 
/*     */   public static String toHexString(BigInteger paramBigInteger)
/*     */   {
/* 182 */     String str = paramBigInteger.toString(16);
/* 183 */     StringBuffer localStringBuffer = new StringBuffer(str.length() * 2);
/*     */ 
/* 185 */     if (str.startsWith("-")) {
/* 186 */       localStringBuffer.append("   -");
/* 187 */       str = str.substring(1);
/*     */     } else {
/* 189 */       localStringBuffer.append("    ");
/*     */     }
/* 191 */     if (str.length() % 2 != 0)
/*     */     {
/* 193 */       str = "0" + str;
/*     */     }
/* 195 */     int i = 0;
/* 196 */     while (i < str.length())
/*     */     {
/* 198 */       localStringBuffer.append(str.substring(i, i + 2));
/* 199 */       i += 2;
/* 200 */       if (i != str.length()) {
/* 201 */         if (i % 64 == 0)
/* 202 */           localStringBuffer.append("\n    ");
/* 203 */         else if (i % 8 == 0) {
/* 204 */           localStringBuffer.append(" ");
/*     */         }
/*     */       }
/*     */     }
/* 208 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String marshal(String paramString)
/*     */   {
/* 215 */     if (paramString != null) {
/* 216 */       StringBuffer localStringBuffer1 = new StringBuffer();
/* 217 */       Object localObject = new StringBuffer(paramString);
/*     */ 
/* 223 */       String str1 = "[Pp][Ee][Rr][Mm][Ii][Ss][Ss][Ii][Oo][Nn]=";
/* 224 */       String str2 = "permission=";
/* 225 */       String str3 = str1 + "[a-zA-Z_$][a-zA-Z0-9_$]*([.][a-zA-Z_$][a-zA-Z0-9_$]*)*";
/*     */ 
/* 227 */       Pattern localPattern = Pattern.compile(str3);
/* 228 */       Matcher localMatcher = localPattern.matcher((CharSequence)localObject);
/* 229 */       StringBuffer localStringBuffer2 = new StringBuffer();
/*     */       String str4;
/* 230 */       while (localMatcher.find()) {
/* 231 */         str4 = localMatcher.group();
/* 232 */         localStringBuffer1.append(str4.replaceFirst(str1, str2));
/* 233 */         localStringBuffer1.append("  ");
/*     */ 
/* 236 */         localMatcher.appendReplacement(localStringBuffer2, "");
/*     */       }
/* 238 */       localMatcher.appendTail(localStringBuffer2);
/* 239 */       localObject = localStringBuffer2;
/*     */ 
/* 248 */       str1 = "[Cc][Oo][Dd][Ee][Bb][Aa][Ss][Ee]=";
/* 249 */       str2 = "codebase=";
/* 250 */       str3 = str1 + "[^, ;]*";
/* 251 */       localPattern = Pattern.compile(str3);
/* 252 */       localMatcher = localPattern.matcher((CharSequence)localObject);
/* 253 */       localStringBuffer2 = new StringBuffer();
/* 254 */       while (localMatcher.find()) {
/* 255 */         str4 = localMatcher.group();
/* 256 */         localStringBuffer1.append(str4.replaceFirst(str1, str2));
/* 257 */         localStringBuffer1.append("  ");
/*     */ 
/* 260 */         localMatcher.appendReplacement(localStringBuffer2, "");
/*     */       }
/* 262 */       localMatcher.appendTail(localStringBuffer2);
/* 263 */       localObject = localStringBuffer2;
/*     */ 
/* 266 */       localStringBuffer1.append(((StringBuffer)localObject).toString().toLowerCase(Locale.ENGLISH));
/*     */ 
/* 268 */       return localStringBuffer1.toString();
/*     */     }
/*     */ 
/* 271 */     return null;
/*     */   }
/*     */ 
/*     */   public static String toString(byte[] paramArrayOfByte)
/*     */   {
/* 277 */     if (paramArrayOfByte == null) {
/* 278 */       return "(null)";
/*     */     }
/* 280 */     StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 3);
/* 281 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 282 */       int j = paramArrayOfByte[i] & 0xFF;
/* 283 */       if (i != 0) {
/* 284 */         localStringBuilder.append(':');
/*     */       }
/* 286 */       localStringBuilder.append(hexDigits[(j >>> 4)]);
/* 287 */       localStringBuilder.append(hexDigits[(j & 0xF)]);
/*     */     }
/* 289 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  49 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.security.auth.debug"));
/*     */ 
/*  53 */     if (args == null) {
/*  54 */       args = str;
/*     */     }
/*  56 */     else if (str != null) {
/*  57 */       args = args + "," + str;
/*     */     }
/*     */ 
/*  60 */     if (args != null) {
/*  61 */       args = marshal(args);
/*  62 */       if (args.equals("help"))
/*  63 */         Help();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.Debug
 * JD-Core Version:    0.6.2
 */