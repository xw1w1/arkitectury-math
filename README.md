# arkitectury-math

Lightweight geometry and color primitives extracted from **Arkitectury**, shared across the Arkitectury engine and **Kanvas UI** (`io.kanvas.math`).

## Types

| Class | Package | Description |
| --- | --- | --- |
| `Rect` | `io.kanvas.math` | 2D float rectangle (`x`, `y`, `width`, `height`), min/max edge mutators, containment, overlap, normalized coordinates, closest-point queries. |
| `Bounds` | `io.kanvas.math` | 3D axis-aligned bounding box (`center`, `extents`), zero-allocation Arvo affine `transform()`, `encapsulate()`, `expand()`, `intersects()`, `sqrDistance()`. |
| `Color` | `io.kanvas.math` | Normalized float RGBA (`0..1`), `toARGB()`, `fromRgba32()`, `toRgba32()`, named color presets. |
| `Color32` | `io.kanvas.math` | Compact 8-bit-per-channel RGBA (`byte r, g, b, a`) with `Color` and packed ARGB conversions. |

## Requirements

- **Java 21+**
- **Dependencies:** `org.joml:joml:1.10.7`.

## Installation (Gradle / JitPack)

Add the JitPack repository and `arkitectury-math` dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.xw1w1:arkitectury-math:1.0.0")
}
```

## Building & Publishing Locally

```bash
./gradlew test
./gradlew publishToMavenLocal
```

Local `publishToMavenLocal` coordinates:

```kotlin
dependencies {
    implementation("io.kanvas:arkitectury-math:1.0.0")
}
```

## Usage Examples

```java
import io.kanvas.math.Bounds;
import io.kanvas.math.Color;
import io.kanvas.math.Color32;
import io.kanvas.math.Rect;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

Rect viewport = Rect.minMax(0f, 0f, 1280f, 720f);
Vector2f center = viewport.getCenter(); // (640, 360)

Bounds box = new Bounds(new Vector3f(0f, 1f, 0f), new Vector3f(2f, 2f, 2f));
Bounds rotated = box.transform(new Matrix4f().rotateY((float) Math.PI / 4f));

Color accent = new Color(0.30f, 0.55f, 0.90f, 1.0f);
int packedArgb = accent.toARGB();
Color32 byteColor = new Color32(accent);
```
