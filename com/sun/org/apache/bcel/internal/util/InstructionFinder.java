/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ClassGenException;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.regexp.internal.RE;
/*     */ import com.sun.org.apache.regexp.internal.RESyntaxException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class InstructionFinder
/*     */ {
/*     */   private static final int OFFSET = 32767;
/*     */   private static final int NO_OPCODES = 256;
/*  96 */   private static final HashMap map = new HashMap();
/*     */   private InstructionList il;
/*     */   private String il_string;
/*     */   private InstructionHandle[] handles;
/*     */ 
/*     */   public InstructionFinder(InstructionList il)
/*     */   {
/* 106 */     this.il = il;
/* 107 */     reread();
/*     */   }
/*     */ 
/*     */   public final void reread()
/*     */   {
/* 114 */     int size = this.il.getLength();
/* 115 */     char[] buf = new char[size];
/* 116 */     this.handles = this.il.getInstructionHandles();
/*     */ 
/* 119 */     for (int i = 0; i < size; i++) {
/* 120 */       buf[i] = makeChar(this.handles[i].getInstruction().getOpcode());
/*     */     }
/* 122 */     this.il_string = new String(buf);
/*     */   }
/*     */ 
/*     */   private static final String mapName(String pattern)
/*     */   {
/* 132 */     String result = (String)map.get(pattern);
/*     */ 
/* 134 */     if (result != null) {
/* 135 */       return result;
/*     */     }
/* 137 */     for (short i = 0; i < 256; i = (short)(i + 1)) {
/* 138 */       if (pattern.equals(com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[i]))
/* 139 */         return "" + makeChar(i);
/*     */     }
/* 141 */     throw new RuntimeException("Instruction unknown: " + pattern);
/*     */   }
/*     */ 
/*     */   private static final String compilePattern(String pattern)
/*     */   {
/* 152 */     String lower = pattern.toLowerCase();
/* 153 */     StringBuffer buf = new StringBuffer();
/* 154 */     int size = pattern.length();
/*     */ 
/* 156 */     for (int i = 0; i < size; i++) {
/* 157 */       char ch = lower.charAt(i);
/*     */ 
/* 159 */       if (Character.isLetterOrDigit(ch)) {
/* 160 */         StringBuffer name = new StringBuffer();
/*     */ 
/* 162 */         while (((Character.isLetterOrDigit(ch)) || (ch == '_')) && (i < size)) {
/* 163 */           name.append(ch);
/*     */ 
/* 165 */           i++; if (i >= size) break;
/* 166 */           ch = lower.charAt(i);
/*     */         }
/*     */ 
/* 171 */         i--;
/*     */ 
/* 173 */         buf.append(mapName(name.toString()));
/* 174 */       } else if (!Character.isWhitespace(ch)) {
/* 175 */         buf.append(ch);
/*     */       }
/*     */     }
/* 178 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private InstructionHandle[] getMatch(int matched_from, int match_length)
/*     */   {
/* 185 */     InstructionHandle[] match = new InstructionHandle[match_length];
/* 186 */     System.arraycopy(this.handles, matched_from, match, 0, match_length);
/*     */ 
/* 188 */     return match;
/*     */   }
/*     */ 
/*     */   public final Iterator search(String pattern, InstructionHandle from, CodeConstraint constraint)
/*     */   {
/* 221 */     String search = compilePattern(pattern);
/* 222 */     int start = -1;
/*     */ 
/* 224 */     for (int i = 0; i < this.handles.length; i++) {
/* 225 */       if (this.handles[i] == from) {
/* 226 */         start = i;
/* 227 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 231 */     if (start == -1)
/* 232 */       throw new ClassGenException("Instruction handle " + from + " not found in instruction list.");
/*     */     try
/*     */     {
/* 235 */       RE regex = new RE(search);
/* 236 */       ArrayList matches = new ArrayList();
/*     */ 
/* 238 */       while ((start < this.il_string.length()) && (regex.match(this.il_string, start))) {
/* 239 */         int startExpr = regex.getParenStart(0);
/* 240 */         int endExpr = regex.getParenEnd(0);
/* 241 */         int lenExpr = regex.getParenLength(0);
/*     */ 
/* 243 */         InstructionHandle[] match = getMatch(startExpr, lenExpr);
/*     */ 
/* 245 */         if ((constraint == null) || (constraint.checkCode(match)))
/* 246 */           matches.add(match);
/* 247 */         start = endExpr;
/*     */       }
/*     */ 
/* 250 */       return matches.iterator();
/*     */     } catch (RESyntaxException e) {
/* 252 */       System.err.println(e);
/*     */     }
/*     */ 
/* 255 */     return null;
/*     */   }
/*     */ 
/*     */   public final Iterator search(String pattern)
/*     */   {
/* 267 */     return search(pattern, this.il.getStart(), null);
/*     */   }
/*     */ 
/*     */   public final Iterator search(String pattern, InstructionHandle from)
/*     */   {
/* 279 */     return search(pattern, from, null);
/*     */   }
/*     */ 
/*     */   public final Iterator search(String pattern, CodeConstraint constraint)
/*     */   {
/* 291 */     return search(pattern, this.il.getStart(), constraint);
/*     */   }
/*     */ 
/*     */   private static final char makeChar(short opcode)
/*     */   {
/* 298 */     return (char)(opcode + 32767);
/*     */   }
/*     */ 
/*     */   public final InstructionList getInstructionList()
/*     */   {
/* 304 */     return this.il;
/*     */   }
/*     */ 
/*     */   private static String precompile(short from, short to, short extra)
/*     */   {
/* 407 */     StringBuffer buf = new StringBuffer("(");
/*     */ 
/* 409 */     for (short i = from; i <= to; i = (short)(i + 1)) {
/* 410 */       buf.append(makeChar(i));
/* 411 */       buf.append('|');
/*     */     }
/*     */ 
/* 414 */     buf.append(makeChar(extra));
/* 415 */     buf.append(")");
/* 416 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private static final String pattern2string(String pattern)
/*     */   {
/* 423 */     return pattern2string(pattern, true);
/*     */   }
/*     */ 
/*     */   private static final String pattern2string(String pattern, boolean make_string) {
/* 427 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 429 */     for (int i = 0; i < pattern.length(); i++) {
/* 430 */       char ch = pattern.charAt(i);
/*     */ 
/* 432 */       if (ch >= '翿') {
/* 433 */         if (make_string)
/* 434 */           buf.append(com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[(ch - '翿')]);
/*     */         else
/* 436 */           buf.append(ch - '翿');
/*     */       }
/* 438 */       else buf.append(ch);
/*     */     }
/*     */ 
/* 441 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 323 */     map.put("arithmeticinstruction", "(irem|lrem|iand|ior|ineg|isub|lneg|fneg|fmul|ldiv|fadd|lxor|frem|idiv|land|ixor|ishr|fsub|lshl|fdiv|iadd|lor|dmul|lsub|ishl|imul|lmul|lushr|dneg|iushr|lshr|ddiv|drem|dadd|ladd|dsub)");
/* 324 */     map.put("invokeinstruction", "(invokevirtual|invokeinterface|invokestatic|invokespecial)");
/* 325 */     map.put("arrayinstruction", "(baload|aastore|saload|caload|fastore|lastore|iaload|castore|iastore|aaload|bastore|sastore|faload|laload|daload|dastore)");
/* 326 */     map.put("gotoinstruction", "(goto|goto_w)");
/* 327 */     map.put("conversioninstruction", "(d2l|l2d|i2s|d2i|l2i|i2b|l2f|d2f|f2i|i2d|i2l|f2d|i2c|f2l|i2f)");
/* 328 */     map.put("localvariableinstruction", "(fstore|iinc|lload|dstore|dload|iload|aload|astore|istore|fload|lstore)");
/* 329 */     map.put("loadinstruction", "(fload|dload|lload|iload|aload)");
/* 330 */     map.put("fieldinstruction", "(getfield|putstatic|getstatic|putfield)");
/* 331 */     map.put("cpinstruction", "(ldc2_w|invokeinterface|multianewarray|putstatic|instanceof|getstatic|checkcast|getfield|invokespecial|ldc_w|invokestatic|invokevirtual|putfield|ldc|new|anewarray)");
/* 332 */     map.put("stackinstruction", "(dup2|swap|dup2_x2|pop|pop2|dup|dup2_x1|dup_x2|dup_x1)");
/* 333 */     map.put("branchinstruction", "(ifle|if_acmpne|if_icmpeq|if_acmpeq|ifnonnull|goto_w|iflt|ifnull|if_icmpne|tableswitch|if_icmple|ifeq|if_icmplt|jsr_w|if_icmpgt|ifgt|jsr|goto|ifne|ifge|lookupswitch|if_icmpge)");
/* 334 */     map.put("returninstruction", "(lreturn|ireturn|freturn|dreturn|areturn|return)");
/* 335 */     map.put("storeinstruction", "(istore|fstore|dstore|astore|lstore)");
/* 336 */     map.put("select", "(tableswitch|lookupswitch)");
/* 337 */     map.put("ifinstruction", "(ifeq|ifgt|if_icmpne|if_icmpeq|ifge|ifnull|ifne|if_icmple|if_icmpge|if_acmpeq|if_icmplt|if_acmpne|ifnonnull|iflt|if_icmpgt|ifle)");
/* 338 */     map.put("jsrinstruction", "(jsr|jsr_w)");
/* 339 */     map.put("variablelengthinstruction", "(tableswitch|jsr|goto|lookupswitch)");
/* 340 */     map.put("unconditionalbranch", "(goto|jsr|jsr_w|athrow|goto_w)");
/* 341 */     map.put("constantpushinstruction", "(dconst|bipush|sipush|fconst|iconst|lconst)");
/* 342 */     map.put("typedinstruction", "(imul|lsub|aload|fload|lor|new|aaload|fcmpg|iand|iaload|lrem|idiv|d2l|isub|dcmpg|dastore|ret|f2d|f2i|drem|iinc|i2c|checkcast|frem|lreturn|astore|lushr|daload|dneg|fastore|istore|lshl|ldiv|lstore|areturn|ishr|ldc_w|invokeinterface|aastore|lxor|ishl|l2d|i2f|return|faload|sipush|iushr|caload|instanceof|invokespecial|putfield|fmul|ireturn|laload|d2f|lneg|ixor|i2l|fdiv|lastore|multianewarray|i2b|getstatic|i2d|putstatic|fcmpl|saload|ladd|irem|dload|jsr_w|dconst|dcmpl|fsub|freturn|ldc|aconst_null|castore|lmul|ldc2_w|dadd|iconst|f2l|ddiv|dstore|land|jsr|anewarray|dmul|bipush|dsub|sastore|d2i|i2s|lshr|iadd|l2i|lload|bastore|fstore|fneg|iload|fadd|baload|fconst|ior|ineg|dreturn|l2f|lconst|getfield|invokevirtual|invokestatic|iastore)");
/* 343 */     map.put("popinstruction", "(fstore|dstore|pop|pop2|astore|putstatic|istore|lstore)");
/* 344 */     map.put("allocationinstruction", "(multianewarray|new|anewarray|newarray)");
/* 345 */     map.put("indexedinstruction", "(lload|lstore|fload|ldc2_w|invokeinterface|multianewarray|astore|dload|putstatic|instanceof|getstatic|checkcast|getfield|invokespecial|dstore|istore|iinc|ldc_w|ret|fstore|invokestatic|iload|putfield|invokevirtual|ldc|new|aload|anewarray)");
/* 346 */     map.put("pushinstruction", "(dup|lload|dup2|bipush|fload|ldc2_w|sipush|lconst|fconst|dload|getstatic|ldc_w|aconst_null|dconst|iload|ldc|iconst|aload)");
/* 347 */     map.put("stackproducer", "(imul|lsub|aload|fload|lor|new|aaload|fcmpg|iand|iaload|lrem|idiv|d2l|isub|dcmpg|dup|f2d|f2i|drem|i2c|checkcast|frem|lushr|daload|dneg|lshl|ldiv|ishr|ldc_w|invokeinterface|lxor|ishl|l2d|i2f|faload|sipush|iushr|caload|instanceof|invokespecial|fmul|laload|d2f|lneg|ixor|i2l|fdiv|getstatic|i2b|swap|i2d|dup2|fcmpl|saload|ladd|irem|dload|jsr_w|dconst|dcmpl|fsub|ldc|arraylength|aconst_null|tableswitch|lmul|ldc2_w|iconst|dadd|f2l|ddiv|land|jsr|anewarray|dmul|bipush|dsub|d2i|newarray|i2s|lshr|iadd|lload|l2i|fneg|iload|fadd|baload|fconst|lookupswitch|ior|ineg|lconst|l2f|getfield|invokevirtual|invokestatic)");
/* 348 */     map.put("stackconsumer", "(imul|lsub|lor|iflt|fcmpg|if_icmpgt|iand|ifeq|if_icmplt|lrem|ifnonnull|idiv|d2l|isub|dcmpg|dastore|if_icmpeq|f2d|f2i|drem|i2c|checkcast|frem|lreturn|astore|lushr|pop2|monitorexit|dneg|fastore|istore|lshl|ldiv|lstore|areturn|if_icmpge|ishr|monitorenter|invokeinterface|aastore|lxor|ishl|l2d|i2f|return|iushr|instanceof|invokespecial|fmul|ireturn|d2f|lneg|ixor|pop|i2l|ifnull|fdiv|lastore|i2b|if_acmpeq|ifge|swap|i2d|putstatic|fcmpl|ladd|irem|dcmpl|fsub|freturn|ifgt|castore|lmul|dadd|f2l|ddiv|dstore|land|if_icmpne|if_acmpne|dmul|dsub|sastore|ifle|d2i|i2s|lshr|iadd|l2i|bastore|fstore|fneg|fadd|ior|ineg|ifne|dreturn|l2f|if_icmple|getfield|invokevirtual|invokestatic|iastore)");
/* 349 */     map.put("exceptionthrower", "(irem|lrem|laload|putstatic|baload|dastore|areturn|getstatic|ldiv|anewarray|iastore|castore|idiv|saload|lastore|fastore|putfield|lreturn|caload|getfield|return|aastore|freturn|newarray|instanceof|multianewarray|athrow|faload|iaload|aaload|dreturn|monitorenter|checkcast|bastore|arraylength|new|invokevirtual|sastore|ldc_w|ireturn|invokespecial|monitorexit|invokeinterface|ldc|invokestatic|daload)");
/* 350 */     map.put("loadclass", "(multianewarray|invokeinterface|instanceof|invokespecial|putfield|checkcast|putstatic|invokevirtual|new|getstatic|invokestatic|getfield|anewarray)");
/* 351 */     map.put("instructiontargeter", "(ifle|if_acmpne|if_icmpeq|if_acmpeq|ifnonnull|goto_w|iflt|ifnull|if_icmpne|tableswitch|if_icmple|ifeq|if_icmplt|jsr_w|if_icmpgt|ifgt|jsr|goto|ifne|ifge|lookupswitch|if_icmpge)");
/*     */ 
/* 354 */     map.put("if_icmp", "(if_icmpne|if_icmpeq|if_icmple|if_icmpge|if_icmplt|if_icmpgt)");
/* 355 */     map.put("if_acmp", "(if_acmpeq|if_acmpne)");
/* 356 */     map.put("if", "(ifeq|ifne|iflt|ifge|ifgt|ifle)");
/*     */ 
/* 359 */     map.put("iconst", precompile((short)3, (short)8, (short)2));
/* 360 */     map.put("lconst", new String(new char[] { '(', makeChar(9), '|', makeChar(10), ')' }));
/*     */ 
/* 362 */     map.put("dconst", new String(new char[] { '(', makeChar(14), '|', makeChar(15), ')' }));
/*     */ 
/* 364 */     map.put("fconst", new String(new char[] { '(', makeChar(11), '|', makeChar(12), ')' }));
/*     */ 
/* 367 */     map.put("iload", precompile((short)26, (short)29, (short)21));
/* 368 */     map.put("dload", precompile((short)38, (short)41, (short)24));
/* 369 */     map.put("fload", precompile((short)34, (short)37, (short)23));
/* 370 */     map.put("aload", precompile((short)42, (short)45, (short)25));
/*     */ 
/* 372 */     map.put("istore", precompile((short)59, (short)62, (short)54));
/* 373 */     map.put("dstore", precompile((short)71, (short)74, (short)57));
/* 374 */     map.put("fstore", precompile((short)67, (short)70, (short)56));
/* 375 */     map.put("astore", precompile((short)75, (short)78, (short)58));
/*     */ 
/* 379 */     for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
/* 380 */       String key = (String)i.next();
/* 381 */       String value = (String)map.get(key);
/*     */ 
/* 383 */       char ch = value.charAt(1);
/* 384 */       if (ch < '翿') {
/* 385 */         map.put(key, compilePattern(value));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 391 */     StringBuffer buf = new StringBuffer("(");
/*     */ 
/* 393 */     for (short i = 0; i < 256; i = (short)(i + 1)) {
/* 394 */       if (com.sun.org.apache.bcel.internal.Constants.NO_OF_OPERANDS[i] != -1) {
/* 395 */         buf.append(makeChar(i));
/*     */ 
/* 397 */         if (i < 255)
/* 398 */           buf.append('|');
/*     */       }
/*     */     }
/* 401 */     buf.append(')');
/*     */ 
/* 403 */     map.put("instruction", buf.toString());
/*     */   }
/*     */ 
/*     */   public static abstract interface CodeConstraint
/*     */   {
/*     */     public abstract boolean checkCode(InstructionHandle[] paramArrayOfInstructionHandle);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.InstructionFinder
 * JD-Core Version:    0.6.2
 */