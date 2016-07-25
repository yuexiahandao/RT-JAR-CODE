/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.org.mozilla.javascript.internal.ast.AstRoot;
/*     */ import sun.org.mozilla.javascript.internal.ast.FunctionNode;
/*     */ import sun.org.mozilla.javascript.internal.ast.Jump;
/*     */ import sun.org.mozilla.javascript.internal.ast.Scope;
/*     */ import sun.org.mozilla.javascript.internal.ast.ScriptNode;
/*     */ 
/*     */ public class NodeTransformer
/*     */ {
/*     */   private ObjArray loops;
/*     */   private ObjArray loopEnds;
/*     */   private boolean hasFinally;
/*     */ 
/*     */   public final void transform(ScriptNode paramScriptNode)
/*     */   {
/*  70 */     transformCompilationUnit(paramScriptNode);
/*  71 */     for (int i = 0; i != paramScriptNode.getFunctionCount(); i++) {
/*  72 */       FunctionNode localFunctionNode = paramScriptNode.getFunctionNode(i);
/*  73 */       transform(localFunctionNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void transformCompilationUnit(ScriptNode paramScriptNode)
/*     */   {
/*  79 */     this.loops = new ObjArray();
/*  80 */     this.loopEnds = new ObjArray();
/*     */ 
/*  83 */     this.hasFinally = false;
/*     */ 
/*  86 */     boolean bool1 = (paramScriptNode.getType() != 109) || (((FunctionNode)paramScriptNode).requiresActivation());
/*     */ 
/*  88 */     paramScriptNode.flattenSymbolTable(!bool1);
/*     */ 
/*  92 */     boolean bool2 = ((paramScriptNode instanceof AstRoot)) && (((AstRoot)paramScriptNode).isInStrictMode());
/*     */ 
/*  94 */     transformCompilationUnit_r(paramScriptNode, paramScriptNode, paramScriptNode, bool1, bool2);
/*     */   }
/*     */ 
/*     */   private void transformCompilationUnit_r(ScriptNode paramScriptNode, Node paramNode, Scope paramScope, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 104 */     Node localNode1 = null;
/*     */     while (true)
/*     */     {
/* 107 */       Node localNode2 = null;
/* 108 */       if (localNode1 == null) {
/* 109 */         localNode1 = paramNode.getFirstChild();
/*     */       } else {
/* 111 */         localNode2 = localNode1;
/* 112 */         localNode1 = localNode1.getNext();
/*     */       }
/* 114 */       if (localNode1 == null)
/*     */       {
/*     */         break;
/*     */       }
/* 118 */       int i = localNode1.getType();
/*     */       Object localObject1;
/*     */       Object localObject3;
/*     */       Object localObject6;
/* 119 */       if ((paramBoolean1) && ((i == 129) || (i == 132) || (i == 157)) && ((localNode1 instanceof Scope)))
/*     */       {
/* 124 */         localObject1 = (Scope)localNode1;
/* 125 */         if (((Scope)localObject1).getSymbolTable() != null)
/*     */         {
/* 128 */           localObject3 = new Node(i == 157 ? 158 : 153);
/*     */ 
/* 130 */           Node localNode3 = new Node(153);
/* 131 */           ((Node)localObject3).addChildToBack(localNode3);
/* 132 */           for (localObject6 = ((Scope)localObject1).getSymbolTable().keySet().iterator(); ((Iterator)localObject6).hasNext(); ) { String str = (String)((Iterator)localObject6).next();
/* 133 */             localNode3.addChildToBack(Node.newString(39, str));
/*     */           }
/* 135 */           ((Scope)localObject1).setSymbolTable(null);
/* 136 */           localObject6 = localNode1;
/* 137 */           localNode1 = replaceCurrent(paramNode, localNode2, localNode1, (Node)localObject3);
/* 138 */           i = localNode1.getType();
/* 139 */           ((Node)localObject3).addChildToBack((Node)localObject6);
/*     */         }
/*     */       }
/*     */       Jump localJump;
/*     */       Object localObject7;
/*     */       Object localObject2;
/*     */       Object localObject4;
/*     */       Object localObject5;
/* 143 */       switch (i)
/*     */       {
/*     */       case 114:
/*     */       case 130:
/*     */       case 132:
/* 148 */         this.loops.push(localNode1);
/* 149 */         this.loopEnds.push(((Jump)localNode1).target);
/* 150 */         break;
/*     */       case 123:
/* 154 */         this.loops.push(localNode1);
/* 155 */         localObject1 = localNode1.getNext();
/* 156 */         if (((Node)localObject1).getType() != 3) {
/* 157 */           Kit.codeBug();
/*     */         }
/* 159 */         this.loopEnds.push(localObject1);
/* 160 */         break;
/*     */       case 81:
/* 165 */         localObject1 = (Jump)localNode1;
/* 166 */         localObject3 = ((Jump)localObject1).getFinally();
/* 167 */         if (localObject3 != null) {
/* 168 */           this.hasFinally = true;
/* 169 */           this.loops.push(localNode1);
/* 170 */           this.loopEnds.push(localObject3); } break;
/*     */       case 3:
/*     */       case 131:
/* 177 */         if ((!this.loopEnds.isEmpty()) && (this.loopEnds.peek() == localNode1)) {
/* 178 */           this.loopEnds.pop();
/* 179 */           this.loops.pop(); } break;
/*     */       case 72:
/* 184 */         ((FunctionNode)paramScriptNode).addResumptionPoint(localNode1);
/* 185 */         break;
/*     */       case 4:
/* 189 */         int j = (paramScriptNode.getType() == 109) && (((FunctionNode)paramScriptNode).isGenerator()) ? 1 : 0;
/*     */ 
/* 191 */         if (j != 0) {
/* 192 */           localNode1.putIntProp(20, 1);
/*     */         }
/*     */ 
/* 201 */         if (this.hasFinally)
/*     */         {
/* 203 */           localObject3 = null;
/* 204 */           for (int k = this.loops.size() - 1; k >= 0; k--) {
/* 205 */             localObject6 = (Node)this.loops.get(k);
/* 206 */             int n = ((Node)localObject6).getType();
/* 207 */             if ((n == 81) || (n == 123))
/*     */             {
/* 209 */               if (n == 81) {
/* 210 */                 localJump = new Jump(135);
/* 211 */                 Node localNode6 = ((Jump)localObject6).getFinally();
/* 212 */                 localJump.target = localNode6;
/* 213 */                 localObject7 = localJump;
/*     */               } else {
/* 215 */                 localObject7 = new Node(3);
/*     */               }
/* 217 */               if (localObject3 == null) {
/* 218 */                 localObject3 = new Node(129, localNode1.getLineno());
/*     */               }
/*     */ 
/* 221 */               ((Node)localObject3).addChildToBack((Node)localObject7);
/*     */             }
/*     */           }
/* 224 */           if (localObject3 != null) {
/* 225 */             Node localNode4 = localNode1;
/* 226 */             localObject6 = localNode4.getFirstChild();
/* 227 */             localNode1 = replaceCurrent(paramNode, localNode2, localNode1, (Node)localObject3);
/* 228 */             if ((localObject6 == null) || (j != 0)) {
/* 229 */               ((Node)localObject3).addChildToBack(localNode4); continue;
/*     */             }
/* 231 */             Node localNode5 = new Node(134, (Node)localObject6);
/* 232 */             ((Node)localObject3).addChildToFront(localNode5);
/* 233 */             localNode4 = new Node(64);
/* 234 */             ((Node)localObject3).addChildToBack(localNode4);
/*     */ 
/* 236 */             transformCompilationUnit_r(paramScriptNode, localNode5, paramScope, paramBoolean1, paramBoolean2);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 241 */         break;
/*     */       case 120:
/*     */       case 121:
/* 249 */         localObject2 = (Jump)localNode1;
/* 250 */         localObject3 = ((Jump)localObject2).getJumpStatement();
/* 251 */         if (localObject3 == null) Kit.codeBug();
/*     */ 
/* 253 */         int m = this.loops.size();
/*     */         while (true) { if (m == 0)
/*     */           {
/* 258 */             throw Kit.codeBug();
/*     */           }
/* 260 */           m--;
/* 261 */           localObject6 = (Node)this.loops.get(m);
/* 262 */           if (localObject6 == localObject3)
/*     */           {
/*     */             break;
/*     */           }
/* 266 */           int i1 = ((Node)localObject6).getType();
/* 267 */           if (i1 == 123) {
/* 268 */             localObject7 = new Node(3);
/* 269 */             localNode2 = addBeforeCurrent(paramNode, localNode2, localNode1, (Node)localObject7);
/*     */           }
/* 271 */           else if (i1 == 81) {
/* 272 */             localObject7 = (Jump)localObject6;
/* 273 */             localJump = new Jump(135);
/* 274 */             localJump.target = ((Jump)localObject7).getFinally();
/* 275 */             localNode2 = addBeforeCurrent(paramNode, localNode2, localNode1, localJump);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 280 */         if (i == 120)
/* 281 */           ((Jump)localObject2).target = ((Jump)localObject3).target;
/*     */         else {
/* 283 */           ((Jump)localObject2).target = ((Jump)localObject3).getContinue();
/*     */         }
/* 285 */         ((Jump)localObject2).setType(5);
/*     */ 
/* 287 */         break;
/*     */       case 38:
/* 291 */         visitCall(localNode1, paramScriptNode);
/* 292 */         break;
/*     */       case 30:
/* 295 */         visitNew(localNode1, paramScriptNode);
/* 296 */         break;
/*     */       case 153:
/*     */       case 158:
/* 300 */         localObject2 = localNode1.getFirstChild();
/* 301 */         if (((Node)localObject2).getType() == 153)
/*     */         {
/* 304 */           boolean bool = (paramScriptNode.getType() != 109) || (((FunctionNode)paramScriptNode).requiresActivation());
/*     */ 
/* 306 */           localNode1 = visitLet(bool, paramNode, localNode2, localNode1);
/* 307 */         }break;
/*     */       case 122:
/*     */       case 154:
/* 316 */         localObject2 = new Node(129);
/* 317 */         for (localObject4 = localNode1.getFirstChild(); localObject4 != null; )
/*     */         {
/* 320 */           localObject5 = localObject4;
/* 321 */           localObject4 = ((Node)localObject4).getNext();
/* 322 */           if (((Node)localObject5).getType() == 39) {
/* 323 */             if (((Node)localObject5).hasChildren())
/*     */             {
/* 325 */               localObject6 = ((Node)localObject5).getFirstChild();
/* 326 */               ((Node)localObject5).removeChild((Node)localObject6);
/* 327 */               ((Node)localObject5).setType(49);
/* 328 */               localObject5 = new Node(i == 154 ? 155 : 8, (Node)localObject5, (Node)localObject6);
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 335 */             if (((Node)localObject5).getType() != 158) {
/* 336 */               throw Kit.codeBug();
/*     */             }
/* 338 */             localObject6 = new Node(133, (Node)localObject5, localNode1.getLineno());
/* 339 */             ((Node)localObject2).addChildToBack((Node)localObject6);
/*     */           }
/*     */         }
/* 341 */         localNode1 = replaceCurrent(paramNode, localNode2, localNode1, (Node)localObject2);
/* 342 */         break;
/*     */       case 137:
/* 346 */         localObject2 = paramScope.getDefiningScope(localNode1.getString());
/* 347 */         if (localObject2 != null) {
/* 348 */           localNode1.setScope((Scope)localObject2);
/*     */         }
/*     */ 
/* 351 */         break;
/*     */       case 7:
/*     */       case 32:
/* 359 */         localObject2 = localNode1.getFirstChild();
/* 360 */         if (i == 7) {
/* 361 */           while (((Node)localObject2).getType() == 26) {
/* 362 */             localObject2 = ((Node)localObject2).getFirstChild();
/*     */           }
/* 364 */           if ((((Node)localObject2).getType() == 12) || (((Node)localObject2).getType() == 13))
/*     */           {
/* 367 */             localObject4 = ((Node)localObject2).getFirstChild();
/* 368 */             localObject5 = ((Node)localObject2).getLastChild();
/* 369 */             if ((((Node)localObject4).getType() == 39) && (((Node)localObject4).getString().equals("undefined")))
/*     */             {
/* 371 */               localObject2 = localObject5;
/* 372 */             } else if ((((Node)localObject5).getType() == 39) && (((Node)localObject5).getString().equals("undefined")))
/*     */             {
/* 374 */               localObject2 = localObject4;
/*     */             }
/*     */           }
/*     */         }
/* 377 */         if (((Node)localObject2).getType() == 33)
/* 378 */           ((Node)localObject2).setType(34); break;
/*     */       case 8:
/* 383 */         if (paramBoolean2) {
/* 384 */           localNode1.setType(73);
/*     */         }
/*     */ 
/*     */       case 31:
/*     */       case 39:
/*     */       case 155:
/* 392 */         if (!paramBoolean1)
/*     */         {
/* 396 */           if (i == 39) {
/* 397 */             localObject2 = localNode1;
/*     */           } else {
/* 399 */             localObject2 = localNode1.getFirstChild();
/* 400 */             if (((Node)localObject2).getType() != 49) {
/* 401 */               if (i == 31) {
/*     */                 break label1734;
/*     */               }
/* 404 */               throw Kit.codeBug();
/*     */             }
/*     */           }
/* 407 */           if (((Node)localObject2).getScope() == null)
/*     */           {
/* 410 */             localObject4 = ((Node)localObject2).getString();
/* 411 */             localObject5 = paramScope.getDefiningScope((String)localObject4);
/* 412 */             if (localObject5 != null) {
/* 413 */               ((Node)localObject2).setScope((Scope)localObject5);
/* 414 */               if (i == 39) {
/* 415 */                 localNode1.setType(55);
/* 416 */               } else if ((i == 8) || (i == 73))
/*     */               {
/* 418 */                 localNode1.setType(56);
/* 419 */                 ((Node)localObject2).setType(41);
/* 420 */               } else if (i == 155) {
/* 421 */                 localNode1.setType(156);
/* 422 */                 ((Node)localObject2).setType(41);
/* 423 */               } else if (i == 31)
/*     */               {
/* 425 */                 localObject6 = new Node(44);
/* 426 */                 localNode1 = replaceCurrent(paramNode, localNode2, localNode1, (Node)localObject6);
/*     */               } else {
/* 428 */                 throw Kit.codeBug();
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       default:
/* 435 */         label1734: transformCompilationUnit_r(paramScriptNode, localNode1, (localNode1 instanceof Scope) ? (Scope)localNode1 : paramScope, paramBoolean1, paramBoolean2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void visitNew(Node paramNode, ScriptNode paramScriptNode)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void visitCall(Node paramNode, ScriptNode paramScriptNode)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected Node visitLet(boolean paramBoolean, Node paramNode1, Node paramNode2, Node paramNode3)
/*     */   {
/* 450 */     Node localNode1 = paramNode3.getFirstChild();
/* 451 */     Node localNode2 = localNode1.getNext();
/* 452 */     paramNode3.removeChild(localNode1);
/* 453 */     paramNode3.removeChild(localNode2);
/* 454 */     int i = paramNode3.getType() == 158 ? 1 : 0;
/*     */     Node localNode3;
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Node localNode5;
/*     */     Node localNode6;
/*     */     Node localNode4;
/* 457 */     if (paramBoolean) {
/* 458 */       localNode3 = new Node(i != 0 ? 159 : 129);
/* 459 */       localNode3 = replaceCurrent(paramNode1, paramNode2, paramNode3, localNode3);
/* 460 */       localObject1 = new ArrayList();
/* 461 */       localObject2 = new Node(66);
/* 462 */       for (localNode5 = localNode1.getFirstChild(); localNode5 != null; localNode5 = localNode5.getNext()) {
/* 463 */         localNode6 = localNode5;
/* 464 */         if (localNode6.getType() == 158)
/*     */         {
/* 466 */           localObject3 = (List)localNode6.getProp(22);
/*     */ 
/* 468 */           Node localNode7 = localNode6.getFirstChild();
/* 469 */           if (localNode7.getType() != 153) throw Kit.codeBug();
/*     */ 
/* 471 */           if (i != 0)
/* 472 */             localNode2 = new Node(89, localNode7.getNext(), localNode2);
/*     */           else {
/* 474 */             localNode2 = new Node(129, new Node(133, localNode7.getNext()), localNode2);
/*     */           }
/*     */ 
/* 480 */           if (localObject3 != null) {
/* 481 */             ((ArrayList)localObject1).addAll((Collection)localObject3);
/* 482 */             for (int j = 0; j < ((List)localObject3).size(); j++) {
/* 483 */               ((Node)localObject2).addChildToBack(new Node(126, Node.newNumber(0.0D)));
/*     */             }
/*     */           }
/*     */ 
/* 487 */           localNode6 = localNode7.getFirstChild();
/*     */         }
/* 489 */         if (localNode6.getType() != 39) throw Kit.codeBug();
/* 490 */         ((ArrayList)localObject1).add(ScriptRuntime.getIndexObject(localNode6.getString()));
/* 491 */         Object localObject3 = localNode6.getFirstChild();
/* 492 */         if (localObject3 == null) {
/* 493 */           localObject3 = new Node(126, Node.newNumber(0.0D));
/*     */         }
/* 495 */         ((Node)localObject2).addChildToBack((Node)localObject3);
/*     */       }
/* 497 */       ((Node)localObject2).putProp(12, ((ArrayList)localObject1).toArray());
/* 498 */       localNode4 = new Node(2, (Node)localObject2);
/* 499 */       localNode3.addChildToBack(localNode4);
/* 500 */       localNode3.addChildToBack(new Node(123, localNode2));
/* 501 */       localNode3.addChildToBack(new Node(3));
/*     */     } else {
/* 503 */       localNode3 = new Node(i != 0 ? 89 : 129);
/* 504 */       localNode3 = replaceCurrent(paramNode1, paramNode2, paramNode3, localNode3);
/* 505 */       localNode4 = new Node(89);
/* 506 */       for (localObject1 = localNode1.getFirstChild(); localObject1 != null; localObject1 = ((Node)localObject1).getNext()) {
/* 507 */         localObject2 = localObject1;
/* 508 */         if (((Node)localObject2).getType() == 158)
/*     */         {
/* 510 */           localNode5 = ((Node)localObject2).getFirstChild();
/* 511 */           if (localNode5.getType() != 153) throw Kit.codeBug();
/*     */ 
/* 513 */           if (i != 0)
/* 514 */             localNode2 = new Node(89, localNode5.getNext(), localNode2);
/*     */           else {
/* 516 */             localNode2 = new Node(129, new Node(133, localNode5.getNext()), localNode2);
/*     */           }
/*     */ 
/* 521 */           Scope.joinScopes((Scope)localObject2, (Scope)paramNode3);
/*     */ 
/* 523 */           localObject2 = localNode5.getFirstChild();
/*     */         }
/* 525 */         if (((Node)localObject2).getType() != 39) throw Kit.codeBug();
/* 526 */         localNode5 = Node.newString(((Node)localObject2).getString());
/* 527 */         localNode5.setScope((Scope)paramNode3);
/* 528 */         localNode6 = ((Node)localObject2).getFirstChild();
/* 529 */         if (localNode6 == null) {
/* 530 */           localNode6 = new Node(126, Node.newNumber(0.0D));
/*     */         }
/* 532 */         localNode4.addChildToBack(new Node(56, localNode5, localNode6));
/*     */       }
/* 534 */       if (i != 0) {
/* 535 */         localNode3.addChildToBack(localNode4);
/* 536 */         paramNode3.setType(89);
/* 537 */         localNode3.addChildToBack(paramNode3);
/* 538 */         paramNode3.addChildToBack(localNode2);
/* 539 */         if ((localNode2 instanceof Scope)) {
/* 540 */           localObject1 = ((Scope)localNode2).getParentScope();
/* 541 */           ((Scope)localNode2).setParentScope((Scope)paramNode3);
/* 542 */           ((Scope)paramNode3).setParentScope((Scope)localObject1);
/*     */         }
/*     */       } else {
/* 545 */         localNode3.addChildToBack(new Node(133, localNode4));
/* 546 */         paramNode3.setType(129);
/* 547 */         localNode3.addChildToBack(paramNode3);
/* 548 */         paramNode3.addChildrenToBack(localNode2);
/* 549 */         if ((localNode2 instanceof Scope)) {
/* 550 */           localObject1 = ((Scope)localNode2).getParentScope();
/* 551 */           ((Scope)localNode2).setParentScope((Scope)paramNode3);
/* 552 */           ((Scope)paramNode3).setParentScope((Scope)localObject1);
/*     */         }
/*     */       }
/*     */     }
/* 556 */     return localNode3;
/*     */   }
/*     */ 
/*     */   private static Node addBeforeCurrent(Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4)
/*     */   {
/* 562 */     if (paramNode2 == null) {
/* 563 */       if (paramNode3 != paramNode1.getFirstChild()) Kit.codeBug();
/* 564 */       paramNode1.addChildToFront(paramNode4);
/*     */     } else {
/* 566 */       if (paramNode3 != paramNode2.getNext()) Kit.codeBug();
/* 567 */       paramNode1.addChildAfter(paramNode4, paramNode2);
/*     */     }
/* 569 */     return paramNode4;
/*     */   }
/*     */ 
/*     */   private static Node replaceCurrent(Node paramNode1, Node paramNode2, Node paramNode3, Node paramNode4)
/*     */   {
/* 575 */     if (paramNode2 == null) {
/* 576 */       if (paramNode3 != paramNode1.getFirstChild()) Kit.codeBug();
/* 577 */       paramNode1.replaceChild(paramNode3, paramNode4);
/* 578 */     } else if (paramNode2.next == paramNode3)
/*     */     {
/* 581 */       paramNode1.replaceChildAfter(paramNode2, paramNode4);
/*     */     } else {
/* 583 */       paramNode1.replaceChild(paramNode3, paramNode4);
/*     */     }
/* 585 */     return paramNode4;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.NodeTransformer
 * JD-Core Version:    0.6.2
 */