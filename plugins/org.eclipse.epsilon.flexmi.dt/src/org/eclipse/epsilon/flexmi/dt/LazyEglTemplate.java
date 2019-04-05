package org.eclipse.epsilon.flexmi.dt;

import java.io.File;
import java.net.URI;

import org.eclipse.epsilon.egl.EglPersistentTemplate;
import org.eclipse.epsilon.egl.dom.GenerationRule;
import org.eclipse.epsilon.egl.exceptions.EglRuntimeException;
import org.eclipse.epsilon.egl.execute.context.IEglContext;
import org.eclipse.epsilon.egl.spec.EglTemplateSpecification;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

public class LazyEglTemplate extends EglPersistentTemplate {
	
	protected ContentTree contentTree;
	protected IEglContext context;
	protected GenerationRule generationRule;
	
	public LazyEglTemplate(EglTemplateSpecification spec, IEglContext context, URI outputRoot, String outputRootPath)
			throws Exception {
		super(spec, context, outputRoot, outputRootPath);
		this.context = context;
	}
	
	@Override
	protected void doGenerate(File file, String targetName, boolean overwrite, boolean merge)
			throws EglRuntimeException {
		while (targetName.startsWith("/")) targetName = targetName.substring(1);
		
		String format = "html";
		if (generationRule != null) {
			Annotation annotation = generationRule.getAnnotation("render");
			if (annotation != null) {
				try {
					format = annotation.getValue(context) + "";
				} catch (EolRuntimeException ex) {
					throw new EglRuntimeException(ex);
				}
			}
		}
		
		contentTree.addPath(targetName, getContents(), format);
	}

	public void setContentTree(ContentTree contentTree) {
		this.contentTree = contentTree;
	}
	
	public void setGenerationRule(GenerationRule generationRule) {
		this.generationRule = generationRule;
	}
}