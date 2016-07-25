/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import com.sun.org.apache.bcel.internal.generic.AllocationInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.ArrayInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.CPInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.CodeExceptionGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.EmptyVisitor;
/*     */ import com.sun.org.apache.bcel.internal.generic.FieldInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.IINC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.InvokeInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.LDC;
/*     */ import com.sun.org.apache.bcel.internal.generic.LDC2_W;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
/*     */ import com.sun.org.apache.bcel.internal.generic.MethodGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
/*     */ import com.sun.org.apache.bcel.internal.generic.ObjectType;
/*     */ import com.sun.org.apache.bcel.internal.generic.RET;
/*     */ import com.sun.org.apache.bcel.internal.generic.ReturnInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.Select;
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ class BCELFactory extends EmptyVisitor
/*     */ {
/*     */   private MethodGen _mg;
/*     */   private PrintWriter _out;
/*     */   private ConstantPoolGen _cp;
/*  85 */   private HashMap branch_map = new HashMap();
/*     */ 
/* 260 */   private ArrayList branches = new ArrayList();
/*     */ 
/*     */   BCELFactory(MethodGen mg, PrintWriter out)
/*     */   {
/*  80 */     this._mg = mg;
/*  81 */     this._cp = mg.getConstantPool();
/*  82 */     this._out = out;
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  88 */     if ((!this._mg.isAbstract()) && (!this._mg.isNative())) {
/*  89 */       for (InstructionHandle ih = this._mg.getInstructionList().getStart(); 
/*  90 */         ih != null; ih = ih.getNext()) {
/*  91 */         Instruction i = ih.getInstruction();
/*     */ 
/*  93 */         if ((i instanceof BranchInstruction)) {
/*  94 */           this.branch_map.put(i, ih);
/*     */         }
/*     */ 
/*  97 */         if (ih.hasTargeters()) {
/*  98 */           if ((i instanceof BranchInstruction))
/*  99 */             this._out.println("    InstructionHandle ih_" + ih.getPosition() + ";");
/*     */           else
/* 101 */             this._out.print("    InstructionHandle ih_" + ih.getPosition() + " = ");
/*     */         }
/*     */         else {
/* 104 */           this._out.print("    ");
/*     */         }
/*     */ 
/* 107 */         if (!visitInstruction(i)) {
/* 108 */           i.accept(this);
/*     */         }
/*     */       }
/* 111 */       updateBranchTargets();
/* 112 */       updateExceptionHandlers();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean visitInstruction(Instruction i) {
/* 117 */     short opcode = i.getOpcode();
/*     */ 
/* 119 */     if ((com.sun.org.apache.bcel.internal.generic.InstructionConstants.INSTRUCTIONS[opcode] != null) && (!(i instanceof ConstantPushInstruction)) && (!(i instanceof ReturnInstruction)))
/*     */     {
/* 122 */       this._out.println("il.append(InstructionConstants." + i.getName().toUpperCase() + ");");
/*     */ 
/* 124 */       return true;
/*     */     }
/*     */ 
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   public void visitLocalVariableInstruction(LocalVariableInstruction i) {
/* 131 */     short opcode = i.getOpcode();
/* 132 */     Type type = i.getType(this._cp);
/*     */ 
/* 134 */     if (opcode == 132) {
/* 135 */       this._out.println("il.append(new IINC(" + i.getIndex() + ", " + ((IINC)i).getIncrement() + "));");
/*     */     }
/*     */     else {
/* 138 */       String kind = opcode < 54 ? "Load" : "Store";
/* 139 */       this._out.println("il.append(_factory.create" + kind + "(" + BCELifier.printType(type) + ", " + i.getIndex() + "));");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitArrayInstruction(ArrayInstruction i)
/*     */   {
/* 146 */     short opcode = i.getOpcode();
/* 147 */     Type type = i.getType(this._cp);
/* 148 */     String kind = opcode < 79 ? "Load" : "Store";
/*     */ 
/* 150 */     this._out.println("il.append(_factory.createArray" + kind + "(" + BCELifier.printType(type) + "));");
/*     */   }
/*     */ 
/*     */   public void visitFieldInstruction(FieldInstruction i)
/*     */   {
/* 155 */     short opcode = i.getOpcode();
/*     */ 
/* 157 */     String class_name = i.getClassName(this._cp);
/* 158 */     String field_name = i.getFieldName(this._cp);
/* 159 */     Type type = i.getFieldType(this._cp);
/*     */ 
/* 161 */     this._out.println("il.append(_factory.createFieldAccess(\"" + class_name + "\", \"" + field_name + "\", " + BCELifier.printType(type) + ", " + "Constants." + com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[opcode].toUpperCase() + "));");
/*     */   }
/*     */ 
/*     */   public void visitInvokeInstruction(InvokeInstruction i)
/*     */   {
/* 169 */     short opcode = i.getOpcode();
/* 170 */     String class_name = i.getClassName(this._cp);
/* 171 */     String method_name = i.getMethodName(this._cp);
/* 172 */     Type type = i.getReturnType(this._cp);
/* 173 */     Type[] arg_types = i.getArgumentTypes(this._cp);
/*     */ 
/* 175 */     this._out.println("il.append(_factory.createInvoke(\"" + class_name + "\", \"" + method_name + "\", " + BCELifier.printType(type) + ", " + BCELifier.printArgumentTypes(arg_types) + ", " + "Constants." + com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[opcode].toUpperCase() + "));");
/*     */   }
/*     */ 
/*     */   public void visitAllocationInstruction(AllocationInstruction i)
/*     */   {
/*     */     Type type;
/*     */     Type type;
/* 186 */     if ((i instanceof CPInstruction))
/* 187 */       type = ((CPInstruction)i).getType(this._cp);
/*     */     else {
/* 189 */       type = ((NEWARRAY)i).getType();
/*     */     }
/*     */ 
/* 192 */     short opcode = ((Instruction)i).getOpcode();
/* 193 */     int dim = 1;
/*     */ 
/* 195 */     switch (opcode) {
/*     */     case 187:
/* 197 */       this._out.println("il.append(_factory.createNew(\"" + ((ObjectType)type).getClassName() + "\"));");
/*     */ 
/* 199 */       break;
/*     */     case 197:
/* 202 */       dim = ((MULTIANEWARRAY)i).getDimensions();
/*     */     case 188:
/*     */     case 189:
/* 206 */       this._out.println("il.append(_factory.createNewArray(" + BCELifier.printType(type) + ", (short) " + dim + "));");
/*     */ 
/* 208 */       break;
/*     */     default:
/* 211 */       throw new RuntimeException("Oops: " + opcode);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void createConstant(Object value) {
/* 216 */     String embed = value.toString();
/*     */ 
/* 218 */     if ((value instanceof String))
/* 219 */       embed = '"' + Utility.convertString(value.toString()) + '"';
/* 220 */     else if ((value instanceof Character)) {
/* 221 */       embed = "(char)0x" + Integer.toHexString(((Character)value).charValue());
/*     */     }
/* 223 */     this._out.println("il.append(new PUSH(_cp, " + embed + "));");
/*     */   }
/*     */ 
/*     */   public void visitLDC(LDC i) {
/* 227 */     createConstant(i.getValue(this._cp));
/*     */   }
/*     */ 
/*     */   public void visitLDC2_W(LDC2_W i) {
/* 231 */     createConstant(i.getValue(this._cp));
/*     */   }
/*     */ 
/*     */   public void visitConstantPushInstruction(ConstantPushInstruction i) {
/* 235 */     createConstant(i.getValue());
/*     */   }
/*     */ 
/*     */   public void visitINSTANCEOF(INSTANCEOF i) {
/* 239 */     Type type = i.getType(this._cp);
/*     */ 
/* 241 */     this._out.println("il.append(new INSTANCEOF(_cp.addClass(" + BCELifier.printType(type) + ")));");
/*     */   }
/*     */ 
/*     */   public void visitCHECKCAST(CHECKCAST i)
/*     */   {
/* 246 */     Type type = i.getType(this._cp);
/*     */ 
/* 248 */     this._out.println("il.append(_factory.createCheckCast(" + BCELifier.printType(type) + "));");
/*     */   }
/*     */ 
/*     */   public void visitReturnInstruction(ReturnInstruction i)
/*     */   {
/* 253 */     Type type = i.getType(this._cp);
/*     */ 
/* 255 */     this._out.println("il.append(_factory.createReturn(" + BCELifier.printType(type) + "));");
/*     */   }
/*     */ 
/*     */   public void visitBranchInstruction(BranchInstruction bi)
/*     */   {
/* 263 */     BranchHandle bh = (BranchHandle)this.branch_map.get(bi);
/* 264 */     int pos = bh.getPosition();
/* 265 */     String name = bi.getName() + "_" + pos;
/*     */ 
/* 267 */     if ((bi instanceof Select)) {
/* 268 */       Select s = (Select)bi;
/* 269 */       this.branches.add(bi);
/*     */ 
/* 271 */       StringBuffer args = new StringBuffer("new int[] { ");
/* 272 */       int[] matchs = s.getMatchs();
/*     */ 
/* 274 */       for (int i = 0; i < matchs.length; i++) {
/* 275 */         args.append(matchs[i]);
/*     */ 
/* 277 */         if (i < matchs.length - 1) {
/* 278 */           args.append(", ");
/*     */         }
/*     */       }
/* 281 */       args.append(" }");
/*     */ 
/* 283 */       this._out.print("    Select " + name + " = new " + bi.getName().toUpperCase() + "(" + args + ", new InstructionHandle[] { ");
/*     */ 
/* 287 */       for (int i = 0; i < matchs.length; i++) {
/* 288 */         this._out.print("null");
/*     */ 
/* 290 */         if (i < matchs.length - 1) {
/* 291 */           this._out.print(", ");
/*     */         }
/*     */       }
/* 294 */       this._out.println(");");
/*     */     } else {
/* 296 */       int t_pos = bh.getTarget().getPosition();
/*     */       String target;
/*     */       String target;
/* 299 */       if (pos > t_pos) {
/* 300 */         target = "ih_" + t_pos;
/*     */       } else {
/* 302 */         this.branches.add(bi);
/* 303 */         target = "null";
/*     */       }
/*     */ 
/* 306 */       this._out.println("    BranchInstruction " + name + " = _factory.createBranchInstruction(" + "Constants." + bi.getName().toUpperCase() + ", " + target + ");");
/*     */     }
/*     */ 
/* 312 */     if (bh.hasTargeters())
/* 313 */       this._out.println("    ih_" + pos + " = il.append(" + name + ");");
/*     */     else
/* 315 */       this._out.println("    il.append(" + name + ");");
/*     */   }
/*     */ 
/*     */   public void visitRET(RET i) {
/* 319 */     this._out.println("il.append(new RET(" + i.getIndex() + ")));");
/*     */   }
/*     */ 
/*     */   private void updateBranchTargets() {
/* 323 */     for (Iterator i = this.branches.iterator(); i.hasNext(); ) {
/* 324 */       BranchInstruction bi = (BranchInstruction)i.next();
/* 325 */       BranchHandle bh = (BranchHandle)this.branch_map.get(bi);
/* 326 */       int pos = bh.getPosition();
/* 327 */       String name = bi.getName() + "_" + pos;
/* 328 */       int t_pos = bh.getTarget().getPosition();
/*     */ 
/* 330 */       this._out.println("    " + name + ".setTarget(ih_" + t_pos + ");");
/*     */ 
/* 332 */       if ((bi instanceof Select)) {
/* 333 */         InstructionHandle[] ihs = ((Select)bi).getTargets();
/*     */ 
/* 335 */         for (int j = 0; j < ihs.length; j++) {
/* 336 */           t_pos = ihs[j].getPosition();
/*     */ 
/* 338 */           this._out.println("    " + name + ".setTarget(" + j + ", ih_" + t_pos + ");");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateExceptionHandlers()
/*     */   {
/* 346 */     CodeExceptionGen[] handlers = this._mg.getExceptionHandlers();
/*     */ 
/* 348 */     for (int i = 0; i < handlers.length; i++) {
/* 349 */       CodeExceptionGen h = handlers[i];
/* 350 */       String type = h.getCatchType() == null ? "null" : BCELifier.printType(h.getCatchType());
/*     */ 
/* 353 */       this._out.println("    method.addExceptionHandler(ih_" + h.getStartPC().getPosition() + ", " + "ih_" + h.getEndPC().getPosition() + ", " + "ih_" + h.getHandlerPC().getPosition() + ", " + type + ");");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.BCELFactory
 * JD-Core Version:    0.6.2
 */