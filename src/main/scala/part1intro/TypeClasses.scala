//noinspection ScalaWeakerAccess
package part1intro

object TypeClasses {

  case class Person(name: String, age: Int)

  // 파트 1 - 타입 클래스 정의
  // JSONSerializer[T]는 "T 타입 값을 JSON 문자열로 바꿀 수 있다"는 능력을 표현한다.
  // 타입 클래스 자체는 동작의 형태만 정의하고, 실제 구현은 타입별 인스턴스가 담당한다.
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  // 파트 2 - 암시적 타입 클래스 인스턴스 만들기
  // implicit object로 정의하면 컴파일러가 JSONSerializer[String]이 필요한 위치에서 자동으로 찾을 수 있다.
  implicit object StringSerializer extends JSONSerializer[String] {
    override def toJson(value: String): String = "\"" + value + "\""
  }

  // Int 타입을 JSON으로 바꾸는 구체적인 구현이다.
  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJson(value: Int): String = value.toString
  }

  // Person 타입 전용 serializer다. 타입마다 원하는 JSON 표현을 독립적으로 정의할 수 있다.
  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJson(value: Person): String =
      s"""
         |{ "name" : ${value.name}, "age" : ${value.age} }
         |""".stripMargin.trim
  }

  // 파트 3 - API 제공하기
  // serializer는 암시적 인자로 전달된다. 호출자는 타입 T만 넘기면, 컴파일러가 알맞은 JSONSerializer[T]를 찾아 넣는다.
  def convertListToJSON[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  // 파트 4 - 확장 메서드로 기존 타입 확장하기
  // 기존 타입을 직접 수정하지 않고도, JSONSerializer[T]가 있는 타입에 .toJson 문법을 추가한다.
  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer[T]) {
      def toJson: String = serializer.toJson(value)
    }
  }

  def main(args: Array[String]): Unit = {
    println(convertListToJSON(List(Person("Alice", 23), Person("Xavier", 45))))
    val bob = Person("Bob", 35)
    // JSONSyntax를 import해야 implicit class가 스코프에 들어와서 bob.toJson을 사용할 수 있다.
    import JSONSyntax._
    println(bob.toJson)
  }
}
