/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO_W;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import java.util.Dictionary;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class TestSeq
/*     */ {
/*     */   private int _kernelType;
/*  62 */   private Vector _patterns = null;
/*     */ 
/*  67 */   private Mode _mode = null;
/*     */ 
/*  72 */   private Template _default = null;
/*     */   private InstructionList _instructionList;
/*  82 */   private InstructionHandle _start = null;
/*     */ 
/*     */   public TestSeq(Vector patterns, Mode mode)
/*     */   {
/*  88 */     this(patterns, -2, mode);
/*     */   }
/*     */ 
/*     */   public TestSeq(Vector patterns, int kernelType, Mode mode) {
/*  92 */     this._patterns = patterns;
/*  93 */     this._kernelType = kernelType;
/*  94 */     this._mode = mode;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     int count = this._patterns.size();
/* 104 */     StringBuffer result = new StringBuffer();
/*     */ 
/* 106 */     for (int i = 0; i < count; i++) {
/* 107 */       LocationPathPattern pattern = (LocationPathPattern)this._patterns.elementAt(i);
/*     */ 
/* 110 */       if (i == 0) {
/* 111 */         result.append("Testseq for kernel ").append(this._kernelType).append('\n');
/*     */       }
/*     */ 
/* 114 */       result.append("   pattern ").append(i).append(": ").append(pattern.toString()).append('\n');
/*     */     }
/*     */ 
/* 118 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public InstructionList getInstructionList()
/*     */   {
/* 125 */     return this._instructionList;
/*     */   }
/*     */ 
/*     */   public double getPriority()
/*     */   {
/* 134 */     Template template = this._patterns.size() == 0 ? this._default : ((Pattern)this._patterns.elementAt(0)).getTemplate();
/*     */ 
/* 136 */     return template.getPriority();
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 144 */     Template template = this._patterns.size() == 0 ? this._default : ((Pattern)this._patterns.elementAt(0)).getTemplate();
/*     */ 
/* 146 */     return template.getPosition();
/*     */   }
/*     */ 
/*     */   public void reduce()
/*     */   {
/* 155 */     Vector newPatterns = new Vector();
/*     */ 
/* 157 */     int count = this._patterns.size();
/* 158 */     for (int i = 0; i < count; i++) {
/* 159 */       LocationPathPattern pattern = (LocationPathPattern)this._patterns.elementAt(i);
/*     */ 
/* 163 */       pattern.reduceKernelPattern();
/*     */ 
/* 166 */       if (pattern.isWildcard()) {
/* 167 */         this._default = pattern.getTemplate();
/* 168 */         break;
/*     */       }
/*     */ 
/* 171 */       newPatterns.addElement(pattern);
/*     */     }
/*     */ 
/* 174 */     this._patterns = newPatterns;
/*     */   }
/*     */ 
/*     */   public void findTemplates(Dictionary templates)
/*     */   {
/* 183 */     if (this._default != null) {
/* 184 */       templates.put(this._default, this);
/*     */     }
/* 186 */     for (int i = 0; i < this._patterns.size(); i++) {
/* 187 */       LocationPathPattern pattern = (LocationPathPattern)this._patterns.elementAt(i);
/*     */ 
/* 189 */       templates.put(pattern.getTemplate(), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   private InstructionHandle getTemplateHandle(Template template)
/*     */   {
/* 200 */     return this._mode.getTemplateInstructionHandle(template);
/*     */   }
/*     */ 
/*     */   private LocationPathPattern getPattern(int n)
/*     */   {
/* 207 */     return (LocationPathPattern)this._patterns.elementAt(n);
/*     */   }
/*     */ 
/*     */   public InstructionHandle compile(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle continuation)
/*     */   {
/* 221 */     if (this._start != null) {
/* 222 */       return this._start;
/*     */     }
/*     */ 
/* 226 */     int count = this._patterns.size();
/* 227 */     if (count == 0) {
/* 228 */       return this._start = getTemplateHandle(this._default);
/*     */     }
/*     */ 
/* 232 */     InstructionHandle fail = this._default == null ? continuation : getTemplateHandle(this._default);
/*     */ 
/* 236 */     for (int n = count - 1; n >= 0; n--) {
/* 237 */       LocationPathPattern pattern = getPattern(n);
/* 238 */       Template template = pattern.getTemplate();
/* 239 */       InstructionList il = new InstructionList();
/*     */ 
/* 242 */       il.append(methodGen.loadCurrentNode());
/*     */ 
/* 245 */       InstructionList ilist = methodGen.getInstructionList(pattern);
/* 246 */       if (ilist == null) {
/* 247 */         ilist = pattern.compile(classGen, methodGen);
/* 248 */         methodGen.addInstructionList(pattern, ilist);
/*     */       }
/*     */ 
/* 252 */       InstructionList copyOfilist = ilist.copy();
/*     */ 
/* 254 */       FlowList trueList = pattern.getTrueList();
/* 255 */       if (trueList != null) {
/* 256 */         trueList = trueList.copyAndRedirect(ilist, copyOfilist);
/*     */       }
/* 258 */       FlowList falseList = pattern.getFalseList();
/* 259 */       if (falseList != null) {
/* 260 */         falseList = falseList.copyAndRedirect(ilist, copyOfilist);
/*     */       }
/*     */ 
/* 263 */       il.append(copyOfilist);
/*     */ 
/* 266 */       InstructionHandle gtmpl = getTemplateHandle(template);
/* 267 */       InstructionHandle success = il.append(new GOTO_W(gtmpl));
/*     */ 
/* 269 */       if (trueList != null) {
/* 270 */         trueList.backPatch(success);
/*     */       }
/* 272 */       if (falseList != null) {
/* 273 */         falseList.backPatch(fail);
/*     */       }
/*     */ 
/* 277 */       fail = il.getStart();
/*     */ 
/* 280 */       if (this._instructionList != null) {
/* 281 */         il.append(this._instructionList);
/*     */       }
/*     */ 
/* 285 */       this._instructionList = il;
/*     */     }
/* 287 */     return this._start = fail;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.TestSeq
 * JD-Core Version:    0.6.2
 */