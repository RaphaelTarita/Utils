package com.tara.util.container.collection;

import java.util.ArrayList;
import java.util.List;

public class Table<E> {
    private Object[] elementData;
    private int xsize;
    private int ysize;

    public Table(int x, int y) {
        xsize = x;
        ysize = y;
        elementData = new Object[xsize * ysize];
    }

    public void set(int x, int y, E data) {
        checkSize(x, y);
        elementData[xsize * y + x] = data;
    }

    @SuppressWarnings("unchecked")
    public E get(int x, int y) {
        return (E) elementData[xsize * y + x];
    }

    @SuppressWarnings("unchecked")
    public List<E> getRow(int rowIndex) {
        checkSize(xsize - 1, rowIndex);
        ArrayList<E> result = new ArrayList<>(xsize);
        for (int i = xsize * rowIndex; i < xsize * (rowIndex + 1); i++) {
            result.add((E) elementData[i]);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<E> getColumn(int columnIndex) {
        checkSize(columnIndex, ysize - 1);
        ArrayList<E> result = new ArrayList<>(ysize);
        for (int i = columnIndex; i <= xsize * (ysize - 1) + columnIndex; i += xsize) {
            result.add((E) elementData[i]);
        }
        return result;
    }

    private void checkSize(int x, int y) {
        if (x >= xsize) {
            throw new TableIndexOutOfBoundsException(Orientation.X, x);
        } else if (y >= ysize) {
            throw new TableIndexOutOfBoundsException(Orientation.Y, y);
        } else if (x * y >= elementData.length) {
            throw new TableIndexOutOfBoundsException(x * y);
        }
    }
}
