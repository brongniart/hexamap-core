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

/**
 *
 */
public abstract class AbstractCoordinate implements Coordinate {
    
    @Override
    public boolean equals(Object obj) {
        if (obj==null) 
            return false;
        try {
        Coordinate coordinate = (Coordinate) obj;
        return (getX()==coordinate.getX()) &&
                (getY()==coordinate.getY());
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public Coordinate add(Coordinate coordinate) {
        return createCoordinate(getX()+coordinate.getX(),getY()+coordinate.getY());
    }

    @Override
    public Coordinate add(Direction direction, int range) {
        return createCoordinate(getX()+range*direction.x,getY()+range*direction.y);
    }
    
    public class NeigboursIterator implements Iterator<Coordinate> {

        private final boolean all;
        private final Coordinate center;
        private int range;
        private Coordinate current;
        private Coordinate first;
        private Direction direction;
        
        private final Direction INIT_DIRECTION = Direction.NORD;
        
        public NeigboursIterator(Coordinate center, int range, boolean all) {
            assert range > 0;
            
            this.all=all;
            this.range=range;
            this.center=center;

            current=center.add(INIT_DIRECTION,range);
            first=null;
            direction=INIT_DIRECTION.next();
        }
        
        @Override
        public boolean hasNext() {
            if (all) {
                return range>1 || !current.equals(first);
            } else {
                return !current.equals(first);
            }
        }

        @Override
        public Coordinate next() {
            //System.err.println("Current: "+current+" Center: "+center+" Range: "+range);
            Coordinate returnValue=current;
            
            //compute the next
            Coordinate next = current.add(direction,1);
            if (all && next.equals(first)) {
                range--;
                next=center.add(INIT_DIRECTION, range);
                direction=INIT_DIRECTION.next();
                first=null;
            } else {
                if (first==null) {
                    first = current;
                }
                if (center.distance(next)>range) {
                    direction=direction.next();
                    next=current.add(direction,1);
                }
            }
            current=next;
            
            assert returnValue!=null;
            return returnValue;
        }
    }
    
    @Override
    public Iterable<Coordinate> getNeigbours(int range) {
        assert range > 0;
        return () -> new NeigboursIterator(this,range,false);
    }
    
    @Override
    public Iterable<Coordinate> getNeigbours() {
        return getNeigbours(1);
    }
    
    @Override
    public Iterable<Coordinate> getAllNeigbours(int range) {
        assert range > 0;
        return () -> new NeigboursIterator(this,range,true);
    }
    
    @Override
    public int distance(Coordinate other) {
        return (abs(getX() - other.getX()) + 
                abs(getY() - other.getY()) + 
                abs(getZ() - other.getZ())) / 2;
    }
    
    @Override
    public String toString() {
        return "{"+getX()+":"+getY()+"}";
    }
}
