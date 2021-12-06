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

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 *
 */
public enum Direction {
    NORD(0, 1), NORD_EAST(1, 0), SOUTH_EAST(1, -1), SOUTH(0, -1), SOUTH_WEST(-1, 0), NORD_WEST(-1, 1);

    public final int x;
    public final int y;

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

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Direction next() {
        return next(1);
    }

    public Direction next(int i) {
        return getIndex(this.ordinal() + i);
    }

    public Direction previous() {
        return previous(1);
    }

    public Direction previous(int i) {
        return getIndex(this.ordinal() - i);
    }

    public static Direction getIndex(int index) {
        return values()[(index % values().length + values().length) % values().length];
    }

    public static Direction getRandom(Random rand) {
        return values()[rand.nextInt(values().length)];
    }
}
