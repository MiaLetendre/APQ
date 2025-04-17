package advancedPQ_Package;
//only uses one heap, increasing the time complexity of reheap but reducing space complexity

import java.util.Arrays;
import java.util.NoSuchElementException;

public class AdvancedPQ<K extends Comparable <K>,V> {
	
	protected DynamicArray<Entry<K,V>> heapArr ;
	//dynamic array, parent at i, child at 2i + 1, and 2i+2
	//flexible comparator for min/max mode
	protected  boolean isMin = true;
	protected Entry<K,V> recentEntry;
	protected int posRecEntry = -1;

	//gives the parent 
	public Entry<K ,V> parent(int i) {
		if (i == 0) {
			return null;
		}
		return heapArr.arr[(i-1)/2];
	}

	//default constructor
	public AdvancedPQ() {
		heapArr = new DynamicArray<>();
	}
	

	//gives the left child locatin
	public Entry<K,V> leftChild( int i ) {
		if ((i*2) + 1 >= heapArr.indexLast) {
			return null;
		}
		return heapArr.arr[(2*i)+1];
	}

	//gives the right child location
	public Entry<K,V> RightChild( int i ) {
		if ((i*2) + 2 >= heapArr.indexLast) {
			return null;
		}
		return heapArr.arr[(2*i)+2];
	}

	//swaps two entries 
	public void swap (int i, int j) {
		Entry<K,V> temp = heapArr.arr[i];
		heapArr.arr[i] = heapArr.arr[j];
		heapArr.arr[j] = temp;
	}
	
	//returns a boolean that tells if the array is a minheap or not, true if it is, false if not
	public boolean mode() {
		return isMin;
	}
	
	//upheaps an entry
	public int upHeap(int i) {
		if (i < 1) {
			return i;
		}else {
			
			//temp is parent of i
			int temp = (i-1)/2; 
			if (i >= heapArr.indexLast || temp < 0) {
				return i;
			}
			if ((isMin && heapArr.arr[i].compareTo(heapArr.arr[temp]) < 0 ) || ((!isMin && heapArr.arr[i].compareTo(heapArr.arr[temp]) > 0 ))) {
				swap(i, temp);
				return upHeap(temp);
				
			}
			return i;
		}
			
	}

	//downheaps an enrty
	public void downHeap(int i ) {
		int left = (i*2) +1;
		int right = (i*2) + 2;
		int track = i;
		
		//checks to make sure the index is not on the bottom
		if (left < heapArr.indexLast && ((isMin &&heapArr.arr[left].compareTo(heapArr.arr[track]) < 0) || (!isMin &&heapArr.arr[left].compareTo(heapArr.arr[track]) > 0))){
				track = left;
		}
		if (right < heapArr.indexLast && ((isMin && heapArr.arr[right].compareTo(heapArr.arr[track]) < 0) || (!isMin && heapArr.arr[right].compareTo(heapArr.arr[track]) < 0))){
				track = right;
			
		}
		if (track != i) {
			swap(i,track);
			downHeap(track);
		}
		
	}

	//reheaps the array 
	public void reHeap() {
		if (isMin) {
		for (int i = (heapArr.indexLast -1 ); i >= 0; i--) {
			downHeap(i);
		}
		} else {
			for (int i = (heapArr.indexLast -1 ); i >= 0; i--) {
				upHeap(i);
			}
		}
			
		
	}

	//inserts an index
	public void insert(K k,V v) {
		Entry<K,V> temp = new Entry<K,V>(k,v);
		heapArr.add(temp);
		posRecEntry = upHeap(heapArr.indexLast -1);
		recentEntry = heapArr.arr[posRecEntry];
	}

	//remooves the top element, replaces with the most recent elements and downheaps
	public Entry<K,V> removeTop(){
		//removes top and replaces with most recent element
		Entry<K,V> temp = heapArr.arr[0];
		swap(0, heapArr.indexLast-1);
		heapArr.remove(heapArr.indexLast -1);
		downHeap(0);
		reHeap();
		return temp;
		
	}
	
	//turns the array from max to min or vice versa
	public void toggle() {
		isMin = !isMin;
		//makes the bottom nodes the top nodes
		for (int i = (heapArr.indexLast -1 ) /2; i >= 0; i--) {
			downHeap(i);
		}
		reHeap();
	}
	
	//gives the top
	public Entry<K,V> top(){
		return heapArr.arr[0];
	}
	
	//removes element, throws a NoSuchElementExpetion, 
	public Entry<K,V> remove(Entry<K,V> e) throws NoSuchElementException {
		for (int i = 0; i < heapArr.indexLast; i++) {
			if (heapArr.arr[i].equals(e)) {
				Entry<K,V> temp = heapArr.arr[i];
				swap(i, posRecEntry);
				heapArr.remove(posRecEntry);
				downHeap(i);
				reHeap();
				return temp;
			}
		}
		throw new NoSuchElementException();
	}

	//replaces a key of an enrty
	public K  replaceKey( Entry<K,V> e, K k) throws NoSuchElementException{
		for (int i = 0; i < heapArr.indexLast; i++) {
			if (heapArr.arr[i].equals(e)) {
				K temp = heapArr.arr[i].getKey();
				heapArr.arr[i].setKey(k);
				reHeap();
				return temp;
			}
		}
		throw new NoSuchElementException();
	}

