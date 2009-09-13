package annis.sqlgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annis.model.AnnisNode;
import annis.model.Annotation;
import annis.model.AnnisNode.TextMatching;
import annis.sqlgen.Expression.Type;
import annis.sqlgen.model.Dominance;
import annis.sqlgen.model.Inclusion;
import annis.sqlgen.model.LeftAlignment;
import annis.sqlgen.model.LeftDominance;
import annis.sqlgen.model.LeftOverlap;
import annis.sqlgen.model.Overlap;
import annis.sqlgen.model.PointingRelation;
import annis.sqlgen.model.Precedence;
import annis.sqlgen.model.RightAlignment;
import annis.sqlgen.model.RightDominance;
import annis.sqlgen.model.RightOverlap;
import annis.sqlgen.model.SameSpan;
import annis.sqlgen.model.Sibling;
import de.deutschdiachrondigital.dddquery.analysis.DepthFirstAdapter;
import de.deutschdiachrondigital.dddquery.node.AAbsolutePathType;
import de.deutschdiachrondigital.dddquery.node.AAndExpr;
import de.deutschdiachrondigital.dddquery.node.AAttributeNodeTest;
import de.deutschdiachrondigital.dddquery.node.AChildAxis;
import de.deutschdiachrondigital.dddquery.node.AComparisonExpr;
import de.deutschdiachrondigital.dddquery.node.AContainingAxis;
import de.deutschdiachrondigital.dddquery.node.ADescendantAxis;
import de.deutschdiachrondigital.dddquery.node.AEdgeTypeSpec;
import de.deutschdiachrondigital.dddquery.node.AElementNodeTest;
import de.deutschdiachrondigital.dddquery.node.AEqComparison;
import de.deutschdiachrondigital.dddquery.node.AExactEdgeAnnotation;
import de.deutschdiachrondigital.dddquery.node.AExistanceEdgeAnnotation;
import de.deutschdiachrondigital.dddquery.node.AFollowingAxis;
import de.deutschdiachrondigital.dddquery.node.AFollowingSiblingAxis;
import de.deutschdiachrondigital.dddquery.node.AFunctionExpr;
import de.deutschdiachrondigital.dddquery.node.AImmediatelyFollowingAxis;
import de.deutschdiachrondigital.dddquery.node.ALeftAlignAxis;
import de.deutschdiachrondigital.dddquery.node.ALeftChildAxis;
import de.deutschdiachrondigital.dddquery.node.AMarkerSpec;
import de.deutschdiachrondigital.dddquery.node.AMatchingElementAxis;
import de.deutschdiachrondigital.dddquery.node.AMetaNodeTest;
import de.deutschdiachrondigital.dddquery.node.AOrExpr;
import de.deutschdiachrondigital.dddquery.node.AOverlappingAxis;
import de.deutschdiachrondigital.dddquery.node.AOverlappingFollowingAxis;
import de.deutschdiachrondigital.dddquery.node.AOverlappingPrecedingAxis;
import de.deutschdiachrondigital.dddquery.node.APathExpr;
import de.deutschdiachrondigital.dddquery.node.AQuotedText;
import de.deutschdiachrondigital.dddquery.node.ARangeSpec;
import de.deutschdiachrondigital.dddquery.node.ARegexpEdgeAnnotation;
import de.deutschdiachrondigital.dddquery.node.ARegexpLiteralExpr;
import de.deutschdiachrondigital.dddquery.node.ARegexpQuotedText;
import de.deutschdiachrondigital.dddquery.node.ARightAlignAxis;
import de.deutschdiachrondigital.dddquery.node.ARightChildAxis;
import de.deutschdiachrondigital.dddquery.node.ASiblingAxis;
import de.deutschdiachrondigital.dddquery.node.AStep;
import de.deutschdiachrondigital.dddquery.node.AStringLiteralExpr;
import de.deutschdiachrondigital.dddquery.node.PComparison;
import de.deutschdiachrondigital.dddquery.node.PExpr;
import de.deutschdiachrondigital.dddquery.node.PNodeTest;
import de.deutschdiachrondigital.dddquery.node.Token;

