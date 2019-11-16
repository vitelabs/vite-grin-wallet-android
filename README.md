# Vite_GrinWallet Android


## Requirements
- Ubuntu 18
* rust 1.34+ (use [rustup]((https://www.rustup.rs/))- i.e. `curl https://sh.rustup.rs -sSf | sh; source $HOME/.cargo/env`)
  * if rust is already installed, you can simply update version with `rustup update`
* NDK20

- create standalone toolchain
```
${NDK_HOME}/build/tools/make_standalone_toolchain.py --api 26 --arch arm64 --install-dir ${STANDALONE_NDK}/arm64
${NDK_HOME}/build/tools/make_standalone_toolchain.py --api 26 --arch arm --install-dir ${STANDALONE_NDK}/arm
${NDK_HOME}/build/tools/make_standalone_toolchain.py --api 26 --arch x86 --install-dir ${STANDALONE_NDK}/x86
```

- export to env

```
export RUST_BACKTRACE=full
export PATH=${NDK_HOME}/toolchains/llvm/prebuilt/linux-x86_64/bin:$PATH
export ${STANDALONE_NDK}/arm/bin
export ${STANDALONE_NDK}/arm64/bin
export ${STANDALONE_NDK}/x86/bin
```

- create catgo config

```
touch cargo-config.toml
```

- add to cargo-config.toml
```
[target.aarch64-linux-android]
ar = "${STANDALONE_NDK}arm64/bin/aarch64-linux-android-ar"
linker = "${STANDALONE_NDK}arm64/bin/aarch64-linux-android-clang"

[target.armv7-linux-androideabi]
ar = "${STANDALONE_NDK}/arm/bin/arm-linux-androideabi-ar"
linker = "${STANDALONE_NDK}/arm/bin/arm-linux-androideabi-clang"

[target.i686-linux-android]
ar = "${STANDALONE_NDK}/x86/bin/i686-linux-android-ar"
linker = "${STANDALONE_NDK}/x86/bin/i686-linux-android-clang"
```
```
cp cargo-config.toml ~/.cargo/config
```

- rust add target

```
rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android
```

- install requirements
```sh
apt install build-essential cmake git libgit2-dev clang libncurses5-dev libncursesw5-dev zlib1g-dev pkg-config libssl-dev llvm
```



## Build 
```
git clone https://github.com/vitelabs/Vite_GrinWallet-android.git
cd Vite_GrinWallet-android
cd rust

#64 bit
cargo build --target aarch64-linux-android --release

#32 bit
CC=arm-linux-androideabi-clang cargo build --target armv7-linux-androideabi --release
```

## Thanks
- https://github.com/cyclefortytwo/ironbelly
- https://github.com/vitelabs/Vite_GrinWallet
- https://github.com/mimblewimble/grin
- https://github.com/mimblewimble/grin-wallet


