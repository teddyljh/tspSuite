package test.junit.org.logisticPlanning.tsp.solving.utils.edgeData;

/**
 * Testing a configuration of the EdgeNumber facility.
 */
public class SymmetricEdgeByte_0_64 extends _EdgeNumberTest {

  /** create the SymmetricEdgeByte_0_64 */
  public SymmetricEdgeByte_0_64() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  final boolean isSymmetric() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  final int n() {
    return 64;
  }

  /** {@inheritDoc} */
  @Override
  final long getMaxValue() {
    return ((java.lang.Byte.MAX_VALUE));
  }

  /** {@inheritDoc} */
  @Override
  final int floatType() {
    return 0;
  }

}