public class ClauseAnalysis extends DepthFirstAdapter {
	
//	private Logger log = Logger.getLogger(this.getClass());
	
	private List<AnnisNode> nodes;
	private Map<String, AnnisNode> boundNodes;
	private int aliasCount;
	private AnnisNode context;
	private AnnisNode target;

	private boolean firstStep;
	private boolean topLevel;
	private Expression.Type textValue;
	private Annotation annotation;
	
	private List<Annotation> metaAnnotations;
	
	private int precedenceBound;
		
	public ClauseAnalysis() {
		this(null, 0, new HashMap<String, AnnisNode>(), 0, new ArrayList<Annotation>());
	}
	
	public ClauseAnalysis(AnnisNode target, int aliasCount, Map<String, AnnisNode> boundNodes, int precedenceBound, List<Annotation> metaAnnotations) {
		this.target = target;
		this.aliasCount = aliasCount;
		this.boundNodes = boundNodes;
		this.precedenceBound = precedenceBound;
		this.metaAnnotations = metaAnnotations;
		
		firstStep = true;
		topLevel = true;
		nodes = new ArrayList<AnnisNode>();
	}
	
	///// Analysis interface
	
	public int nodesCount() {
		return nodes.size();
	}

	///// Syntax Tree Walking
	
	@Override
	public void caseAOrExpr(AOrExpr node) {
		throw new UnsupportedOperationException("using OR in a predicate is not supported");
	}
	
	@Override
	public void caseAAndExpr(AAndExpr node) {
		for (PExpr expr : node.getExpr()) {
			analyzeNestedPath(expr, true);
		}
	}

	@Override
	public void caseAPathExpr(APathExpr node) {
		if (isTopLevel()) {
			setTopLevel(false);
			super.caseAPathExpr(node);
			setTopLevel(true);
		} else {
			analyzeNestedPath(node, false);
		}
	}
	
	@Override
	public void inAPathExpr(APathExpr node) {
		textValue = Type.SPAN;
	}
	
	@Override
	public void outAPathExpr(APathExpr node) {
		if (node.getPathType() instanceof AAbsolutePathType)
			target.setRoot(true);
		if (annotation != null)
			target.addNodeAnnotation(annotation);
	}
	
	@Override
	public void inAStep(AStep node) {
		// XXX: should check for valid combinations of axis and node test here
		final PNodeTest nodeTest = node.getNodeTest();
		if (nodeTest instanceof AAttributeNodeTest || nodeTest instanceof AMetaNodeTest)
			return;
		
		// advance context
		context = target;
		
		// check if target node exists
		String variable = token(node.getVariable());
		if (variable != null) {
			if (boundNodes.containsKey(variable))
				target = boundNodes.get(variable);
			else {
				target = newNode();
				target.setVariable(variable);
				boundNodes.put(variable, target);
			}
		}
		
		// nope, new node
		else
			target = newNode();
	}
	
	@Override
	public void outAStep(AStep node) {
		firstStep = false;
	}
	
	@Override
	public void inAChildAxis(AChildAxis node) {
		if (firstStep)
			return;
		
		String type = token(((AEdgeTypeSpec) node.getEdgeTypeSpec()).getEdgeType());
		String name = token(((AEdgeTypeSpec) node.getEdgeTypeSpec()).getName());
		
		if ("d".equals(type))
			context.addJoin(new Dominance(target, name, 1));
		else if ("p".equals(type))
			context.addJoin(new PointingRelation(target, name, 1));
		else 
			throw new IllegalArgumentException("unknown edge type: " + type);
	}
	
	// XXX: change to isLeftChild(), isRightChild()?
	@Override
	public void inALeftChildAxis(ALeftChildAxis node) {
		context.addJoin(new LeftDominance(target));
	}
	
	@Override
	public void inARightChildAxis(ARightChildAxis node) {
//		context.addJoinX(new NumberJoin(context.postRank(), target.postRank(), +1));
		context.addJoin(new RightDominance(target));
	}
	
