//noinspection TypeAnnotation,ScalaWeakerAccess
package part1intro

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.{Failure, Success}

import java.util.concurrent.Executors

object Essentials {

  // 값
  val aBoolean: Boolean = false

  // 표현식은 평가되어 하나의 값이 된다
  val anIfExpression = if (2 > 3) "bigger" else "smaller"

  // 명령문과 표현식
  val theUnit = println("Hello, Scala") // Unit은 다른 언어의 "void"와 비슷하다

  // 객체 지향 프로그래밍
  class Animal
  class Cat extends Animal
  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  // 상속 모델: 클래스는 최대 1개만 확장하고, 트레이트는 0개 이상 상속할 수 있다
  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Crunch!")
  }

  // 싱글턴
  object MySingleton // 한 줄로 표현한 싱글턴 패턴

  // 컴패니언
  object Carnivore // Carnivore 클래스의 컴패니언 객체

  // 제네릭
  class MyList[A]

  // 메서드 표기법
  val three = 1 + 2
  val anotherThree = 1.+(2)

  // 함수형 프로그래밍
  val incrementer: Int => Int = x => x + 1
  val incremented = incrementer(45) // 결과: 46

  // map, flatMap, filter
  val processedList = List(1, 2, 3).map(incrementer) // List(2,3,4)
  val aLongerList = List(1, 2, 3).flatMap(x => List(x, x + 1)) // List(1,2, 2,3, 3,4)

  // for-comprehension
  val checkerboard = List(1, 2, 3).flatMap(n => List('a', 'b', 'c').map(c => (n, c)))
  val anotherCheckerboard = for {
    n <- List(1, 2, 3) // flatMap으로 변환됨
    c <- List('a', 'b', 'c') // map으로 변환됨
  } yield (n, c) // 위와 같은 표현식

  // Option과 Try
  val anOption: Option[Int] = Option( /* null일 수도 있는 값 */ 3) // Some(3)
  val doubleOption: Option[Int] = anOption.map(_ * 2)

  val anAttempt: Try[Int] = Try( /* 예외를 던질 수도 있는 코드 */ 42) // Success(42)
  val aModifiedAttempt: Try[Int] = anAttempt.map(_ * 10)

  // 패턴 매칭
  val anUnknown: Any = 45
  val ordinal = anUnknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }

  val optionDescription: String = anOption match {
    case Some(value) => s"the option is not empty: $value"
    case None        => "the option is empty"
  }

  // Future
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aFuture = Future {
    // 실행할 코드
    42
  }

  // 완료될 때까지 비동기적으로 기다림
  aFuture.onComplete {
    case Success(value)     => println(s"The async meaning of life is $value")
    case Failure(exception) => println(s"Meaning of value failed: $exception")
  }

  // Future를 map으로 변환
  val anotherFuture = aFuture.map(_ + 1) // 완료되면 Future(43)

  // 부분 함수: 패턴과 일치하지 않으면 매칭 오류 발생
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1   => 43
    case 8   => 56
    case 100 => 999
  }

  // 조금 더 고급 내용
  trait HigherKindedType[F[_]]
  trait SequenceChecker[F[_]] {
    def isSequential: Boolean
  }

  val listChecker = new SequenceChecker[List] {
    override def isSequential: Boolean = true
  }

  def main(args: Array[String]): Unit = {}
}
