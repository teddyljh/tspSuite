package test.junit.org.logisticPlanning.tsp.solving.utils.edgeData;

/**
 * Testing a configuration of the EdgeNumber facility.
 */
public class AsymmetricEdgeInt_m1_2 extends _EdgeNumberTest {

  /** create the AsymmetricEdgeInt_m1_2 */
  public AsymmetricEdgeInt_m1_2() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  final boolean isSymmetric() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  final int n() {
    return 2;
  }

  /** {@inheritDoc} */
  @Override
  final long getMaxValue() {
    return ((java.lang.Integer.MAX_VALUE) + (-1l));
  }

  /** {@inheritDoc} */
  @Override
  final int floatType() {
    return 0;
  }

}
