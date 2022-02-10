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
package hexamap.regions.streams;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.BaseStream;

import hexamap.coordinates.Coordinate;
import hexamap.maps.streams.MapStream;
import hexamap.regions.Region;

/**
 * Only callable from Hexamap
 */
public abstract class RegionStream<CoordinateImpl extends Coordinate> implements BaseStream<Region<CoordinateImpl>,RegionStream<CoordinateImpl>> {
    
    public abstract RegionStream<CoordinateImpl> including(Region<CoordinateImpl> region);
    public abstract RegionStream<CoordinateImpl> including(RegionStream<CoordinateImpl> regions);

    public abstract RegionStream<CoordinateImpl> intersecting();
    public abstract RegionStream<CoordinateImpl> intersecting(Region<CoordinateImpl> region);
    public abstract RegionStream<CoordinateImpl> intersectingCluster(Region<CoordinateImpl> cluster);
    
    public abstract RegionStream<CoordinateImpl> containing(Class<?> dataClass);
    
    public abstract <Data> MapStream<CoordinateImpl,Data> map(Class<Data> dataClass);
    
    public abstract Optional<CoordinateImpl> findAny();
    public abstract Optional<CoordinateImpl> findFirst();
    
    @Override
    public Iterator<Region<CoordinateImpl>> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<Region<CoordinateImpl>> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isParallel() {
        return false;
    }

    @Override
    public RegionStream<CoordinateImpl> sequential() {
        return this;
    }

    @Override
    public RegionStream<CoordinateImpl> parallel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegionStream<CoordinateImpl> unordered() {
        return this;
    }

    @Override
    public RegionStream<CoordinateImpl> onClose(Runnable closeHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
    }
}
