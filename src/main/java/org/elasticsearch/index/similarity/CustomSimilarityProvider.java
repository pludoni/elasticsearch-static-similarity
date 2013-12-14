package org.elasticsearch.index.similarity;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;

/**
 * Simple {@link SimilarityProvider} for a {@link CustomSimilarity}
 *
 * @author tlrx
 *
 */
public class CustomSimilarityProvider extends AbstractSimilarityProvider {

	private final CustomSimilarity similarity;

	@Inject
	public CustomSimilarityProvider( @Assisted String name, @Assisted Settings settings) {
		super(name);
		this.similarity = new CustomSimilarity();
	}

	public CustomSimilarity get() {
		return similarity;
	}
}
