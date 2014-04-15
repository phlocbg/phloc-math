package numbercruncher.primeutils;

/**
 * Interface for a caller of the Miller-Rabin test.
 */
public interface IMillerRabinCaller
{
  /**
   * Report on the status of the Miller-Rabin test.
   * 
   * @param status
   *        the current status
   */
  void reportStatus (MillerRabinStatus status);
}
