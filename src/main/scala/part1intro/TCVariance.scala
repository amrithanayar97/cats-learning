package part1intro


object TCVariance {

  import cats.instances.int._
  import cats.instances.option._
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(30)
  // val anInvalidComparison = Some(2) === None Eq[Some[Int]] not found this is related to variance

  // variance

  class Animal
  class Cat extends Animal

  // covariant type: subtyping is propagated to the generic type

  class Cage[+T]
  val cage: Cage[Animal] = new Cage[Cat] // Cat <: Animal so Cage[Cat] <: Cage[Animal]

  // contravariant type: subtyping is propagated BACKWARDS to the generic type, usually action types so something that does something e.g: to animal or cat
  class Vet[-T]
  val vet: Vet[Cat] = new Vet[Animal] // Cat <: Animal then Vet[Animal]<:Vet[Cat]

  // rule of thumb: if generic type "HAS a T" that is a COVARIANT
  // if generic type "ACTS on T" that is CONTRAVARIANT

  // variance effects how TC instances are being fetched

  trait SoundMaker[-T] //ACTS
  implicit object AnimalSoundMaker extends SoundMaker[Animal]

  def makeSound[T](implicit soundMaker: SoundMaker[T]): Unit = print("hi")
  makeSound[Animal] // ok TC instance is defined above
  makeSound[Cat] // also okay because TC instance for Animal is applicable

  // rule 1: contravariant TCs can use the superclass (Animal) instances if nothing is available strictly for that type (Cat)

  implicit object OptionSoundMaker extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  makeSound[Some[Int]] // ok since contravariant

  //covariant TC
  trait AnimalShow[+T] { // remember this is covariant because it contains some animals
    def show: String
  }
  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show = "animals everywhere"
  }
  implicit object CatsShow extends AnimalShow[Cat] {
    override def show = "cats everywhere"
  }

  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show

  // rule 2: covariant TCs will always use the more specific TC instance for that type

  // so we kinda have to pick a rule?

  // rule 3: you cant have both

  // Cats uses INVARIANT TYPE CLASSES


  // SUMMARY:


def main(args: Array[String]): Unit = {
  println(organizeShow[Cat]) // compiler inject CatsShow as implicit
  //println(organizeShow[Animal]) // compiler sees 2 instances (GeneralAnimalShow and CatsShow)
}
}
