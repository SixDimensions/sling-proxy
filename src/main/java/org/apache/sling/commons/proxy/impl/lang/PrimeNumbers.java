/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.commons.proxy.impl.lang;

/**
 * The first 169 prime numbers
 */
public final class PrimeNumbers {

	private static final PrimeNumbers INSTANCE = new PrimeNumbers();
	private final int[] primes;

	/**
	 * Construct a new instance of the prime numbers
	 */
	private PrimeNumbers() {
		// ** 14 per row
		primes = new int[] { 0, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
				43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107,
				109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173,
				179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239,
				241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311,
				313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383,
				389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457,
				461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541,
				547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613,
				617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683,
				691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769,
				773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857,
				859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941,
				947, 953, 967, 971, 977, 983, 991, 997 };
	}

	/**
	 * Get the PrimeNumbers instance.
	 * 
	 * @return the prime numbers instance
	 */
	public static PrimeNumbers getInstance() {
		return INSTANCE;
	}

	/**
	 * Return 'n'th prime number. A 0 value for 'n' will return the first prime
	 * number.
	 * 
	 * @param n
	 *            int - the requested prime number. A value less than one will
	 *            return the first prime number.
	 * 
	 * @return int
	 */
	public int get(int n) {
		n = (n < 1 ? 1 : n);
		if (n >= primes.length) {
			String msg = "We don't have a prime number for index " + n + ".";
			throw new IndexOutOfBoundsException(msg);
		}
		return primes[n];
	}
}
