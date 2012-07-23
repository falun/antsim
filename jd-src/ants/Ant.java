package ants;

public abstract interface Ant
{
  public abstract Action getAction(Surroundings paramSurroundings);

  public abstract byte[] send();

  public abstract void receive(byte[] paramArrayOfByte);
}

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.Ant
 * JD-Core Version:    0.6.0
 */