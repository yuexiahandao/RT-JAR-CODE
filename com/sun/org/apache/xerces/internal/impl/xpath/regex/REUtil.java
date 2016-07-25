/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ public final class REUtil
/*     */ {
/*     */   static final int CACHESIZE = 20;
/* 249 */   static final RegularExpression[] regexCache = new RegularExpression[20];
/*     */ 
/*     */   static final int composeFromSurrogates(int high, int low)
/*     */   {
/*  34 */     return 65536 + (high - 55296 << 10) + low - 56320;
/*     */   }
/*     */ 
/*     */   static final boolean isLowSurrogate(int ch) {
/*  38 */     return (ch & 0xFC00) == 56320;
/*     */   }
/*     */ 
/*     */   static final boolean isHighSurrogate(int ch) {
/*  42 */     return (ch & 0xFC00) == 55296;
/*     */   }
/*     */ 
/*     */   static final String decomposeToSurrogates(int ch) {
/*  46 */     char[] chs = new char[2];
/*  47 */     ch -= 65536;
/*  48 */     chs[0] = ((char)((ch >> 10) + 55296));
/*  49 */     chs[1] = ((char)((ch & 0x3FF) + 56320));
/*  50 */     return new String(chs);
/*     */   }
/*     */ 
/*     */   static final String substring(CharacterIterator iterator, int begin, int end) {
/*  54 */     char[] src = new char[end - begin];
/*  55 */     for (int i = 0; i < src.length; i++)
/*  56 */       src[i] = iterator.setIndex(i + begin);
/*  57 */     return new String(src);
/*     */   }
/*     */ 
/*     */   static final int getOptionValue(int ch)
/*     */   {
/*  63 */     int ret = 0;
/*  64 */     switch (ch) {
/*     */     case 105:
/*  66 */       ret = 2;
/*  67 */       break;
/*     */     case 109:
/*  69 */       ret = 8;
/*  70 */       break;
/*     */     case 115:
/*  72 */       ret = 4;
/*  73 */       break;
/*     */     case 120:
/*  75 */       ret = 16;
/*  76 */       break;
/*     */     case 117:
/*  78 */       ret = 32;
/*  79 */       break;
/*     */     case 119:
/*  81 */       ret = 64;
/*  82 */       break;
/*     */     case 70:
/*  84 */       ret = 256;
/*  85 */       break;
/*     */     case 72:
/*  87 */       ret = 128;
/*  88 */       break;
/*     */     case 88:
/*  90 */       ret = 512;
/*  91 */       break;
/*     */     case 44:
/*  93 */       ret = 1024;
/*  94 */       break;
/*     */     }
/*     */ 
/*  97 */     return ret;
/*     */   }
/*     */ 
/*     */   static final int parseOptions(String opts) throws ParseException {
/* 101 */     if (opts == null) return 0;
/* 102 */     int options = 0;
/* 103 */     for (int i = 0; i < opts.length(); i++) {
/* 104 */       int v = getOptionValue(opts.charAt(i));
/* 105 */       if (v == 0)
/* 106 */         throw new ParseException("Unknown Option: " + opts.substring(i), -1);
/* 107 */       options |= v;
/*     */     }
/* 109 */     return options;
/*     */   }
/*     */ 
/*     */   static final String createOptionString(int options) {
/* 113 */     StringBuffer sb = new StringBuffer(9);
/* 114 */     if ((options & 0x100) != 0)
/* 115 */       sb.append('F');
/* 116 */     if ((options & 0x80) != 0)
/* 117 */       sb.append('H');
/* 118 */     if ((options & 0x200) != 0)
/* 119 */       sb.append('X');
/* 120 */     if ((options & 0x2) != 0)
/* 121 */       sb.append('i');
/* 122 */     if ((options & 0x8) != 0)
/* 123 */       sb.append('m');
/* 124 */     if ((options & 0x4) != 0)
/* 125 */       sb.append('s');
/* 126 */     if ((options & 0x20) != 0)
/* 127 */       sb.append('u');
/* 128 */     if ((options & 0x40) != 0)
/* 129 */       sb.append('w');
/* 130 */     if ((options & 0x10) != 0)
/* 131 */       sb.append('x');
/* 132 */     if ((options & 0x400) != 0)
/* 133 */       sb.append(',');
/* 134 */     return sb.toString().intern();
/*     */   }
/*     */ 
/*     */   static String stripExtendedComment(String regex)
/*     */   {
/* 140 */     int len = regex.length();
/* 141 */     StringBuffer buffer = new StringBuffer(len);
/* 142 */     int offset = 0;
/*     */     while (true) { if (offset >= len) break label214;
/* 144 */       int ch = regex.charAt(offset++);
/*     */ 
/* 146 */       if ((ch != 9) && (ch != 10) && (ch != 12) && (ch != 13) && (ch != 32))
/*     */       {
/* 149 */         if (ch == 35) {
/* 150 */           if (offset >= len) continue;
/* 151 */           ch = regex.charAt(offset++);
/* 152 */           if (ch == 13) continue; if (ch != 10) break;
/* 153 */           continue;
/*     */         }
/*     */ 
/* 159 */         if ((ch == 92) && (offset < len))
/*     */         {
/*     */           int next;
/* 160 */           if (((next = regex.charAt(offset)) == '#') || (next == 9) || (next == 10) || (next == 12) || (next == 13) || (next == 32))
/*     */           {
/* 163 */             buffer.append((char)next);
/* 164 */             offset++;
/*     */           } else {
/* 166 */             buffer.append('\\');
/* 167 */             buffer.append((char)next);
/* 168 */             offset++;
/*     */           }
/*     */         } else {
/* 171 */           buffer.append((char)ch);
/*     */         }
/*     */       } } label214: return buffer.toString();
/*     */   }
/*     */ 
/*     */   public static void main(String[] argv)
/*     */   {
/* 183 */     String pattern = null;
/*     */     try {
/* 185 */       String options = "";
/* 186 */       String target = null;
/* 187 */       if (argv.length == 0) {
/* 188 */         System.out.println("Error:Usage: java REUtil -i|-m|-s|-u|-w|-X regularExpression String");
/* 189 */         System.exit(0);
/*     */       }
/* 191 */       for (int i = 0; i < argv.length; i++) {
/* 192 */         if ((argv[i].length() == 0) || (argv[i].charAt(0) != '-')) {
/* 193 */           if (pattern == null)
/* 194 */             pattern = argv[i];
/* 195 */           else if (target == null)
/* 196 */             target = argv[i];
/*     */           else
/* 198 */             System.err.println("Unnecessary: " + argv[i]);
/* 199 */         } else if (argv[i].equals("-i"))
/* 200 */           options = options + "i";
/* 201 */         else if (argv[i].equals("-m"))
/* 202 */           options = options + "m";
/* 203 */         else if (argv[i].equals("-s"))
/* 204 */           options = options + "s";
/* 205 */         else if (argv[i].equals("-u"))
/* 206 */           options = options + "u";
/* 207 */         else if (argv[i].equals("-w"))
/* 208 */           options = options + "w";
/* 209 */         else if (argv[i].equals("-X"))
/* 210 */           options = options + "X";
/*     */         else {
/* 212 */           System.err.println("Unknown option: " + argv[i]);
/*     */         }
/*     */       }
/* 215 */       RegularExpression reg = new RegularExpression(pattern, options);
/* 216 */       System.out.println("RegularExpression: " + reg);
/* 217 */       Match match = new Match();
/* 218 */       reg.matches(target, match);
/* 219 */       for (int i = 0; i < match.getNumberOfGroups(); i++) {
/* 220 */         if (i == 0) System.out.print("Matched range for the whole pattern: "); else
/* 221 */           System.out.print("[" + i + "]: ");
/* 222 */         if (match.getBeginning(i) < 0) {
/* 223 */           System.out.println("-1");
/*     */         } else {
/* 225 */           System.out.print(match.getBeginning(i) + ", " + match.getEnd(i) + ", ");
/* 226 */           System.out.println("\"" + match.getCapturedText(i) + "\"");
/*     */         }
/*     */       }
/*     */     } catch (ParseException pe) {
/* 230 */       if (pattern == null) {
/* 231 */         pe.printStackTrace();
/*     */       } else {
/* 233 */         System.err.println("com.sun.org.apache.xerces.internal.utils.regex.ParseException: " + pe.getMessage());
/* 234 */         String indent = "        ";
/* 235 */         System.err.println(indent + pattern);
/* 236 */         int loc = pe.getLocation();
/* 237 */         if (loc >= 0) {
/* 238 */           System.err.print(indent);
/* 239 */           for (int i = 0; i < loc; i++) System.err.print("-");
/* 240 */           System.err.println("^");
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 244 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static RegularExpression createRegex(String pattern, String options)
/*     */     throws ParseException
/*     */   {
/* 258 */     RegularExpression re = null;
/* 259 */     int intOptions = parseOptions(options);
/* 260 */     synchronized (regexCache)
/*     */     {
/* 262 */       for (int i = 0; i < 20; i++) {
/* 263 */         RegularExpression cached = regexCache[i];
/* 264 */         if (cached == null) {
/* 265 */           i = -1;
/* 266 */           break;
/*     */         }
/* 268 */         if (cached.equals(pattern, intOptions)) {
/* 269 */           re = cached;
/* 270 */           break;
/*     */         }
/*     */       }
/* 273 */       if (re != null) {
/* 274 */         if (i != 0) {
/* 275 */           System.arraycopy(regexCache, 0, regexCache, 1, i);
/* 276 */           regexCache[0] = re;
/*     */         }
/*     */       } else {
/* 279 */         re = new RegularExpression(pattern, options);
/* 280 */         System.arraycopy(regexCache, 0, regexCache, 1, 19);
/* 281 */         regexCache[0] = re;
/*     */       }
/*     */     }
/* 284 */     return re;
/*     */   }
/*     */ 
/*     */   public static boolean matches(String regex, String target)
/*     */     throws ParseException
/*     */   {
/* 292 */     return createRegex(regex, null).matches(target);
/*     */   }
/*     */ 
/*     */   public static boolean matches(String regex, String options, String target)
/*     */     throws ParseException
/*     */   {
/* 300 */     return createRegex(regex, options).matches(target);
/*     */   }
/*     */ 
/*     */   public static String quoteMeta(String literal)
/*     */   {
/* 309 */     int len = literal.length();
/* 310 */     StringBuffer buffer = null;
/* 311 */     for (int i = 0; i < len; i++) {
/* 312 */       int ch = literal.charAt(i);
/* 313 */       if (".*+?{[()|\\^$".indexOf(ch) >= 0) {
/* 314 */         if (buffer == null) {
/* 315 */           buffer = new StringBuffer(i + (len - i) * 2);
/* 316 */           if (i > 0) buffer.append(literal.substring(0, i));
/*     */         }
/* 318 */         buffer.append('\\');
/* 319 */         buffer.append((char)ch);
/* 320 */       } else if (buffer != null) {
/* 321 */         buffer.append((char)ch);
/*     */       }
/*     */     }
/* 323 */     return buffer != null ? buffer.toString() : literal;
/*     */   }
/*     */ 
/*     */   static void dumpString(String v)
/*     */   {
/* 329 */     for (int i = 0; i < v.length(); i++) {
/* 330 */       System.out.print(Integer.toHexString(v.charAt(i)));
/* 331 */       System.out.print(" ");
/*     */     }
/* 333 */     System.out.println();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.REUtil
 * JD-Core Version:    0.6.2
 */