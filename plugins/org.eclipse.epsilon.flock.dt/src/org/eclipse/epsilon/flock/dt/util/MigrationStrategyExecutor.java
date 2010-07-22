/*******************************************************************************
 * Copyright (c) 2009 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Louis Rose - initial API and implementation
 ******************************************************************************
 *
 * $Id$
 */
package org.eclipse.epsilon.flock.dt.util;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.epsilon.common.dt.console.EpsilonConsole;
import org.eclipse.epsilon.common.dt.util.LogUtil;
import org.eclipse.epsilon.commons.parse.problem.ParseProblem;
import org.eclipse.epsilon.emc.emf.AbstractEmfModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfModelFactory;
import org.eclipse.epsilon.emc.emf.EmfModelFactory.AccessMode;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.flock.FlockModule;
import org.eclipse.epsilon.flock.FlockResult;

public class MigrationStrategyExecutor {

	private final IResource model;
	private final IPath backup;
	
	private final URI strategy;
	private final Object originalMetamodel, evolvedMetamodel;
	
	public MigrationStrategyExecutor(IResource model, URI strategy, Object originalMetamodel, Object evolvedMetamodel) {
		this.model  = model;
		this.backup = createBackupPath();
		
		this.strategy = strategy;
		this.originalMetamodel = originalMetamodel;
		this.evolvedMetamodel  = evolvedMetamodel;
	}
	
	private IPath createBackupPath() {
		final IPath originalPath = model.getFullPath();
		
		return originalPath
		       	.removeLastSegments(1)
		       	.append(originalPath.removeFileExtension().lastSegment() + "_backup." + originalPath.getFileExtension());
	}
	
	
	public void run() {
		try {
			createBackup();
			migrate();
			refreshProject();
		
		} catch (Exception ex) {
			LogUtil.log("Error encountered whilst executing Flock migration strategy for: " + model.getLocation(), ex);
		}
	}	
	
	
	private void createBackup() throws CoreException {		
		model.move(backup, false, new NullProgressMonitor());
		
		// The move method changes resources, so now the model variable
		// points to a resource that no longer exists.
	}

	private void migrate() throws Exception {
		final AbstractEmfModel original = loadEmfModel("Original", getAbsoluteBackupPath(), originalMetamodel, AccessMode.READ_ONLY);
		final AbstractEmfModel migrated = loadEmfModel("Migrated", model.getLocation(),     evolvedMetamodel,  AccessMode.WRITE_ONLY);
		
		final FlockModule migrator = new FlockModule();
	
		if (migrator.parse(strategy) && migrator.getParseProblems().isEmpty()) {
			final FlockResult result = migrator.execute(original, migrated);
			
			result.printWarnings(EpsilonConsole.getInstance().getWarningStream());
			
			migrated.store();
		
		} else {
			displayParseProblems(migrator.getParseProblems());	
		}
		
		original.dispose();
		migrated.dispose();
	}

	private IPath getAbsoluteBackupPath() {
		return model.getParent().getLocation().append(backup.lastSegment());
	}
	
	private EmfModel loadEmfModel(String name, IPath modelPath, Object metamodel, AccessMode accessMode) throws EolModelLoadingException {
		final EmfModel model = EmfModelFactory.getInstance().loadEmfModel(name, new File(modelPath.toOSString()), metamodel, accessMode);
		model.setExpand(false);
		return model;
	}
	
	private void refreshProject() throws CoreException {
		model.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}
	
	private static void displayParseProblems(Collection<ParseProblem> problems) {
		final StringBuilder message = new StringBuilder("Error(s) encountered while parsing migration strategy:");
		
		for (ParseProblem problem : problems) {
			message.append('\n');
			message.append(problem);
		}
		
		LogUtil.logInfo(message, true);
	}
}
