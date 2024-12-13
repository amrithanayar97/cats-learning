package part2AbstractMath

object Semigroups {

  // Semigroups are a typeclass to COMBINE elements of the same type
  // standard of combination between 2 values

  import cats.Semigroup
  import cats.instances.int._

  val naturalIntSemigroup = Semigroup[Int]
  val intCompination = naturalIntSemigroup.combine(2,46) //addition

  import cats.instances.string._
  val naturalStringSemigroup = Semigroup[String]
  val stringCombination = naturalStringSemigroup.combine("hi", "amritha") //concat

  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine) // combine acts are a reducer between numbers

  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  // can we make this more generic?
  def reduceAnything[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(semigroup.combine)

  //TODO 1: support a new type
 case class Expense(id: Long, amount: Double)

    implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] {(e1, e2) =>
      Expense(Math.max(e1.id,e2.id), e1.amount+e2.amount)
  }


      // extension methods from Semigroup -> |+|
      import cats.syntax.semigroup._

      val anIntSum = 2 |+| 3  //requires implicit semigroup of int

      val aCombinedExpense = Expense(30, 500) |+| Expense(5,900)

      // ToDo 2: implement reduceThings2

     def reduceAnything2[T : Semigroup](list: List[T]): T = list.reduce(_ |+| _) // cleaner wow we can even remove the implicit and use [T: Semigroup]



  def main(args: Array[String]): Unit = {
    println(intCompination)
    println(stringCombination)

    val numbers = (1 to 10).toList
    println(reduceInts(numbers)) // sum of al numbers up to 10

    println(reduceAnything(numbers))


    import cats.instances.option._
    val numberOptions:List[Option[Int]] = numbers.map(n => Option(n))

    println(reduceAnything(numberOptions)) // an Option[Int] contains sum of all number since semigroup is available in scope -> combination option[int] is sum if both Some() or None if one is None

    // Option[String] gives Option[String] that has concat of all strings

    val expenses = List(Expense(1, 200), Expense(2, 300), Expense(3,400))

    println(reduceAnything(expenses))
    println(reduceAnything2(expenses))



  }
}
