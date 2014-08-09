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
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics;

import static org.jenetics.util.math.normalize;

import java.util.Arrays;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date$</em>
 */
public class ProbabilitySelectorTest {

	private static double[] array(final int size, final Random random) {
		final double[] array = new double[size];
		for (int i = 0; i < array.length; ++i) {
			array[i] = random.nextDouble();
		}

		return array;
	}

	@Test(dataProvider = "arraySize")
	public void sort(final Integer size) {
		final double[] probabilities = array(size, new Random());

		final int[] indexes = ProbabilitySelector.sort(probabilities);
		final double[] sorted = probabilities.clone();
		Arrays.sort(sorted);

		for (int i = 0; i < indexes.length; ++i) {
			Assert.assertEquals(probabilities[indexes[i]], sorted[i]);
		}
	}

	@DataProvider(name = "arraySize")
	public Object[][] arraySize() {
		return new Object[][]{
			{1}, {2}, {3}, {5}, {11},
			{100}, {1000}, {10_000}, {100_000}, {500_000}
		};
	}

	@Test(dataProvider = "arraySize")
	public void revert(final Integer size) {
		final double[] probabilities = array(size, new Random(12));
		normalize(probabilities);
		final int[] indexes = ProbabilitySelector.sort(probabilities);

		final double[] inverted = ProbabilitySelector.revert(probabilities);
		final int[] invertedIndexes = ProbabilitySelector.sort(inverted);

		for (int i = 0; i < indexes.length; ++i) {
			Assert.assertEquals(invertedIndexes[i], indexes[indexes.length - i - 1]);
		}
	}

//	private static double[] invert(final double[] probabilities) {
//		final double multiplier = 1.0/(probabilities.length - 1.0);
//		for (int i = 0; i < probabilities.length; ++i) {
//			probabilities[i] = (1.0 - probabilities[i])*multiplier;
//		}
//		return probabilities;
//	}

}
