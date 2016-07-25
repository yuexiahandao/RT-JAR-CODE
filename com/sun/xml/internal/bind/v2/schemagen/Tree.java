/*     */ package com.sun.xml.internal.bind.v2.schemagen;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Occurs;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
/*     */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeDefParticle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class Tree
/*     */ {
/*     */   Tree makeOptional(boolean really)
/*     */   {
/*  56 */     return really ? new Optional(this, null) : this;
/*     */   }
/*     */ 
/*     */   Tree makeRepeated(boolean really)
/*     */   {
/*  67 */     return really ? new Repeated(this, null) : this;
/*     */   }
/*     */ 
/*     */   static Tree makeGroup(GroupKind kind, List<Tree> children)
/*     */   {
/*  75 */     if (children.size() == 1) {
/*  76 */       return (Tree)children.get(0);
/*     */     }
/*     */ 
/*  82 */     List normalizedChildren = new ArrayList(children.size());
/*  83 */     for (Tree t : children) {
/*  84 */       if ((t instanceof Group)) {
/*  85 */         Group g = (Group)t;
/*  86 */         if (g.kind == kind)
/*  87 */           normalizedChildren.addAll(Arrays.asList(g.children));
/*     */       }
/*     */       else
/*     */       {
/*  91 */         normalizedChildren.add(t);
/*     */       }
/*     */     }
/*  94 */     return new Group(kind, (Tree[])normalizedChildren.toArray(new Tree[normalizedChildren.size()]), null);
/*     */   }
/*     */ 
/*     */   abstract boolean isNullable();
/*     */ 
/*     */   boolean canBeTopLevel()
/*     */   {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   protected abstract void write(ContentModelContainer paramContentModelContainer, boolean paramBoolean1, boolean paramBoolean2);
/*     */ 
/*     */   protected void write(TypeDefParticle ct)
/*     */   {
/* 122 */     if (canBeTopLevel()) {
/* 123 */       write((ContentModelContainer)ct._cast(ContentModelContainer.class), false, false);
/*     */     }
/*     */     else
/* 126 */       new Group(GroupKind.SEQUENCE, new Tree[] { this }, null).write(ct);
/*     */   }
/*     */ 
/*     */   protected final void writeOccurs(Occurs o, boolean isOptional, boolean repeated)
/*     */   {
/* 133 */     if (isOptional)
/* 134 */       o.minOccurs(0);
/* 135 */     if (repeated)
/* 136 */       o.maxOccurs("unbounded");
/*     */   }
/*     */ 
/*     */   private static final class Group extends Tree
/*     */   {
/*     */     private final GroupKind kind;
/*     */     private final Tree[] children;
/*     */ 
/*     */     private Group(GroupKind kind, Tree[] children)
/*     */     {
/* 208 */       this.kind = kind;
/* 209 */       this.children = children;
/*     */     }
/*     */ 
/*     */     boolean canBeTopLevel()
/*     */     {
/* 214 */       return true;
/*     */     }
/*     */ 
/*     */     boolean isNullable()
/*     */     {
/* 219 */       if (this.kind == GroupKind.CHOICE) {
/* 220 */         for (Tree t : this.children) {
/* 221 */           if (t.isNullable())
/* 222 */             return true;
/*     */         }
/* 224 */         return false;
/*     */       }
/* 226 */       for (Tree t : this.children) {
/* 227 */         if (!t.isNullable())
/* 228 */           return false;
/*     */       }
/* 230 */       return true;
/*     */     }
/*     */ 
/*     */     protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated)
/*     */     {
/* 236 */       Particle c = this.kind.write(parent);
/* 237 */       writeOccurs(c, isOptional, repeated);
/*     */ 
/* 239 */       for (Tree child : this.children)
/* 240 */         child.write(c, false, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Optional extends Tree
/*     */   {
/*     */     private final Tree body;
/*     */ 
/*     */     private Optional(Tree body)
/*     */     {
/* 155 */       this.body = body;
/*     */     }
/*     */ 
/*     */     boolean isNullable()
/*     */     {
/* 160 */       return true;
/*     */     }
/*     */ 
/*     */     Tree makeOptional(boolean really)
/*     */     {
/* 165 */       return this;
/*     */     }
/*     */ 
/*     */     protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated)
/*     */     {
/* 170 */       this.body.write(parent, true, repeated);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class Repeated extends Tree
/*     */   {
/*     */     private final Tree body;
/*     */ 
/*     */     private Repeated(Tree body)
/*     */     {
/* 181 */       this.body = body;
/*     */     }
/*     */ 
/*     */     boolean isNullable()
/*     */     {
/* 186 */       return this.body.isNullable();
/*     */     }
/*     */ 
/*     */     Tree makeRepeated(boolean really)
/*     */     {
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */     protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated)
/*     */     {
/* 196 */       this.body.write(parent, isOptional, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class Term extends Tree
/*     */   {
/*     */     boolean isNullable()
/*     */     {
/* 144 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.Tree
 * JD-Core Version:    0.6.2
 */