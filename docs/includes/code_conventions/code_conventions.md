# Code conventions

## Agreements
* We write comments in **English**
* We divide modules into **api** **impl** (if possible) to separate the methods outwards and their implementation respectively
* Naming modules via **camelCase**. For example - `buildSrc`
* Documentation in api modules is very welcome, as is logging
* Package naming as follows: 

    For Android modules: `ru.miem.psychoEvaluation.{feature/common/core}.{name of module}.{api/impl}`  
    For multiplatform modules: `ru.miem.psychoEvaluation.multiplatform.{feature/common/core}.{name of module}.{api/impl}`  

  * api/impl - optional, if present
  * Does not apply to `entry/*` modules, as it contains the base path `odoo.miem.android`
  * For example: `ru.miem.psychoEvaluation.core.di.impl`
