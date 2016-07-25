/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ class Op
/*     */ {
/*     */   static final int DOT = 0;
/*     */   static final int CHAR = 1;
/*     */   static final int RANGE = 3;
/*     */   static final int NRANGE = 4;
/*     */   static final int ANCHOR = 5;
/*     */   static final int STRING = 6;
/*     */   static final int CLOSURE = 7;
/*     */   static final int NONGREEDYCLOSURE = 8;
/*     */   static final int QUESTION = 9;
/*     */   static final int NONGREEDYQUESTION = 10;
/*     */   static final int UNION = 11;
/*     */   static final int CAPTURE = 15;
/*     */   static final int BACKREFERENCE = 16;
/*     */   static final int LOOKAHEAD = 20;
/*     */   static final int NEGATIVELOOKAHEAD = 21;
/*     */   static final int LOOKBEHIND = 22;
/*     */   static final int NEGATIVELOOKBEHIND = 23;
/*     */   static final int INDEPENDENT = 24;
/*     */   static final int MODIFIER = 25;
/*     */   static final int CONDITION = 26;
/*  51 */   static int nofinstances = 0;
/*     */   static final boolean COUNT = false;
/*     */   int type;
/* 130 */   Op next = null;
/*     */ 
/*     */   static Op createDot()
/*     */   {
/*  56 */     return new Op(0);
/*     */   }
/*     */ 
/*     */   static CharOp createChar(int data) {
/*  60 */     return new CharOp(1, data);
/*     */   }
/*     */ 
/*     */   static CharOp createAnchor(int data) {
/*  64 */     return new CharOp(5, data);
/*     */   }
/*     */ 
/*     */   static CharOp createCapture(int number, Op next) {
/*  68 */     CharOp op = new CharOp(15, number);
/*  69 */     op.next = next;
/*  70 */     return op;
/*     */   }
/*     */ 
/*     */   static UnionOp createUnion(int size)
/*     */   {
/*  75 */     return new UnionOp(11, size);
/*     */   }
/*     */ 
/*     */   static ChildOp createClosure(int id) {
/*  79 */     return new ModifierOp(7, id, -1);
/*     */   }
/*     */ 
/*     */   static ChildOp createNonGreedyClosure() {
/*  83 */     return new ChildOp(8);
/*     */   }
/*     */ 
/*     */   static ChildOp createQuestion(boolean nongreedy) {
/*  87 */     return new ChildOp(nongreedy ? 10 : 9);
/*     */   }
/*     */ 
/*     */   static RangeOp createRange(Token tok) {
/*  91 */     return new RangeOp(3, tok);
/*     */   }
/*     */ 
/*     */   static ChildOp createLook(int type, Op next, Op branch) {
/*  95 */     ChildOp op = new ChildOp(type);
/*  96 */     op.setChild(branch);
/*  97 */     op.next = next;
/*  98 */     return op;
/*     */   }
/*     */ 
/*     */   static CharOp createBackReference(int refno) {
/* 102 */     return new CharOp(16, refno);
/*     */   }
/*     */ 
/*     */   static StringOp createString(String literal) {
/* 106 */     return new StringOp(6, literal);
/*     */   }
/*     */ 
/*     */   static ChildOp createIndependent(Op next, Op branch) {
/* 110 */     ChildOp op = new ChildOp(24);
/* 111 */     op.setChild(branch);
/* 112 */     op.next = next;
/* 113 */     return op;
/*     */   }
/*     */ 
/*     */   static ModifierOp createModifier(Op next, Op branch, int add, int mask) {
/* 117 */     ModifierOp op = new ModifierOp(25, add, mask);
/* 118 */     op.setChild(branch);
/* 119 */     op.next = next;
/* 120 */     return op;
/*     */   }
/*     */ 
/*     */   static ConditionOp createCondition(Op next, int ref, Op conditionflow, Op yesflow, Op noflow) {
/* 124 */     ConditionOp op = new ConditionOp(26, ref, conditionflow, yesflow, noflow);
/* 125 */     op.next = next;
/* 126 */     return op;
/*     */   }
/*     */ 
/*     */   protected Op(int type)
/*     */   {
/* 133 */     this.type = type;
/*     */   }
/*     */ 
/*     */   int size() {
/* 137 */     return 0;
/*     */   }
/*     */   Op elementAt(int index) {
/* 140 */     throw new RuntimeException("Internal Error: type=" + this.type);
/*     */   }
/*     */   Op getChild() {
/* 143 */     throw new RuntimeException("Internal Error: type=" + this.type);
/*     */   }
/*     */ 
/*     */   int getData() {
/* 147 */     throw new RuntimeException("Internal Error: type=" + this.type);
/*     */   }
/*     */   int getData2() {
/* 150 */     throw new RuntimeException("Internal Error: type=" + this.type);
/*     */   }
/*     */   RangeToken getToken() {
/* 153 */     throw new RuntimeException("Internal Error: type=" + this.type);
/*     */   }
/*     */   String getString() {
/* 156 */     throw new RuntimeException("Internal Error: type=" + this.type);
/*     */   }
/*     */ 
/*     */   static class CharOp extends Op {
/*     */     int charData;
/*     */ 
/*     */     CharOp(int type, int data) {
/* 163 */       super();
/* 164 */       this.charData = data;
/*     */     }
/*     */     int getData() {
/* 167 */       return this.charData;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ChildOp extends Op
/*     */   {
/*     */     Op child;
/*     */ 
/*     */     ChildOp(int type)
/*     */     {
/* 193 */       super();
/*     */     }
/*     */     void setChild(Op child) {
/* 196 */       this.child = child;
/*     */     }
/*     */     Op getChild() {
/* 199 */       return this.child;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConditionOp extends Op
/*     */   {
/*     */     int refNumber;
/*     */     Op condition;
/*     */     Op yes;
/*     */     Op no;
/*     */ 
/*     */     ConditionOp(int type, int refno, Op conditionflow, Op yesflow, Op noflow)
/*     */     {
/* 247 */       super();
/* 248 */       this.refNumber = refno;
/* 249 */       this.condition = conditionflow;
/* 250 */       this.yes = yesflow;
/* 251 */       this.no = noflow;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ModifierOp extends Op.ChildOp
/*     */   {
/*     */     int v1;
/*     */     int v2;
/*     */ 
/*     */     ModifierOp(int type, int v1, int v2)
/*     */     {
/* 207 */       super();
/* 208 */       this.v1 = v1;
/* 209 */       this.v2 = v2;
/*     */     }
/*     */     int getData() {
/* 212 */       return this.v1;
/*     */     }
/*     */     int getData2() {
/* 215 */       return this.v2;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class RangeOp extends Op {
/*     */     Token tok;
/*     */ 
/* 222 */     RangeOp(int type, Token tok) { super();
/* 223 */       this.tok = tok; }
/*     */ 
/*     */     RangeToken getToken() {
/* 226 */       return (RangeToken)this.tok;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StringOp extends Op {
/*     */     String string;
/*     */ 
/* 233 */     StringOp(int type, String literal) { super();
/* 234 */       this.string = literal; }
/*     */ 
/*     */     String getString() {
/* 237 */       return this.string;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class UnionOp extends Op
/*     */   {
/*     */     Vector branches;
/*     */ 
/*     */     UnionOp(int type, int size)
/*     */     {
/* 175 */       super();
/* 176 */       this.branches = new Vector(size);
/*     */     }
/*     */     void addElement(Op op) {
/* 179 */       this.branches.addElement(op);
/*     */     }
/*     */     int size() {
/* 182 */       return this.branches.size();
/*     */     }
/*     */     Op elementAt(int index) {
/* 185 */       return (Op)this.branches.elementAt(index);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.Op
 * JD-Core Version:    0.6.2
 */