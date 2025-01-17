package part2AbstractMath

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

  def main(args: Array[String]): Unit = {
    print(all)
    print(getPairs(numbersList,charsList)) // suddenly okay since monad lisy in scope
    print(getPairs(numberOption,charOption))
    getPairs(numberFuture,charFuture).foreach(print) // same as when run earlier
  }
}
