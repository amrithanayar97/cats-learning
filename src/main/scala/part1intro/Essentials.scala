package part1intro

object Essentials {

  val incrementer: Int => Int = x => x + 1

  val checkerboard = List(1,2,3).flatMap(n => List("a","b","c").map(c => (n,c)))  // List((1,a),(1,b),(2,c),...)

  val alsoCheckerboard = for {
    n <- List(1,2,3)
    c <- List("a","b","c")
  } yield (n,c )

  val partialFuncion: PartialFunction[Int,Int] = {
    case 1 => 50
    case 8 => 55
    case 100 => 999
  }  // partial functions are soley matching

  trait HigherKindedType[F[_]] //abstract class with generic type argument that has type arg itself

  trait SequenceChecker[F[_]] {
    def isSequential: Boolean
  }

  val listChecker = new SequenceChecker[List] { // e.g: we use generic type List without defining List of what
    override def isSequential: Boolean = true
  }

  def main(args: Array[String]): Unit = {

  }
}
