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
package net.imglib2.script.algorithm.integral.histogram.features;

import net.imglib2.script.algorithm.integral.histogram.Histogram;
import net.imglib2.type.numeric.RealType;

/**
 *
 * All computations are done using the type {@link <C>} except for the square root, which is done with double precision.
 * 
 * @author Albert Cardona
 *
 * @param <T> The type of the image from which the {@link Histogram} is computed.
 * @param <C> The type with which the computations necessary to obtain the standard deviation are performed.
 * @see IHUnaryDependentFeature
 */
public class IHStdDev<T extends RealType<T>, C extends RealType<C>> implements IHUnaryDependentFeature<T> {

	private final C sum, e, ecopy;

	public IHStdDev(final C op) {
		this.sum = op.createVariable();
		this.e = op.createVariable();
		this.ecopy = op.createVariable();
	}
	
	@Override
	public void compute(final Histogram<T> histogram, final T median, final T output) {
		sum.setZero();
		for (int i = histogram.nBins() -1; i> -1; --i) {
			e.setReal(histogram.binValue(i).getRealDouble() - median.getRealDouble());
			ecopy.set(e);
			e.mul(ecopy); // pow(x, 2)
			e.mul(histogram.bins[i]);
			sum.add(e);
		}
		output.setReal(Math.sqrt(sum.getRealDouble() / histogram.nPixels));
	}
}