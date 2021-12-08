/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.incubator.grammar;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.BitChromosome;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class CodonsTest {

	@Test(dataProvider = "chromosomeSizes")
	public void toByteArray(final int size) {
		final var ch = BitChromosome.of(size);

		assertThat(Codons.toByteArray(ch)).isEqualTo(ch.toByteArray());
	}

	@DataProvider
	public Object[][] chromosomeSizes() {
		return new Object[][] {
			{1}, {2}, {3}, {7}, {8}, {9}, {10}, {15}, {16}, {17}, {31},
			{32}, {33}, {100}, {1_000}, {10_000}, {100_000}
		};
	}

	@Test(dataProvider = "codonsSizes")
	public void nextIndex(final int size, final int bound) {
		final int[] values = IntStream.range(0, size).toArray();
		final SymbolIndex index = Codons.ofIntArray(values);

		for (int i = 0, n = size*3; i < n; ++i) {
			final int expectedIndex = values[i%values.length]%bound;
			assertThat(index.next(bound)).isEqualTo(expectedIndex);
		}
	}

	@DataProvider
	public Object[][] codonsSizes() {
		return new Object[][] {
			{1, 1},
			{2, 10},
			{3, 2},
			{8, 8},
			{10, 5},
			{16, 10},
			{31, 15},
			{33, 20},
			{100, 5}
		};
	}

}
