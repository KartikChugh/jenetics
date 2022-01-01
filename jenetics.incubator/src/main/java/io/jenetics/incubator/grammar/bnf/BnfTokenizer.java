/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.incubator.grammar.bnf;

import static java.lang.Character.isWhitespace;
import static java.lang.String.format;
import static io.jenetics.incubator.grammar.bnf.Bnf.isIdChar;
import static io.jenetics.incubator.grammar.bnf.Bnf.isStringChar;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.ASSIGN;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.BAR;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.GT;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.ID;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.LT;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.QUOTED_STRING;
import static io.jenetics.incubator.grammar.bnf.BnfTokenizer.BnfTokenType.STRING;

import io.jenetics.incubator.parser.CharSequenceTokenizer;
import io.jenetics.incubator.parser.ParsingException;
import io.jenetics.incubator.parser.Token;

/**
 * Tokenizer for BNF grammars.
 *
 * <pre>{@code
 * ASSIGN: '::=';
 * BAR: '|';
 * GT: '>';
 * LT: '<';
 * ID: ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'-')+;
 * STRING: ( '%s' | '%i' )? '"' ( ~ '"' )* '"';
 * WS: [ \r\n\t] -> skip;
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 7.0
 * @version 7.0
 */
final class BnfTokenizer extends CharSequenceTokenizer<Token> {

	enum BnfTokenType implements Token.Type {
		ASSIGN(1),
		BAR(2),
		GT(3),
		LT(4),
		ID(5),
		STRING(6),
		QUOTED_STRING(7);

		private final int _code;

		BnfTokenType(final int code) {
			_code = code;
		}

		@Override
		public int code() {
			return _code;
		}
	}

	BnfTokenizer(final CharSequence input) {
		super(input);
	}

	@Override
	public Token next() {
		while (isNonEof(c)) {
			final char value = c;
			switch (value) {
				case ' ', '\r', '\n', '\t':
					WS();
					continue;
				case ':':
					return ASSIGN();
				case '|':
					consume();
					return BAR.token(value);
				case '>':
					consume();
					return GT.token(value);
				case '<':
					consume();
					return LT.token(value);
				case '\'':
					return QUOTED_STRING();
				default:
					if (isAlphabetic(c)) {
						return ID();
					} else if (!isWhitespace(c)) {
						return STRING();
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
			}
		}

		return Token.EOF;
	}

	private Token ASSIGN() {
		match(':');
		match(':');
		match('=');
		return ASSIGN.token("::=");
	}

	private Token QUOTED_STRING() {
		final var value = new StringBuilder();

		match('\'');
		while (isNonEof(c) && c != '\'') {
			if (c == '\\') {
				consume();
			}

			value.append(c);
			consume();
		}
		match('\'');

		return QUOTED_STRING.token(value.toString());
	}

	private Token ID() {
		final var value = new StringBuilder();

		while (isIdChar(c)) {
			value.append(c);
			consume();
		}

		return ID.token(value.toString());
	}

	private Token STRING() {
		final var value = new StringBuilder();

		while (isNonEof(c) && isStringChar(c)) {
			value.append(c);
			consume();
		}

		return STRING.token(value.toString());
	}

}
