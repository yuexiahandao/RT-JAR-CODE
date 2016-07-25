package javax.sound.midi;

public abstract interface Soundbank
{
  public abstract String getName();

  public abstract String getVersion();

  public abstract String getVendor();

  public abstract String getDescription();

  public abstract SoundbankResource[] getResources();

  public abstract Instrument[] getInstruments();

  public abstract Instrument getInstrument(Patch paramPatch);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.Soundbank
 * JD-Core Version:    0.6.2
 */