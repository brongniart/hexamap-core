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
import java.util.Random;

import hexamap.coordinates.Coordinate;
import hexamap.coordinates.Direction;

public class Rhombus<CoordinateImpl extends Coordinate> extends Region<CoordinateImpl> {

	private final int lowerBound;
	private final int upperBound;
	private final CoordinateImpl zero;
	@SuppressWarnings("unused")
	private Class<? extends CoordinateImpl> coordinateClazz;
	private Direction direction;

	public Rhombus(int _length, Class<? extends CoordinateImpl> clazz) {
		this(_length, Direction.NORD, clazz);
	}

	public Rhombus(int _length, Direction _direction, Class<? extends CoordinateImpl> clazz) {
		this((_length % 2 == 1) ? -_length / 2 : -_length / 2 + 1, _length / 2, _direction, clazz);
	}

	public Rhombus(int _lowerBound, int _upperBound, Class<? extends CoordinateImpl> clazz) {
		this(_lowerBound, _upperBound, Direction.NORD_WEST, clazz);
	}

	public Rhombus(int _lowerBound, int _upperBound, Direction _direction, Class<? extends CoordinateImpl> clazz) {
		super();

		lowerBound = _lowerBound;
		upperBound = _upperBound;
		direction = _direction;
		coordinateClazz = clazz;
		try {
			zero = clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void switchDirection() {
		direction = direction == Direction.NORD ? Direction.SOUTH_EAST : Direction.NORD_WEST;
	}

	@Override
	public boolean contains(Object obj) {
		if (obj == null) {
			return false;
		}
		try {
			@SuppressWarnings("unchecked")
			CoordinateImpl coordinate = rotate((CoordinateImpl) obj);
			
			return getX(coordinate) <= upperBound && getX(coordinate) >= lowerBound
					&& getY(coordinate) <= upperBound && getY(coordinate) >= lowerBound;
		} catch (ClassCastException e) {
			return false;
		}
	}

	private int getX(CoordinateImpl coordinate) {
		switch (direction) {
		case NORD:
			return coordinate.getY();
		case SOUTH:
			return -coordinate.getY();
		case NORD_EAST:
		case SOUTH_EAST:
			return coordinate.getX();
		case NORD_WEST:
		case SOUTH_WEST:
			return -coordinate.getX();
		default:
			throw new RuntimeException("Unknow direction " + direction);
		}
	}

	private int getY(CoordinateImpl coordinate) {
		switch (direction) {
		case NORD:
			return -coordinate.getX();
		case SOUTH:
			return coordinate.getX();
		case NORD_EAST:
		case SOUTH_EAST:
			return -coordinate.getY();
		case NORD_WEST:
		case SOUTH_WEST:
			return coordinate.getY();
		default:
			throw new RuntimeException("Unknow direction " + direction);
		}
	}

    @SuppressWarnings("unchecked")
	private CoordinateImpl rotate(CoordinateImpl c) {
		// return (CoordinateImpl) zero.createCoordinate(c.getY(),c.getZ());
		return (CoordinateImpl) c.rotate(direction);
	}

	@SuppressWarnings("unchecked")
	private CoordinateImpl createCoordinate(int x,int y) {
		switch (direction) {
		case NORD:
			return (CoordinateImpl) zero.createCoordinateYZ(y, -x);
		case NORD_EAST:
			return (CoordinateImpl) zero.createCoordinateXZ(x, -y);
		case SOUTH_EAST:
			return (CoordinateImpl) zero.createCoordinate(x, -y);
		case SOUTH:
			return (CoordinateImpl) zero.createCoordinateYZ(-y, x);
		case SOUTH_WEST:
			return (CoordinateImpl) zero.createCoordinateXZ(-x, y);
		case NORD_WEST:
			return (CoordinateImpl) zero.createCoordinateYZ(-x, y);
		default:
			throw new RuntimeException("Unknow direction " + direction);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<CoordinateImpl> iterator() {

		return new Iterator<CoordinateImpl>() {
			CoordinateImpl current;
			boolean finished;

			{
				current = createCoordinate(lowerBound, lowerBound);
				finished = false;
			}

			@Override
			public boolean hasNext() {
				return !finished;
			}

			@Override
			public CoordinateImpl next() {
				assert (!finished);

				CoordinateImpl next = current;

				if (getX(current) == upperBound && getY(current) == upperBound) {
					finished = true;
				} else {
					if (getX(current) == upperBound) {
						current = (CoordinateImpl) current.createCoordinate(lowerBound, getY(current) + 1);
					} else {
						current = (CoordinateImpl) current.createCoordinate(getX(current) + 1, getY(current));
					}
				}
				return next;
			}
		};
	}

	private double getLength() {
		return upperBound - lowerBound;
	}

	@Override
	public int size() {
		return (int) Math.pow(getLength(), 2);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Region region) {
		assert region != null;
		return region.getClass() == Rhombus.class && ((Rhombus) region).upperBound == this.upperBound
				&& ((Rhombus) region).lowerBound == this.lowerBound && ((Rhombus) region).direction == this.direction;
	}

	@Override
	public CoordinateImpl getRandom() {
		Random random = new Random();
		int x = random.nextInt(upperBound - lowerBound) + lowerBound;
		int y = random.nextInt(upperBound - lowerBound) + lowerBound;
		return createCoordinate(x,y);
	}
}
