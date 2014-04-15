package numbercruncher.primeutils;

/**
 * Interface for a caller of the Lucas test.
 */
public interface ILucasCaller
{
  /**
   * Report on the status of the Lucas test.
   * 
   * @param status
   *        the current status
   */
  void reportStatus (LucasStatus status);
}
