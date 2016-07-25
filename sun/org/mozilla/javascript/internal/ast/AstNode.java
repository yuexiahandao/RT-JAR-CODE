/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.org.mozilla.javascript.internal.Kit;
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ import sun.org.mozilla.javascript.internal.Token;
/*     */ 
/*     */ public abstract class AstNode extends Node
/*     */   implements Comparable<AstNode>
/*     */ {
/*  97 */   protected int position = -1;
/*  98 */   protected int length = 1;
/*     */   protected AstNode parent;
/* 101 */   private static Map<Integer, String> operatorNames = new HashMap();
/*     */ 
/*     */   public AstNode()
/*     */   {
/* 165 */     super(-1);
/*     */   }
/*     */ 
/*     */   public AstNode(int paramInt)
/*     */   {
/* 173 */     this();
/* 174 */     this.position = paramInt;
/*     */   }
/*     */ 
/*     */   public AstNode(int paramInt1, int paramInt2)
/*     */   {
/* 184 */     this();
/* 185 */     this.position = paramInt1;
/* 186 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 193 */     return this.position;
/*     */   }
/*     */ 
/*     */   public void setPosition(int paramInt)
/*     */   {
/* 200 */     this.position = paramInt;
/*     */   }
/*     */ 
/*     */   public int getAbsolutePosition()
/*     */   {
/* 209 */     int i = this.position;
/* 210 */     AstNode localAstNode = this.parent;
/* 211 */     while (localAstNode != null) {
/* 212 */       i += localAstNode.getPosition();
/* 213 */       localAstNode = localAstNode.getParent();
/*     */     }
/* 215 */     return i;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 222 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(int paramInt)
/*     */   {
/* 229 */     this.length = paramInt;
/*     */   }
/*     */ 
/*     */   public void setBounds(int paramInt1, int paramInt2)
/*     */   {
/* 237 */     setPosition(paramInt1);
/* 238 */     setLength(paramInt2 - paramInt1);
/*     */   }
/*     */ 
/*     */   public void setRelative(int paramInt)
/*     */   {
/* 249 */     this.position -= paramInt;
/*     */   }
/*     */ 
/*     */   public AstNode getParent()
/*     */   {
/* 256 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public void setParent(AstNode paramAstNode)
/*     */   {
/* 265 */     if (paramAstNode == this.parent) {
/* 266 */       return;
/*     */     }
/*     */ 
/* 270 */     if (this.parent != null) {
/* 271 */       setRelative(-this.parent.getPosition());
/*     */     }
/*     */ 
/* 274 */     this.parent = paramAstNode;
/* 275 */     if (paramAstNode != null)
/* 276 */       setRelative(paramAstNode.getPosition());
/*     */   }
/*     */ 
/*     */   public void addChild(AstNode paramAstNode)
/*     */   {
/* 289 */     assertNotNull(paramAstNode);
/* 290 */     int i = paramAstNode.getPosition() + paramAstNode.getLength();
/* 291 */     setLength(i - getPosition());
/* 292 */     addChildToBack(paramAstNode);
/* 293 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstRoot getAstRoot()
/*     */   {
/* 302 */     AstNode localAstNode = this;
/* 303 */     while ((localAstNode != null) && (!(localAstNode instanceof AstRoot))) {
/* 304 */       localAstNode = localAstNode.getParent();
/*     */     }
/* 306 */     return (AstRoot)localAstNode;
/*     */   }
/*     */ 
/*     */   public abstract String toSource(int paramInt);
/*     */ 
/*     */   public String toSource()
/*     */   {
/* 329 */     return toSource(0);
/*     */   }
/*     */ 
/*     */   public String makeIndent(int paramInt)
/*     */   {
/* 337 */     StringBuilder localStringBuilder = new StringBuilder();
/* 338 */     for (int i = 0; i < paramInt; i++) {
/* 339 */       localStringBuilder.append("  ");
/*     */     }
/* 341 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String shortName()
/*     */   {
/* 349 */     String str = getClass().getName();
/* 350 */     int i = str.lastIndexOf(".");
/* 351 */     return str.substring(i + 1);
/*     */   }
/*     */ 
/*     */   public static String operatorToString(int paramInt)
/*     */   {
/* 360 */     String str = (String)operatorNames.get(Integer.valueOf(paramInt));
/* 361 */     if (str == null)
/* 362 */       throw new IllegalArgumentException("Invalid operator: " + paramInt);
/* 363 */     return str;
/*     */   }
/*     */ 
/*     */   public abstract void visit(NodeVisitor paramNodeVisitor);
/*     */ 
/*     */   public boolean hasSideEffects()
/*     */   {
/* 388 */     switch (getType()) {
/*     */     case -1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 30:
/*     */     case 31:
/*     */     case 35:
/*     */     case 37:
/*     */     case 38:
/*     */     case 50:
/*     */     case 51:
/*     */     case 56:
/*     */     case 57:
/*     */     case 64:
/*     */     case 68:
/*     */     case 69:
/*     */     case 70:
/*     */     case 72:
/*     */     case 81:
/*     */     case 82:
/*     */     case 90:
/*     */     case 91:
/*     */     case 92:
/*     */     case 93:
/*     */     case 94:
/*     */     case 95:
/*     */     case 96:
/*     */     case 97:
/*     */     case 98:
/*     */     case 99:
/*     */     case 100:
/*     */     case 101:
/*     */     case 106:
/*     */     case 107:
/*     */     case 109:
/*     */     case 110:
/*     */     case 111:
/*     */     case 112:
/*     */     case 113:
/*     */     case 114:
/*     */     case 117:
/*     */     case 118:
/*     */     case 119:
/*     */     case 120:
/*     */     case 121:
/*     */     case 122:
/*     */     case 123:
/*     */     case 124:
/*     */     case 125:
/*     */     case 129:
/*     */     case 130:
/*     */     case 131:
/*     */     case 132:
/*     */     case 134:
/*     */     case 135:
/*     */     case 139:
/*     */     case 140:
/*     */     case 141:
/*     */     case 142:
/*     */     case 153:
/*     */     case 154:
/*     */     case 158:
/*     */     case 159:
/* 456 */       return true;
/*     */     case 0:
/*     */     case 1:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 18:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/*     */     case 26:
/*     */     case 27:
/*     */     case 28:
/*     */     case 29:
/*     */     case 32:
/*     */     case 33:
/*     */     case 34:
/*     */     case 36:
/*     */     case 39:
/*     */     case 40:
/*     */     case 41:
/*     */     case 42:
/*     */     case 43:
/*     */     case 44:
/*     */     case 45:
/*     */     case 46:
/*     */     case 47:
/*     */     case 48:
/*     */     case 49:
/*     */     case 52:
/*     */     case 53:
/*     */     case 54:
/*     */     case 55:
/*     */     case 58:
/*     */     case 59:
/*     */     case 60:
/*     */     case 61:
/*     */     case 62:
/*     */     case 63:
/*     */     case 65:
/*     */     case 66:
/*     */     case 67:
/*     */     case 71:
/*     */     case 73:
/*     */     case 74:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/*     */     case 78:
/*     */     case 79:
/*     */     case 80:
/*     */     case 83:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 87:
/*     */     case 88:
/*     */     case 89:
/*     */     case 102:
/*     */     case 103:
/*     */     case 104:
/*     */     case 105:
/*     */     case 108:
/*     */     case 115:
/*     */     case 116:
/*     */     case 126:
/*     */     case 127:
/*     */     case 128:
/*     */     case 133:
/*     */     case 136:
/*     */     case 137:
/*     */     case 138:
/*     */     case 143:
/*     */     case 144:
/*     */     case 145:
/*     */     case 146:
/*     */     case 147:
/*     */     case 148:
/*     */     case 149:
/*     */     case 150:
/*     */     case 151:
/*     */     case 152:
/*     */     case 155:
/*     */     case 156:
/* 459 */     case 157: } return false;
/*     */   }
/*     */ 
/*     */   protected void assertNotNull(Object paramObject)
/*     */   {
/* 469 */     if (paramObject == null)
/* 470 */       throw new IllegalArgumentException("arg cannot be null");
/*     */   }
/*     */ 
/*     */   protected <T extends AstNode> void printList(List<T> paramList, StringBuilder paramStringBuilder)
/*     */   {
/* 480 */     int i = paramList.size();
/* 481 */     int j = 0;
/* 482 */     for (AstNode localAstNode : paramList) {
/* 483 */       paramStringBuilder.append(localAstNode.toSource(0));
/* 484 */       if (j++ < i - 1)
/* 485 */         paramStringBuilder.append(", ");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static RuntimeException codeBug()
/*     */     throws RuntimeException
/*     */   {
/* 496 */     throw Kit.codeBug();
/*     */   }
/*     */ 
/*     */   public FunctionNode getEnclosingFunction()
/*     */   {
/* 517 */     AstNode localAstNode = getParent();
/* 518 */     while ((localAstNode != null) && (!(localAstNode instanceof FunctionNode))) {
/* 519 */       localAstNode = localAstNode.getParent();
/*     */     }
/* 521 */     return (FunctionNode)localAstNode;
/*     */   }
/*     */ 
/*     */   public Scope getEnclosingScope()
/*     */   {
/* 532 */     AstNode localAstNode = getParent();
/* 533 */     while ((localAstNode != null) && (!(localAstNode instanceof Scope))) {
/* 534 */       localAstNode = localAstNode.getParent();
/*     */     }
/* 536 */     return (Scope)localAstNode;
/*     */   }
/*     */ 
/*     */   public int compareTo(AstNode paramAstNode)
/*     */   {
/* 551 */     if (equals(paramAstNode)) return 0;
/* 552 */     int i = getAbsolutePosition();
/* 553 */     int j = paramAstNode.getAbsolutePosition();
/* 554 */     if (i < j) return -1;
/* 555 */     if (j < i) return 1;
/* 556 */     int k = getLength();
/* 557 */     int m = paramAstNode.getLength();
/* 558 */     if (k < m) return -1;
/* 559 */     if (m < k) return 1;
/* 560 */     return hashCode() - paramAstNode.hashCode();
/*     */   }
/*     */ 
/*     */   public int depth()
/*     */   {
/* 569 */     return this.parent == null ? 0 : 1 + this.parent.depth();
/*     */   }
/*     */ 
/*     */   public int getLineno()
/*     */   {
/* 611 */     if (this.lineno != -1)
/* 612 */       return this.lineno;
/* 613 */     if (this.parent != null)
/* 614 */       return this.parent.getLineno();
/* 615 */     return -1;
/*     */   }
/*     */ 
/*     */   public String debugPrint()
/*     */   {
/* 625 */     DebugPrintVisitor localDebugPrintVisitor = new DebugPrintVisitor(new StringBuilder(1000));
/* 626 */     visit(localDebugPrintVisitor);
/* 627 */     return localDebugPrintVisitor.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 105 */     operatorNames.put(Integer.valueOf(52), "in");
/* 106 */     operatorNames.put(Integer.valueOf(32), "typeof");
/* 107 */     operatorNames.put(Integer.valueOf(53), "instanceof");
/* 108 */     operatorNames.put(Integer.valueOf(31), "delete");
/* 109 */     operatorNames.put(Integer.valueOf(89), ",");
/* 110 */     operatorNames.put(Integer.valueOf(103), ":");
/* 111 */     operatorNames.put(Integer.valueOf(104), "||");
/* 112 */     operatorNames.put(Integer.valueOf(105), "&&");
/* 113 */     operatorNames.put(Integer.valueOf(106), "++");
/* 114 */     operatorNames.put(Integer.valueOf(107), "--");
/* 115 */     operatorNames.put(Integer.valueOf(9), "|");
/* 116 */     operatorNames.put(Integer.valueOf(10), "^");
/* 117 */     operatorNames.put(Integer.valueOf(11), "&");
/* 118 */     operatorNames.put(Integer.valueOf(12), "==");
/* 119 */     operatorNames.put(Integer.valueOf(13), "!=");
/* 120 */     operatorNames.put(Integer.valueOf(14), "<");
/* 121 */     operatorNames.put(Integer.valueOf(16), ">");
/* 122 */     operatorNames.put(Integer.valueOf(15), "<=");
/* 123 */     operatorNames.put(Integer.valueOf(17), ">=");
/* 124 */     operatorNames.put(Integer.valueOf(18), "<<");
/* 125 */     operatorNames.put(Integer.valueOf(19), ">>");
/* 126 */     operatorNames.put(Integer.valueOf(20), ">>>");
/* 127 */     operatorNames.put(Integer.valueOf(21), "+");
/* 128 */     operatorNames.put(Integer.valueOf(22), "-");
/* 129 */     operatorNames.put(Integer.valueOf(23), "*");
/* 130 */     operatorNames.put(Integer.valueOf(24), "/");
/* 131 */     operatorNames.put(Integer.valueOf(25), "%");
/* 132 */     operatorNames.put(Integer.valueOf(26), "!");
/* 133 */     operatorNames.put(Integer.valueOf(27), "~");
/* 134 */     operatorNames.put(Integer.valueOf(28), "+");
/* 135 */     operatorNames.put(Integer.valueOf(29), "-");
/* 136 */     operatorNames.put(Integer.valueOf(46), "===");
/* 137 */     operatorNames.put(Integer.valueOf(47), "!==");
/* 138 */     operatorNames.put(Integer.valueOf(90), "=");
/* 139 */     operatorNames.put(Integer.valueOf(91), "|=");
/* 140 */     operatorNames.put(Integer.valueOf(93), "&=");
/* 141 */     operatorNames.put(Integer.valueOf(94), "<<=");
/* 142 */     operatorNames.put(Integer.valueOf(95), ">>=");
/* 143 */     operatorNames.put(Integer.valueOf(96), ">>>=");
/* 144 */     operatorNames.put(Integer.valueOf(97), "+=");
/* 145 */     operatorNames.put(Integer.valueOf(98), "-=");
/* 146 */     operatorNames.put(Integer.valueOf(99), "*=");
/* 147 */     operatorNames.put(Integer.valueOf(100), "/=");
/* 148 */     operatorNames.put(Integer.valueOf(101), "%=");
/*     */   }
/*     */ 
/*     */   protected static class DebugPrintVisitor
/*     */     implements NodeVisitor
/*     */   {
/*     */     private StringBuilder buffer;
/*     */     private static final int DEBUG_INDENT = 2;
/*     */ 
/*     */     public DebugPrintVisitor(StringBuilder paramStringBuilder)
/*     */     {
/* 576 */       this.buffer = paramStringBuilder;
/*     */     }
/*     */     public String toString() {
/* 579 */       return this.buffer.toString();
/*     */     }
/*     */     private String makeIndent(int paramInt) {
/* 582 */       StringBuilder localStringBuilder = new StringBuilder(2 * paramInt);
/* 583 */       for (int i = 0; i < 2 * paramInt; i++) {
/* 584 */         localStringBuilder.append(" ");
/*     */       }
/* 586 */       return localStringBuilder.toString();
/*     */     }
/*     */     public boolean visit(AstNode paramAstNode) {
/* 589 */       int i = paramAstNode.getType();
/* 590 */       String str = Token.typeToName(i);
/* 591 */       this.buffer.append(paramAstNode.getAbsolutePosition()).append("\t");
/* 592 */       this.buffer.append(makeIndent(paramAstNode.depth()));
/* 593 */       this.buffer.append(str).append(" ");
/* 594 */       this.buffer.append(paramAstNode.getPosition()).append(" ");
/* 595 */       this.buffer.append(paramAstNode.getLength());
/* 596 */       if (i == 39) {
/* 597 */         this.buffer.append(" ").append(((Name)paramAstNode).getIdentifier());
/*     */       }
/* 599 */       this.buffer.append("\n");
/* 600 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class PositionComparator
/*     */     implements Comparator<AstNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     public int compare(AstNode paramAstNode1, AstNode paramAstNode2)
/*     */     {
/* 160 */       return paramAstNode1.position - paramAstNode2.position;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.AstNode
 * JD-Core Version:    0.6.2
 */