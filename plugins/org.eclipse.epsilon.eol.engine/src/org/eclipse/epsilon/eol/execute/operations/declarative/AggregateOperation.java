/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.eol.execute.operations.declarative;

import java.util.Collection;
import java.util.List;
import org.eclipse.epsilon.common.util.CollectionUtil;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.FrameStack;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.types.EolMap;
import org.eclipse.epsilon.eol.types.EolType;

public class AggregateOperation extends FirstOrderOperation {
	
	@Override
	public EolMap<?, ?> execute(Object target,
			NameExpression operationNameExpression, List<Parameter> iterators,
			List<Expression> expressions, IEolContext context)
			throws EolRuntimeException {

		Parameter iterator = iterators.get(0);
		EolType iteratorType = iterator.getType(context);
		Expression keyExpression = expressions.get(0);
		Expression valueExpression = expressions.get(1);
		Expression initialExpression = expressions.size() > 2 ? expressions.get(2) : null;
		
		Collection<?> source = CollectionUtil.asCollection(target);
		FrameStack scope = context.getFrameStack();
		EolMap<Object, Object> result = new EolMap<>();
		
		for (Object item : source) {
			if (iteratorType == null || iteratorType.isKind(item)) {
				scope.enterLocal(FrameType.UNPROTECTED, keyExpression,
					Variable.createReadOnlyVariable(iterator.getName(), item)
				);
				
				Object total, keyResult = context.getExecutorFactory().execute(keyExpression, context);
				
				if (result.containsKey(keyResult)) {
					total = result.get(keyResult);
				}
				else {
					total = context.getExecutorFactory().execute(initialExpression, context);
				}
				
				scope.put(Variable.createReadOnlyVariable("total", total));
				Object valueResult = context.getExecutorFactory().execute(valueExpression, context);
				result.put(keyResult, valueResult);
				scope.leaveLocal(keyExpression);
			}
		}
		
		return result;
	}

	@Override
	public final Object execute(Object target, Variable iterator, Expression expression,
			IEolContext context) throws EolRuntimeException {
		return null;
	}
}
