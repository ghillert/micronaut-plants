# Plants Demo Micronaut

## How to Run

```bash
./mvnw mn:run
```

And then in a separate terminal window:

```bash
open http://localhost:8080/hello
```
`
Build the native image:

```bash
./mvnw package -Dpackaging=native-image
```

Run the native app:

```bash
./target/micronaut-plants
```

Shrink the native app size using [UPX](https://medium.com/graalvm/compressed-graalvm-native-images-4d233766a214):

```bash
upx -7 -k target/micronaut-plants
```
