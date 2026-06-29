//noinspection TypeAnnotation,ScalaWeakerAccess
package part1intro

object TCVariance {

  import cats.instances.int._ // Eq[Int] 타입 클래스 인스턴스
  import cats.instances.option._ // Eq[Int]를 바탕으로 Eq[Option[Int]] 타입 클래스 인스턴스를 만든다
  import cats.syntax.eq._

  val aComparison = Option(2) === Option(3)
  // val anInvalidComparison = Some(2) === None // Eq[Some[Int]]를 찾을 수 없어서 컴파일되지 않는다.
  // ===는 양쪽 타입이 같고, 그 타입에 대한 Eq 인스턴스가 스코프에 있어야 사용할 수 있다.

  // variance: 제네릭 타입 안에서 상속 관계가 어떻게 전파되는지를 나타낸다.
  class Animal
  class Cat extends Animal

  // 공변 타입: 하위 타입 관계가 제네릭 타입에도 같은 방향으로 전파된다.
  class Cage[+T]
  val cage: Cage[Animal] = new Cage[Cat] // Cat <: Animal 이므로 Cage[Cat] <: Cage[Animal]

  // 반공변 타입: 하위 타입 관계가 제네릭 타입에서는 반대 방향으로 전파된다.
  class Vet[-T]
  val vet: Vet[Cat] = new Vet[Animal] // Cat <: Animal 이면 Vet[Animal] <: Vet[Cat]

  // 경험칙: "T를 가지고 있는 타입"은 공변, "T에 대해 동작하는 타입"은 반공변으로 생각할 수 있다.
  // variance는 타입 클래스 인스턴스를 암시적으로 찾는 방식에도 영향을 준다.

  // 반공변 타입 클래스
  trait SoundMaker[-T]
  implicit object AnimalSoundMaker extends SoundMaker[Animal]
  def makeSound[T](implicit soundMaker: SoundMaker[T]): Unit = println("wow") // 구현 자체는 중요하지 않다.
  makeSound[Animal] // 가능 - 위에서 타입 클래스 인스턴스를 정의했다.
  makeSound[Cat] // 가능 - Cat 전용 인스턴스가 없으면 Animal 인스턴스를 사용할 수 있다.
  // 규칙 1: 반공변 타입 클래스는 정확한 타입의 인스턴스가 없을 때 상위 타입 인스턴스를 사용할 수 있다.

  // 이 규칙은 하위 타입에도 영향을 준다.
  implicit object OptionSoundMaker extends SoundMaker[Option[Int]]
  makeSound[Option[Int]]
  makeSound[Some[Int]]

  // 공변 타입 클래스
  trait AnimalShow[+T] {
    def show: String
  }
  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    def show: String = "animals everywhere"
  }
  implicit object CatShow extends AnimalShow[Cat] {
    def show: String = "so many cats!"
  }

  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show
  // 규칙 2: 공변 타입 클래스는 해당 타입에 더 구체적인 타입 클래스 인스턴스가 있으면 그것을 사용하려 한다.
  // 하지만 일반적인 인스턴스와 구체적인 인스턴스가 함께 있으면 컴파일러가 어떤 것을 써야 할지 애매해질 수 있다.

  // 규칙 3: 반공변과 공변의 장점을 동시에 모두 얻을 수는 없다.
  // Cats는 이런 암시적 탐색의 혼란을 줄이기 위해 타입 클래스를 보통 불변(invariant)으로 정의한다.
  Option(2) === Option.empty[Int]

  def main(args: Array[String]): Unit = {
    println(organizeShow[Cat]) // 가능 - 컴파일러가 CatShow를 암시적으로 전달한다.
    // println(organizeShow[Animal]) // 컴파일되지 않는다 - 암시적 값이 애매하다.
  }
}
