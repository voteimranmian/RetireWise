# RetireWise

An AI-powered Canadian retirement planning coach, built with Kotlin
Multiplatform and Compose Multiplatform (shared code for Android and iOS).

This repository is in **Phase 0 (Foundation)** of the build sequence
described in [docs/RELEASE_PLAN.md](docs/RELEASE_PLAN.md). Phase 0
intentionally contains no authentication, AI integration, financial
calculations, or backend services — only the project scaffolding, a shared
Compose UI screen, dependency injection, navigation shell, and build/test/CI
pipeline. Read [CLAUDE.md](CLAUDE.md) and the documents it links to before
changing code.

## Repository layout

- `apps/androidApp` — Android application module.
- `apps/iosApp` — iOS application (SwiftUI host embedding the shared Compose UI).
- `shared/core`, `shared/design_system`, `shared/navigation` — real, working
  Kotlin Multiplatform modules.
- `shared/*` (all other directories), `backend/*` — placeholder directories
  documenting future modules; each has a README stating the phase in which
  it will be implemented. They are not wired into the Gradle build yet.
- `docs/` — product, architecture, and engineering specifications.
- `docs/ADR/` — architecture decision records.

## Prerequisites

- macOS
- [Homebrew](https://brew.sh)
- JDK 17 (`brew install openjdk@17`)
- Android command line tools (`brew install --cask android-commandlinetools`,
  or equivalent), with `platforms;android-36` and `build-tools;36.0.0`
  installed via `sdkmanager`
- Full Xcode (from the Mac App Store), with the iOS and iOS Simulator
  platforms installed (Xcode > Settings > Components, or
  `xcodebuild -downloadPlatform iOS`)
- [XcodeGen](https://github.com/yonaskolb/XcodeGen) (`brew install xcodegen`)
  — used to generate `apps/iosApp/iosApp.xcodeproj` from
  `apps/iosApp/project.yml`

Set `ANDROID_HOME` and ensure `JAVA_HOME` points at a JDK 17 installation
before running Gradle, e.g. in `~/.zshrc`:

```sh
export ANDROID_HOME="/opt/homebrew/share/android-commandlinetools"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$ANDROID_HOME/platform-tools:$PATH"
```

Copy [.env.example](.env.example) to `.env` if you need to reserve local
configuration; no variables in it are read by any code yet (see the
comments in that file for which phase introduces each one).

## Android

```sh
./gradlew ktlintCheck
./gradlew test
./gradlew :apps:androidApp:assembleDebug
```

The debug APK is written to
`apps/androidApp/build/outputs/apk/debug/`.

## iOS

The Xcode project is generated from `apps/iosApp/project.yml` and is not
committed; regenerate it after checkout or whenever `project.yml` changes:

```sh
cd apps/iosApp
xcodegen generate
open iosApp.xcodeproj
```

Building from Xcode (or `xcodebuild`) runs a script phase that invokes
`./gradlew :shared:navigation:embedAndSignAppleFrameworkForXcode` to build
and embed the shared Kotlin framework automatically — no separate manual
Gradle step is required first.

To build headlessly for the simulator:

```sh
cd apps/iosApp
xcodebuild -project iosApp.xcodeproj -scheme iosApp \
  -sdk iphonesimulator -destination "generic/platform=iOS Simulator" \
  -configuration Debug build
```

## Formatting and linting

```sh
./gradlew ktlintFormat   # auto-fix
./gradlew ktlintCheck    # verify only
```

## Continuous integration

See [.github/workflows/ci.yml](.github/workflows/ci.yml) — runs ktlint,
unit tests, and Android/iOS builds on every push and pull request against
`main`.
