/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ class ParserForXMLSchema extends RegexParser
/*     */ {
/* 372 */   private static Hashtable ranges = null;
/* 373 */   private static Hashtable ranges2 = null;
/*     */   private static final String SPACES = "\t\n\r\r  ";
/*     */   private static final String NAMECHARS = "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣";
/*     */   private static final String LETTERS = "AZazÀÖØöøıĴľŁňŊžƀǰǴǵǺȗɐʨʻˁʰˑΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣ｦﾟ";
/* 503 */   private static final int[] LETTERS_INT = { 120720, 120744, 120746, 120777, 195099, 195101 };
/*     */   private static final String DIGITS = "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩၀၉፩፱០៩᠐᠙０９";
/* 510 */   private static final int[] DIGITS_INT = { 120782, 120831 };
/*     */ 
/*     */   public ParserForXMLSchema()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ParserForXMLSchema(Locale locale)
/*     */   {
/*  40 */     super(locale);
/*     */   }
/*     */ 
/*     */   Token processCaret() throws ParseException {
/*  44 */     next();
/*  45 */     return Token.createChar(94);
/*     */   }
/*     */   Token processDollar() throws ParseException {
/*  48 */     next();
/*  49 */     return Token.createChar(36);
/*     */   }
/*     */   Token processLookahead() throws ParseException {
/*  52 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processNegativelookahead() throws ParseException {
/*  55 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processLookbehind() throws ParseException {
/*  58 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processNegativelookbehind() throws ParseException {
/*  61 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_A() throws ParseException {
/*  64 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_Z() throws ParseException {
/*  67 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_z() throws ParseException {
/*  70 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_b() throws ParseException {
/*  73 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_B() throws ParseException {
/*  76 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_lt() throws ParseException {
/*  79 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_gt() throws ParseException {
/*  82 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processStar(Token tok) throws ParseException {
/*  85 */     next();
/*  86 */     return Token.createClosure(tok);
/*     */   }
/*     */ 
/*     */   Token processPlus(Token tok) throws ParseException {
/*  90 */     next();
/*  91 */     return Token.createConcat(tok, Token.createClosure(tok));
/*     */   }
/*     */ 
/*     */   Token processQuestion(Token tok) throws ParseException {
/*  95 */     next();
/*  96 */     Token par = Token.createUnion();
/*  97 */     par.addChild(tok);
/*  98 */     par.addChild(Token.createEmpty());
/*  99 */     return par;
/*     */   }
/*     */   boolean checkQuestion(int off) {
/* 102 */     return false;
/*     */   }
/*     */   Token processParen() throws ParseException {
/* 105 */     next();
/* 106 */     Token tok = Token.createParen(parseRegex(), 0);
/* 107 */     if (read() != 7) throw ex("parser.factor.1", this.offset - 1);
/* 108 */     next();
/* 109 */     return tok;
/*     */   }
/*     */   Token processParen2() throws ParseException {
/* 112 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processCondition() throws ParseException {
/* 115 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processModifiers() throws ParseException {
/* 118 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processIndependent() throws ParseException {
/* 121 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */   Token processBacksolidus_c() throws ParseException {
/* 124 */     next();
/* 125 */     return getTokenForShorthand(99);
/*     */   }
/*     */   Token processBacksolidus_C() throws ParseException {
/* 128 */     next();
/* 129 */     return getTokenForShorthand(67);
/*     */   }
/*     */   Token processBacksolidus_i() throws ParseException {
/* 132 */     next();
/* 133 */     return getTokenForShorthand(105);
/*     */   }
/*     */   Token processBacksolidus_I() throws ParseException {
/* 136 */     next();
/* 137 */     return getTokenForShorthand(73);
/*     */   }
/*     */   Token processBacksolidus_g() throws ParseException {
/* 140 */     throw ex("parser.process.1", this.offset - 2);
/*     */   }
/*     */   Token processBacksolidus_X() throws ParseException {
/* 143 */     throw ex("parser.process.1", this.offset - 2);
/*     */   }
/*     */   Token processBackreference() throws ParseException {
/* 146 */     throw ex("parser.process.1", this.offset - 4);
/*     */   }
/*     */ 
/*     */   int processCIinCharacterClass(RangeToken tok, int c) {
/* 150 */     tok.mergeRanges(getTokenForShorthand(c));
/* 151 */     return -1;
/*     */   }
/*     */ 
/*     */   protected RangeToken parseCharacterClass(boolean useNrange)
/*     */     throws ParseException
/*     */   {
/* 173 */     setContext(1);
/* 174 */     next();
/* 175 */     boolean nrange = false;
/* 176 */     boolean wasDecoded = false;
/* 177 */     RangeToken base = null;
/*     */     RangeToken tok;
/*     */     RangeToken tok;
/* 179 */     if ((read() == 0) && (this.chardata == 94)) {
/* 180 */       nrange = true;
/* 181 */       next();
/* 182 */       base = Token.createRange();
/* 183 */       base.addRange(0, 1114111);
/* 184 */       tok = Token.createRange();
/*     */     } else {
/* 186 */       tok = Token.createRange();
/*     */     }
/*     */ 
/* 189 */     boolean firstloop = true;
/*     */     int type;
/* 190 */     while ((type = read()) != 1)
/*     */     {
/* 192 */       wasDecoded = false;
/*     */ 
/* 194 */       if ((type == 0) && (this.chardata == 93) && (!firstloop)) {
/* 195 */         if (!nrange) break;
/* 196 */         base.subtractRanges(tok);
/* 197 */         tok = base; break;
/*     */       }
/*     */ 
/* 201 */       int c = this.chardata;
/* 202 */       boolean end = false;
/* 203 */       if (type == 10) {
/* 204 */         switch (c) { case 68:
/*     */         case 83:
/*     */         case 87:
/*     */         case 100:
/*     */         case 115:
/*     */         case 119:
/* 208 */           tok.mergeRanges(getTokenForShorthand(c));
/* 209 */           end = true;
/* 210 */           break;
/*     */         case 67:
/*     */         case 73:
/*     */         case 99:
/*     */         case 105:
/* 214 */           c = processCIinCharacterClass(tok, c);
/* 215 */           if (c < 0) end = true; break;
/*     */         case 80:
/*     */         case 112:
/* 220 */           int pstart = this.offset;
/* 221 */           RangeToken tok2 = processBacksolidus_pP(c);
/* 222 */           if (tok2 == null) throw ex("parser.atom.5", pstart);
/* 223 */           tok.mergeRanges(tok2);
/* 224 */           end = true;
/* 225 */           break;
/*     */         case 45:
/* 228 */           c = decodeEscaped();
/* 229 */           wasDecoded = true;
/* 230 */           break;
/*     */         default:
/* 233 */           c = decodeEscaped();
/*     */         }
/*     */       }
/* 236 */       else if ((type == 24) && (!firstloop))
/*     */       {
/* 238 */         if (nrange) {
/* 239 */           base.subtractRanges(tok);
/* 240 */           tok = base;
/*     */         }
/* 242 */         RangeToken range2 = parseCharacterClass(false);
/* 243 */         tok.subtractRanges(range2);
/* 244 */         if ((read() == 0) && (this.chardata == 93)) break;
/* 245 */         throw ex("parser.cc.5", this.offset);
/*     */       }
/*     */ 
/* 248 */       next();
/* 249 */       if (!end) {
/* 250 */         if (type == 0) {
/* 251 */           if (c == 91) throw ex("parser.cc.6", this.offset - 2);
/* 252 */           if (c == 93) throw ex("parser.cc.7", this.offset - 2);
/* 253 */           if ((c == 45) && (this.chardata != 93) && (!firstloop)) throw ex("parser.cc.8", this.offset - 2);
/*     */         }
/* 255 */         if ((read() != 0) || (this.chardata != 45) || ((c == 45) && (firstloop))) {
/* 256 */           if ((!isSet(2)) || (c > 65535)) {
/* 257 */             tok.addRange(c, c);
/*     */           }
/*     */           else
/* 260 */             addCaseInsensitiveChar(tok, c);
/*     */         }
/*     */         else
/*     */         {
/* 264 */           next();
/* 265 */           if ((type = read()) == 1) throw ex("parser.cc.2", this.offset);
/*     */ 
/* 267 */           if ((type == 0) && (this.chardata == 93)) {
/* 268 */             if ((!isSet(2)) || (c > 65535)) {
/* 269 */               tok.addRange(c, c);
/*     */             }
/*     */             else {
/* 272 */               addCaseInsensitiveChar(tok, c);
/*     */             }
/* 274 */             tok.addRange(45, 45);
/*     */           } else {
/* 276 */             if (type == 24) {
/* 277 */               throw ex("parser.cc.8", this.offset - 1);
/*     */             }
/*     */ 
/* 280 */             int rangeend = this.chardata;
/* 281 */             if (type == 0) {
/* 282 */               if (rangeend == 91) throw ex("parser.cc.6", this.offset - 1);
/* 283 */               if (rangeend == 93) throw ex("parser.cc.7", this.offset - 1);
/* 284 */               if (rangeend == 45) throw ex("parser.cc.8", this.offset - 2);
/*     */             }
/* 286 */             else if (type == 10) {
/* 287 */               rangeend = decodeEscaped();
/* 288 */             }next();
/*     */ 
/* 290 */             if (c > rangeend) throw ex("parser.ope.3", this.offset - 1);
/* 291 */             if ((!isSet(2)) || ((c > 65535) && (rangeend > 65535)))
/*     */             {
/* 293 */               tok.addRange(c, rangeend);
/*     */             }
/*     */             else {
/* 296 */               addCaseInsensitiveCharRange(tok, c, rangeend);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 301 */       firstloop = false;
/*     */     }
/* 303 */     if (read() == 1)
/* 304 */       throw ex("parser.cc.2", this.offset);
/* 305 */     tok.sortRanges();
/* 306 */     tok.compactRanges();
/*     */ 
/* 308 */     setContext(0);
/* 309 */     next();
/*     */ 
/* 311 */     return tok;
/*     */   }
/*     */ 
/*     */   protected RangeToken parseSetOperations() throws ParseException {
/* 315 */     throw ex("parser.process.1", this.offset);
/*     */   }
/*     */ 
/*     */   Token getTokenForShorthand(int ch) {
/* 319 */     switch (ch) {
/*     */     case 100:
/* 321 */       return getRange("xml:isDigit", true);
/*     */     case 68:
/* 323 */       return getRange("xml:isDigit", false);
/*     */     case 119:
/* 325 */       return getRange("xml:isWord", true);
/*     */     case 87:
/* 327 */       return getRange("xml:isWord", false);
/*     */     case 115:
/* 329 */       return getRange("xml:isSpace", true);
/*     */     case 83:
/* 331 */       return getRange("xml:isSpace", false);
/*     */     case 99:
/* 333 */       return getRange("xml:isNameChar", true);
/*     */     case 67:
/* 335 */       return getRange("xml:isNameChar", false);
/*     */     case 105:
/* 337 */       return getRange("xml:isInitialNameChar", true);
/*     */     case 73:
/* 339 */       return getRange("xml:isInitialNameChar", false);
/*     */     }
/* 341 */     throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(ch, 16));
/*     */   }
/*     */ 
/*     */   int decodeEscaped() throws ParseException {
/* 345 */     if (read() != 10) throw ex("parser.next.1", this.offset - 1);
/* 346 */     int c = this.chardata;
/* 347 */     switch (c) { case 110:
/* 348 */       c = 10; break;
/*     */     case 114:
/* 349 */       c = 13; break;
/*     */     case 116:
/* 350 */       c = 9; break;
/*     */     case 40:
/*     */     case 41:
/*     */     case 42:
/*     */     case 43:
/*     */     case 45:
/*     */     case 46:
/*     */     case 63:
/*     */     case 91:
/*     */     case 92:
/*     */     case 93:
/*     */     case 94:
/*     */     case 123:
/*     */     case 124:
/*     */     case 125:
/* 365 */       break;
/*     */     default:
/* 367 */       throw ex("parser.process.1", this.offset - 2);
/*     */     }
/* 369 */     return c;
/*     */   }
/*     */ 
/*     */   protected static synchronized RangeToken getRange(String name, boolean positive)
/*     */   {
/* 375 */     if (ranges == null) {
/* 376 */       ranges = new Hashtable();
/* 377 */       ranges2 = new Hashtable();
/*     */ 
/* 379 */       Token tok = Token.createRange();
/* 380 */       setupRange(tok, "\t\n\r\r  ");
/* 381 */       ranges.put("xml:isSpace", tok);
/* 382 */       ranges2.put("xml:isSpace", Token.complementRanges(tok));
/*     */ 
/* 384 */       tok = Token.createRange();
/* 385 */       setupRange(tok, "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩၀၉፩፱០៩᠐᠙０９");
/* 386 */       setupRange(tok, DIGITS_INT);
/* 387 */       ranges.put("xml:isDigit", tok);
/* 388 */       ranges2.put("xml:isDigit", Token.complementRanges(tok));
/*     */ 
/* 390 */       tok = Token.createRange();
/* 391 */       setupRange(tok, "AZazÀÖØöøıĴľŁňŊžƀǰǴǵǺȗɐʨʻˁʰˑΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣ｦﾟ");
/* 392 */       setupRange(tok, LETTERS_INT);
/* 393 */       tok.mergeRanges((Token)ranges.get("xml:isDigit"));
/* 394 */       ranges.put("xml:isWord", tok);
/* 395 */       ranges2.put("xml:isWord", Token.complementRanges(tok));
/*     */ 
/* 397 */       tok = Token.createRange();
/* 398 */       setupRange(tok, "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣");
/* 399 */       ranges.put("xml:isNameChar", tok);
/* 400 */       ranges2.put("xml:isNameChar", Token.complementRanges(tok));
/*     */ 
/* 402 */       tok = Token.createRange();
/* 403 */       setupRange(tok, "AZazÀÖØöøıĴľŁňŊžƀǰǴǵǺȗɐʨʻˁʰˑΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣ｦﾟ");
/* 404 */       tok.addRange(95, 95);
/* 405 */       tok.addRange(58, 58);
/* 406 */       ranges.put("xml:isInitialNameChar", tok);
/* 407 */       ranges2.put("xml:isInitialNameChar", Token.complementRanges(tok));
/*     */     }
/* 409 */     RangeToken tok = positive ? (RangeToken)ranges.get(name) : (RangeToken)ranges2.get(name);
/*     */ 
/* 411 */     return tok;
/*     */   }
/*     */ 
/*     */   static void setupRange(Token range, String src) {
/* 415 */     int len = src.length();
/* 416 */     for (int i = 0; i < len; i += 2)
/* 417 */       range.addRange(src.charAt(i), src.charAt(i + 1));
/*     */   }
/*     */ 
/*     */   static void setupRange(Token range, int[] src) {
/* 421 */     int len = src.length;
/* 422 */     for (int i = 0; i < len; i += 2)
/* 423 */       range.addRange(src[i], src[(i + 1)]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.ParserForXMLSchema
 * JD-Core Version:    0.6.2
 */