//noinspection ScalaWeakerAccess,TypeAnnotation,ConvertExpressionToSAM
package part1recap

object Implicits {

  // 암시적 클래스
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name"
  }

  // String 타입 자체에는 greet 메서드가 없지만, implicit class를 통해 새 문법을 추가할 수 있다.
  // 컴파일러는 "Peter".greet를 new ImpersonableString("Peter").greet로 바꿔 해석한다.
  implicit class ImpersonableString(name: String) {
    def greet: String = Person(name).greet
  }

  val greeting = "Peter".greet // new ImpersonableString("Peter").greet와 동일

  // 암시적 변환을 스코프로 가져오기
  // duration 패키지의 암시적 변환/확장 메서드를 import하면 Int 값에 second 같은 시간 단위 문법을 사용할 수 있다.
  import scala.concurrent.duration._
  val oneSec = 1.second

  // 암시적 인자와 값
  def increment(x: Int)(implicit amount: Int) = x + amount
  // 같은 스코프 안에 implicit Int 값이 있으면, amount 인자를 생략했을 때 컴파일러가 이 값을 자동으로 전달한다.
  implicit val defaultAmount: Int = 10
  val incremented2 = increment(2) // 컴파일러가 암시적 인자 10을 전달

  // multiply도 implicit Int를 요구하므로, 위의 defaultAmount가 times 인자로 재사용된다.
  def multiply(x: Int)(implicit times: Int) = x * times
  val times2 = multiply(2)

  // 더 복잡한 예제
  // JSONSerializer[T]는 T 타입 값을 JSON 문자열로 바꿀 수 있다는 능력을 표현하는 타입 클래스다.
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  // listToJson은 구체적인 변환 방식을 직접 알지 않고, 암시적 JSONSerializer[T]에게 변환을 위임한다.
  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  // Person 타입에 대한 JSONSerializer 인스턴스다. List[Person]을 변환할 때 컴파일러가 이 값을 찾는다.
  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(person: Person): String =
      s"""
         |{"name" : "${person.name}"}
         |""".stripMargin
  }
  val personJson = listToJson(List(Person("Alice"), Person("Bob")))
  // 암시적 인자는 어떤 타입이 존재함을 증명하는 데 사용됨

  // 암시적 메서드
  // T <: Product는 T가 Product의 하위 타입이어야 한다는 뜻이다.
  // case class는 Product를 상속하므로 productElementName/productElement로 필드 이름과 값을 읽을 수 있다.
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{"${value.productElementName(0)}" : "${value.productElement(0)}"}
         |""".stripMargin.trim
  }

  case class Cat(catName: String)
  // Cat 전용 serializer를 직접 만들지 않아도, 컴파일러가 oneArgCaseClassSerializer[Cat]를 호출해 암시적 인스턴스를 만든다.
  val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))
  // 내부적으로는 다음과 같이 처리됨: val catsToJson = listToJson(List(Cat("Tom"), Cat("Garfield")))(oneArgCaseClassSerializer[Cat])
  // 암시적 메서드는 어떤 타입이 존재함을 증명하는 데 사용됨
  // 암시적 변환에도 사용할 수 있지만 권장하지 않음

  def main(args: Array[String]): Unit = {}
}
