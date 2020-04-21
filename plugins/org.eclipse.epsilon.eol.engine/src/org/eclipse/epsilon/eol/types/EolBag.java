/*******************************************************************************
 * Copyright (c) 2012-2020 The University of York, Antonio García-Domínguez.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Antonio García-Domínguez - add type parameter
 *     Sina Madani - extends {@link AbstractEolCollection}
 ******************************************************************************/
package org.eclipse.epsilon.eol.types;

import org.eclipse.epsilon.common.concurrent.ConcurrencyUtils;

public class EolBag<T> extends AbstractEolCollection<T> {

	public EolBag() {
		this(false);
	}
	
	/**
	 * 
	 * @param threadSafe
	 * @since 1.6
	 */
	public EolBag(boolean threadSafe) {
		super(threadSafe ? ConcurrencyUtils.concurrentOrderedCollection() : new java.util.ArrayList<>());
	}
}
