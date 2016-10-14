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

import com.binarydreamers.trees.BinaryTree.IntervalNode;

import java.util.Comparator;
import java.util.Random;

class MemoryLoad {
  static class StringInterval implements Interval<String> {
    public String string;
    StringInterval(String string) {
      this.string = string;
    }
    
    @Override
    public String getLower() { return this.string; }
    @Override
    public String getUpper() { return this.string; }
    static final Comparator<Interval<String>> comparator = new Comparator<Interval<String>>() {
      @Override
      public int compare(Interval<String> o1, Interval<String> o2) {
        return o1.getLower().compareTo(o2.getLower());
      }
    };
  }

  public static long memUsage() throws InterruptedException {
    collectPoop();
    collectPoop();
    Runtime runtime = Runtime.getRuntime();
    long totalMemory = runtime.totalMemory();

    collectPoop();
    collectPoop();
	long freeMemory = runtime.freeMemory();
	System.out.println("Total memory:" + totalMemory + " Free memory: " + freeMemory);
    return totalMemory - freeMemory;
  }

	private static void collectPoop() throws InterruptedException {
		System.gc();
		Thread.sleep(500);
		System.runFinalization();
		Thread.sleep(500);
	}

	/**
	 * A node in the interval tree
	 */
	static class Something {
		Something left;
		Something right;
		Object  object;
		int balanceFactor;
	}

  public static void main(String[] args) throws InterruptedException {
    int testSize = 500000;
    long[] memUsage = new long[2];
    Random rand = new Random();
    final int sampleSize = 500;

    long size = MyAgent.getObjectSize(new IntervalNode<String>());
    System.out.println("agentsize IntervalNode<String>:" + size);
    size = MyAgent.getObjectSize(new com.binarydreamers.trees.IntervalTree.IntervalNode<String>());
    System.out.println("agentsize IntervalTree.IntervalNode<String>:" + size);

    size = MyAgent.getObjectSize(new Object());
    System.out.println("agentsize Object:" + size);
    size = MyAgent.getObjectSize(new Something());
    System.out.println("agentsize Something:" + size);

    BinaryTree.IntervalNode<String>[] array1 = (IntervalNode<String>[]) new BinaryTree.IntervalNode<?>[100];
    size = MyAgent.getObjectSize(array1);
    System.out.println("agentsize IntervalTree.IntervalNode<String>[100]:" + size);
    
//    IntervalNode<?>[] asdf = new IntervalNode<?>[sampleSize];
//    memUsage[0] = memUsage();
//    for(int i = 0; i < sampleSize; i++) {
//    	asdf[i] = new IntervalNode<String>();
//    }
//    memUsage[1] = memUsage();
//    System.out.println("Usage: " + ((memUsage[1]-memUsage[0])/(float)sampleSize));
//
//    com.binarydreamers.trees.IntervalTree.IntervalNode<?>[] asdf2 = new com.binarydreamers.trees.IntervalTree.IntervalNode<?>[sampleSize];
//    memUsage[0] = memUsage();
//    for(int i = 0; i < sampleSize; i++) {
//    	asdf2[i] = new com.binarydreamers.trees.IntervalTree.IntervalNode<String>();
//    }
//    memUsage[1] = memUsage();
//    System.out.println("Usage: " + ((memUsage[1]-memUsage[0])/(float)sampleSize));
//
//    
//    String[] strings = new String[testSize];
//    memUsage[0] = memUsage();
//    for(int i = 0; i < testSize; i++) {
//      strings[i] = new String(Long.toHexString(rand.nextLong()));
//    }
//    memUsage[1] = memUsage();
//    System.out.println("Usage: " + ((memUsage[1]-memUsage[0])/(float)testSize));
//  
//    memUsage[0] = memUsage();
//    BinaryTree<String> testTree = new BinaryTree<String>();
//    for(String string : strings) {
//      testTree.add(string); 
//    }
//    memUsage[1] = memUsage();
//    System.out.println("Usage: " + ((memUsage[1]-memUsage[0])/(float)testSize));
//
//    memUsage[0] = memUsage();
//    IntervalTree<String> testTree2 = new IntervalTree<String>();
//    for(String string : strings) {
//    	testTree2.add(new StringInterval(string)); 
//    }
//    memUsage[1] = memUsage();
//    System.out.println("Usage: " + ((memUsage[1]-memUsage[0])/(float)testSize));
//
//    com.binarydreamers.trees.IntervalTree.IntervalNode<?>[] asdf3 = new com.binarydreamers.trees.IntervalTree.IntervalNode<?>[sampleSize];
//    memUsage[0] = memUsage();
//    for(int i = 0; i < sampleSize; i++) {
//    	asdf3[i] = new com.binarydreamers.trees.IntervalTree.IntervalNode<String>();
//    }
//    memUsage[1] = memUsage();
//    System.out.println("Usage: " + ((memUsage[1]-memUsage[0])/(float)sampleSize));

  
  }
}