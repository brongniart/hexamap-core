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
package hexamap.regions;

import hexamap.coordinates.Coordinate;
import static java.lang.Math.abs;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @param <CoordinateImpl>
 */
public class Hexagon<CoordinateImpl extends Coordinate> extends Region<CoordinateImpl> {

    private final int range;
    private final int size;
    private final CoordinateImpl zero;
    private final Class<? extends CoordinateImpl> coordinateClazz;
    
    public Hexagon(int range, Class<? extends CoordinateImpl> clazz) {
        super();
		System.out.println("Range:"+range);
        this.range = range;

        int tmpSize = 1;
        for (int i = range; i > 0; i--) {
            tmpSize += i * 6;
        }
        size = tmpSize;
        coordinateClazz = clazz;
        try {
			zero = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            Coordinate coordinate = (Coordinate) obj;
            return coordinate.distance(zero) <= range;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public Iterator<CoordinateImpl> iterator() {
        return new Iterator<CoordinateImpl>() {
            Iterator<Coordinate> internal;
            boolean last = false;

            {
            	if (range > 0) {
                    this.internal = zero.getAllNeigbours(range).iterator();
            	}
            }

            @Override
            public boolean hasNext() {
                return !last;
            }

            @Override
            public CoordinateImpl next() {
                if (internal!=null && internal.hasNext()) {
                    try {
                        return coordinateClazz.getDeclaredConstructor(Coordinate.class).newInstance(internal.next());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    if (!last) {
                        last = true;
                        return zero;
                    } else {
                        assert false;
                        return null;
                    }
                }
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

	@Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Region<CoordinateImpl> region) {
        assert region != null;
        return region.getClass() == Hexagon.class
                && ((Hexagon) region).range == this.range;
    }

    public int getRange() {
        return range;
    }

	@Override
    @SuppressWarnings("unchecked")
    public CoordinateImpl getRandom() {
        Random random = new Random();
        int x = random.nextInt(range * 2 + 1) - range;
        
        int bound = range - abs(x) - 1;
        int y = (x > 0)
                ? random.nextInt(range + bound) - range
                : random.nextInt(range + bound) - bound;
        return (CoordinateImpl) zero.createCoordinate(x, y);
    }
}
