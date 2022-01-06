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

import java.util.Iterator;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;

import hexamap.regions.Region;

/**
 *
 */
public class Segment<CoordinateImpl extends Coordinate> implements Region<CoordinateImpl> {
    private int min,max;

    @Override
    public Iterator<CoordinateImpl> iterator() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return max-min;
    }

    @Override
    public boolean contains(CoordinateImpl coordinate) {
        return false;
    }

    @Override
    public CoordinateImpl getRandom(Random random) {
        return null;
    }
    static public BiPredicate<Coordinate, Integer> getContainTest(Direction direction, Coordinate coordinate) {
        switch (direction) {
        case NORD:
            return new BiPredicate<Coordinate, Integer>() {
                Coordinate center = coordinate;

                @Override
                public boolean test(Coordinate coordinate, Integer distance) {
                    return 0 <= coordinate.getX() - center.getX() && coordinate.getX() - center.getX() <= distance;
                }
            };
        case NORD_EAST:
            return new BiPredicate<Coordinate, Integer>() {
                Coordinate center = coordinate;

                @Override
                public boolean test(Coordinate coordinate, Integer distance) {
                    return -distance <= coordinate.getY() - center.getY() && coordinate.getY() - center.getY() <= 0;
                }
            };
        case SOUTH_EAST:
            return new BiPredicate<Coordinate, Integer>() {
                Coordinate center = coordinate;

                @Override
                public boolean test(Coordinate coordinate, Integer distance) {
                    return 0 <= coordinate.getZ() - center.getZ() && coordinate.getZ() - center.getZ() <= distance;
                }
            };
        case SOUTH:
            return new BiPredicate<Coordinate, Integer>() {
                Coordinate center = coordinate;

                @Override
                public boolean test(Coordinate coordinate, Integer distance) {
                    return -distance <= coordinate.getX() - center.getX() && coordinate.getX() - center.getX() <= 0;
                }
            };
        case SOUTH_WEST:
            return new BiPredicate<Coordinate, Integer>() {
                Coordinate center = coordinate;

                @Override
                public boolean test(Coordinate coordinate, Integer distance) {
                    return 0 <= coordinate.getY() - center.getY() && coordinate.getY() - center.getY() <= distance;
                }
            };
        case NORD_WEST:
            return new BiPredicate<Coordinate, Integer>() {
                Coordinate center = coordinate;

                @Override
                public boolean test(Coordinate coordinate, Integer distance) {
                    return -distance <= coordinate.getZ() - center.getZ() && coordinate.getZ() - center.getZ() <= 0;
                }
            };
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }

    static public Function<Coordinate, Integer> getCoordinate(Direction direction) {
        switch (direction) {
        case NORD:
            return new Function<Coordinate, Integer>() {
                @Override
                public Integer apply(Coordinate coordinate) {
                    return coordinate.getX();
                }
            };
        case NORD_EAST:
            return new Function<Coordinate, Integer>() {
                @Override
                public Integer apply(Coordinate coordinate) {
                    return -coordinate.getY();
                }
            };
        case SOUTH_EAST:
            return new Function<Coordinate, Integer>() {
                @Override
                public Integer apply(Coordinate coordinate) {
                    return coordinate.getZ();
                }
            };
        case SOUTH:
            return new Function<Coordinate, Integer>() {
                @Override
                public Integer apply(Coordinate coordinate) {
                    return -coordinate.getX();
                }
            };
        case SOUTH_WEST:
            return new Function<Coordinate, Integer>() {
                @Override
                public Integer apply(Coordinate coordinate) {
                    return coordinate.getY();
                }
            };
        case NORD_WEST:
            return new Function<Coordinate, Integer>() {
                @Override
                public Integer apply(Coordinate coordinate) {
                    return -coordinate.getZ();
                }
            };
        default:
            throw new RuntimeException("Unexpected direction");
        }
    }
}
