/*      */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*      */ 
/*      */ final class Frame
/*      */ {
/*      */   static final int DIM = -268435456;
/*      */   static final int ARRAY_OF = 268435456;
/*      */   static final int ELEMENT_OF = -268435456;
/*      */   static final int KIND = 251658240;
/*      */   static final int VALUE = 16777215;
/*      */   static final int BASE_KIND = 267386880;
/*      */   static final int BASE_VALUE = 1048575;
/*      */   static final int BASE = 16777216;
/*      */   static final int OBJECT = 24117248;
/*      */   static final int UNINITIALIZED = 25165824;
/*      */   private static final int LOCAL = 33554432;
/*      */   private static final int STACK = 50331648;
/*      */   static final int TOP = 16777216;
/*      */   static final int BOOLEAN = 16777225;
/*      */   static final int BYTE = 16777226;
/*      */   static final int CHAR = 16777227;
/*      */   static final int SHORT = 16777228;
/*      */   static final int INTEGER = 16777217;
/*      */   static final int FLOAT = 16777218;
/*      */   static final int DOUBLE = 16777219;
/*      */   static final int LONG = 16777220;
/*      */   static final int NULL = 16777221;
/*      */   static final int UNINITIALIZED_THIS = 16777222;
/*  268 */   static final int[] SIZE = b;
/*      */   Label owner;
/*      */   int[] inputLocals;
/*      */   int[] inputStack;
/*      */   private int[] outputLocals;
/*      */   private int[] outputStack;
/*      */   private int outputStackTop;
/*      */   private int initializationCount;
/*      */   private int[] initializations;
/*      */ 
/*      */   private int get(int local)
/*      */   {
/*  551 */     if ((this.outputLocals == null) || (local >= this.outputLocals.length))
/*      */     {
/*  554 */       return 0x2000000 | local;
/*      */     }
/*  556 */     int type = this.outputLocals[local];
/*  557 */     if (type == 0)
/*      */     {
/*  560 */       type = this.outputLocals[local] = 0x2000000 | local;
/*      */     }
/*  562 */     return type;
/*      */   }
/*      */ 
/*      */   private void set(int local, int type)
/*      */   {
/*  574 */     if (this.outputLocals == null) {
/*  575 */       this.outputLocals = new int[10];
/*      */     }
/*  577 */     int n = this.outputLocals.length;
/*  578 */     if (local >= n) {
/*  579 */       int[] t = new int[Math.max(local + 1, 2 * n)];
/*  580 */       System.arraycopy(this.outputLocals, 0, t, 0, n);
/*  581 */       this.outputLocals = t;
/*      */     }
/*      */ 
/*  584 */     this.outputLocals[local] = type;
/*      */   }
/*      */ 
/*      */   private void push(int type)
/*      */   {
/*  594 */     if (this.outputStack == null) {
/*  595 */       this.outputStack = new int[10];
/*      */     }
/*  597 */     int n = this.outputStack.length;
/*  598 */     if (this.outputStackTop >= n) {
/*  599 */       int[] t = new int[Math.max(this.outputStackTop + 1, 2 * n)];
/*  600 */       System.arraycopy(this.outputStack, 0, t, 0, n);
/*  601 */       this.outputStack = t;
/*      */     }
/*      */ 
/*  604 */     this.outputStack[(this.outputStackTop++)] = type;
/*      */ 
/*  606 */     int top = this.owner.inputStackTop + this.outputStackTop;
/*  607 */     if (top > this.owner.outputStackMax)
/*  608 */       this.owner.outputStackMax = top;
/*      */   }
/*      */ 
/*      */   private void push(ClassWriter cw, String desc)
/*      */   {
/*  621 */     int type = type(cw, desc);
/*  622 */     if (type != 0) {
/*  623 */       push(type);
/*  624 */       if ((type == 16777220) || (type == 16777219))
/*  625 */         push(16777216);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int type(ClassWriter cw, String desc)
/*      */   {
/*  639 */     int index = desc.charAt(0) == '(' ? desc.indexOf(')') + 1 : 0;
/*      */     String t;
/*  640 */     switch (desc.charAt(index)) {
/*      */     case 'V':
/*  642 */       return 0;
/*      */     case 'B':
/*      */     case 'C':
/*      */     case 'I':
/*      */     case 'S':
/*      */     case 'Z':
/*  648 */       return 16777217;
/*      */     case 'F':
/*  650 */       return 16777218;
/*      */     case 'J':
/*  652 */       return 16777220;
/*      */     case 'D':
/*  654 */       return 16777219;
/*      */     case 'L':
/*  657 */       t = desc.substring(index + 1, desc.length() - 1);
/*  658 */       return 0x1700000 | cw.addType(t);
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'W':
/*      */     case 'X':
/*  663 */     case 'Y': } int dims = index + 1;
/*  664 */     while (desc.charAt(dims) == '[')
/*  665 */       dims++;
/*      */     int data;
/*  667 */     switch (desc.charAt(dims)) {
/*      */     case 'Z':
/*  669 */       data = 16777225;
/*  670 */       break;
/*      */     case 'C':
/*  672 */       data = 16777227;
/*  673 */       break;
/*      */     case 'B':
/*  675 */       data = 16777226;
/*  676 */       break;
/*      */     case 'S':
/*  678 */       data = 16777228;
/*  679 */       break;
/*      */     case 'I':
/*  681 */       data = 16777217;
/*  682 */       break;
/*      */     case 'F':
/*  684 */       data = 16777218;
/*  685 */       break;
/*      */     case 'J':
/*  687 */       data = 16777220;
/*  688 */       break;
/*      */     case 'D':
/*  690 */       data = 16777219;
/*  691 */       break;
/*      */     case 'E':
/*      */     case 'G':
/*      */     case 'H':
/*      */     case 'K':
/*      */     case 'L':
/*      */     case 'M':
/*      */     case 'N':
/*      */     case 'O':
/*      */     case 'P':
/*      */     case 'Q':
/*      */     case 'R':
/*      */     case 'T':
/*      */     case 'U':
/*      */     case 'V':
/*      */     case 'W':
/*      */     case 'X':
/*      */     case 'Y':
/*      */     default:
/*  695 */       t = desc.substring(dims + 1, desc.length() - 1);
/*  696 */       data = 0x1700000 | cw.addType(t);
/*      */     }
/*  698 */     return dims - index << 28 | data;
/*      */   }
/*      */ 
/*      */   private int pop()
/*      */   {
/*  708 */     if (this.outputStackTop > 0) {
/*  709 */       return this.outputStack[(--this.outputStackTop)];
/*      */     }
/*      */ 
/*  712 */     return 0x3000000 | ---this.owner.inputStackTop;
/*      */   }
/*      */ 
/*      */   private void pop(int elements)
/*      */   {
/*  722 */     if (this.outputStackTop >= elements) {
/*  723 */       this.outputStackTop -= elements;
/*      */     }
/*      */     else
/*      */     {
/*  728 */       this.owner.inputStackTop -= elements - this.outputStackTop;
/*  729 */       this.outputStackTop = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void pop(String desc)
/*      */   {
/*  741 */     char c = desc.charAt(0);
/*  742 */     if (c == '(')
/*  743 */       pop((MethodWriter.getArgumentsAndReturnSizes(desc) >> 2) - 1);
/*  744 */     else if ((c == 'J') || (c == 'D'))
/*  745 */       pop(2);
/*      */     else
/*  747 */       pop(1);
/*      */   }
/*      */ 
/*      */   private void init(int var)
/*      */   {
/*  759 */     if (this.initializations == null) {
/*  760 */       this.initializations = new int[2];
/*      */     }
/*  762 */     int n = this.initializations.length;
/*  763 */     if (this.initializationCount >= n) {
/*  764 */       int[] t = new int[Math.max(this.initializationCount + 1, 2 * n)];
/*  765 */       System.arraycopy(this.initializations, 0, t, 0, n);
/*  766 */       this.initializations = t;
/*      */     }
/*      */ 
/*  769 */     this.initializations[(this.initializationCount++)] = var;
/*      */   }
/*      */ 
/*      */   private int init(ClassWriter cw, int t)
/*      */   {
/*      */     int s;
/*  783 */     if (t == 16777222) {
/*  784 */       s = 0x1700000 | cw.addType(cw.thisName);
/*      */     }
/*      */     else
/*      */     {
/*      */       int s;
/*  785 */       if ((t & 0xFFF00000) == 25165824) {
/*  786 */         String type = cw.typeTable[(t & 0xFFFFF)].strVal1;
/*  787 */         s = 0x1700000 | cw.addType(type);
/*      */       } else {
/*  789 */         return t;
/*      */       }
/*      */     }
/*      */     int s;
/*  791 */     for (int j = 0; j < this.initializationCount; j++) {
/*  792 */       int u = this.initializations[j];
/*  793 */       int dim = u & 0xF0000000;
/*  794 */       int kind = u & 0xF000000;
/*  795 */       if (kind == 33554432)
/*  796 */         u = dim + this.inputLocals[(u & 0xFFFFFF)];
/*  797 */       else if (kind == 50331648) {
/*  798 */         u = dim + this.inputStack[(this.inputStack.length - (u & 0xFFFFFF))];
/*      */       }
/*  800 */       if (t == u) {
/*  801 */         return s;
/*      */       }
/*      */     }
/*  804 */     return t;
/*      */   }
/*      */ 
/*      */   void initInputFrame(ClassWriter cw, int access, Type[] args, int maxLocals)
/*      */   {
/*  822 */     this.inputLocals = new int[maxLocals];
/*  823 */     this.inputStack = new int[0];
/*  824 */     int i = 0;
/*  825 */     if ((access & 0x8) == 0) {
/*  826 */       if ((access & 0x40000) == 0)
/*  827 */         this.inputLocals[(i++)] = (0x1700000 | cw.addType(cw.thisName));
/*      */       else {
/*  829 */         this.inputLocals[(i++)] = 16777222;
/*      */       }
/*      */     }
/*  832 */     for (int j = 0; j < args.length; j++) {
/*  833 */       int t = type(cw, args[j].getDescriptor());
/*  834 */       this.inputLocals[(i++)] = t;
/*  835 */       if ((t == 16777220) || (t == 16777219)) {
/*  836 */         this.inputLocals[(i++)] = 16777216;
/*      */       }
/*      */     }
/*  839 */     while (i < maxLocals)
/*  840 */       this.inputLocals[(i++)] = 16777216;
/*      */   }
/*      */ 
/*      */   void execute(int opcode, int arg, ClassWriter cw, Item item)
/*      */   {
/*      */     int t1;
/*      */     int t2;
/*      */     int t3;
/*      */     String s;
/*  859 */     switch (opcode) {
/*      */     case 0:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 145:
/*      */     case 146:
/*      */     case 147:
/*      */     case 167:
/*      */     case 177:
/*  870 */       break;
/*      */     case 1:
/*  872 */       push(16777221);
/*  873 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 16:
/*      */     case 17:
/*      */     case 21:
/*  884 */       push(16777217);
/*  885 */       break;
/*      */     case 9:
/*      */     case 10:
/*      */     case 22:
/*  889 */       push(16777220);
/*  890 */       push(16777216);
/*  891 */       break;
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 23:
/*  896 */       push(16777218);
/*  897 */       break;
/*      */     case 14:
/*      */     case 15:
/*      */     case 24:
/*  901 */       push(16777219);
/*  902 */       push(16777216);
/*  903 */       break;
/*      */     case 18:
/*  905 */       switch (item.type) {
/*      */       case 3:
/*  907 */         push(16777217);
/*  908 */         break;
/*      */       case 5:
/*  910 */         push(16777220);
/*  911 */         push(16777216);
/*  912 */         break;
/*      */       case 4:
/*  914 */         push(16777218);
/*  915 */         break;
/*      */       case 6:
/*  917 */         push(16777219);
/*  918 */         push(16777216);
/*  919 */         break;
/*      */       case 7:
/*  921 */         push(0x1700000 | cw.addType("java/lang/Class"));
/*  922 */         break;
/*      */       default:
/*  925 */         push(0x1700000 | cw.addType("java/lang/String"));
/*      */       }
/*  927 */       break;
/*      */     case 25:
/*  929 */       push(get(arg));
/*  930 */       break;
/*      */     case 46:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*  935 */       pop(2);
/*  936 */       push(16777217);
/*  937 */       break;
/*      */     case 47:
/*      */     case 143:
/*  940 */       pop(2);
/*  941 */       push(16777220);
/*  942 */       push(16777216);
/*  943 */       break;
/*      */     case 48:
/*  945 */       pop(2);
/*  946 */       push(16777218);
/*  947 */       break;
/*      */     case 49:
/*      */     case 138:
/*  950 */       pop(2);
/*  951 */       push(16777219);
/*  952 */       push(16777216);
/*  953 */       break;
/*      */     case 50:
/*  955 */       pop(1);
/*  956 */       t1 = pop();
/*  957 */       push(-268435456 + t1);
/*  958 */       break;
/*      */     case 54:
/*      */     case 56:
/*      */     case 58:
/*  962 */       t1 = pop();
/*  963 */       set(arg, t1);
/*  964 */       if (arg > 0) {
/*  965 */         t2 = get(arg - 1);
/*      */ 
/*  967 */         if ((t2 == 16777220) || (t2 == 16777219))
/*  968 */           set(arg - 1, 16777216);  } break;
/*      */     case 55:
/*      */     case 57:
/*  974 */       pop(1);
/*  975 */       t1 = pop();
/*  976 */       set(arg, t1);
/*  977 */       set(arg + 1, 16777216);
/*  978 */       if (arg > 0) {
/*  979 */         t2 = get(arg - 1);
/*      */ 
/*  981 */         if ((t2 == 16777220) || (t2 == 16777219))
/*  982 */           set(arg - 1, 16777216);  } break;
/*      */     case 79:
/*      */     case 81:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*  992 */       pop(3);
/*  993 */       break;
/*      */     case 80:
/*      */     case 82:
/*  996 */       pop(4);
/*  997 */       break;
/*      */     case 87:
/*      */     case 153:
/*      */     case 154:
/*      */     case 155:
/*      */     case 156:
/*      */     case 157:
/*      */     case 158:
/*      */     case 170:
/*      */     case 171:
/*      */     case 172:
/*      */     case 174:
/*      */     case 176:
/*      */     case 191:
/*      */     case 194:
/*      */     case 195:
/*      */     case 198:
/*      */     case 199:
/* 1015 */       pop(1);
/* 1016 */       break;
/*      */     case 88:
/*      */     case 159:
/*      */     case 160:
/*      */     case 161:
/*      */     case 162:
/*      */     case 163:
/*      */     case 164:
/*      */     case 165:
/*      */     case 166:
/*      */     case 173:
/*      */     case 175:
/* 1028 */       pop(2);
/* 1029 */       break;
/*      */     case 89:
/* 1031 */       t1 = pop();
/* 1032 */       push(t1);
/* 1033 */       push(t1);
/* 1034 */       break;
/*      */     case 90:
/* 1036 */       t1 = pop();
/* 1037 */       t2 = pop();
/* 1038 */       push(t1);
/* 1039 */       push(t2);
/* 1040 */       push(t1);
/* 1041 */       break;
/*      */     case 91:
/* 1043 */       t1 = pop();
/* 1044 */       t2 = pop();
/* 1045 */       t3 = pop();
/* 1046 */       push(t1);
/* 1047 */       push(t3);
/* 1048 */       push(t2);
/* 1049 */       push(t1);
/* 1050 */       break;
/*      */     case 92:
/* 1052 */       t1 = pop();
/* 1053 */       t2 = pop();
/* 1054 */       push(t2);
/* 1055 */       push(t1);
/* 1056 */       push(t2);
/* 1057 */       push(t1);
/* 1058 */       break;
/*      */     case 93:
/* 1060 */       t1 = pop();
/* 1061 */       t2 = pop();
/* 1062 */       t3 = pop();
/* 1063 */       push(t2);
/* 1064 */       push(t1);
/* 1065 */       push(t3);
/* 1066 */       push(t2);
/* 1067 */       push(t1);
/* 1068 */       break;
/*      */     case 94:
/* 1070 */       t1 = pop();
/* 1071 */       t2 = pop();
/* 1072 */       t3 = pop();
/* 1073 */       int t4 = pop();
/* 1074 */       push(t2);
/* 1075 */       push(t1);
/* 1076 */       push(t4);
/* 1077 */       push(t3);
/* 1078 */       push(t2);
/* 1079 */       push(t1);
/* 1080 */       break;
/*      */     case 95:
/* 1082 */       t1 = pop();
/* 1083 */       t2 = pop();
/* 1084 */       push(t1);
/* 1085 */       push(t2);
/* 1086 */       break;
/*      */     case 96:
/*      */     case 100:
/*      */     case 104:
/*      */     case 108:
/*      */     case 112:
/*      */     case 120:
/*      */     case 122:
/*      */     case 124:
/*      */     case 126:
/*      */     case 128:
/*      */     case 130:
/*      */     case 136:
/*      */     case 142:
/*      */     case 149:
/*      */     case 150:
/* 1102 */       pop(2);
/* 1103 */       push(16777217);
/* 1104 */       break;
/*      */     case 97:
/*      */     case 101:
/*      */     case 105:
/*      */     case 109:
/*      */     case 113:
/*      */     case 127:
/*      */     case 129:
/*      */     case 131:
/* 1113 */       pop(4);
/* 1114 */       push(16777220);
/* 1115 */       push(16777216);
/* 1116 */       break;
/*      */     case 98:
/*      */     case 102:
/*      */     case 106:
/*      */     case 110:
/*      */     case 114:
/*      */     case 137:
/*      */     case 144:
/* 1124 */       pop(2);
/* 1125 */       push(16777218);
/* 1126 */       break;
/*      */     case 99:
/*      */     case 103:
/*      */     case 107:
/*      */     case 111:
/*      */     case 115:
/* 1132 */       pop(4);
/* 1133 */       push(16777219);
/* 1134 */       push(16777216);
/* 1135 */       break;
/*      */     case 121:
/*      */     case 123:
/*      */     case 125:
/* 1139 */       pop(3);
/* 1140 */       push(16777220);
/* 1141 */       push(16777216);
/* 1142 */       break;
/*      */     case 132:
/* 1144 */       set(arg, 16777217);
/* 1145 */       break;
/*      */     case 133:
/*      */     case 140:
/* 1148 */       pop(1);
/* 1149 */       push(16777220);
/* 1150 */       push(16777216);
/* 1151 */       break;
/*      */     case 134:
/* 1153 */       pop(1);
/* 1154 */       push(16777218);
/* 1155 */       break;
/*      */     case 135:
/*      */     case 141:
/* 1158 */       pop(1);
/* 1159 */       push(16777219);
/* 1160 */       push(16777216);
/* 1161 */       break;
/*      */     case 139:
/*      */     case 190:
/*      */     case 193:
/* 1165 */       pop(1);
/* 1166 */       push(16777217);
/* 1167 */       break;
/*      */     case 148:
/*      */     case 151:
/*      */     case 152:
/* 1171 */       pop(4);
/* 1172 */       push(16777217);
/* 1173 */       break;
/*      */     case 168:
/*      */     case 169:
/* 1176 */       throw new RuntimeException("JSR/RET are not supported with computeFrames option");
/*      */     case 178:
/* 1178 */       push(cw, item.strVal3);
/* 1179 */       break;
/*      */     case 179:
/* 1181 */       pop(item.strVal3);
/* 1182 */       break;
/*      */     case 180:
/* 1184 */       pop(1);
/* 1185 */       push(cw, item.strVal3);
/* 1186 */       break;
/*      */     case 181:
/* 1188 */       pop(item.strVal3);
/* 1189 */       pop();
/* 1190 */       break;
/*      */     case 182:
/*      */     case 183:
/*      */     case 184:
/*      */     case 185:
/* 1195 */       pop(item.strVal3);
/* 1196 */       if (opcode != 184) {
/* 1197 */         t1 = pop();
/* 1198 */         if ((opcode == 183) && (item.strVal2.charAt(0) == '<'))
/*      */         {
/* 1201 */           init(t1);
/*      */         }
/*      */       }
/* 1204 */       push(cw, item.strVal3);
/* 1205 */       break;
/*      */     case 187:
/* 1207 */       push(0x1800000 | cw.addUninitializedType(item.strVal1, arg));
/* 1208 */       break;
/*      */     case 188:
/* 1210 */       pop();
/* 1211 */       switch (arg) {
/*      */       case 4:
/* 1213 */         push(285212681);
/* 1214 */         break;
/*      */       case 5:
/* 1216 */         push(285212683);
/* 1217 */         break;
/*      */       case 8:
/* 1219 */         push(285212682);
/* 1220 */         break;
/*      */       case 9:
/* 1222 */         push(285212684);
/* 1223 */         break;
/*      */       case 10:
/* 1225 */         push(285212673);
/* 1226 */         break;
/*      */       case 6:
/* 1228 */         push(285212674);
/* 1229 */         break;
/*      */       case 7:
/* 1231 */         push(285212675);
/* 1232 */         break;
/*      */       default:
/* 1235 */         push(285212676);
/* 1236 */       }break;
/*      */     case 189:
/* 1240 */       s = item.strVal1;
/* 1241 */       pop();
/* 1242 */       if (s.charAt(0) == '[')
/* 1243 */         push(cw, '[' + s);
/*      */       else {
/* 1245 */         push(0x11700000 | cw.addType(s));
/*      */       }
/* 1247 */       break;
/*      */     case 192:
/* 1249 */       s = item.strVal1;
/* 1250 */       pop();
/* 1251 */       if (s.charAt(0) == '[')
/* 1252 */         push(cw, s);
/*      */       else {
/* 1254 */         push(0x1700000 | cw.addType(s));
/*      */       }
/* 1256 */       break;
/*      */     case 19:
/*      */     case 20:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*      */     case 33:
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 186:
/*      */     case 196:
/*      */     case 197:
/*      */     default:
/* 1259 */       pop(arg);
/* 1260 */       push(cw, item.strVal1);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean merge(ClassWriter cw, Frame frame, int edge)
/*      */   {
/* 1278 */     boolean changed = false;
/*      */ 
/* 1281 */     int nLocal = this.inputLocals.length;
/* 1282 */     int nStack = this.inputStack.length;
/* 1283 */     if (frame.inputLocals == null) {
/* 1284 */       frame.inputLocals = new int[nLocal];
/* 1285 */       changed = true;
/*      */     }
/*      */ 
/* 1288 */     for (int i = 0; i < nLocal; i++)
/*      */     {
/*      */       int t;
/*      */       int t;
/* 1289 */       if ((this.outputLocals != null) && (i < this.outputLocals.length)) {
/* 1290 */         int s = this.outputLocals[i];
/*      */         int t;
/* 1291 */         if (s == 0) {
/* 1292 */           t = this.inputLocals[i];
/*      */         } else {
/* 1294 */           int dim = s & 0xF0000000;
/* 1295 */           int kind = s & 0xF000000;
/*      */           int t;
/* 1296 */           if (kind == 33554432) {
/* 1297 */             t = dim + this.inputLocals[(s & 0xFFFFFF)];
/*      */           }
/*      */           else
/*      */           {
/*      */             int t;
/* 1298 */             if (kind == 50331648)
/* 1299 */               t = dim + this.inputStack[(nStack - (s & 0xFFFFFF))];
/*      */             else
/* 1301 */               t = s;
/*      */           }
/*      */         }
/*      */       } else {
/* 1305 */         t = this.inputLocals[i];
/*      */       }
/* 1307 */       if (this.initializations != null) {
/* 1308 */         t = init(cw, t);
/*      */       }
/* 1310 */       changed |= merge(cw, t, frame.inputLocals, i);
/*      */     }
/*      */ 
/* 1313 */     if (edge > 0) {
/* 1314 */       for (i = 0; i < nLocal; i++) {
/* 1315 */         int t = this.inputLocals[i];
/* 1316 */         changed |= merge(cw, t, frame.inputLocals, i);
/*      */       }
/* 1318 */       if (frame.inputStack == null) {
/* 1319 */         frame.inputStack = new int[1];
/* 1320 */         changed = true;
/*      */       }
/* 1322 */       changed |= merge(cw, edge, frame.inputStack, 0);
/* 1323 */       return changed;
/*      */     }
/*      */ 
/* 1326 */     int nInputStack = this.inputStack.length + this.owner.inputStackTop;
/* 1327 */     if (frame.inputStack == null) {
/* 1328 */       frame.inputStack = new int[nInputStack + this.outputStackTop];
/* 1329 */       changed = true;
/*      */     }
/*      */ 
/* 1332 */     for (i = 0; i < nInputStack; i++) {
/* 1333 */       int t = this.inputStack[i];
/* 1334 */       if (this.initializations != null) {
/* 1335 */         t = init(cw, t);
/*      */       }
/* 1337 */       changed |= merge(cw, t, frame.inputStack, i);
/*      */     }
/* 1339 */     for (i = 0; i < this.outputStackTop; i++) {
/* 1340 */       int s = this.outputStack[i];
/* 1341 */       int dim = s & 0xF0000000;
/* 1342 */       int kind = s & 0xF000000;
/*      */       int t;
/*      */       int t;
/* 1343 */       if (kind == 33554432) {
/* 1344 */         t = dim + this.inputLocals[(s & 0xFFFFFF)];
/*      */       }
/*      */       else
/*      */       {
/*      */         int t;
/* 1345 */         if (kind == 50331648)
/* 1346 */           t = dim + this.inputStack[(nStack - (s & 0xFFFFFF))];
/*      */         else
/* 1348 */           t = s;
/*      */       }
/* 1350 */       if (this.initializations != null) {
/* 1351 */         t = init(cw, t);
/*      */       }
/* 1353 */       changed |= merge(cw, t, frame.inputStack, nInputStack + i);
/*      */     }
/* 1355 */     return changed;
/*      */   }
/*      */ 
/*      */   private static boolean merge(ClassWriter cw, int t, int[] types, int index)
/*      */   {
/* 1376 */     int u = types[index];
/* 1377 */     if (u == t)
/*      */     {
/* 1379 */       return false;
/*      */     }
/* 1381 */     if ((t & 0xFFFFFFF) == 16777221) {
/* 1382 */       if (u == 16777221) {
/* 1383 */         return false;
/*      */       }
/* 1385 */       t = 16777221;
/*      */     }
/* 1387 */     if (u == 0)
/*      */     {
/* 1389 */       types[index] = t;
/* 1390 */       return true;
/*      */     }
/*      */     int v;
/*      */     int v;
/* 1393 */     if (((u & 0xFF00000) == 24117248) || ((u & 0xF0000000) != 0))
/*      */     {
/* 1395 */       if (t == 16777221)
/*      */       {
/* 1397 */         return false;
/*      */       }
/*      */       int v;
/* 1398 */       if ((t & 0xFFF00000) == (u & 0xFFF00000))
/*      */       {
/*      */         int v;
/* 1399 */         if ((u & 0xFF00000) == 24117248)
/*      */         {
/* 1403 */           v = t & 0xF0000000 | 0x1700000 | cw.getMergedType(t & 0xFFFFF, u & 0xFFFFF);
/*      */         }
/*      */         else
/*      */         {
/* 1408 */           v = 0x1700000 | cw.addType("java/lang/Object");
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         int v;
/* 1410 */         if (((t & 0xFF00000) == 24117248) || ((t & 0xF0000000) != 0))
/*      */         {
/* 1413 */           v = 0x1700000 | cw.addType("java/lang/Object");
/*      */         }
/*      */         else
/* 1416 */           v = 16777216;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       int v;
/* 1418 */       if (u == 16777221)
/*      */       {
/* 1421 */         v = ((t & 0xFF00000) == 24117248) || ((t & 0xF0000000) != 0) ? t : 16777216;
/*      */       }
/*      */       else
/* 1424 */         v = 16777216;
/*      */     }
/* 1426 */     if (u != v) {
/* 1427 */       types[index] = v;
/* 1428 */       return true;
/*      */     }
/* 1430 */     return false;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  260 */     int[] b = new int['Ê'];
/*  261 */     String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
/*      */ 
/*  265 */     for (int i = 0; i < b.length; i++)
/*  266 */       b[i] = (s.charAt(i) - 'E');
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.Frame
 * JD-Core Version:    0.6.2
 */