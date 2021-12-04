/*
 * Copyright (c) 2021, Jérôme Brongniart
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without 
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package hexamap.coordinates;

import static java.lang.Math.abs;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

import hexamap.regions.AbstractRegion;

public abstract class Coordinate extends AbstractRegion<Coordinate> {

    @Override
    public boolean contains(Coordinate coordinate) {
        return getX() == coordinate.getX() && getY() == coordinate.getY();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return contains((Coordinate) obj);
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean remove(Object coordinate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Coordinate getCenter() {
        return this;
    }

    @Override
    public void setCenter(Coordinate center) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return 1;
    }

    private class IteratorCoordinate implements Iterator<Coordinate> {

        private final Coordinate c;
        private boolean nextCalled = false;

        public IteratorCoordinate(Coordinate c) {
            this.c = c;
        }

        @Override
        public boolean hasNext() {
            return !nextCalled;
        }

        @Override
        public Coordinate next() {
            nextCalled = true;
            return c;
        }

    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new IteratorCoordinate(this);
    }

    @Override
    public Coordinate getRandom(Random random) {
        return this;
    }

    public abstract int getX();

    public abstract int getY();

    public abstract int getZ();

    public abstract Coordinate getZero();

    public abstract Coordinate createCoordinate(int x, int y);

    public abstract Coordinate createCoordinateXZ(int x, int z);

    public abstract Coordinate createCoordinateYZ(int y, int z);

    public abstract Coordinate add(Direction direction, int range);

    public abstract void move(Direction direction, int range);

    public int distance(Coordinate other) {
        return Math.max(abs(getX() - other.getX()), Math.max(abs(getY() - other.getY()), abs(getZ() - other.getZ())));
    }

    public class NeigboursIterator implements Iterator<Coordinate> {

        private final boolean all;
        private final Coordinate center;
        private int range;
        private int nbIters;
        private int nbDir;
        private Coordinate current;
        private Direction direction;

        private final Direction INIT_DIRECTION = Direction.NORD;

        public NeigboursIterator(Coordinate center, int range, boolean all) {

            this.all = all;
            this.range = Math.abs(range);
            this.center = center;

            current = null;
        }

        @Override
        public boolean hasNext() {
            if (all) {
                return range > 1 || nbDir < 6 || nbIters < range;
            } else {
                return nbDir < 6 || nbIters < range;
            }
        }

        @Override
        public Coordinate next() {
            if (current == null) {
                current = center.add(INIT_DIRECTION, range);
                direction = INIT_DIRECTION.next(2);

                nbIters = 1;
                nbDir = 1;
            } else if (nbIters < range) {
                current.move(direction, 1);
                nbIters++;
            } else if (nbDir < 6) {
                current.move(direction, 1);
                direction = direction.next();

                nbIters = 1;
                nbDir++;
            } else if (all) {
                range--;
                nbIters = 1;
                nbDir = 1;
                direction = INIT_DIRECTION.next(2);
                current.move(direction, 1);
            } else {
                throw new RuntimeException("Calling next() while hasNext() is false");
            }

            return current;
        }
    }

    public Iterable<Coordinate> getNeigbours(int range) {
        return () -> new NeigboursIterator(this, range, false);
    }

    public Iterable<Coordinate> getNeigbours() {
        return getNeigbours(1);
    }

    public Iterable<Coordinate> getAllNeigbours(int range) {
        return () -> new NeigboursIterator(this, range, true);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
