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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import hexamap.coordinates.Coordinate;

/**
 *
 * @param <CoordinateImpl>
 */
public class Set<CoordinateImpl extends Coordinate> extends Region<CoordinateImpl> {

    private LinkedHashSet<CoordinateImpl> set = new LinkedHashSet<CoordinateImpl>();

    public Set(CoordinateImpl center) {
        super(center);
        set.add(center);
    }

    @Override
    public void setCenter(CoordinateImpl center) {
        super.setCenter(center);
        set.add(center);
    }

    @Override
    public boolean contains(Object obj) {
        return set.contains(obj);
    }

    @Override
    public Iterator<CoordinateImpl> iterator() {
        return set.iterator();
    }

    @Override
    public Stream<CoordinateImpl> tryParallel() {
        return StreamSupport.stream(set.spliterator(),true);
    }
    
    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean add(CoordinateImpl coordinate) {
        return set.add(coordinate);
    }

    @Override
    public boolean remove(Object coordinate) {
        if (center.equals(coordinate)) {
            throw new UnsupportedOperationException("Center cannot be removed");
        }
        return set.remove(coordinate);
    }

    @Override
    public void clear() {
        set.clear();
        set.add(center);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object object) {
        try {
            return set.equals(((Set<CoordinateImpl>) object).set);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public CoordinateImpl getRandom(Random random) {
        return (CoordinateImpl) set.toArray()[random.nextInt(set.size())];
    }
}
