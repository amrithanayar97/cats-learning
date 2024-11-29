package part1intro

object Implicits {
  // implicit class
  case class Person(name: String) {
    def greet: String = s"hi, my name is $name"
  }

  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet
  }

  // explicit way:
  val impersonableString = new ImpersonableString("Amritha")
  impersonableString.greet

  //implicit Way:
  val greeting = "Amritha".greet

  // same with arguments and values

  // example is basically how we encoding/coding



  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[",",","]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(person: Person): String =
      s"""
         |{"name" : "${person.name}"}
         |""".stripMargin
  }

  val personsJson = listToJson(List(Person("Alice"), Person("Bob")))
  // above we need to define implicit serializer for every unique type


  // implicit methods when applicable to more than one type
  // An upper type bound T <: A declares that type variable T refers to a subtype of type A
  // Product type is any case class

  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{"${value.productElementName(0)}" : "${value.productElement(0)}"}
         |""".stripMargin
  } // all case classes extend Product

  // so

  case class Cat(name: String)
  val catsToJson = listToJson(List(Cat("Amritha"), Cat("Idli")))
// this does listToJson(List(Cat("Amritha"), Cat("Idli")))(oneArgCaseClassSerializer[Cat]

 // can be used for implicit conversion but its discouraged
  def main(args: Array[String]): Unit = {
     println(oneArgCaseClassSerializer[Cat].toJson(Cat("Amritha")))
  }
}
