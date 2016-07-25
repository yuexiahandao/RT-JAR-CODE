/*     */ package com.sun.org.apache.bcel.internal;
/*     */ 
/*     */ public abstract interface ExceptionConstants
/*     */ {
/*  69 */   public static final Class THROWABLE = Throwable.class;
/*     */ 
/*  73 */   public static final Class RUNTIME_EXCEPTION = RuntimeException.class;
/*     */ 
/*  77 */   public static final Class LINKING_EXCEPTION = LinkageError.class;
/*     */ 
/*  81 */   public static final Class CLASS_CIRCULARITY_ERROR = ClassCircularityError.class;
/*  82 */   public static final Class CLASS_FORMAT_ERROR = ClassFormatError.class;
/*  83 */   public static final Class EXCEPTION_IN_INITIALIZER_ERROR = ExceptionInInitializerError.class;
/*  84 */   public static final Class INCOMPATIBLE_CLASS_CHANGE_ERROR = IncompatibleClassChangeError.class;
/*  85 */   public static final Class ABSTRACT_METHOD_ERROR = AbstractMethodError.class;
/*  86 */   public static final Class ILLEGAL_ACCESS_ERROR = IllegalAccessError.class;
/*  87 */   public static final Class INSTANTIATION_ERROR = InstantiationError.class;
/*  88 */   public static final Class NO_SUCH_FIELD_ERROR = NoSuchFieldError.class;
/*  89 */   public static final Class NO_SUCH_METHOD_ERROR = NoSuchMethodError.class;
/*  90 */   public static final Class NO_CLASS_DEF_FOUND_ERROR = NoClassDefFoundError.class;
/*  91 */   public static final Class UNSATISFIED_LINK_ERROR = UnsatisfiedLinkError.class;
/*  92 */   public static final Class VERIFY_ERROR = VerifyError.class;
/*     */ 
/*  99 */   public static final Class NULL_POINTER_EXCEPTION = NullPointerException.class;
/* 100 */   public static final Class ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = ArrayIndexOutOfBoundsException.class;
/* 101 */   public static final Class ARITHMETIC_EXCEPTION = ArithmeticException.class;
/* 102 */   public static final Class NEGATIVE_ARRAY_SIZE_EXCEPTION = NegativeArraySizeException.class;
/* 103 */   public static final Class CLASS_CAST_EXCEPTION = ClassCastException.class;
/* 104 */   public static final Class ILLEGAL_MONITOR_STATE = IllegalMonitorStateException.class;
/*     */ 
/* 109 */   public static final Class[] EXCS_CLASS_AND_INTERFACE_RESOLUTION = { NO_CLASS_DEF_FOUND_ERROR, CLASS_FORMAT_ERROR, VERIFY_ERROR, ABSTRACT_METHOD_ERROR, EXCEPTION_IN_INITIALIZER_ERROR, ILLEGAL_ACCESS_ERROR };
/*     */ 
/* 114 */   public static final Class[] EXCS_FIELD_AND_METHOD_RESOLUTION = { NO_SUCH_FIELD_ERROR, ILLEGAL_ACCESS_ERROR, NO_SUCH_METHOD_ERROR };
/*     */ 
/* 118 */   public static final Class[] EXCS_INTERFACE_METHOD_RESOLUTION = new Class[0];
/* 119 */   public static final Class[] EXCS_STRING_RESOLUTION = new Class[0];
/*     */ 
/* 122 */   public static final Class[] EXCS_ARRAY_EXCEPTION = { NULL_POINTER_EXCEPTION, ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION };
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.ExceptionConstants
 * JD-Core Version:    0.6.2
 */