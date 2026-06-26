//noinspection ScalaWeakerAccess,TypeAnnotation,ConvertExpressionToSAM
package part1recap

object Implicits {

  // 암시적 클래스
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name"
  }

  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet
  }

  val greeting = "Peter".greet // new ImpersonableString("Peter").greet와 동일

  // 암시적 변환을 스코프로 가져오기
  import scala.concurrent.duration._
  val oneSec = 1.second

  // 암시적 인자와 값
  def increment(x: Int)(implicit amount: Int) = x + amount
  implicit val defaultAmount: Int = 10
  val incremented2 = increment(2) // 컴파일러가 암시적 인자 10을 전달

  def multiply(x: Int)(implicit times: Int) = x * times
  val times2 = multiply(2)

  // 더 복잡한 예제
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(person: Person): String =
      s"""
         |{"name" : "${person.name}"}
         |""".stripMargin
  }
  val personJson = listToJson(List(Person("Alice"), Person("Bob")))
  // 암시적 인자는 어떤 타입이 존재함을 증명하는 데 사용됨

  // 암시적 메서드
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{"${value.productElementName(0)}" : "${value.productElement(0)}"}
         |""".stripMargin.trim
  }

  case class Cat(catName: String)
  val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))
  // 내부적으로는 다음과 같이 처리됨: val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))(oneArgCaseClassSerializer[Cat])
  // 암시적 메서드는 어떤 타입이 존재함을 증명하는 데 사용됨
  // 암시적 변환에도 사용할 수 있지만 권장하지 않음

  def main(args: Array[String]): Unit = {}
}
