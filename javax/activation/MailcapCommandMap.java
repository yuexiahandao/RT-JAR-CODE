/*     */ package javax.activation;
/*     */ 
/*     */ import com.sun.activation.registries.LogSupport;
/*     */ import com.sun.activation.registries.MailcapFile;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MailcapCommandMap extends CommandMap
/*     */ {
/*     */   private MailcapFile[] DB;
/*     */   private static final int PROG = 0;
/*     */ 
/*     */   public MailcapCommandMap()
/*     */   {
/* 132 */     List dbv = new ArrayList(5);
/* 133 */     MailcapFile mf = null;
/* 134 */     dbv.add(null);
/*     */ 
/* 136 */     LogSupport.log("MailcapCommandMap: load HOME");
/*     */     try {
/* 138 */       String user_home = System.getProperty("user.home");
/*     */ 
/* 140 */       if (user_home != null) {
/* 141 */         String path = user_home + File.separator + ".mailcap";
/* 142 */         mf = loadFile(path);
/* 143 */         if (mf != null)
/* 144 */           dbv.add(mf);
/*     */       }
/*     */     } catch (SecurityException ex) {
/*     */     }
/* 148 */     LogSupport.log("MailcapCommandMap: load SYS");
/*     */     try
/*     */     {
/* 151 */       String system_mailcap = System.getProperty("java.home") + File.separator + "lib" + File.separator + "mailcap";
/*     */ 
/* 153 */       mf = loadFile(system_mailcap);
/* 154 */       if (mf != null)
/* 155 */         dbv.add(mf);
/*     */     } catch (SecurityException ex) {
/*     */     }
/* 158 */     LogSupport.log("MailcapCommandMap: load JAR");
/*     */ 
/* 160 */     loadAllResources(dbv, "META-INF/mailcap");
/*     */ 
/* 162 */     LogSupport.log("MailcapCommandMap: load DEF");
/* 163 */     mf = loadResource("/META-INF/mailcap.default");
/*     */ 
/* 165 */     if (mf != null) {
/* 166 */       dbv.add(mf);
/*     */     }
/* 168 */     this.DB = new MailcapFile[dbv.size()];
/* 169 */     this.DB = ((MailcapFile[])dbv.toArray(this.DB));
/*     */   }
/*     */ 
/*     */   private MailcapFile loadResource(String name)
/*     */   {
/* 176 */     InputStream clis = null;
/*     */     try {
/* 178 */       clis = SecuritySupport.getResourceAsStream(getClass(), name);
/* 179 */       if (clis != null) {
/* 180 */         MailcapFile mf = new MailcapFile(clis);
/* 181 */         if (LogSupport.isLoggable()) {
/* 182 */           LogSupport.log("MailcapCommandMap: successfully loaded mailcap file: " + name);
/*     */         }
/* 184 */         return mf;
/*     */       }
/* 186 */       if (LogSupport.isLoggable())
/* 187 */         LogSupport.log("MailcapCommandMap: not loading mailcap file: " + name);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 191 */       if (LogSupport.isLoggable())
/* 192 */         LogSupport.log("MailcapCommandMap: can't load " + name, e);
/*     */     } catch (SecurityException sex) {
/* 194 */       if (LogSupport.isLoggable())
/* 195 */         LogSupport.log("MailcapCommandMap: can't load " + name, sex);
/*     */     } finally {
/*     */       try {
/* 198 */         if (clis != null)
/* 199 */           clis.close(); 
/*     */       } catch (IOException ex) {  }
/*     */ 
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   private void loadAllResources(List v, String name)
/*     */   {
/* 209 */     boolean anyLoaded = false;
/*     */     try
/*     */     {
/* 212 */       ClassLoader cld = null;
/*     */ 
/* 214 */       cld = SecuritySupport.getContextClassLoader();
/* 215 */       if (cld == null)
/* 216 */         cld = getClass().getClassLoader();
/*     */       URL[] urls;
/*     */       URL[] urls;
/* 217 */       if (cld != null)
/* 218 */         urls = SecuritySupport.getResources(cld, name);
/*     */       else
/* 220 */         urls = SecuritySupport.getSystemResources(name);
/* 221 */       if (urls != null) {
/* 222 */         if (LogSupport.isLoggable())
/* 223 */           LogSupport.log("MailcapCommandMap: getResources");
/* 224 */         for (int i = 0; i < urls.length; i++) {
/* 225 */           URL url = urls[i];
/* 226 */           InputStream clis = null;
/* 227 */           if (LogSupport.isLoggable())
/* 228 */             LogSupport.log("MailcapCommandMap: URL " + url);
/*     */           try {
/* 230 */             clis = SecuritySupport.openStream(url);
/* 231 */             if (clis != null) {
/* 232 */               v.add(new MailcapFile(clis));
/* 233 */               anyLoaded = true;
/* 234 */               if (LogSupport.isLoggable()) {
/* 235 */                 LogSupport.log("MailcapCommandMap: successfully loaded mailcap file from URL: " + url);
/*     */               }
/*     */ 
/*     */             }
/* 240 */             else if (LogSupport.isLoggable()) {
/* 241 */               LogSupport.log("MailcapCommandMap: not loading mailcap file from URL: " + url);
/*     */             }
/*     */           }
/*     */           catch (IOException ioex)
/*     */           {
/* 246 */             if (LogSupport.isLoggable())
/* 247 */               LogSupport.log("MailcapCommandMap: can't load " + url, ioex);
/*     */           }
/*     */           catch (SecurityException sex) {
/* 250 */             if (LogSupport.isLoggable())
/* 251 */               LogSupport.log("MailcapCommandMap: can't load " + url, sex);
/*     */           }
/*     */           finally {
/*     */             try {
/* 255 */               if (clis != null)
/* 256 */                 clis.close(); 
/*     */             } catch (IOException cex) {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception ex) { if (LogSupport.isLoggable()) {
/* 263 */         LogSupport.log("MailcapCommandMap: can't load " + name, ex);
/*     */       }
/*     */     }
/*     */ 
/* 267 */     if (!anyLoaded) {
/* 268 */       if (LogSupport.isLoggable())
/* 269 */         LogSupport.log("MailcapCommandMap: !anyLoaded");
/* 270 */       MailcapFile mf = loadResource("/" + name);
/* 271 */       if (mf != null)
/* 272 */         v.add(mf);
/*     */     }
/*     */   }
/*     */ 
/*     */   private MailcapFile loadFile(String name)
/*     */   {
/* 280 */     MailcapFile mtf = null;
/*     */     try
/*     */     {
/* 283 */       mtf = new MailcapFile(name);
/*     */     }
/*     */     catch (IOException e) {
/*     */     }
/* 287 */     return mtf;
/*     */   }
/*     */ 
/*     */   public MailcapCommandMap(String fileName)
/*     */     throws IOException
/*     */   {
/* 298 */     this();
/*     */ 
/* 300 */     if (LogSupport.isLoggable())
/* 301 */       LogSupport.log("MailcapCommandMap: load PROG from " + fileName);
/* 302 */     if (this.DB[0] == null)
/* 303 */       this.DB[0] = new MailcapFile(fileName);
/*     */   }
/*     */ 
/*     */   public MailcapCommandMap(InputStream is)
/*     */   {
/* 315 */     this();
/*     */ 
/* 317 */     LogSupport.log("MailcapCommandMap: load PROG");
/* 318 */     if (this.DB[0] == null)
/*     */       try {
/* 320 */         this.DB[0] = new MailcapFile(is);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized CommandInfo[] getPreferredCommands(String mimeType)
/*     */   {
/* 341 */     List cmdList = new ArrayList();
/* 342 */     if (mimeType != null) {
/* 343 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 345 */     for (int i = 0; i < this.DB.length; i++) {
/* 346 */       if (this.DB[i] != null)
/*     */       {
/* 348 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 349 */         if (cmdMap != null) {
/* 350 */           appendPrefCmdsToList(cmdMap, cmdList);
/*     */         }
/*     */       }
/*     */     }
/* 354 */     for (int i = 0; i < this.DB.length; i++) {
/* 355 */       if (this.DB[i] != null)
/*     */       {
/* 357 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 358 */         if (cmdMap != null)
/* 359 */           appendPrefCmdsToList(cmdMap, cmdList);
/*     */       }
/*     */     }
/* 362 */     CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
/* 363 */     cmdInfos = (CommandInfo[])cmdList.toArray(cmdInfos);
/*     */ 
/* 365 */     return cmdInfos;
/*     */   }
/*     */ 
/*     */   private void appendPrefCmdsToList(Map cmdHash, List cmdList)
/*     */   {
/* 372 */     Iterator verb_enum = cmdHash.keySet().iterator();
/*     */ 
/* 374 */     while (verb_enum.hasNext()) {
/* 375 */       String verb = (String)verb_enum.next();
/* 376 */       if (!checkForVerb(cmdList, verb)) {
/* 377 */         List cmdList2 = (List)cmdHash.get(verb);
/* 378 */         String className = (String)cmdList2.get(0);
/* 379 */         cmdList.add(new CommandInfo(verb, className));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean checkForVerb(List cmdList, String verb)
/*     */   {
/* 389 */     Iterator ee = cmdList.iterator();
/* 390 */     while (ee.hasNext()) {
/* 391 */       String enum_verb = ((CommandInfo)ee.next()).getCommandName();
/*     */ 
/* 393 */       if (enum_verb.equals(verb))
/* 394 */         return true;
/*     */     }
/* 396 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized CommandInfo[] getAllCommands(String mimeType)
/*     */   {
/* 407 */     List cmdList = new ArrayList();
/* 408 */     if (mimeType != null) {
/* 409 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 411 */     for (int i = 0; i < this.DB.length; i++) {
/* 412 */       if (this.DB[i] != null)
/*     */       {
/* 414 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 415 */         if (cmdMap != null) {
/* 416 */           appendCmdsToList(cmdMap, cmdList);
/*     */         }
/*     */       }
/*     */     }
/* 420 */     for (int i = 0; i < this.DB.length; i++) {
/* 421 */       if (this.DB[i] != null)
/*     */       {
/* 423 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 424 */         if (cmdMap != null)
/* 425 */           appendCmdsToList(cmdMap, cmdList);
/*     */       }
/*     */     }
/* 428 */     CommandInfo[] cmdInfos = new CommandInfo[cmdList.size()];
/* 429 */     cmdInfos = (CommandInfo[])cmdList.toArray(cmdInfos);
/*     */ 
/* 431 */     return cmdInfos;
/*     */   }
/*     */ 
/*     */   private void appendCmdsToList(Map typeHash, List cmdList)
/*     */   {
/* 438 */     Iterator verb_enum = typeHash.keySet().iterator();
/*     */ 
/* 440 */     while (verb_enum.hasNext()) {
/* 441 */       String verb = (String)verb_enum.next();
/* 442 */       List cmdList2 = (List)typeHash.get(verb);
/* 443 */       Iterator cmd_enum = cmdList2.iterator();
/*     */ 
/* 445 */       while (cmd_enum.hasNext()) {
/* 446 */         String cmd = (String)cmd_enum.next();
/* 447 */         cmdList.add(new CommandInfo(verb, cmd));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized CommandInfo getCommand(String mimeType, String cmdName)
/*     */   {
/* 462 */     if (mimeType != null) {
/* 463 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 465 */     for (int i = 0; i < this.DB.length; i++) {
/* 466 */       if (this.DB[i] != null)
/*     */       {
/* 468 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 469 */         if (cmdMap != null)
/*     */         {
/* 471 */           List v = (List)cmdMap.get(cmdName);
/* 472 */           if (v != null) {
/* 473 */             String cmdClassName = (String)v.get(0);
/*     */ 
/* 475 */             if (cmdClassName != null) {
/* 476 */               return new CommandInfo(cmdName, cmdClassName);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 482 */     for (int i = 0; i < this.DB.length; i++)
/* 483 */       if (this.DB[i] != null)
/*     */       {
/* 485 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 486 */         if (cmdMap != null)
/*     */         {
/* 488 */           List v = (List)cmdMap.get(cmdName);
/* 489 */           if (v != null) {
/* 490 */             String cmdClassName = (String)v.get(0);
/*     */ 
/* 492 */             if (cmdClassName != null)
/* 493 */               return new CommandInfo(cmdName, cmdClassName);
/*     */           }
/*     */         }
/*     */       }
/* 497 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized void addMailcap(String mail_cap)
/*     */   {
/* 511 */     LogSupport.log("MailcapCommandMap: add to PROG");
/* 512 */     if (this.DB[0] == null) {
/* 513 */       this.DB[0] = new MailcapFile();
/*     */     }
/* 515 */     this.DB[0].appendToMailcap(mail_cap);
/*     */   }
/*     */ 
/*     */   public synchronized DataContentHandler createDataContentHandler(String mimeType)
/*     */   {
/* 526 */     if (LogSupport.isLoggable()) {
/* 527 */       LogSupport.log("MailcapCommandMap: createDataContentHandler for " + mimeType);
/*     */     }
/* 529 */     if (mimeType != null) {
/* 530 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 532 */     for (int i = 0; i < this.DB.length; i++) {
/* 533 */       if (this.DB[i] != null)
/*     */       {
/* 535 */         if (LogSupport.isLoggable())
/* 536 */           LogSupport.log("  search DB #" + i);
/* 537 */         Map cmdMap = this.DB[i].getMailcapList(mimeType);
/* 538 */         if (cmdMap != null) {
/* 539 */           List v = (List)cmdMap.get("content-handler");
/* 540 */           if (v != null) {
/* 541 */             String name = (String)v.get(0);
/* 542 */             DataContentHandler dch = getDataContentHandler(name);
/* 543 */             if (dch != null) {
/* 544 */               return dch;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 550 */     for (int i = 0; i < this.DB.length; i++)
/* 551 */       if (this.DB[i] != null)
/*     */       {
/* 553 */         if (LogSupport.isLoggable())
/* 554 */           LogSupport.log("  search fallback DB #" + i);
/* 555 */         Map cmdMap = this.DB[i].getMailcapFallbackList(mimeType);
/* 556 */         if (cmdMap != null) {
/* 557 */           List v = (List)cmdMap.get("content-handler");
/* 558 */           if (v != null) {
/* 559 */             String name = (String)v.get(0);
/* 560 */             DataContentHandler dch = getDataContentHandler(name);
/* 561 */             if (dch != null)
/* 562 */               return dch;
/*     */           }
/*     */         }
/*     */       }
/* 566 */     return null;
/*     */   }
/*     */ 
/*     */   private DataContentHandler getDataContentHandler(String name) {
/* 570 */     if (LogSupport.isLoggable())
/* 571 */       LogSupport.log("    got content-handler");
/* 572 */     if (LogSupport.isLoggable())
/* 573 */       LogSupport.log("      class " + name);
/*     */     try {
/* 575 */       ClassLoader cld = null;
/*     */ 
/* 577 */       cld = SecuritySupport.getContextClassLoader();
/* 578 */       if (cld == null)
/* 579 */         cld = getClass().getClassLoader();
/* 580 */       Class cl = null;
/*     */       try {
/* 582 */         cl = cld.loadClass(name);
/*     */       }
/*     */       catch (Exception ex) {
/* 585 */         cl = Class.forName(name);
/*     */       }
/* 587 */       if (cl != null)
/* 588 */         return (DataContentHandler)cl.newInstance();
/*     */     } catch (IllegalAccessException e) {
/* 590 */       if (LogSupport.isLoggable())
/* 591 */         LogSupport.log("Can't load DCH " + name, e);
/*     */     } catch (ClassNotFoundException e) {
/* 593 */       if (LogSupport.isLoggable())
/* 594 */         LogSupport.log("Can't load DCH " + name, e);
/*     */     } catch (InstantiationException e) {
/* 596 */       if (LogSupport.isLoggable())
/* 597 */         LogSupport.log("Can't load DCH " + name, e);
/*     */     }
/* 599 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized String[] getMimeTypes()
/*     */   {
/* 609 */     List mtList = new ArrayList();
/*     */ 
/* 611 */     for (int i = 0; i < this.DB.length; i++) {
/* 612 */       if (this.DB[i] != null)
/*     */       {
/* 614 */         String[] ts = this.DB[i].getMimeTypes();
/* 615 */         if (ts != null) {
/* 616 */           for (int j = 0; j < ts.length; j++)
/*     */           {
/* 618 */             if (!mtList.contains(ts[j]))
/* 619 */               mtList.add(ts[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 624 */     String[] mts = new String[mtList.size()];
/* 625 */     mts = (String[])mtList.toArray(mts);
/*     */ 
/* 627 */     return mts;
/*     */   }
/*     */ 
/*     */   public synchronized String[] getNativeCommands(String mimeType)
/*     */   {
/* 645 */     List cmdList = new ArrayList();
/* 646 */     if (mimeType != null) {
/* 647 */       mimeType = mimeType.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 649 */     for (int i = 0; i < this.DB.length; i++) {
/* 650 */       if (this.DB[i] != null)
/*     */       {
/* 652 */         String[] cmds = this.DB[i].getNativeCommands(mimeType);
/* 653 */         if (cmds != null) {
/* 654 */           for (int j = 0; j < cmds.length; j++)
/*     */           {
/* 656 */             if (!cmdList.contains(cmds[j]))
/* 657 */               cmdList.add(cmds[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 662 */     String[] cmds = new String[cmdList.size()];
/* 663 */     cmds = (String[])cmdList.toArray(cmds);
/*     */ 
/* 665 */     return cmds;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.MailcapCommandMap
 * JD-Core Version:    0.6.2
 */