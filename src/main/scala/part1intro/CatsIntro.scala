package part1intro

object CatsIntro {

  // Eq
  //val aComparison = 2 == "a string"

  // import class type
  import cats.Eq

  // import typeclass instances for the types needed, this imports all typeclass instances that are applicable to int
  import cats.instances.int._

  // Eq will not compile if not same type

  // use type class
  val intEquality = Eq[Int]
  val aTypeSafeComparison = intEquality.eqv(2,3)
  //val anUnsafeComarison = intEquality.eqv(2,"sasa")

  // use extention methods if applicable
  // nearly evey type class will have an extension

  import cats.syntax.eq._

  val anotherTypeSafeComparison = 2 === 3 // uses implicit
  val notEqualComparison = 2 =!= 3
 // val invalidComparison = 2 === "a"

  // can extends to composite types
  import cats.instances.list._ // doesnt compile till import of implicit basically Eq[List[Int]]
  val aListComparison = List(2) === List(3)

  // what if type isnt supported?
  // we can create a TC instance for custom type

  case class ToyCar(model: String, price: Double)

  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1,car2) =>
    car1.price == car2.price
  }

  val compare2ToyCards = ToyCar("Ferrari", 29.99) === ToyCar("Lambo", 29.99) // triple equal valid because presence of implicit eq of toycar

  // Summary

  // import cats.YOURTYPECLASS to use type class api
  // import cats.instances.YOURTYPE._ to bring implicit TC instances for supported type in scope
  // import cats.syntaz.YOURTYPECLASS._ to use extension methods the TC supports

}
