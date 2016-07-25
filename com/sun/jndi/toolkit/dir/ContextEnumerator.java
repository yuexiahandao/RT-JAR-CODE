/*     */ package com.sun.jndi.toolkit.dir;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public class ContextEnumerator
/*     */   implements NamingEnumeration
/*     */ {
/*  38 */   private static boolean debug = false;
/*  39 */   private NamingEnumeration children = null;
/*  40 */   private Binding currentChild = null;
/*  41 */   private boolean currentReturned = false;
/*     */   private Context root;
/*  43 */   private ContextEnumerator currentChildEnum = null;
/*  44 */   private boolean currentChildExpanded = false;
/*  45 */   private boolean rootProcessed = false;
/*  46 */   private int scope = 2;
/*  47 */   private String contextName = "";
/*     */ 
/*     */   public ContextEnumerator(Context paramContext) throws NamingException {
/*  50 */     this(paramContext, 2);
/*     */   }
/*     */ 
/*     */   public ContextEnumerator(Context paramContext, int paramInt)
/*     */     throws NamingException
/*     */   {
/*  56 */     this(paramContext, paramInt, "", paramInt != 1);
/*     */   }
/*     */ 
/*     */   protected ContextEnumerator(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*  62 */     if (paramContext == null) {
/*  63 */       throw new IllegalArgumentException("null context passed");
/*     */     }
/*     */ 
/*  66 */     this.root = paramContext;
/*     */ 
/*  69 */     if (paramInt != 0) {
/*  70 */       this.children = getImmediateChildren(paramContext);
/*     */     }
/*  72 */     this.scope = paramInt;
/*  73 */     this.contextName = paramString;
/*     */ 
/*  75 */     this.rootProcessed = (!paramBoolean);
/*  76 */     prepNextChild();
/*     */   }
/*     */ 
/*     */   protected NamingEnumeration getImmediateChildren(Context paramContext)
/*     */     throws NamingException
/*     */   {
/*  82 */     return paramContext.listBindings("");
/*     */   }
/*     */ 
/*     */   protected ContextEnumerator newEnumerator(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*  88 */     return new ContextEnumerator(paramContext, paramInt, paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean hasMore() throws NamingException {
/*  92 */     return (!this.rootProcessed) || ((this.scope != 0) && (hasMoreDescendants()));
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*     */     try {
/*  98 */       return hasMore(); } catch (NamingException localNamingException) {
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/*     */     try {
/* 106 */       return next();
/*     */     } catch (NamingException localNamingException) {
/* 108 */       throw new NoSuchElementException(localNamingException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object next() throws NamingException {
/* 113 */     if (!this.rootProcessed) {
/* 114 */       this.rootProcessed = true;
/* 115 */       return new Binding("", this.root.getClass().getName(), this.root, true);
/*     */     }
/*     */ 
/* 119 */     if ((this.scope != 0) && (hasMoreDescendants())) {
/* 120 */       return getNextDescendant();
/*     */     }
/*     */ 
/* 123 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException {
/* 127 */     this.root = null;
/*     */   }
/*     */ 
/*     */   private boolean hasMoreChildren() throws NamingException {
/* 131 */     return (this.children != null) && (this.children.hasMore());
/*     */   }
/*     */ 
/*     */   private Binding getNextChild() throws NamingException {
/* 135 */     Binding localBinding1 = (Binding)this.children.next();
/* 136 */     Binding localBinding2 = null;
/*     */ 
/* 141 */     if ((localBinding1.isRelative()) && (!this.contextName.equals(""))) {
/* 142 */       NameParser localNameParser = this.root.getNameParser("");
/* 143 */       Name localName = localNameParser.parse(this.contextName);
/* 144 */       localName.add(localBinding1.getName());
/* 145 */       if (debug) {
/* 146 */         System.out.println("ContextEnumerator: adding " + localName);
/*     */       }
/* 148 */       localBinding2 = new Binding(localName.toString(), localBinding1.getClassName(), localBinding1.getObject(), localBinding1.isRelative());
/*     */     }
/*     */     else
/*     */     {
/* 153 */       if (debug) {
/* 154 */         System.out.println("ContextEnumerator: using old binding");
/*     */       }
/* 156 */       localBinding2 = localBinding1;
/*     */     }
/*     */ 
/* 159 */     return localBinding2;
/*     */   }
/*     */ 
/*     */   private boolean hasMoreDescendants() throws NamingException
/*     */   {
/* 164 */     if (!this.currentReturned) {
/* 165 */       if (debug) System.out.println("hasMoreDescendants returning " + (this.currentChild != null));
/*     */ 
/* 167 */       return this.currentChild != null;
/* 168 */     }if ((this.currentChildExpanded) && (this.currentChildEnum.hasMore()))
/*     */     {
/* 170 */       if (debug) System.out.println("hasMoreDescendants returning true");
/*     */ 
/* 173 */       return true;
/*     */     }
/* 175 */     if (debug) System.out.println("hasMoreDescendants returning hasMoreChildren");
/*     */ 
/* 177 */     return hasMoreChildren();
/*     */   }
/*     */ 
/*     */   private Binding getNextDescendant()
/*     */     throws NamingException
/*     */   {
/* 183 */     if (!this.currentReturned)
/*     */     {
/* 185 */       if (debug) System.out.println("getNextDescedant: simple case");
/*     */ 
/* 187 */       this.currentReturned = true;
/* 188 */       return this.currentChild;
/*     */     }
/* 190 */     if ((this.currentChildExpanded) && (this.currentChildEnum.hasMore()))
/*     */     {
/* 192 */       if (debug) System.out.println("getNextDescedant: expanded case");
/*     */ 
/* 195 */       return (Binding)this.currentChildEnum.next();
/*     */     }
/*     */ 
/* 200 */     if (debug) System.out.println("getNextDescedant: next case");
/*     */ 
/* 202 */     prepNextChild();
/* 203 */     return getNextDescendant();
/*     */   }
/*     */ 
/*     */   private void prepNextChild() throws NamingException
/*     */   {
/* 208 */     if (hasMoreChildren()) {
/*     */       try {
/* 210 */         this.currentChild = getNextChild();
/* 211 */         this.currentReturned = false;
/*     */       } catch (NamingException localNamingException) {
/* 213 */         if (debug) System.out.println(localNamingException);
/* 214 */         if (debug) localNamingException.printStackTrace(); 
/*     */       }
/*     */     }
/* 217 */     else { this.currentChild = null;
/* 218 */       return;
/*     */     }
/*     */ 
/* 221 */     if ((this.scope == 2) && ((this.currentChild.getObject() instanceof Context)))
/*     */     {
/* 223 */       this.currentChildEnum = newEnumerator((Context)this.currentChild.getObject(), this.scope, this.currentChild.getName(), false);
/*     */ 
/* 227 */       this.currentChildExpanded = true;
/* 228 */       if (debug) System.out.println("prepNextChild: expanded"); 
/*     */     }
/* 230 */     else { this.currentChildExpanded = false;
/* 231 */       this.currentChildEnum = null;
/* 232 */       if (debug) System.out.println("prepNextChild: normal");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.ContextEnumerator
 * JD-Core Version:    0.6.2
 */