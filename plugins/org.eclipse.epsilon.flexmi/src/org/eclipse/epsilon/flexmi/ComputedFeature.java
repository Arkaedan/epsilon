package org.eclipse.epsilon.flexmi;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.epsilon.emc.emf.InMemoryEmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.execute.context.Variable;

public class ComputedFeature extends Computation {
	
	protected EStructuralFeature eStructuralFeature;
	protected String attribute;
	
	public ComputedFeature(EObject eObject, EStructuralFeature eStructuralFeature, String attribute, String expression, URI uri, int lineNumber) {
		super();
		this.eObject = eObject;
		this.eStructuralFeature = eStructuralFeature;
		this.expression = expression;
		this.attribute = attribute;
		this.lineNumber = lineNumber;
		this.uri = uri;
	}
	
	public void compute() throws Exception {
		InMemoryEmfModel model = new InMemoryEmfModel(eObject.eResource());
		EolModule module = new EolModule();
		module.parse("return " + expression + ";");
		if (module.getParseProblems().size() > 0) throw new Exception(module.getParseProblems().get(0).getReason());
		module.getContext().getFrameStack().putGlobal(Variable.createReadOnlyVariable("self", eObject));
		module.getContext().getModelRepository().addModel(model);
		eObject.eSet(eStructuralFeature, module.execute());
	}
	
	public EStructuralFeature geteStructuralFeature() {
		return eStructuralFeature;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
}
