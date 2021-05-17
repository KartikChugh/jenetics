package io.jenetics.incubator.grammar;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import io.jenetics.incubator.grammar.Grammar.Symbol;
import io.jenetics.incubator.grammar.Grammar.Terminal;

import io.jenetics.ext.util.TreeFormatter;
import io.jenetics.ext.util.TreeNode;

public class SymbolListParserTest {

	@Test
	public void foo() {
		final var grammar = Grammar.parse("""
			<expr> ::= ( <expr> <op> <expr> ) | <num> | <var> |  <fun> ( <expr>, <var> )
			<fun> ::= FUN1 | FUN2
			<op> ::= + | - | * | /
			<var> ::= x | y
			<num> ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
			"""
		);

		final List<Terminal> list = grammar.generate(new Random(290234568903L)::nextInt);
		final var string = list.stream()
			.map(Symbol::toString)
			.collect(Collectors.joining());

		System.out.println(string);

		final Deque<String> expr = list.stream()
			.map(Object::toString)
			.collect(Collectors.toCollection(ArrayDeque::new));

		System.out.println(expr);

		final TreeNode<String> tree = SymbolListParser.parse(expr);
		System.out.println(TreeFormatter.TREE.format(tree));

		System.out.println(tree);
	}

}
