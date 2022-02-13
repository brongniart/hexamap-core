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
import java.util.Spliterator;
import java.util.stream.BaseStream;

import hexamap.maps.streams.MapStream;
import hexamap.regions.Region;

/**
 * Only callable from Hexamap
 */
public abstract class RegionStream implements BaseStream<Region,RegionStream> {
    
    public abstract RegionStream including(Region region);
    public abstract RegionStream including(RegionStream regions);

    public abstract RegionStream intersecting();
    public abstract RegionStream intersecting(Region region);
    public abstract RegionStream intersectingCluster(Region luster);
    
    public abstract RegionStream containing(Class<?> dataClass);
    
    public abstract <Data> MapStream<Data> map(Class<Data> dataClass);
    
    @Override
    public Iterator<Region> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<Region> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isParallel() {
        return false;
    }

    @Override
    public RegionStream sequential() {
        return this;
    }

    @Override
    public RegionStream parallel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegionStream unordered() {
        return this;
    }

    @Override
    public RegionStream onClose(Runnable closeHandler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
    }
}
