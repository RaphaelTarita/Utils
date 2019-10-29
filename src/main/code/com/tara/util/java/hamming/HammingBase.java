package com.tara.util.java.hamming;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class HammingBase implements List<HammingChunk> {
    private int hammingChunkSize;
    private List<HammingChunk> data = new ArrayList<>();

    private void chunkRaw(byte[] stream) {
        int count = 0;
        byte[] chunkbuf = new byte[hammingChunkSize];
        for (byte b : stream) {
            chunkbuf[count] = b;
            if (count++ >= hammingChunkSize) {
                count = 0;
                data.add(new HammingChunk(chunkbuf));
            }
        }
    }

    private void chunkSplit(List<Boolean> stream) {
        int count = 0;
        List<Boolean> chunkbuf = new ArrayList<>(hammingChunkSize);
        for (boolean b : stream) {
            chunkbuf.add(b);
            if (count++ >= hammingChunkSize) {
                count = 0;
                data.add(new HammingChunk(chunkbuf));
                chunkbuf.clear();
            }
        }
    }

    private byte[] streamRaw() {
        byte[] res = new byte[hammingChunkSize * data.size()];
        int i = 0;
        for (HammingChunk c : data) {
            for (byte b : c.raw()) {
                res[i] = b;
                i++;
            }
        }
        return res;
    }

    private List<HammingBit> streamSplit() {
        List<HammingBit> res = new ArrayList<>(hammingChunkSize * data.size());
        for (HammingChunk c : data) {
            res.addAll(c.split());
        }
        return res;
    }

    protected HammingBase(byte[] rawInput, int chunkSize) {
        hammingChunkSize = chunkSize;
        chunkRaw(rawInput);
        splitUp();
    }

    protected HammingBase(List<Boolean> splitInput, int chunkSize) {
        hammingChunkSize = chunkSize;
        chunkSplit(splitInput);
        merge();
    }

    protected void splitUp() {
        for (HammingChunk c : data) {
            c.splitUp();
        }
    }

    protected void merge() {
        for (HammingChunk c : data) {
            c.merge();
        }
    }

    public byte[] raw() {
        return streamRaw();
    }

    public List<HammingBit> split() {
        return streamSplit();
    }

    public int chunkSize() {
        return hammingChunkSize;
    }

    // List<HammingChunk> implementations forward to 'data' field
    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public Iterator<HammingChunk> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }

    @Override
    public boolean add(HammingChunk hammingChunk) {
        return data.add(hammingChunk);
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends HammingChunk> c) {
        return data.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends HammingChunk> c) {
        return data.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public HammingChunk get(int index) {
        return data.get(index);
    }

    @Override
    public HammingChunk set(int index, HammingChunk element) {
        return data.set(index, element);
    }

    @Override
    public void add(int index, HammingChunk element) {
        data.add(index, element);
    }

    @Override
    public HammingChunk remove(int index) {
        return data.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<HammingChunk> listIterator() {
        return data.listIterator();
    }

    @Override
    public ListIterator<HammingChunk> listIterator(int index) {
        return data.listIterator(index);
    }

    @Override
    public List<HammingChunk> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }
}
