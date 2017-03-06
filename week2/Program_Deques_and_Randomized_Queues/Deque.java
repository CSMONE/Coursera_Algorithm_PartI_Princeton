import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
  
    private Node<Item> first;  
    private Node<Item> last;
    private int n;              

  // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> previous;
        private Node<Item> next;
    }
    
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }
   
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }
   
   // return the number of items on the deque
    public int size() {
        return n;
    }
   
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {  
            throw new java.lang.NullPointerException();  
        }
     
        if (isEmpty()) {
            first = new Node<Item>();
            first.item = item;
            first.previous = null;
            first.next = null;
            last = first;
        }
        else {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.previous = null;
            first.next = oldfirst;
            oldfirst.previous = first;
        }
        n++;
    }
   
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {  
            throw new java.lang.NullPointerException();  
        }
     
        if (isEmpty()) {
            first = new Node<Item>();
            first.item = item;
            first.previous = null;
            first.next = null;
            last = first;
        }
        else {
            Node<Item> oldlast = last;
            last = new Node<Item>();
            last.item = item;
            last.previous = oldlast;
            last.next = null;
            oldlast.next = last;
        }
        n++;
     }
   
   
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }
        Node<Item> oldfirst = first;
        first = first.next;
        n--;
        
        if (isEmpty()) {
            last = null;
            first = null;
        }
        else {
            first.previous = null;
        }
        
        
        return oldfirst.item;
    }
   
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }
        Node<Item> oldlast = last;
        last = last.previous;
        n--;
        
        if (isEmpty()) {
            last = null;
            first = null;
        }
        else {
            last.next = null;
        }
        
        
        return oldlast.item;
    }
   
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator(first);
    }

    private class ListIterator implements Iterator<Item> { 
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
   
    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("+")) {
                deque.addFirst(item);
            }
            if (item.equals("*")) {
                deque.addLast(item);
            }
            
            if (item.equals("-")) {
                String reItem = deque.removeFirst();
            }

            if (item.equals("/")) {
                String reItem = deque.removeLast();
            }
            StdOut.println("(" + deque.size() + " left on queue)");
        }
    }
    
}

