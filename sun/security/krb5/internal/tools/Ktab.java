/*     */ package sun.security.krb5.internal.tools;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import sun.security.krb5.Config;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.crypto.EType;
/*     */ import sun.security.krb5.internal.ktab.KeyTab;
/*     */ import sun.security.krb5.internal.ktab.KeyTabEntry;
/*     */ 
/*     */ public class Ktab
/*     */ {
/*     */   KeyTab table;
/*     */   char action;
/*     */   String name;
/*     */   String principal;
/*     */   boolean showEType;
/*     */   boolean showTime;
/*  61 */   int etype = -1;
/*  62 */   char[] password = null;
/*     */ 
/*  64 */   boolean forced = false;
/*  65 */   boolean append = false;
/*  66 */   int vDel = -1;
/*  67 */   int vAdd = -1;
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*  74 */     Ktab localKtab = new Ktab();
/*  75 */     if ((paramArrayOfString.length == 1) && (paramArrayOfString[0].equalsIgnoreCase("-help"))) {
/*  76 */       localKtab.printHelp();
/*  77 */       return;
/*  78 */     }if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
/*  79 */       localKtab.action = 'l';
/*     */     else {
/*  81 */       localKtab.processArgs(paramArrayOfString);
/*     */     }
/*  83 */     localKtab.table = KeyTab.getInstance(localKtab.name);
/*  84 */     if ((localKtab.table.isMissing()) && (localKtab.action != 'a')) {
/*  85 */       if (localKtab.name == null)
/*  86 */         System.out.println("No default key table exists.");
/*     */       else {
/*  88 */         System.out.println("Key table " + localKtab.name + " does not exist.");
/*     */       }
/*     */ 
/*  91 */       System.exit(-1);
/*     */     }
/*  93 */     if (!localKtab.table.isValid()) {
/*  94 */       if (localKtab.name == null) {
/*  95 */         System.out.println("The format of the default key table  is incorrect.");
/*     */       }
/*     */       else {
/*  98 */         System.out.println("The format of key table " + localKtab.name + " is incorrect.");
/*     */       }
/*     */ 
/* 101 */       System.exit(-1);
/*     */     }
/* 103 */     switch (localKtab.action) {
/*     */     case 'l':
/* 105 */       localKtab.listKt();
/* 106 */       break;
/*     */     case 'a':
/* 108 */       localKtab.addEntry();
/* 109 */       break;
/*     */     case 'd':
/* 111 */       localKtab.deleteEntry();
/* 112 */       break;
/*     */     default:
/* 114 */       localKtab.error(new String[] { "A command must be provided" });
/*     */     }
/*     */   }
/*     */ 
/*     */   void processArgs(String[] paramArrayOfString)
/*     */   {
/* 139 */     int i = 0;
/* 140 */     for (int j = 0; j < paramArrayOfString.length; j++)
/* 141 */       if (paramArrayOfString[j].startsWith("-")) {
/* 142 */         switch (paramArrayOfString[j].toLowerCase(Locale.US))
/*     */         {
/*     */         case "-l":
/* 146 */           this.action = 'l';
/* 147 */           break;
/*     */         case "-a":
/* 149 */           this.action = 'a';
/* 150 */           j++; if ((j >= paramArrayOfString.length) || (paramArrayOfString[j].startsWith("-"))) {
/* 151 */             error(new String[] { "A principal name must be specified after -a" });
/*     */           }
/* 153 */           this.principal = paramArrayOfString[j];
/* 154 */           break;
/*     */         case "-d":
/* 156 */           this.action = 'd';
/* 157 */           j++; if ((j >= paramArrayOfString.length) || (paramArrayOfString[j].startsWith("-"))) {
/* 158 */             error(new String[] { "A principal name must be specified after -d" });
/*     */           }
/* 160 */           this.principal = paramArrayOfString[j];
/* 161 */           break;
/*     */         case "-e":
/* 165 */           if (this.action == 'l') {
/* 166 */             this.showEType = true;
/* 167 */           } else if (this.action == 'd') {
/* 168 */             j++; if ((j >= paramArrayOfString.length) || (paramArrayOfString[j].startsWith("-")))
/* 169 */               error(new String[] { "An etype must be specified after -e" });
/*     */             try
/*     */             {
/* 172 */               this.etype = Integer.parseInt(paramArrayOfString[j]);
/* 173 */               if (this.etype <= 0)
/* 174 */                 throw new NumberFormatException();
/*     */             }
/*     */             catch (NumberFormatException localNumberFormatException1) {
/* 177 */               error(new String[] { paramArrayOfString[j] + " is not a valid etype" });
/*     */             }
/*     */           } else {
/* 180 */             error(new String[] { paramArrayOfString[j] + " is not valid after -" + this.action });
/*     */           }
/* 182 */           break;
/*     */         case "-n":
/* 184 */           j++; if ((j >= paramArrayOfString.length) || (paramArrayOfString[j].startsWith("-")))
/* 185 */             error(new String[] { "A KVNO must be specified after -n" });
/*     */           try
/*     */           {
/* 188 */             this.vAdd = Integer.parseInt(paramArrayOfString[j]);
/* 189 */             if (this.vAdd < 0)
/* 190 */               throw new NumberFormatException();
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException2) {
/* 193 */             error(new String[] { paramArrayOfString[j] + " is not a valid KVNO" });
/*     */           }
/*     */ 
/*     */         case "-k":
/* 197 */           j++; if ((j >= paramArrayOfString.length) || (paramArrayOfString[j].startsWith("-"))) {
/* 198 */             error(new String[] { "A keytab name must be specified after -k" });
/*     */           }
/* 200 */           if ((paramArrayOfString[j].length() >= 5) && (paramArrayOfString[j].substring(0, 5).equalsIgnoreCase("FILE:")))
/*     */           {
/* 202 */             this.name = paramArrayOfString[j].substring(5);
/*     */           }
/* 204 */           else this.name = paramArrayOfString[j];
/*     */ 
/* 206 */           break;
/*     */         case "-t":
/* 208 */           this.showTime = true;
/* 209 */           break;
/*     */         case "-f":
/* 211 */           this.forced = true;
/* 212 */           break;
/*     */         case "-append":
/* 214 */           this.append = true;
/* 215 */           break;
/*     */         default:
/* 217 */           error(new String[] { "Unknown command: " + paramArrayOfString[j] });
/*     */         }
/*     */       }
/*     */       else {
/* 221 */         if (i != 0) {
/* 222 */           error(new String[] { "Useless extra argument " + paramArrayOfString[j] });
/*     */         }
/* 224 */         if (this.action == 'a')
/* 225 */           this.password = paramArrayOfString[j].toCharArray();
/* 226 */         else if (this.action == 'd') {
/* 227 */           switch (paramArrayOfString[j]) { case "all":
/* 228 */             this.vDel = -1; break;
/*     */           case "old":
/* 229 */             this.vDel = -2; break;
/*     */           default:
/*     */             try {
/* 232 */               this.vDel = Integer.parseInt(paramArrayOfString[j]);
/* 233 */               if (this.vDel < 0)
/* 234 */                 throw new NumberFormatException();
/*     */             }
/*     */             catch (NumberFormatException localNumberFormatException3) {
/* 237 */               error(new String[] { paramArrayOfString[j] + " is not a valid KVNO" });
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 242 */           error(new String[] { "Useless extra argument " + paramArrayOfString[j] });
/*     */         }
/* 244 */         i = 1;
/*     */       }
/*     */   }
/*     */ 
/*     */   void addEntry()
/*     */   {
/* 255 */     PrincipalName localPrincipalName = null;
/*     */     try {
/* 257 */       localPrincipalName = new PrincipalName(this.principal);
/* 258 */       if (localPrincipalName.getRealm() == null)
/* 259 */         localPrincipalName.setRealm(Config.getInstance().getDefaultRealm());
/*     */     }
/*     */     catch (KrbException localKrbException1) {
/* 262 */       System.err.println("Failed to add " + this.principal + " to keytab.");
/*     */ 
/* 264 */       localKrbException1.printStackTrace();
/* 265 */       System.exit(-1);
/*     */     }
/* 267 */     if (this.password == null) {
/*     */       try {
/* 269 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*     */ 
/* 271 */         System.out.print("Password for " + localPrincipalName.toString() + ":");
/* 272 */         System.out.flush();
/* 273 */         this.password = localBufferedReader.readLine().toCharArray();
/*     */       } catch (IOException localIOException1) {
/* 275 */         System.err.println("Failed to read the password.");
/* 276 */         localIOException1.printStackTrace();
/* 277 */         System.exit(-1);
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 283 */       this.table.addEntry(localPrincipalName, this.password, this.vAdd, this.append);
/* 284 */       Arrays.fill(this.password, '0');
/*     */ 
/* 286 */       this.table.save();
/* 287 */       System.out.println("Done!");
/* 288 */       System.out.println("Service key for " + this.principal + " is saved in " + this.table.tabName());
/*     */     }
/*     */     catch (KrbException localKrbException2)
/*     */     {
/* 292 */       System.err.println("Failed to add " + this.principal + " to keytab.");
/* 293 */       localKrbException2.printStackTrace();
/* 294 */       System.exit(-1);
/*     */     } catch (IOException localIOException2) {
/* 296 */       System.err.println("Failed to save new entry.");
/* 297 */       localIOException2.printStackTrace();
/* 298 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */ 
/*     */   void listKt()
/*     */   {
/* 306 */     System.out.println("Keytab name: " + this.table.tabName());
/* 307 */     KeyTabEntry[] arrayOfKeyTabEntry = this.table.getEntries();
/* 308 */     if ((arrayOfKeyTabEntry != null) && (arrayOfKeyTabEntry.length > 0)) {
/* 309 */       String[][] arrayOfString = new String[arrayOfKeyTabEntry.length + 1][this.showTime ? 3 : 2];
/* 310 */       int i = 0;
/* 311 */       arrayOfString[0][(i++)] = "KVNO";
/* 312 */       if (this.showTime) arrayOfString[0][(i++)] = "Timestamp";
/* 313 */       arrayOfString[0][(i++)] = "Principal";
/*     */       int m;
/* 314 */       for (int j = 0; j < arrayOfKeyTabEntry.length; j++) {
/* 315 */         i = 0;
/* 316 */         arrayOfString[(j + 1)][(i++)] = arrayOfKeyTabEntry[j].getKey().getKeyVersionNumber().toString();
/*     */ 
/* 318 */         if (this.showTime) arrayOfString[(j + 1)][(i++)] = DateFormat.getDateTimeInstance(3, 3).format(new Date(arrayOfKeyTabEntry[j].getTimeStamp().getTime()));
/*     */ 
/* 322 */         String str = arrayOfKeyTabEntry[j].getService().toString();
/* 323 */         if (this.showEType) {
/* 324 */           m = arrayOfKeyTabEntry[j].getKey().getEType();
/* 325 */           arrayOfString[(j + 1)][(i++)] = (str + " (" + m + ":" + EType.toString(m) + ")");
/*     */         }
/*     */         else {
/* 328 */           arrayOfString[(j + 1)][(i++)] = str;
/*     */         }
/*     */       }
/* 331 */       int[] arrayOfInt = new int[i];
/* 332 */       for (int k = 0; k < i; k++) {
/* 333 */         for (m = 0; m <= arrayOfKeyTabEntry.length; m++) {
/* 334 */           if (arrayOfString[m][k].length() > arrayOfInt[k]) {
/* 335 */             arrayOfInt[k] = arrayOfString[m][k].length();
/*     */           }
/*     */         }
/* 338 */         if (k != 0) arrayOfInt[k] = (-arrayOfInt[k]);
/*     */       }
/* 340 */       for (k = 0; k < i; k++) {
/* 341 */         System.out.printf("%" + arrayOfInt[k] + "s ", new Object[] { arrayOfString[0][k] });
/*     */       }
/* 343 */       System.out.println();
/* 344 */       for (k = 0; k < i; k++) {
/* 345 */         for (m = 0; m < Math.abs(arrayOfInt[k]); m++) System.out.print("-");
/* 346 */         System.out.print(" ");
/*     */       }
/* 348 */       System.out.println();
/* 349 */       for (k = 0; k < arrayOfKeyTabEntry.length; k++) {
/* 350 */         for (m = 0; m < i; m++) {
/* 351 */           System.out.printf("%" + arrayOfInt[m] + "s ", new Object[] { arrayOfString[(k + 1)][m] });
/*     */         }
/* 353 */         System.out.println();
/*     */       }
/*     */     } else {
/* 356 */       System.out.println("0 entry.");
/*     */     }
/*     */   }
/*     */ 
/*     */   void deleteEntry()
/*     */   {
/* 364 */     PrincipalName localPrincipalName = null;
/*     */     try {
/* 366 */       localPrincipalName = new PrincipalName(this.principal);
/* 367 */       if (localPrincipalName.getRealm() == null) {
/* 368 */         localPrincipalName.setRealm(Config.getInstance().getDefaultRealm());
/*     */       }
/* 370 */       if (!this.forced)
/*     */       {
/* 372 */         BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*     */ 
/* 374 */         System.out.print("Are you sure you want to delete service key(s) for " + localPrincipalName.toString() + " (" + (this.etype == -1 ? "all etypes" : new StringBuilder().append("etype=").append(this.etype).toString()) + ", " + (this.vDel == -2 ? "old kvno" : this.vDel == -1 ? "all kvno" : new StringBuilder().append("kvno=").append(this.vDel).toString()) + ") in " + this.table.tabName() + "? (Y/[N]): ");
/*     */ 
/* 380 */         System.out.flush();
/* 381 */         String str = localBufferedReader.readLine();
/* 382 */         if ((!str.equalsIgnoreCase("Y")) && (!str.equalsIgnoreCase("Yes")))
/*     */         {
/* 386 */           System.exit(0);
/*     */         }
/*     */       }
/*     */     } catch (KrbException localKrbException) {
/* 390 */       System.err.println("Error occured while deleting the entry. Deletion failed.");
/*     */ 
/* 392 */       localKrbException.printStackTrace();
/* 393 */       System.exit(-1);
/*     */     } catch (IOException localIOException1) {
/* 395 */       System.err.println("Error occured while deleting the entry.  Deletion failed.");
/*     */ 
/* 397 */       localIOException1.printStackTrace();
/* 398 */       System.exit(-1);
/*     */     }
/*     */ 
/* 401 */     int i = this.table.deleteEntries(localPrincipalName, this.etype, this.vDel);
/*     */ 
/* 403 */     if (i == 0) {
/* 404 */       System.err.println("No matched entry in the keytab. Deletion fails.");
/*     */ 
/* 406 */       System.exit(-1);
/*     */     } else {
/*     */       try {
/* 409 */         this.table.save();
/*     */       } catch (IOException localIOException2) {
/* 411 */         System.err.println("Error occurs while saving the keytab. Deletion fails.");
/*     */ 
/* 413 */         localIOException2.printStackTrace();
/* 414 */         System.exit(-1);
/*     */       }
/* 416 */       System.out.println("Done! " + i + " entries removed.");
/*     */     }
/*     */   }
/*     */ 
/*     */   void error(String[] paramArrayOfString) {
/* 421 */     for (String str : paramArrayOfString) {
/* 422 */       System.out.println("Error: " + str + ".");
/*     */     }
/* 424 */     printHelp();
/* 425 */     System.exit(-1);
/*     */   }
/*     */ 
/*     */   void printHelp()
/*     */   {
/* 431 */     System.out.println("\nUsage: ktab <commands> <options>");
/* 432 */     System.out.println();
/* 433 */     System.out.println("Available commands:");
/* 434 */     System.out.println();
/* 435 */     System.out.println("-l [-e] [-t]\n    list the keytab name and entries. -e with etype, -t with timestamp.");
/*     */ 
/* 437 */     System.out.println("-a <principal name> [<password>] [-n <kvno>] [-append]\n    add new key entries to the keytab for the given principal name with\n    optional <password>. If a <kvno> is specified, new keys' Key Version\n    Numbers equal to the value, otherwise, automatically incrementing\n    the Key Version Numbers. If -append is specified, new keys are\n    appended to the keytab, otherwise, old keys for the\n    same principal are removed.");
/*     */ 
/* 444 */     System.out.println("-d <principal name> [-f] [-e <etype>] [<kvno> | all | old]\n    delete key entries from the keytab for the specified principal. If\n    <kvno> is specified, delete keys whose Key Version Numbers match\n    kvno. If \"all\" is specified, delete all keys. If \"old\" is specified,\n    delete all keys except those with the highest kvno. Default action\n    is \"all\". If <etype> is specified, only keys of this encryption type\n    are deleted. <etype> should be specified as the numberic value etype\n    defined in RFC 3961, section 8. A prompt to confirm the deletion is\n    displayed unless -f is specified.");
/*     */ 
/* 453 */     System.out.println();
/* 454 */     System.out.println("Common option(s):");
/* 455 */     System.out.println();
/* 456 */     System.out.println("-k <keytab name>\n    specify keytab name and path with prefix FILE:");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.tools.Ktab
 * JD-Core Version:    0.6.2
 */