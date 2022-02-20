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
package hexamap.regions.base;

import java.util.Iterator;
import java.util.Random;

import hexamap.coordinates.Axial;
import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;

/**
 *
 * @param <CoordinateImpl>
 */
public class Triangle extends BasePolygon {

    public final Direction direction;
    public final int length;

    public Triangle(Direction direction, int length, Coordinate center) {
        super(center);

        if (length < 0) {
            direction = direction.flip();
            length = -length;
        }
        this.length = length;

        switch (direction) {
        case NORD_EAST:
            this.direction = Direction.SOUTH;
            center.move(Direction.NORD_EAST, length);
            break;
        case NORD_WEST:
            this.direction = Direction.SOUTH;
            center.move(Direction.NORD, length);
            break;
        case SOUTH_EAST:
            this.direction = Direction.NORD;
            center.move(Direction.SOUTH, length);
            break;
        case SOUTH_WEST:
            this.direction = Direction.NORD;
            center.move(Direction.NORD_EAST, length);
            break;
        default:
            this.direction = direction;
            break;
        }
    }

    public String toString() {
        return "[" + this.getClass() + ", direction: " + direction + ", center:" + center + "]";
    }

    @Override
    public boolean contains(Coordinate coordinate) {
        Coordinate c = new Axial(coordinate.getX() - center.getX(), coordinate.getY() - center.getY());

        switch (direction) {
        case NORD:
            return c.getX() <= length && c.getY() <= length;
        case SOUTH:
            return c.getX() >= -length && c.getY() >= -length;
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new Iterator<Coordinate>() {
            Coordinate current = null;

            boolean hasNext = true;
            int iterLength = 0;
            int angle = 0;
            Direction dir_angle = direction.next(2);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Coordinate next() {
                assert hasNext();

                if (current == null) {
                    current = (Coordinate) center.add(Direction.NORD, 0);
                } else if (angle == iterLength) {

                    current.move(dir_angle.flip(), iterLength);
                    current.move(direction, 1);

                    iterLength++;
                    angle = 0;
                } else {
                    angle++;
                    current.move(dir_angle, 1);
                }

                if (iterLength == length && angle == iterLength) {
                    hasNext = false;
                }

                return current;
            }
        };
    }

    @Override
    public int size() {
        return ((length + 2) * (length + 1)) / 2;
    }

    @Override
    public boolean equals(Object object) {
        try {
            return ((Triangle) object).direction == direction && ((Triangle) object).length == length
                    && ((Triangle) object).center == center;
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public int getLength() {
        return length;
    }

    @Override
    public int getIndex(Coordinate coordinate) throws OutOfRegion {
        Coordinate c = new Axial(coordinate.getX() - center.getX(), coordinate.getY() - center.getY());

        int c_length;
        switch (direction) {
        case NORD:
            if (c.getZ() < length || c.getZ() > 0) {
                throw new OutOfRegion(coordinate, this);
            }
            c_length = -c.getZ();
            break;
        case SOUTH:
            if (c.getZ() < 0 || c.getZ() > length) {
                throw new OutOfRegion(coordinate, this);
            }
            c_length = c.getZ();
            break;
        default:
            throw new RuntimeException("Unexpected direction");
        }
        return ((c_length + 1) * c_length) / 2 + (c.getX());
    }
    
    @Override
    public Coordinate getRandom(Random rand) {
        int y = rand.nextInt(length);
        int z = -rand.nextInt(length);

        switch (direction) {
        case NORD:
            return new Axial(center.getX()+-y-z,center.getY()+y);
        case SOUTH:
            return new Axial(center.getX()+y+z,center.getY()-y);
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }


    @Override
    public Coordinate getCoordinate(int index) throws OutOfRegion {
        if (index < 0 || index > size()) {
            throw new OutOfRegion(index, this);
        }
        
        int distance;
        if (index < size() / 2) {
            distance = length;
            int sum = distance;
            while (index >= sum && distance > 0) {
                distance--;
                sum += distance;
            }
            index -= (sum - distance);
        } else {
            distance = 0;
            int sum = size();
            while (index < sum && distance < size() / 2) {
                distance++;
                sum -= distance;
            }
            index -= sum;
        }

        Coordinate result = center.add(direction, distance);
        assert index <= distance;
        
        result.move(direction.next(2), index);
        return result;
    }

    @Override
    public Iterable<Coordinate> vertices() {
        return () -> new Iterator<Coordinate>() {

            private int iter = 0;
            private Coordinate current = null;

            @Override
            public boolean hasNext() {
                return iter < 3;
            }

            @Override
            public Coordinate next() {
                if (current == null) {
                    current = center.copy();
                } else if (iter == 1) {
                    current = current.move(direction, length);
                } else {
                    current = current.move(direction.next(2), length);
                }

                iter++;
                return current;
            }
        };
    }

    @Override
    public Iterable<Segment> edges() {
        return () -> new Iterator<Segment>() {

            private int iter = 0;
            private Segment current = null;

            @Override
            public boolean hasNext() {
                return iter < 3;
            }

            @Override
            public Segment next() {
                if (current == null) {
                    current = new Segment(center, length, direction);
                } else if (iter == 1) {
                    current = new Segment(center.add(direction, length), length, direction.next(2));
                } else {
                    current = new Segment(center.add(direction.next(), length), length, direction.next(4));
                }

                iter++;
                return current;
            }
        };
    }
}
