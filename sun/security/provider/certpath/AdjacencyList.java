/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class AdjacencyList
/*     */ {
/*     */   private ArrayList<BuildStep> mStepList;
/*     */   private List<List<Vertex>> mOrigList;
/*     */ 
/*     */   public AdjacencyList(List<List<Vertex>> paramList)
/*     */   {
/* 103 */     this.mStepList = new ArrayList();
/* 104 */     this.mOrigList = paramList;
/* 105 */     buildList(paramList, 0, null);
/*     */   }
/*     */ 
/*     */   public Iterator<BuildStep> iterator()
/*     */   {
/* 116 */     return Collections.unmodifiableList(this.mStepList).iterator();
/*     */   }
/*     */ 
/*     */   private boolean buildList(List<List<Vertex>> paramList, int paramInt, BuildStep paramBuildStep)
/*     */   {
/* 131 */     List localList = (List)paramList.get(paramInt);
/*     */     try
/*     */     {
/* 135 */       int i = 1;
/*     */ 
/* 137 */       int j = 1;
/*     */ 
/* 139 */       for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Vertex)((Iterator)localObject1).next();
/* 140 */         if (((Vertex)localObject2).getIndex() != -1)
/*     */         {
/* 143 */           if (((List)paramList.get(((Vertex)localObject2).getIndex())).size() != 0) {
/* 144 */             i = 0;
/*     */           }
/*     */         }
/* 147 */         else if (((Vertex)localObject2).getThrowable() == null) {
/* 148 */           j = 0;
/*     */         }
/*     */ 
/* 152 */         this.mStepList.add(new BuildStep((Vertex)localObject2, 1));
/*     */       }
/*     */       Vertex localVertex;
/* 155 */       if (i != 0)
/*     */       {
/* 160 */         if (j != 0)
/*     */         {
/* 162 */           if (paramBuildStep == null)
/* 163 */             this.mStepList.add(new BuildStep(null, 4));
/*     */           else {
/* 165 */             this.mStepList.add(new BuildStep(paramBuildStep.getVertex(), 2));
/*     */           }
/*     */ 
/* 168 */           return false;
/*     */         }
/*     */ 
/* 174 */         localObject1 = new ArrayList();
/* 175 */         for (localObject2 = localList.iterator(); ((Iterator)localObject2).hasNext(); ) { localVertex = (Vertex)((Iterator)localObject2).next();
/* 176 */           if (localVertex.getThrowable() == null) {
/* 177 */             ((List)localObject1).add(localVertex);
/*     */           }
/*     */         }
/* 180 */         if (((List)localObject1).size() == 1)
/*     */         {
/* 182 */           this.mStepList.add(new BuildStep((Vertex)((List)localObject1).get(0), 5));
/*     */         }
/*     */         else
/*     */         {
/* 192 */           this.mStepList.add(new BuildStep((Vertex)((List)localObject1).get(0), 5));
/*     */         }
/*     */ 
/* 196 */         return true;
/*     */       }
/*     */ 
/* 204 */       boolean bool = false;
/*     */ 
/* 206 */       for (Object localObject2 = localList.iterator(); ((Iterator)localObject2).hasNext(); ) { localVertex = (Vertex)((Iterator)localObject2).next();
/*     */ 
/* 212 */         if ((localVertex.getIndex() != -1) && 
/* 213 */           (((List)paramList.get(localVertex.getIndex())).size() != 0))
/*     */         {
/* 217 */           BuildStep localBuildStep = new BuildStep(localVertex, 3);
/* 218 */           this.mStepList.add(localBuildStep);
/* 219 */           bool = buildList(paramList, localVertex.getIndex(), localBuildStep);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 224 */       if (bool)
/*     */       {
/* 226 */         return true;
/*     */       }
/*     */ 
/* 230 */       if (paramBuildStep == null)
/* 231 */         this.mStepList.add(new BuildStep(null, 4));
/*     */       else {
/* 233 */         this.mStepList.add(new BuildStep(paramBuildStep.getVertex(), 2));
/*     */       }
/*     */ 
/* 236 */       return false;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 243 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 252 */     String str = "[\n";
/*     */ 
/* 254 */     int i = 0;
/* 255 */     for (List localList : this.mOrigList) {
/* 256 */       str = str + "LinkedList[" + i++ + "]:\n";
/*     */ 
/* 258 */       for (Vertex localVertex : localList)
/*     */         try {
/* 260 */           str = str + localVertex.toString();
/* 261 */           str = str + "\n";
/*     */         } catch (Exception localException) {
/* 263 */           str = str + "No Such Element\n";
/*     */         }
/*     */     }
/* 266 */     str = str + "]\n";
/*     */ 
/* 268 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.AdjacencyList
 * JD-Core Version:    0.6.2
 */