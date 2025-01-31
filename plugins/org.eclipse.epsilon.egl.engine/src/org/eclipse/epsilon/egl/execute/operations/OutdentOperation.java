package org.eclipse.epsilon.egl.execute.operations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.egl.output.IOutputBuffer;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.control.IExecutionListener;
import org.eclipse.epsilon.eol.execute.operations.simple.SimpleOperation;

public class OutdentOperation extends SimpleOperation {
	
	protected Set<String> ids = new HashSet<>();
	// Execution listeners for indenting the output at the end 
	// of the block containing the call to the _outdent function
	protected List<IExecutionListener> executionListenersForRemoval = new ArrayList<>();
	
	@Override
	public Object execute(Object source, List<?> parameters, IEolContext context, ModuleElement ast)
			throws EolRuntimeException {
		
		// Remove any execution listeners marked for removal
		for (IExecutionListener executionListener : executionListenersForRemoval) {
			context.getExecutorFactory().removeExecutionListener(executionListener);
		}
		
		String id = String.valueOf(parameters.get(0));
		
		if (!ids.contains(id)) {
			IOutputBuffer buffer = (IOutputBuffer) context.getFrameStack().get("out").getValue();
			buffer.getOutdentationFormatter().outdent(buffer.getOffset());
			ids.add(id);
			final ModuleElement moduleElement = context.getFrameStack().getCurrentStatement().getParent();
			
			context.getExecutorFactory().addExecutionListener(new IExecutionListener() {
				
				@Override
				public void finishedExecutingWithException(ModuleElement ast, EolRuntimeException exception, IEolContext context) {}
				
				@Override
				public void finishedExecuting(ModuleElement ast, Object result, IEolContext context) {
					
					if (ast == moduleElement && ids.contains(id)) {
						buffer.getOutdentationFormatter().indent(buffer.getOffset());
						ids.remove(id);
						// Mark the execution listener for removal
						executionListenersForRemoval.add(this);
					}
				}
				
				@Override
				public void aboutToExecute(ModuleElement ast, IEolContext context) {}
			});
		}
		
		return null;
	}

}
