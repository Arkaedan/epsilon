/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.eol.dom;

import java.util.Collection;

import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.exceptions.EolNullPointerException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.introspection.IPropertyGetter;
import org.eclipse.epsilon.eol.types.EolSequence;

public class PropertyCallExpression extends FeatureCallExpression {
	
	public PropertyCallExpression() {}
	
	public PropertyCallExpression(Expression targetExpression, NameExpression propertyNameExpression) {
		this.targetExpression = targetExpression;
		this.nameExpression = propertyNameExpression;
	}
	
	@Override
	public void build(AST cst, IModule module) {
		super.build(cst, module);
		targetExpression = (Expression) module.createAst(cst.getFirstChild(), this);
		nameExpression = (NameExpression) module.createAst(cst.getSecondChild(), this);
	}
	
	@Override
	public Object execute(IEolContext context) throws EolRuntimeException {
		return execute(context.getExecutorFactory().execute(targetExpression, context), nameExpression, context);
	}
	
	public Object execute(Object source, NameExpression propertyNameExpression, IEolContext context) throws EolRuntimeException {
		String propertyName = propertyNameExpression.getName();
		if (source == null) {
			if (isNullSafe()) {
				return null;
			}
			else {
				throw new EolNullPointerException(propertyName, propertyNameExpression);
			}
		}

		IPropertyGetter getter = context.getIntrospectionManager().getPropertyGetterFor(source, propertyName, context);
		// Added support for properties on collections
		if (source instanceof Collection<?> && !getter.hasProperty(source, propertyName, context)) {
			EolSequence<Object> results = new EolSequence<>();
			results.ensureCapacity(((Collection<?>) source).size());
			for (Object content : (Collection<?>) source) {
				results.add(
					context.getIntrospectionManager().getPropertyGetterFor(content, propertyName, context)
						.invoke(content, propertyName, context)
				);
			}
			return results;
		}

		try {
			return wrap(getter.invoke(source, propertyName, context));
		}
		catch (EolRuntimeException eox) {
			if (eox.getAst() == null) {
				eox.setAst(this);
			}
			throw eox;
		}
	}
	
	public void accept(IEolVisitor visitor) {
		visitor.visit(this);
	}
}
