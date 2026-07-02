//noinspection ScalaWeakerAccess,TypeAnnotation
package part2abstractMath

object Semigroups {

  // Semigroup은 같은 타입의 값 두 개를 하나로 결합하는 타입 클래스입니다.
  // Cats가 제공하는 기본 인스턴스에 따라 Int는 덧셈, String은 문자열 이어붙이기로 결합됩니다.
  // 중요한 규칙: combine 연산은 결합 법칙을 만족해야 합니다.
  // 즉 combine(combine(a, b), c)와 combine(a, combine(b, c))의 결과가 같아야 합니다.
  import cats.Semigroup
  val naturalIntSemigroup = Semigroup[Int]
  val intCombination = naturalIntSemigroup.combine(2, 46) // 덧셈
  val naturalStringSemigroup = Semigroup[String]
  val stringCombination = naturalStringSemigroup.combine("I love ", "Cats") // 문자열 이어붙이기

  // 특정 타입에 고정된 API입니다.
  // Int와 String마다 별도 함수를 만들면 중복이 생기고, 새 타입을 지원할 때마다 함수를 추가해야 합니다.
  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  // 일반화된 API입니다.
  // T 타입에 대한 Semigroup 인스턴스만 있으면 어떤 List[T]든 같은 방식으로 줄일 수 있습니다.
  // 여기서 implicit semigroup은 "T를 어떻게 결합할지"를 컴파일러가 찾아 넣어주는 값입니다.
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
    list.reduce(semigroup.combine)

  // 연습 1: 새로운 타입을 Semigroup으로 지원하기
  // Eq에서 했던 것처럼, 우리가 만든 타입에 대한 타입 클래스 인스턴스를 직접 정의합니다.
  case class Expense(id: Long, amount: Double)
  implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] { (e1, e2) =>
    // Expense를 합칠 때 id는 더 큰 값을 유지하고, amount는 합산합니다.
    // 이 규칙도 결합 법칙을 만족해야 reduce처럼 여러 값을 순서대로 결합하는 코드가 안정적으로 동작합니다.
    Expense(Math.max(e1.id, e2.id), e1.amount + e2.amount)
  }

  // Semigroup이 제공하는 확장 메서드 - |+|
  // |+|는 combine을 연산자처럼 사용할 수 있게 해주는 Cats syntax입니다.
  import cats.syntax.semigroup._
  val anIntSum = 2 |+| 3 // 암시적 Semigroup[Int]가 필요합니다.
  val aStringConcat = "we like " |+| "semigroups"
  val aCombinationExpense = Expense(4, 80) |+| Expense(56, 46)

  // 연습 2: |+|를 사용해서 reduceThings2 구현하기
  // [T: Semigroup]은 context bound 문법입니다.
  // "T에 대한 Semigroup 인스턴스가 암시적 스코프에 있어야 한다"는 의미입니다.
  def reduceThings2[T: Semigroup](list: List[T]): T = list.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    println(intCombination)
    println(stringCombination)

    // 특정 타입에 고정된 API 사용
    val numbers = (1 to 10).toList
    println(reduceInts(numbers))
    val strings = List("I'm ", "starting ", "to ", "like ", "semigroups")
    println(reduceStrings(strings))

    // 일반화된 API 사용
    println(reduceThings(numbers)) // 컴파일러가 암시적 Semigroup[Int]를 주입합니다.
    println(reduceThings(strings)) // 컴파일러가 암시적 Semigroup[String]를 주입합니다.
    val numberOptions: List[Option[Int]] = numbers.map(n => Option(n))
    println(reduceThings(numberOptions)) // 숫자들의 합을 담은 Option[Int]
    val stringOptions: List[Option[String]] = strings.map(s => Option(s))
    println(reduceThings(stringOptions))

    // 연습 1 테스트
    val expenses = List(Expense(1, 99), Expense(2, 35), Expense(43, 10))
    println(reduceThings(expenses))

    // 연습 2 테스트
    println(reduceThings2(expenses))
  }
}
