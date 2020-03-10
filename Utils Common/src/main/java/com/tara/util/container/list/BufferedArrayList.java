package com.tara.util.container.list;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BufferedArrayList<E> implements List<E> {
    private static class Buffer<E> {
        private static class BufferNode<E> {
            private E value;
            private BufferNode<E> next;

            public BufferNode(E val) {
                value = val;
                next = null;
            }

            public E get() {
                return value;
            }

            public void add(E val) {
                next = new BufferNode<>(val);
            }

            public BufferNode<E> next() {
                return next;
            }

            public boolean hasNext() {
                return next != null;
            }
        }

        private BufferNode<E> first;
        private BufferNode<E> last;
        private int size;

        public Buffer() {
            first = null;
            last = null;
            size = 0;
        }

        public Buffer(E firstval) {
            first = new BufferNode<>(firstval);
            last = first;
            size = 1;
        }

        public boolean contains(Object o) {
            BufferNode<E> cursor = first;
            if (o == null) {
                do {
                    if (cursor.get() == null) {
                        return true;
                    }
                    cursor = cursor.next();
                } while (cursor.hasNext());
            } else {
                do {
                    if (o.equals(cursor.get())) {
                        return true;
                    }
                    cursor = cursor.next();
                } while (cursor.hasNext());
            }
            return false;
        }

        public boolean remove(Object o) {
            BufferNode<E> cursor = new BufferNode<>(null);
            cursor.next = first;
            if (o == null) {
                do {
                    if (cursor.next().get() == null) {
                        if (cursor.next() == first) {
                            first = cursor.next().next();
                        }
                        cursor.next = cursor.next().next();
                        return true;
                    }
                } while (cursor.next().hasNext());
            } else {
                do {
                    if (o.equals(cursor.next().get())) {
                        if (cursor.next() == first) {
                            first = cursor.next().next();
                        }
                        cursor.next = cursor.next().next();
                        return true;
                    }
                } while (cursor.next().hasNext());
            }
            return false;
        }

        public void buffer(E val) {
            if (size <= 0) {
                first = new BufferNode<>(val);
                last = first;
            } else {
                last.add(val);
                last = last.next();
            }
            size++;
        }

        public E pop() {
            if (size <= 0) {
                throw new IndexOutOfBoundsException("buffer is empty");
            } else {
                E val = first.get();
                if (first == last) {
                    first = null;
                    last = null;
                } else {
                    first = first.next();
                }
                size--;
                return val;
            }
        }

        public int size() {
            return size;
        }
    }

    private class Itr implements Iterator<E> {
        private int arrayCursor;
        private Buffer.BufferNode<E> bufferCursor;
        private int arrayLastRet = -1;
        private Buffer.BufferNode<E> bufferLastRet = null;

        Itr() {
            arrayCursor = startIndex();
            bufferCursor = buffer.first;
        }

        @Override
        public boolean hasNext() {
            return arrayCursor < endIndex() || bufferCursor.hasNext();
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (arrayCursor < endIndex()) {
                arrayLastRet = arrayCursor;
                arrayCursor++;
                return (E) elementData[arrayLastRet];
            } else {
                bufferLastRet = bufferCursor;
                bufferCursor = bufferCursor.next();
                return bufferLastRet.get();
            }
        }

        public void remove() {
            if (arrayLastRet == -1 && bufferLastRet == null) {
                throw new IllegalStateException();
            }
            if (arrayLastRet < endIndex()) {
                BufferedArrayList.this.remove(arrayLastRet);
                arrayCursor = arrayLastRet;
                arrayLastRet = -1;
            } else {
                BufferedArrayList.this.remove(bufferLastRet.get());
                bufferCursor = bufferLastRet;
                bufferLastRet = null;
            }
        }

    }

    private static final int START_SIZE = 0;
    private Object[] elementData;
    private int validIndexStart;
    private int validIndexEnd;
    private Buffer<E> buffer;

    public BufferedArrayList() {
        elementData = new Object[START_SIZE];
        validIndexStart = -1;
        validIndexEnd = -1;
        buffer = new Buffer<>();
    }

    private int startIndex() {
        return validIndexStart != -1
                ? validIndexStart
                : 0;
    }

    private int endIndex() {
        return validIndexEnd != -1
                ? validIndexEnd
                : elementData.length;
    }

    private int flush() {
        int prevEnd = endIndex();
        elementData = Arrays.copyOf(elementData, size());
        int index = prevEnd;
        while (buffer.size() > 0) {
            elementData[index++] = buffer.pop();
        }
        return prevEnd;
    }

    @Override
    public int size() {
        return endIndex() - startIndex() + buffer.size();
    }

    @Override
    public boolean isEmpty() {
        return size() <= 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = startIndex(); i < endIndex(); i++) {
            if (o.equals(elementData[i])) {
                return true;
            }
        }
        return buffer.contains(o);
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    @NotNull
    public Object[] toArray() {
        flush();
        return elementData;
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        flush();
        if (a.length < size()) {
            return (T[]) Arrays.copyOf(elementData, endIndex(), a.getClass());
        }
        System.arraycopy(elementData, startIndex(), a, 0, endIndex());
        if (a.length > size()) {
            for (int i = size(); i < a.length; i++) {
                a[i] = null;
            }
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        buffer.buffer(e);
        return true;
    }

    private void firstRemove(int i) {
        elementData[i] = null;
        validIndexStart = i + 1;
    }

    private void lastRemove(int i) {
        elementData[i] = null;
        validIndexEnd = i;
    }

    private void leftRemove(int i) {
        for (; i > startIndex(); i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[i - 1] = null;
        validIndexStart = i;
    }

    private void rightRemove(int i) {
        for (; i < endIndex() - 1; i++) {
            elementData[i] = elementData[i + 1];
        }
        elementData[i + 1] = null;
        validIndexEnd = i;
    }

    @Override
    public boolean remove(Object o) {
        boolean found = false;
        for (int i = startIndex(); i < endIndex(); i++) {
            if (Objects.equals(o, elementData[i])) {
                found = true;
                if (i == startIndex()) {
                    firstRemove(i);
                } else if (i + 1 == endIndex()) {
                    lastRemove(i);
                } else if (i < (endIndex() - startIndex()) / 2) {
                    leftRemove(i);
                } else {
                    rightRemove(i);
                }
            }
        }
        return found || buffer.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem : c) {
            if (!contains(elem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }
}
