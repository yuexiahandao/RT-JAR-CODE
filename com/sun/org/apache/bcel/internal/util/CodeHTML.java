/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Code;
/*     */ import com.sun.org.apache.bcel.internal.classfile.CodeException;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ final class CodeHTML
/*     */   implements Constants
/*     */ {
/*     */   private String class_name;
/*     */   private Method[] methods;
/*     */   private PrintWriter file;
/*     */   private BitSet goto_set;
/*     */   private ConstantPool constant_pool;
/*     */   private ConstantHTML constant_html;
/*  78 */   private static boolean wide = false;
/*     */ 
/*     */   CodeHTML(String dir, String class_name, Method[] methods, ConstantPool constant_pool, ConstantHTML constant_html)
/*     */     throws IOException
/*     */   {
/*  84 */     this.class_name = class_name;
/*  85 */     this.methods = methods;
/*  86 */     this.constant_pool = constant_pool;
/*  87 */     this.constant_html = constant_html;
/*     */ 
/*  89 */     this.file = new PrintWriter(new FileOutputStream(dir + class_name + "_code.html"));
/*  90 */     this.file.println("<HTML><BODY BGCOLOR=\"#C0C0C0\">");
/*     */ 
/*  92 */     for (int i = 0; i < methods.length; i++) {
/*  93 */       writeMethod(methods[i], i);
/*     */     }
/*  95 */     this.file.println("</BODY></HTML>");
/*  96 */     this.file.close();
/*     */   }
/*     */ 
/*     */   private final String codeToHTML(ByteSequence bytes, int method_number)
/*     */     throws IOException
/*     */   {
/* 109 */     short opcode = (short)bytes.readUnsignedByte();
/*     */ 
/* 112 */     int default_offset = 0;
/*     */ 
/* 115 */     int no_pad_bytes = 0;
/*     */ 
/* 117 */     StringBuffer buf = new StringBuffer("<TT>" + OPCODE_NAMES[opcode] + "</TT></TD><TD>");
/*     */ 
/* 122 */     if ((opcode == 170) || (opcode == 171)) {
/* 123 */       int remainder = bytes.getIndex() % 4;
/* 124 */       no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;
/*     */ 
/* 126 */       for (int i = 0; i < no_pad_bytes; i++) {
/* 127 */         bytes.readByte();
/*     */       }
/*     */ 
/* 130 */       default_offset = bytes.readInt();
/*     */     }
/*     */     int offset;
/*     */     int[] jump_table;
/*     */     int index;
/*     */     int vindex;
/*     */     int class_index;
/*     */     String name;
/*     */     int index;
/* 133 */     switch (opcode) {
/*     */     case 170:
/* 135 */       int low = bytes.readInt();
/* 136 */       int high = bytes.readInt();
/*     */ 
/* 138 */       offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
/* 139 */       default_offset += offset;
/*     */ 
/* 141 */       buf.append("<TABLE BORDER=1><TR>");
/*     */ 
/* 144 */       jump_table = new int[high - low + 1];
/* 145 */       for (int i = 0; i < jump_table.length; i++) {
/* 146 */         jump_table[i] = (offset + bytes.readInt());
/*     */ 
/* 148 */         buf.append("<TH>" + (low + i) + "</TH>");
/*     */       }
/* 150 */       buf.append("<TH>default</TH></TR>\n<TR>");
/*     */ 
/* 153 */       for (int i = 0; i < jump_table.length; i++) {
/* 154 */         buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i] + "\">" + jump_table[i] + "</A></TD>");
/*     */       }
/* 156 */       buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset + "\">" + default_offset + "</A></TD></TR>\n</TABLE>\n");
/*     */ 
/* 159 */       break;
/*     */     case 171:
/* 164 */       int npairs = bytes.readInt();
/* 165 */       offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
/* 166 */       jump_table = new int[npairs];
/* 167 */       default_offset += offset;
/*     */ 
/* 169 */       buf.append("<TABLE BORDER=1><TR>");
/*     */ 
/* 172 */       for (int i = 0; i < npairs; i++) {
/* 173 */         int match = bytes.readInt();
/*     */ 
/* 175 */         jump_table[i] = (offset + bytes.readInt());
/* 176 */         buf.append("<TH>" + match + "</TH>");
/*     */       }
/* 178 */       buf.append("<TH>default</TH></TR>\n<TR>");
/*     */ 
/* 181 */       for (int i = 0; i < npairs; i++) {
/* 182 */         buf.append("<TD><A HREF=\"#code" + method_number + "@" + jump_table[i] + "\">" + jump_table[i] + "</A></TD>");
/*     */       }
/* 184 */       buf.append("<TD><A HREF=\"#code" + method_number + "@" + default_offset + "\">" + default_offset + "</A></TD></TR>\n</TABLE>\n");
/*     */ 
/* 186 */       break;
/*     */     case 153:
/*     */     case 154:
/*     */     case 155:
/*     */     case 156:
/*     */     case 157:
/*     */     case 158:
/*     */     case 159:
/*     */     case 160:
/*     */     case 161:
/*     */     case 162:
/*     */     case 163:
/*     */     case 164:
/*     */     case 165:
/*     */     case 166:
/*     */     case 167:
/*     */     case 168:
/*     */     case 198:
/*     */     case 199:
/* 197 */       index = bytes.getIndex() + bytes.readShort() - 1;
/*     */ 
/* 199 */       buf.append("<A HREF=\"#code" + method_number + "@" + index + "\">" + index + "</A>");
/* 200 */       break;
/*     */     case 200:
/*     */     case 201:
/* 205 */       int windex = bytes.getIndex() + bytes.readInt() - 1;
/* 206 */       buf.append("<A HREF=\"#code" + method_number + "@" + windex + "\">" + windex + "</A>");
/*     */ 
/* 208 */       break;
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/*     */     case 54:
/*     */     case 55:
/*     */     case 56:
/*     */     case 57:
/*     */     case 58:
/*     */     case 169:
/* 215 */       if (wide) {
/* 216 */         int vindex = bytes.readShort();
/* 217 */         wide = false;
/*     */       }
/*     */       else {
/* 220 */         vindex = bytes.readUnsignedByte();
/*     */       }
/* 222 */       buf.append("%" + vindex);
/* 223 */       break;
/*     */     case 196:
/* 231 */       wide = true;
/* 232 */       buf.append("(wide)");
/* 233 */       break;
/*     */     case 188:
/* 238 */       buf.append("<FONT COLOR=\"#00FF00\">" + TYPE_NAMES[bytes.readByte()] + "</FONT>");
/* 239 */       break;
/*     */     case 178:
/*     */     case 179:
/*     */     case 180:
/*     */     case 181:
/* 244 */       index = bytes.readShort();
/* 245 */       ConstantFieldref c1 = (ConstantFieldref)this.constant_pool.getConstant(index, (byte)9);
/*     */ 
/* 247 */       class_index = c1.getClassIndex();
/* 248 */       name = this.constant_pool.getConstantString(class_index, (byte)7);
/* 249 */       name = Utility.compactClassName(name, false);
/*     */ 
/* 251 */       index = c1.getNameAndTypeIndex();
/* 252 */       String field_name = this.constant_pool.constantToString(index, (byte)12);
/*     */ 
/* 254 */       if (name.equals(this.class_name)) {
/* 255 */         buf.append("<A HREF=\"" + this.class_name + "_methods.html#field" + field_name + "\" TARGET=Methods>" + field_name + "</A>\n");
/*     */       }
/*     */       else
/*     */       {
/* 259 */         buf.append(this.constant_html.referenceConstant(class_index) + "." + field_name);
/*     */       }
/* 261 */       break;
/*     */     case 187:
/*     */     case 192:
/*     */     case 193:
/* 266 */       index = bytes.readShort();
/* 267 */       buf.append(this.constant_html.referenceConstant(index));
/* 268 */       break;
/*     */     case 182:
/*     */     case 183:
/*     */     case 184:
/*     */     case 185:
/* 273 */       int m_index = bytes.readShort();
/*     */       int class_index;
/* 276 */       if (opcode == 185) {
/* 277 */         int nargs = bytes.readUnsignedByte();
/* 278 */         int reserved = bytes.readUnsignedByte();
/*     */ 
/* 280 */         ConstantInterfaceMethodref c = (ConstantInterfaceMethodref)this.constant_pool.getConstant(m_index, (byte)11);
/*     */ 
/* 282 */         class_index = c.getClassIndex();
/* 283 */         String str = this.constant_pool.constantToString(c);
/* 284 */         index = c.getNameAndTypeIndex();
/*     */       }
/*     */       else {
/* 287 */         ConstantMethodref c = (ConstantMethodref)this.constant_pool.getConstant(m_index, (byte)10);
/* 288 */         class_index = c.getClassIndex();
/*     */ 
/* 290 */         str = this.constant_pool.constantToString(c);
/* 291 */         index = c.getNameAndTypeIndex();
/*     */       }
/*     */ 
/* 294 */       name = Class2HTML.referenceClass(class_index);
/* 295 */       String str = Class2HTML.toHTML(this.constant_pool.constantToString(this.constant_pool.getConstant(index, (byte)12)));
/*     */ 
/* 298 */       ConstantNameAndType c2 = (ConstantNameAndType)this.constant_pool.getConstant(index, (byte)12);
/*     */ 
/* 300 */       String signature = this.constant_pool.constantToString(c2.getSignatureIndex(), (byte)1);
/*     */ 
/* 302 */       String[] args = Utility.methodSignatureArgumentTypes(signature, false);
/* 303 */       String type = Utility.methodSignatureReturnType(signature, false);
/*     */ 
/* 305 */       buf.append(name + ".<A HREF=\"" + this.class_name + "_cp.html#cp" + m_index + "\" TARGET=ConstantPool>" + str + "</A>" + "(");
/*     */ 
/* 309 */       for (int i = 0; i < args.length; i++) {
/* 310 */         buf.append(Class2HTML.referenceType(args[i]));
/*     */ 
/* 312 */         if (i < args.length - 1) {
/* 313 */           buf.append(", ");
/*     */         }
/*     */       }
/* 316 */       buf.append("):" + Class2HTML.referenceType(type));
/*     */ 
/* 318 */       break;
/*     */     case 19:
/*     */     case 20:
/* 323 */       index = bytes.readShort();
/*     */ 
/* 325 */       buf.append("<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(this.constant_pool.constantToString(index, this.constant_pool.getConstant(index).getTag())) + "</a>");
/*     */ 
/* 331 */       break;
/*     */     case 18:
/* 334 */       index = bytes.readUnsignedByte();
/* 335 */       buf.append("<A HREF=\"" + this.class_name + "_cp.html#cp" + index + "\" TARGET=\"ConstantPool\">" + Class2HTML.toHTML(this.constant_pool.constantToString(index, this.constant_pool.getConstant(index).getTag())) + "</a>");
/*     */ 
/* 341 */       break;
/*     */     case 189:
/* 346 */       index = bytes.readShort();
/*     */ 
/* 348 */       buf.append(this.constant_html.referenceConstant(index));
/* 349 */       break;
/*     */     case 197:
/* 354 */       index = bytes.readShort();
/* 355 */       int dimensions = bytes.readByte();
/* 356 */       buf.append(this.constant_html.referenceConstant(index) + ":" + dimensions + "-dimensional");
/* 357 */       break;
/*     */     case 132:
/*     */       int vindex;
/*     */       int constant;
/* 362 */       if (wide) {
/* 363 */         vindex = bytes.readShort();
/* 364 */         int constant = bytes.readShort();
/* 365 */         wide = false;
/*     */       }
/*     */       else {
/* 368 */         vindex = bytes.readUnsignedByte();
/* 369 */         constant = bytes.readByte();
/*     */       }
/* 371 */       buf.append("%" + vindex + " " + constant);
/* 372 */       break;
/*     */     case 26:
/*     */     case 27:
/*     */     case 28:
/*     */     case 29:
/*     */     case 30:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 34:
/*     */     case 35:
/*     */     case 36:
/*     */     case 37:
/*     */     case 38:
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
/*     */     case 50:
/*     */     case 51:
/*     */     case 52:
/*     */     case 53:
/*     */     case 59:
/*     */     case 60:
/*     */     case 61:
/*     */     case 62:
/*     */     case 63:
/*     */     case 64:
/*     */     case 65:
/*     */     case 66:
/*     */     case 67:
/*     */     case 68:
/*     */     case 69:
/*     */     case 70:
/*     */     case 71:
/*     */     case 72:
/*     */     case 73:
/*     */     case 74:
/*     */     case 75:
/*     */     case 76:
/*     */     case 77:
/*     */     case 78:
/*     */     case 79:
/*     */     case 80:
/*     */     case 81:
/*     */     case 82:
/*     */     case 83:
/*     */     case 84:
/*     */     case 85:
/*     */     case 86:
/*     */     case 87:
/*     */     case 88:
/*     */     case 89:
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
/*     */     case 102:
/*     */     case 103:
/*     */     case 104:
/*     */     case 105:
/*     */     case 106:
/*     */     case 107:
/*     */     case 108:
/*     */     case 109:
/*     */     case 110:
/*     */     case 111:
/*     */     case 112:
/*     */     case 113:
/*     */     case 114:
/*     */     case 115:
/*     */     case 116:
/*     */     case 117:
/*     */     case 118:
/*     */     case 119:
/*     */     case 120:
/*     */     case 121:
/*     */     case 122:
/*     */     case 123:
/*     */     case 124:
/*     */     case 125:
/*     */     case 126:
/*     */     case 127:
/*     */     case 128:
/*     */     case 129:
/*     */     case 130:
/*     */     case 131:
/*     */     case 133:
/*     */     case 134:
/*     */     case 135:
/*     */     case 136:
/*     */     case 137:
/*     */     case 138:
/*     */     case 139:
/*     */     case 140:
/*     */     case 141:
/*     */     case 142:
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
/*     */     case 172:
/*     */     case 173:
/*     */     case 174:
/*     */     case 175:
/*     */     case 176:
/*     */     case 177:
/*     */     case 186:
/*     */     case 190:
/*     */     case 191:
/*     */     case 194:
/*     */     case 195:
/*     */     default:
/* 375 */       if (NO_OF_OPERANDS[opcode] > 0) {
/* 376 */         for (int i = 0; i < TYPE_OF_OPERANDS[opcode].length; i++) {
/* 377 */           switch (TYPE_OF_OPERANDS[opcode][i]) {
/*     */           case 8:
/* 379 */             buf.append(bytes.readUnsignedByte());
/* 380 */             break;
/*     */           case 9:
/* 383 */             buf.append(bytes.readShort());
/* 384 */             break;
/*     */           case 10:
/* 387 */             buf.append(bytes.readInt());
/* 388 */             break;
/*     */           default:
/* 391 */             System.err.println("Unreachable default case reached!");
/* 392 */             System.exit(-1);
/*     */           }
/* 394 */           buf.append("&nbsp;");
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 399 */     buf.append("</TD>");
/* 400 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   private final void findGotos(ByteSequence bytes, Method method, Code code)
/*     */     throws IOException
/*     */   {
/* 411 */     this.goto_set = new BitSet(bytes.available());
/*     */ 
/* 418 */     if (code != null) {
/* 419 */       CodeException[] ce = code.getExceptionTable();
/* 420 */       int len = ce.length;
/*     */ 
/* 422 */       for (int i = 0; i < len; i++) {
/* 423 */         this.goto_set.set(ce[i].getStartPC());
/* 424 */         this.goto_set.set(ce[i].getEndPC());
/* 425 */         this.goto_set.set(ce[i].getHandlerPC());
/*     */       }
/*     */ 
/* 429 */       Attribute[] attributes = code.getAttributes();
/* 430 */       for (int i = 0; i < attributes.length; i++) {
/* 431 */         if (attributes[i].getTag() == 5) {
/* 432 */           LocalVariable[] vars = ((LocalVariableTable)attributes[i]).getLocalVariableTable();
/*     */ 
/* 434 */           for (int j = 0; j < vars.length; j++) {
/* 435 */             int start = vars[j].getStartPC();
/* 436 */             int end = start + vars[j].getLength();
/* 437 */             this.goto_set.set(start);
/* 438 */             this.goto_set.set(end);
/*     */           }
/* 440 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 446 */     for (int i = 0; bytes.available() > 0; i++) {
/* 447 */       int opcode = bytes.readUnsignedByte();
/*     */       int index;
/* 449 */       switch (opcode)
/*     */       {
/*     */       case 170:
/*     */       case 171:
/* 453 */         int remainder = bytes.getIndex() % 4;
/* 454 */         int no_pad_bytes = remainder == 0 ? 0 : 4 - remainder;
/*     */ 
/* 457 */         for (int j = 0; j < no_pad_bytes; j++) {
/* 458 */           bytes.readByte();
/*     */         }
/*     */ 
/* 461 */         int default_offset = bytes.readInt();
/*     */ 
/* 463 */         if (opcode == 170) {
/* 464 */           int low = bytes.readInt();
/* 465 */           int high = bytes.readInt();
/*     */ 
/* 467 */           int offset = bytes.getIndex() - 12 - no_pad_bytes - 1;
/* 468 */           default_offset += offset;
/* 469 */           this.goto_set.set(default_offset);
/*     */ 
/* 471 */           for (int j = 0; j < high - low + 1; j++) {
/* 472 */             int index = offset + bytes.readInt();
/* 473 */             this.goto_set.set(index);
/*     */           }
/*     */         }
/*     */         else {
/* 477 */           int npairs = bytes.readInt();
/*     */ 
/* 479 */           int offset = bytes.getIndex() - 8 - no_pad_bytes - 1;
/* 480 */           default_offset += offset;
/* 481 */           this.goto_set.set(default_offset);
/*     */ 
/* 483 */           for (int j = 0; j < npairs; j++) {
/* 484 */             int match = bytes.readInt();
/*     */ 
/* 486 */             int index = offset + bytes.readInt();
/* 487 */             this.goto_set.set(index);
/*     */           }
/*     */         }
/* 490 */         break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/*     */       case 167:
/*     */       case 168:
/*     */       case 198:
/*     */       case 199:
/* 498 */         index = bytes.getIndex() + bytes.readShort() - 1;
/*     */ 
/* 500 */         this.goto_set.set(index);
/* 501 */         break;
/*     */       case 200:
/*     */       case 201:
/* 505 */         index = bytes.getIndex() + bytes.readInt() - 1;
/* 506 */         this.goto_set.set(index);
/* 507 */         break;
/*     */       case 169:
/*     */       case 172:
/*     */       case 173:
/*     */       case 174:
/*     */       case 175:
/*     */       case 176:
/*     */       case 177:
/*     */       case 178:
/*     */       case 179:
/*     */       case 180:
/*     */       case 181:
/*     */       case 182:
/*     */       case 183:
/*     */       case 184:
/*     */       case 185:
/*     */       case 186:
/*     */       case 187:
/*     */       case 188:
/*     */       case 189:
/*     */       case 190:
/*     */       case 191:
/*     */       case 192:
/*     */       case 193:
/*     */       case 194:
/*     */       case 195:
/*     */       case 196:
/*     */       case 197:
/*     */       default:
/* 510 */         bytes.unreadByte();
/* 511 */         codeToHTML(bytes, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeMethod(Method method, int method_number)
/*     */     throws IOException
/*     */   {
/* 523 */     String signature = method.getSignature();
/*     */ 
/* 525 */     String[] args = Utility.methodSignatureArgumentTypes(signature, false);
/*     */ 
/* 527 */     String type = Utility.methodSignatureReturnType(signature, false);
/*     */ 
/* 529 */     String name = method.getName();
/* 530 */     String html_name = Class2HTML.toHTML(name);
/*     */ 
/* 532 */     String access = Utility.accessToString(method.getAccessFlags());
/* 533 */     access = Utility.replace(access, " ", "&nbsp;");
/*     */ 
/* 535 */     Attribute[] attributes = method.getAttributes();
/*     */ 
/* 537 */     this.file.print("<P><B><FONT COLOR=\"#FF0000\">" + access + "</FONT>&nbsp;" + "<A NAME=method" + method_number + ">" + Class2HTML.referenceType(type) + "</A>&nbsp<A HREF=\"" + this.class_name + "_methods.html#method" + method_number + "\" TARGET=Methods>" + html_name + "</A>(");
/*     */ 
/* 542 */     for (int i = 0; i < args.length; i++) {
/* 543 */       this.file.print(Class2HTML.referenceType(args[i]));
/* 544 */       if (i < args.length - 1) {
/* 545 */         this.file.print(",&nbsp;");
/*     */       }
/*     */     }
/* 548 */     this.file.println(")</B></P>");
/*     */ 
/* 550 */     Code c = null;
/* 551 */     byte[] code = null;
/*     */ 
/* 553 */     if (attributes.length > 0) {
/* 554 */       this.file.print("<H4>Attributes</H4><UL>\n");
/* 555 */       for (int i = 0; i < attributes.length; i++) {
/* 556 */         byte tag = attributes[i].getTag();
/*     */ 
/* 558 */         if (tag != -1) {
/* 559 */           this.file.print("<LI><A HREF=\"" + this.class_name + "_attributes.html#method" + method_number + "@" + i + "\" TARGET=Attributes>" + ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
/*     */         }
/*     */         else {
/* 562 */           this.file.print("<LI>" + attributes[i] + "</LI>");
/*     */         }
/* 564 */         if (tag == 2) {
/* 565 */           c = (Code)attributes[i];
/* 566 */           Attribute[] attributes2 = c.getAttributes();
/* 567 */           code = c.getCode();
/*     */ 
/* 569 */           this.file.print("<UL>");
/* 570 */           for (int j = 0; j < attributes2.length; j++) {
/* 571 */             tag = attributes2[j].getTag();
/* 572 */             this.file.print("<LI><A HREF=\"" + this.class_name + "_attributes.html#" + "method" + method_number + "@" + i + "@" + j + "\" TARGET=Attributes>" + ATTRIBUTE_NAMES[tag] + "</A></LI>\n");
/*     */           }
/*     */ 
/* 577 */           this.file.print("</UL>");
/*     */         }
/*     */       }
/* 580 */       this.file.println("</UL>");
/*     */     }
/*     */ 
/* 583 */     if (code != null)
/*     */     {
/* 587 */       ByteSequence stream = new ByteSequence(code);
/* 588 */       stream.mark(stream.available());
/* 589 */       findGotos(stream, method, c);
/* 590 */       stream.reset();
/*     */ 
/* 592 */       this.file.println("<TABLE BORDER=0><TR><TH ALIGN=LEFT>Byte<BR>offset</TH><TH ALIGN=LEFT>Instruction</TH><TH ALIGN=LEFT>Argument</TH>");
/*     */ 
/* 595 */       for (int i = 0; stream.available() > 0; i++) {
/* 596 */         int offset = stream.getIndex();
/* 597 */         String str = codeToHTML(stream, method_number);
/* 598 */         String anchor = "";
/*     */ 
/* 603 */         if (this.goto_set.get(offset))
/* 604 */           anchor = "<A NAME=code" + method_number + "@" + offset + "></A>";
/*     */         String anchor2;
/*     */         String anchor2;
/* 607 */         if (stream.getIndex() == code.length)
/* 608 */           anchor2 = "<A NAME=code" + method_number + "@" + code.length + ">" + offset + "</A>";
/*     */         else {
/* 610 */           anchor2 = "" + offset;
/*     */         }
/* 612 */         this.file.println("<TR VALIGN=TOP><TD>" + anchor2 + "</TD><TD>" + anchor + str + "</TR>");
/*     */       }
/*     */ 
/* 616 */       this.file.println("<TR><TD> </A></TD></TR>");
/* 617 */       this.file.println("</TABLE>");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.CodeHTML
 * JD-Core Version:    0.6.2
 */