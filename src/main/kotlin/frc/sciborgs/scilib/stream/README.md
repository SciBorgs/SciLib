# Stream

Stream library for easy composition and logging, inspired by [StuyLib](https://github.com/StuyPulse/StuyLib).

## Summary

`Stream` is a functional interface that supports `map`, a function to apply a `(Double) -> Double` function to its output, basic operations with other Streams, and scalar transformations. `Stream` is lazy, meaning it only runs calculations when `get` is called.

`SendableStream` is a class that implements `Stream` and `Sendable`, allowing for its use in networktables. All mapping and operations done to `SendableStream` return a new `SendableStream` with a modified `initSendable` method to allow for logging intermediary values. It can be obtained from a `Stream` using the `log` method.

`Derivative` and `Integral` are classes that impleent `Double -> Double` and can be invoked on streams to calculate their respective operations. They also provide `Stream.differentiate()` and `Stream.integrate()` shorthand for mapping a stream with a new operation.

`Stream` also has various methods reimplementing WPILib's `LinearFilter` static generator methods, providing shorthand for filtering streams.

## Future

- Add a non lazy stream that is accessible via `Stream.poll(rate)`