	@Override
	public void inAExistanceEdgeAnnotation(AExistanceEdgeAnnotation node) {
		String namespace = token(node.getNamespace());
		String name = token(node.getType());
		target.addEdgeAnnotation(new Annotation(namespace, name));
	}
	
	@Override
	public void caseAExactEdgeAnnotation(AExactEdgeAnnotation node) {
		String namespace = token(node.getNamespace());
		String name = token(node.getType());
		String value = token(node.getValue());
		target.addEdgeAnnotation(new Annotation(namespace, name, value, TextMatching.EXACT));
	}
	
	@Override
	public void caseARegexpEdgeAnnotation(ARegexpEdgeAnnotation node) {
		String namespace = token(node.getNamespace());
		String name = token(node.getType());
		String value = token(node.getValue());
		target.addEdgeAnnotation(new Annotation(namespace, name, value, TextMatching.REGEXP));
	}
	
	// FIXME: namespaces in node name
	@Override
	public void inAElementNodeTest(AElementNodeTest node) {
		String name = token(node.getName());
		target.setName(name);
	}
	
	@Override
	public void inAAttributeNodeTest(AAttributeNodeTest node) {
		String namespace = token(node.getNamespace());
		String name = token(node.getName());
		textValue = Type.ATTRIBUTE;
		annotation = new Annotation(namespace, name);
	}
	
	@Override
	public void inAMarkerSpec(AMarkerSpec node) {
		String text = token(node.getMarker());
		target.setMarker(text);
	}
	
	@Override
	public void inADescendantAxis(ADescendantAxis node) {
		String type = token(((AEdgeTypeSpec) node.getEdgeTypeSpec()).getEdgeType());
		String name = token(((AEdgeTypeSpec) node.getEdgeTypeSpec()).getName());

		if ("d".equals(type)) {
			if (node.getRangeSpec() == null)
				context.addJoin(new Dominance(target, name));
			else {
				ARangeSpec rangeSpec = (ARangeSpec) node.getRangeSpec();
				if (rangeSpec.getMax() == null) {
					int distance = number(rangeSpec.getMin());
					context.addJoin(new Dominance(target, name, distance));
				} else {
					int min = number(rangeSpec.getMin());
					int max = number(rangeSpec.getMax());
					context.addJoin(new Dominance(target, name, min, max));
				}
			}
		} else if ("p".equals(type)) {
			if (node.getRangeSpec() == null)
				context.addJoin(new PointingRelation(target, name));
			else {
				ARangeSpec rangeSpec = (ARangeSpec) node.getRangeSpec();
				if (rangeSpec.getMax() == null) {
					int distance = number(rangeSpec.getMin());
					context.addJoin(new PointingRelation(target, name, distance));
				} else {
					int min = number(rangeSpec.getMin());
					int max = number(rangeSpec.getMax());
					context.addJoin(new PointingRelation(target, name, min, max));
				}
			}
		} else 
			throw new IllegalArgumentException("unknown edge type: " + type);
	}
	
//	// XXX: refactor ARangeSpec into ADistance and ARangedDistance
//	// FIXME: logic transfered to inADescendantAxis, inAFollowingAxis
//	@Override
//	public void inARangeSpec(ARangeSpec node) {
//		if (node.getMax() == null) {
//			int distance = number(node.getMin());
//			context.addJoinX(new NumberJoin(context.level(), target.level(), -distance));
//		} else {
//			int min = number(node.getMin());
//			int max = number(node.getMax());
//			context.addJoinX(new NumberJoin(JoinOperation.LT, context.level(), target.level(), -min));
//			context.addJoinX(new NumberJoin(JoinOperation.GT, context.level(), target.level(), -max));
//		}
//	}
	
	@Override
	public void caseAComparisonExpr(AComparisonExpr node) {
		AEqComparison comp = (AEqComparison) node.getComparison();
		
		Expression lhs = evaluate(node.getLhs());
		Expression rhs = evaluate(node.getRhs());

		lhs.assign(comp, rhs);
	}
	
