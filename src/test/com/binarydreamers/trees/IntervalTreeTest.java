/*
Copyright 2013 John Thomas McDole

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.binarydreamers.trees;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;

import com.binarydreamers.trees.IntegerInterval;
import com.binarydreamers.trees.Interval;
import com.binarydreamers.trees.IntervalTree;
import com.binarydreamers.trees.LongInterval;
import com.binarydreamers.trees.IntervalTree.SearchNearest;

public class IntervalTreeTest {
	@Test
	public void testRightLeftRotation() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(10, 100));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(15, 150));
		testTree.verifyHeight();
		testTree.verifyOrder();
	}

	@Test
	public void testLeftRightRotation() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(100, 200));
		testTree.add(new LongInterval(200, 500));
		testTree.verifyHeight();
		testTree.verifyOrder();
	}

	@Test
	public void testLeftRotation() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(100, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(300, 500));
		testTree.verifyHeight();
		testTree.verifyOrder();
	}

	@Test
	public void testRightRotation() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(100, 500));
		testTree.verifyHeight();
		testTree.verifyOrder();
	}

	@Test
	public void testBalanceInsertion() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);

		// balance insertion, make sure leaf is reflected up.
		testTree.clear();
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(100, 500));
		testTree.add(new LongInterval(400, 2000));
		testTree.verifyHeight();
		testTree.verifyOrder();
	}

	@Test
	public void testTreeContains() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		LongInterval[] intervals = generateLongIntervalArray(20);
		for (LongInterval inter : intervals) {
			testTree.add(inter);
		}

		for (LongInterval inter : intervals) {
			if (!testTree.contains(inter)) {
				fail("contains failed");
			}
		}

		if (!testTree.containsAll(Arrays.asList(intervals))) {
			fail("containsAll failed");
		}

		LongInterval outlier = new LongInterval(301, 700);
		if (testTree.contains(outlier)) {
			fail("contains failed (outlier)");
		}
	}

	@Test
	public void testFromCollection() {
		LongInterval[] intervals = generateLongIntervalArray(20);
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.addAll(Arrays.asList(intervals));
		assertEquals(20, testTree.size());
		assertTrue(testTree.containsAll(Arrays.asList(intervals)));
	}

	@Test
	public void testToObjectArray() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		LongInterval[] intervals = generateLongIntervalArray(20);
		testTree.addAll(Arrays.asList(intervals));
		Object[] testArray = testTree.toArray();
		assertArrayEquals(intervals, testArray);
	}

	@Test
	public void testToArray() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		LongInterval[] intervals = generateLongIntervalArray(20);
		testTree.addAll(Arrays.asList(intervals));
		LongInterval[] testArray = testTree.toArray(new LongInterval[0]);
		assertArrayEquals(intervals, testArray);
		testArray = new LongInterval[intervals.length];
		testTree.toArray(testArray);
		assertArrayEquals(intervals, testArray);
	}

	@Test
	public void testIteratorRemove() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		LongInterval[] intervals = generateLongIntervalArray(100);
		testTree.addAll(Arrays.asList(intervals));
		Iterator<Interval<Long>> it = testTree.iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
			testTree.verifyHeight();
			testTree.verifyOrder();
		}
		assertEquals(0, testTree.size());

		testTree.addAll(Arrays.asList(intervals));
		it = testTree.iterator();
		try {
			it.remove();
			fail("Remove without next() should have failed with IllegalStateException");
		} catch (IllegalStateException e) {
		}
		Interval<Long> first = it.next();
		assertTrue(testTree.contains(first));
		it.remove();
		assertFalse(testTree.contains(first));
		try {
			it.remove();
			fail("Double remove should have failed with IllegalStateException");
		} catch (IllegalStateException e) {
		}
	}

	@Test
	public void testIteratorConcurrentFailure() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.addAll(Arrays.asList(generateLongIntervalArray(5)));
		Iterator<Interval<Long>> it = testTree.iterator();
		testTree.add(new LongInterval(101, 102));
		try {
			it.next();
			fail("Should have gotten a ConcurrentModificationException");
		} catch (ConcurrentModificationException e) {
		}
		try {
			it.hasNext();
			fail("Should have gotten a ConcurrentModificationException");
		} catch (ConcurrentModificationException e) {
		}
	}

	@Test
	public void testNearestSearch() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(100, 500));
		Interval<Long> near = testTree.searchNearestElement(new LongInterval(199, 250));
		assertEquals(new Long(200), near.getLower());

		near = testTree.searchNearestElement(new LongInterval(201, 250));
		assertEquals(new Long(200), near.getLower());

		near = testTree.searchNearestElement(new LongInterval(200, 250));
		assertEquals(new Long(200), near.getLower());

		near = testTree.searchNearestElement(new LongInterval(150, 250));
		assertEquals(new Long(100), near.getLower());

		near = testTree.searchNearestElement(new LongInterval(199, 250),
				SearchNearest.SEARCH_NEAREST_ROUNDED_DOWN);
		assertEquals(new Long(100), near.getLower());

		near = testTree.searchNearestElement(new LongInterval(101, 250),
				SearchNearest.SEARCH_NEAREST_ROUNDED_UP);
		assertEquals(new Long(200), near.getLower());
	}

	@Test
	public void testHeadSet() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(100, 500));

		// Note: 200,201 is less than 200,300. 200,301 would return more elements.
		// This is just a side effect of the comparator.
		SortedSet<Interval<Long>> headSet = testTree.headSet(new LongInterval(200, 201));
		assertNotNull(headSet);
		assertEquals(1, headSet.size()); // Strictly less than 200!
		try {
			headSet.add(new LongInterval(201, 300));
			fail("Adding interval outside of sub-view should have failed");
		} catch (IllegalArgumentException passCase) {
		}
		headSet.add(new LongInterval(100, 300));
		assertEquals(4, testTree.size()); // we're backed back the original tree set
		assertEquals(2, headSet.size());
		LongInterval[] tailArray = headSet.toArray(new LongInterval[0]);
		assertEquals(2, tailArray.length);
		assertEquals(new Long(100), tailArray[0].getLower());
		assertEquals(new Long(300), tailArray[0].getUpper());
		assertEquals(new Long(100), tailArray[1].getLower());
		assertEquals(new Long(500), tailArray[1].getUpper());
	}

	@Test
	public void testTailSet() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(100, 500));
		SortedSet<Interval<Long>> tailSet = testTree.tailSet(new LongInterval(200, 500));
		assertNotNull(tailSet);
		assertEquals(1, tailSet.size());
		try {
			tailSet.add(new LongInterval(200, 499));
			fail("Adding interval outside of sub-view should have failed");
		} catch (IllegalArgumentException passCase) {
		}
		tailSet.add(new LongInterval(200, 501));
		assertEquals(4, testTree.size()); // we're backed back the original tree set
		assertEquals(2, tailSet.size());

		LongInterval[] tailArray = tailSet.toArray(new LongInterval[0]);
		assertEquals(2, tailArray.length);
		assertEquals(new Long(200), tailArray[0].getLower());
		assertEquals(new Long(501), tailArray[0].getUpper());
		assertEquals(new Long(300), tailArray[1].getLower());
		assertEquals(new Long(1000), tailArray[1].getUpper());
	}

	@Test
	public void testSubsetClear() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(100, 500));
		testTree.add(new LongInterval(0, 100));
		SortedSet<Interval<Long>> subSet = testTree.subSet(new LongInterval(100, 101),
				new LongInterval(200, 301));
		assertNotNull(subSet);
		assertEquals(2, subSet.size());
		assertEquals(4, testTree.size());
		subSet.clear();
		assertEquals(0, subSet.size());
		assertEquals(2, testTree.size());
	}

	@Test
	public void testSubsetContains() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(300, 1000));
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(100, 500));
		testTree.add(new LongInterval(0, 100));
		SortedSet<Interval<Long>> subSet = testTree.subSet(new LongInterval(100, 101),
				new LongInterval(200, 301));
		assertNotNull(subSet);
		assertTrue(subSet.contains(new LongInterval(200, 300)));
		assertFalse(subSet.contains(new LongInterval(300, 1000)));
		assertFalse(subSet.contains(new LongInterval(0, 100)));
	}

	@Test
	public void testIntervalSearch() {
		IntervalTree<Long> testTree = new IntervalTree<Long>(
				LongInterval.comparator);
		testTree.add(new LongInterval(200, 300));
		testTree.add(new LongInterval(300, 500));
		testTree.add(new LongInterval(100, 500));
		testTree.add(new LongInterval(-100, 0));
		testTree.add(new LongInterval(-50, 100));
		testTree.add(new LongInterval(400, 1000));

		LongInterval search = new LongInterval(100, 350);
		List<Interval<Long>> searchInterval = testTree.searchInterval(search);
		assertEquals(3, searchInterval.size());
	}

	// Ran enough times to verify that all legs of the remove/rebalance worked
	// LongInterval[] elements = generateLongIntervalArray(5000);;
	// testTree.addAll(Arrays.asList(elements));
	//
	// testTree.verifyHeight();
	// testTree.verifyOrder();
	//
	// Random rand = new Random(424242);
	// try {
	// while (testTree.size() > 0) {
	// int i = (Math.abs(rand.nextInt()) % elements.length);
	// if (elements[i] == null)
	// continue;
	//
	// testTree.remove(elements[i]);
	// testTree.verifyHeight();
	// testTree.verifyOrder();
	// elements[i] = null;
	// }
	// } catch (IllegalStateException e) {
	// System.out.println("Exception caught (boo!) : " + e);
	// e.printStackTrace();
	// }

	private static LongInterval[] generateLongIntervalArray(int size) {
		LongInterval[] elements;
		elements = new LongInterval[size];
		for (int y = 0; y < elements.length; y++) {
			long low = y * 50;
			long high = low + ((y % 10) + 1) * 100;
			elements[y] = new LongInterval(low, high);
		}
		return elements;
	}

	@Test
	public void testIntegerTree() {
		IntervalTree<Integer> testTree = new IntervalTree<Integer>(
				IntegerInterval.comparator);

		// balance insertion, make sure leaf is reflected up.
		testTree.clear();
		testTree.add(new IntegerInterval(200, 300));
		testTree.add(new IntegerInterval(300, 1000));
		testTree.add(new IntegerInterval(100, 500));
		testTree.add(new IntegerInterval(400, 2000));
		testTree.verifyHeight();
		testTree.verifyOrder();
		assertEquals(4, testTree.size());
		assertTrue(testTree.contains(new IntegerInterval(100, 500)));
		List<Interval<Integer>> search = testTree.searchInterval(new IntegerInterval(150, 350));
		assertEquals(3, search.size());
	}
}
