package org.logisticPlanning.utils.document.impl.utils;

import java.io.IOException;
import java.text.NumberFormat;

import org.logisticPlanning.utils.document.spec.AbstractInlineElement;
import org.logisticPlanning.utils.document.spec.Sequence;
import org.logisticPlanning.utils.utils.EmptyUtils;

/**
 * Write a sequence of {@code long}s.
 */
public class SequenceLong extends Sequence {

  /** the number format, or {@code null} if writing dem {@code long}s raw */
  private final NumberFormat m_nf;

  /** the data to be written */
  private final long[] m_data;

  /** the destination */
  private final AbstractInlineElement m_dest;

  /**
   * create a sequence of doubles
   *
   * @param dest
   *          the destination
   * @param nf
   *          the number format, or {@code null} if writing dem
   *          {@code long} s raw
   * @param data
   *          the data to be written
   */
  public SequenceLong(final AbstractInlineElement dest,
      final NumberFormat nf, final long... data) {
    super();
    this.m_nf = nf;
    this.m_data = ((data != null) ? data : EmptyUtils.EMPTY_LONGS);
    this.m_dest = dest;
  }

  /** {@inheritDoc} */
  @Override
  public final int length() {
    return this.m_data.length;
  }

  /** {@inheritDoc} */
  @Override
  public final void write(final int index) throws IOException {
    if (this.m_nf != null) {
      this.m_dest.writeDouble(this.m_data[index], this.m_nf);
    } else {
      this.m_dest.writeDouble(this.m_data[index]);
    }
  }
}
