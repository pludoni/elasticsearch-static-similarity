package org.elasticsearch.index.similarity;

import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.SmallFloat;

/**
 * Custom similarity class
 *
 * @author outsmartin
 *
 */
public class CustomSimilarity extends TFIDFSimilarity {
	/** Sole constructor: parameter-free */
	public CustomSimilarity() {
	}
	private static final float[] NORM_TABLE = new float[256];
	static {
	    for (int i = 0; i < 256; i++) {
	      NORM_TABLE[i] = SmallFloat.byte315ToFloat((byte)i);
	    }
	  }
	/** Implemented as <code>overlap / maxOverlap</code>. */
	@Override
	public float coord(int overlap, int maxOverlap) {
		return 1; //overlap / (float) maxOverlap;
	}

	/** Implemented as <code>1/sqrt(sumOfSquaredWeights)</code>. */
	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		return (float) 1;
	}

	/**
	 * Implemented as <code>state.getBoost()*lengthNorm(numTerms)</code>, where
	 * <code>numTerms</code> is {@link FieldInvertState#getLength()} if
	 * {@link #setDiscountOverlaps} is false, else it's
	 * {@link FieldInvertState#getLength()} -
	 * {@link FieldInvertState#getNumOverlap()}.
	 *
	 * @lucene.experimental
	 */
	@Override
	public float lengthNorm(FieldInvertState state) {
		return state.getBoost();
	}

	/** Implemented as <code>sqrt(freq)</code>. */
	@Override
	public float tf(float freq) {
		return (float) 1;
	}

	/** Implemented as <code>1 / (distance + 1)</code>. */
	@Override
	public float sloppyFreq(int distance) {
		return 1.0f;
	}

	/** The default implementation returns <code>1</code> */
	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		return 1;
	}

	/** Implemented as <code>log(numDocs/(docFreq+1)) + 1</code>. */
	@Override
	public float idf(long docFreq, long numDocs) {
		return (float) 1;
	}

	/**
	 * True if overlap tokens (tokens with a position of increment of zero) are
	 * discounted from the document's length.
	 */
	protected boolean discountOverlaps = true;

	/**
	 * Determines whether overlap tokens (Tokens with 0 position increment) are
	 * ignored when computing norm. By default this is true, meaning overlap
	 * tokens do not count when computing norms.
	 *
	 * @see #computeNorm
	 */
	public void setDiscountOverlaps(boolean v) {
		discountOverlaps = v;
	}

	/**
	 * Returns true if overlap tokens are discounted from the document's length.
	 *
	 * @see #setDiscountOverlaps
	 */
	public boolean getDiscountOverlaps() {
		return discountOverlaps;
	}

	@Override
	public String toString() {
		return "DefaultSimilarity";
	}

	  @Override
	  public final long encodeNormValue(float f) {
	    return SmallFloat.floatToByte315(f);
	  }

	  /**
	* Decodes the norm value, assuming it is a single byte.
	*
	* @see #encodeNormValue(float)
	*/
	  @Override
	  public final float decodeNormValue(long norm) {
	    return NORM_TABLE[(int) (norm & 0xFF)]; // & 0xFF maps negative bytes to positive above 127
	  }
}
