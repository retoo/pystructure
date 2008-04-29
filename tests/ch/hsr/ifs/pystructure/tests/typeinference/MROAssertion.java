package ch.hsr.ifs.pystructure.tests.typeinference;

import org.python.pydev.parser.jython.ast.Expr;

import ch.hsr.ifs.pystructure.typeinference.basetype.CombinedType;
import ch.hsr.ifs.pystructure.typeinference.basetype.IType;
import ch.hsr.ifs.pystructure.typeinference.inferencer.PythonTypeInferencer;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Linearisation;
import ch.hsr.ifs.pystructure.typeinference.model.definitions.Module;
import ch.hsr.ifs.pystructure.typeinference.results.types.MetaclassType;
import ch.hsr.ifs.pystructure.typeinference.visitors.Workspace;

public class MROAssertion extends InferencerAssertion {

	private final String solution;

	public MROAssertion(String fileName, String expression, int line, String solution) {
		super(fileName, expression, line);
		this.solution = solution;
	}

	@Override
	protected void doIt(PythonTypeInferencer inferencer, Workspace workspace,
			Module module, Expr expression) {
		CombinedType evaluateType = inferencer.evaluateType(workspace, module, expression.value);

		if (evaluateType.getTypes().size() == 1) {
			IType type = evaluateType.getTypes().iterator().next();

			if (type instanceof MetaclassType) {
				MetaclassType metaClassType = (MetaclassType) type;

				Linearisation li = inferencer.getMRO(workspace, module, metaClassType.getKlass());

				assertNotNull(li);
				assertMro(solution, li);

			} else {
				throw new RuntimeException("invalid type, should be MetaClassType");
			}
		} else {
			throw new RuntimeException("the specified mro expression in " + filename + " on line " + line 
					+ " doesn't resolve or resolves to more than one type.");
		}
	}

	private void assertMro(String expected, Linearisation li) {
		String actual = li.getStringRep();

		if (!expected.equals(actual)) {
			throw new InferencerAssertionError("MRO", expected, actual, expression, filename, line); 
		}
	}
}
