/*
 * generated by Xtext 2.14.0
 */
package org.eclipse.epsilon.examples.evl.xtext.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.epsilon.examples.evl.xtext.parser.antlr.internal.InternalEvlDSLParser;
import org.eclipse.epsilon.examples.evl.xtext.services.EvlDSLGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class EvlDSLParser extends AbstractAntlrParser {

	@Inject
	private EvlDSLGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalEvlDSLParser createParser(XtextTokenStream stream) {
		return new InternalEvlDSLParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}

	public EvlDSLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(EvlDSLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}