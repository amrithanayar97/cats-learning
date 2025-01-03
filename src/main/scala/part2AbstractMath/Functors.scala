package part2AbstractMath

object Functors {

  // Functor is a type class that provides a map method (like ones from lists, options,try ...) but will generalixe the idea of a map function

  val aModifiedList = List(1,2,3).map(_ +1)
  val aModifiedOption = Option(1).map(_ + 1)

  //simplified definition
  trait myFunctor[F[_]] { // higher kinded type class
    def map [A,B](initialValue: F[A])(f: A => B): F[B]
  }

  import cats.Functor
  import cats.instances.list._ // includes Functor[List]

  val  listFunctor = Functor[List]

  val incrementedNumbers = listFunctor.map(List(1,2,3))(_ + 1) // same as above

  import cats.instances.option._ // includes Functor[Option

  val optionFunctor = Functor[Option]
  val incrementorOption = optionFunctor.map(Option(1))( _ +1)

  // okay but this looks longer than just doing .map so why do we need a functor at all?
  // functors become important when we want to generalize a transformation

  // generalizing an API
  def do10xList(list: List[Int]): List[Int] = list.map(_ * 10)
  def do10xOption(option: Option[Int]): Option[Int] = option.map(_ * 10)

  //lets generlize it

  def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] = functor.map(container)(_ *10)

// TODO 1: define your own functor for a binary tree
  trait Tree[+T]

  object Tree {
    def leaf[T](value: T) = Leaf(value)
    def branch[T](value: T, left: Tree[T], right: Tree[T]): Tree[T] = Branch(value, left, right) // smart constructor not case class constructor
  }
  case class Leaf[+T](value: T) extends Tree[T]
  case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

  implicit object TreeFunctor extends Functor[Tree] {
    override def map[A,B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(v) => Leaf(f(v))
      case Branch(v, left,right) => Branch(f(v), map(left)(f), map(right)(f))
    }
  }
// now do10x is applicable to tree


  import cats.syntax.functor._ // this will allow you to call map on your own data structure as long as you have map in scope

  val tree: Tree[Int] = Tree.branch(40, Tree.branch(5, Tree.leaf(10), Tree.leaf(30)), Tree.leaf(20))

  val incrementedTree  = tree.map(_ + 1) // have access to map method even though tree doesnt have a map method

  // TODO2: create a shorter version on do10x method with extention method

  // extention method which is map

  def do15x[F[_]: Functor](container: F[Int]): F[Int] = container.map(_ * 10) // we never use the implicit so it can be done differenlt


  def main(args: Array[String]): Unit = {
    print(do10x(List(1,2,3)))
    print(do10x(Option(1)))

    print(do10x[Tree](Branch(30, Leaf(10), Leaf(20)))) // if type argument removed then since value is of type Branch then the compiler cant find implicit functor of branch
    print(do10x(Tree.branch(30, Tree.leaf(10), Tree.leaf(20)))) // but this works now using smart constructor
    println(incrementedTree)


  }

  // functors are good for data structures that are meant to be transformed in sequence:
  // specialized data structures for high performance algorithms
  // any mappable structure under same high-level api

}



