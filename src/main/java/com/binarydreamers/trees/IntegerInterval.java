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

import java.util.Comparator;

public class IntegerInterval implements Interval<Integer> {
	int low;
	int high;

	public IntegerInterval(int lower, int upper) {
		low = lower;
		high = upper;
	}

	@Override
	public Integer getLower() {
		return low;
	}

	@Override
	public Integer getUpper() {
		return high;
	}

	@Override
	public String toString() {
		return "long(" + low + ", " + high + ")";
	}
	static final Comparator<Interval<Integer>> comparator = new Comparator<Interval<Integer>>() {
		@Override
		public int compare(Interval<Integer> o1, Interval<Integer> o2) {
			int value = (int) (o1.getLower() - o2.getLower());
			if(value == 0) {
				value = (int) (o1.getUpper() - o2.getUpper());
			}
			return value;
		}
	};
}