/*      */ package com.sun.org.apache.bcel.internal.generic;
/*      */ 
/*      */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*      */ import com.sun.org.apache.bcel.internal.util.ByteSequence;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ 
/*      */ public class InstructionList
/*      */   implements Serializable
/*      */ {
/*   88 */   private InstructionHandle start = null; private InstructionHandle end = null;
/*   89 */   private int length = 0;
/*      */   private int[] byte_positions;
/*      */   private ArrayList observers;
/*      */ 
/*      */   public InstructionList()
/*      */   {
/*      */   }
/*      */ 
/*      */   public InstructionList(Instruction i)
/*      */   {
/*  102 */     append(i);
/*      */   }
/*      */ 
/*      */   public InstructionList(BranchInstruction i)
/*      */   {
/*  110 */     append(i);
/*      */   }
/*      */ 
/*      */   public InstructionList(CompoundInstruction c)
/*      */   {
/*  120 */     append(c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  126 */     return this.start == null;
/*      */   }
/*      */ 
/*      */   public static InstructionHandle findHandle(InstructionHandle[] ihs, int[] pos, int count, int target)
/*      */   {
/*  141 */     int l = 0; int r = count - 1;
/*      */     do
/*      */     {
/*  146 */       int i = (l + r) / 2;
/*  147 */       int j = pos[i];
/*      */ 
/*  149 */       if (j == target)
/*  150 */         return ihs[i];
/*  151 */       if (target < j)
/*  152 */         r = i - 1;
/*      */       else
/*  154 */         l = i + 1; 
/*      */     }
/*  155 */     while (l <= r);
/*      */ 
/*  157 */     return null;
/*      */   }
/*      */ 
/*      */   public InstructionHandle findHandle(int pos)
/*      */   {
/*  169 */     InstructionHandle[] ihs = getInstructionHandles();
/*  170 */     return findHandle(ihs, this.byte_positions, this.length, pos);
/*      */   }
/*      */ 
/*      */   public InstructionList(byte[] code)
/*      */   {
/*  179 */     ByteSequence bytes = new ByteSequence(code);
/*  180 */     InstructionHandle[] ihs = new InstructionHandle[code.length];
/*  181 */     int[] pos = new int[code.length];
/*  182 */     int count = 0;
/*      */     try
/*      */     {
/*  188 */       while (bytes.available() > 0)
/*      */       {
/*  190 */         int off = bytes.getIndex();
/*  191 */         pos[count] = off;
/*      */ 
/*  196 */         Instruction i = Instruction.readInstruction(bytes);
/*      */         InstructionHandle ih;
/*      */         InstructionHandle ih;
/*  198 */         if ((i instanceof BranchInstruction))
/*  199 */           ih = append((BranchInstruction)i);
/*      */         else {
/*  201 */           ih = append(i);
/*      */         }
/*  203 */         ih.setPosition(off);
/*  204 */         ihs[count] = ih;
/*      */ 
/*  206 */         count++;
/*      */       }
/*      */     } catch (IOException e) { throw new ClassGenException(e.toString()); }
/*      */ 
/*  210 */     this.byte_positions = new int[count];
/*  211 */     System.arraycopy(pos, 0, this.byte_positions, 0, count);
/*      */ 
/*  216 */     for (int i = 0; i < count; i++)
/*  217 */       if ((ihs[i] instanceof BranchHandle)) {
/*  218 */         BranchInstruction bi = (BranchInstruction)ihs[i].instruction;
/*  219 */         int target = bi.position + bi.getIndex();
/*      */ 
/*  222 */         InstructionHandle ih = findHandle(ihs, pos, count, target);
/*      */ 
/*  224 */         if (ih == null) {
/*  225 */           throw new ClassGenException("Couldn't find target for branch: " + bi);
/*      */         }
/*  227 */         bi.setTarget(ih);
/*      */ 
/*  230 */         if ((bi instanceof Select)) {
/*  231 */           Select s = (Select)bi;
/*  232 */           int[] indices = s.getIndices();
/*      */ 
/*  234 */           for (int j = 0; j < indices.length; j++) {
/*  235 */             target = bi.position + indices[j];
/*  236 */             ih = findHandle(ihs, pos, count, target);
/*      */ 
/*  238 */             if (ih == null) {
/*  239 */               throw new ClassGenException("Couldn't find target for switch: " + bi);
/*      */             }
/*  241 */             s.setTarget(j, ih);
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(InstructionHandle ih, InstructionList il)
/*      */   {
/*  257 */     if (il == null) {
/*  258 */       throw new ClassGenException("Appending null InstructionList");
/*      */     }
/*  260 */     if (il.isEmpty()) {
/*  261 */       return ih;
/*      */     }
/*  263 */     InstructionHandle next = ih.next; InstructionHandle ret = il.start;
/*      */ 
/*  265 */     ih.next = il.start;
/*  266 */     il.start.prev = ih;
/*      */ 
/*  268 */     il.end.next = next;
/*      */ 
/*  270 */     if (next != null)
/*  271 */       next.prev = il.end;
/*      */     else {
/*  273 */       this.end = il.end;
/*      */     }
/*  275 */     this.length += il.length;
/*      */ 
/*  277 */     il.clear();
/*      */ 
/*  279 */     return ret;
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(Instruction i, InstructionList il)
/*      */   {
/*      */     InstructionHandle ih;
/*  293 */     if ((ih = findInstruction2(i)) == null) {
/*  294 */       throw new ClassGenException("Instruction " + i + " is not contained in this list.");
/*      */     }
/*      */ 
/*  297 */     return append(ih, il);
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(InstructionList il)
/*      */   {
/*  308 */     if (il == null) {
/*  309 */       throw new ClassGenException("Appending null InstructionList");
/*      */     }
/*  311 */     if (il.isEmpty()) {
/*  312 */       return null;
/*      */     }
/*  314 */     if (isEmpty()) {
/*  315 */       this.start = il.start;
/*  316 */       this.end = il.end;
/*  317 */       this.length = il.length;
/*      */ 
/*  319 */       il.clear();
/*      */ 
/*  321 */       return this.start;
/*      */     }
/*  323 */     return append(this.end, il);
/*      */   }
/*      */ 
/*      */   private void append(InstructionHandle ih)
/*      */   {
/*  332 */     if (isEmpty()) {
/*  333 */       this.start = (this.end = ih);
/*  334 */       ih.next = (ih.prev = null);
/*      */     }
/*      */     else {
/*  337 */       this.end.next = ih;
/*  338 */       ih.prev = this.end;
/*  339 */       ih.next = null;
/*  340 */       this.end = ih;
/*      */     }
/*      */ 
/*  343 */     this.length += 1;
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(Instruction i)
/*      */   {
/*  353 */     InstructionHandle ih = InstructionHandle.getInstructionHandle(i);
/*  354 */     append(ih);
/*      */ 
/*  356 */     return ih;
/*      */   }
/*      */ 
/*      */   public BranchHandle append(BranchInstruction i)
/*      */   {
/*  366 */     BranchHandle ih = BranchHandle.getBranchHandle(i);
/*  367 */     append(ih);
/*      */ 
/*  369 */     return ih;
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(Instruction i, Instruction j)
/*      */   {
/*  381 */     return append(i, new InstructionList(j));
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(Instruction i, CompoundInstruction c)
/*      */   {
/*  392 */     return append(i, c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(CompoundInstruction c)
/*      */   {
/*  402 */     return append(c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(InstructionHandle ih, CompoundInstruction c)
/*      */   {
/*  413 */     return append(ih, c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public InstructionHandle append(InstructionHandle ih, Instruction i)
/*      */   {
/*  424 */     return append(ih, new InstructionList(i));
/*      */   }
/*      */ 
/*      */   public BranchHandle append(InstructionHandle ih, BranchInstruction i)
/*      */   {
/*  435 */     BranchHandle bh = BranchHandle.getBranchHandle(i);
/*  436 */     InstructionList il = new InstructionList();
/*  437 */     il.append(bh);
/*      */ 
/*  439 */     append(ih, il);
/*      */ 
/*  441 */     return bh;
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(InstructionHandle ih, InstructionList il)
/*      */   {
/*  453 */     if (il == null) {
/*  454 */       throw new ClassGenException("Inserting null InstructionList");
/*      */     }
/*  456 */     if (il.isEmpty()) {
/*  457 */       return ih;
/*      */     }
/*  459 */     InstructionHandle prev = ih.prev; InstructionHandle ret = il.start;
/*      */ 
/*  461 */     ih.prev = il.end;
/*  462 */     il.end.next = ih;
/*      */ 
/*  464 */     il.start.prev = prev;
/*      */ 
/*  466 */     if (prev != null)
/*  467 */       prev.next = il.start;
/*      */     else {
/*  469 */       this.start = il.start;
/*      */     }
/*  471 */     this.length += il.length;
/*      */ 
/*  473 */     il.clear();
/*      */ 
/*  475 */     return ret;
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(InstructionList il)
/*      */   {
/*  485 */     if (isEmpty()) {
/*  486 */       append(il);
/*  487 */       return this.start;
/*      */     }
/*      */ 
/*  490 */     return insert(this.start, il);
/*      */   }
/*      */ 
/*      */   private void insert(InstructionHandle ih)
/*      */   {
/*  499 */     if (isEmpty()) {
/*  500 */       this.start = (this.end = ih);
/*  501 */       ih.next = (ih.prev = null);
/*      */     } else {
/*  503 */       this.start.prev = ih;
/*  504 */       ih.next = this.start;
/*  505 */       ih.prev = null;
/*  506 */       this.start = ih;
/*      */     }
/*      */ 
/*  509 */     this.length += 1;
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(Instruction i, InstructionList il)
/*      */   {
/*      */     InstructionHandle ih;
/*  524 */     if ((ih = findInstruction1(i)) == null) {
/*  525 */       throw new ClassGenException("Instruction " + i + " is not contained in this list.");
/*      */     }
/*      */ 
/*  528 */     return insert(ih, il);
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(Instruction i)
/*      */   {
/*  538 */     InstructionHandle ih = InstructionHandle.getInstructionHandle(i);
/*  539 */     insert(ih);
/*      */ 
/*  541 */     return ih;
/*      */   }
/*      */ 
/*      */   public BranchHandle insert(BranchInstruction i)
/*      */   {
/*  551 */     BranchHandle ih = BranchHandle.getBranchHandle(i);
/*  552 */     insert(ih);
/*  553 */     return ih;
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(Instruction i, Instruction j)
/*      */   {
/*  565 */     return insert(i, new InstructionList(j));
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(Instruction i, CompoundInstruction c)
/*      */   {
/*  576 */     return insert(i, c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(CompoundInstruction c)
/*      */   {
/*  586 */     return insert(c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(InstructionHandle ih, Instruction i)
/*      */   {
/*  597 */     return insert(ih, new InstructionList(i));
/*      */   }
/*      */ 
/*      */   public InstructionHandle insert(InstructionHandle ih, CompoundInstruction c)
/*      */   {
/*  608 */     return insert(ih, c.getInstructionList());
/*      */   }
/*      */ 
/*      */   public BranchHandle insert(InstructionHandle ih, BranchInstruction i)
/*      */   {
/*  619 */     BranchHandle bh = BranchHandle.getBranchHandle(i);
/*  620 */     InstructionList il = new InstructionList();
/*  621 */     il.append(bh);
/*      */ 
/*  623 */     insert(ih, il);
/*      */ 
/*  625 */     return bh;
/*      */   }
/*      */ 
/*      */   public void move(InstructionHandle start, InstructionHandle end, InstructionHandle target)
/*      */   {
/*  642 */     if ((start == null) || (end == null)) {
/*  643 */       throw new ClassGenException("Invalid null handle: From " + start + " to " + end);
/*      */     }
/*  645 */     if ((target == start) || (target == end)) {
/*  646 */       throw new ClassGenException("Invalid range: From " + start + " to " + end + " contains target " + target);
/*      */     }
/*      */ 
/*  649 */     for (InstructionHandle ih = start; ih != end.next; ih = ih.next) {
/*  650 */       if (ih == null)
/*  651 */         throw new ClassGenException("Invalid range: From " + start + " to " + end);
/*  652 */       if (ih == target) {
/*  653 */         throw new ClassGenException("Invalid range: From " + start + " to " + end + " contains target " + target);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  659 */     InstructionHandle prev = start.prev; InstructionHandle next = end.next;
/*      */ 
/*  661 */     if (prev != null)
/*  662 */       prev.next = next;
/*      */     else {
/*  664 */       this.start = next;
/*      */     }
/*  666 */     if (next != null)
/*  667 */       next.prev = prev;
/*      */     else {
/*  669 */       this.end = prev;
/*      */     }
/*  671 */     start.prev = (end.next = null);
/*      */ 
/*  675 */     if (target == null) {
/*  676 */       end.next = this.start;
/*  677 */       this.start = start;
/*      */     } else {
/*  679 */       next = target.next;
/*      */ 
/*  681 */       target.next = start;
/*  682 */       start.prev = target;
/*  683 */       end.next = next;
/*      */ 
/*  685 */       if (next != null)
/*  686 */         next.prev = end;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void move(InstructionHandle ih, InstructionHandle target)
/*      */   {
/*  697 */     move(ih, ih, target);
/*      */   }
/*      */ 
/*      */   private void remove(InstructionHandle prev, InstructionHandle next)
/*      */     throws TargetLostException
/*      */   {
/*      */     InstructionHandle first;
/*      */     InstructionHandle last;
/*  713 */     if ((prev == null) && (next == null))
/*      */     {
/*      */       InstructionHandle last;
/*  714 */       InstructionHandle first = last = this.start;
/*  715 */       this.start = (this.end = null);
/*      */     } else {
/*  717 */       if (prev == null) {
/*  718 */         InstructionHandle first = this.start;
/*  719 */         this.start = next;
/*      */       } else {
/*  721 */         first = prev.next;
/*  722 */         prev.next = next;
/*      */       }
/*      */ 
/*  725 */       if (next == null) {
/*  726 */         InstructionHandle last = this.end;
/*  727 */         this.end = prev;
/*      */       } else {
/*  729 */         last = next.prev;
/*  730 */         next.prev = prev;
/*      */       }
/*      */     }
/*      */ 
/*  734 */     first.prev = null;
/*  735 */     last.next = null;
/*      */ 
/*  737 */     ArrayList target_vec = new ArrayList();
/*      */ 
/*  739 */     for (InstructionHandle ih = first; ih != null; ih = ih.next) {
/*  740 */       ih.getInstruction().dispose();
/*      */     }
/*  742 */     StringBuffer buf = new StringBuffer("{ ");
/*  743 */     for (InstructionHandle ih = first; ih != null; ih = next) {
/*  744 */       next = ih.next;
/*  745 */       this.length -= 1;
/*      */ 
/*  747 */       if (ih.hasTargeters()) {
/*  748 */         target_vec.add(ih);
/*  749 */         buf.append(ih.toString(true) + " ");
/*  750 */         ih.next = (ih.prev = null);
/*      */       } else {
/*  752 */         ih.dispose();
/*      */       }
/*      */     }
/*  755 */     buf.append("}");
/*      */ 
/*  757 */     if (!target_vec.isEmpty()) {
/*  758 */       InstructionHandle[] targeted = new InstructionHandle[target_vec.size()];
/*  759 */       target_vec.toArray(targeted);
/*  760 */       throw new TargetLostException(targeted, buf.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void delete(InstructionHandle ih)
/*      */     throws TargetLostException
/*      */   {
/*  771 */     remove(ih.prev, ih.next);
/*      */   }
/*      */ 
/*      */   public void delete(Instruction i)
/*      */     throws TargetLostException
/*      */   {
/*      */     InstructionHandle ih;
/*  783 */     if ((ih = findInstruction1(i)) == null) {
/*  784 */       throw new ClassGenException("Instruction " + i + " is not contained in this list.");
/*      */     }
/*  786 */     delete(ih);
/*      */   }
/*      */ 
/*      */   public void delete(InstructionHandle from, InstructionHandle to)
/*      */     throws TargetLostException
/*      */   {
/*  800 */     remove(from.prev, to.next);
/*      */   }
/*      */ 
/*      */   public void delete(Instruction from, Instruction to)
/*      */     throws TargetLostException
/*      */   {
/*      */     InstructionHandle from_ih;
/*  814 */     if ((from_ih = findInstruction1(from)) == null)
/*  815 */       throw new ClassGenException("Instruction " + from + " is not contained in this list.");
/*      */     InstructionHandle to_ih;
/*  818 */     if ((to_ih = findInstruction2(to)) == null) {
/*  819 */       throw new ClassGenException("Instruction " + to + " is not contained in this list.");
/*      */     }
/*  821 */     delete(from_ih, to_ih);
/*      */   }
/*      */ 
/*      */   private InstructionHandle findInstruction1(Instruction i)
/*      */   {
/*  831 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  832 */       if (ih.instruction == i)
/*  833 */         return ih;
/*      */     }
/*  835 */     return null;
/*      */   }
/*      */ 
/*      */   private InstructionHandle findInstruction2(Instruction i)
/*      */   {
/*  845 */     for (InstructionHandle ih = this.end; ih != null; ih = ih.prev) {
/*  846 */       if (ih.instruction == i)
/*  847 */         return ih;
/*      */     }
/*  849 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean contains(InstructionHandle i) {
/*  853 */     if (i == null) {
/*  854 */       return false;
/*      */     }
/*  856 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  857 */       if (ih == i)
/*  858 */         return true;
/*      */     }
/*  860 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean contains(Instruction i) {
/*  864 */     return findInstruction1(i) != null;
/*      */   }
/*      */ 
/*      */   public void setPositions() {
/*  868 */     setPositions(false);
/*      */   }
/*      */ 
/*      */   public void setPositions(boolean check)
/*      */   {
/*  879 */     int max_additional_bytes = 0; int additional_bytes = 0;
/*  880 */     int index = 0; int count = 0;
/*  881 */     int[] pos = new int[this.length];
/*      */ 
/*  885 */     if (check) {
/*  886 */       for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  887 */         Instruction i = ih.instruction;
/*      */ 
/*  889 */         if ((i instanceof BranchInstruction)) {
/*  890 */           Instruction inst = ((BranchInstruction)i).getTarget().instruction;
/*  891 */           if (!contains(inst)) {
/*  892 */             throw new ClassGenException("Branch target of " + com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[i.opcode] + ":" + inst + " not in instruction list");
/*      */           }
/*      */ 
/*  896 */           if ((i instanceof Select)) {
/*  897 */             InstructionHandle[] targets = ((Select)i).getTargets();
/*      */ 
/*  899 */             for (int j = 0; j < targets.length; j++) {
/*  900 */               inst = targets[j].instruction;
/*  901 */               if (!contains(inst)) {
/*  902 */                 throw new ClassGenException("Branch target of " + com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[i.opcode] + ":" + inst + " not in instruction list");
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  908 */           if (!(ih instanceof BranchHandle)) {
/*  909 */             throw new ClassGenException("Branch instruction " + com.sun.org.apache.bcel.internal.Constants.OPCODE_NAMES[i.opcode] + ":" + inst + " not contained in BranchHandle.");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  920 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  921 */       Instruction i = ih.instruction;
/*      */ 
/*  923 */       ih.setPosition(index);
/*  924 */       pos[(count++)] = index;
/*      */ 
/*  931 */       switch (i.getOpcode()) { case 167:
/*      */       case 168:
/*  933 */         max_additional_bytes += 2;
/*  934 */         break;
/*      */       case 170:
/*      */       case 171:
/*  937 */         max_additional_bytes += 3;
/*      */       case 169:
/*      */       }
/*      */ 
/*  941 */       index += i.getLength();
/*      */     }
/*      */ 
/*  948 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  949 */       additional_bytes += ih.updatePosition(additional_bytes, max_additional_bytes);
/*      */     }
/*      */ 
/*  954 */     index = count = 0;
/*  955 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  956 */       Instruction i = ih.instruction;
/*      */ 
/*  958 */       ih.setPosition(index);
/*  959 */       pos[(count++)] = index;
/*  960 */       index += i.getLength();
/*      */     }
/*      */ 
/*  963 */     this.byte_positions = new int[count];
/*  964 */     System.arraycopy(pos, 0, this.byte_positions, 0, count);
/*      */   }
/*      */ 
/*      */   public byte[] getByteCode()
/*      */   {
/*  975 */     setPositions();
/*      */ 
/*  977 */     ByteArrayOutputStream b = new ByteArrayOutputStream();
/*  978 */     DataOutputStream out = new DataOutputStream(b);
/*      */     try
/*      */     {
/*  981 */       for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/*  982 */         Instruction i = ih.instruction;
/*  983 */         i.dump(out);
/*      */       }
/*      */     } catch (IOException e) {
/*  986 */       System.err.println(e);
/*  987 */       return null;
/*      */     }
/*      */ 
/*  990 */     return b.toByteArray();
/*      */   }
/*      */ 
/*      */   public Instruction[] getInstructions()
/*      */   {
/*  997 */     ByteSequence bytes = new ByteSequence(getByteCode());
/*  998 */     ArrayList instructions = new ArrayList();
/*      */     try
/*      */     {
/* 1001 */       while (bytes.available() > 0)
/* 1002 */         instructions.add(Instruction.readInstruction(bytes));
/*      */     } catch (IOException e) {
/* 1004 */       throw new ClassGenException(e.toString());
/*      */     }
/* 1006 */     Instruction[] result = new Instruction[instructions.size()];
/* 1007 */     instructions.toArray(result);
/* 1008 */     return result;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 1012 */     return toString(true);
/*      */   }
/*      */ 
/*      */   public String toString(boolean verbose)
/*      */   {
/* 1020 */     StringBuffer buf = new StringBuffer();
/*      */ 
/* 1022 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/* 1023 */       buf.append(ih.toString(verbose) + "\n");
/*      */     }
/*      */ 
/* 1026 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   public Iterator iterator()
/*      */   {
/* 1033 */     return new Iterator() {
/* 1034 */       private InstructionHandle ih = InstructionList.this.start;
/*      */ 
/*      */       public Object next() {
/* 1037 */         InstructionHandle i = this.ih;
/* 1038 */         this.ih = this.ih.next;
/* 1039 */         return i;
/*      */       }
/*      */ 
/*      */       public void remove() {
/* 1043 */         throw new UnsupportedOperationException();
/*      */       }
/*      */       public boolean hasNext() {
/* 1046 */         return this.ih != null;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public InstructionHandle[] getInstructionHandles()
/*      */   {
/* 1054 */     InstructionHandle[] ihs = new InstructionHandle[this.length];
/* 1055 */     InstructionHandle ih = this.start;
/*      */ 
/* 1057 */     for (int i = 0; i < this.length; i++) {
/* 1058 */       ihs[i] = ih;
/* 1059 */       ih = ih.next;
/*      */     }
/*      */ 
/* 1062 */     return ihs;
/*      */   }
/*      */ 
/*      */   public int[] getInstructionPositions()
/*      */   {
/* 1072 */     return this.byte_positions;
/*      */   }
/*      */ 
/*      */   public InstructionList copy()
/*      */   {
/* 1078 */     HashMap map = new HashMap();
/* 1079 */     InstructionList il = new InstructionList();
/*      */ 
/* 1085 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/* 1086 */       Instruction i = ih.instruction;
/* 1087 */       Instruction c = i.copy();
/*      */ 
/* 1089 */       if ((c instanceof BranchInstruction))
/* 1090 */         map.put(ih, il.append((BranchInstruction)c));
/*      */       else {
/* 1092 */         map.put(ih, il.append(c));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1097 */     InstructionHandle ih = this.start;
/* 1098 */     InstructionHandle ch = il.start;
/*      */ 
/* 1100 */     while (ih != null) {
/* 1101 */       Instruction i = ih.instruction;
/* 1102 */       Instruction c = ch.instruction;
/*      */ 
/* 1104 */       if ((i instanceof BranchInstruction)) {
/* 1105 */         BranchInstruction bi = (BranchInstruction)i;
/* 1106 */         BranchInstruction bc = (BranchInstruction)c;
/* 1107 */         InstructionHandle itarget = bi.getTarget();
/*      */ 
/* 1110 */         bc.setTarget((InstructionHandle)map.get(itarget));
/*      */ 
/* 1112 */         if ((bi instanceof Select)) {
/* 1113 */           InstructionHandle[] itargets = ((Select)bi).getTargets();
/* 1114 */           InstructionHandle[] ctargets = ((Select)bc).getTargets();
/*      */ 
/* 1116 */           for (int j = 0; j < itargets.length; j++) {
/* 1117 */             ctargets[j] = ((InstructionHandle)map.get(itargets[j]));
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1122 */       ih = ih.next;
/* 1123 */       ch = ch.next;
/*      */     }
/*      */ 
/* 1126 */     return il;
/*      */   }
/*      */ 
/*      */   public void replaceConstantPool(ConstantPoolGen old_cp, ConstantPoolGen new_cp)
/*      */   {
/* 1133 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/* 1134 */       Instruction i = ih.instruction;
/*      */ 
/* 1136 */       if ((i instanceof CPInstruction)) {
/* 1137 */         CPInstruction ci = (CPInstruction)i;
/* 1138 */         Constant c = old_cp.getConstant(ci.getIndex());
/* 1139 */         ci.setIndex(new_cp.addConstant(c, old_cp));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void clear() {
/* 1145 */     this.start = (this.end = null);
/* 1146 */     this.length = 0;
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 1157 */     for (InstructionHandle ih = this.end; ih != null; ih = ih.prev)
/*      */     {
/* 1161 */       ih.dispose();
/*      */     }
/* 1163 */     clear();
/*      */   }
/*      */ 
/*      */   public InstructionHandle getStart()
/*      */   {
/* 1169 */     return this.start;
/*      */   }
/*      */ 
/*      */   public InstructionHandle getEnd()
/*      */   {
/* 1174 */     return this.end;
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/* 1179 */     return this.length;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 1184 */     return this.length;
/*      */   }
/*      */ 
/*      */   public void redirectBranches(InstructionHandle old_target, InstructionHandle new_target)
/*      */   {
/* 1195 */     for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
/* 1196 */       Instruction i = ih.getInstruction();
/*      */ 
/* 1198 */       if ((i instanceof BranchInstruction)) {
/* 1199 */         BranchInstruction b = (BranchInstruction)i;
/* 1200 */         InstructionHandle target = b.getTarget();
/*      */ 
/* 1202 */         if (target == old_target) {
/* 1203 */           b.setTarget(new_target);
/*      */         }
/* 1205 */         if ((b instanceof Select)) {
/* 1206 */           InstructionHandle[] targets = ((Select)b).getTargets();
/*      */ 
/* 1208 */           for (int j = 0; j < targets.length; j++)
/* 1209 */             if (targets[j] == old_target)
/* 1210 */               ((Select)b).setTarget(j, new_target);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void redirectLocalVariables(LocalVariableGen[] lg, InstructionHandle old_target, InstructionHandle new_target)
/*      */   {
/* 1227 */     for (int i = 0; i < lg.length; i++) {
/* 1228 */       InstructionHandle start = lg[i].getStart();
/* 1229 */       InstructionHandle end = lg[i].getEnd();
/*      */ 
/* 1231 */       if (start == old_target) {
/* 1232 */         lg[i].setStart(new_target);
/*      */       }
/* 1234 */       if (end == old_target)
/* 1235 */         lg[i].setEnd(new_target);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void redirectExceptionHandlers(CodeExceptionGen[] exceptions, InstructionHandle old_target, InstructionHandle new_target)
/*      */   {
/* 1250 */     for (int i = 0; i < exceptions.length; i++) {
/* 1251 */       if (exceptions[i].getStartPC() == old_target) {
/* 1252 */         exceptions[i].setStartPC(new_target);
/*      */       }
/* 1254 */       if (exceptions[i].getEndPC() == old_target) {
/* 1255 */         exceptions[i].setEndPC(new_target);
/*      */       }
/* 1257 */       if (exceptions[i].getHandlerPC() == old_target)
/* 1258 */         exceptions[i].setHandlerPC(new_target);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addObserver(InstructionListObserver o)
/*      */   {
/* 1267 */     if (this.observers == null) {
/* 1268 */       this.observers = new ArrayList();
/*      */     }
/* 1270 */     this.observers.add(o);
/*      */   }
/*      */ 
/*      */   public void removeObserver(InstructionListObserver o)
/*      */   {
/* 1276 */     if (this.observers != null)
/* 1277 */       this.observers.remove(o);
/*      */   }
/*      */ 
/*      */   public void update()
/*      */   {
/*      */     Iterator e;
/* 1285 */     if (this.observers != null)
/* 1286 */       for (e = this.observers.iterator(); e.hasNext(); )
/* 1287 */         ((InstructionListObserver)e.next()).notify(this);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.InstructionList
 * JD-Core Version:    0.6.2
 */