package sun.java2d.cmm;

import java.awt.color.ICC_Profile;

public abstract interface PCMM
{
  public abstract long loadProfile(byte[] paramArrayOfByte);

  public abstract void freeProfile(long paramLong);

  public abstract int getProfileSize(long paramLong);

  public abstract void getProfileData(long paramLong, byte[] paramArrayOfByte);

  public abstract void getTagData(long paramLong, int paramInt, byte[] paramArrayOfByte);

  public abstract int getTagSize(long paramLong, int paramInt);

  public abstract void setTagData(long paramLong, int paramInt, byte[] paramArrayOfByte);

  public abstract ColorTransform createTransform(ICC_Profile paramICC_Profile, int paramInt1, int paramInt2);

  public abstract ColorTransform createTransform(ColorTransform[] paramArrayOfColorTransform);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.PCMM
 * JD-Core Version:    0.6.2
 */