/*      */ package java.util.concurrent;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractMap.SimpleImmutableEntry;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ public class ConcurrentSkipListMap<K, V> extends AbstractMap<K, V>
/*      */   implements ConcurrentNavigableMap<K, V>, Cloneable, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -8627078645895051609L;
/*  329 */   private static final Random seedGenerator = new Random();
/*      */ 
/*  334 */   private static final Object BASE_HEADER = new Object();
/*      */   private volatile transient HeadIndex<K, V> head;
/*      */   private final Comparator<? super K> comparator;
/*      */   private transient int randomSeed;
/*      */   private transient KeySet keySet;
/*      */   private transient EntrySet entrySet;
/*      */   private transient Values values;
/*      */   private transient ConcurrentNavigableMap<K, V> descendingMap;
/*      */   private static final int EQ = 1;
/*      */   private static final int LT = 2;
/*      */   private static final int GT = 0;
/*      */   private static final Unsafe UNSAFE;
/*      */   private static final long headOffset;
/*      */ 
/*      */   final void initialize()
/*      */   {
/*  369 */     this.keySet = null;
/*  370 */     this.entrySet = null;
/*  371 */     this.values = null;
/*  372 */     this.descendingMap = null;
/*  373 */     this.randomSeed = (seedGenerator.nextInt() | 0x100);
/*  374 */     this.head = new HeadIndex(new Node(null, BASE_HEADER, null), null, null, 1);
/*      */   }
/*      */ 
/*      */   private boolean casHead(HeadIndex<K, V> paramHeadIndex1, HeadIndex<K, V> paramHeadIndex2)
/*      */   {
/*  382 */     return UNSAFE.compareAndSwapObject(this, headOffset, paramHeadIndex1, paramHeadIndex2);
/*      */   }
/*      */ 
/*      */   private Comparable<? super K> comparable(Object paramObject)
/*      */     throws ClassCastException
/*      */   {
/*  658 */     if (paramObject == null)
/*  659 */       throw new NullPointerException();
/*  660 */     if (this.comparator != null) {
/*  661 */       return new ComparableUsingComparator(paramObject, this.comparator);
/*      */     }
/*  663 */     return (Comparable)paramObject;
/*      */   }
/*      */ 
/*      */   int compare(K paramK1, K paramK2)
/*      */     throws ClassCastException
/*      */   {
/*  671 */     Comparator localComparator = this.comparator;
/*  672 */     if (localComparator != null) {
/*  673 */       return localComparator.compare(paramK1, paramK2);
/*      */     }
/*  675 */     return ((Comparable)paramK1).compareTo(paramK2);
/*      */   }
/*      */ 
/*      */   boolean inHalfOpenRange(K paramK1, K paramK2, K paramK3)
/*      */   {
/*  684 */     if (paramK1 == null)
/*  685 */       throw new NullPointerException();
/*  686 */     return ((paramK2 == null) || (compare(paramK1, paramK2) >= 0)) && ((paramK3 == null) || (compare(paramK1, paramK3) < 0));
/*      */   }
/*      */ 
/*      */   boolean inOpenRange(K paramK1, K paramK2, K paramK3)
/*      */   {
/*  695 */     if (paramK1 == null)
/*  696 */       throw new NullPointerException();
/*  697 */     return ((paramK2 == null) || (compare(paramK1, paramK2) >= 0)) && ((paramK3 == null) || (compare(paramK1, paramK3) <= 0));
/*      */   }
/*      */ 
/*      */   private Node<K, V> findPredecessor(Comparable<? super K> paramComparable)
/*      */   {
/*  712 */     if (paramComparable == null)
/*  713 */       throw new NullPointerException();
/*      */     while (true) {
/*  715 */       Object localObject1 = this.head;
/*  716 */       Index localIndex = ((Index)localObject1).right;
/*      */       while (true)
/*      */       {
/*      */         Object localObject2;
/*  718 */         if (localIndex != null) {
/*  719 */           localObject2 = localIndex.node;
/*  720 */           Object localObject3 = ((Node)localObject2).key;
/*  721 */           if (((Node)localObject2).value == null) {
/*  722 */             if (!((Index)localObject1).unlink(localIndex))
/*      */               break;
/*  724 */             localIndex = ((Index)localObject1).right;
/*      */           }
/*  727 */           else if (paramComparable.compareTo(localObject3) > 0) {
/*  728 */             localObject1 = localIndex;
/*  729 */             localIndex = localIndex.right;
/*      */           }
/*      */         }
/*      */         else {
/*  733 */           localObject2 = ((Index)localObject1).down;
/*  734 */           if (localObject2 != null) {
/*  735 */             localObject1 = localObject2;
/*  736 */             localIndex = ((Index)localObject2).right;
/*      */           } else {
/*  738 */             return ((Index)localObject1).node;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node<K, V> findNode(Comparable<? super K> paramComparable)
/*      */   {
/*      */     while (true)
/*      */     {
/*  789 */       Object localObject1 = findPredecessor(paramComparable);
/*  790 */       Object localObject2 = ((Node)localObject1).next;
/*      */       while (true) {
/*  792 */         if (localObject2 == null)
/*  793 */           return null;
/*  794 */         Node localNode = ((Node)localObject2).next;
/*  795 */         if (localObject2 != ((Node)localObject1).next)
/*      */           break;
/*  797 */         Object localObject3 = ((Node)localObject2).value;
/*  798 */         if (localObject3 == null) {
/*  799 */           ((Node)localObject2).helpDelete((Node)localObject1, localNode);
/*  800 */           break;
/*      */         }
/*  802 */         if ((localObject3 == localObject2) || (((Node)localObject1).value == null))
/*      */           break;
/*  804 */         int i = paramComparable.compareTo(((Node)localObject2).key);
/*  805 */         if (i == 0)
/*  806 */           return localObject2;
/*  807 */         if (i < 0)
/*  808 */           return null;
/*  809 */         localObject1 = localObject2;
/*  810 */         localObject2 = localNode;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private V doGet(Object paramObject)
/*      */   {
/*  821 */     Comparable localComparable = comparable(paramObject);
/*      */     while (true)
/*      */     {
/*  828 */       Node localNode = findNode(localComparable);
/*  829 */       if (localNode == null)
/*  830 */         return null;
/*  831 */       Object localObject = localNode.value;
/*  832 */       if (localObject != null)
/*  833 */         return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   private V doPut(K paramK, V paramV, boolean paramBoolean)
/*      */   {
/*  848 */     Comparable localComparable = comparable(paramK);
/*      */     label191: 
/*      */     while (true)
/*      */     {
/*  850 */       Object localObject1 = findPredecessor(localComparable);
/*  851 */       Object localObject2 = ((Node)localObject1).next;
/*      */ 
/*  853 */       while (localObject2 != null) {
/*  854 */         localNode = ((Node)localObject2).next;
/*  855 */         if (localObject2 != ((Node)localObject1).next)
/*      */           break label191;
/*  857 */         Object localObject3 = ((Node)localObject2).value;
/*  858 */         if (localObject3 == null) {
/*  859 */           ((Node)localObject2).helpDelete((Node)localObject1, localNode);
/*  860 */           break label191;
/*      */         }
/*  862 */         if ((localObject3 == localObject2) || (((Node)localObject1).value == null))
/*      */           break label191;
/*  864 */         int j = localComparable.compareTo(((Node)localObject2).key);
/*  865 */         if (j > 0) {
/*  866 */           localObject1 = localObject2;
/*  867 */           localObject2 = localNode;
/*      */         }
/*  870 */         else if (j == 0) {
/*  871 */           if ((!paramBoolean) && (!((Node)localObject2).casValue(localObject3, paramV))) break label191;
/*  872 */           return localObject3;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  879 */       Node localNode = new Node(paramK, paramV, (Node)localObject2);
/*  880 */       if (((Node)localObject1).casNext((Node)localObject2, localNode))
/*      */       {
/*  882 */         int i = randomLevel();
/*  883 */         if (i > 0)
/*  884 */           insertIndex(localNode, i);
/*  885 */         return null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int randomLevel()
/*      */   {
/*  900 */     int i = this.randomSeed;
/*  901 */     i ^= i << 13;
/*  902 */     i ^= i >>> 17;
/*  903 */     this.randomSeed = (i ^= i << 5);
/*  904 */     if ((i & 0x80000001) != 0)
/*  905 */       return 0;
/*  906 */     int j = 1;
/*  907 */     while ((i >>>= 1 & 0x1) != 0) j++;
/*  908 */     return j;
/*      */   }
/*      */ 
/*      */   private void insertIndex(Node<K, V> paramNode, int paramInt)
/*      */   {
/*  917 */     HeadIndex localHeadIndex1 = this.head;
/*  918 */     int i = localHeadIndex1.level;
/*      */     Object localObject;
/*  920 */     if (paramInt <= i) {
/*  921 */       localObject = null;
/*  922 */       for (int j = 1; j <= paramInt; j++)
/*  923 */         localObject = new Index(paramNode, (Index)localObject, null);
/*  924 */       addIndex((Index)localObject, localHeadIndex1, paramInt);
/*      */     }
/*      */     else
/*      */     {
/*  935 */       paramInt = i + 1;
/*  936 */       localObject = (Index[])new Index[paramInt + 1];
/*  937 */       Index localIndex = null;
/*  938 */       for (int k = 1; k <= paramInt; k++)
/*      */       {
/*      */         void tmp102_99 = new Index(paramNode, localIndex, null); localIndex = tmp102_99; localObject[k] = tmp102_99;
/*      */       }HeadIndex localHeadIndex2;
/*      */       int m;
/*      */       while (true) {
/*  944 */         localHeadIndex2 = this.head;
/*  945 */         int n = localHeadIndex2.level;
/*  946 */         if (paramInt <= n) {
/*  947 */           m = paramInt;
/*  948 */           break;
/*      */         }
/*  950 */         HeadIndex localHeadIndex3 = localHeadIndex2;
/*  951 */         Node localNode = localHeadIndex2.node;
/*  952 */         for (int i1 = n + 1; i1 <= paramInt; i1++)
/*  953 */           localHeadIndex3 = new HeadIndex(localNode, localHeadIndex3, localObject[i1], i1);
/*  954 */         if (casHead(localHeadIndex2, localHeadIndex3)) {
/*  955 */           m = n;
/*  956 */           break;
/*      */         }
/*      */       }
/*  959 */       addIndex(localObject[m], localHeadIndex2, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void addIndex(Index<K, V> paramIndex, HeadIndex<K, V> paramHeadIndex, int paramInt)
/*      */   {
/*  972 */     int i = paramInt;
/*  973 */     Comparable localComparable = comparable(paramIndex.node.key);
/*  974 */     if (localComparable == null) throw new NullPointerException();
/*      */ 
/*      */     while (true)
/*      */     {
/*  979 */       int j = paramHeadIndex.level;
/*  980 */       Object localObject1 = paramHeadIndex;
/*  981 */       Index localIndex = ((Index)localObject1).right;
/*  982 */       Object localObject2 = paramIndex;
/*      */       while (true)
/*  984 */         if (localIndex != null) {
/*  985 */           Node localNode = localIndex.node;
/*      */ 
/*  987 */           int k = localComparable.compareTo(localNode.key);
/*  988 */           if (localNode.value == null) {
/*  989 */             if (!((Index)localObject1).unlink(localIndex))
/*      */               break;
/*  991 */             localIndex = ((Index)localObject1).right;
/*      */           }
/*  994 */           else if (k > 0) {
/*  995 */             localObject1 = localIndex;
/*  996 */             localIndex = localIndex.right;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1001 */           if (j == i)
/*      */           {
/* 1003 */             if (((Index)localObject2).indexesDeletedNode()) {
/* 1004 */               findNode(localComparable);
/* 1005 */               return;
/*      */             }
/* 1007 */             if (!((Index)localObject1).link(localIndex, (Index)localObject2))
/*      */               break;
/* 1009 */             i--; if (i == 0)
/*      */             {
/* 1011 */               if (((Index)localObject2).indexesDeletedNode())
/* 1012 */                 findNode(localComparable);
/* 1013 */               return;
/*      */             }
/*      */           }
/*      */ 
/* 1017 */           j--; if ((j >= i) && (j < paramInt))
/* 1018 */             localObject2 = ((Index)localObject2).down;
/* 1019 */           localObject1 = ((Index)localObject1).down;
/* 1020 */           localIndex = ((Index)localObject1).right;
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   final V doRemove(Object paramObject1, Object paramObject2)
/*      */   {
/* 1047 */     Comparable localComparable = comparable(paramObject1);
/*      */     label208: 
/*      */     while (true)
/*      */     {
/* 1049 */       Object localObject1 = findPredecessor(localComparable);
/* 1050 */       Object localObject2 = ((Node)localObject1).next;
/*      */       Node localNode;
/*      */       Object localObject3;
/*      */       while (true)
/*      */       {
/* 1052 */         if (localObject2 == null)
/* 1053 */           return null;
/* 1054 */         localNode = ((Node)localObject2).next;
/* 1055 */         if (localObject2 != ((Node)localObject1).next)
/*      */           break label208;
/* 1057 */         localObject3 = ((Node)localObject2).value;
/* 1058 */         if (localObject3 == null) {
/* 1059 */           ((Node)localObject2).helpDelete((Node)localObject1, localNode);
/* 1060 */           break label208;
/*      */         }
/* 1062 */         if ((localObject3 == localObject2) || (((Node)localObject1).value == null))
/*      */           break label208;
/* 1064 */         int i = localComparable.compareTo(((Node)localObject2).key);
/* 1065 */         if (i < 0)
/* 1066 */           return null;
/* 1067 */         if (i <= 0) break;
/* 1068 */         localObject1 = localObject2;
/* 1069 */         localObject2 = localNode;
/*      */       }
/*      */ 
/* 1072 */       if ((paramObject2 != null) && (!paramObject2.equals(localObject3)))
/* 1073 */         return null;
/* 1074 */       if (((Node)localObject2).casValue(localObject3, null))
/*      */       {
/* 1076 */         if ((!((Node)localObject2).appendMarker(localNode)) || (!((Node)localObject1).casNext((Node)localObject2, localNode))) {
/* 1077 */           findNode(localComparable);
/*      */         } else {
/* 1079 */           findPredecessor(localComparable);
/* 1080 */           if (this.head.right == null)
/* 1081 */             tryReduceLevel();
/*      */         }
/* 1083 */         return localObject3;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void tryReduceLevel()
/*      */   {
/* 1109 */     HeadIndex localHeadIndex1 = this.head;
/*      */     HeadIndex localHeadIndex2;
/*      */     HeadIndex localHeadIndex3;
/* 1112 */     if ((localHeadIndex1.level > 3) && ((localHeadIndex2 = (HeadIndex)localHeadIndex1.down) != null) && ((localHeadIndex3 = (HeadIndex)localHeadIndex2.down) != null) && (localHeadIndex3.right == null) && (localHeadIndex2.right == null) && (localHeadIndex1.right == null) && (casHead(localHeadIndex1, localHeadIndex2)) && (localHeadIndex1.right != null))
/*      */     {
/* 1120 */       casHead(localHeadIndex2, localHeadIndex1);
/*      */     }
/*      */   }
/*      */ 
/*      */   Node<K, V> findFirst()
/*      */   {
/*      */     while (true)
/*      */     {
/* 1131 */       Node localNode1 = this.head.node;
/* 1132 */       Node localNode2 = localNode1.next;
/* 1133 */       if (localNode2 == null)
/* 1134 */         return null;
/* 1135 */       if (localNode2.value != null)
/* 1136 */         return localNode2;
/* 1137 */       localNode2.helpDelete(localNode1, localNode2.next);
/*      */     }
/*      */   }
/*      */   Map.Entry<K, V> doRemoveFirstEntry() { Node localNode1;
/*      */     Node localNode2;
/*      */     Node localNode3;
/*      */     Object localObject;
/*      */     do while (true) { localNode1 = this.head.node;
/* 1148 */         localNode2 = localNode1.next;
/* 1149 */         if (localNode2 == null)
/* 1150 */           return null;
/* 1151 */         localNode3 = localNode2.next;
/* 1152 */         if (localNode2 == localNode1.next)
/*      */         {
/* 1154 */           localObject = localNode2.value;
/* 1155 */           if (localObject != null) break;
/* 1156 */           localNode2.helpDelete(localNode1, localNode3);
/*      */         }
/*      */       }
/* 1159 */     while (!localNode2.casValue(localObject, null));
/*      */ 
/* 1161 */     if ((!localNode2.appendMarker(localNode3)) || (!localNode1.casNext(localNode2, localNode3)))
/* 1162 */       findFirst();
/* 1163 */     clearIndexToFirst();
/* 1164 */     return new AbstractMap.SimpleImmutableEntry(localNode2.key, localObject);
/*      */   }
/*      */ 
/*      */   private void clearIndexToFirst()
/*      */   {
/*      */     while (true)
/*      */     {
/* 1173 */       Object localObject = this.head;
/*      */       while (true) {
/* 1175 */         Index localIndex = ((Index)localObject).right;
/* 1176 */         if ((localIndex != null) && (localIndex.indexesDeletedNode()) && (!((Index)localObject).unlink(localIndex)))
/*      */           break;
/* 1178 */         if ((localObject = ((Index)localObject).down) == null) {
/* 1179 */           if (this.head.right == null)
/* 1180 */             tryReduceLevel();
/* 1181 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Node<K, V> findLast()
/*      */   {
/* 1200 */     Object localObject1 = this.head;
/*      */     while (true)
/*      */     {
/*      */       Index localIndex2;
/* 1203 */       if ((localIndex2 = ((Index)localObject1).right) != null) {
/* 1204 */         if (localIndex2.indexesDeletedNode()) {
/* 1205 */           ((Index)localObject1).unlink(localIndex2);
/* 1206 */           localObject1 = this.head;
/*      */         }
/*      */         else {
/* 1209 */           localObject1 = localIndex2;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         Index localIndex1;
/* 1210 */         if ((localIndex1 = ((Index)localObject1).down) != null) {
/* 1211 */           localObject1 = localIndex1;
/*      */         } else {
/* 1213 */           Object localObject2 = ((Index)localObject1).node;
/* 1214 */           Object localObject3 = ((Node)localObject2).next;
/*      */           while (true) {
/* 1216 */             if (localObject3 == null)
/* 1217 */               return ((Node)localObject2).isBaseHeader() ? null : localObject2;
/* 1218 */             Node localNode = ((Node)localObject3).next;
/* 1219 */             if (localObject3 != ((Node)localObject2).next)
/*      */               break;
/* 1221 */             Object localObject4 = ((Node)localObject3).value;
/* 1222 */             if (localObject4 == null) {
/* 1223 */               ((Node)localObject3).helpDelete((Node)localObject2, localNode);
/* 1224 */               break;
/*      */             }
/* 1226 */             if ((localObject4 == localObject3) || (((Node)localObject2).value == null))
/*      */               break;
/* 1228 */             localObject2 = localObject3;
/* 1229 */             localObject3 = localNode;
/*      */           }
/* 1231 */           localObject1 = this.head;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Node<K, V> findPredecessorOfLast()
/*      */   {
/*      */     while (true)
/*      */     {
/* 1245 */       Object localObject = this.head;
/*      */       while (true)
/*      */       {
/*      */         Index localIndex2;
/* 1248 */         if ((localIndex2 = ((Index)localObject).right) != null) {
/* 1249 */           if (localIndex2.indexesDeletedNode()) {
/* 1250 */             ((Index)localObject).unlink(localIndex2);
/* 1251 */             break;
/*      */           }
/*      */ 
/* 1254 */           if (localIndex2.node.next != null)
/* 1255 */             localObject = localIndex2;
/*      */         }
/*      */         else
/*      */         {
/*      */           Index localIndex1;
/* 1259 */           if ((localIndex1 = ((Index)localObject).down) != null)
/* 1260 */             localObject = localIndex1;
/*      */           else
/* 1262 */             return ((Index)localObject).node;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Map.Entry<K, V> doRemoveLastEntry()
/*      */   {
/*      */     label173: 
/*      */     while (true)
/*      */     {
/* 1274 */       Object localObject1 = findPredecessorOfLast();
/* 1275 */       Object localObject2 = ((Node)localObject1).next;
/* 1276 */       if (localObject2 == null) {
/* 1277 */         if (((Node)localObject1).isBaseHeader())
/* 1278 */           return null; 
/*      */       } else {
/*      */         Node localNode;
/*      */         Object localObject3;
/*      */         while (true) { localNode = ((Node)localObject2).next;
/* 1284 */           if (localObject2 != ((Node)localObject1).next)
/*      */             break label173;
/* 1286 */           localObject3 = ((Node)localObject2).value;
/* 1287 */           if (localObject3 == null) {
/* 1288 */             ((Node)localObject2).helpDelete((Node)localObject1, localNode);
/* 1289 */             break label173;
/*      */           }
/* 1291 */           if ((localObject3 == localObject2) || (((Node)localObject1).value == null))
/*      */             break label173;
/* 1293 */           if (localNode == null) break;
/* 1294 */           localObject1 = localObject2;
/* 1295 */           localObject2 = localNode;
/*      */         }
/*      */ 
/* 1298 */         if (((Node)localObject2).casValue(localObject3, null))
/*      */         {
/* 1300 */           Object localObject4 = ((Node)localObject2).key;
/* 1301 */           Comparable localComparable = comparable(localObject4);
/* 1302 */           if ((!((Node)localObject2).appendMarker(localNode)) || (!((Node)localObject1).casNext((Node)localObject2, localNode))) {
/* 1303 */             findNode(localComparable);
/*      */           } else {
/* 1305 */             findPredecessor(localComparable);
/* 1306 */             if (this.head.right == null)
/* 1307 */               tryReduceLevel();
/*      */           }
/* 1309 */           return new AbstractMap.SimpleImmutableEntry(localObject4, localObject3);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   Node<K, V> findNear(K paramK, int paramInt)
/*      */   {
/* 1329 */     Comparable localComparable = comparable(paramK);
/*      */     while (true) {
/* 1331 */       Object localObject1 = findPredecessor(localComparable);
/* 1332 */       Object localObject2 = ((Node)localObject1).next;
/*      */       while (true) {
/* 1334 */         if (localObject2 == null)
/* 1335 */           return ((paramInt & 0x2) == 0) || (((Node)localObject1).isBaseHeader()) ? null : localObject1;
/* 1336 */         Node localNode = ((Node)localObject2).next;
/* 1337 */         if (localObject2 != ((Node)localObject1).next)
/*      */           break;
/* 1339 */         Object localObject3 = ((Node)localObject2).value;
/* 1340 */         if (localObject3 == null) {
/* 1341 */           ((Node)localObject2).helpDelete((Node)localObject1, localNode);
/* 1342 */           break;
/*      */         }
/* 1344 */         if ((localObject3 == localObject2) || (((Node)localObject1).value == null))
/*      */           break;
/* 1346 */         int i = localComparable.compareTo(((Node)localObject2).key);
/* 1347 */         if (((i == 0) && ((paramInt & 0x1) != 0)) || ((i < 0) && ((paramInt & 0x2) == 0)))
/*      */         {
/* 1349 */           return localObject2;
/* 1350 */         }if ((i <= 0) && ((paramInt & 0x2) != 0))
/* 1351 */           return ((Node)localObject1).isBaseHeader() ? null : localObject1;
/* 1352 */         localObject1 = localObject2;
/* 1353 */         localObject2 = localNode;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   AbstractMap.SimpleImmutableEntry<K, V> getNear(K paramK, int paramInt)
/*      */   {
/*      */     while (true)
/*      */     {
/* 1366 */       Node localNode = findNear(paramK, paramInt);
/* 1367 */       if (localNode == null)
/* 1368 */         return null;
/* 1369 */       AbstractMap.SimpleImmutableEntry localSimpleImmutableEntry = localNode.createSnapshot();
/* 1370 */       if (localSimpleImmutableEntry != null)
/* 1371 */         return localSimpleImmutableEntry;
/*      */     }
/*      */   }
/*      */ 
/*      */   public ConcurrentSkipListMap()
/*      */   {
/* 1383 */     this.comparator = null;
/* 1384 */     initialize();
/*      */   }
/*      */ 
/*      */   public ConcurrentSkipListMap(Comparator<? super K> paramComparator)
/*      */   {
/* 1396 */     this.comparator = paramComparator;
/* 1397 */     initialize();
/*      */   }
/*      */ 
/*      */   public ConcurrentSkipListMap(Map<? extends K, ? extends V> paramMap)
/*      */   {
/* 1412 */     this.comparator = null;
/* 1413 */     initialize();
/* 1414 */     putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public ConcurrentSkipListMap(SortedMap<K, ? extends V> paramSortedMap)
/*      */   {
/* 1427 */     this.comparator = paramSortedMap.comparator();
/* 1428 */     initialize();
/* 1429 */     buildFromSorted(paramSortedMap);
/*      */   }
/*      */ 
/*      */   public ConcurrentSkipListMap<K, V> clone()
/*      */   {
/* 1439 */     ConcurrentSkipListMap localConcurrentSkipListMap = null;
/*      */     try {
/* 1441 */       localConcurrentSkipListMap = (ConcurrentSkipListMap)super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 1443 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1446 */     localConcurrentSkipListMap.initialize();
/* 1447 */     localConcurrentSkipListMap.buildFromSorted(this);
/* 1448 */     return localConcurrentSkipListMap;
/*      */   }
/*      */ 
/*      */   private void buildFromSorted(SortedMap<K, ? extends V> paramSortedMap)
/*      */   {
/* 1457 */     if (paramSortedMap == null) {
/* 1458 */       throw new NullPointerException();
/*      */     }
/* 1460 */     HeadIndex localHeadIndex = this.head;
/* 1461 */     Object localObject1 = localHeadIndex.node;
/*      */ 
/* 1465 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1468 */     for (int i = 0; i <= localHeadIndex.level; i++)
/* 1469 */       localArrayList.add(null);
/* 1470 */     Object localObject2 = localHeadIndex;
/* 1471 */     for (int j = localHeadIndex.level; j > 0; j--) {
/* 1472 */       localArrayList.set(j, localObject2);
/* 1473 */       localObject2 = ((Index)localObject2).down;
/*      */     }
/*      */ 
/* 1476 */     Iterator localIterator = paramSortedMap.entrySet().iterator();
/*      */ 
/* 1478 */     while (localIterator.hasNext()) {
/* 1479 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 1480 */       int k = randomLevel();
/* 1481 */       if (k > localHeadIndex.level) k = localHeadIndex.level + 1;
/* 1482 */       Object localObject3 = localEntry.getKey();
/* 1483 */       Object localObject4 = localEntry.getValue();
/* 1484 */       if ((localObject3 == null) || (localObject4 == null))
/* 1485 */         throw new NullPointerException();
/* 1486 */       Node localNode = new Node(localObject3, localObject4, null);
/* 1487 */       ((Node)localObject1).next = localNode;
/* 1488 */       localObject1 = localNode;
/* 1489 */       if (k > 0) {
/* 1490 */         Index localIndex = null;
/* 1491 */         for (int m = 1; m <= k; m++) {
/* 1492 */           localIndex = new Index(localNode, localIndex, null);
/* 1493 */           if (m > localHeadIndex.level) {
/* 1494 */             localHeadIndex = new HeadIndex(localHeadIndex.node, localHeadIndex, localIndex, m);
/*      */           }
/* 1496 */           if (m < localArrayList.size()) {
/* 1497 */             ((Index)localArrayList.get(m)).right = localIndex;
/* 1498 */             localArrayList.set(m, localIndex);
/*      */           } else {
/* 1500 */             localArrayList.add(localIndex);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1504 */     this.head = localHeadIndex;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1521 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1524 */     for (Node localNode = findFirst(); localNode != null; localNode = localNode.next) {
/* 1525 */       Object localObject = localNode.getValidValue();
/* 1526 */       if (localObject != null) {
/* 1527 */         paramObjectOutputStream.writeObject(localNode.key);
/* 1528 */         paramObjectOutputStream.writeObject(localObject);
/*      */       }
/*      */     }
/* 1531 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1540 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1542 */     initialize();
/*      */ 
/* 1552 */     HeadIndex localHeadIndex = this.head;
/* 1553 */     Object localObject1 = localHeadIndex.node;
/* 1554 */     ArrayList localArrayList = new ArrayList();
/* 1555 */     for (int i = 0; i <= localHeadIndex.level; i++)
/* 1556 */       localArrayList.add(null);
/* 1557 */     Object localObject2 = localHeadIndex;
/* 1558 */     for (int j = localHeadIndex.level; j > 0; j--) {
/* 1559 */       localArrayList.set(j, localObject2);
/* 1560 */       localObject2 = ((Index)localObject2).down;
/*      */     }
/*      */     while (true)
/*      */     {
/* 1564 */       Object localObject3 = paramObjectInputStream.readObject();
/* 1565 */       if (localObject3 == null)
/*      */         break;
/* 1567 */       Object localObject4 = paramObjectInputStream.readObject();
/* 1568 */       if (localObject4 == null)
/* 1569 */         throw new NullPointerException();
/* 1570 */       Object localObject5 = localObject3;
/* 1571 */       Object localObject6 = localObject4;
/* 1572 */       int k = randomLevel();
/* 1573 */       if (k > localHeadIndex.level) k = localHeadIndex.level + 1;
/* 1574 */       Node localNode = new Node(localObject5, localObject6, null);
/* 1575 */       ((Node)localObject1).next = localNode;
/* 1576 */       localObject1 = localNode;
/* 1577 */       if (k > 0) {
/* 1578 */         Index localIndex = null;
/* 1579 */         for (int m = 1; m <= k; m++) {
/* 1580 */           localIndex = new Index(localNode, localIndex, null);
/* 1581 */           if (m > localHeadIndex.level) {
/* 1582 */             localHeadIndex = new HeadIndex(localHeadIndex.node, localHeadIndex, localIndex, m);
/*      */           }
/* 1584 */           if (m < localArrayList.size()) {
/* 1585 */             ((Index)localArrayList.get(m)).right = localIndex;
/* 1586 */             localArrayList.set(m, localIndex);
/*      */           } else {
/* 1588 */             localArrayList.add(localIndex);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1592 */     this.head = localHeadIndex;
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/* 1608 */     return doGet(paramObject) != null;
/*      */   }
/*      */ 
/*      */   public V get(Object paramObject)
/*      */   {
/* 1626 */     return doGet(paramObject);
/*      */   }
/*      */ 
/*      */   public V put(K paramK, V paramV)
/*      */   {
/* 1643 */     if (paramV == null)
/* 1644 */       throw new NullPointerException();
/* 1645 */     return doPut(paramK, paramV, false);
/*      */   }
/*      */ 
/*      */   public V remove(Object paramObject)
/*      */   {
/* 1659 */     return doRemove(paramObject, null);
/*      */   }
/*      */ 
/*      */   public boolean containsValue(Object paramObject)
/*      */   {
/* 1675 */     if (paramObject == null)
/* 1676 */       throw new NullPointerException();
/* 1677 */     for (Node localNode = findFirst(); localNode != null; localNode = localNode.next) {
/* 1678 */       Object localObject = localNode.getValidValue();
/* 1679 */       if ((localObject != null) && (paramObject.equals(localObject)))
/* 1680 */         return true;
/*      */     }
/* 1682 */     return false;
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/* 1702 */     long l = 0L;
/* 1703 */     for (Node localNode = findFirst(); localNode != null; localNode = localNode.next) {
/* 1704 */       if (localNode.getValidValue() != null)
/* 1705 */         l += 1L;
/*      */     }
/* 1707 */     return l >= 2147483647L ? 2147483647 : (int)l;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1715 */     return findFirst() == null;
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/* 1722 */     initialize();
/*      */   }
/*      */ 
/*      */   public NavigableSet<K> keySet()
/*      */   {
/* 1758 */     KeySet localKeySet = this.keySet;
/* 1759 */     return this.keySet = new KeySet(this);
/*      */   }
/*      */ 
/*      */   public NavigableSet<K> navigableKeySet() {
/* 1763 */     KeySet localKeySet = this.keySet;
/* 1764 */     return this.keySet = new KeySet(this);
/*      */   }
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/* 1786 */     Values localValues = this.values;
/* 1787 */     return this.values = new Values(this);
/*      */   }
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 1815 */     EntrySet localEntrySet = this.entrySet;
/* 1816 */     return this.entrySet = new EntrySet(this);
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> descendingMap() {
/* 1820 */     ConcurrentNavigableMap localConcurrentNavigableMap = this.descendingMap;
/* 1821 */     return this.descendingMap = new SubMap(this, null, false, null, false, true);
/*      */   }
/*      */ 
/*      */   public NavigableSet<K> descendingKeySet()
/*      */   {
/* 1826 */     return descendingMap().navigableKeySet();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1844 */     if (paramObject == this)
/* 1845 */       return true;
/* 1846 */     if (!(paramObject instanceof Map))
/* 1847 */       return false;
/* 1848 */     Map localMap = (Map)paramObject;
/*      */     try {
/* 1850 */       for (Iterator localIterator = entrySet().iterator(); localIterator.hasNext(); ) { localEntry = (Map.Entry)localIterator.next();
/* 1851 */         if (!localEntry.getValue().equals(localMap.get(localEntry.getKey())))
/* 1852 */           return false;
/*      */       }
/* 1853 */       Map.Entry localEntry;
/* 1853 */       for (localIterator = localMap.entrySet().iterator(); localIterator.hasNext(); ) { localEntry = (Map.Entry)localIterator.next();
/* 1854 */         Object localObject1 = localEntry.getKey();
/* 1855 */         Object localObject2 = localEntry.getValue();
/* 1856 */         if ((localObject1 == null) || (localObject2 == null) || (!localObject2.equals(get(localObject1))))
/* 1857 */           return false;
/*      */       }
/* 1859 */       return true;
/*      */     } catch (ClassCastException localClassCastException) {
/* 1861 */       return false; } catch (NullPointerException localNullPointerException) {
/*      */     }
/* 1863 */     return false;
/*      */   }
/*      */ 
/*      */   public V putIfAbsent(K paramK, V paramV)
/*      */   {
/* 1879 */     if (paramV == null)
/* 1880 */       throw new NullPointerException();
/* 1881 */     return doPut(paramK, paramV, true);
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject1, Object paramObject2)
/*      */   {
/* 1892 */     if (paramObject1 == null)
/* 1893 */       throw new NullPointerException();
/* 1894 */     if (paramObject2 == null)
/* 1895 */       return false;
/* 1896 */     return doRemove(paramObject1, paramObject2) != null;
/*      */   }
/*      */ 
/*      */   public boolean replace(K paramK, V paramV1, V paramV2)
/*      */   {
/* 1907 */     if ((paramV1 == null) || (paramV2 == null))
/* 1908 */       throw new NullPointerException();
/* 1909 */     Comparable localComparable = comparable(paramK);
/*      */     while (true) {
/* 1911 */       Node localNode = findNode(localComparable);
/* 1912 */       if (localNode == null)
/* 1913 */         return false;
/* 1914 */       Object localObject = localNode.value;
/* 1915 */       if (localObject != null) {
/* 1916 */         if (!paramV1.equals(localObject))
/* 1917 */           return false;
/* 1918 */         if (localNode.casValue(localObject, paramV2))
/* 1919 */           return true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public V replace(K paramK, V paramV)
/*      */   {
/* 1934 */     if (paramV == null)
/* 1935 */       throw new NullPointerException();
/* 1936 */     Comparable localComparable = comparable(paramK);
/*      */     while (true) {
/* 1938 */       Node localNode = findNode(localComparable);
/* 1939 */       if (localNode == null)
/* 1940 */         return null;
/* 1941 */       Object localObject = localNode.value;
/* 1942 */       if ((localObject != null) && (localNode.casValue(localObject, paramV)))
/* 1943 */         return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Comparator<? super K> comparator()
/*      */   {
/* 1950 */     return this.comparator;
/*      */   }
/*      */ 
/*      */   public K firstKey()
/*      */   {
/* 1957 */     Node localNode = findFirst();
/* 1958 */     if (localNode == null)
/* 1959 */       throw new NoSuchElementException();
/* 1960 */     return localNode.key;
/*      */   }
/*      */ 
/*      */   public K lastKey()
/*      */   {
/* 1967 */     Node localNode = findLast();
/* 1968 */     if (localNode == null)
/* 1969 */       throw new NoSuchElementException();
/* 1970 */     return localNode.key;
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
/*      */   {
/* 1982 */     if ((paramK1 == null) || (paramK2 == null))
/* 1983 */       throw new NullPointerException();
/* 1984 */     return new SubMap(this, paramK1, paramBoolean1, paramK2, paramBoolean2, false);
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> headMap(K paramK, boolean paramBoolean)
/*      */   {
/* 1995 */     if (paramK == null)
/* 1996 */       throw new NullPointerException();
/* 1997 */     return new SubMap(this, null, false, paramK, paramBoolean, false);
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> tailMap(K paramK, boolean paramBoolean)
/*      */   {
/* 2008 */     if (paramK == null)
/* 2009 */       throw new NullPointerException();
/* 2010 */     return new SubMap(this, paramK, paramBoolean, null, false, false);
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> subMap(K paramK1, K paramK2)
/*      */   {
/* 2020 */     return subMap(paramK1, true, paramK2, false);
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> headMap(K paramK)
/*      */   {
/* 2029 */     return headMap(paramK, false);
/*      */   }
/*      */ 
/*      */   public ConcurrentNavigableMap<K, V> tailMap(K paramK)
/*      */   {
/* 2038 */     return tailMap(paramK, true);
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> lowerEntry(K paramK)
/*      */   {
/* 2053 */     return getNear(paramK, 2);
/*      */   }
/*      */ 
/*      */   public K lowerKey(K paramK)
/*      */   {
/* 2061 */     Node localNode = findNear(paramK, 2);
/* 2062 */     return localNode == null ? null : localNode.key;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> floorEntry(K paramK)
/*      */   {
/* 2076 */     return getNear(paramK, 3);
/*      */   }
/*      */ 
/*      */   public K floorKey(K paramK)
/*      */   {
/* 2085 */     Node localNode = findNear(paramK, 3);
/* 2086 */     return localNode == null ? null : localNode.key;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> ceilingEntry(K paramK)
/*      */   {
/* 2099 */     return getNear(paramK, 1);
/*      */   }
/*      */ 
/*      */   public K ceilingKey(K paramK)
/*      */   {
/* 2107 */     Node localNode = findNear(paramK, 1);
/* 2108 */     return localNode == null ? null : localNode.key;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> higherEntry(K paramK)
/*      */   {
/* 2122 */     return getNear(paramK, 0);
/*      */   }
/*      */ 
/*      */   public K higherKey(K paramK)
/*      */   {
/* 2131 */     Node localNode = findNear(paramK, 0);
/* 2132 */     return localNode == null ? null : localNode.key;
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> firstEntry()
/*      */   {
/*      */     while (true)
/*      */     {
/* 2143 */       Node localNode = findFirst();
/* 2144 */       if (localNode == null)
/* 2145 */         return null;
/* 2146 */       AbstractMap.SimpleImmutableEntry localSimpleImmutableEntry = localNode.createSnapshot();
/* 2147 */       if (localSimpleImmutableEntry != null)
/* 2148 */         return localSimpleImmutableEntry;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> lastEntry()
/*      */   {
/*      */     while (true)
/*      */     {
/* 2160 */       Node localNode = findLast();
/* 2161 */       if (localNode == null)
/* 2162 */         return null;
/* 2163 */       AbstractMap.SimpleImmutableEntry localSimpleImmutableEntry = localNode.createSnapshot();
/* 2164 */       if (localSimpleImmutableEntry != null)
/* 2165 */         return localSimpleImmutableEntry;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> pollFirstEntry()
/*      */   {
/* 2176 */     return doRemoveFirstEntry();
/*      */   }
/*      */ 
/*      */   public Map.Entry<K, V> pollLastEntry()
/*      */   {
/* 2186 */     return doRemoveLastEntry();
/*      */   }
/*      */ 
/*      */   Iterator<K> keyIterator()
/*      */   {
/* 2278 */     return new KeyIterator();
/*      */   }
/*      */ 
/*      */   Iterator<V> valueIterator() {
/* 2282 */     return new ValueIterator();
/*      */   }
/*      */ 
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 2286 */     return new EntryIterator();
/*      */   }
/*      */ 
/*      */   static final <E> List<E> toList(Collection<E> paramCollection)
/*      */   {
/* 2299 */     ArrayList localArrayList = new ArrayList();
/* 2300 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 2301 */       localArrayList.add(localObject); }
/* 2302 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 3111 */       UNSAFE = Unsafe.getUnsafe();
/* 3112 */       ConcurrentSkipListMap localConcurrentSkipListMap = ConcurrentSkipListMap.class;
/* 3113 */       headOffset = UNSAFE.objectFieldOffset(localConcurrentSkipListMap.getDeclaredField("head"));
/*      */     }
/*      */     catch (Exception localException) {
/* 3116 */       throw new Error(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class ComparableUsingComparator<K>
/*      */     implements Comparable<K>
/*      */   {
/*      */     final K actualKey;
/*      */     final Comparator<? super K> cmp;
/*      */ 
/*      */     ComparableUsingComparator(K paramK, Comparator<? super K> paramComparator)
/*      */     {
/*  643 */       this.actualKey = paramK;
/*  644 */       this.cmp = paramComparator;
/*      */     }
/*      */     public int compareTo(K paramK) {
/*  647 */       return this.cmp.compare(this.actualKey, paramK);
/*      */     }
/*      */   }
/*      */ 
/*      */   final class EntryIterator extends ConcurrentSkipListMap<K, V>.Iter<Map.Entry<K, V>>
/*      */   {
/*      */     EntryIterator()
/*      */     {
/* 2266 */       super();
/*      */     }
/* 2268 */     public Map.Entry<K, V> next() { ConcurrentSkipListMap.Node localNode = this.next;
/* 2269 */       Object localObject = this.nextValue;
/* 2270 */       advance();
/* 2271 */       return new AbstractMap.SimpleImmutableEntry(localNode.key, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class EntrySet<K1, V1> extends AbstractSet<Map.Entry<K1, V1>>
/*      */   {
/*      */     private final ConcurrentNavigableMap<K1, V1> m;
/*      */ 
/*      */     EntrySet(ConcurrentNavigableMap<K1, V1> paramConcurrentNavigableMap)
/*      */     {
/* 2411 */       this.m = paramConcurrentNavigableMap;
/*      */     }
/*      */ 
/*      */     public Iterator<Map.Entry<K1, V1>> iterator() {
/* 2415 */       if ((this.m instanceof ConcurrentSkipListMap)) {
/* 2416 */         return ((ConcurrentSkipListMap)this.m).entryIterator();
/*      */       }
/* 2418 */       return ((ConcurrentSkipListMap.SubMap)this.m).entryIterator();
/*      */     }
/*      */ 
/*      */     public boolean contains(Object paramObject) {
/* 2422 */       if (!(paramObject instanceof Map.Entry))
/* 2423 */         return false;
/* 2424 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 2425 */       Object localObject = this.m.get(localEntry.getKey());
/* 2426 */       return (localObject != null) && (localObject.equals(localEntry.getValue()));
/*      */     }
/*      */     public boolean remove(Object paramObject) {
/* 2429 */       if (!(paramObject instanceof Map.Entry))
/* 2430 */         return false;
/* 2431 */       Map.Entry localEntry = (Map.Entry)paramObject;
/* 2432 */       return this.m.remove(localEntry.getKey(), localEntry.getValue());
/*      */     }
/*      */ 
/*      */     public boolean isEmpty() {
/* 2436 */       return this.m.isEmpty();
/*      */     }
/*      */     public int size() {
/* 2439 */       return this.m.size();
/*      */     }
/*      */     public void clear() {
/* 2442 */       this.m.clear();
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/* 2445 */       if (paramObject == this)
/* 2446 */         return true;
/* 2447 */       if (!(paramObject instanceof Set))
/* 2448 */         return false;
/* 2449 */       Collection localCollection = (Collection)paramObject;
/*      */       try {
/* 2451 */         return (containsAll(localCollection)) && (localCollection.containsAll(this));
/*      */       } catch (ClassCastException localClassCastException) {
/* 2453 */         return false; } catch (NullPointerException localNullPointerException) {
/*      */       }
/* 2455 */       return false;
/*      */     }
/*      */     public Object[] toArray() {
/* 2458 */       return ConcurrentSkipListMap.toList(this).toArray(); } 
/* 2459 */     public <T> T[] toArray(T[] paramArrayOfT) { return ConcurrentSkipListMap.toList(this).toArray(paramArrayOfT); }
/*      */ 
/*      */   }
/*      */ 
/*      */   static final class HeadIndex<K, V> extends ConcurrentSkipListMap.Index<K, V>
/*      */   {
/*      */     final int level;
/*      */ 
/*      */     HeadIndex(ConcurrentSkipListMap.Node<K, V> paramNode, ConcurrentSkipListMap.Index<K, V> paramIndex1, ConcurrentSkipListMap.Index<K, V> paramIndex2, int paramInt)
/*      */     {
/*  617 */       super(paramIndex1, paramIndex2);
/*  618 */       this.level = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Index<K, V>
/*      */   {
/*      */     final ConcurrentSkipListMap.Node<K, V> node;
/*      */     final Index<K, V> down;
/*      */     volatile Index<K, V> right;
/*      */     private static final Unsafe UNSAFE;
/*      */     private static final long rightOffset;
/*      */ 
/*      */     Index(ConcurrentSkipListMap.Node<K, V> paramNode, Index<K, V> paramIndex1, Index<K, V> paramIndex2)
/*      */     {
/*  549 */       this.node = paramNode;
/*  550 */       this.down = paramIndex1;
/*  551 */       this.right = paramIndex2;
/*      */     }
/*      */ 
/*      */     final boolean casRight(Index<K, V> paramIndex1, Index<K, V> paramIndex2)
/*      */     {
/*  558 */       return UNSAFE.compareAndSwapObject(this, rightOffset, paramIndex1, paramIndex2);
/*      */     }
/*      */ 
/*      */     final boolean indexesDeletedNode()
/*      */     {
/*  566 */       return this.node.value == null;
/*      */     }
/*      */ 
/*      */     final boolean link(Index<K, V> paramIndex1, Index<K, V> paramIndex2)
/*      */     {
/*  578 */       ConcurrentSkipListMap.Node localNode = this.node;
/*  579 */       paramIndex2.right = paramIndex1;
/*  580 */       return (localNode.value != null) && (casRight(paramIndex1, paramIndex2));
/*      */     }
/*      */ 
/*      */     final boolean unlink(Index<K, V> paramIndex)
/*      */     {
/*  591 */       return (!indexesDeletedNode()) && (casRight(paramIndex, paramIndex.right));
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  599 */         UNSAFE = Unsafe.getUnsafe();
/*  600 */         Index localIndex = Index.class;
/*  601 */         rightOffset = UNSAFE.objectFieldOffset(localIndex.getDeclaredField("right"));
/*      */       }
/*      */       catch (Exception localException) {
/*  604 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class Iter<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     ConcurrentSkipListMap.Node<K, V> lastReturned;
/*      */     ConcurrentSkipListMap.Node<K, V> next;
/*      */     V nextValue;
/*      */ 
/*      */     Iter()
/*      */     {
/*      */       while (true)
/*      */       {
/* 2206 */         this.next = ConcurrentSkipListMap.this.findFirst();
/* 2207 */         if (this.next == null)
/*      */           break;
/* 2209 */         Object localObject = this.next.value;
/* 2210 */         if ((localObject != null) && (localObject != this.next)) {
/* 2211 */           this.nextValue = localObject;
/* 2212 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public final boolean hasNext() {
/* 2218 */       return this.next != null;
/*      */     }
/*      */ 
/*      */     final void advance()
/*      */     {
/* 2223 */       if (this.next == null)
/* 2224 */         throw new NoSuchElementException();
/* 2225 */       this.lastReturned = this.next;
/*      */       while (true) {
/* 2227 */         this.next = this.next.next;
/* 2228 */         if (this.next == null)
/*      */           break;
/* 2230 */         Object localObject = this.next.value;
/* 2231 */         if ((localObject != null) && (localObject != this.next)) {
/* 2232 */           this.nextValue = localObject;
/* 2233 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 2239 */       ConcurrentSkipListMap.Node localNode = this.lastReturned;
/* 2240 */       if (localNode == null) {
/* 2241 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/* 2244 */       ConcurrentSkipListMap.this.remove(localNode.key);
/* 2245 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   final class KeyIterator extends ConcurrentSkipListMap<K, V>.Iter<K>
/*      */   {
/*      */     KeyIterator()
/*      */     {
/* 2258 */       super();
/*      */     }
/* 2260 */     public K next() { ConcurrentSkipListMap.Node localNode = this.next;
/* 2261 */       advance();
/* 2262 */       return localNode.key;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class KeySet<E> extends AbstractSet<E>
/*      */     implements NavigableSet<E>
/*      */   {
/*      */     private final ConcurrentNavigableMap<E, Object> m;
/*      */ 
/*      */     KeySet(ConcurrentNavigableMap<E, Object> paramConcurrentNavigableMap)
/*      */     {
/* 2308 */       this.m = paramConcurrentNavigableMap; } 
/* 2309 */     public int size() { return this.m.size(); } 
/* 2310 */     public boolean isEmpty() { return this.m.isEmpty(); } 
/* 2311 */     public boolean contains(Object paramObject) { return this.m.containsKey(paramObject); } 
/* 2312 */     public boolean remove(Object paramObject) { return this.m.remove(paramObject) != null; } 
/* 2313 */     public void clear() { this.m.clear(); } 
/* 2314 */     public E lower(E paramE) { return this.m.lowerKey(paramE); } 
/* 2315 */     public E floor(E paramE) { return this.m.floorKey(paramE); } 
/* 2316 */     public E ceiling(E paramE) { return this.m.ceilingKey(paramE); } 
/* 2317 */     public E higher(E paramE) { return this.m.higherKey(paramE); } 
/* 2318 */     public Comparator<? super E> comparator() { return this.m.comparator(); } 
/* 2319 */     public E first() { return this.m.firstKey(); } 
/* 2320 */     public E last() { return this.m.lastKey(); } 
/*      */     public E pollFirst() {
/* 2322 */       Map.Entry localEntry = this.m.pollFirstEntry();
/* 2323 */       return localEntry == null ? null : localEntry.getKey();
/*      */     }
/*      */     public E pollLast() {
/* 2326 */       Map.Entry localEntry = this.m.pollLastEntry();
/* 2327 */       return localEntry == null ? null : localEntry.getKey();
/*      */     }
/*      */     public Iterator<E> iterator() {
/* 2330 */       if ((this.m instanceof ConcurrentSkipListMap)) {
/* 2331 */         return ((ConcurrentSkipListMap)this.m).keyIterator();
/*      */       }
/* 2333 */       return ((ConcurrentSkipListMap.SubMap)this.m).keyIterator();
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/* 2336 */       if (paramObject == this)
/* 2337 */         return true;
/* 2338 */       if (!(paramObject instanceof Set))
/* 2339 */         return false;
/* 2340 */       Collection localCollection = (Collection)paramObject;
/*      */       try {
/* 2342 */         return (containsAll(localCollection)) && (localCollection.containsAll(this));
/*      */       } catch (ClassCastException localClassCastException) {
/* 2344 */         return false; } catch (NullPointerException localNullPointerException) {
/*      */       }
/* 2346 */       return false;
/*      */     }
/*      */     public Object[] toArray() {
/* 2349 */       return ConcurrentSkipListMap.toList(this).toArray(); } 
/* 2350 */     public <T> T[] toArray(T[] paramArrayOfT) { return ConcurrentSkipListMap.toList(this).toArray(paramArrayOfT); } 
/*      */     public Iterator<E> descendingIterator() {
/* 2352 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */     public NavigableSet<E> subSet(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2)
/*      */     {
/* 2358 */       return new KeySet(this.m.subMap(paramE1, paramBoolean1, paramE2, paramBoolean2));
/*      */     }
/*      */ 
/*      */     public NavigableSet<E> headSet(E paramE, boolean paramBoolean) {
/* 2362 */       return new KeySet(this.m.headMap(paramE, paramBoolean));
/*      */     }
/*      */     public NavigableSet<E> tailSet(E paramE, boolean paramBoolean) {
/* 2365 */       return new KeySet(this.m.tailMap(paramE, paramBoolean));
/*      */     }
/*      */     public NavigableSet<E> subSet(E paramE1, E paramE2) {
/* 2368 */       return subSet(paramE1, true, paramE2, false);
/*      */     }
/*      */     public NavigableSet<E> headSet(E paramE) {
/* 2371 */       return headSet(paramE, false);
/*      */     }
/*      */     public NavigableSet<E> tailSet(E paramE) {
/* 2374 */       return tailSet(paramE, true);
/*      */     }
/*      */     public NavigableSet<E> descendingSet() {
/* 2377 */       return new KeySet(this.m.descendingMap());
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Node<K, V>
/*      */   {
/*      */     final K key;
/*      */     volatile Object value;
/*      */     volatile Node<K, V> next;
/*      */     private static final Unsafe UNSAFE;
/*      */     private static final long valueOffset;
/*      */     private static final long nextOffset;
/*      */ 
/*      */     Node(K paramK, Object paramObject, Node<K, V> paramNode)
/*      */     {
/*  403 */       this.key = paramK;
/*  404 */       this.value = paramObject;
/*  405 */       this.next = paramNode;
/*      */     }
/*      */ 
/*      */     Node(Node<K, V> paramNode)
/*      */     {
/*  416 */       this.key = null;
/*  417 */       this.value = this;
/*  418 */       this.next = paramNode;
/*      */     }
/*      */ 
/*      */     boolean casValue(Object paramObject1, Object paramObject2)
/*      */     {
/*  425 */       return UNSAFE.compareAndSwapObject(this, valueOffset, paramObject1, paramObject2);
/*      */     }
/*      */ 
/*      */     boolean casNext(Node<K, V> paramNode1, Node<K, V> paramNode2)
/*      */     {
/*  432 */       return UNSAFE.compareAndSwapObject(this, nextOffset, paramNode1, paramNode2);
/*      */     }
/*      */ 
/*      */     boolean isMarker()
/*      */     {
/*  445 */       return this.value == this;
/*      */     }
/*      */ 
/*      */     boolean isBaseHeader()
/*      */     {
/*  453 */       return this.value == ConcurrentSkipListMap.BASE_HEADER;
/*      */     }
/*      */ 
/*      */     boolean appendMarker(Node<K, V> paramNode)
/*      */     {
/*  462 */       return casNext(paramNode, new Node(paramNode));
/*      */     }
/*      */ 
/*      */     void helpDelete(Node<K, V> paramNode1, Node<K, V> paramNode2)
/*      */     {
/*  478 */       if ((paramNode2 == this.next) && (this == paramNode1.next))
/*  479 */         if ((paramNode2 == null) || (paramNode2.value != paramNode2))
/*  480 */           appendMarker(paramNode2);
/*      */         else
/*  482 */           paramNode1.casNext(this, paramNode2.next);
/*      */     }
/*      */ 
/*      */     V getValidValue()
/*      */     {
/*  493 */       Object localObject = this.value;
/*  494 */       if ((localObject == this) || (localObject == ConcurrentSkipListMap.BASE_HEADER))
/*  495 */         return null;
/*  496 */       return localObject;
/*      */     }
/*      */ 
/*      */     AbstractMap.SimpleImmutableEntry<K, V> createSnapshot()
/*      */     {
/*  505 */       Object localObject = getValidValue();
/*  506 */       if (localObject == null)
/*  507 */         return null;
/*  508 */       return new AbstractMap.SimpleImmutableEntry(this.key, localObject);
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/*      */       try
/*      */       {
/*  519 */         UNSAFE = Unsafe.getUnsafe();
/*  520 */         Node localNode = Node.class;
/*  521 */         valueOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("value"));
/*      */ 
/*  523 */         nextOffset = UNSAFE.objectFieldOffset(localNode.getDeclaredField("next"));
/*      */       }
/*      */       catch (Exception localException) {
/*  526 */         throw new Error(localException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class SubMap<K, V> extends AbstractMap<K, V>
/*      */     implements ConcurrentNavigableMap<K, V>, Cloneable, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -7647078645895051609L;
/*      */     private final ConcurrentSkipListMap<K, V> m;
/*      */     private final K lo;
/*      */     private final K hi;
/*      */     private final boolean loInclusive;
/*      */     private final boolean hiInclusive;
/*      */     private final boolean isDescending;
/*      */     private transient ConcurrentSkipListMap.KeySet<K> keySetView;
/*      */     private transient Set<Map.Entry<K, V>> entrySetView;
/*      */     private transient Collection<V> valuesView;
/*      */ 
/*      */     SubMap(ConcurrentSkipListMap<K, V> paramConcurrentSkipListMap, K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2, boolean paramBoolean3)
/*      */     {
/* 2504 */       if ((paramK1 != null) && (paramK2 != null) && (paramConcurrentSkipListMap.compare(paramK1, paramK2) > 0))
/*      */       {
/* 2506 */         throw new IllegalArgumentException("inconsistent range");
/* 2507 */       }this.m = paramConcurrentSkipListMap;
/* 2508 */       this.lo = paramK1;
/* 2509 */       this.hi = paramK2;
/* 2510 */       this.loInclusive = paramBoolean1;
/* 2511 */       this.hiInclusive = paramBoolean2;
/* 2512 */       this.isDescending = paramBoolean3;
/*      */     }
/*      */ 
/*      */     private boolean tooLow(K paramK)
/*      */     {
/* 2518 */       if (this.lo != null) {
/* 2519 */         int i = this.m.compare(paramK, this.lo);
/* 2520 */         if ((i < 0) || ((i == 0) && (!this.loInclusive)))
/* 2521 */           return true;
/*      */       }
/* 2523 */       return false;
/*      */     }
/*      */ 
/*      */     private boolean tooHigh(K paramK) {
/* 2527 */       if (this.hi != null) {
/* 2528 */         int i = this.m.compare(paramK, this.hi);
/* 2529 */         if ((i > 0) || ((i == 0) && (!this.hiInclusive)))
/* 2530 */           return true;
/*      */       }
/* 2532 */       return false;
/*      */     }
/*      */ 
/*      */     private boolean inBounds(K paramK) {
/* 2536 */       return (!tooLow(paramK)) && (!tooHigh(paramK));
/*      */     }
/*      */ 
/*      */     private void checkKeyBounds(K paramK) throws IllegalArgumentException {
/* 2540 */       if (paramK == null)
/* 2541 */         throw new NullPointerException();
/* 2542 */       if (!inBounds(paramK))
/* 2543 */         throw new IllegalArgumentException("key out of range");
/*      */     }
/*      */ 
/*      */     private boolean isBeforeEnd(ConcurrentSkipListMap.Node<K, V> paramNode)
/*      */     {
/* 2550 */       if (paramNode == null)
/* 2551 */         return false;
/* 2552 */       if (this.hi == null)
/* 2553 */         return true;
/* 2554 */       Object localObject = paramNode.key;
/* 2555 */       if (localObject == null)
/* 2556 */         return true;
/* 2557 */       int i = this.m.compare(localObject, this.hi);
/* 2558 */       if ((i > 0) || ((i == 0) && (!this.hiInclusive)))
/* 2559 */         return false;
/* 2560 */       return true;
/*      */     }
/*      */ 
/*      */     private ConcurrentSkipListMap.Node<K, V> loNode()
/*      */     {
/* 2568 */       if (this.lo == null)
/* 2569 */         return this.m.findFirst();
/* 2570 */       if (this.loInclusive) {
/* 2571 */         return this.m.findNear(this.lo, 0x0 | 0x1);
/*      */       }
/* 2573 */       return this.m.findNear(this.lo, 0);
/*      */     }
/*      */ 
/*      */     private ConcurrentSkipListMap.Node<K, V> hiNode()
/*      */     {
/* 2581 */       if (this.hi == null)
/* 2582 */         return this.m.findLast();
/* 2583 */       if (this.hiInclusive) {
/* 2584 */         return this.m.findNear(this.hi, 0x2 | 0x1);
/*      */       }
/* 2586 */       return this.m.findNear(this.hi, 2);
/*      */     }
/*      */ 
/*      */     private K lowestKey()
/*      */     {
/* 2593 */       ConcurrentSkipListMap.Node localNode = loNode();
/* 2594 */       if (isBeforeEnd(localNode)) {
/* 2595 */         return localNode.key;
/*      */       }
/* 2597 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     private K highestKey()
/*      */     {
/* 2604 */       ConcurrentSkipListMap.Node localNode = hiNode();
/* 2605 */       if (localNode != null) {
/* 2606 */         Object localObject = localNode.key;
/* 2607 */         if (inBounds(localObject))
/* 2608 */           return localObject;
/*      */       }
/* 2610 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     private Map.Entry<K, V> lowestEntry() {
/*      */       while (true) {
/* 2615 */         ConcurrentSkipListMap.Node localNode = loNode();
/* 2616 */         if (!isBeforeEnd(localNode))
/* 2617 */           return null;
/* 2618 */         AbstractMap.SimpleImmutableEntry localSimpleImmutableEntry = localNode.createSnapshot();
/* 2619 */         if (localSimpleImmutableEntry != null)
/* 2620 */           return localSimpleImmutableEntry;
/*      */       }
/*      */     }
/*      */ 
/*      */     private Map.Entry<K, V> highestEntry() {
/*      */       while (true) {
/* 2626 */         ConcurrentSkipListMap.Node localNode = hiNode();
/* 2627 */         if ((localNode == null) || (!inBounds(localNode.key)))
/* 2628 */           return null;
/* 2629 */         AbstractMap.SimpleImmutableEntry localSimpleImmutableEntry = localNode.createSnapshot();
/* 2630 */         if (localSimpleImmutableEntry != null)
/* 2631 */           return localSimpleImmutableEntry;
/*      */       }
/*      */     }
/*      */ 
/*      */     private Map.Entry<K, V> removeLowest() {
/*      */       while (true) {
/* 2637 */         ConcurrentSkipListMap.Node localNode = loNode();
/* 2638 */         if (localNode == null)
/* 2639 */           return null;
/* 2640 */         Object localObject1 = localNode.key;
/* 2641 */         if (!inBounds(localObject1))
/* 2642 */           return null;
/* 2643 */         Object localObject2 = this.m.doRemove(localObject1, null);
/* 2644 */         if (localObject2 != null)
/* 2645 */           return new AbstractMap.SimpleImmutableEntry(localObject1, localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Map.Entry<K, V> removeHighest() {
/*      */       while (true) {
/* 2651 */         ConcurrentSkipListMap.Node localNode = hiNode();
/* 2652 */         if (localNode == null)
/* 2653 */           return null;
/* 2654 */         Object localObject1 = localNode.key;
/* 2655 */         if (!inBounds(localObject1))
/* 2656 */           return null;
/* 2657 */         Object localObject2 = this.m.doRemove(localObject1, null);
/* 2658 */         if (localObject2 != null)
/* 2659 */           return new AbstractMap.SimpleImmutableEntry(localObject1, localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Map.Entry<K, V> getNearEntry(K paramK, int paramInt)
/*      */     {
/* 2667 */       if (this.isDescending) {
/* 2668 */         if ((paramInt & 0x2) == 0)
/* 2669 */           paramInt |= 2;
/*      */         else
/* 2671 */           paramInt &= (0x2 ^ 0xFFFFFFFF);
/*      */       }
/* 2673 */       if (tooLow(paramK))
/* 2674 */         return (paramInt & 0x2) != 0 ? null : lowestEntry();
/* 2675 */       if (tooHigh(paramK))
/* 2676 */         return (paramInt & 0x2) != 0 ? highestEntry() : null;
/*      */       while (true) {
/* 2678 */         ConcurrentSkipListMap.Node localNode = this.m.findNear(paramK, paramInt);
/* 2679 */         if ((localNode == null) || (!inBounds(localNode.key)))
/* 2680 */           return null;
/* 2681 */         Object localObject1 = localNode.key;
/* 2682 */         Object localObject2 = localNode.getValidValue();
/* 2683 */         if (localObject2 != null)
/* 2684 */           return new AbstractMap.SimpleImmutableEntry(localObject1, localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private K getNearKey(K paramK, int paramInt)
/*      */     {
/* 2690 */       if (this.isDescending)
/* 2691 */         if ((paramInt & 0x2) == 0)
/* 2692 */           paramInt |= 2;
/*      */         else
/* 2694 */           paramInt &= (0x2 ^ 0xFFFFFFFF);
/*      */       ConcurrentSkipListMap.Node localNode;
/* 2696 */       if (tooLow(paramK)) {
/* 2697 */         if ((paramInt & 0x2) == 0) {
/* 2698 */           localNode = loNode();
/* 2699 */           if (isBeforeEnd(localNode))
/* 2700 */             return localNode.key;
/*      */         }
/* 2702 */         return null;
/*      */       }
/*      */       Object localObject1;
/* 2704 */       if (tooHigh(paramK)) {
/* 2705 */         if ((paramInt & 0x2) != 0) {
/* 2706 */           localNode = hiNode();
/* 2707 */           if (localNode != null) {
/* 2708 */             localObject1 = localNode.key;
/* 2709 */             if (inBounds(localObject1))
/* 2710 */               return localObject1;
/*      */           }
/*      */         }
/* 2713 */         return null;
/*      */       }
/*      */       while (true) {
/* 2716 */         localNode = this.m.findNear(paramK, paramInt);
/* 2717 */         if ((localNode == null) || (!inBounds(localNode.key)))
/* 2718 */           return null;
/* 2719 */         localObject1 = localNode.key;
/* 2720 */         Object localObject2 = localNode.getValidValue();
/* 2721 */         if (localObject2 != null)
/* 2722 */           return localObject1;
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean containsKey(Object paramObject)
/*      */     {
/* 2729 */       if (paramObject == null) throw new NullPointerException();
/* 2730 */       Object localObject = paramObject;
/* 2731 */       return (inBounds(localObject)) && (this.m.containsKey(localObject));
/*      */     }
/*      */ 
/*      */     public V get(Object paramObject) {
/* 2735 */       if (paramObject == null) throw new NullPointerException();
/* 2736 */       Object localObject = paramObject;
/* 2737 */       return !inBounds(localObject) ? null : this.m.get(localObject);
/*      */     }
/*      */ 
/*      */     public V put(K paramK, V paramV) {
/* 2741 */       checkKeyBounds(paramK);
/* 2742 */       return this.m.put(paramK, paramV);
/*      */     }
/*      */ 
/*      */     public V remove(Object paramObject) {
/* 2746 */       Object localObject = paramObject;
/* 2747 */       return !inBounds(localObject) ? null : this.m.remove(localObject);
/*      */     }
/*      */ 
/*      */     public int size() {
/* 2751 */       long l = 0L;
/* 2752 */       for (ConcurrentSkipListMap.Node localNode = loNode(); 
/* 2753 */         isBeforeEnd(localNode); 
/* 2754 */         localNode = localNode.next) {
/* 2755 */         if (localNode.getValidValue() != null)
/* 2756 */           l += 1L;
/*      */       }
/* 2758 */       return l >= 2147483647L ? 2147483647 : (int)l;
/*      */     }
/*      */ 
/*      */     public boolean isEmpty() {
/* 2762 */       return !isBeforeEnd(loNode());
/*      */     }
/*      */ 
/*      */     public boolean containsValue(Object paramObject) {
/* 2766 */       if (paramObject == null)
/* 2767 */         throw new NullPointerException();
/* 2768 */       for (ConcurrentSkipListMap.Node localNode = loNode(); 
/* 2769 */         isBeforeEnd(localNode); 
/* 2770 */         localNode = localNode.next) {
/* 2771 */         Object localObject = localNode.getValidValue();
/* 2772 */         if ((localObject != null) && (paramObject.equals(localObject)))
/* 2773 */           return true;
/*      */       }
/* 2775 */       return false;
/*      */     }
/*      */ 
/*      */     public void clear() {
/* 2779 */       for (ConcurrentSkipListMap.Node localNode = loNode(); 
/* 2780 */         isBeforeEnd(localNode); 
/* 2781 */         localNode = localNode.next)
/* 2782 */         if (localNode.getValidValue() != null)
/* 2783 */           this.m.remove(localNode.key);
/*      */     }
/*      */ 
/*      */     public V putIfAbsent(K paramK, V paramV)
/*      */     {
/* 2790 */       checkKeyBounds(paramK);
/* 2791 */       return this.m.putIfAbsent(paramK, paramV);
/*      */     }
/*      */ 
/*      */     public boolean remove(Object paramObject1, Object paramObject2) {
/* 2795 */       Object localObject = paramObject1;
/* 2796 */       return (inBounds(localObject)) && (this.m.remove(localObject, paramObject2));
/*      */     }
/*      */ 
/*      */     public boolean replace(K paramK, V paramV1, V paramV2) {
/* 2800 */       checkKeyBounds(paramK);
/* 2801 */       return this.m.replace(paramK, paramV1, paramV2);
/*      */     }
/*      */ 
/*      */     public V replace(K paramK, V paramV) {
/* 2805 */       checkKeyBounds(paramK);
/* 2806 */       return this.m.replace(paramK, paramV);
/*      */     }
/*      */ 
/*      */     public Comparator<? super K> comparator()
/*      */     {
/* 2812 */       Comparator localComparator = this.m.comparator();
/* 2813 */       if (this.isDescending) {
/* 2814 */         return Collections.reverseOrder(localComparator);
/*      */       }
/* 2816 */       return localComparator;
/*      */     }
/*      */ 
/*      */     private SubMap<K, V> newSubMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
/*      */     {
/* 2827 */       if (this.isDescending) {
/* 2828 */         K ? = paramK1;
/* 2829 */         paramK1 = paramK2;
/* 2830 */         paramK2 = ?;
/* 2831 */         boolean bool = paramBoolean1;
/* 2832 */         paramBoolean1 = paramBoolean2;
/* 2833 */         paramBoolean2 = bool;
/*      */       }
/*      */       int i;
/* 2835 */       if (this.lo != null) {
/* 2836 */         if (paramK1 == null) {
/* 2837 */           paramK1 = this.lo;
/* 2838 */           paramBoolean1 = this.loInclusive;
/*      */         }
/*      */         else {
/* 2841 */           i = this.m.compare(paramK1, this.lo);
/* 2842 */           if ((i < 0) || ((i == 0) && (!this.loInclusive) && (paramBoolean1)))
/* 2843 */             throw new IllegalArgumentException("key out of range");
/*      */         }
/*      */       }
/* 2846 */       if (this.hi != null) {
/* 2847 */         if (paramK2 == null) {
/* 2848 */           paramK2 = this.hi;
/* 2849 */           paramBoolean2 = this.hiInclusive;
/*      */         }
/*      */         else {
/* 2852 */           i = this.m.compare(paramK2, this.hi);
/* 2853 */           if ((i > 0) || ((i == 0) && (!this.hiInclusive) && (paramBoolean2)))
/* 2854 */             throw new IllegalArgumentException("key out of range");
/*      */         }
/*      */       }
/* 2857 */       return new SubMap(this.m, paramK1, paramBoolean1, paramK2, paramBoolean2, this.isDescending);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> subMap(K paramK1, boolean paramBoolean1, K paramK2, boolean paramBoolean2)
/*      */     {
/* 2865 */       if ((paramK1 == null) || (paramK2 == null))
/* 2866 */         throw new NullPointerException();
/* 2867 */       return newSubMap(paramK1, paramBoolean1, paramK2, paramBoolean2);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> headMap(K paramK, boolean paramBoolean)
/*      */     {
/* 2872 */       if (paramK == null)
/* 2873 */         throw new NullPointerException();
/* 2874 */       return newSubMap(null, false, paramK, paramBoolean);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> tailMap(K paramK, boolean paramBoolean)
/*      */     {
/* 2879 */       if (paramK == null)
/* 2880 */         throw new NullPointerException();
/* 2881 */       return newSubMap(paramK, paramBoolean, null, false);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> subMap(K paramK1, K paramK2) {
/* 2885 */       return subMap(paramK1, true, paramK2, false);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> headMap(K paramK) {
/* 2889 */       return headMap(paramK, false);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> tailMap(K paramK) {
/* 2893 */       return tailMap(paramK, true);
/*      */     }
/*      */ 
/*      */     public SubMap<K, V> descendingMap() {
/* 2897 */       return new SubMap(this.m, this.lo, this.loInclusive, this.hi, this.hiInclusive, !this.isDescending);
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> ceilingEntry(K paramK)
/*      */     {
/* 2904 */       return getNearEntry(paramK, 0x0 | 0x1);
/*      */     }
/*      */ 
/*      */     public K ceilingKey(K paramK) {
/* 2908 */       return getNearKey(paramK, 0x0 | 0x1);
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> lowerEntry(K paramK) {
/* 2912 */       return getNearEntry(paramK, 2);
/*      */     }
/*      */ 
/*      */     public K lowerKey(K paramK) {
/* 2916 */       return getNearKey(paramK, 2);
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> floorEntry(K paramK) {
/* 2920 */       return getNearEntry(paramK, 0x2 | 0x1);
/*      */     }
/*      */ 
/*      */     public K floorKey(K paramK) {
/* 2924 */       return getNearKey(paramK, 0x2 | 0x1);
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> higherEntry(K paramK) {
/* 2928 */       return getNearEntry(paramK, 0);
/*      */     }
/*      */ 
/*      */     public K higherKey(K paramK) {
/* 2932 */       return getNearKey(paramK, 0);
/*      */     }
/*      */ 
/*      */     public K firstKey() {
/* 2936 */       return this.isDescending ? highestKey() : lowestKey();
/*      */     }
/*      */ 
/*      */     public K lastKey() {
/* 2940 */       return this.isDescending ? lowestKey() : highestKey();
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> firstEntry() {
/* 2944 */       return this.isDescending ? highestEntry() : lowestEntry();
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> lastEntry() {
/* 2948 */       return this.isDescending ? lowestEntry() : highestEntry();
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 2952 */       return this.isDescending ? removeHighest() : removeLowest();
/*      */     }
/*      */ 
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 2956 */       return this.isDescending ? removeLowest() : removeHighest();
/*      */     }
/*      */ 
/*      */     public NavigableSet<K> keySet()
/*      */     {
/* 2962 */       ConcurrentSkipListMap.KeySet localKeySet = this.keySetView;
/* 2963 */       return this.keySetView = new ConcurrentSkipListMap.KeySet(this);
/*      */     }
/*      */ 
/*      */     public NavigableSet<K> navigableKeySet() {
/* 2967 */       ConcurrentSkipListMap.KeySet localKeySet = this.keySetView;
/* 2968 */       return this.keySetView = new ConcurrentSkipListMap.KeySet(this);
/*      */     }
/*      */ 
/*      */     public Collection<V> values() {
/* 2972 */       Collection localCollection = this.valuesView;
/* 2973 */       return this.valuesView = new ConcurrentSkipListMap.Values(this);
/*      */     }
/*      */ 
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 2977 */       Set localSet = this.entrySetView;
/* 2978 */       return this.entrySetView = new ConcurrentSkipListMap.EntrySet(this);
/*      */     }
/*      */ 
/*      */     public NavigableSet<K> descendingKeySet() {
/* 2982 */       return descendingMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     Iterator<K> keyIterator() {
/* 2986 */       return new SubMapKeyIterator();
/*      */     }
/*      */ 
/*      */     Iterator<V> valueIterator() {
/* 2990 */       return new SubMapValueIterator();
/*      */     }
/*      */ 
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 2994 */       return new SubMapEntryIterator();
/*      */     }
/*      */ 
/*      */     final class SubMapEntryIterator extends ConcurrentSkipListMap.SubMap<K, V>.SubMapIter<Map.Entry<K, V>>
/*      */     {
/*      */       SubMapEntryIterator()
/*      */       {
/* 3096 */         super();
/*      */       }
/* 3098 */       public Map.Entry<K, V> next() { ConcurrentSkipListMap.Node localNode = this.next;
/* 3099 */         Object localObject = this.nextValue;
/* 3100 */         advance();
/* 3101 */         return new AbstractMap.SimpleImmutableEntry(localNode.key, localObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     abstract class SubMapIter<T>
/*      */       implements Iterator<T>
/*      */     {
/*      */       ConcurrentSkipListMap.Node<K, V> lastReturned;
/*      */       ConcurrentSkipListMap.Node<K, V> next;
/*      */       V nextValue;
/*      */ 
/*      */       SubMapIter()
/*      */       {
/*      */         while (true)
/*      */         {
/* 3010 */           this.next = (ConcurrentSkipListMap.SubMap.this.isDescending ? ConcurrentSkipListMap.SubMap.this.hiNode() : ConcurrentSkipListMap.SubMap.this.loNode());
/* 3011 */           if (this.next == null)
/*      */             break;
/* 3013 */           Object localObject = this.next.value;
/* 3014 */           if ((localObject != null) && (localObject != this.next)) {
/* 3015 */             if (!ConcurrentSkipListMap.SubMap.this.inBounds(this.next.key)) {
/* 3016 */               this.next = null; break;
/*      */             }
/* 3018 */             this.nextValue = localObject;
/* 3019 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public final boolean hasNext() {
/* 3025 */         return this.next != null;
/*      */       }
/*      */ 
/*      */       final void advance() {
/* 3029 */         if (this.next == null)
/* 3030 */           throw new NoSuchElementException();
/* 3031 */         this.lastReturned = this.next;
/* 3032 */         if (ConcurrentSkipListMap.SubMap.this.isDescending)
/* 3033 */           descend();
/*      */         else
/* 3035 */           ascend();
/*      */       }
/*      */ 
/*      */       private void ascend() {
/*      */         while (true) {
/* 3040 */           this.next = this.next.next;
/* 3041 */           if (this.next == null)
/*      */             break;
/* 3043 */           Object localObject = this.next.value;
/* 3044 */           if ((localObject != null) && (localObject != this.next)) {
/* 3045 */             if (ConcurrentSkipListMap.SubMap.this.tooHigh(this.next.key)) {
/* 3046 */               this.next = null; break;
/*      */             }
/* 3048 */             this.nextValue = localObject;
/* 3049 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       private void descend() {
/*      */         while (true) {
/* 3056 */           this.next = ConcurrentSkipListMap.this.findNear(this.lastReturned.key, 2);
/* 3057 */           if (this.next == null)
/*      */             break;
/* 3059 */           Object localObject = this.next.value;
/* 3060 */           if ((localObject != null) && (localObject != this.next)) {
/* 3061 */             if (ConcurrentSkipListMap.SubMap.this.tooLow(this.next.key)) {
/* 3062 */               this.next = null; break;
/*      */             }
/* 3064 */             this.nextValue = localObject;
/* 3065 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */       public void remove() {
/* 3071 */         ConcurrentSkipListMap.Node localNode = this.lastReturned;
/* 3072 */         if (localNode == null)
/* 3073 */           throw new IllegalStateException();
/* 3074 */         ConcurrentSkipListMap.this.remove(localNode.key);
/* 3075 */         this.lastReturned = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     final class SubMapKeyIterator extends ConcurrentSkipListMap.SubMap<K, V>.SubMapIter<K>
/*      */     {
/*      */       SubMapKeyIterator()
/*      */       {
/* 3088 */         super();
/*      */       }
/* 3090 */       public K next() { ConcurrentSkipListMap.Node localNode = this.next;
/* 3091 */         advance();
/* 3092 */         return localNode.key;
/*      */       }
/*      */     }
/*      */ 
/*      */     final class SubMapValueIterator extends ConcurrentSkipListMap.SubMap<K, V>.SubMapIter<V>
/*      */     {
/*      */       SubMapValueIterator()
/*      */       {
/* 3080 */         super();
/*      */       }
/* 3082 */       public V next() { Object localObject = this.nextValue;
/* 3083 */         advance();
/* 3084 */         return localObject;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final class ValueIterator extends ConcurrentSkipListMap<K, V>.Iter<V>
/*      */   {
/*      */     ValueIterator()
/*      */     {
/* 2250 */       super();
/*      */     }
/* 2252 */     public V next() { Object localObject = this.nextValue;
/* 2253 */       advance();
/* 2254 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Values<E> extends AbstractCollection<E>
/*      */   {
/*      */     private final ConcurrentNavigableMap<Object, E> m;
/*      */ 
/*      */     Values(ConcurrentNavigableMap<Object, E> paramConcurrentNavigableMap)
/*      */     {
/* 2384 */       this.m = paramConcurrentNavigableMap;
/*      */     }
/*      */     public Iterator<E> iterator() {
/* 2387 */       if ((this.m instanceof ConcurrentSkipListMap)) {
/* 2388 */         return ((ConcurrentSkipListMap)this.m).valueIterator();
/*      */       }
/* 2390 */       return ((ConcurrentSkipListMap.SubMap)this.m).valueIterator();
/*      */     }
/*      */     public boolean isEmpty() {
/* 2393 */       return this.m.isEmpty();
/*      */     }
/*      */     public int size() {
/* 2396 */       return this.m.size();
/*      */     }
/*      */     public boolean contains(Object paramObject) {
/* 2399 */       return this.m.containsValue(paramObject);
/*      */     }
/*      */     public void clear() {
/* 2402 */       this.m.clear();
/*      */     }
/* 2404 */     public Object[] toArray() { return ConcurrentSkipListMap.toList(this).toArray(); } 
/* 2405 */     public <T> T[] toArray(T[] paramArrayOfT) { return ConcurrentSkipListMap.toList(this).toArray(paramArrayOfT); }
/*      */ 
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ConcurrentSkipListMap
 * JD-Core Version:    0.6.2
 */