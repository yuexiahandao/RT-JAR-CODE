/*     */ package sun.security.krb5.internal.tools;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Date;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.RealmException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ import sun.security.krb5.internal.TicketFlags;
/*     */ import sun.security.krb5.internal.ccache.CredentialsCache;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.krb5.internal.ktab.KeyTab;
/*     */ import sun.security.krb5.internal.ktab.KeyTabEntry;
/*     */ 
/*     */ public class Klist
/*     */ {
/*     */   Object target;
/*  51 */   char[] options = new char[4];
/*     */   String name;
/*     */   char action;
/*  55 */   private static boolean DEBUG = Krb5.DEBUG;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  76 */     Klist localKlist = new Klist();
/*  77 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
/*  78 */       localKlist.action = 'c';
/*     */     else {
/*  80 */       localKlist.processArgs(paramArrayOfString);
/*     */     }
/*  82 */     switch (localKlist.action) {
/*     */     case 'c':
/*  84 */       if (localKlist.name == null) {
/*  85 */         localKlist.target = CredentialsCache.getInstance();
/*  86 */         localKlist.name = CredentialsCache.cacheName();
/*     */       } else {
/*  88 */         localKlist.target = CredentialsCache.getInstance(localKlist.name);
/*     */       }
/*  90 */       if (localKlist.target != null) {
/*  91 */         localKlist.displayCache();
/*     */       } else {
/*  93 */         localKlist.displayMessage("Credentials cache");
/*  94 */         System.exit(-1);
/*     */       }
/*  96 */       break;
/*     */     case 'k':
/*  98 */       KeyTab localKeyTab = KeyTab.getInstance(localKlist.name);
/*  99 */       if (localKeyTab.isMissing()) {
/* 100 */         System.out.println("KeyTab " + localKlist.name + " not found.");
/* 101 */         System.exit(-1);
/* 102 */       } else if (!localKeyTab.isValid()) {
/* 103 */         System.out.println("KeyTab " + localKlist.name + " format not supported.");
/*     */ 
/* 105 */         System.exit(-1);
/*     */       }
/* 107 */       localKlist.target = localKeyTab;
/* 108 */       localKlist.name = localKeyTab.tabName();
/* 109 */       localKlist.displayTab();
/* 110 */       break;
/*     */     default:
/* 112 */       if (localKlist.name != null) {
/* 113 */         localKlist.printHelp();
/* 114 */         System.exit(-1);
/*     */       } else {
/* 116 */         localKlist.target = CredentialsCache.getInstance();
/* 117 */         localKlist.name = CredentialsCache.cacheName();
/* 118 */         if (localKlist.target != null) {
/* 119 */           localKlist.displayCache();
/*     */         } else {
/* 121 */           localKlist.displayMessage("Credentials cache");
/* 122 */           System.exit(-1);
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   void processArgs(String[] paramArrayOfString)
/*     */   {
/* 133 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/*     */       Character localCharacter;
/* 134 */       if ((paramArrayOfString[i].length() >= 2) && (paramArrayOfString[i].startsWith("-")))
/* 135 */         localCharacter = new Character(paramArrayOfString[i].charAt(1));
/* 136 */       switch (localCharacter.charValue()) {
/*     */       case 'c':
/* 138 */         this.action = 'c';
/* 139 */         break;
/*     */       case 'k':
/* 141 */         this.action = 'k';
/* 142 */         break;
/*     */       case 'a':
/* 144 */         this.options[2] = 'a';
/* 145 */         break;
/*     */       case 'n':
/* 147 */         this.options[3] = 'n';
/* 148 */         break;
/*     */       case 'f':
/* 150 */         this.options[1] = 'f';
/* 151 */         break;
/*     */       case 'e':
/* 153 */         this.options[0] = 'e';
/* 154 */         break;
/*     */       case 'K':
/* 156 */         this.options[1] = 'K';
/* 157 */         break;
/*     */       case 't':
/* 159 */         this.options[2] = 't';
/* 160 */         break;
/*     */       default:
/* 162 */         printHelp();
/* 163 */         System.exit(-1); continue;
/*     */ 
/* 167 */         if ((!paramArrayOfString[i].startsWith("-")) && (i == paramArrayOfString.length - 1))
/*     */         {
/* 169 */           this.name = paramArrayOfString[i];
/* 170 */           localCharacter = null;
/*     */         } else {
/* 172 */           printHelp();
/* 173 */           System.exit(-1);
/*     */         }break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void displayTab() {
/* 180 */     KeyTab localKeyTab = (KeyTab)this.target;
/* 181 */     KeyTabEntry[] arrayOfKeyTabEntry = localKeyTab.getEntries();
/* 182 */     if (arrayOfKeyTabEntry.length == 0) {
/* 183 */       System.out.println("\nKey tab: " + this.name + ", " + " 0 entries found.\n");
/*     */     }
/*     */     else {
/* 186 */       if (arrayOfKeyTabEntry.length == 1) {
/* 187 */         System.out.println("\nKey tab: " + this.name + ", " + arrayOfKeyTabEntry.length + " entry found.\n");
/*     */       }
/*     */       else {
/* 190 */         System.out.println("\nKey tab: " + this.name + ", " + arrayOfKeyTabEntry.length + " entries found.\n");
/*     */       }
/* 192 */       for (int i = 0; i < arrayOfKeyTabEntry.length; i++) {
/* 193 */         System.out.println("[" + (i + 1) + "] " + "Service principal: " + arrayOfKeyTabEntry[i].getService().toString());
/*     */ 
/* 196 */         System.out.println("\t KVNO: " + arrayOfKeyTabEntry[i].getKey().getKeyVersionNumber());
/*     */         EncryptionKey localEncryptionKey;
/* 198 */         if (this.options[0] == 'e') {
/* 199 */           localEncryptionKey = arrayOfKeyTabEntry[i].getKey();
/* 200 */           System.out.println("\t Key type: " + localEncryptionKey.getEType());
/*     */         }
/*     */ 
/* 203 */         if (this.options[1] == 'K') {
/* 204 */           localEncryptionKey = arrayOfKeyTabEntry[i].getKey();
/* 205 */           System.out.println("\t Key: " + arrayOfKeyTabEntry[i].getKeyString());
/*     */         }
/*     */ 
/* 208 */         if (this.options[2] == 't')
/* 209 */           System.out.println("\t Time stamp: " + reformat(arrayOfKeyTabEntry[i].getTimeStamp().toDate().toString()));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void displayCache()
/*     */   {
/* 217 */     CredentialsCache localCredentialsCache = (CredentialsCache)this.target;
/* 218 */     sun.security.krb5.internal.ccache.Credentials[] arrayOfCredentials = localCredentialsCache.getCredsList();
/*     */ 
/* 220 */     if (arrayOfCredentials == null) {
/* 221 */       System.out.println("No credentials available in the cache " + this.name);
/*     */ 
/* 223 */       System.exit(-1);
/*     */     }
/* 225 */     System.out.println("\nCredentials cache: " + this.name);
/* 226 */     String str1 = localCredentialsCache.getPrimaryPrincipal().toString();
/* 227 */     int i = arrayOfCredentials.length;
/*     */ 
/* 229 */     if (i == 1) {
/* 230 */       System.out.println("\nDefault principal: " + str1 + ", " + arrayOfCredentials.length + " entry found.\n");
/*     */     }
/*     */     else
/*     */     {
/* 234 */       System.out.println("\nDefault principal: " + str1 + ", " + arrayOfCredentials.length + " entries found.\n");
/*     */     }
/*     */ 
/* 237 */     String str2 = null;
/* 238 */     String str3 = null;
/* 239 */     String str4 = null;
/* 240 */     String str5 = null;
/* 241 */     if (arrayOfCredentials != null) {
/* 242 */       for (int j = 0; j < arrayOfCredentials.length; j++)
/*     */         try {
/* 244 */           str2 = reformat(arrayOfCredentials[j].getAuthTime().toDate().toString());
/*     */ 
/* 246 */           str3 = reformat(arrayOfCredentials[j].getEndTime().toDate().toString());
/*     */ 
/* 248 */           str4 = arrayOfCredentials[j].getServicePrincipal().toString();
/*     */ 
/* 250 */           System.out.println("[" + (j + 1) + "] " + " Service Principal:  " + str4);
/*     */ 
/* 253 */           System.out.println("     Valid starting:  " + str2);
/* 254 */           System.out.println("     Expires:         " + str3);
/* 255 */           if (this.options[0] == 'e') {
/* 256 */             str5 = EType.toString(arrayOfCredentials[j].getEType());
/* 257 */             System.out.println("     Encryption type: " + str5);
/*     */           }
/* 259 */           if (this.options[1] == 'f') {
/* 260 */             System.out.println("     Flags:           " + arrayOfCredentials[j].getTicketFlags().toString());
/*     */           }
/*     */ 
/* 263 */           if (this.options[2] == 'a') {
/* 264 */             int k = 1;
/* 265 */             InetAddress[] arrayOfInetAddress1 = arrayOfCredentials[j].setKrbCreds().getClientAddresses();
/*     */ 
/* 267 */             if (arrayOfInetAddress1 != null)
/* 268 */               for (InetAddress localInetAddress : arrayOfInetAddress1)
/*     */               {
/*     */                 String str6;
/* 270 */                 if (this.options[3] == 'n')
/* 271 */                   str6 = localInetAddress.getHostAddress();
/*     */                 else {
/* 273 */                   str6 = localInetAddress.getCanonicalHostName();
/*     */                 }
/* 275 */                 System.out.println("     " + (k != 0 ? "Addresses:" : "          ") + "       " + str6);
/*     */ 
/* 278 */                 k = 0;
/*     */               }
/*     */             else
/* 281 */               System.out.println("     [No host addresses info]");
/*     */           }
/*     */         }
/*     */         catch (RealmException localRealmException) {
/* 285 */           System.out.println("Error reading principal from the entry.");
/*     */ 
/* 287 */           if (DEBUG) {
/* 288 */             localRealmException.printStackTrace();
/*     */           }
/* 290 */           System.exit(-1);
/*     */         }
/*     */     }
/*     */     else
/* 294 */       System.out.println("\nNo entries found.");
/*     */   }
/*     */ 
/*     */   void displayMessage(String paramString)
/*     */   {
/* 299 */     if (this.name == null)
/* 300 */       System.out.println("Default " + paramString + " not found.");
/*     */     else
/* 302 */       System.out.println(paramString + " " + this.name + " not found.");
/*     */   }
/*     */ 
/*     */   String reformat(String paramString)
/*     */   {
/* 316 */     return paramString.substring(4, 7) + " " + paramString.substring(8, 10) + ", " + paramString.substring(24) + " " + paramString.substring(11, 16);
/*     */   }
/*     */ 
/*     */   void printHelp()
/*     */   {
/* 324 */     System.out.println("\nUsage: klist [[-c] [-f] [-e] [-a [-n]]] [-k [-t] [-K]] [name]");
/*     */ 
/* 326 */     System.out.println("   name\t name of credentials cache or  keytab with the prefix. File-based cache or keytab's prefix is FILE:.");
/*     */ 
/* 329 */     System.out.println("   -c specifes that credential cache is to be listed");
/*     */ 
/* 331 */     System.out.println("   -k specifies that key tab is to be listed");
/* 332 */     System.out.println("   options for credentials caches:");
/* 333 */     System.out.println("\t-f \t shows credentials flags");
/* 334 */     System.out.println("\t-e \t shows the encryption type");
/* 335 */     System.out.println("\t-a \t shows addresses");
/* 336 */     System.out.println("\t  -n \t   do not reverse-resolve addresses");
/* 337 */     System.out.println("   options for keytabs:");
/* 338 */     System.out.println("\t-t \t shows keytab entry timestamps");
/* 339 */     System.out.println("\t-K \t shows keytab entry key value");
/* 340 */     System.out.println("\t-e \t shows keytab entry key type");
/* 341 */     System.out.println("\nUsage: java sun.security.krb5.tools.Klist -help for help.");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.tools.Klist
 * JD-Core Version:    0.6.2
 */