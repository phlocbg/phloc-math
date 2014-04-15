package numbercruncher.primeutils;


/**
 * Primality test that combines the Miller-Rabin and Lucas tests.
 */
public class PrimalityTest
{
  /** number to test for primality */
  private final int m_nP;
  /**
   * number of times to run the Miller-Rabin test
   */
  private final int m_nIterations;

  /**
   * Constructor.
   * 
   * @param p
   *        the number to test for primality
   * @param iterations
   *        the number of times to run the Miller-Rabin test
   */
  public PrimalityTest (final int p, final int iterations)
  {
    this.m_nP = p;
    this.m_nIterations = iterations;
  }

  /**
   * Perform the primality test.
   * 
   * @return true if p is prime, false if p is composite
   */
  public boolean test ()
  {
    return (new MillerRabinTest (m_nP, m_nIterations, null)).test () && (new LucasTest (m_nP, null)).test ();
  }
}
