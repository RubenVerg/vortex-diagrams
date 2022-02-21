# Vortex Diagrams

[this video](https://www.youtube.com/watch?v=6ZrO90AI0c8) for more info

## Setup

Install SBT

```shell
git clone https://github.com/rubenverg/vortex-diagrams.git
cd vortex-diagrams
sbt "~fast"
```

## Development

Entrypoint is in the `VortexDiagrams` object (`main` function), most logic happens in `draw`

If you want to add another coloring algorithm, edit the `algos` map at the top of `VortexDiagrams` with a new entry, then add it (with the same value) to the HTML files.

Simple algorithms like By Length can use `PureAlgorithm` which takes a function `(a: Int, b: Int, modulo: Int, multiplier: Int) => String` that should return a valid HTML Canvas `strokeStyle`/`fillStyle` (`a` and `b` are the two numbers)

More complex algorithms should create a new object inheriting from `Algorithm` with a function `apply` (same as the pure one) and `reset` that is meant for some kind of cleanup (called each time a redraw is triggered)

`Predef` defines some common utilities such as `debug`, `log`, `info`, `warn` and `error` for writing to the JS console, converting between `java.time.Instant`s and JS `Date`s, and the standard `Intl` instances that should be used. To use them, make your object/class/whatever extend `Predef`. Feel free to add stuff you may need.

`Edges` contains utilities for computing edges

For testing things, please add things to the `com.rubenverg.vortex.debug` module, like this: `@JSExportTopLevel("whatever", "com.rubenverg.vortex.debug")`. You may leave these in production. On the JS side, you can use `const { whatever } = await import('./target/scala-3.1.1/vortex-diagrams-fastopt/com.rubenverg.vortex.debug.js')`

Note that `debug` in `Predef` is automatically removed at compile time during production builds, for logging stuff that may be useful use `info` or `log`.

`Arrows` has code for drawing arrows in the canvas. It kinda sucks, please change it ._.