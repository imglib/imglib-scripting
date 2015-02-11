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

package net.imglib2.script.algorithm;

import net.imglib2.algorithm.integral.IntegralImgDouble;
import net.imglib2.converter.Converter;
import net.imglib2.img.Img;
import net.imglib2.script.algorithm.fn.ImgProxy;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/** Integral image that stores sums with double floating-point precision.
 * Will overflow if any sum is larger than Double.MAX_VALUE.
 * 
 * @see net.imglib2.algorithm.integral.IntegralImage
 *
 * @author Albert cardona
 */
public class IntegralImage extends ImgProxy<DoubleType>
{
	public <T extends RealType<T>> IntegralImage(final Img<T> img) {
		super(process(img));
	}

	private static final <T extends RealType<T>> Img<DoubleType> process(
			final Img<T> img) {
		final IntegralImgDouble<T> o =
			new IntegralImgDouble<T>(img, new DoubleType(),
				new Converter<T, DoubleType>() {
					@Override
					public final void convert(final T input, final DoubleType output) {
						output.set(input.getRealDouble());
					}
				});
		o.process();
		return o.getResult();
	}
}
