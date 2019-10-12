# Vite_GrinWallet Android


## Requirements
* Ubuntu 18
* rust 1.34+ (use [rustup]((https://www.rustup.rs/))- i.e. `curl https://sh.rustup.rs -sSf | sh; source $HOME/.cargo/env`)
  * if rust is already installed, you can simply update version with `rustup update`
* NDK20

```
export RUST_BACKTRACE=full
export PATH=$NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64/bin:$PATH
```

```
rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android
```

```sh
apt install build-essential cmake git libgit2-dev clang libncurses5-dev libncursesw5-dev zlib1g-dev pkg-config libssl-dev llvm
```

## Build 
```
git clone https://github.com/vitelabs/Vite_GrinWallet-android.git
cd Vite_GrinWallet-android
cd rust
cargo build --target aarch64-linux-android --release
```

## Thanks
https://github.com/cyclefortytwo/ironbelly
https://github.com/vitelabs/Vite_GrinWallet
https://github.com/mimblewimble/grin
https://github.com/mimblewimble/grin-wallet
