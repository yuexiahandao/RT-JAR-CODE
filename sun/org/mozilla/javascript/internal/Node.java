/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import sun.org.mozilla.javascript.internal.ast.Jump;
/*      */ import sun.org.mozilla.javascript.internal.ast.Name;
/*      */ import sun.org.mozilla.javascript.internal.ast.NumberLiteral;
/*      */ import sun.org.mozilla.javascript.internal.ast.Scope;
/*      */ import sun.org.mozilla.javascript.internal.ast.ScriptNode;
/*      */ 
/*      */ public class Node
/*      */   implements Iterable<Node>
/*      */ {
/*      */   public static final int FUNCTION_PROP = 1;
/*      */   public static final int LOCAL_PROP = 2;
/*      */   public static final int LOCAL_BLOCK_PROP = 3;
/*      */   public static final int REGEXP_PROP = 4;
/*      */   public static final int CASEARRAY_PROP = 5;
/*      */   public static final int TARGETBLOCK_PROP = 6;
/*      */   public static final int VARIABLE_PROP = 7;
/*      */   public static final int ISNUMBER_PROP = 8;
/*      */   public static final int DIRECTCALL_PROP = 9;
/*      */   public static final int SPECIALCALL_PROP = 10;
/*      */   public static final int SKIP_INDEXES_PROP = 11;
/*      */   public static final int OBJECT_IDS_PROP = 12;
/*      */   public static final int INCRDECR_PROP = 13;
/*      */   public static final int CATCH_SCOPE_PROP = 14;
/*      */   public static final int LABEL_ID_PROP = 15;
/*      */   public static final int MEMBER_TYPE_PROP = 16;
/*      */   public static final int NAME_PROP = 17;
/*      */   public static final int CONTROL_BLOCK_PROP = 18;
/*      */   public static final int PARENTHESIZED_PROP = 19;
/*      */   public static final int GENERATOR_END_PROP = 20;
/*      */   public static final int DESTRUCTURING_ARRAY_LENGTH = 21;
/*      */   public static final int DESTRUCTURING_NAMES = 22;
/*      */   public static final int DESTRUCTURING_PARAMS = 23;
/*      */   public static final int JSDOC_PROP = 24;
/*      */   public static final int EXPRESSION_CLOSURE_PROP = 25;
/*      */   public static final int DESTRUCTURING_SHORTHAND = 26;
/*      */   public static final int LAST_PROP = 26;
/*      */   public static final int BOTH = 0;
/*      */   public static final int LEFT = 1;
/*      */   public static final int RIGHT = 2;
/*      */   public static final int NON_SPECIALCALL = 0;
/*      */   public static final int SPECIALCALL_EVAL = 1;
/*      */   public static final int SPECIALCALL_WITH = 2;
/*      */   public static final int DECR_FLAG = 1;
/*      */   public static final int POST_FLAG = 2;
/*      */   public static final int PROPERTY_FLAG = 1;
/*      */   public static final int ATTRIBUTE_FLAG = 2;
/*      */   public static final int DESCENDANTS_FLAG = 4;
/*  361 */   private static final Node NOT_SET = new Node(-1);
/*      */   public static final int END_UNREACHED = 0;
/*      */   public static final int END_DROPS_OFF = 1;
/*      */   public static final int END_RETURNS = 2;
/*      */   public static final int END_RETURNS_VALUE = 4;
/*      */   public static final int END_YIELDS = 8;
/* 1267 */   protected int type = -1;
/*      */   protected Node next;
/*      */   protected Node first;
/*      */   protected Node last;
/* 1271 */   protected int lineno = -1;
/*      */   protected PropListItem propListHead;
/*      */ 
/*      */   public Node(int paramInt)
/*      */   {
/*  132 */     this.type = paramInt;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt, Node paramNode) {
/*  136 */     this.type = paramInt;
/*  137 */     this.first = (this.last = paramNode);
/*  138 */     paramNode.next = null;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt, Node paramNode1, Node paramNode2) {
/*  142 */     this.type = paramInt;
/*  143 */     this.first = paramNode1;
/*  144 */     this.last = paramNode2;
/*  145 */     paramNode1.next = paramNode2;
/*  146 */     paramNode2.next = null;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt, Node paramNode1, Node paramNode2, Node paramNode3) {
/*  150 */     this.type = paramInt;
/*  151 */     this.first = paramNode1;
/*  152 */     this.last = paramNode3;
/*  153 */     paramNode1.next = paramNode2;
/*  154 */     paramNode2.next = paramNode3;
/*  155 */     paramNode3.next = null;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt1, int paramInt2) {
/*  159 */     this.type = paramInt1;
/*  160 */     this.lineno = paramInt2;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt1, Node paramNode, int paramInt2) {
/*  164 */     this(paramInt1, paramNode);
/*  165 */     this.lineno = paramInt2;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt1, Node paramNode1, Node paramNode2, int paramInt2) {
/*  169 */     this(paramInt1, paramNode1, paramNode2);
/*  170 */     this.lineno = paramInt2;
/*      */   }
/*      */ 
/*      */   public Node(int paramInt1, Node paramNode1, Node paramNode2, Node paramNode3, int paramInt2) {
/*  174 */     this(paramInt1, paramNode1, paramNode2, paramNode3);
/*  175 */     this.lineno = paramInt2;
/*      */   }
/*      */ 
/*      */   public static Node newNumber(double paramDouble) {
/*  179 */     NumberLiteral localNumberLiteral = new NumberLiteral();
/*  180 */     localNumberLiteral.setNumber(paramDouble);
/*  181 */     return localNumberLiteral;
/*      */   }
/*      */ 
/*      */   public static Node newString(String paramString) {
/*  185 */     return newString(41, paramString);
/*      */   }
/*      */ 
/*      */   public static Node newString(int paramInt, String paramString) {
/*  189 */     Name localName = new Name();
/*  190 */     localName.setIdentifier(paramString);
/*  191 */     localName.setType(paramInt);
/*  192 */     return localName;
/*      */   }
/*      */ 
/*      */   public int getType() {
/*  196 */     return this.type;
/*      */   }
/*      */ 
/*      */   public Node setType(int paramInt)
/*      */   {
/*  203 */     this.type = paramInt;
/*  204 */     return this;
/*      */   }
/*      */ 
/*      */   public String getJsDoc()
/*      */   {
/*  213 */     return (String)getProp(24);
/*      */   }
/*      */ 
/*      */   public void setJsDoc(String paramString)
/*      */   {
/*  220 */     putProp(24, paramString);
/*      */   }
/*      */ 
/*      */   public boolean hasChildren() {
/*  224 */     return this.first != null;
/*      */   }
/*      */ 
/*      */   public Node getFirstChild() {
/*  228 */     return this.first;
/*      */   }
/*      */ 
/*      */   public Node getLastChild() {
/*  232 */     return this.last;
/*      */   }
/*      */ 
/*      */   public Node getNext() {
/*  236 */     return this.next;
/*      */   }
/*      */ 
/*      */   public Node getChildBefore(Node paramNode) {
/*  240 */     if (paramNode == this.first)
/*  241 */       return null;
/*  242 */     Node localNode = this.first;
/*  243 */     while (localNode.next != paramNode) {
/*  244 */       localNode = localNode.next;
/*  245 */       if (localNode == null)
/*  246 */         throw new RuntimeException("node is not a child");
/*      */     }
/*  248 */     return localNode;
/*      */   }
/*      */ 
/*      */   public Node getLastSibling() {
/*  252 */     Node localNode = this;
/*  253 */     while (localNode.next != null) {
/*  254 */       localNode = localNode.next;
/*      */     }
/*  256 */     return localNode;
/*      */   }
/*      */ 
/*      */   public void addChildToFront(Node paramNode) {
/*  260 */     paramNode.next = this.first;
/*  261 */     this.first = paramNode;
/*  262 */     if (this.last == null)
/*  263 */       this.last = paramNode;
/*      */   }
/*      */ 
/*      */   public void addChildToBack(Node paramNode)
/*      */   {
/*  268 */     paramNode.next = null;
/*  269 */     if (this.last == null) {
/*  270 */       this.first = (this.last = paramNode);
/*  271 */       return;
/*      */     }
/*  273 */     this.last.next = paramNode;
/*  274 */     this.last = paramNode;
/*      */   }
/*      */ 
/*      */   public void addChildrenToFront(Node paramNode) {
/*  278 */     Node localNode = paramNode.getLastSibling();
/*  279 */     localNode.next = this.first;
/*  280 */     this.first = paramNode;
/*  281 */     if (this.last == null)
/*  282 */       this.last = localNode;
/*      */   }
/*      */ 
/*      */   public void addChildrenToBack(Node paramNode)
/*      */   {
/*  287 */     if (this.last != null) {
/*  288 */       this.last.next = paramNode;
/*      */     }
/*  290 */     this.last = paramNode.getLastSibling();
/*  291 */     if (this.first == null)
/*  292 */       this.first = paramNode;
/*      */   }
/*      */ 
/*      */   public void addChildBefore(Node paramNode1, Node paramNode2)
/*      */   {
/*  300 */     if (paramNode1.next != null) {
/*  301 */       throw new RuntimeException("newChild had siblings in addChildBefore");
/*      */     }
/*  303 */     if (this.first == paramNode2) {
/*  304 */       paramNode1.next = this.first;
/*  305 */       this.first = paramNode1;
/*  306 */       return;
/*      */     }
/*  308 */     Node localNode = getChildBefore(paramNode2);
/*  309 */     addChildAfter(paramNode1, localNode);
/*      */   }
/*      */ 
/*      */   public void addChildAfter(Node paramNode1, Node paramNode2)
/*      */   {
/*  316 */     if (paramNode1.next != null) {
/*  317 */       throw new RuntimeException("newChild had siblings in addChildAfter");
/*      */     }
/*  319 */     paramNode1.next = paramNode2.next;
/*  320 */     paramNode2.next = paramNode1;
/*  321 */     if (this.last == paramNode2)
/*  322 */       this.last = paramNode1;
/*      */   }
/*      */ 
/*      */   public void removeChild(Node paramNode) {
/*  326 */     Node localNode = getChildBefore(paramNode);
/*  327 */     if (localNode == null)
/*  328 */       this.first = this.first.next;
/*      */     else
/*  330 */       localNode.next = paramNode.next;
/*  331 */     if (paramNode == this.last) this.last = localNode;
/*  332 */     paramNode.next = null;
/*      */   }
/*      */ 
/*      */   public void replaceChild(Node paramNode1, Node paramNode2) {
/*  336 */     paramNode2.next = paramNode1.next;
/*  337 */     if (paramNode1 == this.first) {
/*  338 */       this.first = paramNode2;
/*      */     } else {
/*  340 */       Node localNode = getChildBefore(paramNode1);
/*  341 */       localNode.next = paramNode2;
/*      */     }
/*  343 */     if (paramNode1 == this.last)
/*  344 */       this.last = paramNode2;
/*  345 */     paramNode1.next = null;
/*      */   }
/*      */ 
/*      */   public void replaceChildAfter(Node paramNode1, Node paramNode2) {
/*  349 */     Node localNode = paramNode1.next;
/*  350 */     paramNode2.next = localNode.next;
/*  351 */     paramNode1.next = paramNode2;
/*  352 */     if (localNode == this.last)
/*  353 */       this.last = paramNode2;
/*  354 */     localNode.next = null;
/*      */   }
/*      */ 
/*      */   public void removeChildren() {
/*  358 */     this.first = (this.last = null);
/*      */   }
/*      */ 
/*      */   public Iterator<Node> iterator()
/*      */   {
/*  416 */     return new NodeIterator();
/*      */   }
/*      */ 
/*      */   private static final String propToString(int paramInt)
/*      */   {
/*  455 */     return null;
/*      */   }
/*      */ 
/*      */   private PropListItem lookupProperty(int paramInt)
/*      */   {
/*  460 */     PropListItem localPropListItem = this.propListHead;
/*  461 */     while ((localPropListItem != null) && (paramInt != localPropListItem.type)) {
/*  462 */       localPropListItem = localPropListItem.next;
/*      */     }
/*  464 */     return localPropListItem;
/*      */   }
/*      */ 
/*      */   private PropListItem ensureProperty(int paramInt)
/*      */   {
/*  469 */     PropListItem localPropListItem = lookupProperty(paramInt);
/*  470 */     if (localPropListItem == null) {
/*  471 */       localPropListItem = new PropListItem(null);
/*  472 */       localPropListItem.type = paramInt;
/*  473 */       localPropListItem.next = this.propListHead;
/*  474 */       this.propListHead = localPropListItem;
/*      */     }
/*  476 */     return localPropListItem;
/*      */   }
/*      */ 
/*      */   public void removeProp(int paramInt)
/*      */   {
/*  481 */     PropListItem localPropListItem1 = this.propListHead;
/*  482 */     if (localPropListItem1 != null) {
/*  483 */       PropListItem localPropListItem2 = null;
/*  484 */       while (localPropListItem1.type != paramInt) {
/*  485 */         localPropListItem2 = localPropListItem1;
/*  486 */         localPropListItem1 = localPropListItem1.next;
/*  487 */         if (localPropListItem1 == null) return;
/*      */       }
/*  489 */       if (localPropListItem2 == null)
/*  490 */         this.propListHead = localPropListItem1.next;
/*      */       else
/*  492 */         localPropListItem2.next = localPropListItem1.next;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getProp(int paramInt)
/*      */   {
/*  499 */     PropListItem localPropListItem = lookupProperty(paramInt);
/*  500 */     if (localPropListItem == null) return null;
/*  501 */     return localPropListItem.objectValue;
/*      */   }
/*      */ 
/*      */   public int getIntProp(int paramInt1, int paramInt2)
/*      */   {
/*  506 */     PropListItem localPropListItem = lookupProperty(paramInt1);
/*  507 */     if (localPropListItem == null) return paramInt2;
/*  508 */     return localPropListItem.intValue;
/*      */   }
/*      */ 
/*      */   public int getExistingIntProp(int paramInt)
/*      */   {
/*  513 */     PropListItem localPropListItem = lookupProperty(paramInt);
/*  514 */     if (localPropListItem == null) Kit.codeBug();
/*  515 */     return localPropListItem.intValue;
/*      */   }
/*      */ 
/*      */   public void putProp(int paramInt, Object paramObject)
/*      */   {
/*  520 */     if (paramObject == null) {
/*  521 */       removeProp(paramInt);
/*      */     } else {
/*  523 */       PropListItem localPropListItem = ensureProperty(paramInt);
/*  524 */       localPropListItem.objectValue = paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putIntProp(int paramInt1, int paramInt2)
/*      */   {
/*  530 */     PropListItem localPropListItem = ensureProperty(paramInt1);
/*  531 */     localPropListItem.intValue = paramInt2;
/*      */   }
/*      */ 
/*      */   public int getLineno()
/*      */   {
/*  539 */     return this.lineno;
/*      */   }
/*      */ 
/*      */   public void setLineno(int paramInt) {
/*  543 */     this.lineno = paramInt;
/*      */   }
/*      */ 
/*      */   public final double getDouble()
/*      */   {
/*  548 */     return ((NumberLiteral)this).getNumber();
/*      */   }
/*      */ 
/*      */   public final void setDouble(double paramDouble) {
/*  552 */     ((NumberLiteral)this).setNumber(paramDouble);
/*      */   }
/*      */ 
/*      */   public final String getString()
/*      */   {
/*  557 */     return ((Name)this).getIdentifier();
/*      */   }
/*      */ 
/*      */   public final void setString(String paramString)
/*      */   {
/*  562 */     if (paramString == null) Kit.codeBug();
/*  563 */     ((Name)this).setIdentifier(paramString);
/*      */   }
/*      */ 
/*      */   public Scope getScope()
/*      */   {
/*  568 */     return ((Name)this).getScope();
/*      */   }
/*      */ 
/*      */   public void setScope(Scope paramScope)
/*      */   {
/*  573 */     if (paramScope == null) Kit.codeBug();
/*  574 */     if (!(this instanceof Name)) {
/*  575 */       throw Kit.codeBug();
/*      */     }
/*  577 */     ((Name)this).setScope(paramScope);
/*      */   }
/*      */ 
/*      */   public static Node newTarget()
/*      */   {
/*  582 */     return new Node(131);
/*      */   }
/*      */ 
/*      */   public final int labelId()
/*      */   {
/*  587 */     if ((this.type != 131) && (this.type != 72)) Kit.codeBug();
/*  588 */     return getIntProp(15, -1);
/*      */   }
/*      */ 
/*      */   public void labelId(int paramInt)
/*      */   {
/*  593 */     if ((this.type != 131) && (this.type != 72)) Kit.codeBug();
/*  594 */     putIntProp(15, paramInt);
/*      */   }
/*      */ 
/*      */   public boolean hasConsistentReturnUsage()
/*      */   {
/*  664 */     int i = endCheck();
/*  665 */     return ((i & 0x4) == 0) || ((i & 0xB) == 0);
/*      */   }
/*      */ 
/*      */   private int endCheckIf()
/*      */   {
/*  677 */     int i = 0;
/*      */ 
/*  679 */     Node localNode1 = this.next;
/*  680 */     Node localNode2 = ((Jump)this).target;
/*      */ 
/*  682 */     i = localNode1.endCheck();
/*      */ 
/*  684 */     if (localNode2 != null)
/*  685 */       i |= localNode2.endCheck();
/*      */     else {
/*  687 */       i |= 1;
/*      */     }
/*  689 */     return i;
/*      */   }
/*      */ 
/*      */   private int endCheckSwitch()
/*      */   {
/*  701 */     int i = 0;
/*      */ 
/*  725 */     return i;
/*      */   }
/*      */ 
/*      */   private int endCheckTry()
/*      */   {
/*  738 */     int i = 0;
/*      */ 
/*  771 */     return i;
/*      */   }
/*      */ 
/*      */   private int endCheckLoop()
/*      */   {
/*  791 */     int i = 0;
/*      */ 
/*  798 */     for (Node localNode = this.first; localNode.next != this.last; localNode = localNode.next);
/*  801 */     if (localNode.type != 6) {
/*  802 */       return 1;
/*      */     }
/*      */ 
/*  805 */     i = ((Jump)localNode).target.next.endCheck();
/*      */ 
/*  808 */     if (localNode.first.type == 45) {
/*  809 */       i &= -2;
/*      */     }
/*      */ 
/*  812 */     i |= getIntProp(18, 0);
/*      */ 
/*  814 */     return i;
/*      */   }
/*      */ 
/*      */   private int endCheckBlock()
/*      */   {
/*  826 */     int i = 1;
/*      */ 
/*  830 */     for (Node localNode = this.first; ((i & 0x1) != 0) && (localNode != null); localNode = localNode.next)
/*      */     {
/*  832 */       i &= -2;
/*  833 */       i |= localNode.endCheck();
/*      */     }
/*  835 */     return i;
/*      */   }
/*      */ 
/*      */   private int endCheckLabel()
/*      */   {
/*  847 */     int i = 0;
/*      */ 
/*  849 */     i = this.next.endCheck();
/*  850 */     i |= getIntProp(18, 0);
/*      */ 
/*  852 */     return i;
/*      */   }
/*      */ 
/*      */   private int endCheckBreak()
/*      */   {
/*  862 */     Jump localJump = ((Jump)this).getJumpStatement();
/*  863 */     localJump.putIntProp(18, 1);
/*  864 */     return 0;
/*      */   }
/*      */ 
/*      */   private int endCheck()
/*      */   {
/*  878 */     switch (this.type)
/*      */     {
/*      */     case 120:
/*  881 */       return endCheckBreak();
/*      */     case 133:
/*  884 */       if (this.first != null)
/*  885 */         return this.first.endCheck();
/*  886 */       return 1;
/*      */     case 72:
/*  889 */       return 8;
/*      */     case 50:
/*      */     case 121:
/*  893 */       return 0;
/*      */     case 4:
/*  896 */       if (this.first != null) {
/*  897 */         return 4;
/*      */       }
/*  899 */       return 2;
/*      */     case 131:
/*  902 */       if (this.next != null) {
/*  903 */         return this.next.endCheck();
/*      */       }
/*  905 */       return 1;
/*      */     case 132:
/*  908 */       return endCheckLoop();
/*      */     case 129:
/*      */     case 141:
/*  913 */       if (this.first == null) {
/*  914 */         return 1;
/*      */       }
/*  916 */       switch (this.first.type) {
/*      */       case 130:
/*  918 */         return this.first.endCheckLabel();
/*      */       case 7:
/*  921 */         return this.first.endCheckIf();
/*      */       case 114:
/*  924 */         return this.first.endCheckSwitch();
/*      */       case 81:
/*  927 */         return this.first.endCheckTry();
/*      */       }
/*      */ 
/*  930 */       return endCheckBlock();
/*      */     }
/*      */ 
/*  934 */     return 1;
/*      */   }
/*      */ 
/*      */   public boolean hasSideEffects()
/*      */   {
/*  940 */     switch (this.type) {
/*      */     case 89:
/*      */     case 133:
/*  943 */       if (this.last != null) {
/*  944 */         return this.last.hasSideEffects();
/*      */       }
/*  946 */       return true;
/*      */     case 102:
/*  949 */       if ((this.first == null) || (this.first.next == null) || (this.first.next.next == null))
/*      */       {
/*  952 */         Kit.codeBug();
/*  953 */       }return (this.first.next.hasSideEffects()) && (this.first.next.next.hasSideEffects());
/*      */     case 104:
/*      */     case 105:
/*  958 */       if ((this.first == null) || (this.last == null))
/*  959 */         Kit.codeBug();
/*  960 */       return (this.first.hasSideEffects()) || (this.last.hasSideEffects());
/*      */     case -1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 30:
/*      */     case 31:
/*      */     case 35:
/*      */     case 37:
/*      */     case 38:
/*      */     case 50:
/*      */     case 51:
/*      */     case 56:
/*      */     case 57:
/*      */     case 64:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 72:
/*      */     case 81:
/*      */     case 82:
/*      */     case 90:
/*      */     case 91:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 95:
/*      */     case 96:
/*      */     case 97:
/*      */     case 98:
/*      */     case 99:
/*      */     case 100:
/*      */     case 101:
/*      */     case 106:
/*      */     case 107:
/*      */     case 112:
/*      */     case 113:
/*      */     case 114:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 120:
/*      */     case 121:
/*      */     case 122:
/*      */     case 123:
/*      */     case 124:
/*      */     case 125:
/*      */     case 129:
/*      */     case 130:
/*      */     case 131:
/*      */     case 132:
/*      */     case 134:
/*      */     case 135:
/*      */     case 139:
/*      */     case 140:
/*      */     case 141:
/*      */     case 142:
/*      */     case 153:
/*      */     case 154:
/*      */     case 158:
/*      */     case 159:
/* 1026 */       return true;
/*      */     case 0:
/*      */     case 1:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 36:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 48:
/*      */     case 49:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 71:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 103:
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 111:
/*      */     case 115:
/*      */     case 116:
/*      */     case 126:
/*      */     case 127:
/*      */     case 128:
/*      */     case 136:
/*      */     case 137:
/*      */     case 138:
/*      */     case 143:
/*      */     case 144:
/*      */     case 145:
/*      */     case 146:
/*      */     case 147:
/*      */     case 148:
/*      */     case 149:
/*      */     case 150:
/*      */     case 151:
/*      */     case 152:
/*      */     case 155:
/*      */     case 156:
/* 1029 */     case 157: } return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1041 */     return String.valueOf(this.type);
/*      */   }
/*      */ 
/*      */   private void toString(ObjToIntMap paramObjToIntMap, StringBuffer paramStringBuffer)
/*      */   {
/*      */   }
/*      */ 
/*      */   public String toStringTree(ScriptNode paramScriptNode)
/*      */   {
/* 1208 */     return null;
/*      */   }
/*      */ 
/*      */   private static void toStringTreeHelper(ScriptNode paramScriptNode, Node paramNode, ObjToIntMap paramObjToIntMap, int paramInt, StringBuffer paramStringBuffer)
/*      */   {
/*      */   }
/*      */ 
/*      */   private static void generatePrintIds(Node paramNode, ObjToIntMap paramObjToIntMap)
/*      */   {
/*      */   }
/*      */ 
/*      */   private static void appendPrintId(Node paramNode, ObjToIntMap paramObjToIntMap, StringBuffer paramStringBuffer)
/*      */   {
/*      */   }
/*      */ 
/*      */   public class NodeIterator
/*      */     implements Iterator<Node>
/*      */   {
/*      */     private Node cursor;
/*  370 */     private Node prev = Node.NOT_SET;
/*      */     private Node prev2;
/*  372 */     private boolean removed = false;
/*      */ 
/*      */     public NodeIterator() {
/*  375 */       this.cursor = Node.this.first;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/*  379 */       return this.cursor != null;
/*      */     }
/*      */ 
/*      */     public Node next() {
/*  383 */       if (this.cursor == null) {
/*  384 */         throw new NoSuchElementException();
/*      */       }
/*  386 */       this.removed = false;
/*  387 */       this.prev2 = this.prev;
/*  388 */       this.prev = this.cursor;
/*  389 */       this.cursor = this.cursor.next;
/*  390 */       return this.prev;
/*      */     }
/*      */ 
/*      */     public void remove() {
/*  394 */       if (this.prev == Node.NOT_SET) {
/*  395 */         throw new IllegalStateException("next() has not been called");
/*      */       }
/*  397 */       if (this.removed) {
/*  398 */         throw new IllegalStateException("remove() already called for current element");
/*      */       }
/*      */ 
/*  401 */       if (this.prev == Node.this.first) {
/*  402 */         Node.this.first = this.prev.next;
/*  403 */       } else if (this.prev == Node.this.last) {
/*  404 */         this.prev2.next = null;
/*  405 */         Node.this.last = this.prev2;
/*      */       } else {
/*  407 */         this.prev2.next = this.cursor;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class PropListItem
/*      */   {
/*      */     PropListItem next;
/*      */     int type;
/*      */     int intValue;
/*      */     Object objectValue;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Node
 * JD-Core Version:    0.6.2
 */