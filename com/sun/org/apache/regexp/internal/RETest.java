/*     */ package com.sun.org.apache.regexp.internal;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class RETest
/*     */ {
/*     */   static final boolean showSuccesses = false;
/*  50 */   static final String NEW_LINE = System.getProperty("line.separator");
/*     */ 
/*  53 */   REDebugCompiler compiler = new REDebugCompiler();
/*     */ 
/* 242 */   int testCount = 0;
/*     */ 
/* 247 */   int failures = 0;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  65 */       if (!test(args)) {
/*  66 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  71 */       e.printStackTrace();
/*  72 */       System.exit(1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean test(String[] args)
/*     */     throws Exception
/*     */   {
/*  83 */     RETest test = new RETest();
/*     */ 
/*  85 */     if (args.length == 2)
/*     */     {
/*  87 */       test.runInteractiveTests(args[1]);
/*     */     }
/*  89 */     else if (args.length == 1)
/*     */     {
/*  92 */       test.runAutomatedTests(args[0]);
/*     */     }
/*     */     else
/*     */     {
/*  96 */       System.out.println("Usage: RETest ([-i] [regex]) ([/path/to/testfile.txt])");
/*  97 */       System.out.println("By Default will run automated tests from file 'docs/RETest.txt' ...");
/*  98 */       System.out.println();
/*  99 */       test.runAutomatedTests("docs/RETest.txt");
/*     */     }
/* 101 */     return test.failures == 0;
/*     */   }
/*     */ 
/*     */   void runInteractiveTests(String expr)
/*     */   {
/* 117 */     RE r = new RE();
/*     */     try
/*     */     {
/* 121 */       r.setProgram(this.compiler.compile(expr));
/*     */ 
/* 124 */       say("" + NEW_LINE + "" + expr + "" + NEW_LINE + "");
/*     */ 
/* 127 */       PrintWriter writer = new PrintWriter(System.out);
/* 128 */       this.compiler.dumpProgram(writer);
/* 129 */       writer.flush();
/*     */ 
/* 131 */       boolean running = true;
/*     */ 
/* 133 */       while (running)
/*     */       {
/* 136 */         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/* 137 */         System.out.print("> ");
/* 138 */         System.out.flush();
/* 139 */         String match = br.readLine();
/*     */ 
/* 141 */         if (match != null)
/*     */         {
/* 144 */           if (r.match(match))
/*     */           {
/* 146 */             say("Match successful.");
/*     */           }
/*     */           else
/*     */           {
/* 150 */             say("Match failed.");
/*     */           }
/*     */ 
/* 154 */           showParens(r);
/*     */         }
/*     */         else
/*     */         {
/* 158 */           running = false;
/* 159 */           System.out.println();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 165 */       say("Error: " + e.toString());
/* 166 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   void die(String s)
/*     */   {
/* 176 */     say("FATAL ERROR: " + s);
/* 177 */     System.exit(-1);
/*     */   }
/*     */ 
/*     */   void fail(StringBuffer log, String s)
/*     */   {
/* 188 */     System.out.print(log.toString());
/* 189 */     fail(s);
/*     */   }
/*     */ 
/*     */   void fail(String s)
/*     */   {
/* 199 */     this.failures += 1;
/* 200 */     say("" + NEW_LINE + "");
/* 201 */     say("*******************************************************");
/* 202 */     say("*********************  FAILURE!  **********************");
/* 203 */     say("*******************************************************");
/* 204 */     say("" + NEW_LINE + "");
/* 205 */     say(s);
/* 206 */     say("");
/*     */ 
/* 208 */     if (this.compiler != null) {
/* 209 */       PrintWriter writer = new PrintWriter(System.out);
/* 210 */       this.compiler.dumpProgram(writer);
/* 211 */       writer.flush();
/* 212 */       say("" + NEW_LINE + "");
/*     */     }
/*     */   }
/*     */ 
/*     */   void say(String s)
/*     */   {
/* 222 */     System.out.println(s);
/*     */   }
/*     */ 
/*     */   void showParens(RE r)
/*     */   {
/* 232 */     for (int i = 0; i < r.getParenCount(); i++)
/*     */     {
/* 235 */       say("$" + i + " = " + r.getParen(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   void runAutomatedTests(String testDocument)
/*     */     throws Exception
/*     */   {
/* 255 */     long ms = System.currentTimeMillis();
/*     */ 
/* 258 */     testPrecompiledRE();
/* 259 */     testSplitAndGrep();
/* 260 */     testSubst();
/* 261 */     testOther();
/*     */ 
/* 264 */     File testInput = new File(testDocument);
/* 265 */     if (!testInput.exists()) {
/* 266 */       throw new Exception("Could not find: " + testDocument);
/*     */     }
/*     */ 
/* 269 */     BufferedReader br = new BufferedReader(new FileReader(testInput));
/*     */     try
/*     */     {
/* 273 */       while (br.ready())
/*     */       {
/* 275 */         RETestCase testcase = getNextTestCase(br);
/* 276 */         if (testcase != null) {
/* 277 */           testcase.runTest();
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 283 */       br.close();
/*     */     }
/*     */ 
/* 287 */     say(NEW_LINE + NEW_LINE + "Match time = " + (System.currentTimeMillis() - ms) + " ms.");
/*     */ 
/* 290 */     if (this.failures > 0) {
/* 291 */       say("*************** THERE ARE FAILURES! *******************");
/*     */     }
/* 293 */     say("Tests complete.  " + this.testCount + " tests, " + this.failures + " failure(s).");
/*     */   }
/*     */ 
/*     */   void testOther()
/*     */     throws Exception
/*     */   {
/* 303 */     RE r = new RE("(a*)b");
/* 304 */     say("Serialized/deserialized (a*)b");
/* 305 */     ByteArrayOutputStream out = new ByteArrayOutputStream(128);
/* 306 */     new ObjectOutputStream(out).writeObject(r);
/* 307 */     ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
/* 308 */     r = (RE)new ObjectInputStream(in).readObject();
/* 309 */     if (!r.match("aaab"))
/*     */     {
/* 311 */       fail("Did not match 'aaab' with deserialized RE.");
/*     */     } else {
/* 313 */       say("aaaab = true");
/* 314 */       showParens(r);
/*     */     }
/*     */ 
/* 318 */     out.reset();
/* 319 */     say("Deserialized (a*)b");
/* 320 */     new ObjectOutputStream(out).writeObject(r);
/* 321 */     in = new ByteArrayInputStream(out.toByteArray());
/* 322 */     r = (RE)new ObjectInputStream(in).readObject();
/* 323 */     if (r.getParenCount() != 0)
/*     */     {
/* 325 */       fail("Has parens after deserialization.");
/*     */     }
/* 327 */     if (!r.match("aaab"))
/*     */     {
/* 329 */       fail("Did not match 'aaab' with deserialized RE.");
/*     */     } else {
/* 331 */       say("aaaab = true");
/* 332 */       showParens(r);
/*     */     }
/*     */ 
/* 336 */     r = new RE("abc(\\w*)");
/* 337 */     say("MATCH_CASEINDEPENDENT abc(\\w*)");
/* 338 */     r.setMatchFlags(1);
/* 339 */     say("abc(d*)");
/* 340 */     if (!r.match("abcddd"))
/*     */     {
/* 342 */       fail("Did not match 'abcddd'.");
/*     */     } else {
/* 344 */       say("abcddd = true");
/* 345 */       showParens(r);
/*     */     }
/*     */ 
/* 348 */     if (!r.match("aBcDDdd"))
/*     */     {
/* 350 */       fail("Did not match 'aBcDDdd'.");
/*     */     } else {
/* 352 */       say("aBcDDdd = true");
/* 353 */       showParens(r);
/*     */     }
/*     */ 
/* 356 */     if (!r.match("ABCDDDDD"))
/*     */     {
/* 358 */       fail("Did not match 'ABCDDDDD'.");
/*     */     } else {
/* 360 */       say("ABCDDDDD = true");
/* 361 */       showParens(r);
/*     */     }
/*     */ 
/* 364 */     r = new RE("(A*)b\\1");
/* 365 */     r.setMatchFlags(1);
/* 366 */     if (!r.match("AaAaaaBAAAAAA"))
/*     */     {
/* 368 */       fail("Did not match 'AaAaaaBAAAAAA'.");
/*     */     } else {
/* 370 */       say("AaAaaaBAAAAAA = true");
/* 371 */       showParens(r);
/*     */     }
/*     */ 
/* 374 */     r = new RE("[A-Z]*");
/* 375 */     r.setMatchFlags(1);
/* 376 */     if (!r.match("CaBgDe12"))
/*     */     {
/* 378 */       fail("Did not match 'CaBgDe12'.");
/*     */     } else {
/* 380 */       say("CaBgDe12 = true");
/* 381 */       showParens(r);
/*     */     }
/*     */ 
/* 385 */     r = new RE("^abc$", 2);
/* 386 */     if (!r.match("\nabc")) {
/* 387 */       fail("\"\\nabc\" doesn't match \"^abc$\"");
/*     */     }
/* 389 */     if (!r.match("\rabc")) {
/* 390 */       fail("\"\\rabc\" doesn't match \"^abc$\"");
/*     */     }
/* 392 */     if (!r.match("\r\nabc")) {
/* 393 */       fail("\"\\r\\nabc\" doesn't match \"^abc$\"");
/*     */     }
/* 395 */     if (!r.match("abc")) {
/* 396 */       fail("\"\\u0085abc\" doesn't match \"^abc$\"");
/*     */     }
/* 398 */     if (!r.match(" abc")) {
/* 399 */       fail("\"\\u2028abc\" doesn't match \"^abc$\"");
/*     */     }
/* 401 */     if (!r.match(" abc")) {
/* 402 */       fail("\"\\u2029abc\" doesn't match \"^abc$\"");
/*     */     }
/*     */ 
/* 406 */     r = new RE("^a.*b$", 2);
/* 407 */     if (r.match("a\nb")) {
/* 408 */       fail("\"a\\nb\" matches \"^a.*b$\"");
/*     */     }
/* 410 */     if (r.match("a\rb")) {
/* 411 */       fail("\"a\\rb\" matches \"^a.*b$\"");
/*     */     }
/* 413 */     if (r.match("a\r\nb")) {
/* 414 */       fail("\"a\\r\\nb\" matches \"^a.*b$\"");
/*     */     }
/* 416 */     if (r.match("ab")) {
/* 417 */       fail("\"a\\u0085b\" matches \"^a.*b$\"");
/*     */     }
/* 419 */     if (r.match("a b")) {
/* 420 */       fail("\"a\\u2028b\" matches \"^a.*b$\"");
/*     */     }
/* 422 */     if (r.match("a b"))
/* 423 */       fail("\"a\\u2029b\" matches \"^a.*b$\"");
/*     */   }
/*     */ 
/*     */   private void testPrecompiledRE()
/*     */   {
/* 430 */     char[] re1Instructions = { '|', '\000', '\032', '|', '\000', '\r', 'A', '\001', '\004', 'a', '|', '\000', '\003', 'G', '\000', 65526, '|', '\000', '\003', 'N', '\000', '\003', 'A', '\001', '\004', 'b', 'E', '\000', '\000' };
/*     */ 
/* 439 */     REProgram re1 = new REProgram(re1Instructions);
/*     */ 
/* 442 */     RE r = new RE(re1);
/* 443 */     say("a*b");
/* 444 */     boolean result = r.match("aaab");
/* 445 */     say("aaab = " + result);
/* 446 */     showParens(r);
/* 447 */     if (!result) {
/* 448 */       fail("\"aaab\" doesn't match to precompiled \"a*b\"");
/*     */     }
/*     */ 
/* 451 */     result = r.match("b");
/* 452 */     say("b = " + result);
/* 453 */     showParens(r);
/* 454 */     if (!result) {
/* 455 */       fail("\"b\" doesn't match to precompiled \"a*b\"");
/*     */     }
/*     */ 
/* 458 */     result = r.match("c");
/* 459 */     say("c = " + result);
/* 460 */     showParens(r);
/* 461 */     if (result) {
/* 462 */       fail("\"c\" matches to precompiled \"a*b\"");
/*     */     }
/*     */ 
/* 465 */     result = r.match("ccccaaaaab");
/* 466 */     say("ccccaaaaab = " + result);
/* 467 */     showParens(r);
/* 468 */     if (!result)
/* 469 */       fail("\"ccccaaaaab\" doesn't match to precompiled \"a*b\"");
/*     */   }
/*     */ 
/*     */   private void testSplitAndGrep()
/*     */   {
/* 475 */     String[] expected = { "xxxx", "xxxx", "yyyy", "zzz" };
/* 476 */     RE r = new RE("a*b");
/* 477 */     String[] s = r.split("xxxxaabxxxxbyyyyaaabzzz");
/* 478 */     for (int i = 0; (i < expected.length) && (i < s.length); i++) {
/* 479 */       assertEquals("Wrong splitted part", expected[i], s[i]);
/*     */     }
/* 481 */     assertEquals("Wrong number of splitted parts", expected.length, s.length);
/*     */ 
/* 484 */     r = new RE("x+");
/* 485 */     expected = new String[] { "xxxx", "xxxx" };
/* 486 */     s = r.grep(s);
/* 487 */     for (int i = 0; i < s.length; i++)
/*     */     {
/* 489 */       say("s[" + i + "] = " + s[i]);
/* 490 */       assertEquals("Grep fails", expected[i], s[i]);
/*     */     }
/* 492 */     assertEquals("Wrong number of string found by grep", expected.length, s.length);
/*     */   }
/*     */ 
/*     */   private void testSubst()
/*     */   {
/* 498 */     RE r = new RE("a*b");
/* 499 */     String expected = "-foo-garply-wacky-";
/* 500 */     String actual = r.subst("aaaabfooaaabgarplyaaabwackyb", "-");
/* 501 */     assertEquals("Wrong result of substitution in \"a*b\"", expected, actual);
/*     */ 
/* 504 */     r = new RE("http://[\\.\\w\\-\\?/~_@&=%]+");
/* 505 */     actual = r.subst("visit us: http://www.apache.org!", "1234<a href=\"$0\">$0</a>", 2);
/*     */ 
/* 507 */     assertEquals("Wrong subst() result", "visit us: 1234<a href=\"http://www.apache.org\">http://www.apache.org</a>!", actual);
/*     */ 
/* 511 */     r = new RE("(.*?)=(.*)");
/* 512 */     actual = r.subst("variable=value", "$1_test_$212", 2);
/*     */ 
/* 514 */     assertEquals("Wrong subst() result", "variable_test_value12", actual);
/*     */ 
/* 517 */     r = new RE("^a$");
/* 518 */     actual = r.subst("a", "b", 2);
/*     */ 
/* 520 */     assertEquals("Wrong subst() result", "b", actual);
/*     */ 
/* 523 */     r = new RE("^a$", 2);
/* 524 */     actual = r.subst("\r\na\r\n", "b", 2);
/*     */ 
/* 526 */     assertEquals("Wrong subst() result", "\r\nb\r\n", actual);
/*     */   }
/*     */ 
/*     */   public void assertEquals(String message, String expected, String actual)
/*     */   {
/* 531 */     if (((expected != null) && (!expected.equals(actual))) || ((actual != null) && (!actual.equals(expected))))
/*     */     {
/* 534 */       fail(message + " (expected \"" + expected + "\", actual \"" + actual + "\")");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void assertEquals(String message, int expected, int actual)
/*     */   {
/* 541 */     if (expected != actual)
/* 542 */       fail(message + " (expected \"" + expected + "\", actual \"" + actual + "\")");
/*     */   }
/*     */ 
/*     */   private boolean getExpectedResult(String yesno)
/*     */   {
/* 555 */     if ("NO".equals(yesno))
/*     */     {
/* 557 */       return false;
/*     */     }
/* 559 */     if ("YES".equals(yesno))
/*     */     {
/* 561 */       return true;
/*     */     }
/*     */ 
/* 566 */     die("Test script error!");
/* 567 */     return false;
/*     */   }
/*     */ 
/*     */   private String findNextTest(BufferedReader br)
/*     */     throws IOException
/*     */   {
/* 579 */     String number = "";
/*     */ 
/* 581 */     while (br.ready())
/*     */     {
/* 583 */       number = br.readLine();
/* 584 */       if (number == null)
/*     */       {
/*     */         break;
/*     */       }
/* 588 */       number = number.trim();
/* 589 */       if (number.startsWith("#"))
/*     */       {
/*     */         break;
/*     */       }
/* 593 */       if (!number.equals(""))
/*     */       {
/* 595 */         say("Script error.  Line = " + number);
/* 596 */         System.exit(-1);
/*     */       }
/*     */     }
/* 599 */     return number;
/*     */   }
/*     */ 
/*     */   private RETestCase getNextTestCase(BufferedReader br)
/*     */     throws IOException
/*     */   {
/* 611 */     String tag = findNextTest(br);
/*     */ 
/* 614 */     if (!br.ready())
/*     */     {
/* 616 */       return null;
/*     */     }
/*     */ 
/* 620 */     String expr = br.readLine();
/*     */ 
/* 623 */     String matchAgainst = br.readLine();
/* 624 */     boolean badPattern = "ERR".equals(matchAgainst);
/* 625 */     boolean shouldMatch = false;
/* 626 */     int expectedParenCount = 0;
/* 627 */     String[] expectedParens = null;
/*     */ 
/* 629 */     if (!badPattern) {
/* 630 */       shouldMatch = getExpectedResult(br.readLine().trim());
/* 631 */       if (shouldMatch) {
/* 632 */         expectedParenCount = Integer.parseInt(br.readLine().trim());
/* 633 */         expectedParens = new String[expectedParenCount];
/* 634 */         for (int i = 0; i < expectedParenCount; i++) {
/* 635 */           expectedParens[i] = br.readLine();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 640 */     return new RETestCase(this, tag, expr, matchAgainst, badPattern, shouldMatch, expectedParens);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.RETest
 * JD-Core Version:    0.6.2
 */