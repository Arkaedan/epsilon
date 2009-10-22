/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.egl.test.acceptance.stop;

import java.io.File;
import java.io.IOException;

import org.eclipse.epsilon.egl.test.acceptance.AcceptanceTestUtil;
import org.eclipse.epsilon.egl.exceptions.EglRuntimeException;
import org.eclipse.epsilon.egl.util.FileUtil;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.junit.BeforeClass;
import org.junit.Test;

public class Stop {

	private static File Stop;
	private static File StopNested;
	
	@BeforeClass
	public static void setUpOnce() {
		Stop       = org.eclipse.epsilon.commons.util.FileUtil.getFile("Stop.egl",       Stop.class);
		StopNested = org.eclipse.epsilon.commons.util.FileUtil.getFile("StopNested.egl", Stop.class);
	}
	
	@Test
	public void stop() throws EglRuntimeException, IOException, EolModelLoadingException {
		AcceptanceTestUtil.test(Stop, "Before");
	}
	
	@Test
	public void stopNested() throws EglRuntimeException, IOException, EolModelLoadingException {
		AcceptanceTestUtil.test(StopNested, "NestedBefore" + FileUtil.NEWLINE +
		                                    "Before"       + FileUtil.NEWLINE +
		                                    "NestedAfter");
	}
}
