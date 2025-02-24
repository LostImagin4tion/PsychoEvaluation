pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "PsychoEvaluation"

// ==== MULTIPLATFORM ====

// Core
include(":multiplatform:core:utils")
include(":multiplatform:core:httpClient")
include(":multiplatform:core:networkApi:authorization")
include(":multiplatform:core:networkApi:registration")
include(":multiplatform:core:networkApi:statistics")

// Common
include(":multiplatform:common")

// Feature
include(":multiplatform:feature")

// ==== ANDROID ====

// Entries
include(":android:entry:app")

// Core
include(":android:core:di:api")
include(":android:core:di:impl")

include(":android:core:dataStorage:api")
include(":android:core:dataStorage:impl")

include(":android:core:deviceApi:api")
include(":android:core:deviceApi:usbDeviceApi:api")
include(":android:core:deviceApi:usbDeviceApi:impl")
include(":android:core:deviceApi:bleDeviceApi:api")
include(":android:core:deviceApi:bleDeviceApi:impl")

include(":android:core:dataAnalysis:airplaneGame:api")
include(":android:core:dataAnalysis:airplaneGame:impl")

include(":android:core:utils")

// Common
include(":android:common:designSystem")

include(":android:common:interactors:usbDeviceInteractor:api")
include(":android:common:interactors:usbDeviceInteractor:impl")

include(":android:common:interactors:bleDeviceInteractor:api")
include(":android:common:interactors:bleDeviceInteractor:impl")

include(":android:common:interactors:settingsInteractor:api")
include(":android:common:interactors:settingsInteractor:impl")

include(":android:common:interactors:networkApi:authorization:api")
include(":android:common:interactors:networkApi:authorization:impl")

include(":android:common:interactors:networkApi:registration:api")
include(":android:common:interactors:networkApi:registration:impl")

include(":android:common:interactors:networkApi:statistics:api")
include(":android:common:interactors:networkApi:statistics:impl")

// Features
include(":android:feature:navigation:api")
include(":android:feature:navigation:impl")

include(":android:feature:authorization:api")
include(":android:feature:authorization:impl")

include(":android:feature:registration:api")
include(":android:feature:registration:impl")

include(":android:feature:userProfile:api")
include(":android:feature:userProfile:impl")

include(":android:feature:settings:api")
include(":android:feature:settings:impl")

include(":android:feature:bluetoothDeviceManager:api")
include(":android:feature:bluetoothDeviceManager:impl")

include(":android:feature:trainingPreparing:api")
include(":android:feature:trainingPreparing:impl")

include(":android:feature:trainingsList:api")
include(":android:feature:trainingsList:impl")

include(":android:feature:trainings:debugTraining:api")
include(":android:feature:trainings:debugTraining:impl")

include(":android:feature:trainings:airplaneGame:api")
include(":android:feature:trainings:airplaneGame:impl")

include(":android:feature:trainings:stopwatchGame:api")
include(":android:feature:trainings:stopwatchGame:impl")

include(":android:feature:trainings:clocksGame:api")
include(":android:feature:trainings:clocksGame:impl")

include(":android:feature:statistics:api")
include(":android:feature:statistics:impl")
