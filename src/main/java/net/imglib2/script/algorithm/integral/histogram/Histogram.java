/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2015 Tobias Pietzsch, Stephan Preibisch, Barry DeZonia,
 * Stephan Saalfeld, Curtis Rueden, Albert Cardona, Christian Dietz, Jean-Yves
 * Tinevez, Johannes Schindelin, Jonathan Hale, Lee Kamentsky, Larry Lindsey, Mark
 * Hiner, Michael Zinsmaier, Martin Horn, Grant Harris, Aivar Grislis, John
 * Bogovic, Steffen Jaensch, Stefan Helfrich, Jan Funke, Nick Perry, Mark Longair,
 * Melissa Linkert and Dimiter Prodanov.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */
package net.imglib2.script.algorithm.integral.histogram;

import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.list.ListImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * 
 * @author Albert Cardona
 *
 * @param <T> The {@link Type} of the values of the image from which the histogram is computed.
 */
public abstract class Histogram<T extends RealType<T>>
{
	public final T min, max, range;
	public final long[] bins;
	protected final Img<T> binValues;
	protected final RandomAccess<T> accessBinValues;
	public final long[] maxPositions, minPositions;
	public long nPixels;

	/**
	 * 
	 * @param nBins The desired number of bins.
	 * @param numDimensions The dimensions of the image region from which the histogram is computed.
	 * @param min The minimum value, from which the first bin starts; all values under min will be added to the first bin.
	 * @param max The maximum value, at which the last bin ends; all values over max will be added to the last bin.
	 * @param op The type in which operations will be computed.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Histogram(
			final int nBins,
			final int numDimensions,
			final T min,
			final T max)
	{
		this.bins = new long[nBins];
		this.maxPositions = new long[numDimensions];
		this.minPositions = new long[numDimensions];
		this.min = min;
		this.max = max;
		//
		this.range = min.createVariable();
		this.range.set(max);
		this.range.sub(min);
		//
		this.binValues = min instanceof NativeType?
				  new ArrayImgFactory().create(new long[]{nBins}, (NativeType)min.createVariable())
				: new ListImgFactory<T>().create(new long[]{nBins}, min.createVariable());
		this.accessBinValues = this.binValues.randomAccess();
	}
	
	public abstract long computeBin(final T value);
	
	public T binValue(final long index) {
		this.accessBinValues.setPosition(index, 0);
		return this.accessBinValues.get();
	}
	
	public abstract Histogram<T> clone();
	
	public final int nBins() { return bins.length; }

	public final void clearBins() {
		for (int i=0; i<bins.length; ++i) bins[i] = 0;
	}
	
	public final void updatePixelCount() {
		nPixels = maxPositions[0] - minPositions[0];
		for (int d=1; d<maxPositions.length; ++d) {
			nPixels *= maxPositions[d] - minPositions[d];
		}
	}
	
	public final void initPositions(final Localizable l, final int offset) {
		for (int d=0; d<maxPositions.length; ++d) {
			final long p = l.getLongPosition(d);
			maxPositions[d] = p + offset;
			minPositions[d] = p + offset;
		}
	}
	
	public final void updatePositions(final long position, final int d) {
		maxPositions[d] = Math.max(maxPositions[d], position);
		minPositions[d] = Math.min(minPositions[d], position);
	}
}