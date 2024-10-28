package businesslogic;

import java.util.List;
import java.util.NoSuchElementException;

public class ExtendedIteratorImpl<Object> implements ExtendedIterator<Object>{

	private List<Object> list;
    private int currentIndex;

    public ExtendedIteratorImpl(List<Object> list) {
        this.list = list;
        this.currentIndex = 0; 
    }
    
	@Override
	public boolean hasNext() {
		return currentIndex < list.size();
	}

	@Override
	public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return list.get(currentIndex++);
	}

	@Override
	public Object previous() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		return list.get(currentIndex--);
	}

	@Override
	public boolean hasPrevious() {
        return currentIndex >= 0;
	}

	@Override
	public void goFirst() {
        currentIndex = 0;		
	}

	@Override
	public void goLast() {
        currentIndex = list.size() - 1;		
	}

}
