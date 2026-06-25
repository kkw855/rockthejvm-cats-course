# CLAUDE.md

이 파일은 Claude Code(claude.ai/code)가 이 저장소에서 작업할 때 참고하는 가이드입니다.

## 프로젝트 개요

Rock The JVM Cats 강좌 저장소입니다. Scala 3로 Cats 타입 클래스 개념을 학습하는 프로젝트이며, 강좌 섹션이 추가될 때마다 번호가 붙은 `partN` 패키지 아래에 파일이 추가됩니다.

## 기술 스택

- Scala 3.8.4
- sbt 2.0.0
- cats-core 2.13.0 (유일한 의존성)

## 주요 명령어

```bash
# 컴파일
sbt compile

# 특정 오브젝트 실행 (main 메서드가 있어야 함)
sbt "runMain part1recap.Essentials"
sbt "runMain playground.Playground"

# sbt 쉘 시작 (반복 실행 시 더 빠름)
sbt

# sbt 쉘 내부에서
run           # 단일 main 클래스 실행
runMain <fqn> # 특정 main 클래스 실행
compile
test
```

## 코드 구조

소스는 `src/main/scala/` 아래에 강좌 파트별로 구성됩니다:

- `part1recap/` — Scala 3 핵심 복습 (OOP, FP, 고차 킨드 타입, Future)
- `playground/` — 빠른 실험을 위한 스크래치 공간

각 강좌 파일은 보통 `main` 메서드를 가진 독립적인 `object`입니다. 새 파트는 동일한 `partNxxx/` 패키지 컨벤션을 따릅니다.

## 참고 사항

- 테스트 소스가 아직 없으므로 `sbt test`는 컴파일만 하고 실행되는 것이 없습니다.
- `playground`는 `cats.Eval`을 사용하는 스크래치 공간으로, 자유롭게 덮어써도 됩니다.
