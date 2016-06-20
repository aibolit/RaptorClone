/*
 * The MIT License
 *
 * Copyright 2016 Aleks.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Objects;

/**
 *
 * @author Aleks
 */
public class MapBounds {

    private final double minX, maxX, minY, maxY;

    public MapBounds(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.minX) ^ (Double.doubleToLongBits(this.minX) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.maxX) ^ (Double.doubleToLongBits(this.maxX) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.minY) ^ (Double.doubleToLongBits(this.minY) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.maxY) ^ (Double.doubleToLongBits(this.maxY) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MapBounds other = (MapBounds) obj;
        if (Double.doubleToLongBits(this.minX) != Double.doubleToLongBits(other.minX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.maxX) != Double.doubleToLongBits(other.maxX)) {
            return false;
        }
        if (Double.doubleToLongBits(this.minY) != Double.doubleToLongBits(other.minY)) {
            return false;
        }
        if (Double.doubleToLongBits(this.maxY) != Double.doubleToLongBits(other.maxY)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MapBounds{" + "minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY + '}';
    }

}