	//replaces value of an enrty
	public V replaceValue(Entry<K,V> e, V v) throws NoSuchElementException{
		for (int i = 0; i < heapArr.indexLast; i++) {
			if (heapArr.arr[i].equals(e)) {
				V temp = heapArr.arr[i].getValue();
				heapArr.arr[i].setValue(v);
				return temp;
			}
		}
		throw new NoSuchElementException();
	}
	

	//boolean to tell if heap is empty
	public boolean isEmpty() {
		if (heapArr.arr[0] == null) {
			return true;
		}
		return false;
	}

	//return the size of the heap
	public int size() {
		return heapArr.indexLast;
	}

	//gives the nth largest or smallest element depending on the type of heap
	public Entry<K,V> peakAt(int n) throws IndexOutOfBoundsException{
		n = n-1;
		if (n < 0 || n >= heapArr.indexLast) {
			throw new IndexOutOfBoundsException();
		}
		
		AdvancedPQ<K,V> temp = new AdvancedPQ<K,V>();
		if (!isMin) {
			temp.toggle();
		}
		for(int i = 0; i< heapArr.indexLast; i++) {
			temp.insert(heapArr.arr[i].getKey(), heapArr.arr[i].getValue());
		}
		Entry<K,V> tempEntry = null;
		for( int i = 0; i <= n; i++ ) {
			tempEntry = temp.removeTop();
		}
		return tempEntry;
		
	}

	//merges the two APQs
	public void merge(AdvancedPQ<K,V> otherAPQ) {
		for (int i = 0; i< otherAPQ.heapArr.indexLast;i++) {
			this.insert(((Entry<K,V>) otherAPQ.heapArr.arr[i]).getKey(), ((Entry<K,V>) otherAPQ.heapArr.arr[i]).getValue());
		}
		for (int i = (heapArr.indexLast -1 ) /2; i >= 0; i--) {
			downHeap(i);
		}
	}

	//string representation of the heap
	public String toString() {
		String ret = "The following are the values of the heap array:\n";
		for (int i = 0; i< heapArr.indexLast;i++) {
			ret+= heapArr.arr[i] + "\n";
		}
		return ret;
	}

	//string representation in order
	public String toStringOrder() {
		
		AdvancedPQ<K,V> temp = new AdvancedPQ<K,V>();
		for(int i = 0; i< heapArr.indexLast; i++) {
			temp.insert(heapArr.arr[i].getKey(), heapArr.arr[i].getValue());
		}
		String ret = "The following are the values of the heap array in order:\n";
		for( int i = 0; i <= heapArr.indexLast; i++ ) {
			ret += temp.removeTop().toString() + "\n";
		}
		return ret;
	}

	//Class that stores each of the heaps entries
	public class Entry<K extends Comparable<K>, V>{
		protected K key;
		protected V value;
		
		
		public Entry(K k, V v) {
			key = k;
			value = v;
		}
		
		public K getKey() {
			return key;
		}
		
		public void setKey(K k) {
			key = k;
		}
		
		public V getValue() {
			return value;
		}
		public void setValue(V v) {
			value = v;
		}
		
		public boolean equals(Object temp) {
			if (temp == null || temp.getClass() != this.getClass() ) {
				return false;
			}
			Entry<K,V> x = (Entry<K,V>) temp;
			return (x.getKey().equals(this.getKey()) && x.getValue().equals(this.getValue()));
		}
		
		public String toString() {
			return ("(Key: " + key + " and value: " + value + ")");
		}
		
		public int compareTo(Entry<K,V> temp) {
			return this.getKey().compareTo(temp.getKey());
		}
	
	}

	//simple dynamic array class
	public class DynamicArray <E>{
			
			//Default size ten
			protected E arr[];
			protected int indexLast;
			protected int size;
			
			public DynamicArray() {
				this.size = 10;
				this.indexLast = 0;
				this.arr = (E[]) new Entry[size];
			}
			
			public Boolean add(E e) {
				
				if(indexLast >=  (size*.75)) {
						increaseSize();
				}
				arr[indexLast++] = e;
				return false;
			}

			public void add(int index, E element) {
				if(index < 0|| index> indexLast) {
					return;
				}
				if(indexLast >=  (size*.75)) {
					increaseSize();
				}
				for (int i = indexLast; i > index; i--) {
					arr[i] = arr[i-1];
				}
				arr[index] = element;
				indexLast++;
				
			}
			
			public void clear() {
				indexLast = 0;
				
				for(int i = 0; i< size; i++) {
					arr[i] = null;
				}
				size = 10;
			
			}
			
			public E remove(int index) {
				if(index < 0|| index>= size) {
					return null;
				}
				E temp = arr[index];
				for(int i = index; i< size-1; i++) {
					arr[i] = arr[i+1];
				}
				arr[--indexLast] = null;
				return temp;
			}
			
			
			public boolean remove(Object O) {
				for(int i = 0; i < indexLast; i++) {
					if(arr[i]!= null && arr[i].equals(O)) {
							remove(i);
							break;
					}
				}
				return false;
				
			}
			
			public int size() {
				return indexLast;	
			}
			
			public String toString() {
				if (indexLast == 0) {
					return "Array currently has no elements";
				}
				String ret = "The following " + arr[0].getClass() + "s are the elements of the array: ";
				for(int i = 0; i< indexLast; i++) {
					ret += arr[i] + " ";
				}
				return ret;
			}
			
			public void increaseSize() {
				size = size*2;
				arr = Arrays.copyOf(arr, size);
			}
	}
}

