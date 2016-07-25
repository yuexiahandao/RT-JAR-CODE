/*     */ package sun.io;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ @Deprecated
/*     */ public class CharacterEncoding
/*     */ {
/*     */   private static boolean sjisIsMS932;
/*  57 */   private static Map<String, String> aliasTable = new HashMap(460, 1.0F);
/*     */   private static volatile boolean installedAll;
/*     */ 
/*     */   public static String aliasName(String paramString)
/*     */   {
/* 116 */     if (paramString.startsWith("\001"))
/*     */     {
/* 118 */       paramString = paramString.substring(1);
/*     */     }
/*     */ 
/* 121 */     String str1 = paramString.toLowerCase(Locale.US);
/* 122 */     String str2 = (String)aliasTable.get(str1);
/*     */ 
/* 124 */     if ((str2 == null) && (!installedAll)) {
/* 125 */       installAll();
/* 126 */       str2 = (String)aliasTable.get(str1);
/*     */     }
/* 128 */     return str2;
/*     */   }
/*     */ 
/*     */   private static synchronized void installAll() {
/* 132 */     if (!installedAll) {
/* 133 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("sun.nio.cs.map");
/* 134 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 135 */       if (str != null)
/* 136 */         sjisIsMS932 = str.equalsIgnoreCase("Windows-31J/Shift_JIS");
/*     */       else {
/* 138 */         sjisIsMS932 = false;
/*     */       }
/*     */ 
/* 142 */       aliasTable.put("8859_1", "ISO8859_1");
/* 143 */       aliasTable.put("iso_8859-1:1987", "ISO8859_1");
/* 144 */       aliasTable.put("iso-ir-100", "ISO8859_1");
/* 145 */       aliasTable.put("iso_8859-1", "ISO8859_1");
/* 146 */       aliasTable.put("iso-8859-1", "ISO8859_1");
/* 147 */       aliasTable.put("iso8859-1", "ISO8859_1");
/* 148 */       aliasTable.put("latin1", "ISO8859_1");
/* 149 */       aliasTable.put("l1", "ISO8859_1");
/* 150 */       aliasTable.put("ibm819", "ISO8859_1");
/* 151 */       aliasTable.put("ibm-819", "ISO8859_1");
/* 152 */       aliasTable.put("cp819", "ISO8859_1");
/* 153 */       aliasTable.put("819", "ISO8859_1");
/* 154 */       aliasTable.put("csisolatin1", "ISO8859_1");
/*     */ 
/* 157 */       aliasTable.put("8859_2", "ISO8859_2");
/* 158 */       aliasTable.put("iso_8859-2:1987", "ISO8859_2");
/* 159 */       aliasTable.put("iso-ir-101", "ISO8859_2");
/* 160 */       aliasTable.put("iso_8859-2", "ISO8859_2");
/* 161 */       aliasTable.put("iso-8859-2", "ISO8859_2");
/* 162 */       aliasTable.put("iso8859-2", "ISO8859_2");
/* 163 */       aliasTable.put("latin2", "ISO8859_2");
/* 164 */       aliasTable.put("l2", "ISO8859_2");
/* 165 */       aliasTable.put("ibm912", "ISO8859_2");
/* 166 */       aliasTable.put("ibm-912", "ISO8859_2");
/* 167 */       aliasTable.put("cp912", "ISO8859_2");
/* 168 */       aliasTable.put("912", "ISO8859_2");
/* 169 */       aliasTable.put("csisolatin2", "ISO8859_2");
/*     */ 
/* 172 */       aliasTable.put("8859_3", "ISO8859_3");
/* 173 */       aliasTable.put("iso_8859-3:1988", "ISO8859_3");
/* 174 */       aliasTable.put("iso-ir-109", "ISO8859_3");
/* 175 */       aliasTable.put("iso_8859-3", "ISO8859_3");
/* 176 */       aliasTable.put("iso-8859-3", "ISO8859_3");
/* 177 */       aliasTable.put("iso8859-3", "ISO8859_3");
/* 178 */       aliasTable.put("latin3", "ISO8859_3");
/* 179 */       aliasTable.put("l3", "ISO8859_3");
/* 180 */       aliasTable.put("ibm913", "ISO8859_3");
/* 181 */       aliasTable.put("ibm-913", "ISO8859_3");
/* 182 */       aliasTable.put("cp913", "ISO8859_3");
/* 183 */       aliasTable.put("913", "ISO8859_3");
/* 184 */       aliasTable.put("csisolatin3", "ISO8859_3");
/*     */ 
/* 187 */       aliasTable.put("8859_4", "ISO8859_4");
/* 188 */       aliasTable.put("iso_8859-4:1988", "ISO8859_4");
/* 189 */       aliasTable.put("iso-ir-110", "ISO8859_4");
/* 190 */       aliasTable.put("iso_8859-4", "ISO8859_4");
/* 191 */       aliasTable.put("iso-8859-4", "ISO8859_4");
/* 192 */       aliasTable.put("iso8859-4", "ISO8859_4");
/* 193 */       aliasTable.put("latin4", "ISO8859_4");
/* 194 */       aliasTable.put("l4", "ISO8859_4");
/* 195 */       aliasTable.put("ibm914", "ISO8859_4");
/* 196 */       aliasTable.put("ibm-914", "ISO8859_4");
/* 197 */       aliasTable.put("cp914", "ISO8859_4");
/* 198 */       aliasTable.put("914", "ISO8859_4");
/* 199 */       aliasTable.put("csisolatin4", "ISO8859_4");
/*     */ 
/* 202 */       aliasTable.put("8859_5", "ISO8859_5");
/* 203 */       aliasTable.put("iso_8859-5:1988", "ISO8859_5");
/* 204 */       aliasTable.put("iso-ir-144", "ISO8859_5");
/* 205 */       aliasTable.put("iso_8859-5", "ISO8859_5");
/* 206 */       aliasTable.put("iso-8859-5", "ISO8859_5");
/* 207 */       aliasTable.put("iso8859-5", "ISO8859_5");
/* 208 */       aliasTable.put("cyrillic", "ISO8859_5");
/* 209 */       aliasTable.put("csisolatincyrillic", "ISO8859_5");
/* 210 */       aliasTable.put("ibm915", "ISO8859_5");
/* 211 */       aliasTable.put("ibm-915", "ISO8859_5");
/* 212 */       aliasTable.put("cp915", "ISO8859_5");
/* 213 */       aliasTable.put("915", "ISO8859_5");
/*     */ 
/* 216 */       aliasTable.put("8859_6", "ISO8859_6");
/* 217 */       aliasTable.put("iso_8859-6:1987", "ISO8859_6");
/* 218 */       aliasTable.put("iso-ir-127", "ISO8859_6");
/* 219 */       aliasTable.put("iso_8859-6", "ISO8859_6");
/* 220 */       aliasTable.put("iso-8859-6", "ISO8859_6");
/* 221 */       aliasTable.put("iso8859-6", "ISO8859_6");
/* 222 */       aliasTable.put("ecma-114", "ISO8859_6");
/* 223 */       aliasTable.put("asmo-708", "ISO8859_6");
/* 224 */       aliasTable.put("arabic", "ISO8859_6");
/* 225 */       aliasTable.put("csisolatinarabic", "ISO8859_6");
/* 226 */       aliasTable.put("ibm1089", "ISO8859_6");
/* 227 */       aliasTable.put("ibm-1089", "ISO8859_6");
/* 228 */       aliasTable.put("cp1089", "ISO8859_6");
/* 229 */       aliasTable.put("1089", "ISO8859_6");
/*     */ 
/* 232 */       aliasTable.put("8859_7", "ISO8859_7");
/* 233 */       aliasTable.put("iso_8859-7:1987", "ISO8859_7");
/* 234 */       aliasTable.put("iso-ir-126", "ISO8859_7");
/* 235 */       aliasTable.put("iso_8859-7", "ISO8859_7");
/* 236 */       aliasTable.put("iso-8859-7", "ISO8859_7");
/* 237 */       aliasTable.put("iso8859-7", "ISO8859_7");
/* 238 */       aliasTable.put("elot_928", "ISO8859_7");
/* 239 */       aliasTable.put("ecma-118", "ISO8859_7");
/* 240 */       aliasTable.put("greek", "ISO8859_7");
/* 241 */       aliasTable.put("greek8", "ISO8859_7");
/* 242 */       aliasTable.put("csisolatingreek", "ISO8859_7");
/* 243 */       aliasTable.put("ibm813", "ISO8859_7");
/* 244 */       aliasTable.put("ibm-813", "ISO8859_7");
/* 245 */       aliasTable.put("cp813", "ISO8859_7");
/* 246 */       aliasTable.put("813", "ISO8859_7");
/* 247 */       aliasTable.put("sun_eu_greek", "ISO8859_7");
/*     */ 
/* 250 */       aliasTable.put("8859_8", "ISO8859_8");
/* 251 */       aliasTable.put("iso_8859-8:1988", "ISO8859_8");
/* 252 */       aliasTable.put("iso-ir-138", "ISO8859_8");
/* 253 */       aliasTable.put("iso_8859-8", "ISO8859_8");
/* 254 */       aliasTable.put("iso-8859-8", "ISO8859_8");
/* 255 */       aliasTable.put("iso8859-8", "ISO8859_8");
/* 256 */       aliasTable.put("hebrew", "ISO8859_8");
/* 257 */       aliasTable.put("csisolatinhebrew", "ISO8859_8");
/* 258 */       aliasTable.put("ibm916", "ISO8859_8");
/* 259 */       aliasTable.put("ibm-916", "ISO8859_8");
/* 260 */       aliasTable.put("cp916", "ISO8859_8");
/* 261 */       aliasTable.put("916", "ISO8859_8");
/*     */ 
/* 264 */       aliasTable.put("8859_9", "ISO8859_9");
/* 265 */       aliasTable.put("iso-ir-148", "ISO8859_9");
/* 266 */       aliasTable.put("iso_8859-9", "ISO8859_9");
/* 267 */       aliasTable.put("iso-8859-9", "ISO8859_9");
/* 268 */       aliasTable.put("iso8859-9", "ISO8859_9");
/* 269 */       aliasTable.put("latin5", "ISO8859_9");
/* 270 */       aliasTable.put("l5", "ISO8859_9");
/* 271 */       aliasTable.put("ibm920", "ISO8859_9");
/* 272 */       aliasTable.put("ibm-920", "ISO8859_9");
/* 273 */       aliasTable.put("cp920", "ISO8859_9");
/* 274 */       aliasTable.put("920", "ISO8859_9");
/* 275 */       aliasTable.put("csisolatin5", "ISO8859_9");
/*     */ 
/* 278 */       aliasTable.put("8859_13", "ISO8859_13");
/* 279 */       aliasTable.put("iso_8859-13", "ISO8859_13");
/* 280 */       aliasTable.put("iso-8859-13", "ISO8859_13");
/* 281 */       aliasTable.put("iso8859-13", "ISO8859_13");
/*     */ 
/* 285 */       aliasTable.put("8859_15", "ISO8859_15");
/* 286 */       aliasTable.put("iso-8859-15", "ISO8859_15");
/* 287 */       aliasTable.put("iso_8859-15", "ISO8859_15");
/* 288 */       aliasTable.put("iso8859-15", "ISO8859_15");
/* 289 */       aliasTable.put("ibm923", "ISO8859_15");
/* 290 */       aliasTable.put("ibm-923", "ISO8859_15");
/* 291 */       aliasTable.put("cp923", "ISO8859_15");
/* 292 */       aliasTable.put("923", "ISO8859_15");
/* 293 */       aliasTable.put("latin0", "ISO8859_15");
/* 294 */       aliasTable.put("latin9", "ISO8859_15");
/* 295 */       aliasTable.put("csisolatin0", "ISO8859_15");
/* 296 */       aliasTable.put("csisolatin9", "ISO8859_15");
/*     */ 
/* 299 */       aliasTable.put("iso8859_15_fdis", "ISO8859_15");
/*     */ 
/* 302 */       aliasTable.put("utf-8", "UTF8");
/*     */ 
/* 305 */       aliasTable.put("unicode-1-1-utf-8", "UTF8");
/*     */ 
/* 308 */       aliasTable.put("unicode-1-1", "UnicodeBigUnmarked");
/*     */ 
/* 311 */       aliasTable.put("iso-10646-ucs-2", "UnicodeBigUnmarked");
/*     */ 
/* 314 */       aliasTable.put("utf-16be", "UnicodeBigUnmarked");
/* 315 */       aliasTable.put("utf-16le", "UnicodeLittleUnmarked");
/* 316 */       aliasTable.put("utf-16", "UTF16");
/*     */ 
/* 319 */       aliasTable.put("x-utf-16be", "UnicodeBigUnmarked");
/* 320 */       aliasTable.put("x-utf-16le", "UnicodeLittleUnmarked");
/*     */ 
/* 322 */       aliasTable.put("unicode", "Unicode");
/*     */ 
/* 325 */       aliasTable.put("ibm037", "Cp037");
/* 326 */       aliasTable.put("ibm-037", "Cp037");
/* 327 */       aliasTable.put("cp037", "Cp037");
/* 328 */       aliasTable.put("037", "Cp037");
/*     */ 
/* 331 */       aliasTable.put("ibm273", "Cp273");
/* 332 */       aliasTable.put("ibm-273", "Cp273");
/* 333 */       aliasTable.put("cp273", "Cp273");
/* 334 */       aliasTable.put("273", "Cp273");
/*     */ 
/* 337 */       aliasTable.put("ibm277", "Cp277");
/* 338 */       aliasTable.put("ibm-277", "Cp277");
/* 339 */       aliasTable.put("cp277", "Cp277");
/* 340 */       aliasTable.put("277", "Cp277");
/*     */ 
/* 343 */       aliasTable.put("ibm278", "Cp278");
/* 344 */       aliasTable.put("ibm-278", "Cp278");
/* 345 */       aliasTable.put("cp278", "Cp278");
/* 346 */       aliasTable.put("278", "Cp278");
/*     */ 
/* 349 */       aliasTable.put("ibm280", "Cp280");
/* 350 */       aliasTable.put("ibm-280", "Cp280");
/* 351 */       aliasTable.put("cp280", "Cp280");
/* 352 */       aliasTable.put("280", "Cp280");
/*     */ 
/* 355 */       aliasTable.put("ibm284", "Cp284");
/* 356 */       aliasTable.put("ibm-284", "Cp284");
/* 357 */       aliasTable.put("cp284", "Cp284");
/* 358 */       aliasTable.put("284", "Cp284");
/*     */ 
/* 361 */       aliasTable.put("ibm285", "Cp285");
/* 362 */       aliasTable.put("ibm-285", "Cp285");
/* 363 */       aliasTable.put("cp285", "Cp285");
/* 364 */       aliasTable.put("285", "Cp285");
/*     */ 
/* 367 */       aliasTable.put("ibm297", "Cp297");
/* 368 */       aliasTable.put("ibm-297", "Cp297");
/* 369 */       aliasTable.put("cp297", "Cp297");
/* 370 */       aliasTable.put("297", "Cp297");
/*     */ 
/* 373 */       aliasTable.put("ibm420", "Cp420");
/* 374 */       aliasTable.put("ibm-420", "Cp420");
/* 375 */       aliasTable.put("cp420", "Cp420");
/* 376 */       aliasTable.put("420", "Cp420");
/*     */ 
/* 379 */       aliasTable.put("ibm424", "Cp424");
/* 380 */       aliasTable.put("ibm-424", "Cp424");
/* 381 */       aliasTable.put("cp424", "Cp424");
/* 382 */       aliasTable.put("424", "Cp424");
/*     */ 
/* 385 */       aliasTable.put("ibm437", "Cp437");
/* 386 */       aliasTable.put("ibm-437", "Cp437");
/* 387 */       aliasTable.put("cp437", "Cp437");
/* 388 */       aliasTable.put("437", "Cp437");
/* 389 */       aliasTable.put("cspc8codepage437", "Cp437");
/*     */ 
/* 392 */       aliasTable.put("ibm500", "Cp500");
/* 393 */       aliasTable.put("ibm-500", "Cp500");
/* 394 */       aliasTable.put("cp500", "Cp500");
/* 395 */       aliasTable.put("500", "Cp500");
/*     */ 
/* 398 */       aliasTable.put("ibm737", "Cp737");
/* 399 */       aliasTable.put("ibm-737", "Cp737");
/* 400 */       aliasTable.put("cp737", "Cp737");
/* 401 */       aliasTable.put("737", "Cp737");
/*     */ 
/* 404 */       aliasTable.put("ibm775", "Cp775");
/* 405 */       aliasTable.put("ibm-775", "Cp775");
/* 406 */       aliasTable.put("cp775", "Cp775");
/* 407 */       aliasTable.put("775", "Cp775");
/*     */ 
/* 409 */       aliasTable.put("ibm833", "Cp833");
/* 410 */       aliasTable.put("ibm-833", "Cp833");
/* 411 */       aliasTable.put("cp833", "Cp833");
/* 412 */       aliasTable.put("833", "Cp833");
/*     */ 
/* 414 */       aliasTable.put("ibm834", "Cp834");
/* 415 */       aliasTable.put("ibm-834", "Cp834");
/* 416 */       aliasTable.put("cp834", "Cp834");
/* 417 */       aliasTable.put("834", "Cp834");
/*     */ 
/* 420 */       aliasTable.put("ibm838", "Cp838");
/* 421 */       aliasTable.put("ibm-838", "Cp838");
/* 422 */       aliasTable.put("cp838", "Cp838");
/* 423 */       aliasTable.put("838", "Cp838");
/*     */ 
/* 427 */       aliasTable.put("ibm850", "Cp850");
/* 428 */       aliasTable.put("ibm-850", "Cp850");
/* 429 */       aliasTable.put("cp850", "Cp850");
/* 430 */       aliasTable.put("850", "Cp850");
/* 431 */       aliasTable.put("cspc850multilingual", "Cp850");
/*     */ 
/* 435 */       aliasTable.put("ibm852", "Cp852");
/* 436 */       aliasTable.put("ibm-852", "Cp852");
/* 437 */       aliasTable.put("cp852", "Cp852");
/* 438 */       aliasTable.put("852", "Cp852");
/* 439 */       aliasTable.put("cspcp852", "Cp852");
/*     */ 
/* 443 */       aliasTable.put("ibm855", "Cp855");
/* 444 */       aliasTable.put("ibm-855", "Cp855");
/* 445 */       aliasTable.put("cp855", "Cp855");
/* 446 */       aliasTable.put("855", "Cp855");
/* 447 */       aliasTable.put("cspcp855", "Cp855");
/*     */ 
/* 451 */       aliasTable.put("ibm856", "Cp856");
/* 452 */       aliasTable.put("ibm-856", "Cp856");
/* 453 */       aliasTable.put("cp856", "Cp856");
/* 454 */       aliasTable.put("856", "Cp856");
/*     */ 
/* 458 */       aliasTable.put("ibm857", "Cp857");
/* 459 */       aliasTable.put("ibm-857", "Cp857");
/* 460 */       aliasTable.put("cp857", "Cp857");
/* 461 */       aliasTable.put("857", "Cp857");
/* 462 */       aliasTable.put("csibm857", "Cp857");
/*     */ 
/* 466 */       aliasTable.put("ibm860", "Cp860");
/* 467 */       aliasTable.put("ibm-860", "Cp860");
/* 468 */       aliasTable.put("cp860", "Cp860");
/* 469 */       aliasTable.put("860", "Cp860");
/* 470 */       aliasTable.put("csibm860", "Cp860");
/*     */ 
/* 473 */       aliasTable.put("ibm861", "Cp861");
/* 474 */       aliasTable.put("ibm-861", "Cp861");
/* 475 */       aliasTable.put("cp861", "Cp861");
/* 476 */       aliasTable.put("cp-is", "Cp861");
/* 477 */       aliasTable.put("861", "Cp861");
/* 478 */       aliasTable.put("csibm861", "Cp861");
/*     */ 
/* 481 */       aliasTable.put("ibm862", "Cp862");
/* 482 */       aliasTable.put("ibm-862", "Cp862");
/* 483 */       aliasTable.put("cp862", "Cp862");
/* 484 */       aliasTable.put("862", "Cp862");
/* 485 */       aliasTable.put("cspc862latinhebrew", "Cp862");
/*     */ 
/* 488 */       aliasTable.put("ibm863", "Cp863");
/* 489 */       aliasTable.put("ibm-863", "Cp863");
/* 490 */       aliasTable.put("cp863", "Cp863");
/* 491 */       aliasTable.put("863", "Cp863");
/* 492 */       aliasTable.put("csibm863", "Cp863");
/*     */ 
/* 495 */       aliasTable.put("ibm864", "Cp864");
/* 496 */       aliasTable.put("ibm-864", "Cp864");
/* 497 */       aliasTable.put("cp864", "Cp864");
/* 498 */       aliasTable.put("csibm864", "Cp864");
/*     */ 
/* 501 */       aliasTable.put("ibm865", "Cp865");
/* 502 */       aliasTable.put("ibm-865", "Cp865");
/* 503 */       aliasTable.put("cp865", "Cp865");
/* 504 */       aliasTable.put("865", "Cp865");
/* 505 */       aliasTable.put("csibm865", "Cp865");
/*     */ 
/* 508 */       aliasTable.put("ibm866", "Cp866");
/* 509 */       aliasTable.put("ibm-866", "Cp866");
/* 510 */       aliasTable.put("cp866", "Cp866");
/* 511 */       aliasTable.put("866", "Cp866");
/* 512 */       aliasTable.put("csibm866", "Cp866");
/*     */ 
/* 515 */       aliasTable.put("ibm868", "Cp868");
/* 516 */       aliasTable.put("ibm-868", "Cp868");
/* 517 */       aliasTable.put("cp868", "Cp868");
/* 518 */       aliasTable.put("868", "Cp868");
/*     */ 
/* 521 */       aliasTable.put("ibm869", "Cp869");
/* 522 */       aliasTable.put("ibm-869", "Cp869");
/* 523 */       aliasTable.put("cp869", "Cp869");
/* 524 */       aliasTable.put("869", "Cp869");
/* 525 */       aliasTable.put("cp-gr", "Cp869");
/* 526 */       aliasTable.put("csibm869", "Cp869");
/*     */ 
/* 529 */       aliasTable.put("ibm870", "Cp870");
/* 530 */       aliasTable.put("ibm-870", "Cp870");
/* 531 */       aliasTable.put("cp870", "Cp870");
/* 532 */       aliasTable.put("870", "Cp870");
/*     */ 
/* 535 */       aliasTable.put("ibm871", "Cp871");
/* 536 */       aliasTable.put("ibm-871", "Cp871");
/* 537 */       aliasTable.put("cp871", "Cp871");
/* 538 */       aliasTable.put("871", "Cp871");
/*     */ 
/* 541 */       aliasTable.put("ibm874", "Cp874");
/* 542 */       aliasTable.put("ibm-874", "Cp874");
/* 543 */       aliasTable.put("cp874", "Cp874");
/* 544 */       aliasTable.put("874", "Cp874");
/*     */ 
/* 547 */       aliasTable.put("ibm875", "Cp875");
/* 548 */       aliasTable.put("ibm-875", "Cp875");
/* 549 */       aliasTable.put("cp875", "Cp875");
/* 550 */       aliasTable.put("875", "Cp875");
/*     */ 
/* 553 */       aliasTable.put("ibm918", "Cp918");
/* 554 */       aliasTable.put("ibm-918", "Cp918");
/* 555 */       aliasTable.put("cp918", "Cp918");
/* 556 */       aliasTable.put("918", "Cp918");
/*     */ 
/* 559 */       aliasTable.put("ibm921", "Cp921");
/* 560 */       aliasTable.put("ibm-921", "Cp921");
/* 561 */       aliasTable.put("cp921", "Cp921");
/* 562 */       aliasTable.put("921", "Cp921");
/*     */ 
/* 565 */       aliasTable.put("ibm922", "Cp922");
/* 566 */       aliasTable.put("ibm-922", "Cp922");
/* 567 */       aliasTable.put("cp922", "Cp922");
/* 568 */       aliasTable.put("922", "Cp922");
/*     */ 
/* 571 */       aliasTable.put("ibm930", "Cp930");
/* 572 */       aliasTable.put("ibm-930", "Cp930");
/* 573 */       aliasTable.put("cp930", "Cp930");
/* 574 */       aliasTable.put("930", "Cp930");
/*     */ 
/* 577 */       aliasTable.put("ibm933", "Cp933");
/* 578 */       aliasTable.put("ibm-933", "Cp933");
/* 579 */       aliasTable.put("cp933", "Cp933");
/* 580 */       aliasTable.put("933", "Cp933");
/*     */ 
/* 583 */       aliasTable.put("ibm935", "Cp935");
/* 584 */       aliasTable.put("ibm-935", "Cp935");
/* 585 */       aliasTable.put("cp935", "Cp935");
/* 586 */       aliasTable.put("935", "Cp935");
/*     */ 
/* 589 */       aliasTable.put("ibm937", "Cp937");
/* 590 */       aliasTable.put("ibm-937", "Cp937");
/* 591 */       aliasTable.put("cp937", "Cp937");
/* 592 */       aliasTable.put("937", "Cp937");
/*     */ 
/* 595 */       aliasTable.put("ibm939", "Cp939");
/* 596 */       aliasTable.put("ibm-939", "Cp939");
/* 597 */       aliasTable.put("cp939", "Cp939");
/* 598 */       aliasTable.put("939", "Cp939");
/*     */ 
/* 601 */       aliasTable.put("ibm942", "Cp942");
/* 602 */       aliasTable.put("ibm-942", "Cp942");
/* 603 */       aliasTable.put("cp942", "Cp942");
/* 604 */       aliasTable.put("942", "Cp942");
/*     */ 
/* 607 */       aliasTable.put("ibm943", "Cp943");
/* 608 */       aliasTable.put("ibm-943", "Cp943");
/* 609 */       aliasTable.put("cp943", "Cp943");
/* 610 */       aliasTable.put("943", "Cp943");
/*     */ 
/* 613 */       aliasTable.put("ibm948", "Cp948");
/* 614 */       aliasTable.put("ibm-948", "Cp948");
/* 615 */       aliasTable.put("cp948", "Cp948");
/* 616 */       aliasTable.put("948", "Cp948");
/*     */ 
/* 619 */       aliasTable.put("ibm949", "Cp949");
/* 620 */       aliasTable.put("ibm-949", "Cp949");
/* 621 */       aliasTable.put("cp949", "Cp949");
/* 622 */       aliasTable.put("949", "Cp949");
/*     */ 
/* 625 */       aliasTable.put("ibm950", "Cp950");
/* 626 */       aliasTable.put("ibm-950", "Cp950");
/* 627 */       aliasTable.put("cp950", "Cp950");
/* 628 */       aliasTable.put("950", "Cp950");
/*     */ 
/* 631 */       aliasTable.put("ibm964", "Cp964");
/* 632 */       aliasTable.put("ibm-964", "Cp964");
/* 633 */       aliasTable.put("cp964", "Cp964");
/* 634 */       aliasTable.put("964", "Cp964");
/*     */ 
/* 637 */       aliasTable.put("ibm970", "Cp970");
/* 638 */       aliasTable.put("ibm-970", "Cp970");
/* 639 */       aliasTable.put("cp970", "Cp970");
/* 640 */       aliasTable.put("970", "Cp970");
/*     */ 
/* 643 */       aliasTable.put("ibm1006", "Cp1006");
/* 644 */       aliasTable.put("ibm-1006", "Cp1006");
/* 645 */       aliasTable.put("cp1006", "Cp1006");
/* 646 */       aliasTable.put("1006", "Cp1006");
/*     */ 
/* 649 */       aliasTable.put("ibm1025", "Cp1025");
/* 650 */       aliasTable.put("ibm-1025", "Cp1025");
/* 651 */       aliasTable.put("cp1025", "Cp1025");
/* 652 */       aliasTable.put("1025", "Cp1025");
/*     */ 
/* 655 */       aliasTable.put("ibm1026", "Cp1026");
/* 656 */       aliasTable.put("ibm-1026", "Cp1026");
/* 657 */       aliasTable.put("cp1026", "Cp1026");
/* 658 */       aliasTable.put("1026", "Cp1026");
/*     */ 
/* 661 */       aliasTable.put("ibm1097", "Cp1097");
/* 662 */       aliasTable.put("ibm-1097", "Cp1097");
/* 663 */       aliasTable.put("cp1097", "Cp1097");
/* 664 */       aliasTable.put("1097", "Cp1097");
/*     */ 
/* 667 */       aliasTable.put("ibm1098", "Cp1098");
/* 668 */       aliasTable.put("ibm-1098", "Cp1098");
/* 669 */       aliasTable.put("cp1098", "Cp1098");
/* 670 */       aliasTable.put("1098", "Cp1098");
/*     */ 
/* 673 */       aliasTable.put("ibm1112", "Cp1112");
/* 674 */       aliasTable.put("ibm-1112", "Cp1112");
/* 675 */       aliasTable.put("cp1112", "Cp1112");
/* 676 */       aliasTable.put("1112", "Cp1112");
/*     */ 
/* 679 */       aliasTable.put("ibm1122", "Cp1122");
/* 680 */       aliasTable.put("ibm-1122", "Cp1122");
/* 681 */       aliasTable.put("cp1122", "Cp1122");
/* 682 */       aliasTable.put("1122", "Cp1122");
/*     */ 
/* 685 */       aliasTable.put("ibm1123", "Cp1123");
/* 686 */       aliasTable.put("ibm-1123", "Cp1123");
/* 687 */       aliasTable.put("cp1123", "Cp1123");
/* 688 */       aliasTable.put("1123", "Cp1123");
/*     */ 
/* 691 */       aliasTable.put("ibm1124", "Cp1124");
/* 692 */       aliasTable.put("ibm-1124", "Cp1124");
/* 693 */       aliasTable.put("cp1124", "Cp1124");
/* 694 */       aliasTable.put("1124", "Cp1124");
/*     */ 
/* 697 */       aliasTable.put("ibm1381", "Cp1381");
/* 698 */       aliasTable.put("ibm-1381", "Cp1381");
/* 699 */       aliasTable.put("cp1381", "Cp1381");
/* 700 */       aliasTable.put("1381", "Cp1381");
/*     */ 
/* 703 */       aliasTable.put("ibm1383", "Cp1383");
/* 704 */       aliasTable.put("ibm-1383", "Cp1383");
/* 705 */       aliasTable.put("cp1383", "Cp1383");
/* 706 */       aliasTable.put("1383", "Cp1383");
/*     */ 
/* 709 */       aliasTable.put("jis auto detect", "JISAutoDetect");
/*     */ 
/* 712 */       aliasTable.put("jis", "ISO2022JP");
/* 713 */       aliasTable.put("iso-2022-jp", "ISO2022JP");
/* 714 */       aliasTable.put("csiso2022jp", "ISO2022JP");
/* 715 */       aliasTable.put("jis_encoding", "ISO2022JP");
/* 716 */       aliasTable.put("csjisencoding", "ISO2022JP");
/*     */ 
/* 719 */       aliasTable.put("windows-31j", "MS932");
/* 720 */       aliasTable.put("cswindows31j", "MS932");
/*     */ 
/* 723 */       aliasTable.put("シフト符号化表現", "SJIS");
/*     */ 
/* 725 */       aliasTable.put("pck", "PCK");
/*     */ 
/* 727 */       if (sjisIsMS932) {
/* 728 */         aliasTable.put("shift_jis", "MS932");
/* 729 */         aliasTable.put("csshiftjis", "MS932");
/* 730 */         aliasTable.put("x-sjis", "MS932");
/* 731 */         aliasTable.put("ms_kanji", "MS932");
/*     */       } else {
/* 733 */         aliasTable.put("shift_jis", "SJIS");
/* 734 */         aliasTable.put("csshiftjis", "SJIS");
/* 735 */         aliasTable.put("x-sjis", "SJIS");
/* 736 */         aliasTable.put("ms_kanji", "SJIS");
/*     */       }
/*     */ 
/* 741 */       aliasTable.put("eucjis", "EUC_JP");
/* 742 */       aliasTable.put("euc-jp", "EUC_JP");
/* 743 */       aliasTable.put("eucjp", "EUC_JP");
/* 744 */       aliasTable.put("extended_unix_code_packed_format_for_japanese", "EUC_JP");
/*     */ 
/* 746 */       aliasTable.put("cseucpkdfmtjapanese", "EUC_JP");
/* 747 */       aliasTable.put("x-euc-jp", "EUC_JP");
/* 748 */       aliasTable.put("x-eucjp", "EUC_JP");
/* 749 */       aliasTable.put("eucjp-open", "EUC_JP_Solaris");
/*     */ 
/* 752 */       aliasTable.put("euc-jp-linux", "EUC_JP_LINUX");
/*     */ 
/* 755 */       aliasTable.put("windows-874", "MS874");
/*     */ 
/* 758 */       aliasTable.put("windows-1250", "Cp1250");
/*     */ 
/* 761 */       aliasTable.put("windows-1251", "Cp1251");
/* 762 */       aliasTable.put("ansi-1251", "Cp1251");
/*     */ 
/* 765 */       aliasTable.put("windows-1252", "Cp1252");
/*     */ 
/* 768 */       aliasTable.put("windows-1253", "Cp1253");
/*     */ 
/* 771 */       aliasTable.put("windows-1254", "Cp1254");
/*     */ 
/* 774 */       aliasTable.put("windows-1255", "Cp1255");
/*     */ 
/* 777 */       aliasTable.put("windows-1256", "Cp1256");
/*     */ 
/* 780 */       aliasTable.put("windows-1257", "Cp1257");
/*     */ 
/* 783 */       aliasTable.put("windows-1258", "Cp1258");
/*     */ 
/* 786 */       aliasTable.put("ibm33722", "Cp33722");
/* 787 */       aliasTable.put("ibm-33722", "Cp33722");
/* 788 */       aliasTable.put("cp33722", "Cp33722");
/* 789 */       aliasTable.put("33722", "Cp33722");
/*     */ 
/* 792 */       aliasTable.put("koi8-r", "KOI8_R");
/* 793 */       aliasTable.put("koi8", "KOI8_R");
/* 794 */       aliasTable.put("cskoi8r", "KOI8_R");
/*     */ 
/* 797 */       aliasTable.put("gb2312", "EUC_CN");
/* 798 */       aliasTable.put("gb2312-80", "EUC_CN");
/* 799 */       aliasTable.put("gb2312-1980", "EUC_CN");
/* 800 */       aliasTable.put("euc-cn", "EUC_CN");
/* 801 */       aliasTable.put("euccn", "EUC_CN");
/*     */ 
/* 803 */       aliasTable.put("big5", "Big5");
/* 804 */       aliasTable.put("big5hk", "Big5_HKSCS");
/* 805 */       aliasTable.put("big5-hkscs", "Big5_HKSCS");
/*     */ 
/* 807 */       aliasTable.put("big5-hkscs:unicode3.0", "Big5_HKSCS");
/* 808 */       aliasTable.put("big5_solaris", "Big5_Solaris");
/*     */ 
/* 811 */       aliasTable.put("cns11643", "EUC_TW");
/* 812 */       aliasTable.put("euc-tw", "EUC_TW");
/* 813 */       aliasTable.put("euctw", "EUC_TW");
/*     */ 
/* 816 */       aliasTable.put("ksc5601", "EUC_KR");
/* 817 */       aliasTable.put("euc-kr", "EUC_KR");
/* 818 */       aliasTable.put("euckr", "EUC_KR");
/* 819 */       aliasTable.put("ks_c_5601-1987", "EUC_KR");
/* 820 */       aliasTable.put("ksc5601-1987", "EUC_KR");
/* 821 */       aliasTable.put("ksc5601_1987", "EUC_KR");
/* 822 */       aliasTable.put("ksc_5601", "EUC_KR");
/* 823 */       aliasTable.put("5601", "EUC_KR");
/*     */ 
/* 825 */       aliasTable.put("ksc5601-1992", "Johab");
/* 826 */       aliasTable.put("ksc5601_1992", "Johab");
/* 827 */       aliasTable.put("ms1361", "Johab");
/*     */ 
/* 829 */       aliasTable.put("windows-949", "MS949");
/*     */ 
/* 832 */       aliasTable.put("iso-2022-kr", "ISO2022KR");
/* 833 */       aliasTable.put("csiso2022kr", "ISO2022KR");
/*     */ 
/* 836 */       aliasTable.put("tis620.2533", "TIS620");
/* 837 */       aliasTable.put("tis-620", "TIS620");
/*     */ 
/* 840 */       aliasTable.put("x-compound-text", "COMPOUND_TEXT");
/* 841 */       aliasTable.put("x11-compound_text", "COMPOUND_TEXT");
/*     */ 
/* 844 */       aliasTable.put("cp942c", "Cp942C");
/* 845 */       aliasTable.put("cp943c", "Cp943C");
/* 846 */       aliasTable.put("cp949c", "Cp949C");
/* 847 */       aliasTable.put("iscii", "ISCII91");
/*     */ 
/* 849 */       installedAll = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   static String getSJISName()
/*     */   {
/* 865 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 868 */         String str = System.getProperty("os.name");
/* 869 */         if ((str.equals("Solaris")) || (str.equals("SunOS"))) {
/* 870 */           return "PCK";
/*     */         }
/*     */ 
/* 873 */         return System.getProperty("file.encoding", null);
/*     */       }
/*     */     });
/* 878 */     if (str != null) {
/* 879 */       if (str.equals("MS932"))
/* 880 */         return str;
/* 881 */       str = aliasName(str);
/*     */     }
/* 883 */     return (str != null) && (str.equals("MS932")) ? str : "SJIS";
/*     */   }
/*     */ 
/*     */   static String getEUCJPName()
/*     */   {
/* 888 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 891 */         String str = System.getProperty("os.name");
/* 892 */         if ((str.equals("Solaris")) || (str.equals("SunOS"))) {
/* 893 */           return "eucJP-open";
/*     */         }
/* 895 */         return "EUC_JP";
/*     */       }
/*     */     });
/* 898 */     return str;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  59 */     aliasTable.put("us-ascii", "ASCII");
/*  60 */     aliasTable.put("ascii", "ASCII");
/*  61 */     aliasTable.put("646", "ASCII");
/*  62 */     aliasTable.put("iso_646.irv:1983", "ASCII");
/*  63 */     aliasTable.put("ansi_x3.4-1968", "ASCII");
/*  64 */     aliasTable.put("iso646-us", "ASCII");
/*  65 */     aliasTable.put("default", "ASCII");
/*  66 */     aliasTable.put("ascii7", "ASCII");
/*     */ 
/*  69 */     aliasTable.put("8859_1", "ISO8859_1");
/*  70 */     aliasTable.put("iso8859_1", "ISO8859_1");
/*  71 */     aliasTable.put("utf-8", "UTF8");
/*  72 */     aliasTable.put("utf8", "UTF8");
/*  73 */     aliasTable.put("utf-16le", "UnicodeLittleUnmarked");
/*     */ 
/*  76 */     aliasTable.put("iso8859-1", "ISO8859_1");
/*  77 */     aliasTable.put("iso8859-2", "ISO8859_2");
/*  78 */     aliasTable.put("iso8859-4", "ISO8859_4");
/*  79 */     aliasTable.put("iso8859-5", "ISO8859_5");
/*  80 */     aliasTable.put("iso8859-6", "ISO8859_6");
/*  81 */     aliasTable.put("iso8859-8", "ISO8859_8");
/*  82 */     aliasTable.put("iso8859-9", "ISO8859_9");
/*  83 */     aliasTable.put("iso8859-13", "ISO8859_13");
/*  84 */     aliasTable.put("iso8859-15", "ISO8859_15");
/*  85 */     aliasTable.put("5601", "EUC_KR");
/*  86 */     aliasTable.put("ansi-1251", "Cp1251");
/*  87 */     aliasTable.put("big5", "Big5");
/*  88 */     aliasTable.put("big5hk", "Big5_HKSCS");
/*  89 */     aliasTable.put("eucjp", "EUC_JP");
/*  90 */     aliasTable.put("cns11643", "EUC_TW");
/*  91 */     aliasTable.put("gb2312", "EUC_CN");
/*  92 */     aliasTable.put("gb18030", "GB18030");
/*  93 */     aliasTable.put("gbk", "GBK");
/*  94 */     aliasTable.put("koi8-r", "KOI8_R");
/*  95 */     aliasTable.put("tis620.2533", "TIS620");
/*     */ 
/*  98 */     aliasTable.put("cp1250", "Cp1250");
/*  99 */     aliasTable.put("cp1251", "Cp1251");
/* 100 */     aliasTable.put("cp1252", "Cp1252");
/* 101 */     aliasTable.put("cp1253", "Cp1253");
/* 102 */     aliasTable.put("cp1254", "Cp1254");
/* 103 */     aliasTable.put("cp1255", "Cp1255");
/* 104 */     aliasTable.put("cp1256", "Cp1256");
/* 105 */     aliasTable.put("cp1257", "Cp1257");
/* 106 */     aliasTable.put("cp1258", "Cp1258");
/* 107 */     aliasTable.put("ms874", "MS874");
/* 108 */     aliasTable.put("ms932", "MS932");
/* 109 */     aliasTable.put("ms949", "MS949");
/* 110 */     aliasTable.put("ms950", "MS950");
/* 111 */     aliasTable.put("ms1361", "MS1361");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharacterEncoding
 * JD-Core Version:    0.6.2
 */