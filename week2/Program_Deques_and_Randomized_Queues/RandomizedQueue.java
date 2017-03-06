import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
  
    private Item[] a;         // array of items
    private int n;            // number of elements on stack
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
     
    }
   
    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
   
    // return the number of items on the queue
    public int size() {
        return n;
    }
   
   // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.NullPointerException();
        } 
        if (n == a.length) {         
            assert 2*a.length >= n;
            Item[] temp = (Item[]) new Object[2*a.length];
            for (int i = 0; i < n; i++) {
                temp[i] = a[i];
            }
            a = temp;
        }
        a[n++] = item;  
    }
   
   // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int index = StdRandom.uniform(n);
        Item item = a[index];
        a[index] = a[n-1];  
        a[n-1] = null;
        n--;
        if (n > 0 && n == a.length/4) {          
            assert a.length/2 >= n;
            Item[] temp = (Item[]) new Object[a.length/2];
            for (int i = 0; i < n; i++) {
                temp[i] = a[i];
            }
            a = temp;
        }
        return item;     
    }
   
   // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int index = StdRandom.uniform(n);
        Item item = a[index];
        return item;
        
    }
   
 // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> { 
        private int i;

        public ListIterator() {
            i = n-1;
        }

        public boolean hasNext()  { return i >= 0;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[i--];
        }
    }  
}
