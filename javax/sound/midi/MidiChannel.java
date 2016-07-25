package javax.sound.midi;

public abstract interface MidiChannel
{
  public abstract void noteOn(int paramInt1, int paramInt2);

  public abstract void noteOff(int paramInt1, int paramInt2);

  public abstract void noteOff(int paramInt);

  public abstract void setPolyPressure(int paramInt1, int paramInt2);

  public abstract int getPolyPressure(int paramInt);

  public abstract void setChannelPressure(int paramInt);

  public abstract int getChannelPressure();

  public abstract void controlChange(int paramInt1, int paramInt2);

  public abstract int getController(int paramInt);

  public abstract void programChange(int paramInt);

  public abstract void programChange(int paramInt1, int paramInt2);

  public abstract int getProgram();

  public abstract void setPitchBend(int paramInt);

  public abstract int getPitchBend();

  public abstract void resetAllControllers();

  public abstract void allNotesOff();

  public abstract void allSoundOff();

  public abstract boolean localControl(boolean paramBoolean);

  public abstract void setMono(boolean paramBoolean);

  public abstract boolean getMono();

  public abstract void setOmni(boolean paramBoolean);

  public abstract boolean getOmni();

  public abstract void setMute(boolean paramBoolean);

  public abstract boolean getMute();

  public abstract void setSolo(boolean paramBoolean);

  public abstract boolean getSolo();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MidiChannel
 * JD-Core Version:    0.6.2
 */