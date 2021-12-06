/*
 * Copyright (c) 2021, J�r�me Brongniart
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
package hexamap.coordinates.streams;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

import hexamap.coordinates.Coordinate;
import hexamap.regions.AbstractRegion;

/**
 *
 */
public abstract class CoordinateStream<CoordinateImpl extends Coordinate> implements BaseStream<CoordinateImpl,CoordinateStream<CoordinateImpl>> {
    
    public abstract CoordinateStream<CoordinateImpl> containedIn(AbstractRegion<Coordinate> region);
    public abstract CoordinateStream<CoordinateImpl> containedIn(CoordinateStream<Coordinate> streams[]);
    public abstract CoordinateStream<CoordinateImpl> within(CoordinateImpl min, CoordinateImpl max);

    public abstract void unique();
    
    @Override
    public Iterator<CoordinateImpl> iterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Spliterator<CoordinateImpl> spliterator() {
       throw new UnsupportedOperationException();
    }

    @Override
    public boolean isParallel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CoordinateStream<CoordinateImpl> sequential() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CoordinateStream<CoordinateImpl> parallel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CoordinateStream<CoordinateImpl> unordered() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CoordinateStream<CoordinateImpl> onClose(Runnable closeHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
