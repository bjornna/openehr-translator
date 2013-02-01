package no.dips.openehr.translator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import org.apache.log4j.Logger;
import org.openehr.am.archetype.Archetype;

import se.acode.openehr.parser.ADLParser;
import se.acode.openehr.parser.ParseException;

public class OpenEHRADLParser {
	private Logger log = Logger.getLogger(OpenEHRADLParser.class);
	protected Archetype archetype;

	private boolean decodeFileBeforeParsing = true;

	public Archetype getArchetype(InputStream inputStream) {
		parse(inputStream);
		return archetype;
	}

	public OpenEHRADLParser() {

	}

	public OpenEHRADLParser(boolean decodeFileBeforeParsing) {
		this.decodeFileBeforeParsing = decodeFileBeforeParsing;
	}

	private void parse(InputStream inputStream) {
		log.debug("Parse inputStream: " + inputStream);
		if (decodeFileBeforeParsing) {
			decodeAndParseInputStream(inputStream);
		} else {
			parseInputStreamDirect(inputStream);
		}
	}

	private void decodeAndParseInputStream(InputStream inputStream) {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
				getCharsetDecoder());
		doParse(inputStreamReader);
	}

	private CharsetDecoder getCharsetDecoder() {

		String charSetName = "UTF-8";
		Charset charset = Charset.forName(charSetName);
		CharsetDecoder charsetDecoder = charset.newDecoder();
		/*
		 * By tuning this parameters we can parse all ADL files. I do not now
		 * what is happening...... bna
		 */
		charsetDecoder.onMalformedInput(CodingErrorAction.REPLACE);
		charsetDecoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		log.debug("getCharsetDecoder: " + charSetName);
		return charsetDecoder;
	}

	private void parseInputStreamDirect(InputStream inputStream) {
		ADLParser parser = new ADLParser(inputStream);
		parseWithGivenParser(parser);
	}

	private void doParse(Reader reader) {
		ADLParser parser = new ADLParser(reader);
		parseWithGivenParser(parser);

	}

	private void parseWithGivenParser(ADLParser parser) {
		try {
			archetype = parser.parse();
		} catch (ParseException e) {
			throw new ArcheTypeException("Kunne ikke parse ADL fil. " + e.getMessage(), e);
		} catch (Exception e) {
			throw new ArcheTypeException("Feil ved parsing ADL fil. " + e.getMessage(), e);
		}

	}

}
