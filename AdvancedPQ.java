package advancedPQ_Package;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class AdvancedPQ<K extends Comparable <K>,V> {
	
	protected DynamicArray<Entry<K,V>> heapArr ;
	//dynamic array, parent at i, child at 2i + 1, and 2i+2
	//need current mode
	//flexible comparator for min/max mode
	protected  boolean isMin = true;
	protected Entry<K,V> recentEntry;
	protected int posRecEntry = -1;
	
	public Entry<K ,V> parent(int i) {
		if (i == 0) {
			return null;
		}
		return heapArr.arr[(i-1)/2];
	}
	
	public AdvancedPQ() {
		heapArr = new DynamicArray<>();
	}
	

	public Entry<K,V> leftChild( int i ) {
		if ((i*2) + 1 >= heapArr.indexLast) {
			return null;
		}
		return heapArr.arr[(2*i)+1];
	}
	
	public Entry<K,V> RightChild( int i ) {
		if ((i*2) + 2 >= heapArr.indexLast) {
			return null;
		}
		return heapArr.arr[(2*i)+2];
	}
	
	public void swap (int i, int j) {
		Entry<K,V> temp = heapArr.arr[i];
		heapArr.arr[i] = heapArr.arr[j];
		heapArr.arr[j] = temp;
	}
	
	
	public boolean mode() {
		return isMin;
	}
	
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
	
	/*
	public int findClosestTwo(int i) {
		int temp = 1;
		while (i > temp) {
			temp*=2;
		}
		return temp;
	}
	*/
	
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
	
	public void insert(K k,V v) {
		Entry<K,V> temp = new Entry<K,V>(k,v);
		heapArr.add(temp);
		posRecEntry = upHeap(heapArr.indexLast -1);
		recentEntry = heapArr.arr[posRecEntry];
	}
	
	public Entry<K,V> removeTop(){
		//removes top and replaces with most recent element
		Entry<K,V> temp = heapArr.arr[0];
		swap(0, heapArr.indexLast-1);
		heapArr.remove(heapArr.indexLast -1);
		downHeap(0);
		reHeap();
		return temp;
		
	}
	
	
	public void toggle() {
		isMin = !isMin;
		//makes the bottom nodes the top nodes
		for (int i = (heapArr.indexLast -1 ) /2; i >= 0; i--) {
			downHeap(i);
		}
		reHeap();
	}
	
	public Entry<K,V> top(){
		return heapArr.arr[0];
	}
	
	//throws a NoSuchElementExpetion
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
	
	public boolean state() {
		return isMin;
	}
	
	public boolean isEmpty() {
		if (heapArr.arr[0] == null) {
			return true;
		}
		return false;
	}
	
	public int size() {
		return heapArr.indexLast;
	}
	
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
	
	public void merge(AdvancedPQ<K,V> otherAPQ) {
		for (int i = 0; i< otherAPQ.heapArr.indexLast;i++) {
			this.insert(((Entry<K,V>) otherAPQ.heapArr.arr[i]).getKey(), ((Entry<K,V>) otherAPQ.heapArr.arr[i]).getValue());
		}
		for (int i = (heapArr.indexLast -1 ) /2; i >= 0; i--) {
			downHeap(i);
		}
	}
	
	public String toString() {
		String ret = "The following are the values of the heap array:\n";
		for (int i = 0; i< heapArr.indexLast;i++) {
			ret+= heapArr.arr[i] + "\n";
		}
		return ret;
	}
	
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

