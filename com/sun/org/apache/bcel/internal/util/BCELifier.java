/*     */ package com.sun.org.apache.bcel.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Repository;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ClassParser;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
/*     */ import com.sun.org.apache.bcel.internal.classfile.EmptyVisitor;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import com.sun.org.apache.bcel.internal.generic.ArrayType;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.MethodGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ public class BCELifier extends EmptyVisitor
/*     */ {
/*     */   private JavaClass _clazz;
/*     */   private PrintWriter _out;
/*     */   private ConstantPoolGen _cp;
/*     */ 
/*     */   public BCELifier(JavaClass clazz, OutputStream out)
/*     */   {
/*  84 */     this._clazz = clazz;
/*  85 */     this._out = new PrintWriter(out);
/*  86 */     this._cp = new ConstantPoolGen(this._clazz.getConstantPool());
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  92 */     visitJavaClass(this._clazz);
/*  93 */     this._out.flush();
/*     */   }
/*     */ 
/*     */   public void visitJavaClass(JavaClass clazz) {
/*  97 */     String class_name = clazz.getClassName();
/*  98 */     String super_name = clazz.getSuperclassName();
/*  99 */     String package_name = clazz.getPackageName();
/* 100 */     String inter = Utility.printArray(clazz.getInterfaceNames(), false, true);
/*     */ 
/* 102 */     if (!"".equals(package_name)) {
/* 103 */       class_name = class_name.substring(package_name.length() + 1);
/* 104 */       this._out.println("package " + package_name + ";\n");
/*     */     }
/*     */ 
/* 107 */     this._out.println("import com.sun.org.apache.bcel.internal.generic.*;");
/* 108 */     this._out.println("import com.sun.org.apache.bcel.internal.classfile.*;");
/* 109 */     this._out.println("import com.sun.org.apache.bcel.internal.*;");
/* 110 */     this._out.println("import java.io.*;\n");
/*     */ 
/* 112 */     this._out.println("public class " + class_name + "Creator implements Constants {");
/* 113 */     this._out.println("  private InstructionFactory _factory;");
/* 114 */     this._out.println("  private ConstantPoolGen    _cp;");
/* 115 */     this._out.println("  private ClassGen           _cg;\n");
/*     */ 
/* 117 */     this._out.println("  public " + class_name + "Creator() {");
/* 118 */     this._out.println("    _cg = new ClassGen(\"" + ("".equals(package_name) ? class_name : new StringBuilder().append(package_name).append(".").append(class_name).toString()) + "\", \"" + super_name + "\", " + "\"" + clazz.getSourceFileName() + "\", " + printFlags(clazz.getAccessFlags(), true) + ", " + "new String[] { " + inter + " });\n");
/*     */ 
/* 126 */     this._out.println("    _cp = _cg.getConstantPool();");
/* 127 */     this._out.println("    _factory = new InstructionFactory(_cg, _cp);");
/* 128 */     this._out.println("  }\n");
/*     */ 
/* 130 */     printCreate();
/*     */ 
/* 132 */     Field[] fields = clazz.getFields();
/*     */ 
/* 134 */     if (fields.length > 0) {
/* 135 */       this._out.println("  private void createFields() {");
/* 136 */       this._out.println("    FieldGen field;");
/*     */ 
/* 138 */       for (int i = 0; i < fields.length; i++) {
/* 139 */         fields[i].accept(this);
/*     */       }
/*     */ 
/* 142 */       this._out.println("  }\n");
/*     */     }
/*     */ 
/* 145 */     Method[] methods = clazz.getMethods();
/*     */ 
/* 147 */     for (int i = 0; i < methods.length; i++) {
/* 148 */       this._out.println("  private void createMethod_" + i + "() {");
/*     */ 
/* 150 */       methods[i].accept(this);
/* 151 */       this._out.println("  }\n");
/*     */     }
/*     */ 
/* 154 */     printMain();
/* 155 */     this._out.println("}");
/*     */   }
/*     */ 
/*     */   private void printCreate() {
/* 159 */     this._out.println("  public void create(OutputStream out) throws IOException {");
/*     */ 
/* 161 */     Field[] fields = this._clazz.getFields();
/* 162 */     if (fields.length > 0) {
/* 163 */       this._out.println("    createFields();");
/*     */     }
/*     */ 
/* 166 */     Method[] methods = this._clazz.getMethods();
/* 167 */     for (int i = 0; i < methods.length; i++) {
/* 168 */       this._out.println("    createMethod_" + i + "();");
/*     */     }
/*     */ 
/* 171 */     this._out.println("    _cg.getJavaClass().dump(out);");
/*     */ 
/* 173 */     this._out.println("  }\n");
/*     */   }
/*     */ 
/*     */   private void printMain() {
/* 177 */     String class_name = this._clazz.getClassName();
/*     */ 
/* 179 */     this._out.println("  public static void _main(String[] args) throws Exception {");
/* 180 */     this._out.println("    " + class_name + "Creator creator = new " + class_name + "Creator();");
/*     */ 
/* 182 */     this._out.println("    creator.create(new FileOutputStream(\"" + class_name + ".class\"));");
/*     */ 
/* 184 */     this._out.println("  }");
/*     */   }
/*     */ 
/*     */   public void visitField(Field field) {
/* 188 */     this._out.println("\n    field = new FieldGen(" + printFlags(field.getAccessFlags()) + ", " + printType(field.getSignature()) + ", \"" + field.getName() + "\", _cp);");
/*     */ 
/* 193 */     ConstantValue cv = field.getConstantValue();
/*     */ 
/* 195 */     if (cv != null) {
/* 196 */       String value = cv.toString();
/* 197 */       this._out.println("    field.setInitValue(" + value + ")");
/*     */     }
/*     */ 
/* 200 */     this._out.println("    _cg.addField(field.getField());");
/*     */   }
/*     */ 
/*     */   public void visitMethod(Method method) {
/* 204 */     MethodGen mg = new MethodGen(method, this._clazz.getClassName(), this._cp);
/*     */ 
/* 206 */     Type result_type = mg.getReturnType();
/* 207 */     Type[] arg_types = mg.getArgumentTypes();
/*     */ 
/* 209 */     this._out.println("    InstructionList il = new InstructionList();");
/* 210 */     this._out.println("    MethodGen method = new MethodGen(" + printFlags(method.getAccessFlags()) + ", " + printType(result_type) + ", " + printArgumentTypes(arg_types) + ", " + "new String[] { " + Utility.printArray(mg.getArgumentNames(), false, true) + " }, \"" + method.getName() + "\", \"" + this._clazz.getClassName() + "\", il, _cp);\n");
/*     */ 
/* 219 */     BCELFactory factory = new BCELFactory(mg, this._out);
/* 220 */     factory.start();
/*     */ 
/* 222 */     this._out.println("    method.setMaxStack();");
/* 223 */     this._out.println("    method.setMaxLocals();");
/* 224 */     this._out.println("    _cg.addMethod(method.getMethod());");
/* 225 */     this._out.println("    il.dispose();");
/*     */   }
/*     */ 
/*     */   static String printFlags(int flags) {
/* 229 */     return printFlags(flags, false);
/*     */   }
/*     */ 
/*     */   static String printFlags(int flags, boolean for_class) {
/* 233 */     if (flags == 0) {
/* 234 */       return "0";
/*     */     }
/* 236 */     StringBuffer buf = new StringBuffer();
/* 237 */     int i = 0; for (int pow = 1; i <= 2048; i++) {
/* 238 */       if ((flags & pow) != 0) {
/* 239 */         if ((pow == 32) && (for_class))
/* 240 */           buf.append("ACC_SUPER | ");
/*     */         else {
/* 242 */           buf.append("ACC_" + com.sun.org.apache.bcel.internal.Constants.ACCESS_NAMES[i].toUpperCase() + " | ");
/*     */         }
/*     */       }
/* 245 */       pow <<= 1;
/*     */     }
/*     */ 
/* 248 */     String str = buf.toString();
/* 249 */     return str.substring(0, str.length() - 3);
/*     */   }
/*     */ 
/*     */   static String printArgumentTypes(Type[] arg_types) {
/* 253 */     if (arg_types.length == 0) {
/* 254 */       return "Type.NO_ARGS";
/*     */     }
/* 256 */     StringBuffer args = new StringBuffer();
/*     */ 
/* 258 */     for (int i = 0; i < arg_types.length; i++) {
/* 259 */       args.append(printType(arg_types[i]));
/*     */ 
/* 261 */       if (i < arg_types.length - 1) {
/* 262 */         args.append(", ");
/*     */       }
/*     */     }
/* 265 */     return "new Type[] { " + args.toString() + " }";
/*     */   }
/*     */ 
/*     */   static String printType(Type type) {
/* 269 */     return printType(type.getSignature());
/*     */   }
/*     */ 
/*     */   static String printType(String signature) {
/* 273 */     Type type = Type.getType(signature);
/* 274 */     byte t = type.getType();
/*     */ 
/* 276 */     if (t <= 12)
/* 277 */       return "Type." + com.sun.org.apache.bcel.internal.Constants.TYPE_NAMES[t].toUpperCase();
/* 278 */     if (type.toString().equals("java.lang.String"))
/* 279 */       return "Type.STRING";
/* 280 */     if (type.toString().equals("java.lang.Object"))
/* 281 */       return "Type.OBJECT";
/* 282 */     if (type.toString().equals("java.lang.StringBuffer"))
/* 283 */       return "Type.STRINGBUFFER";
/* 284 */     if ((type instanceof ArrayType)) {
/* 285 */       ArrayType at = (ArrayType)type;
/*     */ 
/* 287 */       return "new ArrayType(" + printType(at.getBasicType()) + ", " + at.getDimensions() + ")";
/*     */     }
/*     */ 
/* 290 */     return "new ObjectType(\"" + Utility.signatureToString(signature, false) + "\")";
/*     */   }
/*     */ 
/*     */   public static void _main(String[] argv)
/*     */     throws Exception
/*     */   {
/* 299 */     String name = argv[0];
/*     */     JavaClass java_class;
/* 301 */     if ((java_class = Repository.lookupClass(name)) == null) {
/* 302 */       java_class = new ClassParser(name).parse();
/*     */     }
/* 304 */     BCELifier bcelifier = new BCELifier(java_class, System.out);
/* 305 */     bcelifier.start();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.BCELifier
 * JD-Core Version:    0.6.2
 */