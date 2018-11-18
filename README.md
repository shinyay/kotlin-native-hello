# Hello Kotlin/Native

## 概要 / 説明
Kotlin は Java や Scala 同様に JVM 言語として設計されています。そのため、実行するには 仮想マシン(JVM) が必要となります。
ところが、この Kotolin/Native は 仮想マシンを必要せずに動作するように ネイティブバイナリとしてコンパイルする技術です。

## 前提 / 環境
### ランタイムバージョン
- Kotlin : 1.3.0

### 開発環境
- OS : Mac
- IDE : IntelliJ IDEA
- Build : Gradle

## 手順 / 解説
### 1. Kotlin/Native プロジェクトの作成
<img width="954" alt="image.png" src="https://qiita-image-store.s3.amazonaws.com/0/127983/95cf33fc-2c6d-7348-611a-58f4191e26e6.png">

新規プロジェクトを作成する時に選択するプロジェクトタイプとして、Kotlin カテゴリの中に含まれている、Kotlin/Native を選択します。
プロジェクトの構成はデフォルトのままで問題なしです。

### 2. プロジェクト構成
<img width="1280" alt="image.png" src="https://qiita-image-store.s3.amazonaws.com/0/127983/b48a20fc-b09b-7962-9c61-bdb3584997a8.png">

ソースコードディレクトリが自動で作られています。環境の OS が MacOS だからか、ディレクトリ名が **macosMain** となっています。

### 3. ソースコード
#### build.gradle
```gradle
plugins {
    id 'kotlin-multiplatform' version '1.3.0'
}
repositories {
    mavenCentral()
}
kotlin {
    targets {
        fromPreset(presets.macosX64, 'macos')

        configure([macos]) {
            compilations.main.outputKinds('EXECUTABLE')
            compilations.main.entryPoint = 'sample.main'
        }
    }
    sourceSets {
        macosMain {
        }
        macosTest {
        }
    }
}

task runProgram {
    def buildType = 'release'
    dependsOn "link${buildType.capitalize()}ExecutableMacos"
    doLast {
        def programFile = kotlin.targets.macos.compilations.main.getBinary('EXECUTABLE', buildType)
        exec {
            executable programFile
            args ''
        }
    }
}
```

#### アプリケーション
```kotlin
package sample

fun hello(): String = "Hello, Kotlin/Native!\n"

fun main(args: Array<String>) {
    println(hello())
    platform.posix.system("date")
    platform.posix.system("ls -la")
}
```

基本に忠実に HelloWorld の標準出力と、外部コマンドの実行を入れてみました。

### 4. ビルド
<img width="358" alt="image.png" src="https://qiita-image-store.s3.amazonaws.com/0/127983/b544078c-cd13-ae74-a5c7-a9e6c52316ca.png">

Gradle タスクの **build** を実行します。

<img width="1209" alt="image.png" src="https://qiita-image-store.s3.amazonaws.com/0/127983/fe9b9e42-4484-2fa5-1a8b-010b83e291cc.png">

ネイティブバイナリを生成するためか、少し時間がかかるようです。

### 5. 実行
**build/bin/macos/main/release/executable** ディレクトリの配下にネイティブバイナリが生成されます。
このバイナリファイルは、MacOS上で直接どこでも実行できます。

```bash
build/bin/macos/main/release/executable $ ./HelloKotlinNative.kexe
Hello, Kotlin/Native!

2018年 11月18日 日曜日 21時01分51秒 JST
total 1984
drwxr-xr-x  3 shinyay  staff       96 11 18 20:36 .
drwxr-xr-x  3 shinyay  staff       96 11 18 20:35 ..
-rwxr-xr-x  1 shinyay  staff  1013336 11 18 20:36 HelloKotlinNative.kexe
```