	@Override
	public void inAFollowingAxis(AFollowingAxis node) {
		if (node.getRangeSpec() == null) {
			if (precedenceBound > 0)
				context.addJoin(new Precedence(target, 1, precedenceBound));
			else
				context.addJoin(new Precedence(target));
		} else {
			ARangeSpec rangeSpec = (ARangeSpec) node.getRangeSpec();
			int min = number(rangeSpec.getMin());
			if (rangeSpec.getMax() == null)
				context.addJoin(new Precedence(target, min));
			else {
				int max = number(rangeSpec.getMax());
				context.addJoin(new Precedence(target, min, max));
			}
		}
	}
	
	@Override
	public void inAImmediatelyFollowingAxis(AImmediatelyFollowingAxis node) {
//		context.addJoinX(new NumberJoin(context.leftToken(), target.rightToken(), -1));
		context.addJoin(new Precedence(target, 1));
	}
	
	@Override
	public void inAMatchingElementAxis(AMatchingElementAxis node) {
		context.addJoin(new SameSpan(target));
	}
	
	@Override
	public void inALeftAlignAxis(ALeftAlignAxis node) {
		context.addJoin(new LeftAlignment(target));
	}
	
	@Override
	public void inARightAlignAxis(ARightAlignAxis node) {
		context.addJoin(new RightAlignment(target));
	}
	
	@Override
	public void inAContainingAxis(AContainingAxis node) {
		context.addJoin(new Inclusion(target));
	}
	
	@Override
	public void inAOverlappingFollowingAxis(AOverlappingFollowingAxis node) {
		context.addJoin(new LeftOverlap(target));
	}
	
	@Override
	public void inAOverlappingPrecedingAxis(AOverlappingPrecedingAxis node) {
		context.addJoin(new RightOverlap(target));
	}
	
	@Override
	public void inAOverlappingAxis(AOverlappingAxis node) {
		context.addJoin(new Overlap(target));
	}
	
	@Override
	public void inASiblingAxis(ASiblingAxis node) {
		context.addJoin(new Sibling(target));
	}
	
	@Override
	public void inAFollowingSiblingAxis(AFollowingSiblingAxis node) {
		context.addJoin(new Sibling(target));
		context.addJoin(precedenceBound == 0 ? new Precedence(target) : new Precedence(target, 1, precedenceBound));
	}
	
	@Override
	public void inAFunctionExpr(AFunctionExpr node) {
		String name = token(node.getName());

		if ("isToken".equals(name))
			target.setToken(true);
	}
	
	@Override
	public void inAMetaNodeTest(AMetaNodeTest node) {
		annotation = new Annotation(token(node.getNamespace()), token(node.getName()));
	}
	
	@Override
	public void outAMetaNodeTest(AMetaNodeTest node) {
		metaAnnotations.add(annotation);
		annotation = null;
	}
	
	@Override
	public void caseAQuotedText(AQuotedText node) {
		annotation.setValue(token(node.getString()));
		annotation.setTextMatching(TextMatching.EXACT);
	}
	
	@Override
	public void caseARegexpQuotedText(ARegexpQuotedText node) {
		annotation.setValue(token(node.getRegexp()));
		annotation.setTextMatching(TextMatching.REGEXP);
	}

	///// Private Helper
	
	private AnnisNode newNode() {
		AnnisNode node = new AnnisNode(++aliasCount);
		nodes.add(node);
		return node;
	}
	
	private ClauseAnalysis analyzeNestedPath(PExpr expr, boolean advance) {
		ClauseAnalysis nested = new ClauseAnalysis(target, aliasCount, boundNodes, precedenceBound, metaAnnotations);
		expr.apply(nested);
		nodes.addAll(nested.nodes);
		aliasCount = nested.aliasCount;
		if (advance) {
			context = nested.context;
			target = nested.target;
		}
		return nested;
	}
	
