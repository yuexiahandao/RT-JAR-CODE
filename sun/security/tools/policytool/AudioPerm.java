/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ class AudioPerm extends Perm
/*      */ {
/*      */   public AudioPerm()
/*      */   {
/* 3741 */     super("AudioPermission", "javax.sound.sampled.AudioPermission", new String[] { "play", "record" }, null);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.AudioPerm
 * JD-Core Version:    0.6.2
 */