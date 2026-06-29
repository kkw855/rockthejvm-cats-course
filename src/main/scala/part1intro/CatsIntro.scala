//noinspection TypeAnnotation,ScalaWeakerAccess
package part1intro

object CatsIntro {

  // Eq 타입 클래스
  // val aComparison = 2 == "a string" // 잘못된 비교이며 컴파일 에러가 발생한다

  // 파트 1 - 타입 클래스 가져오기
  import cats.Eq

  // 파트 2 - 필요한 타입의 타입 클래스 인스턴스 가져오기
  // Eq[Int] 인스턴스를 스코프에 가져와야 Int 값을 타입 안전하게 비교할 수 있다.
  import cats.instances.int._

  // 파트 3 - 타입 클래스 API 사용하기
  // Eq[Int]는 Int끼리 비교하는 방법을 담은 타입 클래스 인스턴스다.
  val intEquality = Eq[Int]
  val aTypeSafeComparison = intEquality.eqv(2, 3) // false
  // val anUnsafeComparison = intEquality.eqv(2, "a string") // 컴파일되지 않는다

  // 파트 4 - 가능한 경우 확장 메서드 사용하기
  // cats.syntax.eq._를 import하면 Eq 인스턴스가 있는 타입에 ===, =!= 문법을 사용할 수 있다.
  import cats.syntax.eq._
  val anotherTypeSafeComp = 2 === 3 // false
  val neqComparison = 2 =!= 3 // true
  // val invalidComparison = 2 === "a string" // 컴파일되지 않는다
  // 확장 메서드는 알맞은 타입 클래스 인스턴스가 스코프에 있을 때만 사용할 수 있다.

  // 파트 5 - List 같은 합성 타입으로 타입 클래스 연산 확장하기
  // Int에 대한 Eq가 있으면 Cats는 List[Int] 비교에도 Eq를 사용할 수 있다.
  val aListComparison = List(2) === List(3) // false를 반환한다

  // 파트 6 - 커스텀 타입을 위한 타입 클래스 인스턴스 만들기
  case class ToyCar(model: String, price: Double)

  // Eq.instance로 ToyCar끼리 비교하는 기준을 직접 정의한다.
  // 여기서는 모델명은 무시하고 가격이 같으면 같은 ToyCar로 판단한다.
  implicit val toyCarEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1, car2) =>
    car1.price === car2.price
  }

  val compareTwoToyCars = ToyCar("Ferrari", 29.99) === ToyCar("Lamborghini", 29.99) // true

  def main(args: Array[String]): Unit = {}
}
