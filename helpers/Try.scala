import moe.roselia.lisa.LispExp._
import moe.roselia.lisa.Evaluator

def handleOrThrow(value: Evaluator.EvalResult) = value match {
  case Evaluator.EvalSuccess(exp, env) => exp -> env
  case Evaluator.EvalFailureMessage(msg) => throw new RuntimeException(msg)
}

PrimitiveMacro {
  case (op :: caught :: finale :: Nil, env) =>
    try {
      handleOrThrow(Evaluator.eval(op, env))
    } catch {
      case ex =>
        handleOrThrow(Evaluator.eval(Apply(caught, WrappedScalaObject(ex) :: Nil), env))
    } finally {
      Evaluator.eval(finale, env)
    }
}