# Technologies

This is the list of libraries we use in project with litte explanations.

* **Kotlin**. Don't write code in Java!!! Many other technologies in the project are tied specifically to Kotlin. https://kotlinlang.org/
* **Coroutines** for asynchronous work. https://kotlinlang.org/docs/coroutines-guide.html
* **Kotlin Multiplatform** helps us not to write platform-independent code 2 times for Android and iOS. https://kotlinlang.org/docs/multiplatform.html
* **Jetpack Compose** for writing UI. https://developer.android.com/develop/ui/compose
* **Redux** is a cool multiplatform MVI implementation that fits well with Jetpack Compose. https://reduxkotlin.org/
* **Dagger** and **Kotlin-inject** for dependency injection. Don't use Hilt!!! https://dagger.dev/
* **Ktor** is a multiplatform HTTP client from Jetbrains. Don't use Retrofit!!! https://ktor.io/
* **KorGE** is a simple multiplatform game engine written entirely in Kotlin. https://korge.org/

This is only the main part of the dependencies, everything can be found in the file`buildSrc/src/main/kotlin/ru/miem/psychoEvaluation/consts/Dependencies.kt`
