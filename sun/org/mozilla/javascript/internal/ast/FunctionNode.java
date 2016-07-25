/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ 
/*     */ public class FunctionNode extends ScriptNode
/*     */ {
/*     */   public static final int FUNCTION_STATEMENT = 1;
/*     */   public static final int FUNCTION_EXPRESSION = 2;
/*     */   public static final int FUNCTION_EXPRESSION_STATEMENT = 3;
/* 104 */   private static final List<AstNode> NO_PARAMS = Collections.unmodifiableList(new ArrayList());
/*     */   private Name functionName;
/*     */   private List<AstNode> params;
/*     */   private AstNode body;
/*     */   private boolean isExpressionClosure;
/* 111 */   private Form functionForm = Form.FUNCTION;
/* 112 */   private int lp = -1;
/* 113 */   private int rp = -1;
/*     */   private int functionType;
/*     */   private boolean needsActivation;
/*     */   private boolean ignoreDynamicScope;
/*     */   private boolean isGenerator;
/*     */   private List<Node> generatorResumePoints;
/*     */   private Map<Node, int[]> liveLocals;
/*     */   private AstNode memberExprNode;
/*     */ 
/*     */   public FunctionNode()
/*     */   {
/* 125 */     this.type = 109;
/*     */   }
/*     */ 
/*     */   public FunctionNode(int paramInt)
/*     */   {
/* 132 */     super(paramInt);
/*     */ 
/* 125 */     this.type = 109;
/*     */   }
/*     */ 
/*     */   public FunctionNode(int paramInt, Name paramName)
/*     */   {
/* 136 */     super(paramInt);
/*     */ 
/* 125 */     this.type = 109;
/*     */ 
/* 137 */     setFunctionName(paramName);
/*     */   }
/*     */ 
/*     */   public Name getFunctionName()
/*     */   {
/* 145 */     return this.functionName;
/*     */   }
/*     */ 
/*     */   public void setFunctionName(Name paramName)
/*     */   {
/* 153 */     this.functionName = paramName;
/* 154 */     if (paramName != null)
/* 155 */       paramName.setParent(this);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 163 */     return this.functionName != null ? this.functionName.getIdentifier() : "";
/*     */   }
/*     */ 
/*     */   public List<AstNode> getParams()
/*     */   {
/* 172 */     return this.params != null ? this.params : NO_PARAMS;
/*     */   }
/*     */ 
/*     */   public void setParams(List<AstNode> paramList)
/*     */   {
/* 181 */     if (paramList == null) {
/* 182 */       this.params = null;
/*     */     } else {
/* 184 */       if (this.params != null)
/* 185 */         this.params.clear();
/* 186 */       for (AstNode localAstNode : paramList)
/* 187 */         addParam(localAstNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addParam(AstNode paramAstNode)
/*     */   {
/* 198 */     assertNotNull(paramAstNode);
/* 199 */     if (this.params == null) {
/* 200 */       this.params = new ArrayList();
/*     */     }
/* 202 */     this.params.add(paramAstNode);
/* 203 */     paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean isParam(AstNode paramAstNode)
/*     */   {
/* 212 */     return this.params == null ? false : this.params.contains(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getBody()
/*     */   {
/* 222 */     return this.body;
/*     */   }
/*     */ 
/*     */   public void setBody(AstNode paramAstNode)
/*     */   {
/* 237 */     assertNotNull(paramAstNode);
/* 238 */     this.body = paramAstNode;
/* 239 */     if (Boolean.TRUE.equals(paramAstNode.getProp(25))) {
/* 240 */       setIsExpressionClosure(true);
/*     */     }
/* 242 */     int i = paramAstNode.getPosition() + paramAstNode.getLength();
/* 243 */     paramAstNode.setParent(this);
/* 244 */     setLength(i - this.position);
/* 245 */     setEncodedSourceBounds(this.position, i);
/*     */   }
/*     */ 
/*     */   public int getLp()
/*     */   {
/* 252 */     return this.lp;
/*     */   }
/*     */ 
/*     */   public void setLp(int paramInt)
/*     */   {
/* 259 */     this.lp = paramInt;
/*     */   }
/*     */ 
/*     */   public int getRp()
/*     */   {
/* 266 */     return this.rp;
/*     */   }
/*     */ 
/*     */   public void setRp(int paramInt)
/*     */   {
/* 273 */     this.rp = paramInt;
/*     */   }
/*     */ 
/*     */   public void setParens(int paramInt1, int paramInt2)
/*     */   {
/* 280 */     this.lp = paramInt1;
/* 281 */     this.rp = paramInt2;
/*     */   }
/*     */ 
/*     */   public boolean isExpressionClosure()
/*     */   {
/* 288 */     return this.isExpressionClosure;
/*     */   }
/*     */ 
/*     */   public void setIsExpressionClosure(boolean paramBoolean)
/*     */   {
/* 295 */     this.isExpressionClosure = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean requiresActivation()
/*     */   {
/* 310 */     return this.needsActivation;
/*     */   }
/*     */ 
/*     */   public void setRequiresActivation() {
/* 314 */     this.needsActivation = true;
/*     */   }
/*     */ 
/*     */   public boolean getIgnoreDynamicScope() {
/* 318 */     return this.ignoreDynamicScope;
/*     */   }
/*     */ 
/*     */   public void setIgnoreDynamicScope() {
/* 322 */     this.ignoreDynamicScope = true;
/*     */   }
/*     */ 
/*     */   public boolean isGenerator() {
/* 326 */     return this.isGenerator;
/*     */   }
/*     */ 
/*     */   public void setIsGenerator() {
/* 330 */     this.isGenerator = true;
/*     */   }
/*     */ 
/*     */   public void addResumptionPoint(Node paramNode) {
/* 334 */     if (this.generatorResumePoints == null)
/* 335 */       this.generatorResumePoints = new ArrayList();
/* 336 */     this.generatorResumePoints.add(paramNode);
/*     */   }
/*     */ 
/*     */   public List<Node> getResumptionPoints() {
/* 340 */     return this.generatorResumePoints;
/*     */   }
/*     */ 
/*     */   public Map<Node, int[]> getLiveLocals() {
/* 344 */     return this.liveLocals;
/*     */   }
/*     */ 
/*     */   public void addLiveLocals(Node paramNode, int[] paramArrayOfInt) {
/* 348 */     if (this.liveLocals == null)
/* 349 */       this.liveLocals = new HashMap();
/* 350 */     this.liveLocals.put(paramNode, paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public int addFunction(FunctionNode paramFunctionNode)
/*     */   {
/* 355 */     int i = super.addFunction(paramFunctionNode);
/* 356 */     if (getFunctionCount() > 0) {
/* 357 */       this.needsActivation = true;
/*     */     }
/* 359 */     return i;
/*     */   }
/*     */ 
/*     */   public int getFunctionType()
/*     */   {
/* 366 */     return this.functionType;
/*     */   }
/*     */ 
/*     */   public void setFunctionType(int paramInt) {
/* 370 */     this.functionType = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean isGetterOrSetter() {
/* 374 */     return (this.functionForm == Form.GETTER) || (this.functionForm == Form.SETTER);
/*     */   }
/*     */ 
/*     */   public boolean isGetter() {
/* 378 */     return this.functionForm == Form.GETTER;
/*     */   }
/*     */ 
/*     */   public boolean isSetter() {
/* 382 */     return this.functionForm == Form.SETTER;
/*     */   }
/*     */ 
/*     */   public void setFunctionIsGetter() {
/* 386 */     this.functionForm = Form.GETTER;
/*     */   }
/*     */ 
/*     */   public void setFunctionIsSetter() {
/* 390 */     this.functionForm = Form.SETTER;
/*     */   }
/*     */ 
/*     */   public void setMemberExprNode(AstNode paramAstNode)
/*     */   {
/* 404 */     this.memberExprNode = paramAstNode;
/* 405 */     if (paramAstNode != null)
/* 406 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public AstNode getMemberExprNode() {
/* 410 */     return this.memberExprNode;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 415 */     StringBuilder localStringBuilder = new StringBuilder();
/* 416 */     localStringBuilder.append(makeIndent(paramInt));
/* 417 */     localStringBuilder.append("function");
/* 418 */     if (this.functionName != null) {
/* 419 */       localStringBuilder.append(" ");
/* 420 */       localStringBuilder.append(this.functionName.toSource(0));
/*     */     }
/* 422 */     if (this.params == null) {
/* 423 */       localStringBuilder.append("() ");
/*     */     } else {
/* 425 */       localStringBuilder.append("(");
/* 426 */       printList(this.params, localStringBuilder);
/* 427 */       localStringBuilder.append(") ");
/*     */     }
/* 429 */     if (this.isExpressionClosure) {
/* 430 */       localStringBuilder.append(" ");
/* 431 */       localStringBuilder.append(getBody().toSource(0));
/*     */     } else {
/* 433 */       localStringBuilder.append(getBody().toSource(paramInt).trim());
/*     */     }
/* 435 */     if (this.functionType == 1) {
/* 436 */       localStringBuilder.append("\n");
/*     */     }
/* 438 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 448 */     if (paramNodeVisitor.visit(this)) {
/* 449 */       if (this.functionName != null) {
/* 450 */         this.functionName.visit(paramNodeVisitor);
/*     */       }
/* 452 */       for (AstNode localAstNode : getParams()) {
/* 453 */         localAstNode.visit(paramNodeVisitor);
/*     */       }
/* 455 */       getBody().visit(paramNodeVisitor);
/* 456 */       if ((!this.isExpressionClosure) && 
/* 457 */         (this.memberExprNode != null))
/* 458 */         this.memberExprNode.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Form
/*     */   {
/* 102 */     FUNCTION, GETTER, SETTER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.FunctionNode
 * JD-Core Version:    0.6.2
 */