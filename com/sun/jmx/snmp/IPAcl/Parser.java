/*      */ package com.sun.jmx.snmp.IPAcl;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class Parser
/*      */   implements ParserTreeConstants, ParserConstants
/*      */ {
/*   33 */   protected JJTParserState jjtree = new JJTParserState();
/*      */   public ParserTokenManager token_source;
/*      */   ASCII_CharStream jj_input_stream;
/*      */   public Token token;
/*      */   public Token jj_nt;
/*      */   private int jj_ntk;
/*      */   private Token jj_scanpos;
/*      */   private Token jj_lastpos;
/*      */   private int jj_la;
/* 1033 */   public boolean lookingAhead = false;
/*      */   private boolean jj_semLA;
/*      */   private int jj_gen;
/* 1036 */   private final int[] jj_la1 = new int[22];
/* 1037 */   private final int[] jj_la1_0 = { 256, 524288, 1048576, 8192, 0, 393216, 0, -2147483648, 285212672, 0, 0, 0, 0, 8192, 8192, 0, -1862270976, 0, 32768, 8192, 0, -1862270976 };
/* 1038 */   private final int[] jj_la1_1 = { 0, 0, 0, 0, 16, 0, 16, 0, 0, 32, 32, 64, 32, 0, 0, 16, 0, 16, 0, 0, 16, 0 };
/* 1039 */   private final JJCalls[] jj_2_rtns = new JJCalls[3];
/* 1040 */   private boolean jj_rescan = false;
/* 1041 */   private int jj_gc = 0;
/*      */ 
/* 1171 */   private Vector jj_expentries = new Vector();
/*      */   private int[] jj_expentry;
/* 1173 */   private int jj_kind = -1;
/* 1174 */   private int[] jj_lasttokens = new int[100];
/*      */   private int jj_endpos;
/*      */ 
/*      */   public final JDMSecurityDefs SecurityDefs()
/*      */     throws ParseException
/*      */   {
/*   39 */     JDMSecurityDefs localJDMSecurityDefs1 = new JDMSecurityDefs(0);
/*   40 */     int i = 1;
/*   41 */     this.jjtree.openNodeScope(localJDMSecurityDefs1);
/*      */     try {
/*   43 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 8:
/*   45 */         AclBlock();
/*   46 */         break;
/*      */       default:
/*   48 */         this.jj_la1[0] = this.jj_gen;
/*      */       }
/*      */ 
/*   51 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 19:
/*   53 */         TrapBlock();
/*   54 */         break;
/*      */       default:
/*   56 */         this.jj_la1[1] = this.jj_gen;
/*      */       }
/*      */ 
/*   59 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 20:
/*   61 */         InformBlock();
/*   62 */         break;
/*      */       default:
/*   64 */         this.jj_la1[2] = this.jj_gen;
/*      */       }
/*      */ 
/*   67 */       jj_consume_token(0);
/*   68 */       this.jjtree.closeNodeScope(localJDMSecurityDefs1, true);
/*   69 */       i = 0;
/*   70 */       return localJDMSecurityDefs1;
/*      */     } catch (Throwable localThrowable) {
/*   72 */       if (i != 0) {
/*   73 */         this.jjtree.clearNodeScope(localJDMSecurityDefs1);
/*   74 */         i = 0;
/*      */       } else {
/*   76 */         this.jjtree.popNode();
/*      */       }
/*   78 */       if ((localThrowable instanceof RuntimeException)) {
/*   79 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*   81 */       if ((localThrowable instanceof ParseException)) {
/*   82 */         throw ((ParseException)localThrowable);
/*      */       }
/*   84 */       throw ((Error)localThrowable);
/*      */     } finally {
/*   86 */       if (i != 0)
/*   87 */         this.jjtree.closeNodeScope(localJDMSecurityDefs1, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void AclBlock()
/*      */     throws ParseException
/*      */   {
/*   95 */     JDMAclBlock localJDMAclBlock = new JDMAclBlock(1);
/*   96 */     int i = 1;
/*   97 */     this.jjtree.openNodeScope(localJDMAclBlock);
/*      */     try {
/*   99 */       jj_consume_token(8);
/*  100 */       jj_consume_token(9);
/*  101 */       jj_consume_token(13);
/*      */       while (true)
/*      */       {
/*  104 */         AclItem();
/*  105 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 13:
/*      */         }
/*      */       }
/*  110 */       this.jj_la1[3] = this.jj_gen;
/*      */ 
/*  114 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  116 */       if (i != 0) {
/*  117 */         this.jjtree.clearNodeScope(localJDMAclBlock);
/*  118 */         i = 0;
/*      */       } else {
/*  120 */         this.jjtree.popNode();
/*      */       }
/*  122 */       if ((localThrowable instanceof RuntimeException)) {
/*  123 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  125 */       if ((localThrowable instanceof ParseException)) {
/*  126 */         throw ((ParseException)localThrowable);
/*      */       }
/*  128 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  130 */       if (i != 0)
/*  131 */         this.jjtree.closeNodeScope(localJDMAclBlock, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void AclItem()
/*      */     throws ParseException
/*      */   {
/*  138 */     JDMAclItem localJDMAclItem = new JDMAclItem(2);
/*  139 */     int i = 1;
/*  140 */     this.jjtree.openNodeScope(localJDMAclItem);
/*      */     try {
/*  142 */       jj_consume_token(13);
/*  143 */       localJDMAclItem.com = Communities();
/*  144 */       localJDMAclItem.access = Access();
/*  145 */       Managers();
/*  146 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  148 */       if (i != 0) {
/*  149 */         this.jjtree.clearNodeScope(localJDMAclItem);
/*  150 */         i = 0;
/*      */       } else {
/*  152 */         this.jjtree.popNode();
/*      */       }
/*  154 */       if ((localThrowable instanceof RuntimeException)) {
/*  155 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  157 */       if ((localThrowable instanceof ParseException)) {
/*  158 */         throw ((ParseException)localThrowable);
/*      */       }
/*  160 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  162 */       if (i != 0)
/*  163 */         this.jjtree.closeNodeScope(localJDMAclItem, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final JDMCommunities Communities()
/*      */     throws ParseException
/*      */   {
/*  170 */     JDMCommunities localJDMCommunities1 = new JDMCommunities(3);
/*  171 */     int i = 1;
/*  172 */     this.jjtree.openNodeScope(localJDMCommunities1);
/*      */     try {
/*  174 */       jj_consume_token(10);
/*  175 */       jj_consume_token(9);
/*  176 */       Community();
/*      */       while (true)
/*      */       {
/*  179 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 36:
/*  182 */           break;
/*      */         default:
/*  184 */           this.jj_la1[4] = this.jj_gen;
/*  185 */           break;
/*      */         }
/*  187 */         jj_consume_token(36);
/*  188 */         Community();
/*      */       }
/*  190 */       this.jjtree.closeNodeScope(localJDMCommunities1, true);
/*  191 */       i = 0;
/*  192 */       return localJDMCommunities1;
/*      */     } catch (Throwable localThrowable) {
/*  194 */       if (i != 0) {
/*  195 */         this.jjtree.clearNodeScope(localJDMCommunities1);
/*  196 */         i = 0;
/*      */       } else {
/*  198 */         this.jjtree.popNode();
/*      */       }
/*  200 */       if ((localThrowable instanceof RuntimeException)) {
/*  201 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  203 */       if ((localThrowable instanceof ParseException)) {
/*  204 */         throw ((ParseException)localThrowable);
/*      */       }
/*  206 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  208 */       if (i != 0)
/*  209 */         this.jjtree.closeNodeScope(localJDMCommunities1, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void Community()
/*      */     throws ParseException
/*      */   {
/*  217 */     JDMCommunity localJDMCommunity = new JDMCommunity(4);
/*  218 */     int i = 1;
/*  219 */     this.jjtree.openNodeScope(localJDMCommunity);
/*      */     try {
/*  221 */       Token localToken = jj_consume_token(31);
/*  222 */       this.jjtree.closeNodeScope(localJDMCommunity, true);
/*  223 */       i = 0;
/*  224 */       localJDMCommunity.communityString = localToken.image;
/*      */     } finally {
/*  226 */       if (i != 0)
/*  227 */         this.jjtree.closeNodeScope(localJDMCommunity, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final JDMAccess Access()
/*      */     throws ParseException
/*      */   {
/*  234 */     JDMAccess localJDMAccess1 = new JDMAccess(5);
/*  235 */     int i = 1;
/*  236 */     this.jjtree.openNodeScope(localJDMAccess1);
/*      */     try {
/*  238 */       jj_consume_token(7);
/*  239 */       jj_consume_token(9);
/*  240 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 17:
/*  242 */         jj_consume_token(17);
/*  243 */         localJDMAccess1.access = 17;
/*  244 */         break;
/*      */       case 18:
/*  246 */         jj_consume_token(18);
/*  247 */         localJDMAccess1.access = 18;
/*  248 */         break;
/*      */       default:
/*  250 */         this.jj_la1[5] = this.jj_gen;
/*  251 */         jj_consume_token(-1);
/*  252 */         throw new ParseException();
/*      */       }
/*  254 */       this.jjtree.closeNodeScope(localJDMAccess1, true);
/*  255 */       i = 0;
/*  256 */       return localJDMAccess1;
/*      */     } finally {
/*  258 */       if (i != 0)
/*  259 */         this.jjtree.closeNodeScope(localJDMAccess1, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void Managers()
/*      */     throws ParseException
/*      */   {
/*  267 */     JDMManagers localJDMManagers = new JDMManagers(6);
/*  268 */     int i = 1;
/*  269 */     this.jjtree.openNodeScope(localJDMManagers);
/*      */     try {
/*  271 */       jj_consume_token(14);
/*  272 */       jj_consume_token(9);
/*  273 */       Host();
/*      */       while (true)
/*      */       {
/*  276 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 36:
/*  279 */           break;
/*      */         default:
/*  281 */           this.jj_la1[6] = this.jj_gen;
/*  282 */           break;
/*      */         }
/*  284 */         jj_consume_token(36);
/*  285 */         Host();
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  288 */       if (i != 0) {
/*  289 */         this.jjtree.clearNodeScope(localJDMManagers);
/*  290 */         i = 0;
/*      */       } else {
/*  292 */         this.jjtree.popNode();
/*      */       }
/*  294 */       if ((localThrowable instanceof RuntimeException)) {
/*  295 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  297 */       if ((localThrowable instanceof ParseException)) {
/*  298 */         throw ((ParseException)localThrowable);
/*      */       }
/*  300 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  302 */       if (i != 0)
/*  303 */         this.jjtree.closeNodeScope(localJDMManagers, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void Host()
/*      */     throws ParseException
/*      */   {
/*  310 */     JDMHost localJDMHost = new JDMHost(7);
/*  311 */     int i = 1;
/*  312 */     this.jjtree.openNodeScope(localJDMHost);
/*      */     try {
/*  314 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 31:
/*  316 */         HostName();
/*  317 */         break;
/*      */       default:
/*  319 */         this.jj_la1[7] = this.jj_gen;
/*  320 */         if (jj_2_1(2147483647))
/*  321 */           NetMask();
/*  322 */         else if (jj_2_2(2147483647))
/*  323 */           NetMaskV6();
/*  324 */         else if (jj_2_3(2147483647))
/*  325 */           IpAddress();
/*      */         else
/*  327 */           switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */           case 28:
/*  329 */             IpV6Address();
/*  330 */             break;
/*      */           case 24:
/*  332 */             IpMask();
/*  333 */             break;
/*      */           default:
/*  335 */             this.jj_la1[8] = this.jj_gen;
/*  336 */             jj_consume_token(-1);
/*  337 */             throw new ParseException();
/*      */           }
/*      */         break;
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  342 */       if (i != 0) {
/*  343 */         this.jjtree.clearNodeScope(localJDMHost);
/*  344 */         i = 0;
/*      */       } else {
/*  346 */         this.jjtree.popNode();
/*      */       }
/*  348 */       if ((localThrowable instanceof RuntimeException)) {
/*  349 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  351 */       if ((localThrowable instanceof ParseException)) {
/*  352 */         throw ((ParseException)localThrowable);
/*      */       }
/*  354 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  356 */       if (i != 0)
/*  357 */         this.jjtree.closeNodeScope(localJDMHost, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void HostName()
/*      */     throws ParseException
/*      */   {
/*  364 */     JDMHostName localJDMHostName = new JDMHostName(8);
/*  365 */     int i = 1;
/*  366 */     this.jjtree.openNodeScope(localJDMHostName);
/*      */     try {
/*  368 */       Token localToken = jj_consume_token(31);
/*  369 */       localJDMHostName.name.append(localToken.image);
/*      */       while (true)
/*      */       {
/*  372 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 37:
/*  375 */           break;
/*      */         default:
/*  377 */           this.jj_la1[9] = this.jj_gen;
/*  378 */           break;
/*      */         }
/*  380 */         jj_consume_token(37);
/*  381 */         localToken = jj_consume_token(31);
/*  382 */         localJDMHostName.name.append("." + localToken.image);
/*      */       }
/*      */     } finally {
/*  385 */       if (i != 0)
/*  386 */         this.jjtree.closeNodeScope(localJDMHostName, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void IpAddress()
/*      */     throws ParseException
/*      */   {
/*  393 */     JDMIpAddress localJDMIpAddress = new JDMIpAddress(9);
/*  394 */     int i = 1;
/*  395 */     this.jjtree.openNodeScope(localJDMIpAddress);
/*      */     try {
/*  397 */       Token localToken = jj_consume_token(24);
/*  398 */       localJDMIpAddress.address.append(localToken.image);
/*      */       while (true)
/*      */       {
/*  401 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 37:
/*  404 */           break;
/*      */         default:
/*  406 */           this.jj_la1[10] = this.jj_gen;
/*  407 */           break;
/*      */         }
/*  409 */         jj_consume_token(37);
/*  410 */         localToken = jj_consume_token(24);
/*  411 */         localJDMIpAddress.address.append("." + localToken.image);
/*      */       }
/*      */     } finally {
/*  414 */       if (i != 0)
/*  415 */         this.jjtree.closeNodeScope(localJDMIpAddress, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void IpV6Address()
/*      */     throws ParseException
/*      */   {
/*  422 */     JDMIpV6Address localJDMIpV6Address = new JDMIpV6Address(10);
/*  423 */     int i = 1;
/*  424 */     this.jjtree.openNodeScope(localJDMIpV6Address);
/*      */     try {
/*  426 */       Token localToken = jj_consume_token(28);
/*  427 */       this.jjtree.closeNodeScope(localJDMIpV6Address, true);
/*  428 */       i = 0;
/*  429 */       localJDMIpV6Address.address.append(localToken.image);
/*      */     } finally {
/*  431 */       if (i != 0)
/*  432 */         this.jjtree.closeNodeScope(localJDMIpV6Address, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void IpMask()
/*      */     throws ParseException
/*      */   {
/*  439 */     JDMIpMask localJDMIpMask = new JDMIpMask(11);
/*  440 */     int i = 1;
/*  441 */     this.jjtree.openNodeScope(localJDMIpMask);
/*      */     try {
/*  443 */       Token localToken = jj_consume_token(24);
/*  444 */       localJDMIpMask.address.append(localToken.image);
/*      */       while (true)
/*      */       {
/*  447 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 38:
/*  450 */           break;
/*      */         default:
/*  452 */           this.jj_la1[11] = this.jj_gen;
/*  453 */           break;
/*      */         }
/*  455 */         jj_consume_token(38);
/*  456 */         localToken = jj_consume_token(24);
/*  457 */         localJDMIpMask.address.append("." + localToken.image);
/*      */       }
/*      */     } finally {
/*  460 */       if (i != 0)
/*  461 */         this.jjtree.closeNodeScope(localJDMIpMask, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void NetMask()
/*      */     throws ParseException
/*      */   {
/*  468 */     JDMNetMask localJDMNetMask = new JDMNetMask(12);
/*  469 */     int i = 1;
/*  470 */     this.jjtree.openNodeScope(localJDMNetMask);
/*      */     try {
/*  472 */       Token localToken = jj_consume_token(24);
/*  473 */       localJDMNetMask.address.append(localToken.image);
/*      */       while (true)
/*      */       {
/*  476 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 37:
/*  479 */           break;
/*      */         default:
/*  481 */           this.jj_la1[12] = this.jj_gen;
/*  482 */           break;
/*      */         }
/*  484 */         jj_consume_token(37);
/*  485 */         localToken = jj_consume_token(24);
/*  486 */         localJDMNetMask.address.append("." + localToken.image);
/*      */       }
/*  488 */       jj_consume_token(39);
/*  489 */       localToken = jj_consume_token(24);
/*  490 */       this.jjtree.closeNodeScope(localJDMNetMask, true);
/*  491 */       i = 0;
/*  492 */       localJDMNetMask.mask = localToken.image;
/*      */     } finally {
/*  494 */       if (i != 0)
/*  495 */         this.jjtree.closeNodeScope(localJDMNetMask, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void NetMaskV6()
/*      */     throws ParseException
/*      */   {
/*  502 */     JDMNetMaskV6 localJDMNetMaskV6 = new JDMNetMaskV6(13);
/*  503 */     int i = 1;
/*  504 */     this.jjtree.openNodeScope(localJDMNetMaskV6);
/*      */     try {
/*  506 */       Token localToken = jj_consume_token(28);
/*  507 */       localJDMNetMaskV6.address.append(localToken.image);
/*  508 */       jj_consume_token(39);
/*  509 */       localToken = jj_consume_token(24);
/*  510 */       this.jjtree.closeNodeScope(localJDMNetMaskV6, true);
/*  511 */       i = 0;
/*  512 */       localJDMNetMaskV6.mask = localToken.image;
/*      */     } finally {
/*  514 */       if (i != 0)
/*  515 */         this.jjtree.closeNodeScope(localJDMNetMaskV6, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void TrapBlock()
/*      */     throws ParseException
/*      */   {
/*  522 */     JDMTrapBlock localJDMTrapBlock = new JDMTrapBlock(14);
/*  523 */     int i = 1;
/*  524 */     this.jjtree.openNodeScope(localJDMTrapBlock);
/*      */     try {
/*  526 */       jj_consume_token(19);
/*  527 */       jj_consume_token(9);
/*  528 */       jj_consume_token(13);
/*      */       while (true)
/*      */       {
/*  531 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 13:
/*  534 */           break;
/*      */         default:
/*  536 */           this.jj_la1[13] = this.jj_gen;
/*  537 */           break;
/*      */         }
/*  539 */         TrapItem();
/*      */       }
/*  541 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  543 */       if (i != 0) {
/*  544 */         this.jjtree.clearNodeScope(localJDMTrapBlock);
/*  545 */         i = 0;
/*      */       } else {
/*  547 */         this.jjtree.popNode();
/*      */       }
/*  549 */       if ((localThrowable instanceof RuntimeException)) {
/*  550 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  552 */       if ((localThrowable instanceof ParseException)) {
/*  553 */         throw ((ParseException)localThrowable);
/*      */       }
/*  555 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  557 */       if (i != 0)
/*  558 */         this.jjtree.closeNodeScope(localJDMTrapBlock, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void TrapItem()
/*      */     throws ParseException
/*      */   {
/*  565 */     JDMTrapItem localJDMTrapItem = new JDMTrapItem(15);
/*  566 */     int i = 1;
/*  567 */     this.jjtree.openNodeScope(localJDMTrapItem);
/*      */     try {
/*  569 */       jj_consume_token(13);
/*  570 */       localJDMTrapItem.comm = TrapCommunity();
/*  571 */       TrapInterestedHost();
/*      */       while (true)
/*      */       {
/*  574 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 13:
/*  577 */           break;
/*      */         default:
/*  579 */           this.jj_la1[14] = this.jj_gen;
/*  580 */           break;
/*      */         }
/*  582 */         Enterprise();
/*      */       }
/*  584 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  586 */       if (i != 0) {
/*  587 */         this.jjtree.clearNodeScope(localJDMTrapItem);
/*  588 */         i = 0;
/*      */       } else {
/*  590 */         this.jjtree.popNode();
/*      */       }
/*  592 */       if ((localThrowable instanceof RuntimeException)) {
/*  593 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  595 */       if ((localThrowable instanceof ParseException)) {
/*  596 */         throw ((ParseException)localThrowable);
/*      */       }
/*  598 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  600 */       if (i != 0)
/*  601 */         this.jjtree.closeNodeScope(localJDMTrapItem, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final JDMTrapCommunity TrapCommunity()
/*      */     throws ParseException
/*      */   {
/*  608 */     JDMTrapCommunity localJDMTrapCommunity1 = new JDMTrapCommunity(16);
/*  609 */     int i = 1;
/*  610 */     this.jjtree.openNodeScope(localJDMTrapCommunity1);
/*      */     try {
/*  612 */       jj_consume_token(21);
/*  613 */       jj_consume_token(9);
/*  614 */       Token localToken = jj_consume_token(31);
/*  615 */       this.jjtree.closeNodeScope(localJDMTrapCommunity1, true);
/*  616 */       i = 0;
/*  617 */       localJDMTrapCommunity1.community = localToken.image; return localJDMTrapCommunity1;
/*      */     } finally {
/*  619 */       if (i != 0)
/*  620 */         this.jjtree.closeNodeScope(localJDMTrapCommunity1, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void TrapInterestedHost()
/*      */     throws ParseException
/*      */   {
/*  628 */     JDMTrapInterestedHost localJDMTrapInterestedHost = new JDMTrapInterestedHost(17);
/*  629 */     int i = 1;
/*  630 */     this.jjtree.openNodeScope(localJDMTrapInterestedHost);
/*      */     try {
/*  632 */       jj_consume_token(12);
/*  633 */       jj_consume_token(9);
/*  634 */       HostTrap();
/*      */       while (true)
/*      */       {
/*  637 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 36:
/*  640 */           break;
/*      */         default:
/*  642 */           this.jj_la1[15] = this.jj_gen;
/*  643 */           break;
/*      */         }
/*  645 */         jj_consume_token(36);
/*  646 */         HostTrap();
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  649 */       if (i != 0) {
/*  650 */         this.jjtree.clearNodeScope(localJDMTrapInterestedHost);
/*  651 */         i = 0;
/*      */       } else {
/*  653 */         this.jjtree.popNode();
/*      */       }
/*  655 */       if ((localThrowable instanceof RuntimeException)) {
/*  656 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  658 */       if ((localThrowable instanceof ParseException)) {
/*  659 */         throw ((ParseException)localThrowable);
/*      */       }
/*  661 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  663 */       if (i != 0)
/*  664 */         this.jjtree.closeNodeScope(localJDMTrapInterestedHost, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void HostTrap()
/*      */     throws ParseException
/*      */   {
/*  671 */     JDMHostTrap localJDMHostTrap = new JDMHostTrap(18);
/*  672 */     int i = 1;
/*  673 */     this.jjtree.openNodeScope(localJDMHostTrap);
/*      */     try {
/*  675 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 31:
/*  677 */         HostName();
/*  678 */         break;
/*      */       case 24:
/*  680 */         IpAddress();
/*  681 */         break;
/*      */       case 28:
/*  683 */         IpV6Address();
/*  684 */         break;
/*      */       default:
/*  686 */         this.jj_la1[16] = this.jj_gen;
/*  687 */         jj_consume_token(-1);
/*  688 */         throw new ParseException();
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  691 */       if (i != 0) {
/*  692 */         this.jjtree.clearNodeScope(localJDMHostTrap);
/*  693 */         i = 0;
/*      */       } else {
/*  695 */         this.jjtree.popNode();
/*      */       }
/*  697 */       if ((localThrowable instanceof RuntimeException)) {
/*  698 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  700 */       if ((localThrowable instanceof ParseException)) {
/*  701 */         throw ((ParseException)localThrowable);
/*      */       }
/*  703 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  705 */       if (i != 0)
/*  706 */         this.jjtree.closeNodeScope(localJDMHostTrap, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void Enterprise()
/*      */     throws ParseException
/*      */   {
/*  713 */     JDMEnterprise localJDMEnterprise = new JDMEnterprise(19);
/*  714 */     int i = 1;
/*  715 */     this.jjtree.openNodeScope(localJDMEnterprise);
/*      */     try {
/*  717 */       jj_consume_token(13);
/*  718 */       jj_consume_token(11);
/*  719 */       jj_consume_token(9);
/*  720 */       Token localToken = jj_consume_token(35);
/*  721 */       localJDMEnterprise.enterprise = localToken.image;
/*  722 */       jj_consume_token(23);
/*  723 */       jj_consume_token(9);
/*  724 */       TrapNum();
/*      */       while (true)
/*      */       {
/*  727 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 36:
/*  730 */           break;
/*      */         default:
/*  732 */           this.jj_la1[17] = this.jj_gen;
/*  733 */           break;
/*      */         }
/*  735 */         jj_consume_token(36);
/*  736 */         TrapNum();
/*      */       }
/*  738 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  740 */       if (i != 0) {
/*  741 */         this.jjtree.clearNodeScope(localJDMEnterprise);
/*  742 */         i = 0;
/*      */       } else {
/*  744 */         this.jjtree.popNode();
/*      */       }
/*  746 */       if ((localThrowable instanceof RuntimeException)) {
/*  747 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  749 */       if ((localThrowable instanceof ParseException)) {
/*  750 */         throw ((ParseException)localThrowable);
/*      */       }
/*  752 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  754 */       if (i != 0)
/*  755 */         this.jjtree.closeNodeScope(localJDMEnterprise, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void TrapNum()
/*      */     throws ParseException
/*      */   {
/*  762 */     JDMTrapNum localJDMTrapNum = new JDMTrapNum(20);
/*  763 */     int i = 1;
/*  764 */     this.jjtree.openNodeScope(localJDMTrapNum);
/*      */     try {
/*  766 */       Token localToken = jj_consume_token(24);
/*  767 */       localJDMTrapNum.low = Integer.parseInt(localToken.image);
/*  768 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 15:
/*  770 */         jj_consume_token(15);
/*  771 */         localToken = jj_consume_token(24);
/*  772 */         localJDMTrapNum.high = Integer.parseInt(localToken.image);
/*  773 */         break;
/*      */       default:
/*  775 */         this.jj_la1[18] = this.jj_gen;
/*      */       }
/*      */     }
/*      */     finally {
/*  779 */       if (i != 0)
/*  780 */         this.jjtree.closeNodeScope(localJDMTrapNum, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void InformBlock()
/*      */     throws ParseException
/*      */   {
/*  787 */     JDMInformBlock localJDMInformBlock = new JDMInformBlock(21);
/*  788 */     int i = 1;
/*  789 */     this.jjtree.openNodeScope(localJDMInformBlock);
/*      */     try {
/*  791 */       jj_consume_token(20);
/*  792 */       jj_consume_token(9);
/*  793 */       jj_consume_token(13);
/*      */       while (true)
/*      */       {
/*  796 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 13:
/*  799 */           break;
/*      */         default:
/*  801 */           this.jj_la1[19] = this.jj_gen;
/*  802 */           break;
/*      */         }
/*  804 */         InformItem();
/*      */       }
/*  806 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  808 */       if (i != 0) {
/*  809 */         this.jjtree.clearNodeScope(localJDMInformBlock);
/*  810 */         i = 0;
/*      */       } else {
/*  812 */         this.jjtree.popNode();
/*      */       }
/*  814 */       if ((localThrowable instanceof RuntimeException)) {
/*  815 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  817 */       if ((localThrowable instanceof ParseException)) {
/*  818 */         throw ((ParseException)localThrowable);
/*      */       }
/*  820 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  822 */       if (i != 0)
/*  823 */         this.jjtree.closeNodeScope(localJDMInformBlock, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void InformItem()
/*      */     throws ParseException
/*      */   {
/*  830 */     JDMInformItem localJDMInformItem = new JDMInformItem(22);
/*  831 */     int i = 1;
/*  832 */     this.jjtree.openNodeScope(localJDMInformItem);
/*      */     try {
/*  834 */       jj_consume_token(13);
/*  835 */       localJDMInformItem.comm = InformCommunity();
/*  836 */       InformInterestedHost();
/*  837 */       jj_consume_token(16);
/*      */     } catch (Throwable localThrowable) {
/*  839 */       if (i != 0) {
/*  840 */         this.jjtree.clearNodeScope(localJDMInformItem);
/*  841 */         i = 0;
/*      */       } else {
/*  843 */         this.jjtree.popNode();
/*      */       }
/*  845 */       if ((localThrowable instanceof RuntimeException)) {
/*  846 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  848 */       if ((localThrowable instanceof ParseException)) {
/*  849 */         throw ((ParseException)localThrowable);
/*      */       }
/*  851 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  853 */       if (i != 0)
/*  854 */         this.jjtree.closeNodeScope(localJDMInformItem, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final JDMInformCommunity InformCommunity()
/*      */     throws ParseException
/*      */   {
/*  861 */     JDMInformCommunity localJDMInformCommunity1 = new JDMInformCommunity(23);
/*  862 */     int i = 1;
/*  863 */     this.jjtree.openNodeScope(localJDMInformCommunity1);
/*      */     try {
/*  865 */       jj_consume_token(22);
/*  866 */       jj_consume_token(9);
/*  867 */       Token localToken = jj_consume_token(31);
/*  868 */       this.jjtree.closeNodeScope(localJDMInformCommunity1, true);
/*  869 */       i = 0;
/*  870 */       localJDMInformCommunity1.community = localToken.image; return localJDMInformCommunity1;
/*      */     } finally {
/*  872 */       if (i != 0)
/*  873 */         this.jjtree.closeNodeScope(localJDMInformCommunity1, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void InformInterestedHost()
/*      */     throws ParseException
/*      */   {
/*  881 */     JDMInformInterestedHost localJDMInformInterestedHost = new JDMInformInterestedHost(24);
/*  882 */     int i = 1;
/*  883 */     this.jjtree.openNodeScope(localJDMInformInterestedHost);
/*      */     try {
/*  885 */       jj_consume_token(12);
/*  886 */       jj_consume_token(9);
/*  887 */       HostInform();
/*      */       while (true)
/*      */       {
/*  890 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 36:
/*  893 */           break;
/*      */         default:
/*  895 */           this.jj_la1[20] = this.jj_gen;
/*  896 */           break;
/*      */         }
/*  898 */         jj_consume_token(36);
/*  899 */         HostInform();
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  902 */       if (i != 0) {
/*  903 */         this.jjtree.clearNodeScope(localJDMInformInterestedHost);
/*  904 */         i = 0;
/*      */       } else {
/*  906 */         this.jjtree.popNode();
/*      */       }
/*  908 */       if ((localThrowable instanceof RuntimeException)) {
/*  909 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  911 */       if ((localThrowable instanceof ParseException)) {
/*  912 */         throw ((ParseException)localThrowable);
/*      */       }
/*  914 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  916 */       if (i != 0)
/*  917 */         this.jjtree.closeNodeScope(localJDMInformInterestedHost, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void HostInform()
/*      */     throws ParseException
/*      */   {
/*  924 */     JDMHostInform localJDMHostInform = new JDMHostInform(25);
/*  925 */     int i = 1;
/*  926 */     this.jjtree.openNodeScope(localJDMHostInform);
/*      */     try {
/*  928 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 31:
/*  930 */         HostName();
/*  931 */         break;
/*      */       case 24:
/*  933 */         IpAddress();
/*  934 */         break;
/*      */       case 28:
/*  936 */         IpV6Address();
/*  937 */         break;
/*      */       default:
/*  939 */         this.jj_la1[21] = this.jj_gen;
/*  940 */         jj_consume_token(-1);
/*  941 */         throw new ParseException();
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/*  944 */       if (i != 0) {
/*  945 */         this.jjtree.clearNodeScope(localJDMHostInform);
/*  946 */         i = 0;
/*      */       } else {
/*  948 */         this.jjtree.popNode();
/*      */       }
/*  950 */       if ((localThrowable instanceof RuntimeException)) {
/*  951 */         throw ((RuntimeException)localThrowable);
/*      */       }
/*  953 */       if ((localThrowable instanceof ParseException)) {
/*  954 */         throw ((ParseException)localThrowable);
/*      */       }
/*  956 */       throw ((Error)localThrowable);
/*      */     } finally {
/*  958 */       if (i != 0)
/*  959 */         this.jjtree.closeNodeScope(localJDMHostInform, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final boolean jj_2_1(int paramInt)
/*      */   {
/*  965 */     this.jj_la = paramInt; this.jj_lastpos = (this.jj_scanpos = this.token);
/*  966 */     boolean bool = !jj_3_1();
/*  967 */     jj_save(0, paramInt);
/*  968 */     return bool;
/*      */   }
/*      */ 
/*      */   private final boolean jj_2_2(int paramInt) {
/*  972 */     this.jj_la = paramInt; this.jj_lastpos = (this.jj_scanpos = this.token);
/*  973 */     boolean bool = !jj_3_2();
/*  974 */     jj_save(1, paramInt);
/*  975 */     return bool;
/*      */   }
/*      */ 
/*      */   private final boolean jj_2_3(int paramInt) {
/*  979 */     this.jj_la = paramInt; this.jj_lastpos = (this.jj_scanpos = this.token);
/*  980 */     boolean bool = !jj_3_3();
/*  981 */     jj_save(2, paramInt);
/*  982 */     return bool;
/*      */   }
/*      */ 
/*      */   private final boolean jj_3_3() {
/*  986 */     if (jj_scan_token(24)) return true;
/*  987 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/*  988 */     if (jj_scan_token(37)) return true;
/*  989 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/*  990 */     return false;
/*      */   }
/*      */ 
/*      */   private final boolean jj_3_2() {
/*  994 */     if (jj_scan_token(28)) return true;
/*  995 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/*  996 */     if (jj_scan_token(39)) return true;
/*  997 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/*  998 */     if (jj_scan_token(24)) return true;
/*  999 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/* 1000 */     return false;
/*      */   }
/*      */ 
/*      */   private final boolean jj_3_1() {
/* 1004 */     if (jj_scan_token(24)) return true;
/* 1005 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/*      */     do
/*      */     {
/* 1008 */       Token localToken = this.jj_scanpos;
/* 1009 */       if (jj_3R_14()) { this.jj_scanpos = localToken; break; } 
/* 1010 */     }while ((this.jj_la != 0) || (this.jj_scanpos != this.jj_lastpos)); return false;
/*      */ 
/* 1012 */     if (jj_scan_token(39)) return true;
/* 1013 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/* 1014 */     if (jj_scan_token(24)) return true;
/* 1015 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/* 1016 */     return false;
/*      */   }
/*      */ 
/*      */   private final boolean jj_3R_14() {
/* 1020 */     if (jj_scan_token(37)) return true;
/* 1021 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/* 1022 */     if (jj_scan_token(24)) return true;
/* 1023 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) return false;
/* 1024 */     return false;
/*      */   }
/*      */ 
/*      */   public Parser(InputStream paramInputStream)
/*      */   {
/* 1044 */     this.jj_input_stream = new ASCII_CharStream(paramInputStream, 1, 1);
/* 1045 */     this.token_source = new ParserTokenManager(this.jj_input_stream);
/* 1046 */     this.token = new Token();
/* 1047 */     this.jj_ntk = -1;
/* 1048 */     this.jj_gen = 0;
/* 1049 */     for (int i = 0; i < 22; i++) this.jj_la1[i] = -1;
/* 1050 */     for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls(); 
/*      */   }
/*      */ 
/*      */   public void ReInit(InputStream paramInputStream)
/*      */   {
/* 1054 */     this.jj_input_stream.ReInit(paramInputStream, 1, 1);
/* 1055 */     this.token_source.ReInit(this.jj_input_stream);
/* 1056 */     this.token = new Token();
/* 1057 */     this.jj_ntk = -1;
/* 1058 */     this.jjtree.reset();
/* 1059 */     this.jj_gen = 0;
/* 1060 */     for (int i = 0; i < 22; i++) this.jj_la1[i] = -1;
/* 1061 */     for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls(); 
/*      */   }
/*      */ 
/*      */   public Parser(Reader paramReader)
/*      */   {
/* 1065 */     this.jj_input_stream = new ASCII_CharStream(paramReader, 1, 1);
/* 1066 */     this.token_source = new ParserTokenManager(this.jj_input_stream);
/* 1067 */     this.token = new Token();
/* 1068 */     this.jj_ntk = -1;
/* 1069 */     this.jj_gen = 0;
/* 1070 */     for (int i = 0; i < 22; i++) this.jj_la1[i] = -1;
/* 1071 */     for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls(); 
/*      */   }
/*      */ 
/*      */   public void ReInit(Reader paramReader)
/*      */   {
/* 1075 */     this.jj_input_stream.ReInit(paramReader, 1, 1);
/* 1076 */     this.token_source.ReInit(this.jj_input_stream);
/* 1077 */     this.token = new Token();
/* 1078 */     this.jj_ntk = -1;
/* 1079 */     this.jjtree.reset();
/* 1080 */     this.jj_gen = 0;
/* 1081 */     for (int i = 0; i < 22; i++) this.jj_la1[i] = -1;
/* 1082 */     for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls(); 
/*      */   }
/*      */ 
/*      */   public Parser(ParserTokenManager paramParserTokenManager)
/*      */   {
/* 1086 */     this.token_source = paramParserTokenManager;
/* 1087 */     this.token = new Token();
/* 1088 */     this.jj_ntk = -1;
/* 1089 */     this.jj_gen = 0;
/* 1090 */     for (int i = 0; i < 22; i++) this.jj_la1[i] = -1;
/* 1091 */     for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls(); 
/*      */   }
/*      */ 
/*      */   public void ReInit(ParserTokenManager paramParserTokenManager)
/*      */   {
/* 1095 */     this.token_source = paramParserTokenManager;
/* 1096 */     this.token = new Token();
/* 1097 */     this.jj_ntk = -1;
/* 1098 */     this.jjtree.reset();
/* 1099 */     this.jj_gen = 0;
/* 1100 */     for (int i = 0; i < 22; i++) this.jj_la1[i] = -1;
/* 1101 */     for (i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */ 
/*      */   private final Token jj_consume_token(int paramInt)
/*      */     throws ParseException
/*      */   {
/* 1106 */     Token localToken;
/* 1106 */     if ((localToken = this.token).next != null) this.token = this.token.next; else
/* 1107 */       this.token = (this.token.next = this.token_source.getNextToken());
/* 1108 */     this.jj_ntk = -1;
/* 1109 */     if (this.token.kind == paramInt) {
/* 1110 */       this.jj_gen += 1;
/* 1111 */       if (++this.jj_gc > 100) {
/* 1112 */         this.jj_gc = 0;
/* 1113 */         for (int i = 0; i < this.jj_2_rtns.length; i++) {
/* 1114 */           JJCalls localJJCalls = this.jj_2_rtns[i];
/* 1115 */           while (localJJCalls != null) {
/* 1116 */             if (localJJCalls.gen < this.jj_gen) localJJCalls.first = null;
/* 1117 */             localJJCalls = localJJCalls.next;
/*      */           }
/*      */         }
/*      */       }
/* 1121 */       return this.token;
/*      */     }
/* 1123 */     this.token = localToken;
/* 1124 */     this.jj_kind = paramInt;
/* 1125 */     throw generateParseException();
/*      */   }
/*      */ 
/*      */   private final boolean jj_scan_token(int paramInt) {
/* 1129 */     if (this.jj_scanpos == this.jj_lastpos) {
/* 1130 */       this.jj_la -= 1;
/* 1131 */       if (this.jj_scanpos.next == null)
/* 1132 */         this.jj_lastpos = (this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken());
/*      */       else
/* 1134 */         this.jj_lastpos = (this.jj_scanpos = this.jj_scanpos.next);
/*      */     }
/*      */     else {
/* 1137 */       this.jj_scanpos = this.jj_scanpos.next;
/*      */     }
/* 1139 */     if (this.jj_rescan) {
/* 1140 */       int i = 0; for (Token localToken = this.token; 
/* 1141 */         (localToken != null) && (localToken != this.jj_scanpos); localToken = localToken.next) i++;
/* 1142 */       if (localToken != null) jj_add_error_token(paramInt, i);
/*      */     }
/* 1144 */     return this.jj_scanpos.kind != paramInt;
/*      */   }
/*      */ 
/*      */   public final Token getNextToken() {
/* 1148 */     if (this.token.next != null) this.token = this.token.next; else
/* 1149 */       this.token = (this.token.next = this.token_source.getNextToken());
/* 1150 */     this.jj_ntk = -1;
/* 1151 */     this.jj_gen += 1;
/* 1152 */     return this.token;
/*      */   }
/*      */ 
/*      */   public final Token getToken(int paramInt) {
/* 1156 */     Token localToken = this.lookingAhead ? this.jj_scanpos : this.token;
/* 1157 */     for (int i = 0; i < paramInt; i++) {
/* 1158 */       if (localToken.next != null) localToken = localToken.next; else
/* 1159 */         localToken = localToken.next = this.token_source.getNextToken();
/*      */     }
/* 1161 */     return localToken;
/*      */   }
/*      */ 
/*      */   private final int jj_ntk() {
/* 1165 */     if ((this.jj_nt = this.token.next) == null) {
/* 1166 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
/*      */     }
/* 1168 */     return this.jj_ntk = this.jj_nt.kind;
/*      */   }
/*      */ 
/*      */   private void jj_add_error_token(int paramInt1, int paramInt2)
/*      */   {
/* 1178 */     if (paramInt2 >= 100) return;
/* 1179 */     if (paramInt2 == this.jj_endpos + 1) {
/* 1180 */       this.jj_lasttokens[(this.jj_endpos++)] = paramInt1;
/* 1181 */     } else if (this.jj_endpos != 0) {
/* 1182 */       this.jj_expentry = new int[this.jj_endpos];
/* 1183 */       for (int i = 0; i < this.jj_endpos; i++) {
/* 1184 */         this.jj_expentry[i] = this.jj_lasttokens[i];
/*      */       }
/* 1186 */       i = 0;
/* 1187 */       for (Enumeration localEnumeration = this.jj_expentries.elements(); localEnumeration.hasMoreElements(); ) {
/* 1188 */         int[] arrayOfInt = (int[])localEnumeration.nextElement();
/* 1189 */         if (arrayOfInt.length == this.jj_expentry.length) {
/* 1190 */           i = 1;
/* 1191 */           for (int j = 0; j < this.jj_expentry.length; j++) {
/* 1192 */             if (arrayOfInt[j] != this.jj_expentry[j]) {
/* 1193 */               i = 0;
/* 1194 */               break;
/*      */             }
/*      */           }
/* 1197 */           if (i != 0) break;
/*      */         }
/*      */       }
/* 1200 */       if (i == 0) this.jj_expentries.addElement(this.jj_expentry);
/* 1201 */       if (paramInt2 != 0)
/*      */       {
/*      */         int tmp205_204 = paramInt2; this.jj_endpos = tmp205_204; this.jj_lasttokens[(tmp205_204 - 1)] = paramInt1;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/* 1206 */   public final ParseException generateParseException() { this.jj_expentries.removeAllElements();
/* 1207 */     boolean[] arrayOfBoolean = new boolean[40];
/* 1208 */     for (int i = 0; i < 40; i++) {
/* 1209 */       arrayOfBoolean[i] = false;
/*      */     }
/* 1211 */     if (this.jj_kind >= 0) {
/* 1212 */       arrayOfBoolean[this.jj_kind] = true;
/* 1213 */       this.jj_kind = -1;
/*      */     }
/* 1215 */     for (i = 0; i < 22; i++) {
/* 1216 */       if (this.jj_la1[i] == this.jj_gen) {
/* 1217 */         for (j = 0; j < 32; j++) {
/* 1218 */           if ((this.jj_la1_0[i] & 1 << j) != 0) {
/* 1219 */             arrayOfBoolean[j] = true;
/*      */           }
/* 1221 */           if ((this.jj_la1_1[i] & 1 << j) != 0) {
/* 1222 */             arrayOfBoolean[(32 + j)] = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1227 */     for (i = 0; i < 40; i++) {
/* 1228 */       if (arrayOfBoolean[i] != 0) {
/* 1229 */         this.jj_expentry = new int[1];
/* 1230 */         this.jj_expentry[0] = i;
/* 1231 */         this.jj_expentries.addElement(this.jj_expentry);
/*      */       }
/*      */     }
/* 1234 */     this.jj_endpos = 0;
/* 1235 */     jj_rescan_token();
/* 1236 */     jj_add_error_token(0, 0);
/* 1237 */     int[][] arrayOfInt = new int[this.jj_expentries.size()][];
/* 1238 */     for (int j = 0; j < this.jj_expentries.size(); j++) {
/* 1239 */       arrayOfInt[j] = ((int[])(int[])this.jj_expentries.elementAt(j));
/*      */     }
/* 1241 */     return new ParseException(this.token, arrayOfInt, tokenImage); }
/*      */ 
/*      */   public final void enable_tracing()
/*      */   {
/*      */   }
/*      */ 
/*      */   public final void disable_tracing() {
/*      */   }
/*      */ 
/*      */   private final void jj_rescan_token() {
/* 1251 */     this.jj_rescan = true;
/* 1252 */     for (int i = 0; i < 3; i++) {
/* 1253 */       JJCalls localJJCalls = this.jj_2_rtns[i];
/*      */       do {
/* 1255 */         if (localJJCalls.gen > this.jj_gen) {
/* 1256 */           this.jj_la = localJJCalls.arg; this.jj_lastpos = (this.jj_scanpos = localJJCalls.first);
/* 1257 */           switch (i) { case 0:
/* 1258 */             jj_3_1(); break;
/*      */           case 1:
/* 1259 */             jj_3_2(); break;
/*      */           case 2:
/* 1260 */             jj_3_3();
/*      */           }
/*      */         }
/* 1263 */         localJJCalls = localJJCalls.next;
/* 1264 */       }while (localJJCalls != null);
/*      */     }
/* 1266 */     this.jj_rescan = false;
/*      */   }
/*      */ 
/*      */   private final void jj_save(int paramInt1, int paramInt2) {
/* 1270 */     JJCalls localJJCalls = this.jj_2_rtns[paramInt1];
/* 1271 */     while (localJJCalls.gen > this.jj_gen) {
/* 1272 */       if (localJJCalls.next == null) { localJJCalls = localJJCalls.next = new JJCalls(); break; }
/* 1273 */       localJJCalls = localJJCalls.next;
/*      */     }
/* 1275 */     localJJCalls.gen = (this.jj_gen + paramInt2 - this.jj_la); localJJCalls.first = this.token; localJJCalls.arg = paramInt2;
/*      */   }
/*      */ 
/*      */   static final class JJCalls
/*      */   {
/*      */     int gen;
/*      */     Token first;
/*      */     int arg;
/*      */     JJCalls next;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.Parser
 * JD-Core Version:    0.6.2
 */