package part2AbstractMath

import cats.implicits.toFlatMapOps

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}

object Monads {

  //lists
  val numbersList = List(1,2,3)
  val charsList = List("a","b","c")

  // TODO 1.1: how do you create all combinations of (number,char)?
  val all = for {
    number <- numbersList
    character <- charsList
  } yield (number,character)
  //can also do using flatmap and map

  //options
  val numberOption = Option(2)
  val charOption = Option('d')

  // ToDo 2.1: how do you create all combinations of this

  val allOption = for {
    number <- numberOption
    char <- charOption
  } yield (number,char)
  // again can use flatmap and map

  //futures

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val numberFuture = Future(42)
  val charFuture = Future('z')
  // ToDo 2.2: how do you create all combinations of this

  val comboFuture = for {
    number <- numberFuture
    char <- charFuture
  } yield (number,char)

  //again same with flatmap and map

// these can all be combined with same pattern, that consists of 2 fundamental opeterations:
  // - wraping a value in an M value
  // - the flatMapping mechanism
  //note flat map is used for values that arent sequential, need to stop thinking of flatMap as sequention

  // monad is a higher kinded type class

  trait MyMonad[M[_]] {
    def pure[A](value:A): M[A] // e.g if n is int -> List[Int]
    def flatMap[A,B](ma: M[A])(f: A => M[B]): M[B]
    def map[A,B](ma: M[A])(f: A =>B): M[B] =
      flatMap(ma)(x => pure(f(x))) // monads are also functors
  }

  // Cats Monad
  import cats.Monad
  import cats.instances.option._ //implicit Monad[Option]
  val optionMonad = Monad[Option]
  val anOption = optionMonad.pure(4) //returns option(4)

  // monad to transform
  val aTransformedOption = optionMonad.flatMap(anOption)(x => if (x % 3 == 0) Some( x +1) else None)

  import cats.instances.list._
  val listMonad = Monad[List]
  val aList  = listMonad.pure(3) //List(3)
  val aTransformedList = listMonad.flatMap(aList)(x => List(x, x+1))

  // TODO 2: use a monad[Future]

  import cats.instances.future._
  val futureMonad = Monad[Future]
  val aFuture = futureMonad.pure(10)// required an implicit execution context
  val aTransformedFuture = futureMonad.flatMap(aFuture)(x => Future(x + 10))

  // specialised for when you want to combine (tuple) values from two of these e/g: 2 future or 2 options...

  def getPairsList(numbers: List[Int], chars: List[Char]): List[(Int, Char)] = numbers.flatMap(n => chars.map(c => (n,c))) // same thing
  def getPairsOption(number: Option[Int], char: Option[Char]): Option[(Int, Char)] = number.flatMap(n => char.map(c => (n,c))) // same thing and so on....

  // implementation basically dont change so instead of duplicating the method what can we do
  //generalize
  def getPairs[M[_], A, B](ma:M[A], mb: M[B])(implicit monad: Monad[M]): M[(A,B)] = // with general method we can flatMap any datastructure as long as implicit is in scope
    monad.flatMap(ma)(a => monad.map(mb)(b => (a,b)))

  //powerful because we cna transform any monadic values

  // extension methods - wierder imports - pure, flatMap
  import cats.syntax.applicative._ //pure is here (part of applicative typeclass)
  val oneOption = 1.pure[Option] // implicit Monad[Option] will be used => Some(1)
  // can decorate any value and turn into a monadic value
  val oneList = 1.pure[List] // List(1)
  import cats.syntax.flatMap // flatMap is here part of its own package
  // rn we have option, list and future already in scope all of which have their own implementation of flatMap so not very useful atm s o lets make our own monad
  val oneOptionTransformed = oneOption.flatMap(x => (x + 1).pure[Option]) // flatMap becomes more useful when you implement data types that dont normally have their own flatMap method

  //TO DO 3: implement map method in MyMonad -> line 55
  // Monads extend functors so also have access to  functors extension methods
  val oneOptionMapped = Monad[Option].map(Option(2))(_ + 1) // done explicitly here
  import cats.syntax.functor._ //map is here
  val oneOptionMapped2 = oneOption.map(_ +2)
  // becuase we now have access to the map extension method and the flatMap extension method we can now have access to for comprehnesions
  val composedOptionFor = for {
    one <- 1.pure[Option]
    two <- 2.pure[Option]
  } yield one + two

  //TO DO 4: implement a shorter version of getPairs using for-comp
  def getPairs2[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: Monad[M]): M[(A,B)] =
    for {
      a <- ma
      b <- mb
    } yield (a,b) // same as ma.flatMap(a => mb.map(b => (a,b)))


  // SUMMARY
  // Monads are a higher-kinded type class that provides 2 fundamenal methods
  // - a pure method to wrap a normal value into a monadic value
  // - a flatMap method to transform monadic values in sequence
  // can implement map in terms of pure and flatMap, monads are natual extension of functors
  // extension methods are in other packages
  // monads are a powerful took for sequential transformations e.g: list combinations, option combinations, asynchronous chained computations and dependant computations
  // FOR_COMPREHENSIONS ARE NOT ITERERATIONS, more a mental model of chained transformations

  def main(args: Array[String]): Unit = {
    print(all)
    print(getPairs2(numbersList,charsList)) // suddenly okay since monad list in scope
    print(getPairs2(numberOption,charOption))
    getPairs(numberFuture,charFuture).foreach(print) // same as when run earlier
  }
}
