/*      */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*      */ 
/*      */ class MethodWriter
/*      */   implements MethodVisitor
/*      */ {
/*      */   static final int ACC_CONSTRUCTOR = 262144;
/*      */   static final int SAME_FRAME = 0;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
/*      */   static final int RESERVED = 128;
/*      */   static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
/*      */   static final int CHOP_FRAME = 248;
/*      */   static final int SAME_FRAME_EXTENDED = 251;
/*      */   static final int APPEND_FRAME = 252;
/*      */   static final int FULL_FRAME = 255;
/*      */   private static final int FRAMES = 0;
/*      */   private static final int MAXS = 1;
/*      */   private static final int NOTHING = 2;
/*      */   MethodWriter next;
/*      */   final ClassWriter cw;
/*      */   private int access;
/*      */   private final int name;
/*      */   private final int desc;
/*      */   private final String descriptor;
/*      */   String signature;
/*      */   int classReaderOffset;
/*      */   int classReaderLength;
/*      */   int exceptionCount;
/*      */   int[] exceptions;
/*      */   private ByteVector annd;
/*      */   private AnnotationWriter anns;
/*      */   private AnnotationWriter ianns;
/*      */   private AnnotationWriter[] panns;
/*      */   private AnnotationWriter[] ipanns;
/*      */   private int synthetics;
/*      */   private Attribute attrs;
/*  254 */   private ByteVector code = new ByteVector();
/*      */   private int maxStack;
/*      */   private int maxLocals;
/*      */   private int frameCount;
/*      */   private ByteVector stackMap;
/*      */   private int previousFrameOffset;
/*      */   private int[] previousFrame;
/*      */   private int frameIndex;
/*      */   private int[] frame;
/*      */   private int handlerCount;
/*      */   private Handler firstHandler;
/*      */   private Handler lastHandler;
/*      */   private int localVarCount;
/*      */   private ByteVector localVar;
/*      */   private int localVarTypeCount;
/*      */   private ByteVector localVarType;
/*      */   private int lineNumberCount;
/*      */   private ByteVector lineNumber;
/*      */   private Attribute cattrs;
/*      */   private boolean resize;
/*      */   private int subroutines;
/*      */   private final int compute;
/*      */   private Label labels;
/*      */   private Label previousBlock;
/*      */   private Label currentBlock;
/*      */   private int stackSize;
/*      */   private int maxStackSize;
/*      */ 
/*      */   MethodWriter(ClassWriter cw, int access, String name, String desc, String signature, String[] exceptions, boolean computeMaxs, boolean computeFrames)
/*      */   {
/*  451 */     if (cw.firstMethod == null)
/*  452 */       cw.firstMethod = this;
/*      */     else {
/*  454 */       cw.lastMethod.next = this;
/*      */     }
/*  456 */     cw.lastMethod = this;
/*  457 */     this.cw = cw;
/*  458 */     this.access = access;
/*  459 */     this.name = cw.newUTF8(name);
/*  460 */     this.desc = cw.newUTF8(desc);
/*  461 */     this.descriptor = desc;
/*      */ 
/*  463 */     this.signature = signature;
/*      */ 
/*  465 */     if ((exceptions != null) && (exceptions.length > 0)) {
/*  466 */       this.exceptionCount = exceptions.length;
/*  467 */       this.exceptions = new int[this.exceptionCount];
/*  468 */       for (int i = 0; i < this.exceptionCount; i++) {
/*  469 */         this.exceptions[i] = cw.newClass(exceptions[i]);
/*      */       }
/*      */     }
/*  472 */     this.compute = (computeMaxs ? 1 : computeFrames ? 0 : 2);
/*  473 */     if ((computeMaxs) || (computeFrames)) {
/*  474 */       if ((computeFrames) && ("<init>".equals(name))) {
/*  475 */         this.access |= 262144;
/*      */       }
/*      */ 
/*  478 */       int size = getArgumentsAndReturnSizes(this.descriptor) >> 2;
/*  479 */       if ((access & 0x8) != 0) {
/*  480 */         size--;
/*      */       }
/*  482 */       this.maxLocals = size;
/*      */ 
/*  484 */       this.labels = new Label();
/*  485 */       this.labels.status |= 8;
/*  486 */       visitLabel(this.labels);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AnnotationVisitor visitAnnotationDefault()
/*      */   {
/*  498 */     this.annd = new ByteVector();
/*  499 */     return new AnnotationWriter(this.cw, false, this.annd, null, 0);
/*      */   }
/*      */ 
/*      */   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
/*      */   {
/*  509 */     ByteVector bv = new ByteVector();
/*      */ 
/*  511 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/*  512 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
/*  513 */     if (visible) {
/*  514 */       aw.next = this.anns;
/*  515 */       this.anns = aw;
/*      */     } else {
/*  517 */       aw.next = this.ianns;
/*  518 */       this.ianns = aw;
/*      */     }
/*  520 */     return aw;
/*      */   }
/*      */ 
/*      */   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
/*      */   {
/*  531 */     ByteVector bv = new ByteVector();
/*  532 */     if ("Ljava/lang/Synthetic;".equals(desc))
/*      */     {
/*  535 */       this.synthetics = Math.max(this.synthetics, parameter + 1);
/*  536 */       return new AnnotationWriter(this.cw, false, bv, null, 0);
/*      */     }
/*      */ 
/*  539 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/*  540 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
/*  541 */     if (visible) {
/*  542 */       if (this.panns == null) {
/*  543 */         this.panns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
/*      */       }
/*  545 */       aw.next = this.panns[parameter];
/*  546 */       this.panns[parameter] = aw;
/*      */     } else {
/*  548 */       if (this.ipanns == null) {
/*  549 */         this.ipanns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
/*      */       }
/*  551 */       aw.next = this.ipanns[parameter];
/*  552 */       this.ipanns[parameter] = aw;
/*      */     }
/*  554 */     return aw;
/*      */   }
/*      */ 
/*      */   public void visitAttribute(Attribute attr) {
/*  558 */     if (attr.isCodeAttribute()) {
/*  559 */       attr.next = this.cattrs;
/*  560 */       this.cattrs = attr;
/*      */     } else {
/*  562 */       attr.next = this.attrs;
/*  563 */       this.attrs = attr;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitCode()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
/*      */   {
/*  577 */     if (this.compute == 0) {
/*  578 */       return;
/*      */     }
/*      */ 
/*  581 */     if (type == -1) {
/*  582 */       startFrame(this.code.length, nLocal, nStack);
/*  583 */       for (int i = 0; i < nLocal; i++) {
/*  584 */         if ((local[i] instanceof String)) {
/*  585 */           this.frame[(this.frameIndex++)] = (0x1700000 | this.cw.addType((String)local[i]));
/*      */         }
/*  587 */         else if ((local[i] instanceof Integer))
/*  588 */           this.frame[(this.frameIndex++)] = ((Integer)local[i]).intValue();
/*      */         else {
/*  590 */           this.frame[(this.frameIndex++)] = (0x1800000 | this.cw.addUninitializedType("", ((Label)local[i]).position));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  595 */       for (int i = 0; i < nStack; i++) {
/*  596 */         if ((stack[i] instanceof String)) {
/*  597 */           this.frame[(this.frameIndex++)] = (0x1700000 | this.cw.addType((String)stack[i]));
/*      */         }
/*  599 */         else if ((stack[i] instanceof Integer))
/*  600 */           this.frame[(this.frameIndex++)] = ((Integer)stack[i]).intValue();
/*      */         else {
/*  602 */           this.frame[(this.frameIndex++)] = (0x1800000 | this.cw.addUninitializedType("", ((Label)stack[i]).position));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  607 */       endFrame();
/*      */     }
/*      */     else
/*      */     {
/*      */       int delta;
/*      */       int delta;
/*  610 */       if (this.stackMap == null) {
/*  611 */         this.stackMap = new ByteVector();
/*  612 */         delta = this.code.length;
/*      */       } else {
/*  614 */         delta = this.code.length - this.previousFrameOffset - 1;
/*      */       }
/*      */ 
/*  617 */       switch (type) {
/*      */       case 0:
/*  619 */         this.stackMap.putByte(255).putShort(delta).putShort(nLocal);
/*      */ 
/*  622 */         for (int i = 0; i < nLocal; i++) {
/*  623 */           writeFrameType(local[i]);
/*      */         }
/*  625 */         this.stackMap.putShort(nStack);
/*  626 */         for (int i = 0; i < nStack; i++) {
/*  627 */           writeFrameType(stack[i]);
/*      */         }
/*  629 */         break;
/*      */       case 1:
/*  631 */         this.stackMap.putByte(251 + nLocal).putShort(delta);
/*      */ 
/*  633 */         for (int i = 0; i < nLocal; i++) {
/*  634 */           writeFrameType(local[i]);
/*      */         }
/*  636 */         break;
/*      */       case 2:
/*  638 */         this.stackMap.putByte(251 - nLocal).putShort(delta);
/*      */ 
/*  640 */         break;
/*      */       case 3:
/*  642 */         if (delta < 64)
/*  643 */           this.stackMap.putByte(delta);
/*      */         else {
/*  645 */           this.stackMap.putByte(251).putShort(delta);
/*      */         }
/*  647 */         break;
/*      */       case 4:
/*  649 */         if (delta < 64)
/*  650 */           this.stackMap.putByte(64 + delta);
/*      */         else {
/*  652 */           this.stackMap.putByte(247).putShort(delta);
/*      */         }
/*      */ 
/*  655 */         writeFrameType(stack[0]);
/*      */       }
/*      */ 
/*  659 */       this.previousFrameOffset = this.code.length;
/*  660 */       this.frameCount += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitInsn(int opcode)
/*      */   {
/*  666 */     this.code.putByte(opcode);
/*      */ 
/*  669 */     if (this.currentBlock != null) {
/*  670 */       if (this.compute == 0) {
/*  671 */         this.currentBlock.frame.execute(opcode, 0, null, null);
/*      */       }
/*      */       else {
/*  674 */         int size = this.stackSize + Frame.SIZE[opcode];
/*  675 */         if (size > this.maxStackSize) {
/*  676 */           this.maxStackSize = size;
/*      */         }
/*  678 */         this.stackSize = size;
/*      */       }
/*      */ 
/*  681 */       if (((opcode >= 172) && (opcode <= 177)) || (opcode == 191))
/*      */       {
/*  684 */         noSuccessor();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitIntInsn(int opcode, int operand)
/*      */   {
/*  691 */     if (this.currentBlock != null) {
/*  692 */       if (this.compute == 0) {
/*  693 */         this.currentBlock.frame.execute(opcode, operand, null, null);
/*  694 */       } else if (opcode != 188)
/*      */       {
/*  697 */         int size = this.stackSize + 1;
/*  698 */         if (size > this.maxStackSize) {
/*  699 */           this.maxStackSize = size;
/*      */         }
/*  701 */         this.stackSize = size;
/*      */       }
/*      */     }
/*      */ 
/*  705 */     if (opcode == 17)
/*  706 */       this.code.put12(opcode, operand);
/*      */     else
/*  708 */       this.code.put11(opcode, operand);
/*      */   }
/*      */ 
/*      */   public void visitVarInsn(int opcode, int var)
/*      */   {
/*  714 */     if (this.currentBlock != null) {
/*  715 */       if (this.compute == 0) {
/*  716 */         this.currentBlock.frame.execute(opcode, var, null, null);
/*      */       }
/*  719 */       else if (opcode == 169)
/*      */       {
/*  721 */         this.currentBlock.status |= 256;
/*      */ 
/*  724 */         this.currentBlock.inputStackTop = this.stackSize;
/*  725 */         noSuccessor();
/*      */       } else {
/*  727 */         int size = this.stackSize + Frame.SIZE[opcode];
/*  728 */         if (size > this.maxStackSize) {
/*  729 */           this.maxStackSize = size;
/*      */         }
/*  731 */         this.stackSize = size;
/*      */       }
/*      */     }
/*      */ 
/*  735 */     if (this.compute != 2)
/*      */     {
/*      */       int n;
/*      */       int n;
/*  738 */       if ((opcode == 22) || (opcode == 24) || (opcode == 55) || (opcode == 57))
/*      */       {
/*  741 */         n = var + 2;
/*      */       }
/*  743 */       else n = var + 1;
/*      */ 
/*  745 */       if (n > this.maxLocals) {
/*  746 */         this.maxLocals = n;
/*      */       }
/*      */     }
/*      */ 
/*  750 */     if ((var < 4) && (opcode != 169))
/*      */     {
/*      */       int opt;
/*      */       int opt;
/*  752 */       if (opcode < 54)
/*      */       {
/*  754 */         opt = 26 + (opcode - 21 << 2) + var;
/*      */       }
/*      */       else {
/*  757 */         opt = 59 + (opcode - 54 << 2) + var;
/*      */       }
/*  759 */       this.code.putByte(opt);
/*  760 */     } else if (var >= 256) {
/*  761 */       this.code.putByte(196).put12(opcode, var);
/*      */     } else {
/*  763 */       this.code.put11(opcode, var);
/*      */     }
/*  765 */     if ((opcode >= 54) && (this.compute == 0) && (this.handlerCount > 0))
/*  766 */       visitLabel(new Label());
/*      */   }
/*      */ 
/*      */   public void visitTypeInsn(int opcode, String type)
/*      */   {
/*  771 */     Item i = this.cw.newClassItem(type);
/*      */ 
/*  773 */     if (this.currentBlock != null) {
/*  774 */       if (this.compute == 0) {
/*  775 */         this.currentBlock.frame.execute(opcode, this.code.length, this.cw, i);
/*  776 */       } else if (opcode == 187)
/*      */       {
/*  779 */         int size = this.stackSize + 1;
/*  780 */         if (size > this.maxStackSize) {
/*  781 */           this.maxStackSize = size;
/*      */         }
/*  783 */         this.stackSize = size;
/*      */       }
/*      */     }
/*      */ 
/*  787 */     this.code.put12(opcode, i.index);
/*      */   }
/*      */ 
/*      */   public void visitFieldInsn(int opcode, String owner, String name, String desc)
/*      */   {
/*  796 */     Item i = this.cw.newFieldItem(owner, name, desc);
/*      */ 
/*  798 */     if (this.currentBlock != null) {
/*  799 */       if (this.compute == 0) {
/*  800 */         this.currentBlock.frame.execute(opcode, 0, this.cw, i);
/*      */       }
/*      */       else
/*      */       {
/*  804 */         char c = desc.charAt(0);
/*      */         int size;
/*  805 */         switch (opcode) {
/*      */         case 178:
/*  807 */           size = this.stackSize + ((c == 'D') || (c == 'J') ? 2 : 1);
/*  808 */           break;
/*      */         case 179:
/*  810 */           size = this.stackSize + ((c == 'D') || (c == 'J') ? -2 : -1);
/*  811 */           break;
/*      */         case 180:
/*  813 */           size = this.stackSize + ((c == 'D') || (c == 'J') ? 1 : 0);
/*  814 */           break;
/*      */         default:
/*  817 */           size = this.stackSize + ((c == 'D') || (c == 'J') ? -3 : -2);
/*      */         }
/*      */ 
/*  821 */         if (size > this.maxStackSize) {
/*  822 */           this.maxStackSize = size;
/*      */         }
/*  824 */         this.stackSize = size;
/*      */       }
/*      */     }
/*      */ 
/*  828 */     this.code.put12(opcode, i.index);
/*      */   }
/*      */ 
/*      */   public void visitMethodInsn(int opcode, String owner, String name, String desc)
/*      */   {
/*  837 */     boolean itf = opcode == 185;
/*  838 */     Item i = this.cw.newMethodItem(owner, name, desc, itf);
/*  839 */     int argSize = i.intVal;
/*      */ 
/*  841 */     if (this.currentBlock != null) {
/*  842 */       if (this.compute == 0) {
/*  843 */         this.currentBlock.frame.execute(opcode, 0, this.cw, i);
/*      */       }
/*      */       else
/*      */       {
/*  853 */         if (argSize == 0)
/*      */         {
/*  856 */           argSize = getArgumentsAndReturnSizes(desc);
/*      */ 
/*  859 */           i.intVal = argSize;
/*      */         }
/*      */         int size;
/*      */         int size;
/*  862 */         if (opcode == 184)
/*  863 */           size = this.stackSize - (argSize >> 2) + (argSize & 0x3) + 1;
/*      */         else {
/*  865 */           size = this.stackSize - (argSize >> 2) + (argSize & 0x3);
/*      */         }
/*      */ 
/*  868 */         if (size > this.maxStackSize) {
/*  869 */           this.maxStackSize = size;
/*      */         }
/*  871 */         this.stackSize = size;
/*      */       }
/*      */     }
/*      */ 
/*  875 */     if (itf) {
/*  876 */       if (argSize == 0) {
/*  877 */         argSize = getArgumentsAndReturnSizes(desc);
/*  878 */         i.intVal = argSize;
/*      */       }
/*  880 */       this.code.put12(185, i.index).put11(argSize >> 2, 0);
/*      */     } else {
/*  882 */       this.code.put12(opcode, i.index);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitJumpInsn(int opcode, Label label) {
/*  887 */     Label nextInsn = null;
/*      */ 
/*  889 */     if (this.currentBlock != null) {
/*  890 */       if (this.compute == 0) {
/*  891 */         this.currentBlock.frame.execute(opcode, 0, null, null);
/*      */ 
/*  893 */         label.getFirst().status |= 16;
/*      */ 
/*  895 */         addSuccessor(0, label);
/*  896 */         if (opcode != 167)
/*      */         {
/*  898 */           nextInsn = new Label();
/*      */         }
/*      */       }
/*  901 */       else if (opcode == 168) {
/*  902 */         if ((label.status & 0x200) == 0) {
/*  903 */           label.status |= 512;
/*  904 */           this.subroutines += 1;
/*      */         }
/*  906 */         this.currentBlock.status |= 128;
/*  907 */         addSuccessor(this.stackSize + 1, label);
/*      */ 
/*  909 */         nextInsn = new Label();
/*      */       }
/*      */       else
/*      */       {
/*  920 */         this.stackSize += Frame.SIZE[opcode];
/*  921 */         addSuccessor(this.stackSize, label);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  926 */     if (((label.status & 0x2) != 0) && (label.position - this.code.length < -32768))
/*      */     {
/*  936 */       if (opcode == 167) {
/*  937 */         this.code.putByte(200);
/*  938 */       } else if (opcode == 168) {
/*  939 */         this.code.putByte(201);
/*      */       }
/*      */       else
/*      */       {
/*  943 */         if (nextInsn != null) {
/*  944 */           nextInsn.status |= 16;
/*      */         }
/*  946 */         this.code.putByte(opcode <= 166 ? (opcode + 1 ^ 0x1) - 1 : opcode ^ 0x1);
/*      */ 
/*  949 */         this.code.putShort(8);
/*  950 */         this.code.putByte(200);
/*      */       }
/*  952 */       label.put(this, this.code, this.code.length - 1, true);
/*      */     }
/*      */     else
/*      */     {
/*  960 */       this.code.putByte(opcode);
/*  961 */       label.put(this, this.code, this.code.length - 1, false);
/*      */     }
/*  963 */     if (this.currentBlock != null) {
/*  964 */       if (nextInsn != null)
/*      */       {
/*  969 */         visitLabel(nextInsn);
/*      */       }
/*  971 */       if (opcode == 167)
/*  972 */         noSuccessor();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLabel(Label label)
/*      */   {
/*  979 */     this.resize |= label.resolve(this, this.code.length, this.code.data);
/*      */ 
/*  981 */     if ((label.status & 0x1) != 0) {
/*  982 */       return;
/*      */     }
/*  984 */     if (this.compute == 0) {
/*  985 */       if (this.currentBlock != null) {
/*  986 */         if (label.position == this.currentBlock.position)
/*      */         {
/*  988 */           this.currentBlock.status |= label.status & 0x10;
/*  989 */           label.frame = this.currentBlock.frame;
/*  990 */           return;
/*      */         }
/*      */ 
/*  993 */         addSuccessor(0, label);
/*      */       }
/*      */ 
/*  996 */       this.currentBlock = label;
/*  997 */       if (label.frame == null) {
/*  998 */         label.frame = new Frame();
/*  999 */         label.frame.owner = label;
/*      */       }
/*      */ 
/* 1002 */       if (this.previousBlock != null) {
/* 1003 */         if (label.position == this.previousBlock.position) {
/* 1004 */           this.previousBlock.status |= label.status & 0x10;
/* 1005 */           label.frame = this.previousBlock.frame;
/* 1006 */           this.currentBlock = this.previousBlock;
/* 1007 */           return;
/*      */         }
/* 1009 */         this.previousBlock.successor = label;
/*      */       }
/* 1011 */       this.previousBlock = label;
/* 1012 */     } else if (this.compute == 1) {
/* 1013 */       if (this.currentBlock != null)
/*      */       {
/* 1015 */         this.currentBlock.outputStackMax = this.maxStackSize;
/* 1016 */         addSuccessor(this.stackSize, label);
/*      */       }
/*      */ 
/* 1019 */       this.currentBlock = label;
/*      */ 
/* 1021 */       this.stackSize = 0;
/* 1022 */       this.maxStackSize = 0;
/*      */ 
/* 1024 */       if (this.previousBlock != null) {
/* 1025 */         this.previousBlock.successor = label;
/*      */       }
/* 1027 */       this.previousBlock = label;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLdcInsn(Object cst) {
/* 1032 */     Item i = this.cw.newConstItem(cst);
/*      */ 
/* 1034 */     if (this.currentBlock != null) {
/* 1035 */       if (this.compute == 0) {
/* 1036 */         this.currentBlock.frame.execute(18, 0, this.cw, i);
/*      */       }
/*      */       else
/*      */       {
/*      */         int size;
/*      */         int size;
/* 1040 */         if ((i.type == 5) || (i.type == 6))
/*      */         {
/* 1042 */           size = this.stackSize + 2;
/*      */         }
/* 1044 */         else size = this.stackSize + 1;
/*      */ 
/* 1047 */         if (size > this.maxStackSize) {
/* 1048 */           this.maxStackSize = size;
/*      */         }
/* 1050 */         this.stackSize = size;
/*      */       }
/*      */     }
/*      */ 
/* 1054 */     int index = i.index;
/* 1055 */     if ((i.type == 5) || (i.type == 6))
/* 1056 */       this.code.put12(20, index);
/* 1057 */     else if (index >= 256)
/* 1058 */       this.code.put12(19, index);
/*      */     else
/* 1060 */       this.code.put11(18, index);
/*      */   }
/*      */ 
/*      */   public void visitIincInsn(int var, int increment)
/*      */   {
/* 1065 */     if ((this.currentBlock != null) && 
/* 1066 */       (this.compute == 0)) {
/* 1067 */       this.currentBlock.frame.execute(132, var, null, null);
/*      */     }
/*      */ 
/* 1070 */     if (this.compute != 2)
/*      */     {
/* 1072 */       int n = var + 1;
/* 1073 */       if (n > this.maxLocals) {
/* 1074 */         this.maxLocals = n;
/*      */       }
/*      */     }
/*      */ 
/* 1078 */     if ((var > 255) || (increment > 127) || (increment < -128)) {
/* 1079 */       this.code.putByte(196).put12(132, var).putShort(increment);
/*      */     }
/*      */     else
/*      */     {
/* 1083 */       this.code.putByte(132).put11(var, increment);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels)
/*      */   {
/* 1094 */     int source = this.code.length;
/* 1095 */     this.code.putByte(170);
/* 1096 */     this.code.length += (4 - this.code.length % 4) % 4;
/* 1097 */     dflt.put(this, this.code, source, true);
/* 1098 */     this.code.putInt(min).putInt(max);
/* 1099 */     for (int i = 0; i < labels.length; i++) {
/* 1100 */       labels[i].put(this, this.code, source, true);
/*      */     }
/*      */ 
/* 1103 */     visitSwitchInsn(dflt, labels);
/*      */   }
/*      */ 
/*      */   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
/*      */   {
/* 1112 */     int source = this.code.length;
/* 1113 */     this.code.putByte(171);
/* 1114 */     this.code.length += (4 - this.code.length % 4) % 4;
/* 1115 */     dflt.put(this, this.code, source, true);
/* 1116 */     this.code.putInt(labels.length);
/* 1117 */     for (int i = 0; i < labels.length; i++) {
/* 1118 */       this.code.putInt(keys[i]);
/* 1119 */       labels[i].put(this, this.code, source, true);
/*      */     }
/*      */ 
/* 1122 */     visitSwitchInsn(dflt, labels);
/*      */   }
/*      */ 
/*      */   private void visitSwitchInsn(Label dflt, Label[] labels)
/*      */   {
/* 1127 */     if (this.currentBlock != null) {
/* 1128 */       if (this.compute == 0) {
/* 1129 */         this.currentBlock.frame.execute(171, 0, null, null);
/*      */ 
/* 1131 */         addSuccessor(0, dflt);
/* 1132 */         dflt.getFirst().status |= 16;
/* 1133 */         for (int i = 0; i < labels.length; i++) {
/* 1134 */           addSuccessor(0, labels[i]);
/* 1135 */           labels[i].getFirst().status |= 16;
/*      */         }
/*      */       }
/*      */       else {
/* 1139 */         this.stackSize -= 1;
/*      */ 
/* 1141 */         addSuccessor(this.stackSize, dflt);
/* 1142 */         for (int i = 0; i < labels.length; i++) {
/* 1143 */           addSuccessor(this.stackSize, labels[i]);
/*      */         }
/*      */       }
/*      */ 
/* 1147 */       noSuccessor();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitMultiANewArrayInsn(String desc, int dims) {
/* 1152 */     Item i = this.cw.newClassItem(desc);
/*      */ 
/* 1154 */     if (this.currentBlock != null) {
/* 1155 */       if (this.compute == 0) {
/* 1156 */         this.currentBlock.frame.execute(197, dims, this.cw, i);
/*      */       }
/*      */       else
/*      */       {
/* 1160 */         this.stackSize += 1 - dims;
/*      */       }
/*      */     }
/*      */ 
/* 1164 */     this.code.put12(197, i.index).putByte(dims);
/*      */   }
/*      */ 
/*      */   public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
/*      */   {
/* 1173 */     this.handlerCount += 1;
/* 1174 */     Handler h = new Handler();
/* 1175 */     h.start = start;
/* 1176 */     h.end = end;
/* 1177 */     h.handler = handler;
/* 1178 */     h.desc = type;
/* 1179 */     h.type = (type != null ? this.cw.newClass(type) : 0);
/* 1180 */     if (this.lastHandler == null)
/* 1181 */       this.firstHandler = h;
/*      */     else {
/* 1183 */       this.lastHandler.next = h;
/*      */     }
/* 1185 */     this.lastHandler = h;
/*      */   }
/*      */ 
/*      */   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
/*      */   {
/* 1196 */     if (signature != null) {
/* 1197 */       if (this.localVarType == null) {
/* 1198 */         this.localVarType = new ByteVector();
/*      */       }
/* 1200 */       this.localVarTypeCount += 1;
/* 1201 */       this.localVarType.putShort(start.position).putShort(end.position - start.position).putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(signature)).putShort(index);
/*      */     }
/*      */ 
/* 1207 */     if (this.localVar == null) {
/* 1208 */       this.localVar = new ByteVector();
/*      */     }
/* 1210 */     this.localVarCount += 1;
/* 1211 */     this.localVar.putShort(start.position).putShort(end.position - start.position).putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(desc)).putShort(index);
/*      */ 
/* 1216 */     if (this.compute != 2)
/*      */     {
/* 1218 */       char c = desc.charAt(0);
/* 1219 */       int n = index + ((c == 'J') || (c == 'D') ? 2 : 1);
/* 1220 */       if (n > this.maxLocals)
/* 1221 */         this.maxLocals = n;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitLineNumber(int line, Label start)
/*      */   {
/* 1227 */     if (this.lineNumber == null) {
/* 1228 */       this.lineNumber = new ByteVector();
/*      */     }
/* 1230 */     this.lineNumberCount += 1;
/* 1231 */     this.lineNumber.putShort(start.position);
/* 1232 */     this.lineNumber.putShort(line);
/*      */   }
/*      */ 
/*      */   public void visitMaxs(int maxStack, int maxLocals) {
/* 1236 */     if (this.compute == 0)
/*      */     {
/* 1238 */       Handler handler = this.firstHandler;
/* 1239 */       while (handler != null) {
/* 1240 */         Label l = handler.start.getFirst();
/* 1241 */         Label h = handler.handler.getFirst();
/* 1242 */         Label e = handler.end.getFirst();
/*      */ 
/* 1244 */         String t = handler.desc == null ? "java/lang/Throwable" : handler.desc;
/*      */ 
/* 1247 */         int kind = 0x1700000 | this.cw.addType(t);
/*      */ 
/* 1249 */         h.status |= 16;
/*      */ 
/* 1251 */         while (l != e)
/*      */         {
/* 1253 */           Edge b = new Edge();
/* 1254 */           b.info = kind;
/* 1255 */           b.successor = h;
/*      */ 
/* 1257 */           b.next = l.successors;
/* 1258 */           l.successors = b;
/*      */ 
/* 1260 */           l = l.successor;
/*      */         }
/* 1262 */         handler = handler.next;
/*      */       }
/*      */ 
/* 1266 */       Frame f = this.labels.frame;
/* 1267 */       Type[] args = Type.getArgumentTypes(this.descriptor);
/* 1268 */       f.initInputFrame(this.cw, this.access, args, this.maxLocals);
/* 1269 */       visitFrame(f);
/*      */ 
/* 1277 */       int max = 0;
/* 1278 */       Label changed = this.labels;
/* 1279 */       while (changed != null)
/*      */       {
/* 1281 */         Label l = changed;
/* 1282 */         changed = changed.next;
/* 1283 */         l.next = null;
/* 1284 */         f = l.frame;
/*      */ 
/* 1286 */         if ((l.status & 0x10) != 0) {
/* 1287 */           l.status |= 32;
/*      */         }
/*      */ 
/* 1290 */         l.status |= 64;
/*      */ 
/* 1292 */         int blockMax = f.inputStack.length + l.outputStackMax;
/* 1293 */         if (blockMax > max) {
/* 1294 */           max = blockMax;
/*      */         }
/*      */ 
/* 1297 */         Edge e = l.successors;
/* 1298 */         while (e != null) {
/* 1299 */           Label n = e.successor.getFirst();
/* 1300 */           boolean change = f.merge(this.cw, n.frame, e.info);
/* 1301 */           if ((change) && (n.next == null))
/*      */           {
/* 1304 */             n.next = changed;
/* 1305 */             changed = n;
/*      */           }
/* 1307 */           e = e.next;
/*      */         }
/*      */       }
/* 1310 */       this.maxStack = max;
/*      */ 
/* 1313 */       Label l = this.labels;
/* 1314 */       while (l != null) {
/* 1315 */         f = l.frame;
/* 1316 */         if ((l.status & 0x20) != 0) {
/* 1317 */           visitFrame(f);
/*      */         }
/* 1319 */         if ((l.status & 0x40) == 0)
/*      */         {
/* 1321 */           Label k = l.successor;
/* 1322 */           int start = l.position;
/* 1323 */           int end = (k == null ? this.code.length : k.position) - 1;
/*      */ 
/* 1325 */           if (end >= start)
/*      */           {
/* 1327 */             for (int i = start; i < end; i++) {
/* 1328 */               this.code.data[i] = 0;
/*      */             }
/* 1330 */             this.code.data[end] = -65;
/*      */ 
/* 1332 */             startFrame(start, 0, 1);
/* 1333 */             this.frame[(this.frameIndex++)] = (0x1700000 | this.cw.addType("java/lang/Throwable"));
/*      */ 
/* 1335 */             endFrame();
/*      */           }
/*      */         }
/* 1338 */         l = l.successor;
/*      */       }
/* 1340 */     } else if (this.compute == 1)
/*      */     {
/* 1342 */       Handler handler = this.firstHandler;
/* 1343 */       while (handler != null) {
/* 1344 */         Label l = handler.start;
/* 1345 */         Label h = handler.handler;
/* 1346 */         Label e = handler.end;
/*      */ 
/* 1348 */         while (l != e)
/*      */         {
/* 1350 */           Edge b = new Edge();
/* 1351 */           b.info = 2147483647;
/* 1352 */           b.successor = h;
/*      */ 
/* 1354 */           if ((l.status & 0x80) == 0) {
/* 1355 */             b.next = l.successors;
/* 1356 */             l.successors = b;
/*      */           }
/*      */           else
/*      */           {
/* 1361 */             b.next = l.successors.next.next;
/* 1362 */             l.successors.next.next = b;
/*      */           }
/*      */ 
/* 1365 */           l = l.successor;
/*      */         }
/* 1367 */         handler = handler.next;
/*      */       }
/*      */ 
/* 1370 */       if (this.subroutines > 0)
/*      */       {
/* 1377 */         int id = 0;
/* 1378 */         this.labels.visitSubroutine(null, 1L, this.subroutines);
/*      */ 
/* 1380 */         Label l = this.labels;
/* 1381 */         while (l != null) {
/* 1382 */           if ((l.status & 0x80) != 0)
/*      */           {
/* 1384 */             Label subroutine = l.successors.next.successor;
/*      */ 
/* 1386 */             if ((subroutine.status & 0x400) == 0)
/*      */             {
/* 1388 */               id++;
/* 1389 */               subroutine.visitSubroutine(null, id / 32L << 32 | 1L << id % 32, this.subroutines);
/*      */             }
/*      */           }
/*      */ 
/* 1393 */           l = l.successor;
/*      */         }
/*      */ 
/* 1396 */         l = this.labels;
/* 1397 */         while (l != null) {
/* 1398 */           if ((l.status & 0x80) != 0) {
/* 1399 */             Label L = this.labels;
/* 1400 */             while (L != null) {
/* 1401 */               L.status &= -1025;
/* 1402 */               L = L.successor;
/*      */             }
/*      */ 
/* 1405 */             Label subroutine = l.successors.next.successor;
/* 1406 */             subroutine.visitSubroutine(l, 0L, this.subroutines);
/*      */           }
/* 1408 */           l = l.successor;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1422 */       int max = 0;
/* 1423 */       Label stack = this.labels;
/* 1424 */       while (stack != null)
/*      */       {
/* 1426 */         Label l = stack;
/* 1427 */         stack = stack.next;
/*      */ 
/* 1429 */         int start = l.inputStackTop;
/* 1430 */         int blockMax = start + l.outputStackMax;
/*      */ 
/* 1432 */         if (blockMax > max) {
/* 1433 */           max = blockMax;
/*      */         }
/*      */ 
/* 1436 */         Edge b = l.successors;
/* 1437 */         if ((l.status & 0x80) != 0)
/*      */         {
/* 1439 */           b = b.next;
/*      */         }
/* 1441 */         while (b != null) {
/* 1442 */           l = b.successor;
/*      */ 
/* 1444 */           if ((l.status & 0x8) == 0)
/*      */           {
/* 1446 */             l.inputStackTop = (b.info == 2147483647 ? 1 : start + b.info);
/*      */ 
/* 1449 */             l.status |= 8;
/* 1450 */             l.next = stack;
/* 1451 */             stack = l;
/*      */           }
/* 1453 */           b = b.next;
/*      */         }
/*      */       }
/* 1456 */       this.maxStack = max;
/*      */     } else {
/* 1458 */       this.maxStack = maxStack;
/* 1459 */       this.maxLocals = maxLocals;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void visitEnd()
/*      */   {
/*      */   }
/*      */ 
/*      */   static int getArgumentsAndReturnSizes(String desc)
/*      */   {
/* 1481 */     int n = 1;
/* 1482 */     int c = 1;
/*      */     while (true) {
/* 1484 */       char car = desc.charAt(c++);
/* 1485 */       if (car == ')') {
/* 1486 */         car = desc.charAt(c);
/* 1487 */         return n << 2 | ((car == 'D') || (car == 'J') ? 2 : car == 'V' ? 0 : 1);
/*      */       }
/* 1489 */       if (car == 'L') {
/* 1490 */         while (desc.charAt(c++) != ';');
/* 1492 */         n++;
/* 1493 */       } else if (car == '[') {
/* 1494 */         while ((car = desc.charAt(c)) == '[') {
/* 1495 */           c++;
/*      */         }
/* 1497 */         if ((car == 'D') || (car == 'J'))
/* 1498 */           n--;
/*      */       }
/* 1500 */       else if ((car == 'D') || (car == 'J')) {
/* 1501 */         n += 2;
/*      */       } else {
/* 1503 */         n++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addSuccessor(int info, Label successor)
/*      */   {
/* 1516 */     Edge b = new Edge();
/* 1517 */     b.info = info;
/* 1518 */     b.successor = successor;
/*      */ 
/* 1520 */     b.next = this.currentBlock.successors;
/* 1521 */     this.currentBlock.successors = b;
/*      */   }
/*      */ 
/*      */   private void noSuccessor()
/*      */   {
/* 1529 */     if (this.compute == 0) {
/* 1530 */       Label l = new Label();
/* 1531 */       l.frame = new Frame();
/* 1532 */       l.frame.owner = l;
/* 1533 */       l.resolve(this, this.code.length, this.code.data);
/* 1534 */       this.previousBlock.successor = l;
/* 1535 */       this.previousBlock = l;
/*      */     } else {
/* 1537 */       this.currentBlock.outputStackMax = this.maxStackSize;
/*      */     }
/* 1539 */     this.currentBlock = null;
/*      */   }
/*      */ 
/*      */   private void visitFrame(Frame f)
/*      */   {
/* 1553 */     int nTop = 0;
/* 1554 */     int nLocal = 0;
/* 1555 */     int nStack = 0;
/* 1556 */     int[] locals = f.inputLocals;
/* 1557 */     int[] stacks = f.inputStack;
/*      */ 
/* 1560 */     for (int i = 0; i < locals.length; i++) {
/* 1561 */       int t = locals[i];
/* 1562 */       if (t == 16777216) {
/* 1563 */         nTop++;
/*      */       } else {
/* 1565 */         nLocal += nTop + 1;
/* 1566 */         nTop = 0;
/*      */       }
/* 1568 */       if ((t == 16777220) || (t == 16777219)) {
/* 1569 */         i++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1574 */     for (i = 0; i < stacks.length; i++) {
/* 1575 */       int t = stacks[i];
/* 1576 */       nStack++;
/* 1577 */       if ((t == 16777220) || (t == 16777219)) {
/* 1578 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1582 */     startFrame(f.owner.position, nLocal, nStack);
/* 1583 */     for (i = 0; nLocal > 0; nLocal--) {
/* 1584 */       int t = locals[i];
/* 1585 */       this.frame[(this.frameIndex++)] = t;
/* 1586 */       if ((t == 16777220) || (t == 16777219))
/* 1587 */         i++;
/* 1583 */       i++;
/*      */     }
/*      */ 
/* 1590 */     for (i = 0; i < stacks.length; i++) {
/* 1591 */       int t = stacks[i];
/* 1592 */       this.frame[(this.frameIndex++)] = t;
/* 1593 */       if ((t == 16777220) || (t == 16777219)) {
/* 1594 */         i++;
/*      */       }
/*      */     }
/* 1597 */     endFrame();
/*      */   }
/*      */ 
/*      */   private void startFrame(int offset, int nLocal, int nStack)
/*      */   {
/* 1610 */     int n = 3 + nLocal + nStack;
/* 1611 */     if ((this.frame == null) || (this.frame.length < n)) {
/* 1612 */       this.frame = new int[n];
/*      */     }
/* 1614 */     this.frame[0] = offset;
/* 1615 */     this.frame[1] = nLocal;
/* 1616 */     this.frame[2] = nStack;
/* 1617 */     this.frameIndex = 3;
/*      */   }
/*      */ 
/*      */   private void endFrame()
/*      */   {
/* 1625 */     if (this.previousFrame != null) {
/* 1626 */       if (this.stackMap == null) {
/* 1627 */         this.stackMap = new ByteVector();
/*      */       }
/* 1629 */       writeFrame();
/* 1630 */       this.frameCount += 1;
/*      */     }
/* 1632 */     this.previousFrame = this.frame;
/* 1633 */     this.frame = null;
/*      */   }
/*      */ 
/*      */   private void writeFrame()
/*      */   {
/* 1641 */     int clocalsSize = this.frame[1];
/* 1642 */     int cstackSize = this.frame[2];
/* 1643 */     if ((this.cw.version & 0xFFFF) < 50) {
/* 1644 */       this.stackMap.putShort(this.frame[0]).putShort(clocalsSize);
/* 1645 */       writeFrameTypes(3, 3 + clocalsSize);
/* 1646 */       this.stackMap.putShort(cstackSize);
/* 1647 */       writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
/* 1648 */       return;
/*      */     }
/* 1650 */     int localsSize = this.previousFrame[1];
/* 1651 */     int type = 255;
/* 1652 */     int k = 0;
/*      */     int delta;
/*      */     int delta;
/* 1654 */     if (this.frameCount == 0)
/* 1655 */       delta = this.frame[0];
/*      */     else {
/* 1657 */       delta = this.frame[0] - this.previousFrame[0] - 1;
/*      */     }
/* 1659 */     if (cstackSize == 0) {
/* 1660 */       k = clocalsSize - localsSize;
/* 1661 */       switch (k) {
/*      */       case -3:
/*      */       case -2:
/*      */       case -1:
/* 1665 */         type = 248;
/* 1666 */         localsSize = clocalsSize;
/* 1667 */         break;
/*      */       case 0:
/* 1669 */         type = delta < 64 ? 0 : 251;
/* 1670 */         break;
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/* 1674 */         type = 252;
/*      */       }
/*      */     }
/* 1677 */     else if ((clocalsSize == localsSize) && (cstackSize == 1)) {
/* 1678 */       type = delta < 63 ? 64 : 247;
/*      */     }
/*      */ 
/* 1682 */     if (type != 255)
/*      */     {
/* 1684 */       int l = 3;
/* 1685 */       for (int j = 0; j < localsSize; j++) {
/* 1686 */         if (this.frame[l] != this.previousFrame[l]) {
/* 1687 */           type = 255;
/* 1688 */           break;
/*      */         }
/* 1690 */         l++;
/*      */       }
/*      */     }
/* 1693 */     switch (type) {
/*      */     case 0:
/* 1695 */       this.stackMap.putByte(delta);
/* 1696 */       break;
/*      */     case 64:
/* 1698 */       this.stackMap.putByte(64 + delta);
/* 1699 */       writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
/* 1700 */       break;
/*      */     case 247:
/* 1702 */       this.stackMap.putByte(247).putShort(delta);
/*      */ 
/* 1704 */       writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
/* 1705 */       break;
/*      */     case 251:
/* 1707 */       this.stackMap.putByte(251).putShort(delta);
/* 1708 */       break;
/*      */     case 248:
/* 1710 */       this.stackMap.putByte(251 + k).putShort(delta);
/* 1711 */       break;
/*      */     case 252:
/* 1713 */       this.stackMap.putByte(251 + k).putShort(delta);
/* 1714 */       writeFrameTypes(3 + localsSize, 3 + clocalsSize);
/* 1715 */       break;
/*      */     default:
/* 1718 */       this.stackMap.putByte(255).putShort(delta).putShort(clocalsSize);
/*      */ 
/* 1721 */       writeFrameTypes(3, 3 + clocalsSize);
/* 1722 */       this.stackMap.putShort(cstackSize);
/* 1723 */       writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeFrameTypes(int start, int end)
/*      */   {
/* 1737 */     for (int i = start; i < end; i++) {
/* 1738 */       int t = this.frame[i];
/* 1739 */       int d = t & 0xF0000000;
/* 1740 */       if (d == 0) {
/* 1741 */         int v = t & 0xFFFFF;
/* 1742 */         switch (t & 0xFF00000) {
/*      */         case 24117248:
/* 1744 */           this.stackMap.putByte(7).putShort(this.cw.newClass(this.cw.typeTable[v].strVal1));
/*      */ 
/* 1746 */           break;
/*      */         case 25165824:
/* 1748 */           this.stackMap.putByte(8).putShort(this.cw.typeTable[v].intVal);
/* 1749 */           break;
/*      */         default:
/* 1751 */           this.stackMap.putByte(v);
/*      */         }
/*      */       } else {
/* 1754 */         StringBuffer buf = new StringBuffer();
/* 1755 */         d >>= 28;
/* 1756 */         while (d-- > 0) {
/* 1757 */           buf.append('[');
/*      */         }
/* 1759 */         if ((t & 0xFF00000) == 24117248) {
/* 1760 */           buf.append('L');
/* 1761 */           buf.append(this.cw.typeTable[(t & 0xFFFFF)].strVal1);
/* 1762 */           buf.append(';');
/*      */         } else {
/* 1764 */           switch (t & 0xF) {
/*      */           case 1:
/* 1766 */             buf.append('I');
/* 1767 */             break;
/*      */           case 2:
/* 1769 */             buf.append('F');
/* 1770 */             break;
/*      */           case 3:
/* 1772 */             buf.append('D');
/* 1773 */             break;
/*      */           case 9:
/* 1775 */             buf.append('Z');
/* 1776 */             break;
/*      */           case 10:
/* 1778 */             buf.append('B');
/* 1779 */             break;
/*      */           case 11:
/* 1781 */             buf.append('C');
/* 1782 */             break;
/*      */           case 12:
/* 1784 */             buf.append('S');
/* 1785 */             break;
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 8:
/*      */           default:
/* 1787 */             buf.append('J');
/*      */           }
/*      */         }
/* 1790 */         this.stackMap.putByte(7).putShort(this.cw.newClass(buf.toString()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeFrameType(Object type) {
/* 1796 */     if ((type instanceof String))
/* 1797 */       this.stackMap.putByte(7).putShort(this.cw.newClass((String)type));
/* 1798 */     else if ((type instanceof Integer))
/* 1799 */       this.stackMap.putByte(((Integer)type).intValue());
/*      */     else
/* 1801 */       this.stackMap.putByte(8).putShort(((Label)type).position);
/*      */   }
/*      */ 
/*      */   final int getSize()
/*      */   {
/* 1815 */     if (this.classReaderOffset != 0) {
/* 1816 */       return 6 + this.classReaderLength;
/*      */     }
/* 1818 */     if (this.resize)
/*      */     {
/* 1821 */       resizeInstructions();
/*      */     }
/*      */ 
/* 1826 */     int size = 8;
/* 1827 */     if (this.code.length > 0) {
/* 1828 */       this.cw.newUTF8("Code");
/* 1829 */       size += 18 + this.code.length + 8 * this.handlerCount;
/* 1830 */       if (this.localVar != null) {
/* 1831 */         this.cw.newUTF8("LocalVariableTable");
/* 1832 */         size += 8 + this.localVar.length;
/*      */       }
/* 1834 */       if (this.localVarType != null) {
/* 1835 */         this.cw.newUTF8("LocalVariableTypeTable");
/* 1836 */         size += 8 + this.localVarType.length;
/*      */       }
/* 1838 */       if (this.lineNumber != null) {
/* 1839 */         this.cw.newUTF8("LineNumberTable");
/* 1840 */         size += 8 + this.lineNumber.length;
/*      */       }
/* 1842 */       if (this.stackMap != null) {
/* 1843 */         boolean zip = (this.cw.version & 0xFFFF) >= 50;
/* 1844 */         this.cw.newUTF8(zip ? "StackMapTable" : "StackMap");
/* 1845 */         size += 8 + this.stackMap.length;
/*      */       }
/* 1847 */       if (this.cattrs != null) {
/* 1848 */         size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1855 */     if (this.exceptionCount > 0) {
/* 1856 */       this.cw.newUTF8("Exceptions");
/* 1857 */       size += 8 + 2 * this.exceptionCount;
/*      */     }
/* 1859 */     if (((this.access & 0x1000) != 0) && ((this.cw.version & 0xFFFF) < 49))
/*      */     {
/* 1862 */       this.cw.newUTF8("Synthetic");
/* 1863 */       size += 6;
/*      */     }
/* 1865 */     if ((this.access & 0x20000) != 0) {
/* 1866 */       this.cw.newUTF8("Deprecated");
/* 1867 */       size += 6;
/*      */     }
/* 1869 */     if (this.signature != null) {
/* 1870 */       this.cw.newUTF8("Signature");
/* 1871 */       this.cw.newUTF8(this.signature);
/* 1872 */       size += 8;
/*      */     }
/* 1874 */     if (this.annd != null) {
/* 1875 */       this.cw.newUTF8("AnnotationDefault");
/* 1876 */       size += 6 + this.annd.length;
/*      */     }
/* 1878 */     if (this.anns != null) {
/* 1879 */       this.cw.newUTF8("RuntimeVisibleAnnotations");
/* 1880 */       size += 8 + this.anns.getSize();
/*      */     }
/* 1882 */     if (this.ianns != null) {
/* 1883 */       this.cw.newUTF8("RuntimeInvisibleAnnotations");
/* 1884 */       size += 8 + this.ianns.getSize();
/*      */     }
/* 1886 */     if (this.panns != null) {
/* 1887 */       this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
/* 1888 */       size += 7 + 2 * (this.panns.length - this.synthetics);
/* 1889 */       for (int i = this.panns.length - 1; i >= this.synthetics; i--) {
/* 1890 */         size += (this.panns[i] == null ? 0 : this.panns[i].getSize());
/*      */       }
/*      */     }
/* 1893 */     if (this.ipanns != null) {
/* 1894 */       this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
/* 1895 */       size += 7 + 2 * (this.ipanns.length - this.synthetics);
/* 1896 */       for (int i = this.ipanns.length - 1; i >= this.synthetics; i--) {
/* 1897 */         size += (this.ipanns[i] == null ? 0 : this.ipanns[i].getSize());
/*      */       }
/*      */     }
/* 1900 */     if (this.attrs != null) {
/* 1901 */       size += this.attrs.getSize(this.cw, null, 0, -1, -1);
/*      */     }
/* 1903 */     return size;
/*      */   }
/*      */ 
/*      */   final void put(ByteVector out)
/*      */   {
/* 1913 */     out.putShort(this.access).putShort(this.name).putShort(this.desc);
/* 1914 */     if (this.classReaderOffset != 0) {
/* 1915 */       out.putByteArray(this.cw.cr.b, this.classReaderOffset, this.classReaderLength);
/* 1916 */       return;
/*      */     }
/* 1918 */     int attributeCount = 0;
/* 1919 */     if (this.code.length > 0) {
/* 1920 */       attributeCount++;
/*      */     }
/* 1922 */     if (this.exceptionCount > 0) {
/* 1923 */       attributeCount++;
/*      */     }
/* 1925 */     if (((this.access & 0x1000) != 0) && ((this.cw.version & 0xFFFF) < 49))
/*      */     {
/* 1928 */       attributeCount++;
/*      */     }
/* 1930 */     if ((this.access & 0x20000) != 0) {
/* 1931 */       attributeCount++;
/*      */     }
/* 1933 */     if (this.signature != null) {
/* 1934 */       attributeCount++;
/*      */     }
/* 1936 */     if (this.annd != null) {
/* 1937 */       attributeCount++;
/*      */     }
/* 1939 */     if (this.anns != null) {
/* 1940 */       attributeCount++;
/*      */     }
/* 1942 */     if (this.ianns != null) {
/* 1943 */       attributeCount++;
/*      */     }
/* 1945 */     if (this.panns != null) {
/* 1946 */       attributeCount++;
/*      */     }
/* 1948 */     if (this.ipanns != null) {
/* 1949 */       attributeCount++;
/*      */     }
/* 1951 */     if (this.attrs != null) {
/* 1952 */       attributeCount += this.attrs.getCount();
/*      */     }
/* 1954 */     out.putShort(attributeCount);
/* 1955 */     if (this.code.length > 0) {
/* 1956 */       int size = 12 + this.code.length + 8 * this.handlerCount;
/* 1957 */       if (this.localVar != null) {
/* 1958 */         size += 8 + this.localVar.length;
/*      */       }
/* 1960 */       if (this.localVarType != null) {
/* 1961 */         size += 8 + this.localVarType.length;
/*      */       }
/* 1963 */       if (this.lineNumber != null) {
/* 1964 */         size += 8 + this.lineNumber.length;
/*      */       }
/* 1966 */       if (this.stackMap != null) {
/* 1967 */         size += 8 + this.stackMap.length;
/*      */       }
/* 1969 */       if (this.cattrs != null) {
/* 1970 */         size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
/*      */       }
/*      */ 
/* 1976 */       out.putShort(this.cw.newUTF8("Code")).putInt(size);
/* 1977 */       out.putShort(this.maxStack).putShort(this.maxLocals);
/* 1978 */       out.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
/* 1979 */       out.putShort(this.handlerCount);
/* 1980 */       if (this.handlerCount > 0) {
/* 1981 */         Handler h = this.firstHandler;
/* 1982 */         while (h != null) {
/* 1983 */           out.putShort(h.start.position).putShort(h.end.position).putShort(h.handler.position).putShort(h.type);
/*      */ 
/* 1987 */           h = h.next;
/*      */         }
/*      */       }
/* 1990 */       attributeCount = 0;
/* 1991 */       if (this.localVar != null) {
/* 1992 */         attributeCount++;
/*      */       }
/* 1994 */       if (this.localVarType != null) {
/* 1995 */         attributeCount++;
/*      */       }
/* 1997 */       if (this.lineNumber != null) {
/* 1998 */         attributeCount++;
/*      */       }
/* 2000 */       if (this.stackMap != null) {
/* 2001 */         attributeCount++;
/*      */       }
/* 2003 */       if (this.cattrs != null) {
/* 2004 */         attributeCount += this.cattrs.getCount();
/*      */       }
/* 2006 */       out.putShort(attributeCount);
/* 2007 */       if (this.localVar != null) {
/* 2008 */         out.putShort(this.cw.newUTF8("LocalVariableTable"));
/* 2009 */         out.putInt(this.localVar.length + 2).putShort(this.localVarCount);
/* 2010 */         out.putByteArray(this.localVar.data, 0, this.localVar.length);
/*      */       }
/* 2012 */       if (this.localVarType != null) {
/* 2013 */         out.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
/* 2014 */         out.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
/* 2015 */         out.putByteArray(this.localVarType.data, 0, this.localVarType.length);
/*      */       }
/* 2017 */       if (this.lineNumber != null) {
/* 2018 */         out.putShort(this.cw.newUTF8("LineNumberTable"));
/* 2019 */         out.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
/* 2020 */         out.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
/*      */       }
/* 2022 */       if (this.stackMap != null) {
/* 2023 */         boolean zip = (this.cw.version & 0xFFFF) >= 50;
/* 2024 */         out.putShort(this.cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
/* 2025 */         out.putInt(this.stackMap.length + 2).putShort(this.frameCount);
/* 2026 */         out.putByteArray(this.stackMap.data, 0, this.stackMap.length);
/*      */       }
/* 2028 */       if (this.cattrs != null) {
/* 2029 */         this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, out);
/*      */       }
/*      */     }
/* 2032 */     if (this.exceptionCount > 0) {
/* 2033 */       out.putShort(this.cw.newUTF8("Exceptions")).putInt(2 * this.exceptionCount + 2);
/*      */ 
/* 2035 */       out.putShort(this.exceptionCount);
/* 2036 */       for (int i = 0; i < this.exceptionCount; i++) {
/* 2037 */         out.putShort(this.exceptions[i]);
/*      */       }
/*      */     }
/* 2040 */     if (((this.access & 0x1000) != 0) && ((this.cw.version & 0xFFFF) < 49))
/*      */     {
/* 2043 */       out.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
/*      */     }
/* 2045 */     if ((this.access & 0x20000) != 0) {
/* 2046 */       out.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
/*      */     }
/* 2048 */     if (this.signature != null) {
/* 2049 */       out.putShort(this.cw.newUTF8("Signature")).putInt(2).putShort(this.cw.newUTF8(this.signature));
/*      */     }
/*      */ 
/* 2053 */     if (this.annd != null) {
/* 2054 */       out.putShort(this.cw.newUTF8("AnnotationDefault"));
/* 2055 */       out.putInt(this.annd.length);
/* 2056 */       out.putByteArray(this.annd.data, 0, this.annd.length);
/*      */     }
/* 2058 */     if (this.anns != null) {
/* 2059 */       out.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
/* 2060 */       this.anns.put(out);
/*      */     }
/* 2062 */     if (this.ianns != null) {
/* 2063 */       out.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
/* 2064 */       this.ianns.put(out);
/*      */     }
/* 2066 */     if (this.panns != null) {
/* 2067 */       out.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
/* 2068 */       AnnotationWriter.put(this.panns, this.synthetics, out);
/*      */     }
/* 2070 */     if (this.ipanns != null) {
/* 2071 */       out.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
/* 2072 */       AnnotationWriter.put(this.ipanns, this.synthetics, out);
/*      */     }
/* 2074 */     if (this.attrs != null)
/* 2075 */       this.attrs.put(this.cw, null, 0, -1, -1, out);
/*      */   }
/*      */ 
/*      */   private void resizeInstructions()
/*      */   {
/* 2099 */     byte[] b = this.code.data;
/*      */ 
/* 2127 */     int[] allIndexes = new int[0];
/* 2128 */     int[] allSizes = new int[0];
/*      */ 
/* 2132 */     boolean[] resize = new boolean[this.code.length];
/*      */ 
/* 2135 */     int state = 3;
/*      */     int label;
/*      */     do
/*      */     {
/* 2137 */       if (state == 3) {
/* 2138 */         state = 2;
/*      */       }
/* 2140 */       u = 0;
/* 2141 */       while (u < b.length) {
/* 2142 */         int opcode = b[u] & 0xFF;
/* 2143 */         int insert = 0;
/*      */         int newOffset;
/* 2145 */         switch (ClassWriter.TYPE[opcode]) {
/*      */         case 0:
/*      */         case 4:
/* 2148 */           u++;
/* 2149 */           break;
/*      */         case 8:
/*      */           int label;
/* 2151 */           if (opcode > 201)
/*      */           {
/* 2155 */             opcode = opcode < 218 ? opcode - 49 : opcode - 20;
/* 2156 */             label = u + readUnsignedShort(b, u + 1);
/*      */           } else {
/* 2158 */             label = u + readShort(b, u + 1);
/*      */           }
/* 2160 */           newOffset = getNewOffset(allIndexes, allSizes, u, label);
/* 2161 */           if ((newOffset < -32768) || (newOffset > 32767))
/*      */           {
/* 2164 */             if (resize[u] == 0) {
/* 2165 */               if ((opcode == 167) || (opcode == 168))
/*      */               {
/* 2171 */                 insert = 2;
/*      */               }
/*      */               else
/*      */               {
/* 2179 */                 insert = 5;
/*      */               }
/* 2181 */               resize[u] = true;
/*      */             }
/*      */           }
/* 2184 */           u += 3;
/* 2185 */           break;
/*      */         case 9:
/* 2187 */           u += 5;
/* 2188 */           break;
/*      */         case 13:
/* 2190 */           if (state == 1)
/*      */           {
/* 2198 */             newOffset = getNewOffset(allIndexes, allSizes, 0, u);
/* 2199 */             insert = -(newOffset & 0x3);
/* 2200 */           } else if (resize[u] == 0)
/*      */           {
/* 2204 */             insert = u & 0x3;
/* 2205 */             resize[u] = true;
/*      */           }
/*      */ 
/* 2208 */           u = u + 4 - (u & 0x3);
/* 2209 */           u += 4 * (readInt(b, u + 8) - readInt(b, u + 4) + 1) + 12;
/* 2210 */           break;
/*      */         case 14:
/* 2212 */           if (state == 1)
/*      */           {
/* 2214 */             int newOffset = getNewOffset(allIndexes, allSizes, 0, u);
/* 2215 */             insert = -(newOffset & 0x3);
/* 2216 */           } else if (resize[u] == 0)
/*      */           {
/* 2218 */             insert = u & 0x3;
/* 2219 */             resize[u] = true;
/*      */           }
/*      */ 
/* 2222 */           u = u + 4 - (u & 0x3);
/* 2223 */           u += 8 * readInt(b, u + 4) + 8;
/* 2224 */           break;
/*      */         case 16:
/* 2226 */           opcode = b[(u + 1)] & 0xFF;
/* 2227 */           if (opcode == 132)
/* 2228 */             u += 6;
/*      */           else {
/* 2230 */             u += 4;
/*      */           }
/* 2232 */           break;
/*      */         case 1:
/*      */         case 3:
/*      */         case 10:
/* 2236 */           u += 2;
/* 2237 */           break;
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*      */         case 11:
/*      */         case 12:
/* 2243 */           u += 3;
/* 2244 */           break;
/*      */         case 7:
/* 2246 */           u += 5;
/* 2247 */           break;
/*      */         case 15:
/*      */         default:
/* 2250 */           u += 4;
/*      */         }
/*      */ 
/* 2253 */         if (insert != 0)
/*      */         {
/* 2256 */           int[] newIndexes = new int[allIndexes.length + 1];
/* 2257 */           int[] newSizes = new int[allSizes.length + 1];
/* 2258 */           System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
/*      */ 
/* 2263 */           System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
/* 2264 */           newIndexes[allIndexes.length] = u;
/* 2265 */           newSizes[allSizes.length] = insert;
/* 2266 */           allIndexes = newIndexes;
/* 2267 */           allSizes = newSizes;
/* 2268 */           if (insert > 0) {
/* 2269 */             state = 3;
/*      */           }
/*      */         }
/*      */       }
/* 2273 */       if (state < 3)
/* 2274 */         state--;
/*      */     }
/* 2276 */     while (state != 0);
/*      */ 
/* 2282 */     ByteVector newCode = new ByteVector(this.code.length);
/*      */ 
/* 2284 */     int u = 0;
/*      */     int label;
/*      */     int newOffset;
/* 2285 */     while (u < this.code.length) {
/* 2286 */       int opcode = b[u] & 0xFF;
/*      */       int v;
/*      */       int j;
/* 2287 */       switch (ClassWriter.TYPE[opcode]) {
/*      */       case 0:
/*      */       case 4:
/* 2290 */         newCode.putByte(opcode);
/* 2291 */         u++;
/* 2292 */         break;
/*      */       case 8:
/* 2294 */         if (opcode > 201)
/*      */         {
/* 2298 */           opcode = opcode < 218 ? opcode - 49 : opcode - 20;
/* 2299 */           label = u + readUnsignedShort(b, u + 1);
/*      */         } else {
/* 2301 */           label = u + readShort(b, u + 1);
/*      */         }
/* 2303 */         newOffset = getNewOffset(allIndexes, allSizes, u, label);
/* 2304 */         if (resize[u] != 0)
/*      */         {
/* 2310 */           if (opcode == 167) {
/* 2311 */             newCode.putByte(200);
/* 2312 */           } else if (opcode == 168) {
/* 2313 */             newCode.putByte(201);
/*      */           } else {
/* 2315 */             newCode.putByte(opcode <= 166 ? (opcode + 1 ^ 0x1) - 1 : opcode ^ 0x1);
/*      */ 
/* 2318 */             newCode.putShort(8);
/* 2319 */             newCode.putByte(200);
/*      */ 
/* 2321 */             newOffset -= 3;
/*      */           }
/* 2323 */           newCode.putInt(newOffset);
/*      */         } else {
/* 2325 */           newCode.putByte(opcode);
/* 2326 */           newCode.putShort(newOffset);
/*      */         }
/* 2328 */         u += 3;
/* 2329 */         break;
/*      */       case 9:
/* 2331 */         label = u + readInt(b, u + 1);
/* 2332 */         newOffset = getNewOffset(allIndexes, allSizes, u, label);
/* 2333 */         newCode.putByte(opcode);
/* 2334 */         newCode.putInt(newOffset);
/* 2335 */         u += 5;
/* 2336 */         break;
/*      */       case 13:
/* 2339 */         v = u;
/* 2340 */         u = u + 4 - (v & 0x3);
/*      */ 
/* 2342 */         newCode.putByte(170);
/* 2343 */         newCode.length += (4 - newCode.length % 4) % 4;
/* 2344 */         label = v + readInt(b, u);
/* 2345 */         u += 4;
/* 2346 */         newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 2347 */         newCode.putInt(newOffset);
/* 2348 */         j = readInt(b, u);
/* 2349 */         u += 4;
/* 2350 */         newCode.putInt(j);
/* 2351 */         j = readInt(b, u) - j + 1;
/* 2352 */         u += 4;
/* 2353 */         newCode.putInt(readInt(b, u - 4));
/*      */       case 14:
/*      */       case 16:
/*      */       case 1:
/*      */       case 3:
/*      */       case 10:
/*      */       case 2:
/*      */       case 5:
/*      */       case 6:
/*      */       case 11:
/*      */       case 12:
/*      */       case 7:
/*      */       case 15:
/*      */       default:
/* 2354 */         while (j > 0) {
/* 2355 */           label = v + readInt(b, u);
/* 2356 */           u += 4;
/* 2357 */           newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 2358 */           newCode.putInt(newOffset);
/*      */ 
/* 2354 */           j--; continue;
/*      */ 
/* 2363 */           v = u;
/* 2364 */           u = u + 4 - (v & 0x3);
/*      */ 
/* 2366 */           newCode.putByte(171);
/* 2367 */           newCode.length += (4 - newCode.length % 4) % 4;
/* 2368 */           label = v + readInt(b, u);
/* 2369 */           u += 4;
/* 2370 */           newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 2371 */           newCode.putInt(newOffset);
/* 2372 */           j = readInt(b, u);
/* 2373 */           u += 4;
/* 2374 */           newCode.putInt(j);
/* 2375 */           while (j > 0) {
/* 2376 */             newCode.putInt(readInt(b, u));
/* 2377 */             u += 4;
/* 2378 */             label = v + readInt(b, u);
/* 2379 */             u += 4;
/* 2380 */             newOffset = getNewOffset(allIndexes, allSizes, v, label);
/* 2381 */             newCode.putInt(newOffset);
/*      */ 
/* 2375 */             j--; continue;
/*      */ 
/* 2385 */             opcode = b[(u + 1)] & 0xFF;
/* 2386 */             if (opcode == 132) {
/* 2387 */               newCode.putByteArray(b, u, 6);
/* 2388 */               u += 6;
/*      */             } else {
/* 2390 */               newCode.putByteArray(b, u, 4);
/* 2391 */               u += 4;
/*      */ 
/* 2393 */               break;
/*      */ 
/* 2397 */               newCode.putByteArray(b, u, 2);
/* 2398 */               u += 2;
/* 2399 */               break;
/*      */ 
/* 2405 */               newCode.putByteArray(b, u, 3);
/* 2406 */               u += 3;
/* 2407 */               break;
/*      */ 
/* 2409 */               newCode.putByteArray(b, u, 5);
/* 2410 */               u += 5;
/* 2411 */               break;
/*      */ 
/* 2414 */               newCode.putByteArray(b, u, 4);
/* 2415 */               u += 4;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2421 */     if (this.frameCount > 0) {
/* 2422 */       if (this.compute == 0) {
/* 2423 */         this.frameCount = 0;
/* 2424 */         this.stackMap = null;
/* 2425 */         this.previousFrame = null;
/* 2426 */         this.frame = null;
/* 2427 */         Frame f = new Frame();
/* 2428 */         f.owner = this.labels;
/* 2429 */         Type[] args = Type.getArgumentTypes(this.descriptor);
/* 2430 */         f.initInputFrame(this.cw, this.access, args, this.maxLocals);
/* 2431 */         visitFrame(f);
/* 2432 */         Label l = this.labels;
/* 2433 */         while (l != null)
/*      */         {
/* 2438 */           u = l.position - 3;
/* 2439 */           if (((l.status & 0x20) != 0) || ((u >= 0) && (resize[u] != 0)))
/*      */           {
/* 2441 */             getNewOffset(allIndexes, allSizes, l);
/*      */ 
/* 2443 */             visitFrame(l.frame);
/*      */           }
/* 2445 */           l = l.successor;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2462 */         this.cw.invalidFrames = true;
/*      */       }
/*      */     }
/*      */ 
/* 2466 */     Handler h = this.firstHandler;
/* 2467 */     while (h != null) {
/* 2468 */       getNewOffset(allIndexes, allSizes, h.start);
/* 2469 */       getNewOffset(allIndexes, allSizes, h.end);
/* 2470 */       getNewOffset(allIndexes, allSizes, h.handler);
/* 2471 */       h = h.next;
/*      */     }
/*      */ 
/* 2475 */     for (int i = 0; i < 2; i++) {
/* 2476 */       ByteVector bv = i == 0 ? this.localVar : this.localVarType;
/* 2477 */       if (bv != null) {
/* 2478 */         b = bv.data;
/* 2479 */         u = 0;
/* 2480 */         while (u < bv.length) {
/* 2481 */           label = readUnsignedShort(b, u);
/* 2482 */           newOffset = getNewOffset(allIndexes, allSizes, 0, label);
/* 2483 */           writeShort(b, u, newOffset);
/* 2484 */           label += readUnsignedShort(b, u + 2);
/* 2485 */           newOffset = getNewOffset(allIndexes, allSizes, 0, label) - newOffset;
/*      */ 
/* 2487 */           writeShort(b, u + 2, newOffset);
/* 2488 */           u += 10;
/*      */         }
/*      */       }
/*      */     }
/* 2492 */     if (this.lineNumber != null) {
/* 2493 */       b = this.lineNumber.data;
/* 2494 */       u = 0;
/* 2495 */       while (u < this.lineNumber.length) {
/* 2496 */         writeShort(b, u, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, u)));
/*      */ 
/* 2500 */         u += 4;
/*      */       }
/*      */     }
/*      */ 
/* 2504 */     Attribute attr = this.cattrs;
/* 2505 */     while (attr != null) {
/* 2506 */       Label[] labels = attr.getLabels();
/* 2507 */       if (labels != null) {
/* 2508 */         for (i = labels.length - 1; i >= 0; i--) {
/* 2509 */           getNewOffset(allIndexes, allSizes, labels[i]);
/*      */         }
/*      */       }
/* 2512 */       attr = attr.next;
/*      */     }
/*      */ 
/* 2516 */     this.code = newCode;
/*      */   }
/*      */ 
/*      */   static int readUnsignedShort(byte[] b, int index)
/*      */   {
/* 2527 */     return (b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF;
/*      */   }
/*      */ 
/*      */   static short readShort(byte[] b, int index)
/*      */   {
/* 2538 */     return (short)((b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF);
/*      */   }
/*      */ 
/*      */   static int readInt(byte[] b, int index)
/*      */   {
/* 2549 */     return (b[index] & 0xFF) << 24 | (b[(index + 1)] & 0xFF) << 16 | (b[(index + 2)] & 0xFF) << 8 | b[(index + 3)] & 0xFF;
/*      */   }
/*      */ 
/*      */   static void writeShort(byte[] b, int index, int s)
/*      */   {
/* 2561 */     b[index] = ((byte)(s >>> 8));
/* 2562 */     b[(index + 1)] = ((byte)s);
/*      */   }
/*      */ 
/*      */   static int getNewOffset(int[] indexes, int[] sizes, int begin, int end)
/*      */   {
/* 2592 */     int offset = end - begin;
/* 2593 */     for (int i = 0; i < indexes.length; i++) {
/* 2594 */       if ((begin < indexes[i]) && (indexes[i] <= end))
/*      */       {
/* 2596 */         offset += sizes[i];
/* 2597 */       } else if ((end < indexes[i]) && (indexes[i] <= begin))
/*      */       {
/* 2599 */         offset -= sizes[i];
/*      */       }
/*      */     }
/* 2602 */     return offset;
/*      */   }
/*      */ 
/*      */   static void getNewOffset(int[] indexes, int[] sizes, Label label)
/*      */   {
/* 2626 */     if ((label.status & 0x4) == 0) {
/* 2627 */       label.position = getNewOffset(indexes, sizes, 0, label.position);
/* 2628 */       label.status |= 4;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.MethodWriter
 * JD-Core Version:    0.6.2
 */