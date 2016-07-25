/*    */ package sun.security.provider.certpath;
/*    */ 
/*    */ import java.security.PublicKey;
/*    */ import java.security.cert.CertPath;
/*    */ import java.security.cert.PKIXCertPathBuilderResult;
/*    */ import java.security.cert.PolicyNode;
/*    */ import java.security.cert.TrustAnchor;
/*    */ import sun.security.util.Debug;
/*    */ 
/*    */ public class SunCertPathBuilderResult extends PKIXCertPathBuilderResult
/*    */ {
/* 52 */   private static final Debug debug = Debug.getInstance("certpath");
/*    */   private AdjacencyList adjList;
/*    */ 
/*    */   SunCertPathBuilderResult(CertPath paramCertPath, TrustAnchor paramTrustAnchor, PolicyNode paramPolicyNode, PublicKey paramPublicKey, AdjacencyList paramAdjacencyList)
/*    */   {
/* 71 */     super(paramCertPath, paramTrustAnchor, paramPolicyNode, paramPublicKey);
/* 72 */     this.adjList = paramAdjacencyList;
/*    */   }
/*    */ 
/*    */   public AdjacencyList getAdjacencyList()
/*    */   {
/* 81 */     return this.adjList;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.SunCertPathBuilderResult
 * JD-Core Version:    0.6.2
 */