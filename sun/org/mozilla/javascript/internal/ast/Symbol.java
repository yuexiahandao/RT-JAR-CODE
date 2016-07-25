/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ import sun.org.mozilla.javascript.internal.Token;
/*     */ 
/*     */ public class Symbol
/*     */ {
/*     */   private int declType;
/*  55 */   private int index = -1;
/*     */   private String name;
/*     */   private Node node;
/*     */   private Scope containingTable;
/*     */ 
/*     */   public Symbol()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Symbol(int paramInt, String paramString)
/*     */   {
/*  69 */     setName(paramString);
/*  70 */     setDeclType(paramInt);
/*     */   }
/*     */ 
/*     */   public int getDeclType()
/*     */   {
/*  77 */     return this.declType;
/*     */   }
/*     */ 
/*     */   public void setDeclType(int paramInt)
/*     */   {
/*  84 */     if ((paramInt != 109) && (paramInt != 87) && (paramInt != 122) && (paramInt != 153) && (paramInt != 154))
/*     */     {
/*  89 */       throw new IllegalArgumentException("Invalid declType: " + paramInt);
/*  90 */     }this.declType = paramInt;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  97 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString)
/*     */   {
/* 104 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public Node getNode()
/*     */   {
/* 111 */     return this.node;
/*     */   }
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 118 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int paramInt)
/*     */   {
/* 125 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public void setNode(Node paramNode)
/*     */   {
/* 132 */     this.node = paramNode;
/*     */   }
/*     */ 
/*     */   public Scope getContainingTable()
/*     */   {
/* 139 */     return this.containingTable;
/*     */   }
/*     */ 
/*     */   public void setContainingTable(Scope paramScope)
/*     */   {
/* 146 */     this.containingTable = paramScope;
/*     */   }
/*     */ 
/*     */   public String getDeclTypeName() {
/* 150 */     return Token.typeToName(this.declType);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 155 */     StringBuilder localStringBuilder = new StringBuilder();
/* 156 */     localStringBuilder.append("Symbol (");
/* 157 */     localStringBuilder.append(getDeclTypeName());
/* 158 */     localStringBuilder.append(") name=");
/* 159 */     localStringBuilder.append(this.name);
/* 160 */     if (this.node != null) {
/* 161 */       localStringBuilder.append(" line=");
/* 162 */       localStringBuilder.append(this.node.getLineno());
/*     */     }
/* 164 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Symbol
 * JD-Core Version:    0.6.2
 */