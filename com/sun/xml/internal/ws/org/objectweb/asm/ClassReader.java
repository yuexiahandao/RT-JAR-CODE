/*      */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ public class ClassReader
/*      */ {
/*      */   static final boolean SIGNATURES = true;
/*      */   static final boolean ANNOTATIONS = true;
/*      */   static final boolean FRAMES = true;
/*      */   static final boolean WRITER = true;
/*      */   static final boolean RESIZE = true;
/*      */   public static final int SKIP_CODE = 1;
/*      */   public static final int SKIP_DEBUG = 2;
/*      */   public static final int SKIP_FRAMES = 4;
/*      */   public static final int EXPAND_FRAMES = 8;
/*      */   public final byte[] b;
/*      */   private final int[] items;
/*      */   private final String[] strings;
/*      */   private final int maxStringLength;
/*      */   public final int header;
/*      */ 
/*      */   public ClassReader(byte[] b)
/*      */   {
/*  183 */     this(b, 0, b.length);
/*      */   }
/*      */ 
/*      */   public ClassReader(byte[] b, int off, int len)
/*      */   {
/*  194 */     this.b = b;
/*      */ 
/*  196 */     this.items = new int[readUnsignedShort(off + 8)];
/*  197 */     int n = this.items.length;
/*  198 */     this.strings = new String[n];
/*  199 */     int max = 0;
/*  200 */     int index = off + 10;
/*  201 */     for (int i = 1; i < n; i++) {
/*  202 */       this.items[i] = (index + 1);
/*      */       int size;
/*  204 */       switch (b[index]) {
/*      */       case 3:
/*      */       case 4:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*  211 */         size = 5;
/*  212 */         break;
/*      */       case 5:
/*      */       case 6:
/*  215 */         size = 9;
/*  216 */         i++;
/*  217 */         break;
/*      */       case 1:
/*  219 */         size = 3 + readUnsignedShort(index + 1);
/*  220 */         if (size > max)
/*  221 */           max = size; break;
/*      */       case 2:
/*      */       case 7:
/*      */       case 8:
/*      */       default:
/*  227 */         size = 3;
/*      */       }
/*      */ 
/*  230 */       index += size;
/*      */     }
/*  232 */     this.maxStringLength = max;
/*      */ 
/*  234 */     this.header = index;
/*      */   }
/*      */ 
/*      */   public int getAccess()
/*      */   {
/*  247 */     return readUnsignedShort(this.header);
/*      */   }
/*      */ 
/*      */   public String getClassName()
/*      */   {
/*  259 */     return readClass(this.header + 2, new char[this.maxStringLength]);
/*      */   }
/*      */ 
/*      */   public String getSuperName()
/*      */   {
/*  273 */     int n = this.items[readUnsignedShort(this.header + 4)];
/*  274 */     return n == 0 ? null : readUTF8(n, new char[this.maxStringLength]);
/*      */   }
/*      */ 
/*      */   public String[] getInterfaces()
/*      */   {
/*  287 */     int index = this.header + 6;
/*  288 */     int n = readUnsignedShort(index);
/*  289 */     String[] interfaces = new String[n];
/*  290 */     if (n > 0) {
/*  291 */       char[] buf = new char[this.maxStringLength];
/*  292 */       for (int i = 0; i < n; i++) {
/*  293 */         index += 2;
/*  294 */         interfaces[i] = readClass(index, buf);
/*      */       }
/*      */     }
/*  297 */     return interfaces;
/*      */   }
/*      */ 
/*      */   void copyPool(ClassWriter classWriter)
/*      */   {
/*  307 */     char[] buf = new char[this.maxStringLength];
/*  308 */     int ll = this.items.length;
/*  309 */     Item[] items2 = new Item[ll];
/*  310 */     for (int i = 1; i < ll; i++) {
/*  311 */       int index = this.items[i];
/*  312 */       int tag = this.b[(index - 1)];
/*  313 */       Item item = new Item(i);
/*      */ 
/*  315 */       switch (tag) {
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*  319 */         int nameType = this.items[readUnsignedShort(index + 2)];
/*  320 */         item.set(tag, readClass(index, buf), readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
/*      */ 
/*  324 */         break;
/*      */       case 3:
/*  327 */         item.set(readInt(index));
/*  328 */         break;
/*      */       case 4:
/*  331 */         item.set(Float.intBitsToFloat(readInt(index)));
/*  332 */         break;
/*      */       case 12:
/*  335 */         item.set(tag, readUTF8(index, buf), readUTF8(index + 2, buf), null);
/*      */ 
/*  339 */         break;
/*      */       case 5:
/*  342 */         item.set(readLong(index));
/*  343 */         i++;
/*  344 */         break;
/*      */       case 6:
/*  347 */         item.set(Double.longBitsToDouble(readLong(index)));
/*  348 */         i++;
/*  349 */         break;
/*      */       case 1:
/*  352 */         String s = this.strings[i];
/*  353 */         if (s == null) {
/*  354 */           index = this.items[i];
/*  355 */           s = this.strings[i] =  = readUTF(index + 2, readUnsignedShort(index), buf);
/*      */         }
/*      */ 
/*  359 */         item.set(tag, s, null, null);
/*      */ 
/*  361 */         break;
/*      */       case 2:
/*      */       case 7:
/*      */       case 8:
/*      */       default:
/*  366 */         item.set(tag, readUTF8(index, buf), null, null);
/*      */       }
/*      */ 
/*  370 */       int index2 = item.hashCode % items2.length;
/*  371 */       item.next = items2[index2];
/*  372 */       items2[index2] = item;
/*      */     }
/*      */ 
/*  375 */     int off = this.items[1] - 1;
/*  376 */     classWriter.pool.putByteArray(this.b, off, this.header - off);
/*  377 */     classWriter.items = items2;
/*  378 */     classWriter.threshold = ((int)(0.75D * ll));
/*  379 */     classWriter.index = ll;
/*      */   }
/*      */ 
/*      */   public ClassReader(InputStream is)
/*      */     throws IOException
/*      */   {
/*  389 */     this(readClass(is));
/*      */   }
/*      */ 
/*      */   public ClassReader(String name)
/*      */     throws IOException
/*      */   {
/*  399 */     this(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
/*      */   }
/*      */ 
/*      */   private static byte[] readClass(InputStream is)
/*      */     throws IOException
/*      */   {
/*  411 */     if (is == null) {
/*  412 */       throw new IOException("Class not found");
/*      */     }
/*  414 */     byte[] b = new byte[is.available()];
/*  415 */     int len = 0;
/*      */     while (true) {
/*  417 */       int n = is.read(b, len, b.length - len);
/*  418 */       if (n == -1) {
/*  419 */         if (len < b.length) {
/*  420 */           byte[] c = new byte[len];
/*  421 */           System.arraycopy(b, 0, c, 0, len);
/*  422 */           b = c;
/*      */         }
/*  424 */         return b;
/*      */       }
/*  426 */       len += n;
/*  427 */       if (len == b.length) {
/*  428 */         byte[] c = new byte[b.length + 1000];
/*  429 */         System.arraycopy(b, 0, c, 0, len);
/*  430 */         b = c;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void accept(ClassVisitor classVisitor, int flags)
/*      */   {
/*  450 */     accept(classVisitor, new Attribute[0], flags);
/*      */   }
/*      */ 
/*      */   public void accept(ClassVisitor classVisitor, Attribute[] attrs, int flags)
/*      */   {
/*  476 */     byte[] b = this.b;
/*  477 */     char[] c = new char[this.maxStringLength];
/*      */ 
/*  487 */     int anns = 0;
/*  488 */     int ianns = 0;
/*  489 */     Attribute cattrs = null;
/*      */ 
/*  492 */     int u = this.header;
/*  493 */     int access = readUnsignedShort(u);
/*  494 */     String name = readClass(u + 2, c);
/*  495 */     int v = this.items[readUnsignedShort(u + 4)];
/*  496 */     String superClassName = v == 0 ? null : readUTF8(v, c);
/*  497 */     String[] implementedItfs = new String[readUnsignedShort(u + 6)];
/*  498 */     int w = 0;
/*  499 */     u += 8;
/*  500 */     for (int i = 0; i < implementedItfs.length; i++) {
/*  501 */       implementedItfs[i] = readClass(u, c);
/*  502 */       u += 2;
/*      */     }
/*      */ 
/*  505 */     boolean skipCode = (flags & 0x1) != 0;
/*  506 */     boolean skipDebug = (flags & 0x2) != 0;
/*  507 */     boolean unzip = (flags & 0x8) != 0;
/*      */ 
/*  510 */     v = u;
/*  511 */     i = readUnsignedShort(v);
/*  512 */     v += 2;
/*  513 */     for (; i > 0; i--) {
/*  514 */       int j = readUnsignedShort(v + 6);
/*  515 */       v += 8;
/*  516 */       for (; j > 0; j--) {
/*  517 */         v += 6 + readInt(v + 2);
/*      */       }
/*      */     }
/*  520 */     i = readUnsignedShort(v);
/*  521 */     v += 2;
/*  522 */     for (; i > 0; i--) {
/*  523 */       int j = readUnsignedShort(v + 6);
/*  524 */       v += 8;
/*  525 */       for (; j > 0; j--) {
/*  526 */         v += 6 + readInt(v + 2);
/*      */       }
/*      */     }
/*      */ 
/*  530 */     String signature = null;
/*  531 */     String sourceFile = null;
/*  532 */     String sourceDebug = null;
/*  533 */     String enclosingOwner = null;
/*  534 */     String enclosingName = null;
/*  535 */     String enclosingDesc = null;
/*      */ 
/*  537 */     i = readUnsignedShort(v);
/*  538 */     v += 2;
/*  539 */     for (; i > 0; i--) {
/*  540 */       String attrName = readUTF8(v, c);
/*      */ 
/*  543 */       if ("SourceFile".equals(attrName)) {
/*  544 */         sourceFile = readUTF8(v + 6, c);
/*  545 */       } else if ("InnerClasses".equals(attrName)) {
/*  546 */         w = v + 6;
/*  547 */       } else if ("EnclosingMethod".equals(attrName)) {
/*  548 */         enclosingOwner = readClass(v + 6, c);
/*  549 */         int item = readUnsignedShort(v + 8);
/*  550 */         if (item != 0) {
/*  551 */           enclosingName = readUTF8(this.items[item], c);
/*  552 */           enclosingDesc = readUTF8(this.items[item] + 2, c);
/*      */         }
/*  554 */       } else if ("Signature".equals(attrName)) {
/*  555 */         signature = readUTF8(v + 6, c);
/*  556 */       } else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*  557 */         anns = v + 6;
/*  558 */       } else if ("Deprecated".equals(attrName)) {
/*  559 */         access |= 131072;
/*  560 */       } else if ("Synthetic".equals(attrName)) {
/*  561 */         access |= 4096;
/*  562 */       } else if ("SourceDebugExtension".equals(attrName)) {
/*  563 */         int len = readInt(v + 2);
/*  564 */         sourceDebug = readUTF(v + 6, len, new char[len]);
/*  565 */       } else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
/*  566 */         ianns = v + 6;
/*      */       } else {
/*  568 */         Attribute attr = readAttribute(attrs, attrName, v + 6, readInt(v + 2), c, -1, null);
/*      */ 
/*  575 */         if (attr != null) {
/*  576 */           attr.next = cattrs;
/*  577 */           cattrs = attr;
/*      */         }
/*      */       }
/*  580 */       v += 6 + readInt(v + 2);
/*      */     }
/*      */ 
/*  583 */     classVisitor.visit(readInt(4), access, name, signature, superClassName, implementedItfs);
/*      */ 
/*  591 */     if ((!skipDebug) && ((sourceFile != null) || (sourceDebug != null))) {
/*  592 */       classVisitor.visitSource(sourceFile, sourceDebug);
/*      */     }
/*      */ 
/*  596 */     if (enclosingOwner != null) {
/*  597 */       classVisitor.visitOuterClass(enclosingOwner, enclosingName, enclosingDesc);
/*      */     }
/*      */ 
/*  604 */     for (i = 1; i >= 0; i--) {
/*  605 */       v = i == 0 ? ianns : anns;
/*  606 */       if (v != 0) {
/*  607 */         int j = readUnsignedShort(v);
/*  608 */         v += 2;
/*  609 */         for (; j > 0; j--) {
/*  610 */           v = readAnnotationValues(v + 2, c, true, classVisitor.visitAnnotation(readUTF8(v, c), i != 0));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  620 */     while (cattrs != null) {
/*  621 */       Attribute attr = cattrs.next;
/*  622 */       cattrs.next = null;
/*  623 */       classVisitor.visitAttribute(cattrs);
/*  624 */       cattrs = attr;
/*      */     }
/*      */ 
/*  628 */     if (w != 0) {
/*  629 */       i = readUnsignedShort(w);
/*  630 */       w += 2;
/*  631 */       for (; i > 0; i--) {
/*  632 */         classVisitor.visitInnerClass(readUnsignedShort(w) == 0 ? null : readClass(w, c), readUnsignedShort(w + 2) == 0 ? null : readClass(w + 2, c), readUnsignedShort(w + 4) == 0 ? null : readUTF8(w + 4, c), readUnsignedShort(w + 6));
/*      */ 
/*  639 */         w += 8;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  644 */     i = readUnsignedShort(u);
/*  645 */     u += 2;
/*  646 */     for (; i > 0; i--) {
/*  647 */       access = readUnsignedShort(u);
/*  648 */       name = readUTF8(u + 2, c);
/*  649 */       String desc = readUTF8(u + 4, c);
/*      */ 
/*  652 */       int fieldValueItem = 0;
/*  653 */       signature = null;
/*  654 */       anns = 0;
/*  655 */       ianns = 0;
/*  656 */       cattrs = null;
/*      */ 
/*  658 */       int j = readUnsignedShort(u + 6);
/*  659 */       u += 8;
/*  660 */       for (; j > 0; j--) {
/*  661 */         String attrName = readUTF8(u, c);
/*      */ 
/*  664 */         if ("ConstantValue".equals(attrName)) {
/*  665 */           fieldValueItem = readUnsignedShort(u + 6);
/*  666 */         } else if ("Signature".equals(attrName)) {
/*  667 */           signature = readUTF8(u + 6, c);
/*  668 */         } else if ("Deprecated".equals(attrName)) {
/*  669 */           access |= 131072;
/*  670 */         } else if ("Synthetic".equals(attrName)) {
/*  671 */           access |= 4096;
/*  672 */         } else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*  673 */           anns = u + 6;
/*  674 */         } else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
/*  675 */           ianns = u + 6;
/*      */         } else {
/*  677 */           Attribute attr = readAttribute(attrs, attrName, u + 6, readInt(u + 2), c, -1, null);
/*      */ 
/*  684 */           if (attr != null) {
/*  685 */             attr.next = cattrs;
/*  686 */             cattrs = attr;
/*      */           }
/*      */         }
/*  689 */         u += 6 + readInt(u + 2);
/*      */       }
/*      */ 
/*  692 */       FieldVisitor fv = classVisitor.visitField(access, name, desc, signature, fieldValueItem == 0 ? null : readConst(fieldValueItem, c));
/*      */ 
/*  698 */       if (fv != null)
/*      */       {
/*  700 */         for (j = 1; j >= 0; j--) {
/*  701 */           v = j == 0 ? ianns : anns;
/*  702 */           if (v != 0) {
/*  703 */             int k = readUnsignedShort(v);
/*  704 */             v += 2;
/*  705 */             for (; k > 0; k--) {
/*  706 */               v = readAnnotationValues(v + 2, c, true, fv.visitAnnotation(readUTF8(v, c), j != 0));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  714 */         while (cattrs != null) {
/*  715 */           Attribute attr = cattrs.next;
/*  716 */           cattrs.next = null;
/*  717 */           fv.visitAttribute(cattrs);
/*  718 */           cattrs = attr;
/*      */         }
/*  720 */         fv.visitEnd();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  725 */     i = readUnsignedShort(u);
/*  726 */     u += 2;
/*  727 */     for (; i > 0; i--) {
/*  728 */       int u0 = u + 6;
/*  729 */       access = readUnsignedShort(u);
/*  730 */       name = readUTF8(u + 2, c);
/*  731 */       String desc = readUTF8(u + 4, c);
/*  732 */       signature = null;
/*  733 */       anns = 0;
/*  734 */       ianns = 0;
/*  735 */       int dann = 0;
/*  736 */       int mpanns = 0;
/*  737 */       int impanns = 0;
/*  738 */       cattrs = null;
/*  739 */       v = 0;
/*  740 */       w = 0;
/*      */ 
/*  743 */       int j = readUnsignedShort(u + 6);
/*  744 */       u += 8;
/*  745 */       for (; j > 0; j--) {
/*  746 */         String attrName = readUTF8(u, c);
/*  747 */         int attrSize = readInt(u + 2);
/*  748 */         u += 6;
/*      */ 
/*  751 */         if ("Code".equals(attrName)) {
/*  752 */           if (!skipCode)
/*  753 */             v = u;
/*      */         }
/*  755 */         else if ("Exceptions".equals(attrName)) {
/*  756 */           w = u;
/*  757 */         } else if ("Signature".equals(attrName)) {
/*  758 */           signature = readUTF8(u, c);
/*  759 */         } else if ("Deprecated".equals(attrName)) {
/*  760 */           access |= 131072;
/*  761 */         } else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*  762 */           anns = u;
/*  763 */         } else if ("AnnotationDefault".equals(attrName)) {
/*  764 */           dann = u;
/*  765 */         } else if ("Synthetic".equals(attrName)) {
/*  766 */           access |= 4096;
/*  767 */         } else if ("RuntimeInvisibleAnnotations".equals(attrName)) {
/*  768 */           ianns = u;
/*  769 */         } else if ("RuntimeVisibleParameterAnnotations".equals(attrName))
/*      */         {
/*  771 */           mpanns = u;
/*  772 */         } else if ("RuntimeInvisibleParameterAnnotations".equals(attrName))
/*      */         {
/*  774 */           impanns = u;
/*      */         } else {
/*  776 */           Attribute attr = readAttribute(attrs, attrName, u, attrSize, c, -1, null);
/*      */ 
/*  783 */           if (attr != null) {
/*  784 */             attr.next = cattrs;
/*  785 */             cattrs = attr;
/*      */           }
/*      */         }
/*  788 */         u += attrSize;
/*      */       }
/*      */       String[] exceptions;
/*      */       String[] exceptions;
/*  792 */       if (w == 0) {
/*  793 */         exceptions = null;
/*      */       } else {
/*  795 */         exceptions = new String[readUnsignedShort(w)];
/*  796 */         w += 2;
/*  797 */         for (j = 0; j < exceptions.length; j++) {
/*  798 */           exceptions[j] = readClass(w, c);
/*  799 */           w += 2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  804 */       MethodVisitor mv = classVisitor.visitMethod(access, name, desc, signature, exceptions);
/*      */ 
/*  810 */       if (mv != null)
/*      */       {
/*  822 */         if ((mv instanceof MethodWriter)) {
/*  823 */           MethodWriter mw = (MethodWriter)mv;
/*  824 */           if ((mw.cw.cr == this) && 
/*  825 */             (signature == mw.signature)) {
/*  826 */             boolean sameExceptions = false;
/*  827 */             if (exceptions == null) {
/*  828 */               sameExceptions = mw.exceptionCount == 0;
/*      */             }
/*  830 */             else if (exceptions.length == mw.exceptionCount) {
/*  831 */               sameExceptions = true;
/*  832 */               for (j = exceptions.length - 1; j >= 0; j--)
/*      */               {
/*  834 */                 w -= 2;
/*  835 */                 if (mw.exceptions[j] != readUnsignedShort(w))
/*      */                 {
/*  837 */                   sameExceptions = false;
/*  838 */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  843 */             if (sameExceptions)
/*      */             {
/*  850 */               mw.classReaderOffset = u0;
/*  851 */               mw.classReaderLength = (u - u0);
/*  852 */               continue;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  858 */         if (dann != 0) {
/*  859 */           AnnotationVisitor dv = mv.visitAnnotationDefault();
/*  860 */           readAnnotationValue(dann, c, null, dv);
/*  861 */           if (dv != null) {
/*  862 */             dv.visitEnd();
/*      */           }
/*      */         }
/*      */ 
/*  866 */         for (j = 1; j >= 0; j--) {
/*  867 */           w = j == 0 ? ianns : anns;
/*  868 */           if (w != 0) {
/*  869 */             int k = readUnsignedShort(w);
/*  870 */             w += 2;
/*  871 */             for (; k > 0; k--) {
/*  872 */               w = readAnnotationValues(w + 2, c, true, mv.visitAnnotation(readUTF8(w, c), j != 0));
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  880 */         if (mpanns != 0) {
/*  881 */           readParameterAnnotations(mpanns, desc, c, true, mv);
/*      */         }
/*  883 */         if (impanns != 0) {
/*  884 */           readParameterAnnotations(impanns, desc, c, false, mv);
/*      */         }
/*  886 */         while (cattrs != null) {
/*  887 */           Attribute attr = cattrs.next;
/*  888 */           cattrs.next = null;
/*  889 */           mv.visitAttribute(cattrs);
/*  890 */           cattrs = attr;
/*      */         }
/*      */       }
/*      */       else {
/*  894 */         if ((mv != null) && (v != 0)) {
/*  895 */           int maxStack = readUnsignedShort(v);
/*  896 */           int maxLocals = readUnsignedShort(v + 2);
/*  897 */           int codeLength = readInt(v + 4);
/*  898 */           v += 8;
/*      */ 
/*  900 */           int codeStart = v;
/*  901 */           int codeEnd = v + codeLength;
/*      */ 
/*  903 */           mv.visitCode();
/*      */ 
/*  907 */           Label[] labels = new Label[codeLength + 2];
/*  908 */           readLabel(codeLength + 1, labels);
/*  909 */           while (v < codeEnd) {
/*  910 */             w = v - codeStart;
/*  911 */             int opcode = b[v] & 0xFF;
/*  912 */             switch (ClassWriter.TYPE[opcode]) {
/*      */             case 0:
/*      */             case 4:
/*  915 */               v++;
/*  916 */               break;
/*      */             case 8:
/*  918 */               readLabel(w + readShort(v + 1), labels);
/*  919 */               v += 3;
/*  920 */               break;
/*      */             case 9:
/*  922 */               readLabel(w + readInt(v + 1), labels);
/*  923 */               v += 5;
/*  924 */               break;
/*      */             case 16:
/*  926 */               opcode = b[(v + 1)] & 0xFF;
/*  927 */               if (opcode == 132)
/*  928 */                 v += 6;
/*      */               else {
/*  930 */                 v += 4;
/*      */               }
/*  932 */               break;
/*      */             case 13:
/*  935 */               v = v + 4 - (w & 0x3);
/*      */ 
/*  937 */               readLabel(w + readInt(v), labels);
/*  938 */               j = readInt(v + 8) - readInt(v + 4) + 1;
/*  939 */               v += 12;
/*      */             case 14:
/*      */             case 1:
/*      */             case 3:
/*      */             case 10:
/*      */             case 2:
/*      */             case 5:
/*      */             case 6:
/*      */             case 11:
/*      */             case 12:
/*      */             case 7:
/*      */             case 15:
/*      */             default:
/*  940 */               while (j > 0) {
/*  941 */                 readLabel(w + readInt(v), labels);
/*  942 */                 v += 4;
/*      */ 
/*  940 */                 j--; continue;
/*      */ 
/*  947 */                 v = v + 4 - (w & 0x3);
/*      */ 
/*  949 */                 readLabel(w + readInt(v), labels);
/*  950 */                 j = readInt(v + 4);
/*  951 */                 v += 8;
/*  952 */                 while (j > 0) {
/*  953 */                   readLabel(w + readInt(v + 4), labels);
/*  954 */                   v += 8;
/*      */ 
/*  952 */                   j--; continue;
/*      */ 
/*  960 */                   v += 2;
/*  961 */                   break;
/*      */ 
/*  967 */                   v += 3;
/*  968 */                   break;
/*      */ 
/*  970 */                   v += 5;
/*  971 */                   break;
/*      */ 
/*  974 */                   v += 4;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*  979 */           j = readUnsignedShort(v);
/*  980 */           v += 2;
/*  981 */           for (; j > 0; j--) {
/*  982 */             Label start = readLabel(readUnsignedShort(v), labels);
/*  983 */             Label end = readLabel(readUnsignedShort(v + 2), labels);
/*  984 */             Label handler = readLabel(readUnsignedShort(v + 4), labels);
/*  985 */             int type = readUnsignedShort(v + 6);
/*  986 */             if (type == 0)
/*  987 */               mv.visitTryCatchBlock(start, end, handler, null);
/*      */             else {
/*  989 */               mv.visitTryCatchBlock(start, end, handler, readUTF8(this.items[type], c));
/*      */             }
/*      */ 
/*  994 */             v += 8;
/*      */           }
/*      */ 
/*  998 */           int varTable = 0;
/*  999 */           int varTypeTable = 0;
/* 1000 */           int stackMap = 0;
/* 1001 */           int frameCount = 0;
/* 1002 */           int frameMode = 0;
/* 1003 */           int frameOffset = 0;
/* 1004 */           int frameLocalCount = 0;
/* 1005 */           int frameLocalDiff = 0;
/* 1006 */           int frameStackCount = 0;
/* 1007 */           Object[] frameLocal = null;
/* 1008 */           Object[] frameStack = null;
/* 1009 */           boolean zip = true;
/* 1010 */           cattrs = null;
/* 1011 */           j = readUnsignedShort(v);
/* 1012 */           v += 2;
/* 1013 */           for (; j > 0; j--) {
/* 1014 */             String attrName = readUTF8(v, c);
/* 1015 */             if ("LocalVariableTable".equals(attrName)) {
/* 1016 */               if (!skipDebug) {
/* 1017 */                 varTable = v + 6;
/* 1018 */                 int k = readUnsignedShort(v + 6);
/* 1019 */                 w = v + 8;
/* 1020 */                 for (; k > 0; k--) {
/* 1021 */                   int label = readUnsignedShort(w);
/* 1022 */                   if (labels[label] == null) {
/* 1023 */                     readLabel(label, labels).status |= 1;
/*      */                   }
/* 1025 */                   label += readUnsignedShort(w + 2);
/* 1026 */                   if (labels[label] == null) {
/* 1027 */                     readLabel(label, labels).status |= 1;
/*      */                   }
/* 1029 */                   w += 10;
/*      */                 }
/*      */               }
/* 1032 */             } else if ("LocalVariableTypeTable".equals(attrName))
/* 1033 */               varTypeTable = v + 6;
/* 1034 */             else if ("LineNumberTable".equals(attrName)) {
/* 1035 */               if (!skipDebug) {
/* 1036 */                 int k = readUnsignedShort(v + 6);
/* 1037 */                 w = v + 8;
/* 1038 */                 for (; k > 0; k--) {
/* 1039 */                   int label = readUnsignedShort(w);
/* 1040 */                   if (labels[label] == null) {
/* 1041 */                     readLabel(label, labels).status |= 1;
/*      */                   }
/* 1043 */                   labels[label].line = readUnsignedShort(w + 2);
/* 1044 */                   w += 4;
/*      */                 }
/*      */               }
/* 1047 */             } else if ("StackMapTable".equals(attrName)) {
/* 1048 */               if ((flags & 0x4) == 0) {
/* 1049 */                 stackMap = v + 8;
/* 1050 */                 frameCount = readUnsignedShort(v + 6);
/*      */               }
/*      */ 
/*      */             }
/* 1068 */             else if ("StackMap".equals(attrName)) {
/* 1069 */               if ((flags & 0x4) == 0) {
/* 1070 */                 stackMap = v + 8;
/* 1071 */                 frameCount = readUnsignedShort(v + 6);
/* 1072 */                 zip = false;
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1080 */               for (int k = 0; k < attrs.length; k++) {
/* 1081 */                 if (attrs[k].type.equals(attrName)) {
/* 1082 */                   Attribute attr = attrs[k].read(this, v + 6, readInt(v + 2), c, codeStart - 8, labels);
/*      */ 
/* 1088 */                   if (attr != null) {
/* 1089 */                     attr.next = cattrs;
/* 1090 */                     cattrs = attr;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/* 1095 */             v += 6 + readInt(v + 2);
/*      */           }
/*      */ 
/* 1099 */           if (stackMap != 0)
/*      */           {
/* 1102 */             frameLocal = new Object[maxLocals];
/* 1103 */             frameStack = new Object[maxStack];
/* 1104 */             if (unzip) {
/* 1105 */               int local = 0;
/* 1106 */               if ((access & 0x8) == 0) {
/* 1107 */                 if ("<init>".equals(name))
/* 1108 */                   frameLocal[(local++)] = Opcodes.UNINITIALIZED_THIS;
/*      */                 else {
/* 1110 */                   frameLocal[(local++)] = readClass(this.header + 2, c);
/*      */                 }
/*      */               }
/* 1113 */               j = 1;
/*      */               while (true) {
/* 1115 */                 int k = j;
/* 1116 */                 switch (desc.charAt(j++)) {
/*      */                 case 'B':
/*      */                 case 'C':
/*      */                 case 'I':
/*      */                 case 'S':
/*      */                 case 'Z':
/* 1122 */                   frameLocal[(local++)] = Opcodes.INTEGER;
/* 1123 */                   break;
/*      */                 case 'F':
/* 1125 */                   frameLocal[(local++)] = Opcodes.FLOAT;
/* 1126 */                   break;
/*      */                 case 'J':
/* 1128 */                   frameLocal[(local++)] = Opcodes.LONG;
/* 1129 */                   break;
/*      */                 case 'D':
/* 1131 */                   frameLocal[(local++)] = Opcodes.DOUBLE;
/* 1132 */                   break;
/*      */                 case '[':
/* 1134 */                   while (desc.charAt(j) == '[') {
/* 1135 */                     j++;
/*      */                   }
/* 1137 */                   if (desc.charAt(j) == 'L') {
/* 1138 */                     j++;
/* 1139 */                     while (desc.charAt(j) != ';') {
/* 1140 */                       j++;
/*      */                     }
/*      */                   }
/* 1143 */                   frameLocal[(local++)] = desc.substring(k, ++j);
/* 1144 */                   break;
/*      */                 case 'L':
/* 1146 */                   while (desc.charAt(j) != ';') {
/* 1147 */                     j++;
/*      */                   }
/* 1149 */                   frameLocal[(local++)] = desc.substring(k + 1, j++);
/*      */                 case 'E':
/*      */                 case 'G':
/*      */                 case 'H':
/*      */                 case 'K':
/*      */                 case 'M':
/*      */                 case 'N':
/*      */                 case 'O':
/*      */                 case 'P':
/*      */                 case 'Q':
/*      */                 case 'R':
/*      */                 case 'T':
/*      */                 case 'U':
/*      */                 case 'V':
/*      */                 case 'W':
/*      */                 case 'X':
/* 1156 */                 case 'Y': }  } frameLocalCount = local;
/*      */             }
/*      */ 
/* 1164 */             frameOffset = -1;
/*      */           }
/* 1166 */           v = codeStart;
/*      */ 
/* 1168 */           while (v < codeEnd) {
/* 1169 */             w = v - codeStart;
/*      */ 
/* 1171 */             Label l = labels[w];
/* 1172 */             if (l != null) {
/* 1173 */               mv.visitLabel(l);
/* 1174 */               if ((!skipDebug) && (l.line > 0)) {
/* 1175 */                 mv.visitLineNumber(l.line, l);
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/* 1180 */             while ((frameLocal != null) && ((frameOffset == w) || (frameOffset == -1)))
/*      */             {
/* 1185 */               if ((!zip) || (unzip)) {
/* 1186 */                 mv.visitFrame(-1, frameLocalCount, frameLocal, frameStackCount, frameStack);
/*      */               }
/* 1191 */               else if (frameOffset != -1) {
/* 1192 */                 mv.visitFrame(frameMode, frameLocalDiff, frameLocal, frameStackCount, frameStack);
/*      */               }
/*      */ 
/* 1199 */               if (frameCount > 0)
/*      */               {
/*      */                 int tag;
/*      */                 int tag;
/* 1201 */                 if (zip) {
/* 1202 */                   tag = b[(stackMap++)] & 0xFF;
/*      */                 } else {
/* 1204 */                   tag = 255;
/* 1205 */                   frameOffset = -1;
/*      */                 }
/* 1207 */                 frameLocalDiff = 0;
/*      */                 int delta;
/* 1208 */                 if (tag < 64)
/*      */                 {
/* 1210 */                   int delta = tag;
/* 1211 */                   frameMode = 3;
/* 1212 */                   frameStackCount = 0;
/* 1213 */                 } else if (tag < 128) {
/* 1214 */                   int delta = tag - 64;
/*      */ 
/* 1216 */                   stackMap = readFrameType(frameStack, 0, stackMap, c, labels);
/*      */ 
/* 1221 */                   frameMode = 4;
/* 1222 */                   frameStackCount = 1;
/*      */                 } else {
/* 1224 */                   delta = readUnsignedShort(stackMap);
/* 1225 */                   stackMap += 2;
/* 1226 */                   if (tag == 247)
/*      */                   {
/* 1228 */                     stackMap = readFrameType(frameStack, 0, stackMap, c, labels);
/*      */ 
/* 1233 */                     frameMode = 4;
/* 1234 */                     frameStackCount = 1;
/* 1235 */                   } else if ((tag >= 248) && (tag < 251))
/*      */                   {
/* 1238 */                     frameMode = 2;
/* 1239 */                     frameLocalDiff = 251 - tag;
/*      */ 
/* 1241 */                     frameLocalCount -= frameLocalDiff;
/* 1242 */                     frameStackCount = 0;
/* 1243 */                   } else if (tag == 251)
/*      */                   {
/* 1245 */                     frameMode = 3;
/* 1246 */                     frameStackCount = 0;
/* 1247 */                   } else if (tag < 255) {
/* 1248 */                     j = unzip ? frameLocalCount : 0;
/* 1249 */                     for (int k = tag - 251; 
/* 1250 */                       k > 0; k--)
/*      */                     {
/* 1252 */                       stackMap = readFrameType(frameLocal, j++, stackMap, c, labels);
/*      */                     }
/*      */ 
/* 1258 */                     frameMode = 1;
/* 1259 */                     frameLocalDiff = tag - 251;
/*      */ 
/* 1261 */                     frameLocalCount += frameLocalDiff;
/* 1262 */                     frameStackCount = 0;
/*      */                   } else {
/* 1264 */                     frameMode = 0;
/* 1265 */                     int n = frameLocalDiff = frameLocalCount = readUnsignedShort(stackMap);
/* 1266 */                     stackMap += 2;
/* 1267 */                     for (j = 0; n > 0; n--) {
/* 1268 */                       stackMap = readFrameType(frameLocal, j++, stackMap, c, labels);
/*      */                     }
/*      */ 
/* 1274 */                     n = frameStackCount = readUnsignedShort(stackMap);
/* 1275 */                     stackMap += 2;
/* 1276 */                     for (j = 0; n > 0; n--) {
/* 1277 */                       stackMap = readFrameType(frameStack, j++, stackMap, c, labels);
/*      */                     }
/*      */ 
/*      */                   }
/*      */ 
/*      */                 }
/*      */ 
/* 1285 */                 frameOffset += delta + 1;
/* 1286 */                 readLabel(frameOffset, labels);
/*      */ 
/* 1288 */                 frameCount--;
/*      */               } else {
/* 1290 */                 frameLocal = null;
/*      */               }
/*      */             }
/*      */ 
/* 1294 */             int opcode = b[v] & 0xFF;
/*      */             int label;
/* 1295 */             switch (ClassWriter.TYPE[opcode]) {
/*      */             case 0:
/* 1297 */               mv.visitInsn(opcode);
/* 1298 */               v++;
/* 1299 */               break;
/*      */             case 4:
/* 1301 */               if (opcode > 54) {
/* 1302 */                 opcode -= 59;
/* 1303 */                 mv.visitVarInsn(54 + (opcode >> 2), opcode & 0x3);
/*      */               }
/*      */               else {
/* 1306 */                 opcode -= 26;
/* 1307 */                 mv.visitVarInsn(21 + (opcode >> 2), opcode & 0x3);
/*      */               }
/*      */ 
/* 1310 */               v++;
/* 1311 */               break;
/*      */             case 8:
/* 1313 */               mv.visitJumpInsn(opcode, labels[(w + readShort(v + 1))]);
/*      */ 
/* 1315 */               v += 3;
/* 1316 */               break;
/*      */             case 9:
/* 1318 */               mv.visitJumpInsn(opcode - 33, labels[(w + readInt(v + 1))]);
/*      */ 
/* 1320 */               v += 5;
/* 1321 */               break;
/*      */             case 16:
/* 1323 */               opcode = b[(v + 1)] & 0xFF;
/* 1324 */               if (opcode == 132) {
/* 1325 */                 mv.visitIincInsn(readUnsignedShort(v + 2), readShort(v + 4));
/*      */ 
/* 1327 */                 v += 6;
/*      */               } else {
/* 1329 */                 mv.visitVarInsn(opcode, readUnsignedShort(v + 2));
/*      */ 
/* 1331 */                 v += 4;
/*      */               }
/* 1333 */               break;
/*      */             case 13:
/* 1336 */               v = v + 4 - (w & 0x3);
/*      */ 
/* 1338 */               label = w + readInt(v);
/* 1339 */               int min = readInt(v + 4);
/* 1340 */               int max = readInt(v + 8);
/* 1341 */               v += 12;
/* 1342 */               Label[] table = new Label[max - min + 1];
/* 1343 */               for (j = 0; j < table.length; j++) {
/* 1344 */                 table[j] = labels[(w + readInt(v))];
/* 1345 */                 v += 4;
/*      */               }
/* 1347 */               mv.visitTableSwitchInsn(min, max, labels[label], table);
/*      */ 
/* 1351 */               break;
/*      */             case 14:
/* 1354 */               v = v + 4 - (w & 0x3);
/*      */ 
/* 1356 */               label = w + readInt(v);
/* 1357 */               j = readInt(v + 4);
/* 1358 */               v += 8;
/* 1359 */               int[] keys = new int[j];
/* 1360 */               Label[] values = new Label[j];
/* 1361 */               for (j = 0; j < keys.length; j++) {
/* 1362 */                 keys[j] = readInt(v);
/* 1363 */                 values[j] = labels[(w + readInt(v + 4))];
/* 1364 */                 v += 8;
/*      */               }
/* 1366 */               mv.visitLookupSwitchInsn(labels[label], keys, values);
/*      */ 
/* 1369 */               break;
/*      */             case 3:
/* 1371 */               mv.visitVarInsn(opcode, b[(v + 1)] & 0xFF);
/* 1372 */               v += 2;
/* 1373 */               break;
/*      */             case 1:
/* 1375 */               mv.visitIntInsn(opcode, b[(v + 1)]);
/* 1376 */               v += 2;
/* 1377 */               break;
/*      */             case 2:
/* 1379 */               mv.visitIntInsn(opcode, readShort(v + 1));
/* 1380 */               v += 3;
/* 1381 */               break;
/*      */             case 10:
/* 1383 */               mv.visitLdcInsn(readConst(b[(v + 1)] & 0xFF, c));
/* 1384 */               v += 2;
/* 1385 */               break;
/*      */             case 11:
/* 1387 */               mv.visitLdcInsn(readConst(readUnsignedShort(v + 1), c));
/*      */ 
/* 1389 */               v += 3;
/* 1390 */               break;
/*      */             case 6:
/*      */             case 7:
/* 1393 */               int cpIndex = this.items[readUnsignedShort(v + 1)];
/* 1394 */               String iowner = readClass(cpIndex, c);
/* 1395 */               cpIndex = this.items[readUnsignedShort(cpIndex + 2)];
/* 1396 */               String iname = readUTF8(cpIndex, c);
/* 1397 */               String idesc = readUTF8(cpIndex + 2, c);
/* 1398 */               if (opcode < 182)
/* 1399 */                 mv.visitFieldInsn(opcode, iowner, iname, idesc);
/*      */               else {
/* 1401 */                 mv.visitMethodInsn(opcode, iowner, iname, idesc);
/*      */               }
/* 1403 */               if (opcode == 185)
/* 1404 */                 v += 5;
/*      */               else {
/* 1406 */                 v += 3;
/*      */               }
/* 1408 */               break;
/*      */             case 5:
/* 1410 */               mv.visitTypeInsn(opcode, readClass(v + 1, c));
/* 1411 */               v += 3;
/* 1412 */               break;
/*      */             case 12:
/* 1414 */               mv.visitIincInsn(b[(v + 1)] & 0xFF, b[(v + 2)]);
/* 1415 */               v += 3;
/* 1416 */               break;
/*      */             case 15:
/*      */             default:
/* 1419 */               mv.visitMultiANewArrayInsn(readClass(v + 1, c), b[(v + 3)] & 0xFF);
/*      */ 
/* 1421 */               v += 4;
/*      */             }
/*      */           }
/*      */ 
/* 1425 */           Label l = labels[(codeEnd - codeStart)];
/* 1426 */           if (l != null) {
/* 1427 */             mv.visitLabel(l);
/*      */           }
/*      */ 
/* 1430 */           if ((!skipDebug) && (varTable != 0)) {
/* 1431 */             int[] typeTable = null;
/* 1432 */             if (varTypeTable != 0) {
/* 1433 */               int k = readUnsignedShort(varTypeTable) * 3;
/* 1434 */               w = varTypeTable + 2;
/* 1435 */               typeTable = new int[k];
/* 1436 */               while (k > 0) {
/* 1437 */                 typeTable[(--k)] = (w + 6);
/* 1438 */                 typeTable[(--k)] = readUnsignedShort(w + 8);
/* 1439 */                 typeTable[(--k)] = readUnsignedShort(w);
/* 1440 */                 w += 10;
/*      */               }
/*      */             }
/* 1443 */             int k = readUnsignedShort(varTable);
/* 1444 */             w = varTable + 2;
/* 1445 */             for (; k > 0; k--) {
/* 1446 */               int start = readUnsignedShort(w);
/* 1447 */               int length = readUnsignedShort(w + 2);
/* 1448 */               int index = readUnsignedShort(w + 8);
/* 1449 */               String vsignature = null;
/* 1450 */               if (typeTable != null) {
/* 1451 */                 for (int a = 0; a < typeTable.length; a += 3) {
/* 1452 */                   if ((typeTable[a] == start) && (typeTable[(a + 1)] == index))
/*      */                   {
/* 1455 */                     vsignature = readUTF8(typeTable[(a + 2)], c);
/* 1456 */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/* 1460 */               mv.visitLocalVariable(readUTF8(w + 4, c), readUTF8(w + 6, c), vsignature, labels[start], labels[(start + length)], index);
/*      */ 
/* 1466 */               w += 10;
/*      */             }
/*      */           }
/*      */ 
/* 1470 */           while (cattrs != null) {
/* 1471 */             Attribute attr = cattrs.next;
/* 1472 */             cattrs.next = null;
/* 1473 */             mv.visitAttribute(cattrs);
/* 1474 */             cattrs = attr;
/*      */           }
/*      */ 
/* 1477 */           mv.visitMaxs(maxStack, maxLocals);
/*      */         }
/*      */ 
/* 1480 */         if (mv != null) {
/* 1481 */           mv.visitEnd();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1486 */     classVisitor.visitEnd();
/*      */   }
/*      */ 
/*      */   private void readParameterAnnotations(int v, String desc, char[] buf, boolean visible, MethodVisitor mv)
/*      */   {
/* 1509 */     int n = this.b[(v++)] & 0xFF;
/*      */ 
/* 1516 */     int synthetics = Type.getArgumentTypes(desc).length - n;
/*      */ 
/* 1518 */     for (int i = 0; i < synthetics; i++)
/*      */     {
/* 1520 */       AnnotationVisitor av = mv.visitParameterAnnotation(i, "Ljava/lang/Synthetic;", false);
/* 1521 */       if (av != null) {
/* 1522 */         av.visitEnd();
/*      */       }
/*      */     }
/* 1525 */     for (; i < n + synthetics; i++) {
/* 1526 */       int j = readUnsignedShort(v);
/* 1527 */       v += 2;
/* 1528 */       for (; j > 0; j--) {
/* 1529 */         AnnotationVisitor av = mv.visitParameterAnnotation(i, readUTF8(v, buf), visible);
/* 1530 */         v = readAnnotationValues(v + 2, buf, true, av);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int readAnnotationValues(int v, char[] buf, boolean named, AnnotationVisitor av)
/*      */   {
/* 1553 */     int i = readUnsignedShort(v);
/* 1554 */     v += 2;
/* 1555 */     if (named) {
/* 1556 */       for (; i > 0; i--) {
/* 1557 */         v = readAnnotationValue(v + 2, buf, readUTF8(v, buf), av);
/*      */       }
/*      */     }
/* 1560 */     for (; i > 0; i--) {
/* 1561 */       v = readAnnotationValue(v, buf, null, av);
/*      */     }
/*      */ 
/* 1564 */     if (av != null) {
/* 1565 */       av.visitEnd();
/*      */     }
/* 1567 */     return v;
/*      */   }
/*      */ 
/*      */   private int readAnnotationValue(int v, char[] buf, String name, AnnotationVisitor av)
/*      */   {
/* 1589 */     if (av == null) {
/* 1590 */       switch (this.b[v] & 0xFF) {
/*      */       case 101:
/* 1592 */         return v + 5;
/*      */       case 64:
/* 1594 */         return readAnnotationValues(v + 3, buf, true, null);
/*      */       case 91:
/* 1596 */         return readAnnotationValues(v + 1, buf, false, null);
/*      */       }
/* 1598 */       return v + 3;
/*      */     }
/*      */ 
/* 1601 */     switch (this.b[(v++)] & 0xFF) {
/*      */     case 68:
/*      */     case 70:
/*      */     case 73:
/*      */     case 74:
/* 1606 */       av.visit(name, readConst(readUnsignedShort(v), buf));
/* 1607 */       v += 2;
/* 1608 */       break;
/*      */     case 66:
/* 1610 */       av.visit(name, new Byte((byte)readInt(this.items[readUnsignedShort(v)])));
/*      */ 
/* 1612 */       v += 2;
/* 1613 */       break;
/*      */     case 90:
/* 1615 */       av.visit(name, readInt(this.items[readUnsignedShort(v)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
/*      */ 
/* 1618 */       v += 2;
/* 1619 */       break;
/*      */     case 83:
/* 1621 */       av.visit(name, new Short((short)readInt(this.items[readUnsignedShort(v)])));
/*      */ 
/* 1623 */       v += 2;
/* 1624 */       break;
/*      */     case 67:
/* 1626 */       av.visit(name, new Character((char)readInt(this.items[readUnsignedShort(v)])));
/*      */ 
/* 1628 */       v += 2;
/* 1629 */       break;
/*      */     case 115:
/* 1631 */       av.visit(name, readUTF8(v, buf));
/* 1632 */       v += 2;
/* 1633 */       break;
/*      */     case 101:
/* 1635 */       av.visitEnum(name, readUTF8(v, buf), readUTF8(v + 2, buf));
/* 1636 */       v += 4;
/* 1637 */       break;
/*      */     case 99:
/* 1639 */       av.visit(name, Type.getType(readUTF8(v, buf)));
/* 1640 */       v += 2;
/* 1641 */       break;
/*      */     case 64:
/* 1643 */       v = readAnnotationValues(v + 2, buf, true, av.visitAnnotation(name, readUTF8(v, buf)));
/*      */ 
/* 1647 */       break;
/*      */     case 91:
/* 1649 */       int size = readUnsignedShort(v);
/* 1650 */       v += 2;
/* 1651 */       if (size == 0)
/* 1652 */         return readAnnotationValues(v - 2, buf, false, av.visitArray(name));
/*      */       int i;
/* 1657 */       switch (this.b[(v++)] & 0xFF) {
/*      */       case 66:
/* 1659 */         byte[] bv = new byte[size];
/* 1660 */         for (i = 0; i < size; i++) {
/* 1661 */           bv[i] = ((byte)readInt(this.items[readUnsignedShort(v)]));
/* 1662 */           v += 3;
/*      */         }
/* 1664 */         av.visit(name, bv);
/* 1665 */         v--;
/* 1666 */         break;
/*      */       case 90:
/* 1668 */         boolean[] zv = new boolean[size];
/* 1669 */         for (i = 0; i < size; i++) {
/* 1670 */           zv[i] = (readInt(this.items[readUnsignedShort(v)]) != 0 ? 1 : false);
/* 1671 */           v += 3;
/*      */         }
/* 1673 */         av.visit(name, zv);
/* 1674 */         v--;
/* 1675 */         break;
/*      */       case 83:
/* 1677 */         short[] sv = new short[size];
/* 1678 */         for (i = 0; i < size; i++) {
/* 1679 */           sv[i] = ((short)readInt(this.items[readUnsignedShort(v)]));
/* 1680 */           v += 3;
/*      */         }
/* 1682 */         av.visit(name, sv);
/* 1683 */         v--;
/* 1684 */         break;
/*      */       case 67:
/* 1686 */         char[] cv = new char[size];
/* 1687 */         for (i = 0; i < size; i++) {
/* 1688 */           cv[i] = ((char)readInt(this.items[readUnsignedShort(v)]));
/* 1689 */           v += 3;
/*      */         }
/* 1691 */         av.visit(name, cv);
/* 1692 */         v--;
/* 1693 */         break;
/*      */       case 73:
/* 1695 */         int[] iv = new int[size];
/* 1696 */         for (i = 0; i < size; i++) {
/* 1697 */           iv[i] = readInt(this.items[readUnsignedShort(v)]);
/* 1698 */           v += 3;
/*      */         }
/* 1700 */         av.visit(name, iv);
/* 1701 */         v--;
/* 1702 */         break;
/*      */       case 74:
/* 1704 */         long[] lv = new long[size];
/* 1705 */         for (i = 0; i < size; i++) {
/* 1706 */           lv[i] = readLong(this.items[readUnsignedShort(v)]);
/* 1707 */           v += 3;
/*      */         }
/* 1709 */         av.visit(name, lv);
/* 1710 */         v--;
/* 1711 */         break;
/*      */       case 70:
/* 1713 */         float[] fv = new float[size];
/* 1714 */         for (i = 0; i < size; i++) {
/* 1715 */           fv[i] = Float.intBitsToFloat(readInt(this.items[readUnsignedShort(v)]));
/* 1716 */           v += 3;
/*      */         }
/* 1718 */         av.visit(name, fv);
/* 1719 */         v--;
/* 1720 */         break;
/*      */       case 68:
/* 1722 */         double[] dv = new double[size];
/* 1723 */         for (i = 0; i < size; i++) {
/* 1724 */           dv[i] = Double.longBitsToDouble(readLong(this.items[readUnsignedShort(v)]));
/* 1725 */           v += 3;
/*      */         }
/* 1727 */         av.visit(name, dv);
/* 1728 */         v--;
/* 1729 */         break;
/*      */       case 69:
/*      */       case 71:
/*      */       case 72:
/*      */       case 75:
/*      */       case 76:
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*      */       case 81:
/*      */       case 82:
/*      */       case 84:
/*      */       case 85:
/*      */       case 86:
/*      */       case 87:
/*      */       case 88:
/*      */       case 89:
/*      */       default:
/* 1731 */         v = readAnnotationValues(v - 3, buf, false, av.visitArray(name)); } break;
/*      */     case 65:
/*      */     case 69:
/*      */     case 71:
/*      */     case 72:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 89:
/*      */     case 92:
/*      */     case 93:
/*      */     case 94:
/*      */     case 95:
/*      */     case 96:
/*      */     case 97:
/*      */     case 98:
/*      */     case 100:
/*      */     case 102:
/*      */     case 103:
/*      */     case 104:
/*      */     case 105:
/*      */     case 106:
/*      */     case 107:
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 111:
/*      */     case 112:
/*      */     case 113:
/* 1737 */     case 114: } return v;
/*      */   }
/*      */ 
/*      */   private int readFrameType(Object[] frame, int index, int v, char[] buf, Label[] labels)
/*      */   {
/* 1747 */     int type = this.b[(v++)] & 0xFF;
/* 1748 */     switch (type) {
/*      */     case 0:
/* 1750 */       frame[index] = Opcodes.TOP;
/* 1751 */       break;
/*      */     case 1:
/* 1753 */       frame[index] = Opcodes.INTEGER;
/* 1754 */       break;
/*      */     case 2:
/* 1756 */       frame[index] = Opcodes.FLOAT;
/* 1757 */       break;
/*      */     case 3:
/* 1759 */       frame[index] = Opcodes.DOUBLE;
/* 1760 */       break;
/*      */     case 4:
/* 1762 */       frame[index] = Opcodes.LONG;
/* 1763 */       break;
/*      */     case 5:
/* 1765 */       frame[index] = Opcodes.NULL;
/* 1766 */       break;
/*      */     case 6:
/* 1768 */       frame[index] = Opcodes.UNINITIALIZED_THIS;
/* 1769 */       break;
/*      */     case 7:
/* 1771 */       frame[index] = readClass(v, buf);
/* 1772 */       v += 2;
/* 1773 */       break;
/*      */     default:
/* 1775 */       frame[index] = readLabel(readUnsignedShort(v), labels);
/* 1776 */       v += 2;
/*      */     }
/* 1778 */     return v;
/*      */   }
/*      */ 
/*      */   protected Label readLabel(int offset, Label[] labels)
/*      */   {
/* 1793 */     if (labels[offset] == null) {
/* 1794 */       labels[offset] = new Label();
/*      */     }
/* 1796 */     return labels[offset];
/*      */   }
/*      */ 
/*      */   private Attribute readAttribute(Attribute[] attrs, String type, int off, int len, char[] buf, int codeOff, Label[] labels)
/*      */   {
/* 1833 */     for (int i = 0; i < attrs.length; i++) {
/* 1834 */       if (attrs[i].type.equals(type)) {
/* 1835 */         return attrs[i].read(this, off, len, buf, codeOff, labels);
/*      */       }
/*      */     }
/* 1838 */     return new Attribute(type).read(this, off, len, null, -1, null);
/*      */   }
/*      */ 
/*      */   public int getItem(int item)
/*      */   {
/* 1855 */     return this.items[item];
/*      */   }
/*      */ 
/*      */   public int readByte(int index)
/*      */   {
/* 1867 */     return this.b[index] & 0xFF;
/*      */   }
/*      */ 
/*      */   public int readUnsignedShort(int index)
/*      */   {
/* 1879 */     byte[] b = this.b;
/* 1880 */     return (b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF;
/*      */   }
/*      */ 
/*      */   public short readShort(int index)
/*      */   {
/* 1892 */     byte[] b = this.b;
/* 1893 */     return (short)((b[index] & 0xFF) << 8 | b[(index + 1)] & 0xFF);
/*      */   }
/*      */ 
/*      */   public int readInt(int index)
/*      */   {
/* 1905 */     byte[] b = this.b;
/* 1906 */     return (b[index] & 0xFF) << 24 | (b[(index + 1)] & 0xFF) << 16 | (b[(index + 2)] & 0xFF) << 8 | b[(index + 3)] & 0xFF;
/*      */   }
/*      */ 
/*      */   public long readLong(int index)
/*      */   {
/* 1919 */     long l1 = readInt(index);
/* 1920 */     long l0 = readInt(index + 4) & 0xFFFFFFFF;
/* 1921 */     return l1 << 32 | l0;
/*      */   }
/*      */ 
/*      */   public String readUTF8(int index, char[] buf)
/*      */   {
/* 1936 */     int item = readUnsignedShort(index);
/* 1937 */     String s = this.strings[item];
/* 1938 */     if (s != null) {
/* 1939 */       return s;
/*      */     }
/* 1941 */     index = this.items[item];
/* 1942 */     return this.strings[item] =  = readUTF(index + 2, readUnsignedShort(index), buf);
/*      */   }
/*      */ 
/*      */   private String readUTF(int index, int utfLen, char[] buf)
/*      */   {
/* 1955 */     int endIndex = index + utfLen;
/* 1956 */     byte[] b = this.b;
/* 1957 */     int strLen = 0;
/*      */ 
/* 1959 */     while (index < endIndex) {
/* 1960 */       int c = b[(index++)] & 0xFF;
/*      */       int d;
/* 1961 */       switch (c >> 4)
/*      */       {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/* 1971 */         buf[(strLen++)] = ((char)c);
/* 1972 */         break;
/*      */       case 12:
/*      */       case 13:
/* 1976 */         d = b[(index++)];
/* 1977 */         buf[(strLen++)] = ((char)((c & 0x1F) << 6 | d & 0x3F));
/* 1978 */         break;
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       default:
/* 1981 */         d = b[(index++)];
/* 1982 */         int e = b[(index++)];
/* 1983 */         buf[(strLen++)] = ((char)((c & 0xF) << 12 | (d & 0x3F) << 6 | e & 0x3F));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1988 */     return new String(buf, 0, strLen);
/*      */   }
/*      */ 
/*      */   public String readClass(int index, char[] buf)
/*      */   {
/* 2006 */     return readUTF8(this.items[readUnsignedShort(index)], buf);
/*      */   }
/*      */ 
/*      */   public Object readConst(int item, char[] buf)
/*      */   {
/* 2022 */     int index = this.items[item];
/* 2023 */     switch (this.b[(index - 1)]) {
/*      */     case 3:
/* 2025 */       return new Integer(readInt(index));
/*      */     case 4:
/* 2027 */       return new Float(Float.intBitsToFloat(readInt(index)));
/*      */     case 5:
/* 2029 */       return new Long(readLong(index));
/*      */     case 6:
/* 2031 */       return new Double(Double.longBitsToDouble(readLong(index)));
/*      */     case 7:
/* 2033 */       return Type.getObjectType(readUTF8(index, buf));
/*      */     }
/*      */ 
/* 2036 */     return readUTF8(index, buf);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.ClassReader
 * JD-Core Version:    0.6.2
 */