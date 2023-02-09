/*******************************************************************************
 * Copyright (c) 2011-2023 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 *     Antonio Garcia-Dominguez - add source text for resiliency
 ******************************************************************************/
package org.eclipse.epsilon.egl.engine.traceability.fine.trace;

import java.util.Objects;

public class Region {

	public int offset, length;
	public String text;
	
	public Region(int offset, int length, String text) {
		this.offset = offset;
		this.length = length;
		this.text = text;
	}

	
	// Getters for compatibility with JavaModel, which are used in acceptance tests

	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}

	public String getText() {
		return text;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Region other = (Region) obj;
		return length == other.length && offset == other.offset && Objects.equals(text, other.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(length, offset, text);
	}

	@Override
	public String toString() {
		return offset + "+" + length + "(" + text + ")";
	}
}
