package test.junit.org.logisticPlanning.tsp.solving.utils.edgeData;

/**
 * Testing a configuration of the EdgeNumber facility.
 */
public class AsymmetricEdgeDouble_0_2 extends _EdgeNumberTest {

  /** create the AsymmetricEdgeDouble_0_2 */
  public AsymmetricEdgeDouble_0_2() {
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
    return Short.MAX_VALUE;
  }

  /** {@inheritDoc} */
  @Override
  final int floatType() {
    return 2;
  }

}
