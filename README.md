# Vite_GrinWallet Android


## Requirements
* Ubuntu 18
* rust 1.34+ (use [rustup]((https://www.rustup.rs/))- i.e. `curl https://sh.rustup.rs -sSf | sh; source $HOME/.cargo/env`)
  * if rust is already installed, you can simply update version with `rustup update`
* clang
* ncurses and libs (ncurses, ncursesw5)
* zlib libs (zlib1g-dev or zlib-devel)
* pkg-config
* libssl-dev
* linux-headers (reported needed on Alpine linux)
* llvm

For Debian-based distributions (Debian, Ubuntu, Mint, etc), all in one line (except Rust):

```sh
apt install build-essential cmake git libgit2-dev clang libncurses5-dev libncursesw5-dev zlib1g-dev pkg-config libssl-dev llvm
```

## Build 
```
git clone https://github.com/vitelabs/Vite_GrinWallet-android.git
cd Vite_GrinWallet-android
cargo build --target aarch64-linux-android --release
```

## Thanks
https://github.com/cyclefortytwo/ironbelly
https://github.com/vitelabs/Vite_GrinWallet
https://github.com/mimblewimble/grin
https://github.com/mimblewimble/grin-wallet
