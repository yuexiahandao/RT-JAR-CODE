/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class Trie
/*     */ {
/*     */   public static final int ALPHA_SIZE = 128;
/*     */   Node m_Root;
/*  42 */   private char[] m_charBuffer = new char[0];
/*     */ 
/*     */   public Trie()
/*     */   {
/*  49 */     this.m_Root = new Node();
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/*  63 */     int len = key.length();
/*  64 */     if (len > this.m_charBuffer.length)
/*     */     {
/*  67 */       this.m_charBuffer = new char[len];
/*     */     }
/*     */ 
/*  70 */     Node node = this.m_Root;
/*     */ 
/*  72 */     for (int i = 0; i < len; i++)
/*     */     {
/*  74 */       Node nextNode = node.m_nextChar[java.lang.Character.toUpperCase(key.charAt(i))];
/*     */ 
/*  76 */       if (nextNode != null)
/*     */       {
/*  78 */         node = nextNode;
/*     */       }
/*     */       else
/*     */       {
/*  82 */         for (; i < len; i++)
/*     */         {
/*  84 */           Node newNode = new Node();
/*     */ 
/*  86 */           node.m_nextChar[java.lang.Character.toUpperCase(key.charAt(i))] = newNode;
/*  87 */           node.m_nextChar[java.lang.Character.toLowerCase(key.charAt(i))] = newNode;
/*  88 */           node = newNode;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  94 */     Object ret = node.m_Value;
/*     */ 
/*  96 */     node.m_Value = value;
/*     */ 
/*  98 */     return ret;
/*     */   }
/*     */ 
/*     */   public Object get(String key)
/*     */   {
/* 111 */     int len = key.length();
/*     */ 
/* 116 */     if (this.m_charBuffer.length < len) {
/* 117 */       return null;
/*     */     }
/* 119 */     Node node = this.m_Root;
/* 120 */     switch (len)
/*     */     {
/*     */     case 0:
/* 127 */       return null;
/*     */     case 1:
/* 132 */       char ch = key.charAt(0);
/* 133 */       if (ch < '')
/*     */       {
/* 135 */         node = node.m_nextChar[ch];
/* 136 */         if (node != null)
/* 137 */           return node.m_Value;
/*     */       }
/* 139 */       return null;
/*     */     }
/*     */ 
/* 164 */     key.getChars(0, len, this.m_charBuffer, 0);
/*     */ 
/* 166 */     for (int i = 0; i < len; i++)
/*     */     {
/* 168 */       char ch = this.m_charBuffer[i];
/* 169 */       if ('' <= ch)
/*     */       {
/* 172 */         return null;
/*     */       }
/*     */ 
/* 175 */       node = node.m_nextChar[ch];
/* 176 */       if (node == null) {
/* 177 */         return null;
/*     */       }
/*     */     }
/* 180 */     return node.m_Value;
/*     */   }
/*     */ 
/*     */   class Node
/*     */   {
/*     */     Node[] m_nextChar;
/*     */     Object m_Value;
/*     */ 
/*     */     Node()
/*     */     {
/* 197 */       this.m_nextChar = new Node[''];
/* 198 */       this.m_Value = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.Trie
 * JD-Core Version:    0.6.2
 */