package part2AbstractMath

object Monoids {
  import cats.Semigroup
  import cats.instances.int._
  import cats.syntax.semigroup._

  val numbers = (11 to 1000).toList
  // |+| is always associative
  val sumLeft = numbers.foldLeft(0)(_ |+| _)
  val sumRight = numbers.foldRight(0)(_ |+| _)

  //def combineFold[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
  //  list.foldLeft(...)(_ |+| _) // what can we put as starting value? semigroup is not enough to give us starting value unless we know type T

  // MONOIDS are like a semigroup but with the extra capability of having starting value
  import cats.Monoid
  val intMonoid = Monoid[Int]
  val combineInt = intMonoid.combine(23,99)
  val zero = intMonoid.empty // returns zero value for that type so here 0

  import cats.instances.string._ // bring the implicit Monoid[String] in scope
  val emptyString = Monoid[String].empty // ""
  val combineString = Monoid[String].combine("I understand ", "monoids")

  import cats.instances.option._ // construct an implicit Monoid[Option[Int
  val emptyOption = Monoid[Option[Int]].empty //None
  val combineOption = Monoid[Option[Int]].combine(Option(2), Option.empty[Int]) //Some(2) combines without needing to unwrap option

  // extension methods for Moniods - |+| (same as semigroups)
 // import cats.syntax.monoid._ //either this one or cats.syntax.semigroup._
  val combinedOptionFancy = Option(3) |+| Option(7)

  //ToDo1: implement a combineFold

  def combineFold[T](list: List[T])(implicit monoid: Monoid[T]): T =
    list.foldLeft(monoid.empty)(_ |+| _)

  //ToDo1: combine a list of phonebooks as Maps[String,Int]
  val phonebooks = List(
    Map(
      "Alice" -> 235,
      "Bob" -> 647
    ),
  Map(
    "Charlie" -> 372,
    "Daniel" -> 889
  ),
    Map(
      "Tina" -> 123
    )
  )

  import cats.instances.map._
  val massivePhonebook =combineFold(phonebooks)

  // TO DO 3

  case class ShoppingCart(items: List[String], total: Double)
  implicit val shoppingCartMonoid: Monoid[ShoppingCart] = Monoid.instance(
    ShoppingCart(List(), 0.0),
    (sa,sb) => ShoppingCart(sa.items ++ sb.items, sa.total + sb.total)
  )
  def checkout(shoppingCardsL: List[ShoppingCart]): ShoppingCart =
    combineFold(shoppingCardsL)


  def main(args: Array[String]): Unit = {
    println(sumLeft)
    println(sumRight)//always same as sumLeft

    println(combineFold(numbers)) // also same
    println(combineFold(List("I ", "like ","monoids")))

    println(checkout(List(ShoppingCart(List("iphone","shoes"), 799),ShoppingCart(List("TV"), 20000),ShoppingCart(List(),0))))
  }


}
