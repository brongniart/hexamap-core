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

import static java.lang.Math.abs;

import java.util.Iterator;
import java.util.Random;

import hexamap.coordinates.Coordinate;

public class Rhombus<CoordinateImpl extends Coordinate> extends Region<CoordinateImpl> {

    private final int length;
    private final CoordinateImpl zero;
	@SuppressWarnings("unused")
	private Class<CoordinateImpl> coordinateClazz;

    public Rhombus(int _length, Class<CoordinateImpl> clazz) throws Exception {
        super();
        
        length = _length;
        coordinateClazz = clazz;
        zero = clazz.getDeclaredConstructor().newInstance();
    }

    @Override
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
			CoordinateImpl coordinate = (CoordinateImpl) obj;
            return coordinate.getX() <= length &&
            		coordinate.getX() >= lowerBound() &&
            		coordinate.getY() <= length &&
            		coordinate.getY() >= lowerBound();
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public Iterator<CoordinateImpl> iterator() {
    	
        return new Iterator<CoordinateImpl>() {
        	CoordinateImpl current;
            boolean finished;

            {
                current = (CoordinateImpl) zero.createCoordinate(lowerBound(),lowerBound());
                finished=false;
            }

            @Override
            public boolean hasNext() {
                return !finished;
            }

            @Override
            public CoordinateImpl next() {
            	assert(!finished);
            	CoordinateImpl next = current;
            	
            	if (current.getX()==length/2 && current.getY()==length/2) {
            		finished=true;
            	} else {
            		if (current.getX()==length/2) {
            			current = (CoordinateImpl) current.createCoordinate(lowerBound(),current.getY()+1);
            		} else {
            			current = (CoordinateImpl) current.createCoordinate(current.getX()+1,current.getY());
            		}
            	}
            	return next;
            }
        };
    }
    
    private int lowerBound() {
    	if (length%2==1) {
    		return -length/2;
    		
    	} else {
    		return -length/2+1;
    	}
    }
    

    @Override
    public int size() {
    	return (int) Math.pow(length,2);
    }

    @Override
    public boolean equals(Region region) {
        assert region != null;
        return region.getClass() == Rhombus.class
                && ((Rhombus) region).length == this.length;
    }

    @Override
    public CoordinateImpl getRandom() {
        Random random = new Random();
        int x = random.nextInt(lowerBound()+length) - lowerBound();
        int y = random.nextInt(lowerBound()+length) - lowerBound();
        return (CoordinateImpl) zero.createCoordinate(x, y);
    }
}