	private Expression evaluate(final PExpr expr) {
		// string literal
		if (expr instanceof AStringLiteralExpr) {
			return new AbstractExpression(Type.STRING) {

				protected void doAssign(PComparison op, Expression rhs) {
//					context.addJoinX(new JoinX(
//							sqlStringLiteral(toSql()), sqlStringLiteral(rhs.toSql())));
					throw new UnsupportedOperationException("Can't assign something to a string");
				}

				public boolean canAssign(PComparison op, Expression rhs) {
//					return rhs.getType() == Type.STRING;
					return false;
				}

				public String toSql() {
					AStringLiteralExpr stringLiteral = (AStringLiteralExpr) expr;
					return token(stringLiteral.getString());
				}

//				private String sqlStringLiteral(String string) {
//					return "'" + string + "'";
//				}
			};
		
		// regexp literal
		} else if (expr instanceof ARegexpLiteralExpr) {
			return new AbstractExpression(Type.REGEXP) {

				protected void doAssign(PComparison op, Expression rhs) {
//					context.addJoinX(new JoinX(JoinOperation.REGEXP, 
//							sqlStringLiteral(toSql()), sqlStringLiteral(rhs.toSql())));	// XXX: not really a join between tables
					throw new UnsupportedOperationException("Can't assign something to a regexp");
				}

				public boolean canAssign(PComparison op, Expression rhs) {
//					return rhs.getType() == Type.STRING;
					return false;
				}

				public String toSql() {
					ARegexpLiteralExpr regexpLiteral = (ARegexpLiteralExpr) expr;
					return token(regexpLiteral.getRegexp());
				}
				
//				private String sqlStringLiteral(String string) {
//					return "'" + string + "'";
//				}
			};
		
		// path
		} else if (expr instanceof APathExpr) {
			final ClauseAnalysis nested = analyzeNestedPath(expr, false);
			return new AbstractExpression(nested.textValue) {

				protected void doAssign(PComparison op, Expression rhs) {
					
					
					TextMatching textMatching = null;
					switch (rhs.getType()) {
					case STRING: textMatching = TextMatching.EXACT; break;
					case REGEXP: textMatching = TextMatching.REGEXP; break;
					default: unsupported(
							"unsupported operation on node span: expected '=' or '~'; was " + rhs.getType());
					}

					switch(getType()) {
					case SPAN: 
						AnnisNode target = nested.target;
						target.setSpannedText(rhs.toSql(), textMatching); break;
					case ATTRIBUTE: 
						Annotation annotation = nested.annotation; 
						annotation.setValue(rhs.toSql());
						annotation.setTextMatching(textMatching);
						break;
					default: unsupported("can't evaluate text value of path: " + getType());
					}
				}

				public boolean canAssign(PComparison op, Expression rhs) {
					return rhs.getType() == Type.STRING || rhs.getType() == Type.REGEXP;
				}

			};
		} 
		
		unsupported("can't evaluate expression: " + expr.getClass());
		return null;
	}
	
	private void unsupported(String message) {
		throw new UnsupportedOperationException(message);
	}
	
	private int number(Token token) {
		return Integer.parseInt(token(token));
	}

	private String token(Token token) {
		return token != null ? token.getText() : null;
	}
	
	///// Getter / Setter
	
	public boolean isTopLevel() {
		return topLevel;
	}

	public void setTopLevel(boolean topLevel) {
		this.topLevel = topLevel;
	}

	public AnnisNode getContext() {
		return context;
	}

	public void setContext(AnnisNode context) {
		this.context = context;
	}

	public AnnisNode getTarget() {
		return target;
	}

	public void setTarget(AnnisNode target) {
		this.target = target;
	}

	public Type getTextValue() {
		return textValue;
	}

	public void setTextValue(Type textValue) {
		this.textValue = textValue;
	}

	public List<AnnisNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<AnnisNode> nodes) {
		this.nodes = nodes;
	}

	public boolean isFirstStep() {
		return firstStep;
	}

	public void setFirstStep(boolean firstStep) {
		this.firstStep = firstStep;
	}

	public int getPrecedenceBound() {
		return precedenceBound;
	}

	public void setPrecedenceBound(int precedenceBound) {
		this.precedenceBound = precedenceBound;
	}

	public List<Annotation> getMetaAnnotations() {
		return metaAnnotations;
	}

	public void setMetaAnnotations(List<Annotation> metaAnnotations) {
		this.metaAnnotations = metaAnnotations;
	}
	
}