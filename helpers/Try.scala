import moe.roselia.lisa.LispExp._
import moe.roselia.lisa.Evaluator

def handleOrThrow(value: Evaluator.EvalResult) = value match {
  case Evaluator.EvalSuccess(exp, env) => exp -> env
  case _ => throw new RuntimeException(value.toString)
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
