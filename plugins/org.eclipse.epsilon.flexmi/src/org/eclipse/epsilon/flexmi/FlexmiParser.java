package org.eclipse.epsilon.flexmi;

import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.flexmi.xml.FlexmiXmlParser.Handler;
import org.w3c.dom.Document;

public interface FlexmiParser {
	
	/**
	 * Starts the parsing of a Flexmi resource
	 */
	public void parse(FlexmiResource resource, InputStream inputStream,
			Handler handler) throws FlexmiParseException;

	/**
	 * Allows parsing included fragments that have a different URI
	 * and do not require startDocument events
	 */
	public void parse(FlexmiResource resource, URI uri, InputStream inputStream,
			Handler handler, boolean processDocument) throws Exception;

	/**
	 * Performs the actual parsing of the input contents into a DOM
	 */
	public Document parse(InputStream inputStream) throws Exception;

	public FlexmiFlavour getFlavour();
	
}
